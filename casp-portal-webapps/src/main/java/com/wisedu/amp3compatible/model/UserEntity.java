package com.wisedu.amp3compatible.model;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wisedu.minos.casp.portal.common.utils.ArrayUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.UserType;

import java.util.*;

/**
 * 用户实体类
 * <p>应用管理平台</p>
 * <p>江苏金智教育信息技术有限公司</p>
 * <p>功能说明：保存登录用户的基本属性,其他属性可以自定义扩展</p>
 *
 * @author 丁窍
 * @version 1.0    创建时间：2016年1月25日下午8:43:13	丁窍	发布
 */
public class UserEntity  {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 中文名
     */
    private String cname;

    /**
     * 用户性别
     */
    private String userSex;

    /**
     * 用户部门
     */
    private String userDept;

    /**
     * 用户班级（暂时无数据）
     */
    private String userClass;

    /**
     * 用户类型
     *
     * @see UserType
     */
    private UserType userType;    // 用户类型

    /**
     * 用户角色集合
     */
    private String[] roles;    // 角色列表 001 | 002

    /**
     * 用户的用户组
     */
    private List<GroupEntity> groups;

    /**
     * 用户属性集合,用于自定义用户属性
     */
    private Map<String, Object> propertys;

    private String userTypeName;

    /**
     * 用户的单体应用列表
     */
    private Set<String> monomerEnvironmentIds;

    public Set<String> getMonomerEnvironmentIds () {
        return monomerEnvironmentIds;
    }

    public void setMonomerEnvironmentIds (Set<String> monomerEnvironmentIds) {
        this.monomerEnvironmentIds = monomerEnvironmentIds;
    }

    public String getUserTypeName () {
        return userTypeName;
    }

    public void setUserTypeName (String userTypeName) {
        this.userTypeName = userTypeName;
    }

    /**
     * 前台标记，是否为新增
     */
    private boolean isNew = false;

    private String pgt;

    public String getPgt () {
        return pgt;
    }

    public void setPgt (String pgt) {
        this.pgt = pgt;
    }

    /**
     * @return the userId
     */
    public String getUserId () {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getCname () {
        return cname;
    }

    public void setCname (String cname) {
        this.cname = cname;
    }

    public String getUserSex () {
        return userSex;
    }

    public void setUserSex (String userSex) {
        this.userSex = userSex;
    }

    public String getUserDept () {
        return userDept;
    }

    public void setUserDept (String userDept) {
        this.userDept = userDept;
    }

    public String getUserClass () {
        return userClass;
    }

    public void setUserClass (String userClass) {
        this.userClass = userClass;
    }

    @JsonIgnore
    public List<GroupEntity> getGroups () {
        return groups;
    }

    public void setGroups (List<GroupEntity> groups) {
        this.groups = groups;
    }

    @JsonIgnore
    public List<String> getGroupIds () {
        if ( CollectionUtil.isEmpty(groups) )
            return Collections.emptyList();

        List<String> groupIdList = new ArrayList<String>();
        for ( GroupEntity group : groups ) {
            groupIdList.add(group.getId());
        }
        return groupIdList;
    }

    /**
     * @return the roles
     */
    @JsonIgnore
    public Set<String> getRoles () {
        if ( ArrayUtil.isEmpty(roles) )
            return Collections.emptySet();
        return new HashSet<String>(Arrays.asList(roles));
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles (Collection<String> roles) {
        if ( CollectionUtil.isEmpty(roles) )
            this.roles = ArrayUtil.emptyStringArray();
        else
            this.roles = roles.toArray(new String[ 0 ]);
    }

    public boolean isNew () {
        return isNew;
    }

    public void setNew (boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * @return the userType
     */
//    public UserType getUserType () {
//        if ( userType == null )
//            return UserType.STUDENT;
//        return userType;
//    }

    /**
     * @param userType the userType to set
     */
    public void setUserType (UserType userType) {
        this.userType = userType;
    }

    /**
     * @return the propertys
     */
    @JsonIgnore
    public Map<String, Object> getPropertys () {
        return propertys;
    }

    /**
     * @param name
     * @param value
     */
    public void setProperty (String name, Object value) {
        if ( StringUtils.isEmpty(name) )
            return;
        if ( this.propertys == null )
            this.propertys = new HashMap<String, Object>();
        this.propertys.put(name, value);
    }

    /**
     * 方法名：移除用户属性
     * <p>功能说明：</p>
     *
     * @param name
     */
    public void remoreProperty (String name) {
        if ( StringUtils.isEmpty(name) )
            return;
        if ( MapUtils.isNotEmpty(propertys) )
            propertys.remove(name);
    }

    /**
     * 方法名：获取用户属性
     * <p>功能说明：</p>
     *
     * @param name
     * @return
     * @see #getProperty(String, Class)
     */
    @JsonIgnore
    public Object getProperty (String name) {
        return getProperty(name, Object.class);
    }

    /**
     * 方法名：获得用户指定属性
     * <p>功能说明：获取指定属性，并指定返回数据的类型</p>
     *
     * @param propertyName 属性名称
     * @param requireType  属性值类型
     * @return
     */
    @JsonIgnore
    @SuppressWarnings("unchecked")
    public <T> T getProperty (String propertyName, Class<T> requireType) {
        if ( StringUtils.isEmpty(propertyName) )
            return null;

        return ( T ) (this.propertys == null ? null : this.propertys.get(propertyName));
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString () {
        return "UserEntity [cname=" + cname + ", roles=" + Arrays.toString(roles) + ", userId=" + getUserId()
                + "]";
    }

//    /**
//     * 是否有系统管理员权限
//     *
//     * @return
//     */
//    public boolean isSystemAdmin () {
//        return getRoles().contains(Constant.Role.SYSTEM_MANAGER);
//    }
//
//    /**
//     * 是否有域管理员权限
//     *
//     * @return
//     */
//    public boolean isDomainAdmin () {
//        return getRoles().contains(Constant.Role.DOMAIN_MANAGER);
//    }
//
//    /**
//     * 是否有应用管理员权限
//     *
//     * @return
//     */
//    public boolean isAppAdmin () {
//        return getRoles().contains(Constant.Role.APP_MANAGER);
//    }
//
//    /**
//     * 是否有超级管理员权限
//     *
//     * @return
//     */
//    public boolean isSuperAdmin () {
//        return getRoles().contains(Constant.Role.SUPER_MANAGER);
//    }
//
//    /**
//     * 是否部门管理员
//     *
//     * @return
//     */
//    public boolean isDeptAdmin () {
//        return getRoles().contains(Constant.Role.DEPT_MANAGER);
//    }
//
//    /**
//     * 用戶是否有管控台操作权限
//     *
//     * @return
//     */
//    public boolean hasAdminRole () {
//        return isSuperAdmin() || isSystemAdmin() || isDomainAdmin() || isAppAdmin();
//    }
}



