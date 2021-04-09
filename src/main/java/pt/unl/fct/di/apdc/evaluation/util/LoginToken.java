package pt.unl.fct.di.apdc.evaluation.util;



public class LoginToken {
public static final long EXPIRATION_TIME = 1000*60*60*2; //2h
public String user;
public String role;
public long valid_From;
public long valid_To;
public String validityID;
public LoginToken(String username, String role) {
this.user = username;
this.role = role;
this.valid_From = System.currentTimeMillis();
this.valid_To = this.valid_From + LoginToken.EXPIRATION_TIME;
this.validityID = user + "1234";
}
}
