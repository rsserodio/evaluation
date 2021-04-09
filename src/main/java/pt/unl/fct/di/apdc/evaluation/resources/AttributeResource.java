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

import pt.unl.fct.di.apdc.evaluation.util.AttData;


@Path("/changeAttributes")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class AttributeResource {

	private static final Logger LOG = Logger.getLogger(AttributeResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();
	
	public AttributeResource() {}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changeAtt(AttData data) {
		
		LOG.fine("Changing attributes attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		
		try {
			Entity user = txn.get(userKey);
			
			if(user != null && user.getLong("tokenEnd") >= System.currentTimeMillis() && user.getLong("tokenEnd")!=0 && user.getString("tokenId").equals(data.tokenID)) {
				String profile = "";
				if (!data.profile.equals("")){
					profile = data.profile;
				}
				String phoneFixed = "";
				if (!data.phoneFixed.equals("")){
					phoneFixed = data.phoneFixed;
				}
				String phoneMobile = "";
				if (!data.phoneMobile.equals("")){
					phoneMobile = data.phoneMobile;
				}
				String address = "";
				if (!data.address.equals("")){
					address = data.address;
				}
				String addressComp = "";
				if (!data.addressComp.equals("")){
					addressComp = data.addressComp;
				}
				String location = "";
				if (!data.location.equals("")){
					location = data.location;
				}
				
				
				user = Entity.newBuilder(userKey)
						.set("username", data.username)
						.set("email", user.getString("email"))
						.set("password", user.getString("password"))
						.set("role", user.getString("role"))
						.set("profile", profile)
						.set("phoneFixed", phoneFixed)
						.set("phoneMobile", phoneMobile)
						.set("address", address)
						.set("addressComp", addressComp)
						.set("location", location)
						.set("state", user.getString("state"))
						.set("tokenId", user.getString("tokenId"))
						.set("tokenStart", user.getLong("tokenStart"))
						.set("tokenEnd", user.getLong("tokenEnd"))
						.build();
				txn.put(user);
				
				LOG.info("User " + data.username + " has successfully changed attributes!");
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
