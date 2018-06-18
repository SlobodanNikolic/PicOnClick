package dao;

import java.util.ArrayList;
import dto.Photo;
import dto.User;

import java.sql.Connection;

public class PhotoManager
{
	
	public ArrayList<Photo> getPhotosByName(String name) throws Exception
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		photos = access.getPhotosByName(con, name);
		return photos;
	}
	
	public boolean addPhoto(Photo photo, User user) throws Exception {
		
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.addPhoto(con, photo, user);
	}
	
	
}