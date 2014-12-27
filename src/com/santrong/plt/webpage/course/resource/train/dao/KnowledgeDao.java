package com.santrong.plt.webpage.course.resource.train.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.StringUtils;
import com.santrong.plt.criteria.Statement;
import com.santrong.plt.log.Log;
import com.santrong.plt.opt.ThreadUtils;
import com.santrong.plt.util.BeanUtils;
import com.santrong.plt.util.MyUtils;
import com.santrong.plt.webpage.BaseDao;
import com.santrong.plt.webpage.course.resource.train.entry.KnowledgeItem;
import com.santrong.plt.webpage.course.resource.train.entry.KnowledgeGradeView;
import com.santrong.plt.webpage.course.resource.train.entry.KnowledgeQuery;

/**
 * @author weinianjie
 * @date 2014年12月25日
 * @time 下午4:38:20
 */
public class KnowledgeDao extends BaseDao {
	
	//TODO 新增知识点
	public boolean insert(KnowledgeItem knowledgeItem) {
		try {
			KnowledgeMapper mapper = this.getMapper(KnowledgeMapper.class);
			if (mapper != null) {
				return mapper.insert(knowledgeItem) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}
	
	//TODO 修改知识点
	public boolean update(KnowledgeItem knowledgeItem) {
		try {
			KnowledgeMapper mapper = this.getMapper(KnowledgeMapper.class);
			if (mapper != null) {
				return mapper.update(knowledgeItem) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}
	
	//TODO 删除知识点
	public boolean deleteById(String id) {
		try {
			KnowledgeMapper mapper = this.getMapper(KnowledgeMapper.class);
			if (mapper != null) {
				return mapper.deleteById(id) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}
	
	//TODO 查询知识点
	public KnowledgeItem selectById(String id) {
		try {
			KnowledgeMapper mapper = this.getMapper(KnowledgeMapper.class);
			if (mapper != null) {
				return mapper.selectById(id);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	//TODO 更新知识点
	
	//TODO 知识点分页列表
	public List<KnowledgeGradeView> selectByQuery(KnowledgeQuery query) {
		List<KnowledgeGradeView> list = new ArrayList<KnowledgeGradeView>();
		try {
			Statement criteria = new Statement("knowledge","a");
			criteria.setFields("a.*,b.subjectName,b.subjectEnName,c.gradeName,c.levelName,c.gradeEnName,c.levelEnName");
			criteria.ljoin("subject", "b", "a.subjectId", "b.id");
			criteria.ljoin("grade", "c", "a.gradeId", "c.id");
			
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("a.knowledgeName", "?")));
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
				criteria.where(eq("c.levelEnName", "?"));
				criteria.setStringParam(query.getLevelEnName());
			}
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
			while (rs.next()) {
				KnowledgeGradeView item = new KnowledgeGradeView();
				BeanUtils.Rs2Bean(rs, item);
				list.add(item);
			}
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return list;
	}
	
	//TODO 知识点分页总数
	public int selectCountByQuery(KnowledgeQuery query) {
		int count = 0;
		try {
			Statement criteria = new Statement("knowledge","a");
			criteria.setFields("count(*) cn");
			criteria.ljoin("subject", "b", "a.subjectId", "b.id");
			criteria.ljoin("grade", "c", "a.gradeId", "c.id");
			
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("a.knowledgeName", "?")));
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
				criteria.where(eq("c.levelEnName", "?"));
				criteria.setStringParam(query.getLevelEnName());
			}
						
			Connection conn = ThreadUtils.currentConnection();
			PreparedStatement stm = criteria.getRealStatement(conn);
			ResultSet rs = stm.executeQuery();
			rs.next();
			count = rs.getInt("cn");
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return count;
	}
}
