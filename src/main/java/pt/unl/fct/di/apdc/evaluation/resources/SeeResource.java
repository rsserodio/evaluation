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

import pt.unl.fct.di.apdc.evaluation.util.SeeData;
import pt.unl.fct.di.apdc.evaluation.util.SeeToken;

@Path("/getMyAttributes")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class SeeResource {

	private static final Logger LOG = Logger.getLogger(SeeResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	
	
	private final Gson g = new Gson();
	public SeeResource() {}
	
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	public Response SeeResponse(SeeData data) {
		
		LOG.fine("Getting own attributes attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity gotUser = txn.get(userKey);
			if(gotUser!=null  && gotUser.getLong("tokenEnd") >= System.currentTimeMillis() && gotUser.getLong("tokenEnd")!=0 && gotUser.getString("tokenId").equals(data.tokenID)) {
				
				
				SeeToken token = new SeeToken(gotUser.getString("profile"),
						gotUser.getString("phoneFixed"), gotUser.getString("phoneMobile"), gotUser.getString("address"), gotUser.getString("addressComp"),
						gotUser.getString("location"));
				
				LOG.info("User " + data.username + " has successfully got his own attributes!");
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









