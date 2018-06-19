package services;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
 
import com.google.gson.Gson;
 
import dao.AccessManager;
import dto.User;
 
@Path("/userService")
public class UserService
{
	@GET
	@Path("/users")
	@Produces("application/json")
	public String users()
	{
		String users = null;
		ArrayList<User> userList = new ArrayList<User>();
		
		try{
			userList = new AccessManager().getUsers();
			Gson gson = new Gson();
			users = gson.toJson(userList);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return users;
	}
	
	@GET
	@Path("/user/{name}")
	@Produces("application/json")
	public String getUserByName(@PathParam("name") String name)
	{
		User user = new User();
		String userString = "";
		
		try{
			user = new AccessManager().getUserByName(name);
			Gson gson = new Gson();
			userString = gson.toJson(user);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return userString;
	}
	
	@POST
	@Path("/user")
    @Produces("text/json")
    @Consumes("application/json")
    public User addUser(User user){
		
		System.out.println(user.getName());
		
    	User value = null;
		try {
			value = new AccessManager().addUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
	
	@GET
	@Path("/user/activate/{name}")
    @Produces("text/json")
    public boolean activateUser(@PathParam("name") String name){
		
    	boolean value = false;
		try {
			value = new AccessManager().activateUser(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
	
	@POST
	@Path("/login")
    @Produces("text/json")
    @Consumes("application/json")
    public User login(User user){
		
		System.out.println(user.getName());
		
    	User value = null;
		try {
			value = new AccessManager().login(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
	
	@GET
	@Path("/user/request/operator/{name}")
    @Produces("text/json")
    public boolean opRequest(@PathParam("name") String name){
		
    	boolean value = false;
		try {
			value = new AccessManager().opRequest(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
	
	@GET
	@Path("/user/request/operator/{name}/confirm")
    @Produces("text/json")
    public boolean confirmOperator(@PathParam("name") String name){
		
    	boolean value = false;
		try {
			value = new AccessManager().confirmOperator(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
}