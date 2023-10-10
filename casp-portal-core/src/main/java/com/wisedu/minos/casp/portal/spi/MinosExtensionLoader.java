package com.wisedu.minos.casp.portal.spi;

import com.wisedu.minos.casp.portal.spi.annotation.MinosSPI;
import org.apache.dubbo.common.utils.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;
/**
 * 扫描当前classpath下所有Minos的插件，并加载到当前系统中
 */
public class MinosExtensionLoader<T> {

    private static final Logger logger = LogManager.getLogger(MinosExtensionLoader.class);

    private static final String MINOS_DIRECTORY = "META-INF/minos/";

    // 产品自身带的配置，优先级低于自定义的插件
    private static final String MINOS_DIRECTORY_INNER = MINOS_DIRECTORY + "inner/";

    private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*");

    private static final ConcurrentMap<Class<?>, MinosExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();

    private static final ConcurrentMap<Class<?>, Object> EXTENSION_INSTANCES = new ConcurrentHashMap<>();

    private final Class<?> type;

    private final ConcurrentMap<Class<?>, String> cachedNames = new ConcurrentHashMap<>();

    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    private final ConcurrentMap<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    private String cachedDefaultName;

    private Set<Class<?>> cachedWrapperClasses;

    private Map<String, IllegalStateException> exceptions = new ConcurrentHashMap<>();

    private MinosExtensionLoader(Class<?> type) {
        this.type = type;
    }

    private static <T> boolean withExtensionAnnotation(Class<T> type) {
        return type.isAnnotationPresent(MinosSPI.class);
    }

    private static ClassLoader findClassLoader() {
        return ClassUtils.getClassLoader(MinosExtensionLoader.class);
    }

    public static <T> MinosExtensionLoader<T> getExtensionLoader(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("MinosExtension type == null");
        }
        if (!type.isInterface()) {
            throw new IllegalArgumentException("MinosExtension type (" + type + ") is not an interface!");
        }
        if (!withExtensionAnnotation(type)) {
            throw new IllegalArgumentException("MinosExtension type (" + type +
                    ") is not an extension, because it is NOT annotated with @" + MinosSPI.class.getSimpleName() + "!");
        }

        MinosExtensionLoader<T> loader = (MinosExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
            EXTENSION_LOADERS.putIfAbsent(type, new MinosExtensionLoader<T>(type));
            loader = (MinosExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

    /**
     * 获取当前系统中该接口注册的所有插件名称
     * @return
     */
    public Set<String> getSupportedExtensions() {
        Map<String, Class<?>> clazzes = getExtensionClasses();
        return Collections.unmodifiableSet(new TreeSet<>(clazzes.keySet()));
    }

    /**
     * 获取当前系统中该接口的所有实现类对象
     * @return
     */
    public Map<String ,T> getSupportedExtensionInstances() {
        Map<String , T> instances = new HashMap<>();
        Set<String> supportedExtensions = getSupportedExtensions();
        if (CollectionUtils.isNotEmpty(supportedExtensions)) {
            for (String name : supportedExtensions) {
                instances.put(name,getExtension(name));
            }
        }
        return instances;
    }

    /**
     * 根据某个注册的插件名称获取对应的实现类
     * @param name
     * @return
     */
    public T getSupportedExtensionInstanceByName(String name){
        Map<String,T> instances = getSupportedExtensionInstances();
        if (instances == null || instances.isEmpty()){
            return null;
        }
        return instances.get(name);
    }

    private Holder<Object> getOrCreateHolder(String name) {
        Holder<Object> holder = cachedInstances.get(name);
        if (holder == null) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            holder = cachedInstances.get(name);
        }
        return holder;
    }

    /**
     * Find the extension with the given name. If the specified name is not found, then {@link IllegalStateException}
     * will be thrown.
     */
    @SuppressWarnings("unchecked")
    private T getExtension(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Extension name == null");
        }
        if ("true".equals(name)) {
            return getDefaultExtension();
        }
        final Holder<Object> holder = getOrCreateHolder(name);
        Object instance = holder.get();
        if (instance == null) {
            synchronized (holder) {
                instance = holder.get();
                if (instance == null) {
                    instance = createExtension(name);
                    holder.set(instance);
                }
            }
        }
        return (T) instance;
    }


