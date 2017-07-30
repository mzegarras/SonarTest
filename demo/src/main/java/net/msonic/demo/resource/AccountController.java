package net.msonic.demo.resource;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.msonic.demo.service.AccountService;
import net.msonic.demo.service.UserNotFoundException;
import net.msonic.demo.service.UserOrPasswordIncorrectException;

@RestController
@RequestMapping("account")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	

	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ResponseData<User> test(@Valid @RequestBody final LoginRequest loginRequest) throws UserOrPasswordIncorrectException {
		
		ResponseData<User> response = new ResponseData<>();
		
		User user = accountService.login(loginRequest.getUserName(), loginRequest.getPassword());
		
		response.setData(user);
		
		response.setMessage(String.format("Bienvenido: %s", user.getNombre()));
		
		return response;
		
	}

	@RequestMapping("/{userName}")
	@ResponseBody
	public ResponseData<User> getUser(@PathVariable("userName") String userName) throws UserNotFoundException {
		
		ResponseData<User> responseData = new ResponseData<>();
		responseData.setMessage("");
		responseData.setData(accountService.getUser(userName));
		return responseData;

	}
	
	
	@RequestMapping(value ="/save",method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void saveUser(@Valid @RequestBody final User user) {
		accountService.save(user);
	}
	
	
	

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ResponseData<User> handleAppException(UserNotFoundException ex) {
		ResponseData<User> app = new ResponseData<>();
		app.setData(null);
		app.setMessage("Usuario no existe");
		return app;
	 }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseData<User> handleAppException(MethodArgumentNotValidException ex) {
		ResponseData<User> app = new ResponseData<>();
		app.setData(null);
		app.setMessage("Error validando parámetros");
		return app;
	}
	
	@ExceptionHandler(UserOrPasswordIncorrectException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ResponseBody
	public ResponseData<User> handleAppException(UserOrPasswordIncorrectException ex) {
		ResponseData<User> app = new ResponseData<>();
		app.setData(null);
		app.setMessage("Usuario o password incorrecto");
		return app;
	}
	
	@ExceptionHandler(UnsupportedOperationException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ResponseBody
	public ResponseData<User> handleAppException(UnsupportedOperationException ex) {
		ResponseData<User> app = new ResponseData<>();
		app.setData(null);
		app.setMessage("No disponible");
		return app;
	}
	
	

}
