package pt.unl.fct.di.apdc.evaluation.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Transaction;

import pt.unl.fct.di.apdc.evaluation.util.LogoutData;


@Path("/logout")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class LogoutResource {

	private static final Logger LOG = Logger.getLogger(LogoutResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	
	
	public LogoutResource() {}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logout(LogoutData data) {
		LOG.fine("Logout attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity user = txn.get(userKey);
			if(user != null) {
				String tID = user.getString("tokenId");
				if(data.tokenID.equals(tID)) {
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
							.set("tokenId", "none")
							.set("tokenStart", 0)
							.set("tokenEnd", 0)
							.build();
					txn.put(user);

					LOG.info("User " + data.username + " has successfully logged out!");
					txn.commit();
					return Response.ok("{}").build();
				}	else {
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
