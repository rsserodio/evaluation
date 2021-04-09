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



import pt.unl.fct.di.apdc.evaluation.util.StateData;


@Path("/changeStates")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class StateResource {

	private static final Logger LOG = Logger.getLogger(StateResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();
	
	public StateResource() {}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeState(StateData data) {
		
		LOG.fine("Changing states attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Key userKeyState = datastore.newKeyFactory().setKind("User").newKey(data.userToChange);
		
		Transaction txn = datastore.newTransaction();
		
		
		try {
			Entity user = txn.get(userKey);
			Entity remove = txn.get(userKeyState);
			
			if(user != null && remove != null && ((data.state.equals("ENABLED") && remove.getString("state").equals("DISABLED")) || (data.state.equals("DISABLED") && remove.getString("state").equals("ENABLED")))
					&& !user.getString("role").equals("USER")
					&& ((remove.getString("role").equals("USER") && !user.getString("role").equals("USER")) 
					|| (remove.getString("role").equals("GBO")  && (user.getString("role").equals("GA") || user.getString("role").equals("SU")))
					|| (remove.getString("role").equals("GA")  && user.getString("role").equals("SU"))
					) 
					&& user.getLong("tokenEnd") >= System.currentTimeMillis() && user.getLong("tokenEnd")!=0 && user.getString("tokenId").equals(data.tokenID)) {
			
				
				remove = Entity.newBuilder(userKeyState)
						.set("username", data.userToChange)
						.set("email", remove.getString("email"))
						.set("password", remove.getString("password"))
						.set("role", remove.getString("role"))
						.set("profile", remove.getString("profile"))
						.set("phoneFixed", remove.getString("phoneFixed"))
						.set("phoneMobile", remove.getString("phoneMobile"))
						.set("address", remove.getString("address"))
						.set("addressComp", remove.getString("addressComp"))
						.set("location", remove.getString("location"))
						.set("state", data.state)
						.set("tokenId", remove.getString("tokenId"))
						.set("tokenStart", remove.getLong("tokenStart"))
						.set("tokenEnd", remove.getLong("tokenEnd"))
						.build();
				txn.put(remove);
				
				LOG.info("User " + data.userToChange + " has successfully changed states!");
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













