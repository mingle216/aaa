package com.wisedu.minos.casp.portal.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import com.wisedu.minos.casp.portal.model.SearchObject;
import com.wisedu.minos.util.MinosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;



/**
 * 业务模型和数据库模型转化工具类
 * @author zhangjian 0116211
 * @create 2019-11-28
 **/

public class AppBeanUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppBeanUtil.class);

    /**
     * 生成查询的条件方法
     */
    public static <T> QueryWrapper<T> searchObjectCondition(List<SearchObject> body, Class<T> clazz) {
        try {
            // 根据传入的模型类，获取属性名与数据库列名自建的映射关系
            Map<String,String> field2ColumnMap = CommonUtil.getField2ColumnMap(clazz);
            Map<String, Class<?>> fieldType2ColumnMap = CommonUtil.getFieldType2ColumnMap(clazz);
            if (CollectionUtils.isEmpty(field2ColumnMap)){
                throw new MinosException("传入的类"+clazz.getName()+"不是实体对象列");
            }

            QueryWrapper<T> outWrapper = new QueryWrapper<>();
            outWrapper.eq("is_deleted",0);
            if (!CollectionUtils.isEmpty(body)) {
                for (SearchObject searchObject : body) {
                    if (Strings.isNullOrEmpty(searchObject.getValue())){
                        continue;
                    }
                    String[] fields = searchObject.getField().split(",");

                    List<String> columns = CommonUtil.getColumnNamesBySearchField(field2ColumnMap,fields);
                    if (CollectionUtils.isEmpty(columns)){
                        continue;
                    }
                    Object val = CommonUtil.convertValue2DBType(columns, fieldType2ColumnMap, searchObject.getValue());
                    outWrapper.and(dictEntityQueryWrapper -> {
                        switch (searchObject.getComparator()) {
                            case ILIKE:
                                CommonUtil.addIlikeCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case LIKE:
                                CommonUtil.addlikeCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case NOT:
                                CommonUtil.addNotCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case EQ:
                                CommonUtil.addEqCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case GE:
                                CommonUtil.addGeCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case LE:
                                CommonUtil.addLeCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case GT:
                                CommonUtil.addGtCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case LT:
                                    CommonUtil.addLtCondition(dictEntityQueryWrapper, columns, val);
                                break;
                            case IN:
                                CommonUtil.addInCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case NOT_IN:
                                CommonUtil.addNotInCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            case BETWEEN:
                                CommonUtil.addBetweenCondition(dictEntityQueryWrapper,columns,val);
                                break;
                            default:
                                break;
                        }
                        return dictEntityQueryWrapper;
                    });
                }
            }
            return outWrapper;
        } catch (Exception e) {
            LOGGER.error("搜索失败",e);
            throw new MinosException(e.getMessage());
        }
    }

}
