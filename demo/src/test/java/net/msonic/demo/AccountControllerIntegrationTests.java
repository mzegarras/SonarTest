package net.msonic.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


import net.msonic.demo.repository.AccountRepository;
import net.msonic.demo.repository.impl.AccountRepositoryImpl;
import net.msonic.demo.resource.LoginRequest;
import net.msonic.demo.resource.ResponseData;
import net.msonic.demo.resource.User;
import net.msonic.demo.service.AccountService;
import net.msonic.demo.service.UserOrPasswordIncorrectException;
import net.msonic.demo.service.impl.AccountServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("IntegrationTest")

public class AccountControllerIntegrationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void contextLoads() {
	}
	
	
	@Test
	public void whenUserValidAndPasswordValidThenLogonOk() throws UserOrPasswordIncorrectException {


		LoginRequest loginRequest = new LoginRequest();

		loginRequest.setUserName("mzegarra@gmail.com");
		loginRequest.setPassword("12E4s67890");

		ResponseEntity<WrapperUserResponse> response = restTemplate.postForEntity("/account/login", loginRequest,
				WrapperUserResponse.class);

		// verificando status
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// verificando message
		assertThat(response.getBody().getMessage()).isEqualTo(String.format("Bienvenido: %s", "Manuel"));

	}

	/*
	 * @Test public void whenUserAndPasswordEmptyInvalidThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * @Test public void whenUserAndPasswordNullInvalidThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * loginRequest.setPassword(null); loginRequest.setUserName(null);
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * 
	 * @Test public void whenUserValidAndPasswordNullThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName("mzegarra@gmail.com");
	 * loginRequest.setPassword(null);
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * @Test public void whenUserValidAndPasswordEmptyInvalidThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName("mzegarra@gmail.com"); loginRequest.setPassword("");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * @Test public void whenUserNullAndPasswordValidThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName(null); loginRequest.setPassword("12E4s67890");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * @Test public void whenUserEmptyAndPasswordValidThenErrorBadRequest() {
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName(null); loginRequest.setPassword("12E4s67890");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * 
	 * @Test public void whenUserInvalidFormatAndPasswordValidThenErrorBadRequest()
	 * { LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName("mzegarra@gmailcom##");
	 * loginRequest.setPassword("12E4s67890");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	 * 
	 * assertThat(response.getBody().getData()).isNull();
	 * assertThat(response.getBody().getMessage()).
	 * isEqualTo("Error validando parámetros"); }
	 * 
	 * 
	 * @Rule public final ExpectedException exception = ExpectedException.none();
	 * 
	 * 
	 * @Test public void whenUserNotExistsThenLogonError() throws
	 * UserOrPasswordIncorrectException {
	 * 
	 * when(this.accountService.login("mzegarra@gmail.com",
	 * "12E4s67890")).thenThrow(new UserOrPasswordIncorrectException());
	 * 
	 * 
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName("mzegarra@gmail.com");
	 * loginRequest.setPassword("12E4s67890");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class); assertThat(response.getStatusCode()).isEqualTo(HttpStatus.
	 * INTERNAL_SERVER_ERROR);
	 * 
	 * 
	 * //verificando message assertThat(response.getBody().getMessage()).
	 * isEqualTo("Usuario o password incorrecto");
	 * 
	 * 
	 * //double check verify(this.accountService,
	 * times(1)).login("mzegarra@gmail.com", "12E4s67890"); }
	 * 
	 * @Test public void whenUserValidAndPasswordValidThenLogonOk() throws
	 * UserOrPasswordIncorrectException {
	 * 
	 * User user = new User();
	 * 
	 * user.setCodigo("0001"); user.setNombre("Manuel");
	 * user.setApellido("Zegarra"); user.setUserName("mzegarra@gmail.com");
	 * user.setPassword("123456");
	 * 
	 * when(this.accountService.login("mzegarra@gmail.com",
	 * "12E4s67890")).thenReturn(user);
	 * 
	 * LoginRequest loginRequest = new LoginRequest();
	 * 
	 * loginRequest.setUserName("mzegarra@gmail.com");
	 * loginRequest.setPassword("12E4s67890");
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.postForEntity("/account/login",loginRequest,WrapperUserResponse.
	 * class);
	 * 
	 * //verificando status
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	 * 
	 * //verificando message
	 * assertThat(response.getBody().getMessage()).isEqualTo(String.
	 * format("Bienvenido: %s", user.getNombre()));
	 * 
	 * //double check verify(this.accountService,
	 * times(1)).login("mzegarra@gmail.com", "12E4s67890");
	 * 
	 * 
	 * }
	 * 
	 * 
	 * @Test public void whenUserNotExistThenError404() throws Exception {
	 * 
	 * 
	 * when(this.accountService.getUser("0001")).thenThrow(new
	 * UserNotFoundException());
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.getForEntity("/account/0001", WrapperUserResponse.class);
	 * 
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	 * assertThat(response.getBody().getMessage()).isEqualTo("Usuario no existe");
	 * 
	 * verify(this.accountService, times(1)).getUser("0001"); }
	 * 
	 * 
	 * @Test public void whenUsertExistThenOk() throws Exception {
	 * 
	 * User user = new User();
	 * 
	 * user.setCodigo("0001"); user.setNombre("Manuel");
	 * user.setApellido("Zegarra"); user.setUserName("mzegarra@gmail.com");
	 * user.setPassword("123456");
	 * 
	 * 
	 * when(this.accountService.getUser("0001")).thenReturn(user);
	 * 
	 * 
	 * ResponseEntity<WrapperUserResponse> response =
	 * restTemplate.getForEntity("/account/0001", WrapperUserResponse.class);
	 * 
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	 * assertThat(response.getBody().getMessage()).isEqualTo("");
	 * 
	 * verify(this.accountService, times(1)).getUser("0001"); }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * @Test public void whenUserWithOutCodeThenErrorBadRequest() { User user = new
	 * User(); ResponseEntity<Void> response =
	 * restTemplate.postForEntity("/account/save",user,Void.class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); }
	 * 
	 * 
	 * @Test public void whenUserCodeLenghtMinErrorThenErrorBadRequest() { User user
	 * = new User(); user.setCodigo("1"); ResponseEntity<Void> response =
	 * restTemplate.postForEntity("/account/save",user,Void.class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); }
	 * 
	 * 
	 * 
	 * @Test public void whenUserCodeLenghtMaxErrorThenErrorBadRequest() {
	 * 
	 * User user = new User(); user.setCodigo("123456");
	 * 
	 * ResponseEntity<Void> response =
	 * restTemplate.postForEntity("/account/save",user,Void.class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST); }
	 * 
	 * @Test public void whenSaveUserSaveThenOk() { User user = new User();
	 * user.setCodigo("001"); user.setNombre("MANUEL ALBERTO"); ResponseEntity<Void>
	 * response = restTemplate.postForEntity("/account/save",user,Void.class);
	 * assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED); }
	 */

	static class WrapperUserResponse extends ResponseData<User> {
	}

	@Profile("IntegrationTest")
	@Configuration	
	@ComponentScan("net.msonic.demo")
	static public class ConfigTest001 {

		@Bean
		@Primary
		public AccountService accountService() {
			return new AccountServiceImpl();
		}

		@Bean
		@Primary
		public AccountRepository accountRepository() {
			return new AccountRepositoryImpl();
		}
	}
}
