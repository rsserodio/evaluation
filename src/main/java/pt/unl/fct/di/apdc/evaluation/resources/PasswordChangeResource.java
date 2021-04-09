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

import pt.unl.fct.di.apdc.evaluation.util.PasswordData;


@Path("/changePassword")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PasswordChangeResource {

	private static final Logger LOG = Logger.getLogger(PasswordChangeResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();
	
	public PasswordChangeResource() {}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response passChange(PasswordData data) {
		LOG.fine("Password changing attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		
		try {
			Entity user = txn.get(userKey);
			
			if(user != null && user.getLong("tokenEnd") >= System.currentTimeMillis() && user.getLong("tokenEnd")!=0 && user.getString("tokenId").equals(data.tokenID) &&
					user.getString("password").equals(DigestUtils.sha512Hex(data.password))  && data.toChange.equals(data.toChangeConf) && 
					data.toChange.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")) {
			
				
				user = Entity.newBuilder(userKey)
						.set("username", data.username)
						.set("email", user.getString("email"))
						.set("password", DigestUtils.sha512Hex(data.toChange))
						.set("role", user.getString("role"))
						.set("profile", user.getString("profile"))
						.set("phoneFixed", user.getString("phoneFixed"))
						.set("phoneMobile", user.getString("phoneMobile"))
						.set("address", user.getString("address"))
						.set("addressComp", user.getString("addressComp"))
						.set("location", user.getString("location"))
						.set("state", user.getString("state"))
						.set("tokenId", user.getString("tokenId"))
						.set("tokenStart", user.getLong("tokenStart"))
						.set("tokenEnd", user.getLong("tokenEnd"))
						.build();
				txn.put(user);
				
				LOG.info("User " + data.username + " has successfully changed passwords!");
				txn.commit();
				return Response.ok("{}").build();
				
				
				
				
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








