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

	public boolean opRequest(String name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.opRequest(con, name);
	}

	public boolean confirmOperator(String name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.confirmOperator(con, name);
	}

	public User changePass(User user) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.changePass(con, user);
	}

	public ArrayList<User> getPending() throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.getPending(con);
	}

	public boolean block(String name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.block(con, name);
	}

	public boolean opRemove(String name) throws Exception {
		
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.opRemove(con, name);
	}

	public User addCard(User user) throws Exception {
		// TODO Auto-generated method stub
		
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.addCard(con, user);
	}

	public User getUserById(int name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		Access access = new Access();
		return access.getUserById(con, name);
		
	}
}