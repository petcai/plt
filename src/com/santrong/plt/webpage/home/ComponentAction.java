package com.santrong.plt.webpage.home;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.santrong.plt.log.Log;
import com.santrong.plt.util.AreaUtils;
import com.santrong.plt.util.MyUtils;
import com.santrong.plt.webpage.BaseAction;
import com.santrong.plt.webpage.course.dao.CourseDao;
import com.santrong.plt.webpage.course.entry.WeikeKnowledgeView;
import com.santrong.plt.webpage.course.resource.train.dao.KnowledgeDao;
import com.santrong.plt.webpage.course.resource.train.dao.TrainQuestionDao;
import com.santrong.plt.webpage.home.dao.AreaDao;
import com.santrong.plt.webpage.home.entry.AreaItem;
import com.santrong.plt.webpage.manage.superman.entry.KnowledgeItem;
import com.santrong.plt.webpage.manage.superman.entry.KnowledgeQuestionView;
import com.santrong.plt.webpage.school.dao.SchoolDao;
import com.santrong.plt.webpage.school.entry.SchoolItem;

/**
 * @author weinianjie
 * @date 2014年11月17日
 * @time 下午7:46:07
 */
@Controller
@RequestMapping("/component")
public class ComponentAction extends BaseAction {
	
	// 修改图片控件
	@RequestMapping("/change/cover")
	public String changeCover(){
		return "/component/changeCover";
	}
	
	// 选择学校控件
	@RequestMapping("/choice/school")
	public String choiceSchool(){
		AreaDao dao = new AreaDao();
		List<AreaItem> provinceList = dao.selectProvince();
		this.getRequest().setAttribute("provinceList", provinceList);
		return "/component/choiceSchool";
	}
	
	// 选择学校控件_根据区域请求内容
	@RequestMapping("/choice/school/data")
	@ResponseBody
	public String choiceSchoolData(String areaCode){
		if(areaCode == null) {
			areaCode = "";
		}
		String _code = AreaUtils.lostTail(areaCode);
		if(_code.length() % 2 == 1) {// 奇数补0成偶数
			_code += "0";
		}
		
		Gson gson = new Gson();
		if(_code.length() >= 6) {// 查学校
			SchoolDao dao = new SchoolDao();
			List<SchoolItem> dataList = dao.selectByAreaCode(areaCode);
			return gson.toJson(dataList);
		}else {// 查区域
			AreaDao dao = new AreaDao();
			List<AreaItem> dataList = dao.selectByFather(areaCode);	
			return gson.toJson(dataList);
		}
	}
	
	/**
	 * 初始化 试题/微课 绑定知识点的界面列表控件
	 * @return
	 */
	@RequestMapping(value="/bind/bingKnowledge")
	public String bingKnowledge() {
		try {
			HttpServletRequest request = getRequest();
			String questionId = request.getParameter("questionId");
			String courseId = request.getParameter("courseId");
			String knowledgeIds = request.getParameter("knowledgeIds");
			String unitId = request.getParameter("unitId");
			
			List<KnowledgeItem> knowledgeList = null;
					
			if (MyUtils.isNotNull(unitId)) {

				KnowledgeDao kDao = new KnowledgeDao();
				
				knowledgeList = kDao.selectByUnitId(unitId);
				// 修改
				// TODO hasIds == 1,表明 knowledgeIds不为空（已经绑定），即是没有修改年级、学科、单元的情况
				if (MyUtils.isNotNull(knowledgeIds)) {
					
					// TODO 试题绑定知识点
					if (MyUtils.isNotNull(questionId)) {
						TrainQuestionDao tqDao = new TrainQuestionDao();
						//  获取【已绑定】的知识点列表
						List<KnowledgeQuestionView> bingKnowledgeList = tqDao.selectKnowledge2QuestionByQId(questionId);
						
						//  获取【未绑定】的知识点列表
						if (bingKnowledgeList != null && bingKnowledgeList.size() > 0 && knowledgeList != null && knowledgeList.size() > 0) {
							int allCount = 0 ;
							for (KnowledgeQuestionView bingKItem : bingKnowledgeList) {
								for (KnowledgeItem allKItem : knowledgeList) {
									if (allKItem.getId().equals(bingKItem.getKnowledgeId())) {
										knowledgeList.remove(allCount);
										allCount = 0;
										break;
									}
									++ allCount;
								}
							}
						}
						request.setAttribute("bingKnowledgeList", bingKnowledgeList);//【已绑定】的知识点列表
					} else if (MyUtils.isNotNull(courseId)) {// TODO 课程、微课绑定知识点
						CourseDao courseDao = new CourseDao();
						//  获取【已绑定】的知识点列表
						List<WeikeKnowledgeView> bingKnowledgeList = courseDao.selectCourse2KnowledgeByCouseId(courseId);
						
						//  获取【未绑定】的知识点列表
						if (bingKnowledgeList != null && bingKnowledgeList.size() > 0 && knowledgeList != null && knowledgeList.size() > 0) {
							int allCount = 0 ;
							for (WeikeKnowledgeView bingKItem : bingKnowledgeList) {
								for (KnowledgeItem allKItem : knowledgeList) {
									if (allKItem.getId().equals(bingKItem.getKnowledgeId())) {
										knowledgeList.remove(allCount);
										allCount = 0;
										break;
									}
									++ allCount;
								}
							}
						}
						request.setAttribute("bingKnowledgeList", bingKnowledgeList);//【已绑定】的知识点列表
					}
				}
			}
			request.setAttribute("knowledgeList", knowledgeList);//【未绑定】的知识点列表
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return "/component/bingKnowledge";
	}
	
}
