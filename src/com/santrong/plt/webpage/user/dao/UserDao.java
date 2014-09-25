package com.santrong.plt.webpage.user.dao;

import com.santrong.plt.webpage.BaseDao;
import com.santrong.plt.webpage.user.entry.UserItem;
import com.santrong.plt.webpage.user.mapper.UserMapper;

/**
 * @author weinianjie
 * @date 2014年7月14日
 * @time 下午5:49:51
 */
public class UserDao extends BaseDao{
	
	
	public UserItem selectByUserName(String username) {
		
		UserMapper mapper = this.getMapper(UserMapper.class);
		if(mapper != null) {
			return mapper.selectByUserName(username);
		}
		return null;
	}
	
	
	public int update(UserItem user) {
		
		UserMapper mapper = this.getMapper(UserMapper.class);
		if(mapper != null) {
			return mapper.update(user);
		}
		return 0;
	}
}
