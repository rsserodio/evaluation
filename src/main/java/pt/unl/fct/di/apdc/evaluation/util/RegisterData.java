package pt.unl.fct.di.apdc.evaluation.util;

public class RegisterData {

	public String username;
	public String email;
	public String password;
	public String passwordConfirm;
	public String role;
	
	
	
	public RegisterData() {
		
	}
	
	public RegisterData(String username, String email, String password, String passwordConfirm, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.role = role;
	}
}
