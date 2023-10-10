package com.wisedu.casp.controller.file;

import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.result.ResultData;
import com.wisedu.minos.casp.portal.service.impl.FileService;
import com.wisedu.minos.casp.portal.vo.CardTemUpVo;
import com.wisedu.minos.casp.portal.vo.SwitchVersionVo;
import com.wisedu.minos.gateway.client.annotation.IgnoreGatewaySecurity;
import com.wisedu.minos.gateway.client.annotation.ManagerGatewayApi;
import com.wisedu.minos.gateway.client.util.ApiOperationTypeEnums;
import com.wisedu.minos.util.MinosException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 功能描述：文件相关控制器
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title FileController
 * @Author: jcx
 * @Date: 2020/8/14
 */
@RestController
@RequestMapping("/fileTodo")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * uploadFile
     * <p/>
     * 卡片、模板包上传服务器指定目录
     *
     * @param files
     * @param type 0:模板 1:卡片
     * @return com.wisedu.minos.casp.portal.common.result.ResultData
     * @throws
     * @date 2020-10-8 19:32
     */
    @ManagerGatewayApi(name = "卡片、模板包上传服务器指定目录", realPath = "/fileTodo/uploadFile/{type}")
    @RequestMapping("/uploadFile/{type}")
    public ResultData uploadFile(@RequestParam("files") MultipartFile files, @PathVariable("type") String type) {
        return fileService.uploadFile(files, type);
    }

    /**
     * @return ResultData
     * @Author jcx
     * @Description 上传单个卡片、模板包至oss
     * @Date 10:31 2020/9/16
     * @Param file:
     **/
    @ManagerGatewayApi(name = "上传单个卡片、模板包至oss", realPath = "/fileTodo/uploadFileToOss")
    @RequestMapping("/uploadFileToOss")
    public ResultData uploadFileToOss(@RequestParam("files") MultipartFile file) {
        return ResultData.success(fileService.uploadFileToOss(file));
    }



    /**
     * @return ResultData
     * @Author jcx
     * @Description 上传多个卡片、模板包至oss
     * @Date 10:31 2020/9/16
     * @Param file:
     **/
    @ManagerGatewayApi(name = "上传单个卡片、模板包至oss", realPath = "/fileTodo/uploadFilesToOss")
    @RequestMapping("/uploadFilesToOss")
    public ResultData uploadFilesToOss(@RequestParam("files") MultipartFile file) {
        return ResultData.success(fileService.uploadFileToOss(file));
    }

    /**
     * @return ResultData
     * @Author jcx
     * @Description 多个卡片、模板包上传服务器指定目录
     * @Date 17:36 2020/8/14
     * @Param files:
     **/
    @ManagerGatewayApi(name = "多个卡片、模板包上传服务器指定目录", realPath = "/fileTodo/uploadFiles/{type}")
    @RequestMapping("/uploadFiles/{type}")
    public ResultData uploadFiles(@RequestParam("files") MultipartFile[] files, @PathVariable("type") String type) {
        return ResultData.success(fileService.uploadFiles(files, type));
    }

    /**
     * @Author jcx
     * @Description 下载服务器指定目录下单个文件
     * @Date 17:38 2020/8/14
     * @Param files:
     **/
    @ManagerGatewayApi(name = "下载服务器指定目录下单个文件", realPath = "/fileTodo/downloadFile/{type}/{filePath}/{fileName}")
    @RequestMapping("/downloadFile/{type}/{filePath}/{fileName}")
    public void downloadFile(HttpServletResponse response, @PathVariable("type") String type, @PathVariable("filePath") String filePath, @PathVariable("fileName") String fileName) {
        fileService.downloadFile(response, filePath, fileName, type);
    }

    /**
     * @return void
     * @Author jcx
     * @Description 从OSS下载文件
     * @Date 10:20 2020/9/18
     * @Param request:
     * @Param response:
     **/
    @ManagerGatewayApi(name = "从OSS下载文件", realPath = "/fileTodo/downLoadFileFromOss/{wid}")
    @RequestMapping("/downLoadFileFromOss/{wid}")
    @ResponseBody
    public void downLoadFileFromOss(HttpServletRequest request, HttpServletResponse response, @PathVariable("wid") String wid) {
        fileService.downLoadFileFromOss(response, wid);
    }

    /**
     * @Author jcx
     * @Description 从OSS下载卡片或者模板包
     * @Date 14:40 2020/10/10
     * @Param response:
     * @Param wid:
     * @Param type: 0:模板  1:卡片
     * @return void
     **/
    @ManagerGatewayApi(name = "从OSS下载卡片或者模板包", realPath = "/fileTodo/downCardOrTemFromOss/{wid}/{type}")
    @RequestMapping("/downCardOrTemFromOss/{wid}/{type}")
    public void downCardOrTemFromOss(HttpServletResponse response, @PathVariable("wid") String wid, @PathVariable("type") String type) {
        if ( StringUtils.isBlank(wid) || StringUtils.isBlank(type) ) {
            throw new MinosException(GlobalEnum.PARAM_FAIL.getMsg());
        }
        fileService.downCardOrTemFromOss(response, wid,type);
    }

    /**
     * @return void
     * @Author jcx
     * @Description 卡片、模板确认/取消覆盖方法、确认/取消安装最新新卡片、模板
     * @Date 16:58 2020/9/21
     * @Param isRestart:  是否重启服务
     * @Param isCancel:  是否取消  1：是  0：否
     **/
    @ManagerGatewayApi(name = "卡片、模板确认/取消覆盖方法、确认/取消安装最新新卡片、模板", realPath = "/fileTodo/confirmCoverage",operationType = ApiOperationTypeEnums.API_OPERATION_ADD)
    @RequestMapping("/confirmCoverage")
    public ResultData confirmCoverage(@RequestBody CardTemUpVo vo) {
        fileService.uploadCardTempToOss(vo);
        return ResultData.success();
    }

    /**
     * @return void
     * @Author jcx
     * @Description 上传其他文件至服务器本地目录
     * @Date 10:50 2020/9/23
     * @Param file: 附件
     * @Param type: 0:图标 后续拓展
     **/
    @ManagerGatewayApi(name = "上传其他文件至服务器本地目录", realPath = "/fileTodo/uploadLocalServer/{type}")
    @RequestMapping("/uploadLocalServer/{type}")
    public ResultData uploadLocalServer(@RequestParam("files") MultipartFile file, @PathVariable("type") String type) {
        return ResultData.success(fileService.uploadLocalServer(file, type));
    }

    /**
     * @Author jcx
     * @Description 卡片、模板切换版本
     * @Date 10:43 2020/10/13
     * @Param vo:
     * @return ResultData
     **/
    @ManagerGatewayApi(name = "卡片、模板切换版本", realPath = "/fileTodo/switchVersion")
    @RequestMapping("/switchVersion")
    public ResultData switchVersion(HttpServletResponse response,@RequestBody SwitchVersionVo vo) {
        return fileService.switchVersion(response,vo);
    }

    /**
     *  下载小程序包
     * @param response
     * @param type
     * @param portalDomain
     */
    @ManagerGatewayApi(name = "下载小程序包", realPath = "/fileTodo/downloadApplet/{type}")
    @RequestMapping("/downloadApplet/{type}")
    public void downloadApplet(HttpServletResponse response, @PathVariable String type, @RequestBody String portalDomain) {
        if ( StringUtils.isBlank(portalDomain) || StringUtils.isBlank(type) ) {
            throw new MinosException(GlobalEnum.PARAM_FAIL.getMsg());
        }
        fileService.downloadApplet(response, type, portalDomain);
    }

    /**
     * @Author hlchen02
     * @Description 根据url返回流
     * @Date 17:38 2020/8/14
     * @Param files:
     **/
    @IgnoreGatewaySecurity
    @ManagerGatewayApi(name = "下载指定链接文件", realPath = "/fileTodo/downloadFileByUrl")
    @RequestMapping("/downloadFileByUrl")
    public void downloadFileByUrl(HttpServletRequest request, HttpServletResponse response, @RequestParam("fileUrl") String fileUrl, @RequestParam(required = false, value = "fileName") String fileName) {
        fileService.downloadFileByUrl(request, response, fileUrl, fileName);
    }
}
