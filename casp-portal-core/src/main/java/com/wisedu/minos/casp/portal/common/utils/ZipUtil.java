package com.wisedu.minos.casp.portal.common.utils;

import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 功能描述：文档压缩相关工具类
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title ZipUtil
 * @Author: jcx
 * @Date: 2020/7/29
 */
public class ZipUtil {

    private static final Logger logger = LogManager.getLogger(ZipUtil.class);
    /**
     * 文档压缩
     *
     * @param file 需要压缩的文件或目录
     * @param dest 压缩后的文件名称
     * @throws IOException
     */
    public static void deCompress(File file, String dest) throws IOException {
        try ( ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
              BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zos)) {
            String dir = "";
            if (file.isDirectory()) {
                dir = file.getName();
            }
            zipFile(file, zos, dir,bufferedOutputStream);
        } catch (IOException e) {
            logger.error("文档压缩发生异常",e);
            throw new BusinessException("文档压缩发生异常");
        }
    }

    public static void zipFile(File inFile, ZipOutputStream zos, String dir,BufferedOutputStream bufferedOutputStream) throws IOException {
        if (inFile.isDirectory()) {
            zipDirectoryFile(inFile, zos, dir, bufferedOutputStream);
        } else {
            zipNotDirectoryFile(inFile, zos, dir, bufferedOutputStream);
        }
    }

    private static void zipNotDirectoryFile(File inFile, ZipOutputStream zos, String dir, BufferedOutputStream bufferedOutputStream) throws IOException {
        String entryName = dir + "/" + inFile.getName();
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream(inFile))) {
            int len = 0;
            while ((len = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(len);
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            logger.error("文档压缩发生异常",e);
            throw new BusinessException("文档压缩发生异常");
        }
    }

    private static void zipDirectoryFile(File inFile, ZipOutputStream zos, String dir, BufferedOutputStream bufferedOutputStream) throws IOException {
        File[] files = inFile.listFiles();
        if (files == null || files.length == 0) {
            String entryName = dir + "/";
            zos.putNextEntry(new ZipEntry(entryName));
            return;
        }
        for (File file : files) {
            String entryName = dir + "/" + file.getName();
            if (file.isDirectory()) {
                zipFile(file, zos, entryName,bufferedOutputStream);
            } else {
                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);
                try ( BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
                    int len = 0;
                    while ((len = bufferedInputStream.read()) != -1) {
                        bufferedOutputStream.write(len);
                    }
                    bufferedOutputStream.flush();
                } catch (IOException e) {
                    logger.error("文档压缩发生异常",e);
                    throw new BusinessException("文档压缩发生异常");
                }
            }
        }
    }

    /**
     * 文档解压
     *
     * @param source 需要解压缩的文档名称
     * @param path   需要解压缩的路径
     * @param isUnCompressLocal   解压是否在当前文档名称文件夹目录下  例，true :解压 C:/Users/a.zip  解压结果为  C:/Users/a/
     */
    public static List<String> unCompress(File source, String path,boolean isUnCompressLocal) throws IOException {
        ZipEntry zipEntry = null;
        //压缩文件中的文件路径合集
        List<String> fileNamePathArray = new ArrayList<>();
        String unCompressPath=isUnCompressLocal?path+"/"+FileUtil.getFileName(source.getName()):path;
        FileUtil.createPaths(unCompressPath);
        //实例化ZipFile，每一个zip压缩文件都可以表示为一个ZipFile
        //实例化一个Zip压缩文件的ZipInputStream对象，可以利用该类的getNextEntry()方法依次拿到每一个ZipEntry对象
        try (
                ZipFile zipFile = new ZipFile(source) ;
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(source));
        ) {
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                String filePath = unCompressPath + "/" + fileName;
                fileNamePathArray.add(filePath);
                if (zipEntry.isDirectory()) {
                    File temp = new File(filePath);
                    if (!temp.exists()) {
                        temp.mkdirs();
                    }
                } else {
                    File temp = new File(filePath);
                    if (!temp.getParentFile().exists()) {
                        temp.getParentFile().mkdirs();
                    }
                    try (
                         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(temp));
                         //通过ZipFile的getInputStream方法拿到具体的ZipEntry的输入流
                         BufferedInputStream bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry))
                    ){
                        int len = 0;
                        while ((len = bufferedInputStream.read()) != -1) {
                            bufferedOutputStream.write(len);
                        }

                        zipInputStream.closeEntry();
                        bufferedInputStream.close();
                        bufferedOutputStream.close();
                    } catch (IOException e) {
                        logger.error("文档解压发生异常",e);
                        throw new BusinessException("文档解压发生异常");
                    }
                }

            }
            zipInputStream.close();
        } catch (IOException e) {
            logger.error("文档解压发生异常",e);
            throw new BusinessException("文档解压发生异常");
        }
        return fileNamePathArray;
    }

    /**
     * @Title: compress
     * @param filePaths 需要压缩的文件地址列表（绝对路径）
     * @param zipFilePath 需要压缩到哪个zip文件（无需创建这样一个zip，只需要指定一个全路径）
     * @param keepDirStructure 压缩后目录是否保持原目录结构
     * @throws IOException
     * @return int   压缩成功的文件个数
     */
    public static int compress(List<String> filePaths, String zipFilePath,Boolean keepDirStructure) throws IOException{
        byte[] buf = new byte[1024];
        File zipFile = new File(zipFilePath);
        //zip文件不存在，则创建文件，用于压缩
        if(!zipFile.exists())
            zipFile.createNewFile();
        int fileCount = 0;//记录压缩了几个文件？
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
            for(int i = 0; i < filePaths.size(); i++){
                String relativePath = filePaths.get(i);
                if( StringUtils.isEmpty(relativePath)){
                    continue;
                }
                File sourceFile = new File(relativePath);//绝对路径找到file
                if(sourceFile == null || !sourceFile.exists()){
                    continue;
                }

                FileInputStream fis = new FileInputStream(sourceFile);
                if(keepDirStructure!=null && keepDirStructure){
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(relativePath));
                }else{
                    //直接放到压缩包的根目录
                    zos.putNextEntry(new ZipEntry(sourceFile.getName()));
                }
                int len;
                while((len = fis.read(buf)) > 0){
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                fis.close();
                fileCount++;
            }
            zos.close();
        } catch (Exception e) {
            logger.error("文档压缩发生异常",e);
            throw new BusinessException("文档压缩发生异常");
        }
        return fileCount;
    }
}
