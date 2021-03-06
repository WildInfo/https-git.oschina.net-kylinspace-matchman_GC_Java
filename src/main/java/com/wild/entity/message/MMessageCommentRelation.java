package com.wild.entity.message;

import java.io.Serializable;

/**
 * 热点，消息与评论关系
 * 
 * @author Wild
 *
 */
public class MMessageCommentRelation implements Serializable {

	private static final long serialVersionUID = -2001468437579196488L;

	private String MID;// 主键ID
	private String MInformationID;// 消息ID
	private String MKMessageID;// 热点ID
	private String MKUserID;// 用户ID
	private String MKUserGC;// 当前用户GC号
	private String MKCommentID;// 评论ID

	public MMessageCommentRelation(String mID, String mInformationID, String mKMessageID, String mKUserID,
			String mKUserGC, String mKCommentID) {
		super();
		MID = mID;
		MInformationID = mInformationID;
		MKMessageID = mKMessageID;
		MKUserID = mKUserID;
		MKUserGC = mKUserGC;
		MKCommentID = mKCommentID;
	}

	public MMessageCommentRelation() {
	}

	public String getMID() {
		return MID;
	}

	public void setMID(String mID) {
		MID = mID;
	}

	public String getMInformationID() {
		return MInformationID;
	}

	public void setMInformationID(String mInformationID) {
		MInformationID = mInformationID;
	}

	public String getMKMessageID() {
		return MKMessageID;
	}

	public void setMKMessageID(String mKMessageID) {
		MKMessageID = mKMessageID;
	}

	public String getMKUserID() {
		return MKUserID;
	}

	public void setMKUserID(String mKUserID) {
		MKUserID = mKUserID;
	}

	public String getMKUserGC() {
		return MKUserGC;
	}

	public void setMKUserGC(String mKUserGC) {
		MKUserGC = mKUserGC;
	}

	public String getMKCommentID() {
		return MKCommentID;
	}

	public void setMKCommentID(String mKCommentID) {
		MKCommentID = mKCommentID;
	}

}
