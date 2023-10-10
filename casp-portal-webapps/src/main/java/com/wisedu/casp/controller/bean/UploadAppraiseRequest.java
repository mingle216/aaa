package com.wisedu.casp.controller.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title UploadAppraiseRequest
 * @Author: 01120034
 * @Date: 2023/4/4
 */
@Data
public class UploadAppraiseRequest implements Serializable {
    private List<String> uploadFile;
    private String data;

}
