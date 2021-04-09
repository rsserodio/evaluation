package pt.unl.fct.di.apdc.evaluation.util;

public class AttData {

	public String username;
	public String tokenID;
	public String profile;
	public String phoneFixed;
	public String phoneMobile;
	public String address;
	public String addressComp;
	public String location;
	
	public AttData() {
		
	}
	
	public AttData(String username, String tokenID, String profile, String phoneFixed, String phoneMobile, String address, String addressComp, String location) {
		this.username = username;
		this.tokenID = tokenID;
		this.profile = profile;
		this.phoneFixed = phoneFixed;
		this.phoneMobile = phoneMobile;
		this.address = address;
		this.addressComp = addressComp;
		this.location = location;
	}
}