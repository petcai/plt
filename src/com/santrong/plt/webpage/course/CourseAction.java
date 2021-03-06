package com.santrong.plt.webpage.course;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.mysql.jdbc.StringUtils;
import com.santrong.plt.log.Log;
import com.santrong.plt.opt.ParamHelper;
import com.santrong.plt.opt.ThreadUtils;
import com.santrong.plt.opt.area.AreaEntry;
import com.santrong.plt.opt.grade.GradeDefine;
import com.santrong.plt.opt.grade.GradeLevelEntry;
import com.santrong.plt.system.Global;
import com.santrong.plt.util.MyUtils;
import com.santrong.plt.webpage.BaseAction;
import com.santrong.plt.webpage.course.dao.ChapterDao;
import com.santrong.plt.webpage.course.dao.CollectCourseDao;
import com.santrong.plt.webpage.course.dao.CommentDao;
import com.santrong.plt.webpage.course.dao.CourseDao;
import com.santrong.plt.webpage.course.dao.OrderDao;
import com.santrong.plt.webpage.course.entry.ChapterAndResourceEntry;
import com.santrong.plt.webpage.course.entry.ChapterDetailView;
import com.santrong.plt.webpage.course.entry.ChinaBankPayGateParams;
import com.santrong.plt.webpage.course.entry.CollectionItem;
import com.santrong.plt.webpage.course.entry.CommentItem;
import com.santrong.plt.webpage.course.entry.CommentUserView;
import com.santrong.plt.webpage.course.entry.CourseDetailView;
import com.santrong.plt.webpage.course.entry.CourseItem;
import com.santrong.plt.webpage.course.entry.CourseQuery;
import com.santrong.plt.webpage.course.entry.OrderItem;
import com.santrong.plt.webpage.course.entry.ResourceEntry;
import com.santrong.plt.webpage.course.entry.ResourceType;
import com.santrong.plt.webpage.course.resource.live.dao.LiveDao;
import com.santrong.plt.webpage.course.resource.live.entry.LiveItem;
import com.santrong.plt.webpage.home.dao.GradeDao;
import com.santrong.plt.webpage.home.dao.SubjectDao;
import com.santrong.plt.webpage.home.entry.GradeView;
import com.santrong.plt.webpage.home.entry.SubjectItem;
import com.santrong.plt.webpage.school.dao.SchoolDao;
import com.santrong.plt.webpage.school.entry.SchoolItem;
import com.santrong.plt.webpage.school.entry.SchoolQuery;
import com.santrong.plt.webpage.teacher.dao.UserDao;
import com.santrong.plt.webpage.teacher.entry.UserCourseView;
import com.santrong.plt.webpage.teacher.entry.UserItem;
import com.santrong.plt.webpage.teacher.entry.UserQuery;

/**
 * @author weinianjie
 * @date 2014年10月10日
 * @time 下午5:01:51
 */
@Controller
@RequestMapping("/course")
public class CourseAction extends BaseAction {
	
	/**
	 * 课程首页
	 * @return
	 */
	@RequestMapping(value="")
	public String index() {
		return catagory("all", "all");
	}
	
	
	/**
	 * 按年级搜索的课程
	 * @param grade
	 * @return
	 */
	@RequestMapping("/{grade}")
	public String catagory(@PathVariable String grade) {
		return catagory(grade, "all");
	}
	
