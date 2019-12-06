package com.mframe.mapper;

import com.mframe.model.User;

public interface UserMapper {
	
	User selectByUserId(Integer userId);
	
	int saveUser(User user);

}
