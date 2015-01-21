package com.santrong.plt.webpage.course.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.santrong.plt.criteria.Statement;
import com.santrong.plt.log.Log;
import com.santrong.plt.opt.ThreadUtils;
import com.santrong.plt.util.AreaUtils;
import com.santrong.plt.util.BeanUtils;
import com.santrong.plt.util.MyUtils;
import com.santrong.plt.webpage.BaseDao;
import com.santrong.plt.webpage.course.entry.CourseItem;
import com.santrong.plt.webpage.course.entry.WeikeDetailItem;
import com.santrong.plt.webpage.course.entry.WeikeQuery;

/**
 * @author weinianjie
 * @date 2015年1月19日
 * @time 下午3:22:49
 */
public class WeikeDao extends BaseDao {
	
	/**
	 * 根据微课Id查询微课
	 * @param courseId
	 * @return
	 */
	public WeikeDetailItem selectByCourseId(String courseId) {
		try {
			WeikeMapper mapper = this.getMapper(WeikeMapper.class);
			if (mapper != null) {
				return mapper.selectByCourseId(courseId);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	/**
	 * 根据具体搜索条件查询课程
	 * @param query
	 * @return
	 */
	public 	List<CourseItem> selectByQuery(WeikeQuery query) {
		List<CourseItem> list = new ArrayList<CourseItem>();
		
		try{
			Statement criteria = new Statement("course", "a");
			criteria.setFields("a.*");
			criteria.ljoin("subject", "b", "a.subjectId", "b.id");
			criteria.ljoin("grade", "c", "a.gradeId", "c.id");
			criteria.ljoin("user", "d", "a.ownerId", "d.id");
			criteria.ljoin("school", "e", "d.schoolId", "e.id");
			
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("a.courseName", "?")));
				criteria.setStringParam("%" + query.getKeywords() + "%");
			}
			// 科目条件
			if(MyUtils.isNotNull(query.getSubjectEnName())) {
				criteria.where(eq("b.subjectEnName", "?"));
				criteria.setStringParam(query.getSubjectEnName());
			}
			// 类别条件
			if(MyUtils.isNotNull(query.getGradeEnName())) {
				criteria.where(eq("c.gradeEnName", "?"));
				criteria.setStringParam(query.getGradeEnName());
			}
			// 年级条件
			if(MyUtils.isNotNull(query.getLevelEnName())) {
				criteria.where(eq("c.LevelEnName", "?"));
				criteria.setStringParam(query.getLevelEnName());
			}
			// 是否含有直播
			if(query.isLive()) {
				criteria.where(eq("a.live", "1"));
			}
			// 所属区域
			if(MyUtils.isNotNull(query.getAreaCode())) {
				criteria.where(like("e.areaCode", "?"));
				criteria.setStringParam(AreaUtils.lostTail(query.getAreaCode()) + "%");
			}
			// 所属用户
			if(MyUtils.isNotNull(query.getUserId())) {
				criteria.where(eq("a.ownerId", "?"));
				criteria.setStringParam(query.getUserId());
			}
			// 所属学校
			if(MyUtils.isNotNull(query.getSchoolId())) {
				criteria.where(eq("e.id", "?"));
				criteria.setStringParam(query.getSchoolId());
			}
			// 状态，等于0是未发布状态
			if(query.getStatus() > 0) {
				criteria.where(eq("a.status", "?"));
				criteria.setIntParam(query.getStatus());
			}else{
				// 未删除
				criteria.where(ne("a.status", "-1"));				
			}
			// 微课课程
			criteria.where(eq("a.courseType", "1"));
			
			// 排序
			if(!StringUtils.isNullOrEmpty(query.getOrderBy())) {
				if("desc".equalsIgnoreCase(query.getOrderRule())) {
					criteria.desc("a." + query.getOrderBy());
				}else {
					criteria.asc("a." + query.getOrderBy());
				}
			}
			
			// 分页
			criteria.limit(query.getLimitBegin(), query.getLimitEnd());
			
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			while(rs.next()){
				CourseItem item = new CourseItem();
				BeanUtils.Rs2Bean(rs, item);
				list.add(item);
			}
			
		}catch(Exception e){
			Log.printStackTrace(e);
		}
		
		return list;
	}	
	
	/**
	 * 根据具体搜索条件查询课程总数
	 * @param query
	 * @return
	 */
	public 	int selectCountByQuery(WeikeQuery query) {
		int count = 0;
		
		try{
			Statement criteria = new Statement("course", "a");
			criteria.setFields("count(*) cn");
			criteria.ljoin("subject", "b", "a.subjectId", "b.id");
			criteria.ljoin("grade", "c", "a.gradeId", "c.id");
			criteria.ljoin("user", "d", "a.ownerId", "d.id");
			criteria.ljoin("school", "e", "d.schoolId", "e.id");
			
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("a.courseName", "?")));
				criteria.setStringParam("%" + query.getKeywords() + "%");
			}
			// 科目条件
			if(MyUtils.isNotNull(query.getSubjectEnName())) {
				criteria.where(eq("b.subjectEnName", "?"));
				criteria.setStringParam(query.getSubjectEnName());
			}
			// 类别条件
			if(MyUtils.isNotNull(query.getGradeEnName())) {
				criteria.where(eq("c.gradeEnName", "?"));
				criteria.setStringParam(query.getGradeEnName());
			}
			// 年级条件
			if(MyUtils.isNotNull(query.getLevelEnName())) {
				criteria.where(eq("c.LevelEnName", "?"));
				criteria.setStringParam(query.getLevelEnName());
			}
			// 是否含有直播
			if(query.isLive()) {
				criteria.where(eq("a.live", "1"));
			}
			// 所属区域
			if(MyUtils.isNotNull(query.getAreaCode())) {
				criteria.where(like("e.areaCode", "?"));
				criteria.setStringParam(AreaUtils.lostTail(query.getAreaCode()) + "%");
			}
			// 所属用户
			if(MyUtils.isNotNull(query.getUserId())) {
				criteria.where(eq("a.ownerId", "?"));
				criteria.setStringParam(query.getUserId());
			}				
			// 所属学校
			if(MyUtils.isNotNull(query.getSchoolId())) {
				criteria.where(eq("e.id", "?"));
				criteria.setStringParam(query.getSchoolId());
			}
			// 状态，等于0是未发布状态
			if(query.getStatus() > 0) {
				criteria.where(eq("a.status", "?"));
				criteria.setIntParam(query.getStatus());
			}else{
				// 未删除
				criteria.where(ne("a.status", "-1"));				
			}
			// 微课课程
			criteria.where(eq("a.courseType", "1"));
			
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			rs.next();
			count = rs.getInt("cn");
			
		}catch(Exception e){
			Log.printStackTrace(e);
		}
		
		return count;
	}
}