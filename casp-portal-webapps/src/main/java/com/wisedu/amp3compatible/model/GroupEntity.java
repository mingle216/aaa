package com.wisedu.amp3compatible.model;

import java.io.Serializable;

/**
 * @author yzyang@wisedu.com
 * @version V1.0
 * @ClassName: RoleEntity
 * @Description:
 * @date 2015/2/6 13:13
 */
public class GroupEntity implements Serializable {

	private static final long serialVersionUID = 8872175988196263977L;

	private String id;
	private String name;
	public static final short DYNAMIC = 1;
    private String description;
    private short dynamic = 0;
    private String containsSql = "";	// 包含用户sql
    private String checkSql = "";	// 存在用户sql
    private String domainId;	// 业务域ID
    private String domainName;	// 业务域Name

	//是否可用,如果不可用将不会添加给用户
    private boolean availabe = Boolean.FALSE;

    private String creator = "";	// 群组创建者账号
	private String operator="";//群组更改者
    private GroupType type;

    public enum GroupType {
    	COMMON("0"), DOMAIN("1"), APP_PRIVATE("2");

    	private String value;
    	private GroupType(String value) {
    		this.value = value;
    	}
		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

    	public String toString() {
    		return getValue();
    	}
    }

    public GroupEntity() {
    	//constructor
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String groupName) {
		this.name = groupName;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAvailabe() {
        return availabe;
    }

    public void setAvailabe(boolean availabe) {
        this.availabe = availabe;
    }

    /**
   	 * @return the dynamic
   	 */
   	public boolean isDynamicGroup() {
   		return DYNAMIC == dynamic;
   	}

    /**
	 * @param dynamic the dynamic to set
	 */
	public void setDynamic(short dynamic) {
		this.dynamic = dynamic;
	}

	/**
	 * @return the containsSql
	 */
	public String getContainsSql() {
		return containsSql;
	}

	/**
	 * @param containsSql the containsSql to set
	 */
	public void setContainsSql(String containsSql) {
		this.containsSql = containsSql;
	}

	/**
	 * @return the checkSql
	 */
	public String getCheckSql() {
		return checkSql;
	}

	/**
	 * @param checkSql the checkSql to set
	 */
	public void setCheckSql(String checkSql) {
		this.checkSql = checkSql;
	}

	/**
	 * @return the domainId
	 */
	public String getDomainId() {
		return domainId;
	}

	/**
	 * @param domainId the domainId to set
	 */
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	/**
	 * @return the domainName
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * @param domainName the domainName to set
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * @return the dynamic
	 */
	public short getDynamic() {
		return dynamic;
	}

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the type
	 */
	public GroupType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(GroupType type) {
		this.type = type;
	}

	public String getOperator()
	{
		return operator;
	}

	public void setOperator(String operator)
	{
		this.operator = operator;
	}
}
