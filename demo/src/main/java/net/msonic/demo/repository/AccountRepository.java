package net.msonic.demo.repository;

import net.msonic.demo.resource.User;

public interface AccountRepository {
	
	User getUser(String userName);
	
}