	/**
	 * 按年级和科目搜索的课程
	 * @param grade
	 * @param subject
	 * @return
	 */
	@RequestMapping("/{grade}/{subject}")
	public String catagory(@PathVariable String grade, @PathVariable String subject) {
		HttpServletRequest request = getRequest();
		AreaEntry area = (AreaEntry)(request.getSession().getAttribute(Global.SessionKey_Area));
		
		// 条件-科目
		SubjectDao subjectDao = new SubjectDao();
		List<SubjectItem> subjectList = null;
		if(grade.equals("all")) {
			subjectList = subjectDao.selectAll();
		}else {
			subjectList = subjectDao.selectByGradeEnName(grade);
		}
		
		// 条件-类别
		GradeDao gradeDao = new GradeDao();
		List<GradeView> gradeList = null;
		if(subject.equals("all")) {
			gradeList = gradeDao.selectGrade();
		}else {
			gradeList = gradeDao.selectGradeBySubjectEnName(subject);
		}
		
		// 条件-年级
		List<GradeLevelEntry> levelList = null;
		if(!grade.equals("all")) {
			levelList = GradeDefine.getByGradeEnName(grade).getGradeLevelList();
		}		
		
		
		
		// 获取参数
		ParamHelper param = new ParamHelper();
		param.init(request);
		
		int page = this.getIntParameter("page");
		if(page == 0) {
			page = 1;
		}

		// 构建查询条件
		CourseQuery courseQuery = new CourseQuery();
		courseQuery.setStatus(CourseItem.Status_Publish);
		courseQuery.setPageNum(page);
		courseQuery.setAreaCode(area.getCityCode());
		if(MyUtils.isNotNull(subject) && !subject.equals("all")) {
			courseQuery.setSubjectEnName(subject);
		}
		if(MyUtils.isNotNull(grade) && !grade.equals("all")) {
			courseQuery.setGradeEnName(grade);
		}
		if(MyUtils.isNotNull(param.getKeyword())) {
			courseQuery.setKeywords(param.getKeyword());
		}
		courseQuery.setLevelEnName(param.getParamByStart("level"));
		courseQuery.setLive(param.getParamContain("live"));
	
		
		if(MyUtils.isNotNull(param.getOrderBy())) {
			courseQuery.setOrderBy(param.getOrderBy());
			courseQuery.setOrderRule(param.getOrderRule());
		}
		
		// 查询课程列表
		CourseDao courseDao = new CourseDao();
		courseQuery.setCount(courseDao.selectCountByQuery(courseQuery));
		List<CourseItem> courseList = courseDao.selectByQuery(courseQuery);
		
		// 学校列表
		SchoolDao schoolDao = new SchoolDao();
		SchoolQuery schoolQuery = new SchoolQuery();
		schoolQuery.setAreaCode(area.getCityCode());
		schoolQuery.setPageSize(4);
		List<SchoolItem> schoolList = schoolDao.selectByQuery(schoolQuery);
		
		// 老师列表
		UserDao userDao = new UserDao();
		UserQuery userQuery = new UserQuery();
		userQuery.setAreaCode(area.getCityCode());
		userQuery.setRole(UserItem.Role_Teacher);
		userQuery.setPageSize(4);
		List<UserItem> teacherList = userDao.selectByQuery(userQuery);
		
		request.setAttribute("query", courseQuery);
		
		// 参数组
		request.setAttribute("grade", grade);
		request.setAttribute("subject", subject);
//		request.setAttribute("param", param);
		ThreadUtils.setParam(param);
		
		// 搜索条件
		request.setAttribute("subjectList", subjectList);
		request.setAttribute("gradeList", gradeList);
		request.setAttribute("levelList", levelList);		
		
		// 主内容
		request.setAttribute("courseList", courseList);
		request.setAttribute("schoolList", schoolList);
		request.setAttribute("teacherList", teacherList);
		
		return "course/index";
	}
	
