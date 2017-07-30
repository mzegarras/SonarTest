package net.msonic.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.msonic.demo.repository.AccountRepository;
import net.msonic.demo.resource.User;
import net.msonic.demo.service.AccountService;
import net.msonic.demo.service.UserNotFoundException;
import net.msonic.demo.service.UserOrPasswordIncorrectException;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Override
	public User login(String userName, String password) throws UserOrPasswordIncorrectException {
		User user = accountRepository.getUser(userName);
		
		if(user.getUserName().compareTo(userName)!=0 || user.getPassword().compareTo(password)!=0) {
			throw new UserOrPasswordIncorrectException();
		}
		
		return user;
		
	}

	@Override
	public User getUser(String userName) throws UserNotFoundException  {
		User user = accountRepository.getUser(userName);
		
		if(user==null) {
			throw new UserNotFoundException();
		}else {
			return user;
		}
	}

	@Override
	public void save(User user) {
		throw new UnsupportedOperationException();
	}
	
	

}
