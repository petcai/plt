package com.santrong.plt.webpage;

import java.sql.SQLException;

import com.santrong.plt.opt.ThreadUtils;

/**
 * @Author weinianjie
 * @Date 2014-7-7
 * @Time 下午10:54:14
 */
public class MapperFactor {
	public <T> T xx(Class<T> T) {
		try {
			return ThreadUtils.currentSqlSession().getMapper(T);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
