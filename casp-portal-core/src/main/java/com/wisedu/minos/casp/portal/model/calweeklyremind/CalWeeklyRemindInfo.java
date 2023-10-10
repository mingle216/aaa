package com.wisedu.minos.casp.portal.model.calweeklyremind;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 * //todo 添加描述
 * @date 2023/3/29 12:16
 * @author jszhang@wisedu
 * @version 1.0
 **/
@Data
public class CalWeeklyRemindInfo {
    /**f
     * 用户和日历的关系
     */
    private List<SimpleUserCalCardRel> userCals;
    /**f
     * 卡片配置
     */
    private Map<String, List<String>> cardConfigMap;

    private String startTime;
    private String endTime;
    private String mailSendChannelId;
    private String mailSendInstanceWid;
    private String title;
}
