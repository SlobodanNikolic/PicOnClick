package dao;

import java.util.ArrayList;
import dto.User;
import java.sql.Connection;

public class AccessManager
{
	public ArrayList<User> getUsers() throws Exception
	{
		ArrayList<User> userList = new ArrayList<User>();
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		userList = access.getUsers(con);
		return userList;
	}
	
	public User getUserByName(String name) throws Exception
	{
		User user = new User();
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		user = access.getUserByName(con, name);
		return user;
	}
	
	public User addUser(User user) throws Exception {
		
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.addUser(con, user);
	}
	
	public boolean activateUser(String name) throws Exception {
		
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.activateUser(con, name);
	}

	public User login(User user) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.login(con, user);
	}
}