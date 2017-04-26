package com.wild.handler.message;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.wild.entity.message.MMessage;
import com.wild.entity.message.MMessageCommentRelation;
import com.wild.entity.user.WDetails;
import com.wild.entity.user.WUserDetailsRelation;
import com.wild.enums.message.GetStatusEnum;
import com.wild.enums.message.StatusEnum;
import com.wild.service.message.MCommentService;
import com.wild.service.user.WUserService;
import com.wild.utils.UUIDUtil;

/**
 * 热点消息
 * 
 * @author Wild
 *
 */
@Controller
@RequestMapping("/mmessage")
public class MMessageHandler {
	@Autowired
	private MCommentService commentService;

	@Autowired
	private WUserService userService;

	/**
	 * 发布热点
	 * 
	 * @param out
	 * @param session
	 * @param request
	 */
	@RequestMapping("/insertMessage")
	public void insertMessage(PrintWriter out, HttpSession session, HttpServletRequest request) {
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		String tokenId = request.getParameter("tokenId");// 用户id
		String currency = request.getParameter("currency");// 游戏币数量
		String contents = "";// 热点内容
		try {
			contents = new String(request.getParameter("contents").getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (StringUtils.isNotBlank(tokenId) && StringUtils.isNotBlank(currency) && StringUtils.isNotBlank(contents)) {

			MMessage message = new MMessage();

			message.setMID(UUIDUtil.createUUID());
			message.setMContent(contents);
			message.setMGrade(Integer.parseInt(currency));
			message.setMDate(new Date());
			message.setMGetStatus(GetStatusEnum.unreceived);
			message.setMStatus(StatusEnum.normal);

			int messageResult = commentService.insertMessage(message);// 插入热点
			MMessageCommentRelation commentRelation = new MMessageCommentRelation(UUIDUtil.createUUID(), null,
					message.getMID(), tokenId, null);
			int commentRelationResult = commentService.insertIMC(commentRelation);// 添加热点与用户关系
			if ((messageResult > 0) && (commentRelationResult > 0)) {
				MMessage messageJson = commentService.selectMessage(message);// 查询热点消息

				map2.put("messageinfo", messageJson);
				map2.put("tokenId", message.getMID());

				map.put("result", 1);
				map.put("desc", "添加成功!");
				map.put("data", map2);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			} else {
				map.put("result", 0);
				map.put("desc", "添加失败!");
				map.put("data", map2);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			}
		}
	}

	/**
	 * 查询游戏币数量
	 * 
	 * @param out
	 * @param session
	 * @param request
	 */
	@RequestMapping("/selectCurrey")
	public void selectCurrey(PrintWriter out, HttpSession session, HttpServletRequest request) {
		Gson gson = new Gson();
		WUserDetailsRelation detailsRelation = new WUserDetailsRelation();
		WDetails details = new WDetails();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		String tokenId = request.getParameter("tokenId");// 用户id
		if (StringUtils.isNotBlank(tokenId)) {
			detailsRelation.setWKUserID(tokenId);
		}

		List<WUserDetailsRelation> relation = userService.userDetilsById(detailsRelation);// 根据用户id查询出详情
		if (relation.size() > 0) {
			details.setWID(relation.get(0).getWKDetailsID());// 用户详细id

			List<WDetails> updetails = userService.selectDetils(details);
			if (updetails.size() > 0) {

				map2.put("currency", updetails.get(0).getWCurrency());

				map.put("result", 1);
				map.put("desc", "查询成功!");
				map.put("data", map2);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			} else {
				map.put("result", 0);
				map.put("desc", "查询失败!");
				map.put("data", map2);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			}
		} else {
			map.put("result", 0);
			map.put("desc", "查询失败!");
			map.put("data", map2);

			list.add(map);

			out.println(gson.toJson(list));
			out.flush();
			out.close();
		}
	}

	/**
	 * 根据user查看消息列表
	 * 
	 * @param out
	 * @param session
	 * @param request
	 */
	@RequestMapping("/messageUser")
	public void messageRelation(PrintWriter out, HttpSession session, HttpServletRequest request) {
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<>();
		List<Map<String, Object>> list2 = new ArrayList<>();
		MMessageCommentRelation messageRelation = new MMessageCommentRelation();
		String tokenId = request.getParameter("tokenId");// 用户id
		if (StringUtils.isNotBlank(tokenId)) {
			messageRelation.setMKUserID(tokenId);
			List<MMessageCommentRelation> messageUser = commentService.messageRelation(messageRelation);// 添加热点与用户关系
			if ((messageUser.size() > 0)) {
				for (int i = 0; i < messageUser.size(); i++) {
					MMessage message = new MMessage();
					message.setMID(messageUser.get(i).getMKMessageID());
					MMessage messageDetails = commentService.selectMessage(message);// 根据热点id查询热点
					map3.put("message" + i + "", messageDetails);
				}
				map.put("result", 1);
				map.put("desc", "查询成功!");
				map.put("data", list2);
				list2.add(map3);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			} else {
				map.put("result", 0);
				map.put("desc", "查询失败!");
				map.put("data", map2);

				list.add(map);

				out.println(gson.toJson(list));
				out.flush();
				out.close();
			}
		}
	}

}
