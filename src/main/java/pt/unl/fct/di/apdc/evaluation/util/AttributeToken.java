package pt.unl.fct.di.apdc.evaluation.util;

public class AttributeToken {

	public String username;
	public String email;
	public String password;
	public String profile;
	public String phoneFixed;
	public String phoneMobile;
	public String address;
	public String addressComp;
	public String location;
	public String state;
	public String role;
	
	public AttributeToken(String username, String email, String password, String profile, String phoneFixed, String phoneMobile,
			String address, String addressComp, String location, String state, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.profile = profile;
		this.phoneFixed = phoneFixed;
		this.phoneMobile = phoneMobile;
		this.address = address;
		this.addressComp = addressComp;
		this.location = location;
		this.state = state;
		this.role = role;
		
	}
}
