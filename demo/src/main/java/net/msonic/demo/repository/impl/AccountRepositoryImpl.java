package net.msonic.demo.repository.impl;

import org.springframework.stereotype.Repository;

import net.msonic.demo.repository.AccountRepository;
import net.msonic.demo.resource.User;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

	@Override
	public User getUser(String userName) {
		User user = new User();
		
		user.setCodigo("0001");
		user.setNombre("Manuel");
		user.setApellido("Zegarra");
		user.setUserName("mzegarra@gmail.com");
		user.setPassword("12E4s67890");
		return user;
	}

}
