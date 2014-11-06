package com.santrong.plt.webpage.live;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.santrong.plt.webpage.BaseAction;
import com.santrong.plt.webpage.course.service.CourseService;

/**
 * @author weinianjie
 * @date 2014年10月10日
 * @time 下午5:01:51
 */
@Controller
@RequestMapping("/live")
public class LiveAction extends BaseAction {
	
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
		
		CourseService courseService = new CourseService(getRequest(), getResponse());
		courseService.publishDetailFilter(grade, subject, null);
		
		return "course/index";
	}
}