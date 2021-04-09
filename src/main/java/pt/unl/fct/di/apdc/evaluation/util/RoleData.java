package pt.unl.fct.di.apdc.evaluation.util;

public class RoleData {

	public String username;
	public String userToChange;
	public String tokenID;
	public String role;
	
	public RoleData() {
		
	}
	
	public RoleData(String username, String userToChange, String tokenID, String role) {
		this.username = username;
		this.userToChange = userToChange;
		this.tokenID = tokenID;
		this.role = role;
	}
}
