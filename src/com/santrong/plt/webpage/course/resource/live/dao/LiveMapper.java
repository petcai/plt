package com.santrong.plt.webpage.course.resource.live.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.santrong.plt.webpage.course.resource.live.entry.LiveItem;
import com.santrong.plt.webpage.manage.teacher.entry.TeacherLiveForm;

/**
 * @author weinianjie
 * @date 2014年10月10日
 * @time 上午10:19:47
 */
public interface LiveMapper {

	
	@Select("select * from resource_live_score")
	List<LiveItem> selectAll();
	
	@Select("select * from resource_live where DATE_FORMAT(cts, '%Y-%m-%d') = DATE_FORMAT(now(), '%Y-%m-%d');")
	List<LiveItem> selectByToday();
	
	@Select("select * from resource_live where id = #{id};")
	LiveItem selectById(String id);	
	
	@Delete("delete from resource_live where id = #{id};")
	int delete(String id);
	
	@Insert("insert into resource_live values(#{id},#{title},#{url},#{beginTime},#{endTime},#{duration},#{groupId},#{ownerId},#{cts},#{uts})")
	int insert(LiveItem liveItem);
	
	@Update("update resource_live set "
			+ "title = #{title},"
			+ "url = #{url},"
			+ "beginTime = #{beginTime},"
			+ "endTime = #{endTime},"
			+ "duration = #{duration},"
			+ "groupId = #{groupId},"
			+ "uts = #{uts}"
			+ " where id = #{id}")
	int update(LiveItem liveItem);
	
	@Select("select a.id,a.beginTime,a.endTime,b.title,c.remark,d.courseName from resource_live a "
			+ "left join course_chapter_to_resource b on a.id=b.resourceId "
			+ "left join course_chapter c on b.chapterId=c.id "
			+ "left join course d on c.courseId=d.id "
			+ "where a.ownerId=#{userId} and a.endTime > now() order by a.beginTime")
	List<TeacherLiveForm> selectTeacherLive(String userId);
}
