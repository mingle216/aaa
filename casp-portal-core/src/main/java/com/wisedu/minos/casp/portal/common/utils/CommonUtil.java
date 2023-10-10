package com.wisedu.minos.casp.portal.common.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.common.base.CaseFormat;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.config.ApplicationContextUtil;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.wisedu.minos.casp.portal.common.constant.Global.BATCH_PRE_COMMIT;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;


/**
 * 公共操作类
 *
 * @author zhangjian 0116211
 * @create 2019-10-30
 **/

public class CommonUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

	// 低级密码强度
	public static final int SIMPLE_PWD_TYPE = 1;
	// 中级密码强度
	public static final int MIDDLE_PWD_TYPE = 2;
	// 高级密码强度
	public static final int HIGH_PWD_TYPE = 3;
	// 默认密码长度
	private static final int DEFAULT_PWD_LENGTH = 8;
	private static final char[] SIMPLE_PWD = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] MIDDLE_PWD = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final char[] HIGH_PWD = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-',
			'+', '=', '_', ';', ':', ',', '?', '<', '>'};

	private static final String SPLIT = "-";

	private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]*");

	private static String driver;

	@Value("${spring.datasource.driver-class-name}")
	public void setDriver(String driver) {
		CommonUtil.driver = driver;
	}

	/**
	 * 获取数据库时间
	 *
	 * @return
	 */
	public static Date getSystemDate() {

		if ("com.mysql.cj.jdbc.Driver".equals(driver)) {

			LOGGER.debug("获取mysql数据库时间");
			return getMysqlDate();

		} else if ("oracle.jdbc.driver.OracleDriver".equals(driver)) {

			LOGGER.debug("获取oracle数据库时间");
			return getOracleDate();

		} else if ("org.postgresql.Driver".equals(driver)) {

			LOGGER.debug("获取pgsql数据库时间");
			return getPgSqlDate();
		}

		return new Date();
	}

	/**
	 * @return Date
	 * @Author jcx
	 * @Description 获取MySql数据库时间
	 * @Date 14:55 2020/7/30
	 **/
	private static Date getMysqlDate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		return jdbcTemplate.queryForObject("SELECT current_timestamp()", Date.class);
	}

	/**
	 * @return Date
	 * @Author jcx
	 * @Description 获取Oracle数据库时间
	 * @Date 14:55 2020/7/30
	 **/
	private static Date getOracleDate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		return jdbcTemplate.queryForObject("SELECT sysdate FROM dual", Date.class);
	}

	/**
	 * @return Date
	 * @Author jcx
	 * @Description 获取PgSql数据库时间
	 * @Date 14:55 2020/7/30
	 **/
	private static Date getPgSqlDate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
		return jdbcTemplate.queryForObject("SELECT current_timestamp", Date.class);
	}

	private static DataSource getDataSource() {
		return ApplicationContextUtil.get(DataSource.class);
	}

	/**
	 * @return String
	 * @Author jcx
	 * @Description 新文件名获取
	 * @Date 14:57 2020/7/30
	 * @Param fileType:
	 **/
	public static String getPathFileName(String fileType) {
		// 获取当前时间的毫秒表示，作为新文件名
		long countNumber = System.currentTimeMillis();
		String random = getIntCode(4);

		return countNumber + random + "." + fileType;
	}

	/**
	 * 判断map是否为空
	 *
	 * @param map
	 * @return
	 */
	public static <T, E> boolean mapIsNotNull(Map<T, E> map) {
		return map != null && map.size() > 0;
	}
	
	/***
	 * @Author jcx
	 * @Description 判断map中key是否存在，且不为null和空
	 * @Date 17:28 2021/3/23
	 * @return boolean
	 **/
	public static <T, E> boolean mapKeyIsNotNull(Map<T, E> map,T t) {
		return null!=map&&map.size() > 0 &&map.containsKey(t) &&null!=map.get(t)&&!"".equals(String.valueOf(map.get(t)));
	}

	/**
	 * 根据参数长度，产生数字验证码
	 *
	 * @param length
	 * @return
	 */
	private static String getIntCode(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append((random.nextInt() % 10 + 10) % 10);
		}
		return sb.toString();
	}

	/**
	 * 判断传入的邮箱格式是否正确，由于邮箱可以为空，因此
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmailFormat(String email) {
		if (Strings.isEmpty(email)) {
			return true;
		}
		return email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+");
	}

	/**
	 * 判断传入的手机号是否正确，由于手机号可以为空，因此为空也返回true
	 *
	 * @param phone
	 * @return
	 */
	public static boolean isPhoneFormat(String phone) {
		if (Strings.isEmpty(phone)) {
			return true;
		}
		return phone.matches("^(\\d{1,7})?(\\d{1,11})$");
	}


	/**
	 * 检查查询的关键字中是否含有特殊字符，如果有则抛出异常
	 *
	 * @param val
	 */
	public static void checkQuerySpecialWord(Object val) {
		if (val != null && StringUtils.isNotEmpty(val.toString())) {
			if (val.toString().indexOf("_") != -1 || val.toString().indexOf("%") != -1) {
				throw new MinosException("搜索关键字中含有_或%特殊字符,不允许搜索");
			}
		}
	}

	public static <T> void addBetweenCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			// between是多值
			String[] vals = val.toString().split(",");
			if (vals.length != 2) {
				return;
			}
			outWrapper.or(w -> {
				for (String column : columns) {
					w.between(column, vals[0], vals[1]).or();
				}
				return w;
			});
		}
	}

	public static <T> void addBetweenCondition(QueryWrapper<T> outWrapper, List<String> columns, String val, Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (!Strings.isEmpty(val)) {
			// between是多值
			String[] vals = val.split(",");
			if (vals.length != 2) {
				return;
			}
			outWrapper.or(w -> {
				for (String column : columns) {
					String c = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
					Optional<Field> o = Arrays.stream(fields).filter(e -> e.getName().equals(c)).findAny();
					if (o.isPresent()) {
						//如果是日期类型的需要转换下类型
						if (o.get().getType() == LocalDateTime.class) {
							w.between(column,
									CommonUtil.getLocalDateTime(vals[0], Global.DATETIME_FORMAT),
									CommonUtil.getLocalDateTime(vals[1], Global.DATETIME_FORMAT)).or();
						} else {
							w.between(column, vals[0], vals[1]).or();
						}
					} else {
						w.between(column, vals[0], vals[1]).or();
					}
				}
				return w;
			});
		}
	}

	public static <T> void addInCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			// in是传入多值，因此还是需要使用逗号分隔
			String[] vals = val.toString().split(",");
			outWrapper.or(w -> {
				for (String column : columns) {
					w.in(column, vals).or();
				}
				return w;
			});
		}
	}

	public static <T> void addNotInCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && StringUtils.isNotEmpty(val.toString())) {
			// not in是传入多值，因此还是需要使用逗号分隔
			String[] vals = val.toString().split(",");
			outWrapper.or(w -> {
				for (String column : columns) {
					w.notIn(column, vals).or();
				}
				return w;
			});
		}
	}

	public static <T> void addLtCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.lt(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addLtCondition(QueryWrapper<T> outWrapper, List<String> columns, LocalDateTime[] vals) {
		outWrapper.or(w -> {
			for (String column : columns) {
				for (LocalDateTime val : vals) {
					w.lt(column, val).or();
				}
			}
			return w;
		});
	}

	public static <T> void addGtCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.gt(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addLeCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.le(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addLeCondition(QueryWrapper<T> outWrapper, List<String> columns, LocalDateTime[] vals) {
		outWrapper.or(w -> {
			for (String column : columns) {
				for (LocalDateTime val : vals) {
					w.le(column, val).or();
				}
			}
			return w;
		});
	}

	public static <T> void addGeCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.ge(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addGeCondition(QueryWrapper<T> outWrapper, List<String> columns, LocalDateTime[] vals) {
		outWrapper.or(w -> {
			for (String column : columns) {
				for (LocalDateTime val : vals) {
					w.ge(column, val).or();
				}
			}
			return w;
		});
	}

	public static <T> void addEqCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.eq(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addEqCondition(QueryWrapper<T> outWrapper, List<String> columns, List<String> val, Class<T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (String item : val) {
			if (Strings.isNotEmpty(item)) {
				outWrapper.or(w -> {
					for (String column : columns) {
						String c = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column);
						boolean m = false;
						for ( Field field : fields ) {
							if(c.equals(field.getName())){
								//不同类型进行赋值
								if(field.getType() == Long.class){
									w.eq(column, Long.parseLong(item)).or();
								}else if(field.getType() == Integer.class){
									w.eq(column, Integer.parseInt(item)).or();
								}else{
									w.eq(column, item).or();
								}
								m = true;
								break;
							}
						}
						if(!m){
							w.eq(column, item).or();
						}
					}
					return w;
				});
			}
		}
	}

	public static <T> void addNotCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			outWrapper.or(w -> {
				for (String column : columns) {
					w.ne(column, val).or();
				}
				return w;
			});
		}
	}

	public static <T> void addlikeCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			// 检查查询的关键字是否含有特殊字符
			CommonUtil.checkQuerySpecialWord(val);
			outWrapper.or(w -> {
				for (String column : columns) {
					if (column.startsWith("[") && column.endsWith("]")) {
						String finalColumn = column.substring(1, column.length() - 1);
						w.eq(finalColumn, StringUtils.trim(val.toString())).or();
					} else {
						w.like(column, StringUtils.trim(val.toString())).or();
					}
				}
				return w;
			});
		}
	}

	/**
	 * 拼接搜索的查询条件
	 *
	 * @param outWrapper
	 * @param columns
	 * @param <T>
	 */
	public static <T> void addIlikeCondition(QueryWrapper<T> outWrapper, List<String> columns, Object val) {
		if (val != null && Strings.isNotEmpty(val.toString())) {
			// 检查查询的关键字是否含有特殊字符
			CommonUtil.checkQuerySpecialWord(val);
			outWrapper.or(w -> {
				for (String column : columns) {
					if (column.startsWith("[") && column.endsWith("]")) {
						String finalColumn = column.substring(1, column.length() - 1);
						w.eq(finalColumn, StringUtils.trim(val.toString())).or();
					} else if (val instanceof String) {
						w.apply("lower(" + column + ") like {0} ", "%" + StringUtils.trim(val.toString()).toLowerCase() + "%").or();
					} else {
						w.eq(column, val).or();
					}
				}
				return w;
			});
		}
	}

	/**
	 * 将搜索条件中的列转化为数据库列名
	 *
	 * @param field2ColumnMap
	 * @param fields
	 * @return
	 */
	public static List<String> getColumnNamesBySearchField(Map<String, String> field2ColumnMap, String[] fields) {
		List<String> result = new ArrayList<>();
		if (fields == null || fields.length == 0) {
			return result;
		}
		for (String field : fields) {
			Boolean isEquals = false;
			if (field.startsWith("[") && field.endsWith("]")) {
				field = field.substring(1, field.length() - 1);
				isEquals = true;
			}
			String columnName = field2ColumnMap.get(field);
			if (org.apache.logging.log4j.util.Strings.isBlank(columnName)) {
				LOGGER.error(field + "对应的属性不存在数据库列，忽略查询");
				continue;
			}
			if (isEquals) {
				columnName = "[" + columnName + "]";
			}
			result.add(columnName);
		}
		return result;
	}

	public static Object convertValue2DBType(List<String> columns, Map<String, Class<?>> fieldType2ColumnMap, Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Object obj = value;
		for (String column : columns) {
			if (value instanceof String) {
				if (column.startsWith("[") && column.endsWith("]")) {
					column = column.substring(1, column.length() - 1);
				}
				Class<?> aClass = fieldType2ColumnMap.get(column);
				if (aClass == Integer.class || aClass == Long.class) {
					Class[] paramsClasses={value.toString().getClass()};
					Object[] params = {value.toString()};
					Constructor c = aClass.getConstructor(paramsClasses);
					obj = c.newInstance(params);
				}
			}
		}
		return obj;
	}


	/**
	 * 根据周期获取对应的corn表达式
	 *
	 * @param period
	 * @param unit   1 分钟 2 小时 3 天
	 * @return
	 */
	public static String getCornExpre(int period, String unit) {
		String cron = "";
		switch (unit) {
			case "1":
				cron = "0 0/" + period + " * * * ?";
				break;
			case "2":
				cron = "0 0 */" + period + " * * ?";
				break;
			case "3":
				cron = "0 0 0 1/" + period + " * ?";
				break;
			default:
				throw new MinosException(unit + "不正确，必须为 1,2,3");
		}
		return cron;
	}

	/**
	 * 将一个大的List转化为按照1000个一个List进行分组，用于数据库的in操作
	 *
	 * @param entities
	 * @param <T>
	 * @return
	 */
	public static <T> List<List<T>> getListArray(List<T> entities) {
		List<List<T>> result = new ArrayList<>();

		if (CollectionUtils.isEmpty(entities)) {
			return result;
		}
		int size = entities.size();
		List<T> item = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			if (i != 0 && i % BATCH_PRE_COMMIT == 0) {
				result.add(item);
				item = new ArrayList<>();
			}
			item.add(entities.get(i));

		}
		if (!CollectionUtils.isEmpty(item)) {
			result.add(item);
		}
		return result;
	}

	/**
	 * 使用批量模式保存大量数据
	 *
	 * @param mapper
	 * @param entities
	 * @param session
	 * @param <R>
	 */
	public static <R> int batchSaveDatas(BaseMapper mapper, List<R> entities, SqlSession session) {
		int num = 0;
		if (CollectionUtils.isEmpty(entities)) {
			return num;
		}
		try {
			for (int i = 0; i < entities.size(); i++) {
				num += mapper.insert(entities.get(i));
				if (i % BATCH_PRE_COMMIT == BATCH_PRE_COMMIT - 1 || i == entities.size() - 1) {
					session.commit();
					session.clearCache();
				}
			}
		} catch (Exception e) {
			LOGGER.error("批量保存数据失败", e);
			session.rollback();
		} finally {
			session.close();
		}
		return num;
	}


	public static int getIntValue(String str) {
		if (Strings.isBlank(str)) {
			return 0;
		}
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			LOGGER.error(str + "不是Int类型", e);
			return 0;
		}
	}

	public static String getStringValue(String str) {
		return str == null ? "" : str;
	}

	/**
	 * 从后往前拼接名称
	 *
	 * @param names
	 * @return
	 */
	public static String getDisplayName(List<String> names) {
		if (CollectionUtils.isEmpty(names)) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		int size = names.size();
		for (int i = size - 1; i >= 0; i--) {
			builder.append(names.get(i));
			if (i != 0) {
				builder.append(SPLIT);
			}
		}
		return builder.toString();
	}


	/**
	 * 将数据库模型对象中的属性名称与数据库的列名的映射关系返回，key为模型属性，value为数据库列名
	 *
	 * @param clz
	 * @return
	 */
	public static Map<String, String> getField2ColumnMap(Class clz) {
		Map<String, String> result = new HashMap<>();
		Field[] fields = clz.getDeclaredFields();

		if (fields == null || fields.length == 0) {
			return result;
		}
		for (Field field : fields) {
			TableId tableId = field.getAnnotation(TableId.class);
			if (tableId != null) {
				result.put(field.getName(), tableId.value());
			}
			TableField tableField = field.getAnnotation(TableField.class);
			if (tableField != null) {
				result.put(field.getName(), tableField.value());
			}
		}
		return result;

	}

	public static Map<String, Class<?>> getFieldType2ColumnMap(Class clz) {
		Map<String, Class<?>> result = new HashMap<>();
		Field[] fields = clz.getDeclaredFields();

		if (fields == null || fields.length == 0) {
			return result;
		}
		for (Field field : fields) {
			Class<?> type = field.getType();
			TableId tableId = field.getAnnotation(TableId.class);
			if (tableId != null) {
				result.put(tableId.value(), type);
			}
			TableField tableField = field.getAnnotation(TableField.class);
			if (tableField != null) {
				result.put(tableField.value(), type);
			}
		}
		return result;

	}

	/**
	 * 判断所有的参数是否都是空的，只要有一个空，就返回true
	 *
	 * @param strs
	 * @return
	 */
	public static boolean isEmpty(String... strs) {
		if (strs == null || strs.length == 0) {
			return true;
		}
		for (String str : strs) {
			if (Strings.isEmpty(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断所有的参数是否都是空的，如果都是空的则返回true
	 *
	 * @param strs
	 * @return
	 */
	public static boolean isAllEmpty(String... strs) {
		if (strs == null || strs.length == 0) {
			return true;
		}
		for (String str : strs) {
			if (!Strings.isEmpty(str)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 将多个字段拼接在一起
	 *
	 * @param pre
	 * @param keys
	 * @return
	 */
	public static String getAppendStr(String pre, String... keys) {
		if (keys == null || keys.length == 0) {
			return pre;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(pre);
		for (String key : keys) {
			builder.append(key);
		}
		return builder.toString();
	}

	/**
	 * 构造缓存的Key
	 *
	 * @param pre
	 * @param keys
	 * @return
	 */
	public static String getCacheKey(String pre, String... keys) {
		if (keys == null || keys.length == 0) {
			return pre;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(pre);
		for (String key : keys) {
			builder.append(":").append(key);
		}
		return builder.toString();
	}

	/**
	 * 生成密码的接口,生成的密码长度为8位
	 *
	 * @param type 1 只数字 2 字母+数字 3 字母 + 数字 + 特殊字符
	 * @return
	 */
	public static String generatePwd(int type) {
		String pwd = "";
		switch (type) {
			case SIMPLE_PWD_TYPE:
				pwd = generatePwd(DEFAULT_PWD_LENGTH, SIMPLE_PWD);
				break;
			case MIDDLE_PWD_TYPE:
				pwd = generatePwd(DEFAULT_PWD_LENGTH, MIDDLE_PWD);
				break;
			case HIGH_PWD_TYPE:
				pwd = generatePwd(DEFAULT_PWD_LENGTH, HIGH_PWD);
				break;
			default:
				pwd = generatePwd(DEFAULT_PWD_LENGTH, SIMPLE_PWD);
				break;
		}
		return pwd;
	}

	/**
	 * 根据需要的长度以及对应的密码字符集生成对应的密码
	 *
	 * @param length
	 * @param chars
	 * @return
	 */
	private static String generatePwd(int length, char[] chars) {
		int maxNum = chars.length;
		int i;
		int count = 0;
		StringBuilder pwd = new StringBuilder("");
		Random r = new Random();
		while (count < length) {
			i = Math.abs(r.nextInt(maxNum));
			if (i >= 0 && i < maxNum) {
				pwd.append(chars[i]);
				count++;
			}
		}
		return pwd.toString();
	}

	/**
	 * 获取今天的23:59:59
	 *
	 * @return
	 */
	public static LocalDateTime getTodayExpire() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);

		return getLocalDateTime(calendar);
	}

	public static LocalDateTime getLocalDateTime(Calendar calendar) {
		return LocalDateTime.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
	}

	public static LocalDateTime getLocalDateTime(String date, String dateTimeFormat) {
		if (Strings.isBlank(date)) {
			return null;
		}
		try {
			SimpleDateFormat format = new SimpleDateFormat(dateTimeFormat);
			Date d = format.parse(date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(d);
			return getLocalDateTime(calendar);
		} catch (Exception e) {
			LOGGER.error(date + "日期格式不正确", e);
			return null;
		}
	}

	/**
	 * 将日期格式转换为到天
	 *
	 * @param dateTime
	 * @return
	 */
	public static String toStringDate(LocalDateTime dateTime) {
		if (dateTime == null) {
			return "";
		}
		return dateTime.format(ISO_LOCAL_DATE);
	}

	public static String toStringDate(LocalDateTime dateTime, String format) {
		if (dateTime == null) {
			return "";
		}
		return dateTime.format(DateTimeFormatter.ofPattern(format));
	}

	public static String toStringDate(Date date, String format) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}


	/**
	 * 判断是否为数字格式不限制位数
	 *
	 * @param o 待校验参数
	 * @return 如果全为数字，返回true；否则，返回false
	 */
	public static boolean isNumber(Object o) {
		return NUMBER_PATTERN.matcher(String.valueOf(o)).matches();
	}

	/**
	 * 判断是否含有特殊字符
	 *
	 * @param str
	 * @return true为包含，false为不包含
	 */
	public static boolean isSpecialChar(String str) {
		String regEx = "[- `~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/**
	 * 将map转换为一个对象
	 *
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T mapToObject(Map<String, String> map, Class<T> beanClass) throws Exception {
		if (map == null) {
			return null;
		}
		T obj = beanClass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			Method setter = property.getWriteMethod();
			if (setter != null) {
				setter.invoke(obj, map.get(property.getName()));
			}
		}
		return obj;
	}

	public static int countIntervalSeconds(int time, int unit) {
		int intervalSeconds = 0;
		if (unit == 0) {
			intervalSeconds = time;
		} else if (unit == 1) {          //分
			intervalSeconds = time * 60;
		} else if (unit == 2) {         //时 * 60 * 60
			intervalSeconds = time * 3600;
		} else if (unit == 3) {         //天 * 60 * 60 * 24
			intervalSeconds = time * 86400;
		}
		return intervalSeconds;
	}

	/**
	 * 判断客户端类型是否为移动端
	 *
	 * @param request
	 * @return
	 */
	public static boolean isMobileDevice(HttpServletRequest request) {
		try {
			if (request == null) {
				return false;
			}
			String userAgent = request.getHeader("User-Agent");
			if (userAgent != null) {
				return userAgent.matches(".*((Mobile)|(Android)|(iPhone)|(iPad)|(iOS)|(iPh)).*");
			}
		} catch (Exception e) {
			// ignore;
			LOGGER.info("判断客户端类型是否为移动端失败:", e);
		}
		return false;
	}
	/***
	 * isMobileDevice
	 * 判断当前是否是移动设备，线程安全，<p/>
	 *
	 * @param
	 * @throws
	 * @return boolean
	 * @date 2021/3/12 14:37
	 * @author jszhang
	 */
	public static boolean isMobileDevice() {
		return isMobileDevice(
			((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
	}

	/***
	 * likeKeyWordEncode
	 * like包含的关键字编码，同时mapper里的like要添加 escape '/'<p/>
	 *
	 * @param keyworld
	 * @throws
	 * @return java.lang.String
	 * @date 2020/7/31 14:14
	 */
	public static String encodeLikeKeyWordEncode(String keyworld) {
		if (org.apache.commons.lang.StringUtils.isBlank(keyworld)) {
			return null;
		} else {
			return keyworld.replaceAll("/", "//")
					.replaceAll("%", "/%")
					.replaceAll("_", "/_")
					;
		}
	}
}
