package net.msonic.demo;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.msonic.demo.resource.AccountController;
import net.msonic.demo.resource.LoginRequest;
import net.msonic.demo.resource.User;
import net.msonic.demo.service.AccountService;
import net.msonic.demo.service.UserNotFoundException;
import net.msonic.demo.service.UserOrPasswordIncorrectException;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
    private AccountService accountService;

	@Test
	public void whenUserAndPasswordEmptyInvalidThenErrorBadRequest() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest();

		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
		
	}
	
	@Test
	public void whenUserAndPasswordNullInvalidThenErrorBadRequest() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest();
		loginRequest.setPassword(null);
		loginRequest.setUserName(null);
        
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
		
	}
	
	@Test
	public void whenUserValidAndPasswordNullThenErrorBadRequest() throws JsonProcessingException, Exception{
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName("mzegarra@gmail.com");
		loginRequest.setPassword(null);
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	@Test
	public void whenUserValidAndPasswordEmptyInvalidThenErrorBadRequest() throws JsonProcessingException, Exception {
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName("mzegarra@gmail.com");
		loginRequest.setPassword("");
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	@Test
	public void whenUserNullAndPasswordValidThenErrorBadRequest() throws JsonProcessingException, Exception{
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName(null);
		loginRequest.setPassword("12E4s67890");
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	
	@Test
	public void whenUserEmptyAndPasswordValidThenErrorBadRequest() throws JsonProcessingException, Exception{
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName(null);
		loginRequest.setPassword("12E4s67890");
	
        
        mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	
	@Test
	public void whenUserInvalidFormatAndPasswordValidThenErrorBadRequest() throws JsonProcessingException, Exception{
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName("mzegarra@gmailcom##");
		loginRequest.setPassword("12E4s67890");
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	
	@Test
	public void whenUserNotExistsThenLogonError() throws JsonProcessingException, Exception {
		
		when(this.accountService.login("mzegarra@gmail.com", "12E4s67890")).thenThrow(new UserOrPasswordIncorrectException());
		
		
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName("mzegarra@gmail.com");
		loginRequest.setPassword("12E4s67890");
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden())
				.andExpect(content().json("{\"message\":\"Usuario o password incorrecto\",\"data\":null}"));

        //double check
        verify(this.accountService, times(1)).login("mzegarra@gmail.com", "12E4s67890");
	}
	
	
	@Test
	public void whenUserValidAndPasswordValidThenLogonOk() throws JsonProcessingException, Exception {
		
		User user = new User();
		
		user.setCodigo("0001");
		user.setNombre("Manuel");
		user.setApellido("Zegarra");
		user.setUserName("mzegarra@gmail.com");
		user.setPassword("123456");
		
		when(this.accountService.login("mzegarra@gmail.com", "12E4s67890")).thenReturn(user);
		
		LoginRequest loginRequest = new LoginRequest();
		
		loginRequest.setUserName("mzegarra@gmail.com");
		loginRequest.setPassword("12E4s67890");
		
		mvc.perform(post("/account/login")
				.content(mapper.writeValueAsString(loginRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"message\":\"Bienvenido: Manuel\",\"data\":{\"codigo\":\"0001\",\"nombre\":\"Manuel\",\"apellido\":\"Zegarra\",\"userName\":\"mzegarra@gmail.com\",\"password\":\"123456\"}}"));
        
        //double check
        verify(this.accountService, times(1)).login("mzegarra@gmail.com", "12E4s67890");
        
	}
	
	@Test
	public void whenUserNotExistThenError404() throws Exception {
			
		
        when(this.accountService.getUser("0001")).thenThrow(new UserNotFoundException());
        
        
    		mvc.perform(get("/account/0001")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(content().json("{\"message\":\"Usuario no existe\",\"data\":null}"));
    	
        
        verify(this.accountService, times(1)).getUser("0001");
	}
	
	@Test
	public void whenUsertExistThenOk() throws Exception {
			
		User user = new User();
		
		user.setCodigo("0001");
		user.setNombre("Manuel");
		user.setApellido("Zegarra");
		user.setUserName("mzegarra@gmail.com");
		user.setPassword("123456");
		
		
        when(this.accountService.getUser("0001")).thenReturn(user);
        
        
    		mvc.perform(get("/account/0001")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"message\":\"\",\"data\":{\"codigo\":\"0001\",\"nombre\":\"Manuel\",\"apellido\":\"Zegarra\",\"userName\":\"mzegarra@gmail.com\",\"password\":\"123456\"}}"));
    	
        
        verify(this.accountService, times(1)).getUser("0001");
	}
	
	@Test
	public void whenUserWithOutCodeThenErrorBadRequest() throws JsonProcessingException, Exception {
		User user = new User();
		
		mvc.perform(post("/account/save")
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
       
	}
	
	@Test
	public void whenUserCodeLenghtMinErrorThenErrorBadRequest() throws JsonProcessingException, Exception {
		User user = new User();
		user.setCodigo("1");
		mvc.perform(post("/account/save")
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	

	@Test
	public void whenUserCodeLenghtMaxErrorThenErrorBadRequest() throws JsonProcessingException, Exception {
		
		User user = new User();
		user.setCodigo("123456");
		
		mvc.perform(post("/account/save")
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().json("{\"message\":\"Error validando parámetros\",\"data\":null}"));
	}
	
	
	@Test
	public void whenSaveUserThenServiceUnavailable() throws JsonProcessingException, Exception {
		
		User user = new User();
		user.setCodigo("001");
		user.setNombre("MANUEL ALBERTO");
		
		doThrow(new UnsupportedOperationException()).when(this.accountService).save(any());

		
		mvc.perform(post("/account/save")
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isServiceUnavailable())
				.andExpect(content().json("{\"message\":\"No disponible\",\"data\":null}"));
	}
	
	@Test
	public void whenSaveUserThenOk() throws JsonProcessingException, Exception {
		
		User user = new User();
		user.setCodigo("001");
		user.setNombre("MANUEL ALBERTO");
		
		doNothing().when(this.accountService).save(anyObject());


		
		mvc.perform(post("/account/save")
				.content(mapper.writeValueAsString(user))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isAccepted());
	}
	
	
	
	
	
	
	
	

}
