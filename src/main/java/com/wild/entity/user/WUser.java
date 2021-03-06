package com.wild.entity.user;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wild.enums.message.StatusEnum;
import com.wild.enums.user.UserVersioniEnum;

/**
 * 用户
 * 
 * @author Wild
 *
 */
public class WUser implements Serializable {

	private static final long serialVersionUID = 1632537799039372010L;

	private String tokenId;// 用户id
	private String WGCNum;// GC号
	private String NickName;// 用户昵称
	private int Sex;// 用户性别
	private String loginName;// 用户帐号
	private String password;// 用户密码
	private int Age;// 用户年龄
	private Date WDate;// 注册时间
	private StatusEnum WStatus;// 用户状态
	private UserVersioniEnum WSuperManager;// 用户角色
	private String validateCode;

	public WUser() {
		super();
	}

	public WUser(String tokenId, String wGCNum, String nickName, int sex, String loginName, String password, int age,
			Date wDate, StatusEnum wStatus, UserVersioniEnum wSuperManager, String validateCode) {
		super();
		this.tokenId = tokenId;
		WGCNum = wGCNum;
		NickName = nickName;
		Sex = sex;
		this.loginName = loginName;
		this.password = password;
		Age = age;
		setWDate(wDate);
		WStatus = wStatus;
		WSuperManager = wSuperManager;
		this.validateCode = validateCode;
	}

	public String getWGCNum() {
		return WGCNum;
	}

	public void setWGCNum(String wGCNum) {
		WGCNum = wGCNum;
	}

	public String getNickName() {
		return NickName;
	}

	public void setNickName(String nickName) {
		NickName = nickName;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAge() {
		return Age;
	}

	public void setAge(int age) {
		Age = age;
	}

	/*
	 * public void setWDate(String wDate) { SimpleDateFormat f = new
	 * SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); try { this.WDate =
	 * f.parse(wDate); } catch (ParseException e) { e.printStackTrace(); } }
	 */

	public StatusEnum getWStatus() {
		return WStatus;
	}

	public void setWStatus(StatusEnum wStatus) {
		WStatus = wStatus;
	}

	public UserVersioniEnum getWSuperManager() {
		return WSuperManager;
	}

	public void setWSuperManager(UserVersioniEnum wSuperManager) {
		WSuperManager = wSuperManager;
	}

	public String getValidateCode() {
		return validateCode;
	}

	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Date getWDate() {
		return WDate;
	}

	public void setWDate(Date wDate) {
		WDate = wDate;
	}
/*	public void setWDate(String wDate) {
		DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			this.WDate = f.parse(wDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}*/

}