	/**
	 * 课程详细页面
	 * @param id
	 * @return
	 */
	@RequestMapping("/{id}.html")
	public String detail(@PathVariable String id) {
		
		// 课程详细
		CourseDao courseDao = new CourseDao();
		CourseDetailView course = courseDao.selectDetailById(id);
		if(course == null) {
			return "404";
		}

		// 课程章节
		ChapterDao chapterDao = new ChapterDao();
		List<ChapterAndResourceEntry> resourceList = chapterDao.selectByCourseId(course.getId());
		
		Map<String, ChapterDetailView> chapterMap = new LinkedHashMap<String, ChapterDetailView>();
		for(ChapterAndResourceEntry cr : resourceList) {
			ChapterDetailView cdv = chapterMap.get(cr.getId());
			if(cdv == null) {
				cdv = new ChapterDetailView();
				cdv.setRemark(cr.getRemark());
			}
			if(cdv.getResourceList() == null) {
				cdv.setResourceList(new ArrayList<ResourceEntry>());
			}
			if(cr.getResourceType() > 0) {
				ResourceEntry resource = new ResourceEntry();
				resource.setTitle(cr.getTitle());
				resource.setId(cr.getResourceId());
				resource.setType(cr.getResourceType());
				
				/*
				 * 思路：
				 * 1、如果是直播的话，就根据直播ID获取开始时间和时长，先用开始时间和当前时间进行比较；
				 * 	    结果等于-1，直播状态标识为-1：未开始；
				 *	    结果等于0，直播状态标识为0：正在直播中；
				 * 2、结果等于1，再把开始时间和时长相加后，再和当前时间进行比较；
				 *	    结果等于-1或0，直播状态标识为0：正在直播中；
				 *	    结果等于1，直播状态标识为0：已经结束；
				 */
				if (cr.getResourceType() == ResourceType.Type_Live) {
					LiveDao liveDao = new LiveDao();
					LiveItem liveItem = liveDao.selectById(cr.getResourceId());
					if (liveItem != null && liveItem.getBeginTime() != null) {
						int status = -1;
						//先用开始时间和当前时间进行比较
						//比较两个日期时间的大小 ,-1:str1小于str2; 0:str1等于str2; 1:str1大于str2;时间都要精确到分
						int result = MyUtils.compareTo(MyUtils.getNowDate(MyUtils.DF_yyyy_MM_dd_HH_mm), 
								MyUtils.dateToString(liveItem.getBeginTime(), MyUtils.DF_yyyy_MM_dd_HH_mm), MyUtils.DF_yyyy_MM_dd_HH_mm);
						if (result == -1) {
							status = -1;
						} else if (result == 0) {
							status = 0;
						} else {
							//结果等于1，再把开始时间和时长相加后，再和当前时间进行比较；
							int result2 = MyUtils.compareTo(MyUtils.getNowDate(MyUtils.DF_yyyy_MM_dd_HH_mm), 
									MyUtils.dateToString(MyUtils.addDateMinute(liveItem.getBeginTime(), liveItem.getDuration()),MyUtils.DF_yyyy_MM_dd_HH_mm), MyUtils.DF_yyyy_MM_dd_HH_mm);
							status = (result2 < 0) ? 0 : 1;
						}
						resource.setStatus(status);//-1：未开始，0：正在直播中，1：已经结束
					}
				}
				
				cdv.getResourceList().add(resource);
			}
			chapterMap.put(cr.getId(), cdv);
		}
		
		List<ChapterDetailView> chapterList = new ArrayList<ChapterDetailView>();
		for(Iterator<Map.Entry<String, ChapterDetailView>> it = chapterMap.entrySet().iterator();it.hasNext();){
            Entry<String, ChapterDetailView> entry = (Entry<String, ChapterDetailView>)it.next();
            chapterList.add(entry.getValue());
        }  
		course.setChapterDetailList(chapterList);
		
		// 课程评价
		CommentDao commentDao = new CommentDao();
		List<CommentUserView> commentList = commentDao.selectByCourseId(course.getId());
		course.setCommentList(commentList);
		
		
		// 课程所有者的信息
		UserDao userDao = new UserDao();
		UserCourseView teacher = userDao.selectTeacherByUserId(course.getOwnerId());
		
		// 老师名下其他课程
		
		// 是否已购买
		boolean hasBuy = false;
		if(this.currentUser() != null) {
			OrderDao orderDao = new OrderDao();
			OrderItem order = orderDao.selectByCourseIdAndUserId(course.getId(), this.currentUser().getId());
			if(order != null && order.getStatus() == OrderItem.Status_Pay) {
				hasBuy = true;
			}
		}
		
		HttpServletRequest request = getRequest();
		request.setAttribute("course", course);
		request.setAttribute("teacher", teacher);
		request.setAttribute("hasBuy", hasBuy);
		
		return "course/detail";
	}
	
	/**
	 * 课程详细页面-课程评价回复功能
	 * @author huangweihua
	 * @param courseId, remark
	 * @return
	 */
	@RequestMapping(value="/comment", method=RequestMethod.POST)
	public String postNewReply(String courseId ,String remark) {
		
		if(StringUtils.isNullOrEmpty(courseId) || StringUtils.isNullOrEmpty(remark) ) {
			return this.redirect("/course/" + courseId + ".html#course_comment");
		}
		/*课程评论页面，判断用户是否登录改成使用BaseAction中的currentUser方法，拿到user后判断user是否为空来判断用户登录状态*/
		// 获取当前用户对象信息
		UserItem user = this.currentUser();
		if (user == null) {
			// 没登陆
			return this.redirect("/account/login");
		}
		
		// 判断该用户是否已经买了该课程，如果没有付款成功购买，不让评论该课程
		OrderDao orderDao = new OrderDao();
		if (orderDao.existsByCourseIdAndUserId(courseId, user.getId())) {
			//打开事务
			ThreadUtils.beginTranx();
			
			// 往课程评论表插入一条记录
			CommentDao commentDao = new CommentDao();
			CommentItem commentItem = new CommentItem();
			commentItem.setId(MyUtils.getGUID());
			commentItem.setUserId(user.getId());
			commentItem.setCourseId(courseId);
			commentItem.setRemark(remark);
			commentItem.setCts(new Date());
			commentItem.setUts(new Date());
			commentDao.insert(commentItem);
			
			// 往课程评论表插入一条记录成功后，修改课程主表course中的课程评论数量commentCount，自动加1
			CourseDao courseDao = new CourseDao();
			courseDao.addComment(courseId);
			
			//关闭事务
			ThreadUtils.commitTranx();
		}
		
		return this.redirect("/course/" + courseId + ".html#course_comment");
	}
	
