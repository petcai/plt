package com.santrong.plt.webpage.course.resource.train.entry;

import com.santrong.plt.opt.PageQuery;

/**
 * @author weinianjie
 * @date 2014年11月5日
 * @time 下午5:12:49
 */
public class TrainQuery extends PageQuery {
	private String ownerId;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
}
