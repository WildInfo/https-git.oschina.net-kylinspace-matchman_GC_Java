package com.wild.entity.user;

import java.io.Serializable;

/**
 * 用户与用户详情关系
 * 
 * @author Wild
 *
 */
public class WUserDetailsRelation implements Serializable {

	private static final long serialVersionUID = -5121087614919443282L;

	private WUser user;
	private Wdetails details;

	public WUser getUser() {
		return user;
	}

	public void setUser(WUser user) {
		this.user = user;
	}

	public Wdetails getDetails() {
		return details;
	}

	public void setDetails(Wdetails details) {
		this.details = details;
	}
}
