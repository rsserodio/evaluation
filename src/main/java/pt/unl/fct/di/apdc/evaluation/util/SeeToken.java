package pt.unl.fct.di.apdc.evaluation.util;

public class SeeToken {

	public String profile;
	public String phoneFixed;
	public String phoneMobile;
	public String address;
	public String addressComp;
	public String location;

	
	public SeeToken(String profile, String phoneFixed, String phoneMobile,
			String address, String addressComp, String location) {
		this.profile = profile;
		this.phoneFixed = phoneFixed;
		this.phoneMobile = phoneMobile;
		this.address = address;
		this.addressComp = addressComp;
		this.location = location;	
	}
}
