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

import pt.unl.fct.di.apdc.evaluation.util.RemoveData;



@Path("/remove")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RemoveResource {
	
	
	private static final Logger LOG = Logger.getLogger(RemoveResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	
	
	public RemoveResource(){}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response remove(RemoveData data) {
		
		LOG.fine("Remove attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Key userKeyRemove = datastore.newKeyFactory().setKind("User").newKey(data.usernameRemove);
		
		Transaction txn = datastore.newTransaction();
		
		try {
			Entity user = txn.get(userKey);
			Entity userRemove = txn.get(userKeyRemove);
			if(user != null && userRemove != null && user.getLong("tokenEnd") >= System.currentTimeMillis() && user.getLong("tokenEnd")!=0 && user.getString("tokenId").equals(data.tokenID) ) {
				if(user.getString("username").equals(data.usernameRemove)) {
					txn.delete(userKey);
					LOG.info("User " + data.username + " has been successfully removed!");
					txn.commit();
					return Response.ok("{}").build();
				} else if(!user.getString("role").equals("USER")) {
					txn.delete(userKeyRemove);
					LOG.info("User " + data.usernameRemove + " has been successfully removed!");
					txn.commit();
					return Response.ok("{}").build();
				} else {
					txn.rollback();
					return Response.status(Status.BAD_REQUEST).entity("Invalid Attributes!").build();
				} 
				
				
			} 
			else {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("Invalid session!").build();
			} 
				
			} finally {
				if(txn.isActive())
					txn.rollback();
			}
		
		
		
	}

}
