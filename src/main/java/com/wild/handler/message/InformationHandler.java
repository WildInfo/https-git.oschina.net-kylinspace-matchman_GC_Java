package com.wild.handler.message;

import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.wild.entity.message.IInformation;
import com.wild.entity.message.MComment;
import com.wild.entity.message.MMessageCommentRelation;
import com.wild.entity.user.FriendShip;
import com.wild.entity.user.WUser;
import com.wild.enums.message.StatusEnum;
import com.wild.service.message.InformationService;
import com.wild.service.message.MCommentService;
import com.wild.service.user.FriendShipService;
import com.wild.utils.SessionAttribute;
import com.wild.utils.UUIDUtil;

@Controller
@RequestMapping("/information")
public class InformationHandler {
	@Autowired
	private InformationService informationService;
	
	@Autowired
	private MCommentService mCommentService;
	
	@Autowired
	private FriendShipService friendShipService;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	/**
	 * 查询公开信息
	 * @param map
	 * @param out
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getInfos")
	public void getPublicNews(ModelMap map,PrintWriter out,HttpServletRequest request,HttpServletResponse response){
		List<IInformation> infos = informationService.getPublicNews("湖南工学院");
		Gson gson = new Gson();
		out.print(gson.toJson(infos));
		out.flush();
		out.close();
	}
	
	/**
	 * 插入公开信息
	 */
	@RequestMapping(value="/insertInfo",method=RequestMethod.GET)
	public void insertInfo(ModelMap map,PrintWriter out,HttpServletRequest request,HttpSession session){
		String iContent = request.getParameter("iContent");
		String iImage = request.getParameter("iImage");
		String address = request.getParameter("address");
		String id = UUIDUtil.createUUID();
		WUser user = (WUser) session.getAttribute(SessionAttribute.USERLOGIN);
		try {
			String userid = user.getWGCNum();
			IInformation info = new IInformation(id, iContent, iImage, address, sdf.parse(sdf.format(new Date())),
					StatusEnum.normal, userid, "", "");
			System.out.println(info);
			int result = informationService.insertPublicNews(info);
			Map<String,Object> json = new HashMap<String,Object>();
			Gson gson = new Gson();
			json.put("result", result);
			if(result>0){
				json.put("desc", "发布消息成功");
			}else{
				json.put("desc", "发布消息失败");
			}
			out.print(gson.toJson(json));
			out.flush();
			out.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询评论
	 */
	@RequestMapping(value="/getComments",method=RequestMethod.GET)
	public void getComments(@RequestParam("iid")String iid, ModelMap map,PrintWriter out,HttpServletRequest request){
		IInformation information = new IInformation();
		information.setIID(iid);
		List<MComment> infos = mCommentService.getComments(information);
		out.print(infos);
		out.flush();
		out.close();
	}
	
	/**
	 * 插入评论
	 * @param map
	 * @param out
	 */
	@RequestMapping(value="/insertComment",method=RequestMethod.GET)
	public void insertComment(ModelMap map,PrintWriter out,HttpServletRequest request){
		String publishUser = request.getParameter("publishUser");
		String targetUser = request.getParameter("targetUser");
		String content = request.getParameter("content");
		String parent = request.getParameter("parent");
		String parentType = request.getParameter("parentType");
		String id = UUIDUtil.createUUID();
		try {
			MComment comment = new MComment(id, publishUser, targetUser, content, 0, 
						sdf.parse(sdf.format(new Date())), parent, parentType, "", "", "");
			int result = mCommentService.insertComment(comment);
			MMessageCommentRelation mcr = new MMessageCommentRelation("", parent, "", comment.getMOwnerUser(), comment.getMID());
			Map<String, Object> json = new HashMap<String, Object>();
			Gson gson = new Gson();
			json.put("result", result);
			if(result>0){
				int r = mCommentService.insertIMC(mcr);
				if(r>0)
					json.put("desc", "插入评论成功");
				else
					json.put("desc", "插入评论失败");
			}else{
				json.put("desc", "插入评论失败");
			}
			out.println(gson.toJson(json));
			out.flush();
			out.close();
		} catch (ParseException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * 获取信息详情
	 * @param map
	 * @param out
	 * @param request
	 */
	@RequestMapping(value="/getInfoDetail",method=RequestMethod.GET)
	public void getInfoDetail(ModelMap map,PrintWriter out,HttpServletRequest request,HttpSession session){
		WUser user = (WUser) session.getAttribute(SessionAttribute.USERLOGIN);  //当前登陆用户
		System.out.println(user);
		String iid = request.getParameter("IID");  //消息ID
		String userid = request.getParameter("IUserId");   //发的消息的用户ID
		List<FriendShip> friends = friendShipService.getFriends(userid);   //要查看的消息的用户的好友
		IInformation information = new IInformation();
		information.setIID(iid);
		information.setIUserId(userid);
		//判断当前用户是否是要查看的信息的用户的好友
		boolean isFriend = false;
		List<IInformation> infos;
		for(FriendShip fs:friends){
			if(fs.getFKFriendID().equals(user.getWGCNum())){
				isFriend = true;
				break;
			}
		}
		if(userid.equals(user.getWGCNum()) || isFriend){//该条信息是否是当前用户发送的或查看的用户是该条消息的好友
			infos = informationService.getInfoDetails(information);
		}else{//说明是陌生人
			infos = informationService.getInfoDetailsByStrenger(information);
		}
		out.print(infos);
		out.flush();
		out.close();
	}
	
}
