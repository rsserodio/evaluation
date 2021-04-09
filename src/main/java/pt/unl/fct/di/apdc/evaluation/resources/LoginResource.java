package pt.unl.fct.di.apdc.evaluation.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.codec.digest.DigestUtils;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;
import com.google.gson.Gson;
import pt.unl.fct.di.apdc.evaluation.resources.LoginResource;
import pt.unl.fct.di.apdc.evaluation.util.LoginData;
import pt.unl.fct.di.apdc.evaluation.util.LoginToken;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LoginResource {
	
	private static final Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	
	
	private final Gson g = new Gson();
	public LoginResource() {}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response loginResponse(LoginData data) {
		LOG.fine("Login attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		try {
		Entity user = txn.get(userKey);
		
		if(user != null) {
			String pwd = user.getString("password");
			if(pwd.equals(DigestUtils.sha512Hex(data.password))) {
				LoginToken logT = new LoginToken(data.username, user.getString("role"));
				user = Entity.newBuilder(userKey)
						.set("username", data.username)
						.set("email", user.getString("email"))
						.set("password", user.getString("password"))
						.set("role", user.getString("role"))
						.set("profile", user.getString("profile"))
						.set("phoneFixed", user.getString("phoneFixed"))
						.set("phoneMobile", user.getString("phoneMobile"))
						.set("address", user.getString("address"))
						.set("addressComp", user.getString("addressComp"))
						.set("location", user.getString("location"))
						.set("state", user.getString("state"))
						.set("tokenId", logT.validityID)
						.set("tokenStart", logT.valid_From)
						.set("tokenEnd", logT.valid_To)
						.build();
				txn.put(user);
				
				LOG.info("User " + data.username + " has successfully logged in!");
				txn.commit();
				return Response.ok(g.toJson(logT)).build();
				
				
						
			} else {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("Invalid Attributes!").build();
			}
		} else {
			txn.rollback();
			return Response.status(Status.BAD_REQUEST).entity("Invalid Attributes!").build();
		}
		} finally {
			if(txn.isActive())
				txn.rollback();
		}
	}
}







