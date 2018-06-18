package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
 
import dto.User;
 
public class Access
{
	public ArrayList<User> getUsers(Connection con) throws SQLException
	{
		ArrayList<User> userList = new ArrayList<User>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM users");
		ResultSet rs = stmt.executeQuery();
		try{
			while(rs.next()){
				
				User userObj = new User();
				userObj.setId(rs.getInt("id"));
				userObj.setBlocked(rs.getBoolean("blocked"));
				userObj.setEmail(rs.getString("email"));
				userObj.setDaily(rs.getInt("daily"));
				userObj.setWeekly(rs.getInt("weekly"));
				userObj.setName(rs.getString("name"));
				userObj.setPassword(rs.getString("password"));
				userObj.setSeller(rs.getBoolean("seller"));
				userObj.setState(rs.getString("state"));
				userObj.setActivated(rs.getBoolean("activated"));

				userList.add(userObj);
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		return userList;
		 
	}
	
	public User getUserByName(Connection con, String name) throws SQLException
	{
		
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM users WHERE name = ?");
		stmt.setString(1, name);
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("User found");

				User userObj = new User();
				userObj.setId(rs.getInt("id"));
				userObj.setBlocked(rs.getBoolean("blocked"));
				userObj.setEmail(rs.getString("email"));
				userObj.setDaily(rs.getInt("daily"));
				userObj.setWeekly(rs.getInt("weekly"));
				userObj.setName(rs.getString("name"));
				userObj.setPassword(rs.getString("password"));
				userObj.setSeller(rs.getBoolean("seller"));
				userObj.setState(rs.getString("state"));
				userObj.setActivated(rs.getBoolean("activated"));
				
				return userObj;
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean addUser(Connection con, User user) throws SQLException{
		
		User foundUser = getUserByName(con, user.getName());
		
		if(foundUser == null) {
			PreparedStatement stmt = con.prepareStatement("INSERT INTO users(name, password, email, state, weekly, daily, blocked, seller)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setString(1, user.getName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getEmail());
			stmt.setString(4, user.getState());
			stmt.setInt(5, user.getWeekly());
			stmt.setInt(6, user.getDaily());
			stmt.setBoolean(7, user.isBlocked());
			stmt.setBoolean(8, user.isSeller());
			stmt.setBoolean(9, user.isActivated());
			
			System.out.println(stmt.toString());
			if(stmt.executeUpdate() > 0) {
				return true;
			}
			else return false;
		}
		else return false;
		
	
	}
	
	public boolean activateUser(Connection con, String name) throws SQLException{
		
		User foundUser = getUserByName(con, name);
		
		if(foundUser != null) {
			PreparedStatement stmt = con.prepareStatement("UPDATE users SET activated = 1 WHERE name = ?");
			stmt.setString(1, name);
			
			System.out.println(stmt.toString());
			if(stmt.executeUpdate() > 0)
				return true;
			else return false;
		}
		else return false;
		
	
	}
}
