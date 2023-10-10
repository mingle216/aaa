package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 功能描述：文件相关工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FileUtil
 * @Author: jcx
 * @Date: 2020/7/23
 */
public class FileUtil {

    private static final Logger log = LogManager.getLogger(FileUtil.class);

    /**
     * 系统文件分隔符key
     */
    public static final String SYS_FILE_SEPARATOR = "file.separator";

    private static final String FILE_SEPARATOR = System.getProperty(SYS_FILE_SEPARATOR);

    private FileUtil () {
        //过sonar检查
    }

    /**
     * @return File
     * @Author jcx
     * @Description MultipartFile转File
     * @Date 18:21 2020/7/23
     * @Param file:
     **/
    public static File multipartFileToFile (MultipartFile file) {

        File toFile = null;
        if ( file != null && file.getSize() > 0 && StringUtils.isNotBlank(file.getOriginalFilename()) ) {

            InputStream ins = null;
            OutputStream os = null;
            try {
                ins = file.getInputStream();
                toFile = new File(file.getOriginalFilename());

                os = new FileOutputStream(toFile);
                int bytesRead = 0;
                byte[] buffer = new byte[ 8192 ];
                while ( (bytesRead = ins.read(buffer, 0, 8192)) != - 1 ) {
                    os.write(buffer, 0, bytesRead);
                }
            } catch ( Exception e ) {

                log.debug("MultipartFile转File失败：" + e);
            } finally {
                try {
                    if ( ins != null ) {
                        ins.close();
                    }
                    if ( os != null ) {
                        os.close();
                    }
                } catch ( IOException e ) {
                    log.error("关闭流异常：" + e);
                }
            }
        }

        return toFile;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 根据文件名取得文件扩展名
     * @Date 18:22 2020/7/23
     * @Param fileName: 文件名称
     **/
    public static String getFileExtension (String fileName) {

        if ( fileName == null ) {
            return null;
        }

        int lastPosDot = - 1;
        String ret = null;

        if ( (lastPosDot = fileName.lastIndexOf(".")) > 0 ) {
            ret = fileName.substring(lastPosDot + 1);
        }

        return ret;
    }

    /**
     * @return String
     * @Author jcx
     * @Description 取得去除扩展名的真实文件名
     * @Date 18:23 2020/7/23
     * @Param fileName:文件名称
     **/
    public static String getFileName (String fileName) {
        if ( fileName == null ) {
            return null;
        }
        String ret = fileName.substring(0, fileName.lastIndexOf("."));
        return ret;
    }

    /**
     * 允许上传文件集合
     */
    protected static String[] ALLOW_FORMAT = {
            ".jpg", ".jpeg", ".png", ".xls", ".xlsx", ".ppt", ".pptx",
            ".doc", ".docx", ".pdf", ".zip"
    };

    /**
     * @return boolean
     * @Author jcx
     * @Description 校验后缀名是否合法
     * @Date 18:19 2020/7/23
     * @Param fileName: 文件名称
     * @Param allowFormat: 允许上传的文件后缀名集合
     **/
    public static boolean checkSuffix (String fileName, String[] allowFormat) {
        if ( StringUtils.isBlank(fileName) )
            return false;
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        Set<String> setSuffix = new HashSet<>(Arrays.asList(allowFormat));
        //toLowerCase 转换成小写
        return setSuffix.contains(suffix.toLowerCase());
    }

    /**
     * 判断文件大小
     *
     * @param len  文件
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize (Long len, int size, String unit) {
        double fileSize = 0;
        if ( "B".equals(unit.toUpperCase()) ) {
            fileSize = ( double ) len;
        } else if ( "K".equals(unit.toUpperCase()) ) {
            fileSize = ( double ) len / 1024;
        } else if ( "M".equals(unit.toUpperCase()) ) {
            fileSize = ( double ) len / 1048576;
        } else if ( "G".equals(unit.toUpperCase()) ) {
            fileSize = ( double ) len / 1073741824;
        }
        return fileSize > size;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 根据文件流下载文件
     * @Date 18:24 2020/7/23
     * @Param request:
     * @Param response:
     * @Param fileByte: 文件流
     * @Param fileName: 文件名称
     **/
    public static void downLoadFileByByte (HttpServletResponse response, byte[] fileByte, String fileName) {
        InputStream fileIn = null;
        OutputStream out = null;
        try {
            fileIn = new ByteArrayInputStream(fileByte);
            writeRespon(response, fileName, response.getOutputStream(), fileIn);
        } catch ( Exception e ) {
            log.debug("根据文件流下载文件失败：{}", e);
        } finally {
            try {
                if ( fileIn != null ) {
                    fileIn.close();
                }
                if ( out != null ) {
                    out.flush();
                    out.close();
                }
            } catch ( IOException e ) {
                log.debug("根据文件流下载文件失败：" + e);
            }
        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 下载网络文件
     * @Date 18:25 2020/7/23
     * @Param response:
     * @Param fileUrl: 远程URL
     * @Param fileName: 文件名称
     **/
    public static void download (HttpServletResponse response, String fileUrl, String fileName) throws IOException {
        BufferedInputStream dis = null;
        BufferedOutputStream fos = null;
        try {

            URL url = new URL(fileUrl);
            response.reset();//避免空行
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Content-Length", String.valueOf(url.openConnection().getContentLength()));

            dis = new BufferedInputStream(url.openStream());
            fos = new BufferedOutputStream(response.getOutputStream());

            byte[] buff = new byte[ 2048 ];
            int bytesRead;
            while ( - 1 != (bytesRead = dis.read(buff, 0, buff.length)) ) {
                fos.write(buff, 0, bytesRead);
            }
        } catch ( Exception e ) {
            log.error("下载网络文件失败：" + e);
        } finally {
            if ( dis != null )
                dis.close();
            if ( fos != null )
                fos.close();

        }
    }

    public static boolean downloadFileFromURL(String url, String dirLocation, String fileName) {
        boolean flag=true;
        try {
            URL httpUrl = new URL(url);
            File file = new File(dirLocation + fileName);
            file.createNewFile();
            FileUtils.copyURLToFile(httpUrl, file);
        } catch (Exception e) {
            flag=false;
            log.error("下载网络文件失败：" + e);
        }
        return flag;
    }

    /**
     * @return void
     * @Author jcx
     * @Description 下载相对路径下资源
     * @Date 18:26 2020/7/23
     * @Param response:
     * @Param path: 相对路径
     * @Param fileName: 文件名称
     **/
    public static void downTemplate (HttpServletResponse response, String path, String fileName) {
        /** 将文件名称进行编码 */
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + ";filename*=utf-8''"
                    + URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("content-type:octet-stream");
            /** 读取服务器端模板文件*/
            inputStream = FileUtil.class.getResourceAsStream(path);

            writeRespon(response, fileName, response.getOutputStream(), inputStream);

        } catch ( Exception e ) {
            log.debug("下载相对路径下资源发生了异常：" + e);
        } finally {
            try {
                if ( inputStream != null ) {
                    inputStream.close();
                }
                if ( outputStream != null ) {
                    outputStream.close();
                }
            } catch ( IOException e ) {
                log.debug("下载相对路径下资源发生了异常：" + e);
            }
        }
    }


    /**
     * @return void
     * @Author jcx
     * @Description 下载本地指定目录下的文件
     * @Date 11:15 2020/7/24
     * @Param response:
     * @Param filePath:指定目录
     * @Param fileName 文件名
     **/
    public static void downLocalFile (HttpServletResponse response, String filePath, String filePathName, String fileTrueName) {
        if ( ! filePath.endsWith("/") ) {
            filePath = filePath + "/";
        }
        File folder = new File(filePath);
        //如果目录不存在则直接抛异常
        if ( ! folder.isDirectory() ) {
            throw new BusinessException(GlobalEnum.DIRECTORY_ERROR.getCode(), GlobalEnum.DIRECTORY_ERROR.getMsg());
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            String fileName = filePathName.substring(filePathName.lastIndexOf("/") + 1, filePathName.length());
            String path = filePath + fileName;
            File file = new File(path);
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            writeRespon(response, fileTrueName, outputStream, inputStream);
        } catch ( Exception e ) {
            log.debug("下载本地指定目录下的文件发生异常" + e);
        } finally {
            try {
                if ( inputStream != null ) {
                    inputStream.close();
                }
                if ( outputStream != null ) {
                    outputStream.close();
                }
            } catch ( IOException e ) {
                log.debug("下载本地指定目录下的文件发生异常" + e);
            }
        }
    }

    /**
     * @return void
     * @Author jcx
     * @Description 统一以流的形式写出
     * @Date 15:18 2020/7/30
     * @Param response:
     * @Param fileName:
     * @Param outputStream:
     * @Param inputStream:
     **/
    private static void writeRespon (HttpServletResponse response, String fileName,
                                     OutputStream outputStream, InputStream inputStream) throws Exception {
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + ";filename*=utf-8''"
                + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream;charset=utf-8");
        byte[] outputByte = new byte[ 1024 ];
        int readTmp = 0;
        while ( (readTmp = inputStream.read(outputByte)) != - 1 ) {
            outputStream.write(outputByte, 0, readTmp); //并不是每次都能读到1024个字节，所有用readTmp作为每次读取数据的长度，否则会出现文件损坏的错误
        }
    }

    /**
     * @return byte
     * @Author
     * @Description 文件转换文件流
     * @Date 9:29 2020/7/24
     * @Param file:
     **/
    public static byte[] getFileToByte (File file) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(( int ) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[ bufSize ];
            int len;
            while ( - 1 != (len = in.read(buffer, 0, bufSize)) ) {
                bos.write(buffer, 0, len);
            }
            byte[] fileByte = bos.toByteArray();
            return fileByte;
        } catch ( Exception e ) {
            log.error(GlobalEnum.FILE_TO_BYTE_ERROR.getMsg() + e);
            throw new BusinessException(GlobalEnum.FILE_TO_BYTE_ERROR.getCode(), GlobalEnum.FILE_TO_BYTE_ERROR.getMsg());
        } finally {
            try {
                if ( in != null ) {
                    in.close();
                }
                if ( bos != null ) {
                    bos.close();
                }
            } catch ( Exception e ) {
                log.error(GlobalEnum.FILE_TO_BYTE_ERROR.getMsg() + e);
            }
        }
    }

    /***
     * @Author jcx
     * @Description 上传文件至服务器指定目录
     * @Date 10:02 2020/7/24
     * @Param file: 文件对象
     * @Param path: 文件存放的服务器指定目录
     * @return boolean
     **/
    public static boolean uploadFile (MultipartFile file, String fileName, String path) {
        if ( ! file.isEmpty() ) {
            try {
                //文件存放的服务器指定目录
                File folder = new File(path);
                //如果目录不存在则创建
                if ( ! folder.isDirectory() ) {
                    folder.mkdirs();
                }
                // getOriginalFilename()是包含源文件后缀的全名
                if ( ! path.endsWith("/") ) {
                    path = path + "/";
                }
                String filePath = path + fileName;
                File saveDir = new File(filePath);
                FileUtils.copyInputStreamToFile(file.getInputStream(), saveDir);
                return true;
            } catch ( Exception e ) {
                log.debug(GlobalEnum.UPLOAD_FILE_ERROR.getMsg() + e);
            }
        }
        return false;
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 上传文件至服务器指定目录
     * @Date 10:44 2020/7/24
     * @Param path: 文件存放的服务器指定目录
     * @Param fileName: 文件全名称，带后缀  eg:  abc.jpg
     * @Param bytes: 文件流
     * @Param isSourceName: 是否使用原来文件名
     **/
    public static boolean uploadFile (String path, String fileName, boolean isSourceName, byte[] bytes) {
        FileOutputStream downloadFile = null;
        try {
            //文件存放的服务器指定目录
            File folder = new File(path);
            //如果目录不存在则创建
            if ( ! folder.isDirectory() ) {
                folder.mkdirs();
            }
            if ( ! path.endsWith("/") ) {
                path = path + "/";
            }
            String name = "";
            if ( isSourceName ) {
                name = fileName;
            } else {
                name = CommonUtil.getPathFileName(getFileExtension(fileName));
            }
            String filePath = path + name;
            downloadFile = new FileOutputStream(filePath);
            downloadFile.write(bytes);
            return true;
        } catch ( Exception e ) {
            log.debug(GlobalEnum.UPLOAD_FILE_ERROR.getMsg() + e);
        } finally {
            if ( downloadFile != null ) {
                try {
                    downloadFile.flush();
                    downloadFile.close();
                } catch ( Exception e ) {
                    log.debug(GlobalEnum.UPLOAD_FILE_ERROR.getMsg() + e);
                }
            }
        }
        return false;
    }

    /**
     * Buffer的大小
     */
    private static Integer BUFFER_SIZE = 1024 * 1024 * 10;

    private static MessageDigest MD5 = null;

    static {
        try {
            MD5 = MessageDigest.getInstance("MD5");
        } catch ( NoSuchAlgorithmException ne ) {
            log.info("初始化MessageDigest错误---{}",ne);
        }
    }

    /**
     * 获取文件的md5
     *
     * @param file
     * @return
     */
    public static String fileMD5 (File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[ 8192 ];
            int length;
            while ( (length = fileInputStream.read(buffer)) != - 1 ) {
                MD5.update(buffer, 0, length);
            }
            return new BigInteger(1, MD5.digest()).toString(16);
        } catch ( IOException e ) {
            log.info("获取文件的md5发生错误--",e);
            return null;
        } finally {
            try {
                if ( fileInputStream != null )
                    fileInputStream.close();
            } catch ( IOException e ) {
                log.info("获取文件的md5发生错误--",e);
            }
        }
    }

    /**
     * 获取文件的行数
     *
     * @param file 统计的文件
     * @return 文件行数
     */
    public final static int countLines (File file) {
        try ( LineNumberReader rf = new LineNumberReader(new FileReader(file)) ) {
            long fileLength = file.length();
            rf.skip(fileLength);
            return rf.getLineNumber();
        } catch ( IOException e ) {
            log.info("获取文件的行数发生错误--",e);
        }
        return 0;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file 需要出来的文件
     * @return 包含所有行的list
     */
    public final static List<String> lines (File file) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file))
        ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                list.add(line);
            }
        } catch ( IOException e ) {
            log.info("以列表的方式获取文件的所有行发生错误--",e);
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的所有行
     *
     * @param file     需要处理的文件
     * @param encoding 指定读取文件的编码
     * @return 包含所有行的list
     */
    public final static List<String> lines (File file, String encoding) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))
        ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                list.add(line);
            }
        } catch ( IOException e ) {
            log.info("以列表的方式获取文件的所有行发生错误--",e);
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file  处理的文件
     * @param lines 需要读取的行数
     * @return 包含制定行的list
     */
    public final static List<String> lines (File file, int lines) {
        List<String> list = new ArrayList<>();
        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                list.add(line);
                if ( list.size() == lines ) {
                    break;
                }
            }
        } catch ( IOException e ) {
            log.info("以列表的方式获取文件的指定的行数数据发生错误--",e);
        }
        return list;
    }

    /**
     * 以列表的方式获取文件的指定的行数数据
     *
     * @param file     需要处理的函数
     * @param lines    需要处理的行还俗
     * @param encoding 指定读取文件的编码
     * @return 包含制定行的list
     */
    public final static List<String> lines (File file, int lines, String encoding) {
        List<String> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding))
        ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                list.add(line);
                if ( list.size() == lines ) {
                    break;
                }
            }
        } catch ( IOException e ) {
            log.info("以列表的方式获取文件的指定的行数数据发生错误--",e);
        }
        return list;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file 需要处理的函数
     * @param str  添加的子字符串
     * @return 是否成功
     */
    public final static boolean appendLine (File file, String str) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(FILE_SEPARATOR + str);
            return true;
        } catch ( IOException e ) {
            log.info("在文件末尾追加一行发生错误--",e);
        }
        return false;
    }

    /**
     * 在文件末尾追加一行
     *
     * @param file     需要处理的文件
     * @param str      添加的字符串
     * @param encoding 指定写入的编码
     * @return 是否成功
     */
    public final static boolean appendLine (File file, String str, String encoding) {
        String lineSeparator = System.getProperty("line.separator", "\n");
        try ( RandomAccessFile randomFile = new RandomAccessFile(file, "rw") ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write((lineSeparator + str).getBytes(encoding));
        } catch ( IOException e ) {
            log.info("在文件末尾追加一行发生错误--",e);
        }
        return false;
    }

    /**
     * 将字符串写入到文件中
     */
    public final static boolean write (File file, String str) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            randomFile.writeBytes(str);
            return true;
        } catch ( IOException e ) {
            log.info("将字符串写入到文件中发生错误--",e);
        }
        return false;
    }

    /**
     * 将字符串以追加的方式写入到文件中
     */
    public final static boolean writeAppend (File file, String str) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.writeBytes(str);
            return true;
        } catch ( IOException e ) {
            log.info("将字符串以追加的方式写入到文件中发生错误--",e);
        }
        return false;
    }

    /**
     * 将字符串以制定的编码写入到文件中
     */
    public final static boolean write (File file, String str, String encoding) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch ( IOException e ) {
            log.info("将字符串以制定的编码写入到文件中发生错误--",e);
        }
        return false;
    }

    /**
     * 将字符串以追加的方式以制定的编码写入到文件中
     */
    public final static boolean writeAppend (File file, String str, String encoding) {
        try (
                RandomAccessFile randomFile = new RandomAccessFile(file, "rw")
        ) {
            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(str.getBytes(encoding));
            return true;
        } catch ( IOException e ) {
            log.info("将字符串以追加的方式以制定的编码写入到文件中发生错误--",e);
        }
        return false;
    }

    /**
     * 快速清空一个超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean cleanFile (File file) {
        try (
                FileWriter fw = new FileWriter(file)
        ) {
            fw.write("");
            return true;
        } catch ( IOException e ) {
            log.info("快速清空一个超大的文件发生错误--",e);
        }
        return false;
    }

    /**
     * 获取文件的Mime类型
     *
     * @param file 需要处理的文件
     * @return 返回文件的mime类型
     * @throws IOException
     */
    public final static String mimeType (String file) throws IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(file);
    }

    /**
     * 获取文件的类型 只利用文件头做判断故不全
     *
     * @param file 需要处理的文件
     * @return 文件类型
     */
    public final static String fileType (File file) {
        return FileTypeUtil.getFileType(file);
    }

    /**
     * 获取文件最后的修改时间
     *
     * @param file 需要处理的文件
     * @return 返回文件的修改时间
     */
    public final static Date modifyTime (File file) {
        return new Date(file.lastModified());
    }


    /**
     * 复制文件
     *
     * @param resourcePath 源文件
     * @param targetPath   目标文件
     * @return 是否成功
     */
    public final static boolean copy (String resourcePath, String targetPath) {
        File file = null;
        try {
            file = new File(resourcePath);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            log.info("复制文件发生错误--",e);
        }
        return copy(file, targetPath);
    }

    /**
     * 复制文件
     * 通过该方式复制文件文件越大速度越是明显
     *
     * @param file       需要处理的文件
     * @param targetFile 目标文件
     * @return 是否成功
     */
    public final static boolean copy (File file, String targetFile) {
        boolean flag=true;
        FileChannel in=null;
        FileChannel out=null;
        try {
            File outFile=new File(targetFile.substring(0,targetFile.lastIndexOf("/")+1));
            if(!outFile.exists()){
                outFile.mkdirs();
            }
            FileInputStream fin = new FileInputStream(file) ;
            FileOutputStream fout = new FileOutputStream(new File(targetFile));
            in = fin.getChannel();
            out= fout.getChannel();
            //设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while ( in.read(buffer) != - 1 ) {
                //准备写入，防止其他读取，锁住文件
                buffer.flip();
                out.write(buffer);
                //准备读取。将缓冲区清理完毕，移动文件内部指针
                buffer.clear();
            }
        } catch ( IOException e ) {
            flag=false;
            log.info("复制文件发生错误--",e);
        }finally {
            try {
                if(null!=in){
                    in.close();
                }
                if(null!=out){
                    out.close();
                }
            } catch (IOException e) {
                flag=false;
                log.info("复制文件发生错误--",e);
            }
        }
        return flag;
    }
    public final static boolean copyByInputStream (InputStream in, String targetFile) {
        boolean flag=true;
        FileOutputStream fout = null;
        try{
            File outFile=new File(targetFile.substring(0,targetFile.lastIndexOf("/")+1));
            if(!outFile.exists()){
                outFile.mkdirs();
            }
            fout = new FileOutputStream(new File(targetFile));
            //设定缓冲区
            byte[] b = new byte[1024];
            while ( in.read(b) != - 1 ) {
                fout.write(b);
            }
        } catch ( IOException e ) {
            flag=false;
            log.info("复制文件发生错误--",e);
        }finally {
            try {
                if(null!=in){
                    in.close();
                }
                if(null!=fout){
                    fout.close();
                }
            } catch (Exception e) {
                flag=false;
                log.info("复制文件发生错误（finally）--",e);
            }
        }
        return flag;
    }

    /**
     * 创建多级目录
     *
     * @param paths 需要创建的目录
     * @return 是否成功
     */
    public final static boolean createPaths (String paths) {
        File dir = new File(paths);
        return ! dir.exists() && dir.mkdirs();
    }

    /**
     * 创建文件支持多级目录
     *
     * @param filePath 需要创建的文件
     * @return 是否成功
     */
    public final static boolean createFiles (String filePath) {
        File file = new File(filePath);
        if ( file.isDirectory() ) {
            if ( ! file.exists() ) {
                return file.mkdirs();
            }
        } else {
            File dir = file.getParentFile();
            if ( ! dir.exists() ) {
                if ( dir.mkdirs() ) {
                    try {
                        return file.createNewFile();
                    } catch ( IOException e ) {
                        log.info("创建文件支持多级目录发生错误--",e);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 删除一个文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteFile (File file) {
        return file.delete();
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 根据路径删除文件
     * @Date 17:36 2020/9/18
     * @Param path:
     **/
    public final static boolean deletePathFile (List<String> paths) {
        if( CollectionUtils.isEmpty(paths)){
            return false;
        }
        boolean flag=true;
        for(String path : paths){
            File file = new File(path);
            if ( file.exists() ) {
                boolean deleteFlag = file.delete();
                if(!deleteFlag){
                    flag=false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 删除一个目录
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteDir (File file) {
        List<File> files = listFileAll(file);
        if ( CheckUtil.valid(files) ) {
            for ( File f : files ) {
                if ( f.isDirectory() ) {
                    deleteDir(f);
                } else {
                    deleteFile(f);
                }
            }
        }
        return file.delete();
    }


    /**
     * 快速的删除超大的文件
     *
     * @param file 需要处理的文件
     * @return 是否成功
     */
    public final static boolean deleteBigFile (File file) {
        return cleanFile(file) && file.delete();
    }


    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public final static void copyDir (String filePath, String targetPath) {
        File file = new File(filePath);
        copyDir(file, targetPath);
    }

    /**
     * 复制目录
     *
     * @param filePath   需要处理的文件
     * @param targetPath 目标文件
     */
    public final static void copyDir (File filePath, String targetPath) {
        File targetFile = new File(targetPath);
        if ( ! targetFile.exists() ) {
            createPaths(targetPath);
        }
        File[] files = filePath.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                String path = file.getName();
                if ( file.isDirectory() ) {
                    copyDir(file, targetPath + "/" + path);
                } else {
                    copy(file, targetPath + "/" + path);
                }
            }
        }
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 包含所有文件的的list
     */
    public final static List<File> listFile (String path) {
        File file = new File(path);
        return listFile(file);
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path  需要处理的文件
     * @param child 是否罗列子文件
     * @return 包含所有文件的的list
     */
    public final static List<File> listFile (String path, boolean child) {
        return listFile(new File(path), child);
    }


    /**
     * 罗列指定路径下的全部文件
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public final static List<File> listFile (File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件
     *
     * @param path  指定的路径
     * @param child 是否罗列子目录
     * @return
     */
    public final static List<File> listFile (File path, boolean child) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( child && file.isDirectory() ) {
                    list.addAll(listFile(file));
                } else {
                    list.add(file);
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path 需要处理的文件
     * @return 返回文件列表
     */
    public final static List<File> listFileAll (File path) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                list.add(file);
                if ( file.isDirectory() ) {
                    list.addAll(listFileAll(file));
                }
            }
        }
        return list;
    }

    /**
     * 罗列指定路径下的全部文件包括文件夹
     *
     * @param path   需要处理的文件
     * @param filter 处理文件的filter
     * @return 返回文件列表
     */
    public final static List<File> listFileFilter (File path, FilenameFilter filter) {
        List<File> list = new ArrayList<>();
        File[] files = path.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    list.addAll(listFileFilter(file, filter));
                } else {
                    if ( filter.accept(file.getParentFile(), file.getName()) ) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 获取指定目录下的特点文件,通过后缀名过滤
     *
     * @param dirPath  需要处理的文件
     * @param postfixs 文件后缀
     * @return 返回文件列表
     */
    public final static List<File> listFileFilter (File dirPath, final String postfixs) {
        /*
        如果在当前目录中使用Filter讲只罗列当前目录下的文件不会罗列孙子目录下的文件
        FilenameFilter filefilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(postfixs);
            }
        };
        */
        List<File> list = new ArrayList<File>();
        File[] files = dirPath.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    list.addAll(listFileFilter(file, postfixs));
                } else {
                    String fileName = file.getName().toLowerCase();
                    if ( fileName.endsWith(postfixs.toLowerCase()) ) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 在指定的目录下搜寻文个文件
     *
     * @param dirPath  搜索的目录
     * @param fileName 搜索的文件名
     * @return 返回文件列表
     */
    public final static List<File> searchFile (File dirPath, String fileName) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    list.addAll(searchFile(file, fileName));
                } else {
                    String name = file.getName();
                    if ( name.equals(fileName) ) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查找符合正则表达式reg的的文件
     *
     * @param dirPath 搜索的目录
     * @param reg     正则表达式
     * @return 返回文件列表
     */
    public final static List<File> searchFileReg (File dirPath, String reg) {
        List<File> list = new ArrayList<>();
        File[] files = dirPath.listFiles();
        if ( CheckUtil.valid(files) ) {
            for ( File file : files ) {
                if ( file.isDirectory() ) {
                    list.addAll(searchFile(file, reg));
                } else {
                    String name = file.getName();
                    if ( RegUtil.isMatche(name, reg) ) {
                        list.add(file);
                    }
                }
            }
        }
        return list;
    }

    /**
     * @return InputStream
     * @Author jcx
     * @Description 获取网络图片流
     * @Date 10:17 2020/8/5
     * @Param url:
     **/
    public static InputStream getImageStream (String url) {
        try {
            HttpURLConnection connection = ( HttpURLConnection ) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if ( connection.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            }
        } catch ( IOException e ) {
            log.info("获取网络图片出现异常,图片路径为：{}，错误：{}",url,e);
        }
        return null;
    }

    /**
     * 递归创建文件夹
     *
     * @param path 由目录创建的file对象
     * @throws FileNotFoundException
     */
    public static void mkDir (File path) throws FileNotFoundException {
        if ( path.getParentFile() == null ) {
            path = path.getAbsoluteFile();
        }

        if ( path.getParentFile().exists() ) {
            modifyFileAuth(path);
            if ( ! path.exists() && ! path.mkdir() ) {
                throw new FileNotFoundException();
            }
        } else {
            mkDir(path.getParentFile());
            modifyFileAuth(path);
            if ( ! path.exists() && ! path.mkdir() ) {
                throw new FileNotFoundException();
            }
        }
    }

    /**
     * 修改文件权限，设置为可读写
     *
     * @param file
     */
    private static void modifyFileAuth (File file) {
        boolean ans = file.setExecutable(true, false);
        ans = file.setReadable(true, false) && ans;
        ans = file.setWritable(true, false) && ans;
        if ( log.isDebugEnabled() ) {
            log.debug("create file auth : {}", ans);
        }
    }

    /**
     * @return InputStream
     * @Author jcx
     * @Description 读取文件  返回输入流
     * @Date 16:15 2020/8/5
     * @Param fileName:
     **/
    public static InputStream getStreamByFileName (String fileName) throws IOException {
        if ( fileName == null ) {
            throw new IllegalArgumentException("fileName should not be null!");
        }

        if ( isAbsFile(fileName) ) {
            // 绝对路径
            Path path = Paths.get(fileName);
            return Files.newInputStream(path);
        } else if ( fileName.startsWith("~") ) {
            // 用户目录下的绝对路径文件
            fileName = parseHomeDir2AbsDir(fileName);
            return Files.newInputStream(Paths.get(fileName));
        } else { // 相对路径
            return FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
    }

    /**
     * @return boolean
     * @Author jcx
     * @Description 判断操作系统不同的操作符
     * @Date 16:30 2020/8/5
     * @Param fileName:
     **/
    public static boolean isAbsFile (String fileName) {
        if ( OsUtil.isWinOS() ) {
            // windows 操作系统时，绝对地址形如  c:\descktop
            return fileName.contains(":") || fileName.startsWith("\\");
        } else {
            // mac or linux
            return fileName.startsWith("/");
        }
    }

    /**
     * 将用户目录下地址~/xxx 转换为绝对地址
     *
     * @param path
     * @return
     */
    public static String parseHomeDir2AbsDir (String path) {
        String homeDir = System.getProperties().getProperty("user.home");
        return StringUtils.replace(path, "~", homeDir);
    }

    /**
     * 获取文件对应的魔数
     *
     * @param file
     * @return
     */
    public static String getMagicNum (String file) {
        try ( InputStream stream = FileUtil.getStreamByFileName(file) ) {

            byte[] b = new byte[ 28 ];
            stream.read(b, 0, 28);

            return bytesToHex(b);
        } catch ( IOException e ) {
            log.info("获取文件对应的魔数出现异常，错误：{}",e);
            return null;
        }
    }

    /**
     * 获取流文件对应的魔数
     *
     * @param inputStream
     * @return
     */
    public static String getMagicNum (ByteArrayInputStream inputStream) {
        byte[] bytes = new byte[ 28 ];
        inputStream.read(bytes, 0, 28);
        inputStream.reset();
        return bytesToHex(bytes);
    }

    /**
     * 将字节数组转换成16进制字符串
     */
    private static String bytesToHex (byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if ( src == null || src.length <= 0 ) {
            return null;
        }
        for ( byte aSrc : src ) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if ( hv.length() < 2 ) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 转换为字节数组输入流，可以重复消费流中数据
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static ByteArrayInputStream toByteArrayInputStream (InputStream inputStream) throws IOException {
        if ( inputStream instanceof ByteArrayInputStream ) {
            return ( ByteArrayInputStream ) inputStream;
        }
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream() ) {
            BufferedInputStream br = new BufferedInputStream(inputStream);
            byte[] b = new byte[ 1024 ];
            for ( int c ; (c = br.read(b)) != - 1 ; ) {
                bos.write(b, 0, c);
            }
            // 主动告知回收
            b = null;
            br.close();
            inputStream.close();
            return new ByteArrayInputStream(bos.toByteArray());
        }
    }
}

