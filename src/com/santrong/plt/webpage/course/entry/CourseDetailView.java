package com.santrong.plt.webpage.course.entry;

import java.util.Date;
import java.util.List;

import com.santrong.plt.util.MyUtils;


/**
 * @author weinianjie
 * @date 2014年10月10日
 * @time 上午10:16:39
 */
public class CourseDetailView {
	private String id;
	private String courseName;
	private String teacher;
	private String ownerId;
	private String gradeId;
	private String subjectId;
	private String url;
	private int price;
	private int limitCount;
	private Date endTime;
	private int collectCount;
	private int saleCount;
	private int commentCount;
	private int chapterCount;
	private String remark;
	private int status;
	private Date cts;
	private Date uts;
	private String subjectName;
	private String gradeName;
	private String levelName;
	
	private List<ChapterDetailView> chapterDetailList;
	private List<CommentUserView> commentList;
	
	
	// 获取缩略图
	public String getThumbnail() {
		if(MyUtils.isNotNull(this.url)) {
			return url;
		}else {
			return "/resource/photo/course01.jpg";
		}
	}	
	public int getLimitCount() {
		return limitCount;
	}
	public void setLimitCount(int limitCount) {
		this.limitCount = limitCount;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public int getChapterCount() {
		return chapterCount;
	}
	public void setChapterCount(int chapterCount) {
		this.chapterCount = chapterCount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<ChapterDetailView> getChapterDetailList() {
		return chapterDetailList;
	}
	public void setChapterDetailList(List<ChapterDetailView> chapterDetailList) {
		this.chapterDetailList = chapterDetailList;
	}
	public List<CommentUserView> getCommentList() {
		return commentList;
	}
	public void setCommentList(List<CommentUserView> commentList) {
		this.commentList = commentList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public int getCollectCount() {
		return collectCount;
	}
	public void setCollectCount(int collectCount) {
		this.collectCount = collectCount;
	}
	public int getSaleCount() {
		return saleCount;
	}
	public void setSaleCount(int saleCount) {
		this.saleCount = saleCount;
	}
	public Date getCts() {
		return cts;
	}
	public void setCts(Date cts) {
		this.cts = cts;
	}
	public Date getUts() {
		return uts;
	}
	public void setUts(Date uts) {
		this.uts = uts;
	}
	public String getTeacher() {
		return teacher;
	}
	public void setTeacher(String teacher) {
		this.teacher = teacher;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
}
