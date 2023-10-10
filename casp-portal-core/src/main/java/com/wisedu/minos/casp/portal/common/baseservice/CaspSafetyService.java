package com.wisedu.minos.casp.portal.common.baseservice;

import com.wisedu.minos.casp.portal.common.utils.StringUtil;
import com.wisedu.minos.util.MinosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CASP公共的Service
 */
@Service
public class CaspSafetyService {
    @Value("${file.systemCardUploadPath}")
    private String systemCardUploadPath;

    @Value("${file.cardUploadPath}")
    private String cardUploadPath;

    @Value("${file.systemTemplateUploadPath}")
    private String systemTemplateUploadPath;

    @Value("${file.templateUploadPath}")
    private String templateUploadPath;

    @Value("${file.uploadLocalIncoPath}")
    private String uploadLocalIncoPath;

    @Value("${file.uploadPath}")
    private String uploadPath;

    /**
     * 判断传入的目录是否为允许写入目录,合法的目录返回true
     * @param path
     * @return
     */
    public boolean legalPath(String path){
        if (StringUtil.isEmpty(path)){
            throw new MinosException("写入的目录为空");
        }
        if (path.startsWith(systemCardUploadPath)
                || path.startsWith(cardUploadPath)
                || path.startsWith(systemTemplateUploadPath)
                || path.startsWith(templateUploadPath)
                || path.startsWith(uploadLocalIncoPath)
                || path.startsWith(uploadPath)){
            return true;
        }
        throw new MinosException(path + "需要写入的目录为非法目录，不允许写入");
    }

    /**
     * 判断传入的字符是否为合法字符,如果为合法字符，则返回false
     * @param word
     * @return
     */
    public boolean legalCharacter(String word){
        if (StringUtil.isEmpty(word)){
            return true;
        }
        Pattern p=Pattern.compile("^[0-9a-zA-Z_-]{1,}$");
        Matcher m=p.matcher(word);

        return !m.matches();
    }
}
