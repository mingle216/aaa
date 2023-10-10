package com.wisedu.minos.casp.portal.utils.comparatorUtil;

import com.wisedu.minos.casp.portal.vo.AppAppraiseSummaryVo;

import java.util.Comparator;

/**
 * @描述
 * @创建人 wangrong
 * @创建时间 2021/12/22
 */

public class AppAppraiseComparator implements Comparator<AppAppraiseSummaryVo> {


    @Override
    public int compare(AppAppraiseSummaryVo o1, AppAppraiseSummaryVo o2) {
        //按平均分降序排序
        Double avgScoreOne = o1.getAvgScore();
        Double avgScoreTwo = o2.getAvgScore();
        return avgScoreTwo.compareTo(avgScoreOne);
    }
}
