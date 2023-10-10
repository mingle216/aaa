package com.wisedu.minos.casp.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.casp.portal.common.constant.DbFieldConstant;
import com.wisedu.minos.casp.portal.common.constant.Global;
import com.wisedu.minos.casp.portal.common.constant.GlobalEnum;
import com.wisedu.minos.casp.portal.common.exception.BusinessException;
import com.wisedu.minos.casp.portal.common.page.PageFactory;
import com.wisedu.minos.casp.portal.common.page.PageResult;
import com.wisedu.minos.casp.portal.common.utils.*;
import com.wisedu.minos.casp.portal.dao.entity.CardEntity;
import com.wisedu.minos.casp.portal.dao.entity.ShowProgrammeEntity;
import com.wisedu.minos.casp.portal.dao.entity.TemplateEntity;
import com.wisedu.minos.casp.portal.dao.entity.VersionManagementEntity;
import com.wisedu.minos.casp.portal.dao.mapper.ShowProgrammeMapper;
import com.wisedu.minos.casp.portal.dao.mapper.TemplateMapper;
import com.wisedu.minos.casp.portal.dao.mapper.VersionManagementMapper;
import com.wisedu.minos.casp.portal.model.*;
import com.wisedu.minos.casp.portal.service.TemplateApiAdapter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 功能描述：
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title TemplateApiAdapterImpl
 * @Author: jcx
 * @Date: 2020/9/17
 */
@Service
public class TemplateApiAdapterImpl implements TemplateApiAdapter {
	@Autowired
	private TemplateMapper templateMapper;
	@Autowired
	private ShowProgrammeMapper showProgrammeMapper;

	@Autowired
	private TemplateService templateService;
	@Value("${file.templateUploadPath}")
	private String templateLocalUploadPath;

