package net.msonic.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import net.msonic.demo.repository.AccountRepository;
import net.msonic.demo.resource.User;
import net.msonic.demo.service.AccountService;
import net.msonic.demo.service.UserNotFoundException;
import net.msonic.demo.service.UserOrPasswordIncorrectException;
import net.msonic.demo.service.impl.AccountServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class AccountServiceTest {

	@MockBean
	private AccountRepository accountRepository;

	@Autowired
	private AccountService accountService;

	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	
	
	@Test
	public void whenUserOkAndPasswordIncorrectThenError() throws UserOrPasswordIncorrectException {
		User  user = new User();
		user.setCodigo("001");
		user.setApellido("Zegarra");
		user.setNombre("Manuel");
		user.setPassword("1234567890");
		user.setUserName("mzegarra@gmail.com");
		
		exception.expect(UserOrPasswordIncorrectException.class);
		
		when(this.accountRepository.getUser("mzegarra@gmail.com")).thenReturn(user);
		
		User userFromService = accountService.login("mzegarra@gmail.com", "abc123");

		
	}
	
	@Test
	public void whenUserIncorrectAndPasswordOkThenError() throws UserOrPasswordIncorrectException {
		User  user = new User();
		user.setCodigo("001");
		user.setApellido("Zegarra");
		user.setNombre("Manuel");
		user.setPassword("1234567890");
		user.setUserName("mzegarra@gmail.com");
		
		exception.expect(UserOrPasswordIncorrectException.class);
		
		when(this.accountRepository.getUser("mzegarra1@gmail.com")).thenReturn(user);
		
		User userFromService = accountService.login("mzegarra1@gmail.com", "1234567890");

	}
	
	@Test
	public void whenUserIncorrectAndPasswordIncorrectThenError() throws UserOrPasswordIncorrectException {
		User  user = new User();
		user.setCodigo("001");
		user.setApellido("Zegarra");
		user.setNombre("Manuel");
		user.setPassword("1234567890");
		user.setUserName("mzegarra@gmail.com");
		
		exception.expect(UserOrPasswordIncorrectException.class);
		
		when(this.accountRepository.getUser("mzegarra1@gmail.com")).thenReturn(user);
		
		User userFromService = accountService.login("mzegarra1@gmail.com", "123456789011");

	}
	
	@Test
	public void whenUserAndPasswordOkThenLoginOk() throws UserOrPasswordIncorrectException {
		User  user = new User();
		user.setCodigo("001");
		user.setApellido("Zegarra");
		user.setNombre("Manuel");
		user.setPassword("1234567890");
		user.setUserName("mzegarra@gmail.com");
		
		when(this.accountRepository.getUser("mzegarra@gmail.com")).thenReturn(user);
		
		User userFromService = accountService.login("mzegarra@gmail.com", "1234567890");
		
		
		
		assertThat(userFromService).isNotNull();
		
		
		verify(this.accountRepository, times(1)).getUser("mzegarra@gmail.com");

	}
	
	
	
	@Test
	public void whenUserExistsThenOk() throws UserNotFoundException {
		User  user = new User();
		user.setCodigo("001");
		user.setApellido("Zegarra");
		user.setNombre("Manuel");
		user.setPassword("1234567890");
		user.setUserName("mzegarra@gmail.com");
		
		when(this.accountRepository.getUser("001")).thenReturn(user);
		
		User userFromService = accountService.getUser("001");
		
		assertThat(userFromService).isNotNull();		
		
	}
	
	@Test
	public void whenUserNotExistsThenError() throws Exception {

		when(this.accountRepository.getUser("001")).thenReturn(null);
		
		exception.expect(UserNotFoundException.class);

		accountService.getUser("001");

	}

	@Profile("test")
	@Configuration
	static public class ConfigTest001 {

		@Bean
		@Primary
		public AccountService accountService() {
			return new AccountServiceImpl();
		}
	}

}
