package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
 
import dto.Photo;
import dto.User;
 
public class PhotoAccess
{
	
	public ArrayList<Photo> getPhotosByName(Connection con, String name) throws SQLException
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE name = ?");
		stmt.setString(1, name);
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("Photo found");

				Photo photoObj = new Photo();
				photoObj.setId(rs.getInt("id"));
				photoObj.setNumOfSales(rs.getInt("numOfSales"));
				photoObj.setPrice(rs.getInt("price"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setPath(rs.getString("path"));

				photos.add(photoObj);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return photos;
	}
	
	public boolean addPhoto(Connection con, Photo photo, User user) throws SQLException{
		
		PreparedStatement stmt = con.prepareStatement("INSERT INTO photos(date, numOfSales, price, res, description, rating, place, ownerId, name, path)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		stmt.setDate(1, photo.getDate());
		stmt.setInt(2, photo.getNumOfSales());
		stmt.setInt(3, photo.getPrice());
		stmt.setInt(4, photo.getRes());
		stmt.setString(5, photo.getDescription());
		stmt.setInt(6, photo.getRating());
		stmt.setString(7, photo.getPlace());
		stmt.setInt(8, photo.getOwnerId());
		stmt.setString(9, photo.getName());
		stmt.setString(10, photo.getPath());
		
		System.out.println(stmt.toString());
		if(stmt.executeUpdate() > 0) {
			sendPhotoByEmail(photo, user);
			return true;
		}
		else return false;
	
	}
	
	
	public void sendPhotoByEmail(Photo photo, User user) {
			
			final String photoname = "cobibitza@gmail.com";
			final String password = "originalrap";

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(photoname, password);
				}
			  });

			try {

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("cobibitza@gmail.com"));
				message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
		         message.setSubject("Pic On Click Activation");
		
		         // Now set the actual message
		         message.setText("Hi " + photo.getName() + ", thanks for registering on Pic On Click! Just one more step, and you are all done."
		         		+ "To complete the activation, please click this link: http://localhost:8080/PicOnClick/rest/photoService/photo/activate/" + photo.getName());

				Transport.send(message);

				System.out.println("Done");

			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
	      
	      
	}

	
}
