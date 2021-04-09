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
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.evaluation.util.AttributeToken;
import pt.unl.fct.di.apdc.evaluation.util.GBOdata;


@Path("/getAllAttributes")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class GBOResource {
	
	private static final Logger LOG = Logger.getLogger(GBOResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	
	
	private final Gson g = new Gson();
	public GBOResource() {}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response GBOResponse(GBOdata data) {
		LOG.fine("Getting attributes attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Key userTo = datastore.newKeyFactory().setKind("User").newKey(data.gotUser);
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity user = txn.get(userKey);
			Entity gotUser = txn.get(userTo);
			if(!user.getString("role").equals("USER") && user != null && gotUser != null && user.getLong("tokenEnd") >= System.currentTimeMillis() && user.getLong("tokenEnd")!=0 && user.getString("tokenId").equals(data.tokenID)) {
				AttributeToken token = new AttributeToken(data.gotUser, gotUser.getString("email"), gotUser.getString("password"), gotUser.getString("profile"),
						gotUser.getString("phoneFixed"), gotUser.getString("phoneMobile"), gotUser.getString("address"), gotUser.getString("addressComp"),
						gotUser.getString("location"), gotUser.getString("state"), gotUser.getString("role")); 
				
				LOG.info("User " + data.username + " has successfully got the needed attributes!");
				txn.commit();
				return Response.ok(g.toJson(token)).build();
				
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
