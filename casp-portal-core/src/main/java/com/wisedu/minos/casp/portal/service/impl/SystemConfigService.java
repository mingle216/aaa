package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.SystemConfigEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ShowProgrammeMapper;
import com.wisedu.minos.casp.portal.dao.mapper.SystemConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SystemConfigService extends ServiceImpl<SystemConfigMapper, SystemConfigEntity> {
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    public String getSystemConfigValue(String configKey) {
        List<SystemConfigEntity> systemConfigList = systemConfigMapper.selectList(Wrappers.<SystemConfigEntity>lambdaQuery().eq(SystemConfigEntity::getConfigKey, configKey).eq(SystemConfigEntity::getIsDeleted, 0));
        if (!CollectionUtils.isEmpty(systemConfigList)) {
            SystemConfigEntity systemConfig = systemConfigList.get(0);
            return StringUtils.isEmpty(systemConfig.getConfigValue()) ? systemConfig.getDefaultValue() : systemConfig.getConfigValue();
        }
        return null;
    }

    public List<SystemConfigEntity> matchConfigKey(String keyPrefix) {
        return systemConfigMapper.selectList(Wrappers.<SystemConfigEntity>lambdaQuery().likeRight(SystemConfigEntity::getConfigKey, keyPrefix).eq(SystemConfigEntity::getIsDeleted, 0));
    }

    public SystemConfigEntity getSystemConfig(String configKey) {
        List<SystemConfigEntity> systemConfigList = systemConfigMapper.selectList(Wrappers.<SystemConfigEntity>lambdaQuery().eq(SystemConfigEntity::getConfigKey, configKey).eq(SystemConfigEntity::getIsDeleted, 0));
        if (!CollectionUtils.isEmpty(systemConfigList)) {
            SystemConfigEntity systemConfig = systemConfigList.get(0);
            return systemConfig;
        }
        return null;
    }

    public Map<String, String> getSystemConfigMap(ConfigModel configModel) {
        LambdaQueryWrapper<SystemConfigEntity> wrapper = Wrappers.<SystemConfigEntity>lambdaQuery().eq(SystemConfigEntity::getIsDeleted, 0);
        if (null != configModel) {
            wrapper = wrapper.eq(SystemConfigEntity::getConfigModel, configModel.getCode());
        }
        List<SystemConfigEntity> systemConfigEntities = systemConfigMapper.selectList(wrapper);
        return systemConfigEntities
                .stream().collect(Collectors.toMap(SystemConfigEntity::getConfigKey, this::getConfigValue));
    }

    private String getConfigValue(SystemConfigEntity item) {
        String defaultValue = StringUtils.isEmpty(item.getDefaultValue()) ? "" : item.getDefaultValue();
        return StringUtils.isEmpty(item.getConfigValue()) ? defaultValue : item.getConfigValue();
    }

    /**
     * getSystemConfig
     * <p/>
     * 根据key值获取配置
     *
     * @param configKeys
     * @return java.util.List<java.lang.String>
     * @throws
     * @date 2020-11-1 12:01
     */
    public List<SystemConfigEntity> getSystemConfigs(List<String> configKeys) {
        return systemConfigMapper.selectList(Wrappers.<SystemConfigEntity>lambdaQuery()
                .eq(SystemConfigEntity::getIsDeleted, Global.DeleteStatus.NO.getId())
                .in(SystemConfigEntity::getConfigKey, configKeys));
    }

    public enum ConfigModel {
        SYSTEM(0, "系统配置"), PAGE(1, "页面配置"), OTHER(2, "其他配置");

        private int code;
        private String message;

        ConfigModel(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