    /**
     * Return default extension, return <code>null</code> if it's not configured.
     */
    private T getDefaultExtension() {
        getExtensionClasses();
        if (StringUtils.isEmpty(cachedDefaultName) || "true".equals(cachedDefaultName)) {
            return null;
        }
        return getExtension(cachedDefaultName);
    }


    private IllegalStateException findException(String name) {
        for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
            if (entry.getKey().toLowerCase().contains(name.toLowerCase())) {
                return entry.getValue();
            }
        }
        StringBuilder buf = new StringBuilder("No such extension " + type.getName() + " by name " + name);


        int i = 1;
        for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
            if (i == 1) {
                buf.append(", possible causes: ");
            }

            buf.append("\r\n(");
            buf.append(i++);
            buf.append(") ");
            buf.append(entry.getKey());
            buf.append(":\r\n");
            buf.append(StringUtils.toString(entry.getValue()));
        }
        return new IllegalStateException(buf.toString());
    }

    @SuppressWarnings("unchecked")
    private T createExtension(String name) {
        Class<?> clazz = getExtensionClasses().get(name);
        if (clazz == null) {
            throw findException(name);
        }
        try {
            T instance = (T) EXTENSION_INSTANCES.get(clazz);
            if (instance == null) {
                EXTENSION_INSTANCES.putIfAbsent(clazz, clazz.newInstance());
                instance = (T) EXTENSION_INSTANCES.get(clazz);
            }
            return instance;
        } catch (Exception t) {
            throw new IllegalStateException("Extension instance (name: " + name + ", class: " +
                    type + ") couldn't be instantiated: " + t.getMessage(), t);
        }
    }

    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

    /**
     * synchronized in getExtensionClasses
     * */
    private Map<String, Class<?>> loadExtensionClasses() {
        cacheDefaultExtensionName();
        Map<String, Class<?>> extensionClasses = new HashMap<>();
        loadDirectory(extensionClasses,MINOS_DIRECTORY_INNER , type.getName());
        loadDirectory(extensionClasses, MINOS_DIRECTORY, type.getName());
        return extensionClasses;
    }

    /**
     * extract and cache default extension name if exists
     */
    private void cacheDefaultExtensionName() {
        final MinosSPI defaultAnnotation = type.getAnnotation(MinosSPI.class);
        if (defaultAnnotation == null) {
            return;
        }

        String value = defaultAnnotation.value();
        if ((value = value.trim()).length() > 0) {
            String[] names = NAME_SEPARATOR.split(value);
            if (names.length > 1) {
                throw new IllegalStateException("More than 1 default extension name on extension " + type.getName()
                        + ": " + Arrays.toString(names));
            }
            if (names.length == 1) {
                cachedDefaultName = names[0];
            }
        }
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type) {
        loadDirectory(extensionClasses, dir, type, false);
    }

    private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type, boolean extensionLoaderClassLoaderFirst) {
        String fileName = dir + type;
        try {
            Enumeration<java.net.URL> urls = null;
            ClassLoader classLoader = findClassLoader();

            // try to load from ExtensionLoader's ClassLoader first
            if (extensionLoaderClassLoaderFirst) {
                ClassLoader extensionLoaderClassLoader = MinosExtensionLoader.class.getClassLoader();
                if (ClassLoader.getSystemClassLoader() != extensionLoaderClassLoader) {
                    urls = extensionLoaderClassLoader.getResources(fileName);
                }
            }

            if(urls == null || !urls.hasMoreElements()) {
                if (classLoader != null) {
                    urls = classLoader.getResources(fileName);
                } else {
                    urls = ClassLoader.getSystemResources(fileName);
                }
            }

            if (urls != null) {
                while (urls.hasMoreElements()) {
                    java.net.URL resourceURL = urls.nextElement();
                    loadResource(extensionClasses, classLoader, resourceURL);
                }
            }
        } catch (Exception t) {
            logger.error("Exception occurred when loading extension class (interface: " +
                    type + ", description file: " + fileName + ").", t);
        }
    }

    private void loadResource(Map<String, Class<?>> extensionClasses, ClassLoader classLoader, java.net.URL resourceURL) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceURL.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    final int ci = line.indexOf('#');
                    if (ci >= 0) {
                        line = line.substring(0, ci);
                    }
                    line = line.trim();
                    if (line.length() > 0) {
                        try {
                            String name = null;
                            int i = line.indexOf('=');
                            if (i > 0) {
                                name = line.substring(0, i).trim();
                                line = line.substring(i + 1).trim();
                            }
                            if (line.length() > 0) {
                                loadClass(extensionClasses, resourceURL, Class.forName(line, true, classLoader), name);
                            }
                        } catch (Exception t) {
                            IllegalStateException e = new IllegalStateException("Failed to load extension class (interface: " + type + ", class line: " + line + ") in " + resourceURL + ", cause: " + t.getMessage(), t);
                            exceptions.put(line, e);
                        }
                    }
                }
            }
        } catch (Exception t) {
            logger.error("Exception occurred when loading extension class (interface: " +
                    type + ", class file: " + resourceURL + ") in " + resourceURL, t);
        }
    }

    private void loadClass(Map<String, Class<?>> extensionClasses, java.net.URL resourceURL, Class<?> clazz, String name) throws NoSuchMethodException {
        if (!type.isAssignableFrom(clazz)) {
            throw new IllegalStateException("Error occurred when loading extension class (interface: " +
                    type + ", class line: " + clazz.getName() + "), class "
                    + clazz.getName() + " is not subtype of interface.");
        }
        if (isWrapperClass(clazz)) {
            cacheWrapperClass(clazz);
        } else {
            clazz.getConstructor();

            String[] names = NAME_SEPARATOR.split(name);
            if (ArrayUtils.isNotEmpty(names)) {
                for (String n : names) {
                    cacheName(clazz, n);
                    saveInExtensionClass(extensionClasses, clazz, n);
                }
            }
        }
    }

    /**
     * cache name
     */
    private void cacheName(Class<?> clazz, String name) {
        if (!cachedNames.containsKey(clazz)) {
            cachedNames.put(clazz, name);
        }
    }

    /**
     * put clazz in extensionClasses
     */
    private void saveInExtensionClass(Map<String, Class<?>> extensionClasses, Class<?> clazz, String name) {
        Class<?> c = extensionClasses.get(name);
        if (c == null) {
            extensionClasses.put(name, clazz);
        } else if (c != clazz) {
            String duplicateMsg = "Duplicate extension " + type.getName() + " name " + name + " on " + c.getName() + " and " + clazz.getName();
            logger.warn(duplicateMsg);
            // 但出现重复名称时使用外部组件覆盖内部组件
            extensionClasses.put(name, clazz);
        }
    }


    /**
     * cache wrapper class
     * <p>
     * like: ProtocolFilterWrapper, ProtocolListenerWrapper
     */
    private void cacheWrapperClass(Class<?> clazz) {
        if (cachedWrapperClasses == null) {
            cachedWrapperClasses = new ConcurrentHashSet<>();
        }
        cachedWrapperClasses.add(clazz);
    }

    /**
     * test if clazz is a wrapper class
     * <p>
     * which has Constructor with given class type as its only argument
     */
    private boolean isWrapperClass(Class<?> clazz) {
        try {
            clazz.getConstructor(type);
            return true;
        } catch (NoSuchMethodException e) {
            logger.debug("isWrapperClass方法getConstructor错误,返回false,错误信息{}",e);
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[" + type.getName() + "]";
    }

}
