package com.tienthanh.domain;

import java.util.Date;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractClass {
	private Date createDate;
	private Date modifyDate;

	public AbstractClass() {
	}

	public AbstractClass(Date createDate, Date modifyDate) {
		this.createDate = createDate;
		this.modifyDate = modifyDate;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}
