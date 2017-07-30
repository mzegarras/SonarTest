package net.msonic.demo.service;

import net.msonic.demo.resource.User;

public interface AccountService {
	
	User login(String userName,String password) throws UserOrPasswordIncorrectException;
	User getUser(String userName) throws UserNotFoundException;
	void save(User user);
}
