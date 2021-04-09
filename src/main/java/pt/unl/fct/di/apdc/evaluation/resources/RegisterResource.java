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



import pt.unl.fct.di.apdc.evaluation.util.RegisterData;



@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class RegisterResource {

	private static final Logger LOG = Logger.getLogger(RegisterResource.class.getName());
	private	final	Datastore datastore	= DatastoreOptions.getDefaultInstance().getService();	


	
	public RegisterResource() {}
	
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(RegisterData data) {
		LOG.fine("Registering attempt by: " + data.username);
		
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		
		Transaction txn = datastore.newTransaction();
		
		try {
			
			
			if(!data.password.equals(data.passwordConfirm) || !data.email.matches("^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{2,6}))?$")
					|| !data.password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")) {
				txn.rollback();
				return Response.status(Status.BAD_REQUEST).entity("Invalid Attributes!").build();
			}
			
			Entity user = txn.get(userKey);
			
				if(user != null) {
					txn.rollback();
					return Response.status(Status.BAD_REQUEST).entity("Invalid Attributes!").build();
				} else {
					String role = data.role;
					switch(data.role.toUpperCase()) {
						case "GBO":
							role = "GBO";
						break;
						case "GA":
							role = "GA";
						break;
						case "SU":
							role = "SU";
						break;
						case "USER":
							role = "USER";
						default:
							role = "USER";
					}
					user = Entity.newBuilder(userKey)
							.set("username", data.username)
							.set("email", data.email)
							.set("password", DigestUtils.sha512Hex(data.password))
							.set("role", role)
							.set("profile", "")
							.set("phoneFixed", "")
							.set("phoneMobile", "")
							.set("address", "")
							.set("addressComp", "")
							.set("location", "")
							.set("state", "ENABLED")
							.set("tokenId", "none")
							.set("tokenStart", 0)
							.set("tokenEnd", 0)
							.build();
					txn.add(user);
					
					
					
					LOG.info("User " + data.username + " has been successfully registered!");
					txn.commit();
					return Response.ok("{}").build();
					
				}
			
			
			
		} finally {
			if(txn.isActive())
				txn.rollback();
		}
		
		
		
	}

	
}
