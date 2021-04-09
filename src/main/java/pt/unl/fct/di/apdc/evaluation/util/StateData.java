package pt.unl.fct.di.apdc.evaluation.util;

public class StateData {

	public String username;
	public String userToChange;
	public String tokenID;
	public String state;
	
	public StateData() {
		
	}
	
	public StateData(String username, String userToChange, String tokenID, String state) {
		this.username = username;
		this.userToChange = userToChange;
		this.tokenID = tokenID;
		this.state = state;
	}
}
