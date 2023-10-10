package com.wisedu.minos.casp.portal.common.page;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wisedu.minos.casp.portal.common.utils.StringUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 功能描述：分页工厂
 * 修改记录:
 * <pre>
 * 修改时间：
 * 修改人：
 * 修改内容：
 * </pre>
 *
 * @title PageFactory
 * @Author: jcx
 * @Date: 2020/8/14
 */
@Slf4j
public class PageFactory {

	private static final String ORDER_BY_TYPE_ASC = "asc";

	private static final String ORDER_BY_TYPE_DESC = "desc";

	public static <T> PageResult<T> pageResult(IPage<T> page) {
		return new PageResult<T>(page);
	}

	/**
	 * @Author jcx
	 * @Description 创建分页
	 * @Date 10:42 2020/8/14
	 * @Param pageBase:
	 * @return Page<T>
	 **/
	public static <T> Page<T> createPage(PageBase pageBase) {
		Long pageSize = 10L;
		Long pageNumber = 1L;
		if (pageBase != null && pageBase.getPageSize()!=null) {
			pageSize = Long.valueOf(pageBase.getPageSize());
		}
		if (pageBase != null && pageBase.getPageNumber()!=null) {
			pageNumber = Long.valueOf(pageBase.getPageNumber());
		}
		Page<T> page = new Page<T>(pageNumber, pageSize);
		if (pageBase!=null && StringUtil.isNotEmpty(pageBase.getOrderByType())){

			if (ORDER_BY_TYPE_ASC.equals(pageBase.getOrderByType())) {
				page.setAsc(new String[] { pageBase.getOrderByField() });

			} else if (ORDER_BY_TYPE_DESC.equals(pageBase.getOrderByType())){
				page.setDesc(new String[] { pageBase.getOrderByField() });
			}
		}
		return page;
	}

	/**
	 * 类中无pageSize pageNumber开启分页，此类需继承PageBase
	 * @param s
	 * @param <S>
	 * @return
	 */
	public static  <S extends PageBase,T> Page<T> createPageBegin (S s){

		return createPage(buildPageBase(s));

	}

	/***
	 * @Author jcx
	 * @Description 类中有pageSize pageNumber开启分页,此类无需继承PageBase
	 * @Date 13:12 2020/9/19
	 * @Param s:
	 * @return Page<T>
	 **/
	public static  <S,T> Page<T> createPageBeginPage (S s){
		return createPage(buildPageBaseNoPage(s));
	}
	private static <S>PageBase buildPageBaseNoPage (S s){
		PageBase base = new PageBase();
		Class<?> aClass = s.getClass();
		try {
			//两种方式获取泛型属性值
			//1.获取get方法获取属性值 传入属性名称
			PropertyDescriptor pd = new PropertyDescriptor("pageSize", aClass);
			Method rm = pd.getReadMethod();
			// 这块一定要传入s而不是 s.getClass().newInstance()
			Integer pageSize = (Integer) rm.invoke(s);

			// 2.获取属性值
			Field sysids = s.getClass().getDeclaredField("pageNumber");
			sysids.setAccessible(true);
			// 这块一定要传入s而不是 s.getClass().newInstance()
			Integer pageNumber = (Integer) sysids.get(s);

			base.setPageNumber(pageNumber);
			base.setPageSize(pageSize);
		} catch ( Exception e ) {
			LOGGER.info("context",e);
		}
		return base;
	}
	/**
	 * 组装分页参数
	 * @param pageBase
	 * @return
	 */
	private static <S>PageBase buildPageBase (S pageBase){
		PageBase base = new PageBase();
		if ( pageBase instanceof PageBase){
			if (((PageBase) pageBase).getPageNumber()!=null)
				base.setPageNumber(((PageBase) pageBase).getPageNumber());
			if (((PageBase) pageBase).getPageSize()!=null)
				base.setPageSize(((PageBase) pageBase).getPageSize());
			if ( StringUtil.isNotEmpty(((PageBase) pageBase).getOrderByField()))
				base.setOrderByField(((PageBase) pageBase).getOrderByField());
			if (StringUtil.isNotEmpty(((PageBase) pageBase).getOrderByType()))
				base.setOrderByType(((PageBase) pageBase).getOrderByType());
		}
		return base;
	}

	private PageFactory(){
		//过sonar检查
	}

	/**
	 *
	 * @param list 使用list.subList 实现分页
	 * @param pageNum 当前页
	 * @param pageSize 需要返回的数量
	 * @param <T>
	 * @return 返回当前页数据
	 */
	public static <T> List<T> getListPaging(List<T> list, int pageNum, int pageSize) {
		if(list == null || list.size() <= 0){
			return new ArrayList<>(0);
		}
		//开始下标
		int startIndex = (pageNum - 1) * pageSize;
		//结束下标 subList()方法不包含结束下标的元素
		int endIndex = pageNum * pageSize;
		//list总条数
		int total = list.size();
		//总页数
		int pageCount = 0;
		//通过取余决定是否给总页数加1
		int num = total % pageSize;
		if (num == 0) {
			pageCount = total / pageSize;
		} else {
			pageCount = total / pageSize + 1;
		}
		//如果当前页是最后一页的话 要包含集合的最后一条数据，因为sublist方法本身结束的下标是不包含当前元素的
		if (pageNum == pageCount) {
			endIndex = total;
		}
		return list.subList(startIndex, endIndex);
	}

}