	/**
	 * 1、点击收藏,修改该课程的收藏数量,自动加1;<br>
	 * 2、弹出提示收藏成功，ajax异步，不判断重复收藏
	 * @author huangweihua
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/coll_course", method=RequestMethod.POST)
	@ResponseBody
	public String collectionCourse(String courseId) {
		try {
			// 获取当前用户对象信息
			UserItem user = this.currentUser();
			if (user == null) {
				// 没登陆，注意：异步的时候才这样子写，jquery对返回的结果作了判断
				return "loginPage";
			}
			
			CollectCourseDao collectCourseDao = new CollectCourseDao();
			// 如果没搜藏过
			if(!collectCourseDao.exists(courseId, user.getId())) {
				ThreadUtils.beginTranx();
				// 往课程收藏表插入一条记录
				CollectionItem collection = new CollectionItem();
				collection.setUserId(user.getId());
				collection.setCourseId(courseId);
				collection.setCts(new Date());
				collectCourseDao.insert(collection);

				// 点击收藏，修改该课程的收藏数量,自动加1
				CourseDao courseDao = new CourseDao();
				courseDao.addCollection(courseId);
				ThreadUtils.commitTranx();
			}

		} catch (Exception e) {
			ThreadUtils.rollbackTranx();
			Log.printStackTrace(e);
			return FAIL;
		}
		return SUCCESS;
	}
	
	/**
	 * 购买
	 * @param courseId
	 * @return
	 */
	@RequestMapping(value="/buy", method=RequestMethod.POST)
	@ResponseBody
	public String buyCourse(String courseId) {
		try {
			// 获取当前用户对象信息
			UserItem user = this.currentUser();
			if (user == null) {
				// 没登陆，注意：异步的时候才这样子写，jquery对返回的结果作了判断
				return "loginPage";
			}
			
			CourseDao courseDao = new CourseDao();
			CourseItem course = courseDao.selectById(courseId);
			if(course == null) {
				return "课程不存在";
			}
			
			if (course.getLimitCount() <= course.getSaleCount()) {
				return "对不起，亲！该课程已经达到限购人数了！请您去购买其他老师的课程吧！";
			}
			
			// 策略：购买过的，提示已经购买；购买过但是未付款的，激活原来的购买；购买过但是取消了的，激活原来的购买；付款成功购买量才自增。
			OrderDao orderDao = new OrderDao();
			OrderItem order = orderDao.selectByCourseIdAndUserId(courseId, user.getId());
			if(order != null) {// 已经购买
				if(order.getStatus() == OrderItem.Status_Pay) {// 已经付款
					return "已经购买过了";
				}else {// 未曾付款，包括超时被取消了的订单，直接删除
					orderDao.delete(order.getId());
				}
			}
			
			order = new OrderItem();
			order.setId(MyUtils.getGUID());
			order.setUserId(user.getId());
			order.setCourseId(courseId);
			order.setPrice(course.getPrice());
			order.setStatus(OrderItem.Status_Notpay);
			order.setCts(new Date());
			order.setUts(new Date());
			if(orderDao.insert(order) <= 0) {
				return "订单创建失败";
			}
			
			// 构造网银在线支付参数
			ChinaBankPayGateParams chinaBank = new ChinaBankPayGateParams();
			chinaBank.setV_mid("23142561");// 商户号
			chinaBank.setV_oid(order.getId());// 订单号
			chinaBank.setV_amount("0.01");// 价格
			chinaBank.setV_moneytype("CNY");// 货币类型
			chinaBank.setV_url("http://" + Global.PltDomain + "/pay/chinabank/receive");// 跳转地址
			chinaBank.setV_md5info(chinaBank.calMd5("weinianjieblacksheepwall"));
			chinaBank.setV_rcvname(user.getUsername());// 订单人
			chinaBank.setRemark1(user.getId());// 订单人ID
			
			Gson gson = new Gson();
			return gson.toJson(chinaBank);
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return FAIL;
	}	
}