	@Autowired
	private VersionManagementMapper versionManagementMapper;
	@Override
	@Transactional(rollbackFor=Exception.class)
	public InlineResponse2009 deleteTemplate(List<String> body) {
		Assert.notNull(body, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
		Assert.isTrue(!body.isEmpty(), GlobalEnum.VALIDATION.getCode(), GlobalEnum.VALIDATION.getMsg());
		List<TemplateEntity> list = new ArrayList<>();
		//查询展示方案表，用来判断是否关联展示方案
		QueryWrapper<ShowProgrammeEntity> wrapper = new QueryWrapper<>();
		wrapper.eq(DbFieldConstant.IS_DELETED,Global.DeleteStatus.NO.getId());
		List<ShowProgrammeEntity> programmeList = showProgrammeMapper.selectList(wrapper);
		Map programmeMap = null;
		if( CollectionUtils.isNotEmpty(programmeList) ){
			programmeMap = programmeList.stream().collect(
					Collectors.toMap(ShowProgrammeEntity::getTemplateId,
							ShowProgrammeEntity::getWid, (oldVal, currVal) -> currVal));
		}
		List<VersionManagementEntity> versionManagements = new ArrayList<>();
		List<String> jarNames = new ArrayList<>();
		for (String wid : body) {
			if(null!=programmeMap&&null!=programmeMap.get(wid)){
				throw new BusinessException("展示方案关联到此模版，如已关联则必须解除关联方可删除!");
			}
			TemplateEntity templateEntity = new TemplateEntity();
			templateEntity.setWid(wid);
			templateEntity.setIsDeleted(Global.DeleteStatus.YES.getId());
			templateEntity.setTemplateStatus(Global.CARD_STATUS_NOT_AVAILABLE);
			list.add(templateEntity);
			VersionManagementEntity versionManagement= new VersionManagementEntity();
			TemplateEntity templateEntityFir = templateMapper.selectById(wid);
			versionManagement.setForeignKey(templateEntityFir.getTemplateId());
			versionManagement.setVersionNumber(templateEntityFir.getVersionNumber());
			versionManagements.add(versionManagement);

			String jarTemplateName = templateEntityFir.getTemplateId().toLowerCase().replaceAll("_", "-") + "-";
			jarNames.add(jarTemplateName);
		}
		templateService.updateBatchById(list);
		//删除模板的同时需连版本管理表记录也停用
		if( CollectionUtils.isNotEmpty(versionManagements) ){
			for(VersionManagementEntity versionManagementEntity:versionManagements){
				QueryWrapper<VersionManagementEntity> wrapperSql = new QueryWrapper<>();
				wrapperSql.eq(DbFieldConstant.FOREIGN_KEY,versionManagementEntity.getForeignKey())
						.eq(DbFieldConstant.VERSION_NUMBER,versionManagementEntity.getVersionNumber())
						.eq(DbFieldConstant.VERSION_TYPE,Integer.parseInt(Global.TEMPLATE_UPLOAD_TYPE))
						.eq(DbFieldConstant.VERSION_STATUS,Global.EnableStatus.ENABLE.getId());
				VersionManagementEntity dbVersionManagement= versionManagementMapper.selectOne(wrapperSql);
				if(null!=dbVersionManagement){
					dbVersionManagement.setVersionStatus(Global.EnableStatus.DISABLE.getId());
					versionManagementMapper.updateById(dbVersionManagement);
				}
			}
		}
		//删除Jar包
		if(!jarNames.isEmpty()){
			FileService.deleteJar(templateLocalUploadPath,jarNames);
		}
		InlineResponse2009 inlineResponse2009 = new InlineResponse2009();
		inlineResponse2009.setErrmsg("删除成功");
		return inlineResponse2009;
	}



	@Override
	public InlineResponse2008 getTemplateInfo(String wid) {
		Assert.notNull(wid, GlobalEnum.PARAM_FAIL.getCode(), GlobalEnum.PARAM_FAIL.getMsg());
		QueryWrapper<TemplateEntity> wrapper = new QueryWrapper<>();
		wrapper.lambda().eq(TemplateEntity::getWid, wid).eq(TemplateEntity::getIsDeleted, Global.DeleteStatus.NO.getId());
		TemplateEntity data = templateService.getOne(wrapper);
		if (ObjectUtils.isEmpty(data)) {
			return new InlineResponse2008();
		}
		return new InlineResponse2008().data((TemplateInfo) ObjectUtil.copyProperties(data, new TemplateInfo()));
	}

	@Override
	public InlineResponse2007 queryTemplates(CommonQueryListReq body) {
		//开启分页
		Page<TemplateEntity> page = PageFactory.createPageBeginPage(body);
		QueryWrapper<TemplateEntity> queryWrapper = AppBeanUtil.searchObjectCondition(body.getSearchCriteria(), TemplateEntity.class);
		queryWrapper.orderByDesc(DbFieldConstant.UPDATE_TIME);
		IPage<TemplateEntity> templateEntityIPage = templateMapper.selectPage(page, queryWrapper);
		List<TemplateEntity> data = templateEntityIPage.getRecords();
		InlineResponse2007 inlineResponse2007 = ObjectUtil.copyProperties(new PageResult(templateEntityIPage), new InlineResponse2007());
		if(CollectionUtils.isNotEmpty(data)){
			List<TemplateInfo> templateInfos = ObjectUtil.copyListProperties(data, TemplateInfo::new, (templateEntity, templateInfo) ->{
				// 这里可以定义特定的转换规则，用来转换时间
				templateInfo.setUpdateTime(DateUtil.getStrFromDate(templateEntity.getUpdateTime(),DateUtil.DATE_FORMATE_STRING_A));
			});
			QueryWrapper<ShowProgrammeEntity> wrapper = new QueryWrapper<>();
			wrapper.lambda().eq(ShowProgrammeEntity::getIsDeleted,Global.DeleteStatus.NO.getId()).
					in(ShowProgrammeEntity::getTemplateId,templateInfos.stream().map(e->e.getWid()).collect(Collectors.toList()));
			List<ShowProgrammeEntity> programmeList = showProgrammeMapper.selectList(wrapper);
			for (TemplateInfo info: templateInfos){
				info.setRefPage(programmeList.stream().anyMatch(e -> e.getTemplateId().equals(info.getWid())));
			}
			return inlineResponse2007.data(templateInfos);
		}
		return inlineResponse2007;
	}

	@Override
	public InlineResponse2008 saveTemplateConfig(CommonStringInfo body) {
		return null;
	}
}
