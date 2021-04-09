package pt.unl.fct.di.apdc.evaluation.util;

public class PasswordData {

	public String username;
	public String password;
	public String toChange;
	public String toChangeConf;
	public String tokenID;
	
	public PasswordData() {
		
	}
	
	public PasswordData(String username, String password, String toChange, String toChangeConf, String tokenID) {
		this.username = username;
		this.password = password;
		this.toChange = toChange;
		this.toChangeConf = toChangeConf;
		this.tokenID = tokenID;
	}
}