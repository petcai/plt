package com.santrong.plt.webpage.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.santrong.plt.webpage.course.entry.ChapterAndResourceEntry;
import com.santrong.plt.webpage.course.entry.ChapterItem;

/**
 * @author weinianjie
 * @date 2014年10月20日
 * @time 下午12:06:23
 */
public interface ChapterMapper {
	
	@Select("select a.id, a.remark, b.title, b.resourceId, b.resourceType from course_chapter a "
			+ "left join course_chapter_to_resource b on a.id=b.chapterId "
			+ "where a.courseId=#{courseId} order by a.priority asc, b.priority asc")
	List<ChapterAndResourceEntry> selectByCourseId(String courseId);
	
	@Select("select a.* from course_chapter a left join course_chapter_to_resource b on a.id=b.chapterId where b.resourceId=#{resourceId}")
	ChapterItem selectByResourceId(String resourceId);
}
