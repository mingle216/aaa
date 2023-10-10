package com.wisedu.minos.casp.portal.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.wisedu.minos.casp.portal.common.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 功能描述：自动填充 时间
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title MyMetaObjectHandler
 * @Author: jcx
 * @Date: 2020/9/11
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private final static String UPDATE_TIME = "updateTime";

    private final static String CREATE_TIME = "createTime";

    private final static String IS_DELETE = "isDeleted";//1 正常  0 删除

    private final static Integer DEFAULT_VAL = 1;


    // 新增的时候自动填充
    @Override
    public void insertFill(MetaObject metaObject) {


        Date date = null;

        if (metaObject.hasSetter(CREATE_TIME)){//创建时间
            Object object = getFieldValByName(CREATE_TIME,metaObject);

            if (!Objects.nonNull(object)){

                date = getSysDate();
                this.setFieldValByName(CREATE_TIME, date, metaObject);
            }
        }

        if (metaObject.hasSetter(UPDATE_TIME)){//更新时间
            Object object = getFieldValByName(UPDATE_TIME,metaObject);

            if (!Objects.nonNull(object)){

                if (date == null){
                    date = getSysDate();
                }
                this.setFieldValByName(UPDATE_TIME, date, metaObject);
            }


        }

        if (metaObject.hasSetter(IS_DELETE)){//是否删除

            Object object = getFieldValByName(IS_DELETE,metaObject);
            if (!Objects.nonNull(object)){
                this.setFieldValByName(IS_DELETE, DEFAULT_VAL, metaObject);
            }
        }
    }

    // 更新的税后自动填充
    @Override
    public void updateFill(MetaObject metaObject) {

        if (metaObject.hasSetter(UPDATE_TIME)) {//更新时间


            Object object = getFieldValByName(UPDATE_TIME,metaObject);

            if (!Objects.nonNull(object)){

                this.setFieldValByName(UPDATE_TIME, getSysDate(), metaObject);
            }

        }

    }

    private Date getSysDate(){

        return CommonUtil.getSystemDate();
    }
}
