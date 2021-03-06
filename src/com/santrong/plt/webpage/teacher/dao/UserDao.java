package com.santrong.plt.webpage.teacher.dao;

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
import com.santrong.plt.webpage.teacher.entry.UserCourseView;
import com.santrong.plt.webpage.teacher.entry.UserDetailView;
import com.santrong.plt.webpage.teacher.entry.UserItem;
import com.santrong.plt.webpage.teacher.entry.UserQuery;
import com.santrong.plt.webpage.teacher.entry.UserTmpItem;

/**
 * @author weinianjie
 * @date 2014年7月14日
 * @time 下午5:49:51
 */
public class UserDao extends BaseDao{
	
	
	public UserItem selectByUserName(String username) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectByUserName(username);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	public UserItem selectByEmail(String email) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectByEmail(email);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}	
	
	public UserItem selectById(String id) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectById(id);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	public boolean existsByUserName(String username) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.existsByUserName(username) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}	
	
	public boolean existsByEmail(String email) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.existsByEmail(email) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}		
	
	public boolean existsByPhone(String phone) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.existsByPhone(phone) > 0;
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}		
	
	public int insert(UserItem user) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.insert(user);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}
	
	public int insertTmp(UserTmpItem tmp) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.insertTmp(tmp);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}
	
	public UserTmpItem selectTmpByUserId(String userId) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectTmpByUserId(userId);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}	
	
	public int updateTmp(UserTmpItem tmp) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.updateTmp(tmp);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}	
	
	public int update(UserItem user) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.update(user);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return 0;
	}
	
	/**
	 * 根据具体查询条件查询用户
	 * @param query
	 * @return
	 */
	public 	List<UserItem> selectByQuery(UserQuery query) {
		List<UserItem> list = new ArrayList<UserItem>();
		
		try{
			Statement criteria = new Statement("user", "a");
			criteria.setFields("a.*,b.schoolName");
			criteria.ljoin("school", "b", "a.schoolId", "b.id");
			criteria.ljoin("subject", "c", "a.subjectId", "c.id");
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("a.showName", "?")));
				criteria.setStringParam("%" + query.getKeywords() + "%");
			}
			// 科目条件
			if(MyUtils.isNotNull(query.getSubjectEnName())) {
				criteria.where(eq("c.subjectEnName", "?"));
				criteria.setStringParam(query.getSubjectEnName());
			}			
			// 类型包含
			if(query.getSchoolGrade() > 0) {
				criteria.where(eq("(b.schoolGrade & ?)", "?"));
				criteria.setIntParam(query.getSchoolGrade());
				criteria.setIntParam(query.getSchoolGrade());
			}
			// 类型绝对等
			if(query.getSchoolAbsoluteGrade() > 0) {
				criteria.where(eq("b.schoolGrade", "?"));
				criteria.setIntParam(query.getSchoolAbsoluteGrade());
			}
			// 所属区域
			if(MyUtils.isNotNull(query.getAreaCode())) {
				criteria.where(like("b.areaCode", "?"));
				criteria.setStringParam(AreaUtils.lostTail(query.getAreaCode()) + "%");
			}
			// 角色包含
			if(query.getRole() > 0) {
				criteria.where(eq("(a.role & ?)", "?"));
				criteria.setIntParam(query.getRole());
				criteria.setIntParam(query.getRole());
			}
			// 所属学校
			if(MyUtils.isNotNull(query.getSchoolId())) {
				criteria.where(eq("a.schoolId", "?"));
				criteria.setStringParam(query.getSchoolId());
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
			while(rs.next()){
				UserItem item = new UserItem();
				BeanUtils.Rs2Bean(rs, item);
				list.add(item);
			}
			
		}catch(Exception e){
			Log.printStackTrace(e);
		}
		
		return list;
	}	
	
	/**
	 * 根据具体查询条件查询用户总数
	 * @param query
	 * @return
	 */
	public 	int selectCountByQuery(UserQuery query) {
		int count = 0;
		
		try{
			Statement criteria = new Statement("user", "a");
			criteria.setFields("count(*) cn"); 
			criteria.ljoin("school", "b", "a.schoolId", "b.id");
			criteria.ljoin("subject", "c", "a.subjectId", "c.id");
			// 关键词
			if(!StringUtils.isNullOrEmpty(query.getKeywords())) {
				criteria.where(or(
						like("u.showName", "?")));
				criteria.setStringParam("%" + query.getKeywords() + "%");
			}
			// 科目条件
			if(MyUtils.isNotNull(query.getSubjectEnName())) {
				criteria.where(eq("c.subjectEnName", "?"));
				criteria.setStringParam(query.getSubjectEnName());
			}			
			// 类型包含
			if(query.getSchoolGrade() > 0) {
				criteria.where(eq("(b.schoolGrade & ?)", "?"));
				criteria.setIntParam(query.getSchoolGrade());
				criteria.setIntParam(query.getSchoolGrade());
			}
			// 类型绝对等
			if(query.getSchoolAbsoluteGrade() > 0) {
				criteria.where(eq("b.schoolGrade", "?"));
				criteria.setIntParam(query.getSchoolAbsoluteGrade());
			}
			// 角色包含
			if(query.getRole() > 0) {
				criteria.where(eq("(a.role & ?)", "?"));
				criteria.setIntParam(query.getRole());
				criteria.setIntParam(query.getRole());
			}					
			// 所属区域
			if(MyUtils.isNotNull(query.getAreaCode())) {
				criteria.where(like("b.areaCode", "?"));
				criteria.setStringParam(AreaUtils.lostTail(query.getAreaCode()) + "%");
			}
			// 所属学校
			if(MyUtils.isNotNull(query.getSchoolId())) {
				criteria.where(eq("a.schoolId", "?"));
				criteria.setStringParam(query.getSchoolId());
			}
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
	
	// 获取用户详细信息，包括扩展部分的详细信息
	public UserDetailView selectDetailById(String id) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectDetailById(id);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
		
	/**
	 * 查询学校里的老师 
	 * @author huangweihua
	 * @param  role shcoolId
	 * @return 
	 */
	public List<UserItem> selectTeacherBySchoolId(String shcoolId) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if (mapper != null) {
				return mapper.selectTeacherBySchoolId(shcoolId);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	/**
 	 * 查询课程所有者的信息，包含姓名，所属学校，教学科目，性别，课程数，注册时间
 	 * @author huangweihua
 	 * @param id
 	 * @return
 	 */
	public UserCourseView selectTeacherByUserId(String id) {
		try {
			UserMapper mapper = this.getMapper(UserMapper.class);
			if (mapper != null) {
				return mapper.selectTeacherByUserId(id);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	/**
	 * 查询多个用户信息
	 * @param ids
	 * @return
	 */
	public List<UserItem> selectByIds(String[] ids) {
		try {
			String _ids = MyUtils.consistIds(ids);//组装IDS
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectByIds(_ids);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
	
	/**
	 * 查询多个用户信息,不包括ids里的用户
	 * @param ids
	 * @return
	 */
	public List<UserItem> selectByNotInIds(String[] ids) {
		try {
			String _ids = MyUtils.consistIds(ids);//组装IDS
			UserMapper mapper = this.getMapper(UserMapper.class);
			if(mapper != null) {
				return mapper.selectByNotInIds(_ids);
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
		return null;
	}
}
