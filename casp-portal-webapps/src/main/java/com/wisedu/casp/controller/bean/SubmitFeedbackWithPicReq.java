package com.wisedu.casp.controller.bean;

import com.wisedu.casp.expansion.dubbo.apps.feedback.service.vo.DubboRelatedContentInfo;
import lombok.Data;

@Data
public class SubmitFeedbackWithPicReq {

    private String feedbackTitle;

    private String feedbackContent;

    private String phrasesWid;

    private String phrasesContent;

    private DubboRelatedContentInfo relatedContentInfo;

    private String userId;

    private String userName;

    private String appraisePics;

}
