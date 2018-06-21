package dao;
 
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.imageio.ImageIO;

import dto.Photo;
import dto.User;
import sun.misc.BASE64Encoder;
 
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
				photoObj.setPriceHD(rs.getInt("priceHD"));
				photoObj.setPriceFullHD(rs.getInt("priceFullHD"));
				photoObj.setPrice4K(rs.getInt("price4K"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setName(rs.getString("name"));
				photoObj.setPath(rs.getString("path"));
				photoObj.setApproved(rs.getBoolean("approved"));
				photoObj.setTags(rs.getString("tags"));
				photoObj.setTimesRated(rs.getInt("timesRated"));
				photos.add(photoObj);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return photos;
	}
	
	public boolean addPhoto(Connection con, Photo photo, User user) throws SQLException{
		
		System.out.println(photo.getPath());
		
		PreparedStatement stmt = con.prepareStatement("INSERT INTO photos(date, numOfSales, priceHD, priceFullHD, price4K, res, description, rating, place, ownerId, name, path, approved, tags, timesRated)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		
		stmt.setDate(1, photo.getDate());
		stmt.setInt(2, photo.getNumOfSales());
		stmt.setInt(3, photo.getPriceHD());
		stmt.setInt(4, photo.getPriceFullHD());
		stmt.setInt(5, photo.getPrice4K());
		stmt.setInt(6, photo.getRes());
		stmt.setString(7, photo.getDescription());
		stmt.setInt(8, photo.getRating());
		stmt.setString(9, photo.getPlace());
		stmt.setInt(10, photo.getOwnerId());
		stmt.setString(11, photo.getName());
		stmt.setString(12, photo.getPath());
		stmt.setBoolean(13, photo.isApproved());
		stmt.setString(14, photo.getTags());
		stmt.setInt(15, photo.getTimesRated());
		System.out.println(stmt.toString());
		
		if(stmt.executeUpdate() > 0) {
			return true;
		}
		else return false;
	
	}
	
	

	public ArrayList<Photo> getPhotosByAuthor(Connection con, int name, int pageNum) throws SQLException {
		
		ArrayList<Photo> photos = new ArrayList<Photo>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE ownerId = ?");
		stmt.setInt(1, name);
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("Photo found");

				Photo photoObj = new Photo();
				photoObj.setId(rs.getInt("id"));
				photoObj.setNumOfSales(rs.getInt("numOfSales"));
				photoObj.setPriceHD(rs.getInt("priceHD"));
				photoObj.setPriceFullHD(rs.getInt("priceFullHD"));
				photoObj.setPrice4K(rs.getInt("price4K"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setName(rs.getString("name"));
				photoObj.setPath(rs.getString("path"));
				photoObj.setApproved(rs.getBoolean("approved"));
				photoObj.setTags(rs.getString("tags"));
				photoObj.setTimesRated(rs.getInt("timesRated"));

				photos.add(photoObj);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		if(photos.size() >= pageNum*10) {
			return new ArrayList<Photo>(photos.subList(pageNum*10-10, pageNum*10));
		}
		else {
			return new ArrayList<Photo>(photos.subList(pageNum*10-10, photos.size()));
		}
		
	}

	public Photo getPhotoById(Connection con, int id) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE id = ?");
		stmt.setInt(1, id);
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("Photo found");

				Photo photoObj = new Photo();
				photoObj.setId(rs.getInt("id"));
				photoObj.setNumOfSales(rs.getInt("numOfSales"));
				photoObj.setPriceHD(rs.getInt("priceHD"));
				photoObj.setPriceFullHD(rs.getInt("priceFullHD"));
				photoObj.setPrice4K(rs.getInt("price4K"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setName(rs.getString("name"));
				photoObj.setPath(rs.getString("path"));
				photoObj.setApproved(rs.getBoolean("approved"));
				photoObj.setTags(rs.getString("tags"));
				photoObj.setTimesRated(rs.getInt("timesRated"));
				
				return photoObj;
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return null;	
	}

	public boolean updatePhoto(Connection con, Photo photo) throws SQLException {
		
		PreparedStatement stmt = con.prepareStatement("UPDATE photos SET numOfSales = ?, rating = ?, approved = ?, timesRated = ? WHERE id = ?");
		stmt.setInt(1, photo.getNumOfSales());
		stmt.setInt(2, photo.getRating());
		stmt.setBoolean(3, photo.isApproved());
		stmt.setInt(4, photo.getTimesRated());
		stmt.setInt(5, photo.getId());
		
		System.out.println(stmt.toString());
		
		if(stmt.executeUpdate() > 0)
			return true;
		else return false;

	}

	public ArrayList<Photo> getUnapproved(Connection con) throws SQLException {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE approved = 0");
		
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("Photo found");

				Photo photoObj = new Photo();
				photoObj.setId(rs.getInt("id"));
				photoObj.setNumOfSales(rs.getInt("numOfSales"));
				photoObj.setPriceHD(rs.getInt("priceHD"));
				photoObj.setPriceFullHD(rs.getInt("priceFullHD"));
				photoObj.setPrice4K(rs.getInt("price4K"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setName(rs.getString("name"));
				photoObj.setPath(rs.getString("path"));
				photoObj.setApproved(rs.getBoolean("approved"));
				photoObj.setTags(rs.getString("tags"));
				photoObj.setTimesRated(rs.getInt("timesRated"));
				photos.add(photoObj);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return photos;
	}

	public boolean addTag(Connection con, String name) throws SQLException {
		
		PreparedStatement stmt = con.prepareStatement("INSERT INTO tags(tagName)"
				+ "VALUES(?)");
		
		stmt.setString(1, name);
		
		if(stmt.executeUpdate() > 0) {
			return true;
		}
		else return false;
	}

	public ArrayList<Photo> getPhotosByTag(Connection con, String name) throws SQLException {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		PreparedStatement stmt = con.prepareStatement("SELECT * FROM photos WHERE tags LIKE ?");
		stmt.setString(1, "%" + name + "%");
		System.out.println(stmt.toString());
		ResultSet rs = stmt.executeQuery();
		System.out.println(rs.toString());

		try{
			while(rs.next()){
				System.out.println("Photo found");

				Photo photoObj = new Photo();
				photoObj.setId(rs.getInt("id"));
				photoObj.setNumOfSales(rs.getInt("numOfSales"));
				photoObj.setPriceHD(rs.getInt("priceHD"));
				photoObj.setPriceFullHD(rs.getInt("priceFullHD"));
				photoObj.setPrice4K(rs.getInt("price4K"));
				photoObj.setRes(rs.getInt("res"));
				photoObj.setDescription(rs.getString("description"));
				photoObj.setRating(rs.getInt("rating"));
				photoObj.setPlace(rs.getString("place"));
				photoObj.setDate(rs.getDate("date"));
				photoObj.setOwnerId(rs.getInt("ownerId"));
				photoObj.setName(rs.getString("name"));
				photoObj.setPath(rs.getString("path"));
				photoObj.setApproved(rs.getBoolean("approved"));
				photoObj.setTags(rs.getString("tags"));
				photoObj.setTimesRated(rs.getInt("timesRated"));
				photos.add(photoObj);
			}
			
		} catch (SQLException e){
			e.printStackTrace();
		}
		
		return photos;
	}

	
	public Photo buyPhotoById(Connection con, int id, String name) throws SQLException {
		
		User user = new Access().getUserByName(con, name);
		if(user == null)
			return null;
		
		Photo wantedPhoto = getPhotoById(con, id);
		
		if(wantedPhoto != null) {
			wantedPhoto.setNumOfSales(wantedPhoto.getNumOfSales()+1);
			if(!updatePhoto(con, wantedPhoto))
				return null;
			
			PreparedStatement stmt = con.prepareStatement("INSERT INTO userPhotos(userId, photoId)"
					+ "VALUES(?, ?)");
			
			stmt.setInt(2, wantedPhoto.getId());
			stmt.setInt(1, user.getId());
			
			if(stmt.executeUpdate() <= 0) {
				return null;
			}
			
			
		}
		
		sendPhotoByEmail(user, wantedPhoto);
		
		return wantedPhoto;
		
	}

	
	public void sendPhotoByEmail(User user, Photo photo) {
		
		final String username = "cobibitza@gmail.com";
		final String password = "originalrap";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("cobibitza@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(user.getEmail()));
	         message.setSubject("PicOnClick - Your photo is here");
	
	         //Pravimo multipart
	         MimeMultipart content = new MimeMultipart();
	         MimeBodyPart mainPart = new MimeBodyPart();
	         
	         mainPart.setText("Hello there! Here is your photo.");
	         content.addBodyPart(mainPart);
	         
	         //Dodajemo sliku
	         MimeBodyPart imagePart = new MimeBodyPart();

	         
	        try {
				imagePart.attachFile(photo.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	         content.addBodyPart(imagePart);        
	         message.setContent(content);
	         // Now set the actual message
	         
			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
      
      
	}
	
	
	
}
