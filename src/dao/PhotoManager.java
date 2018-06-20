package dao;

import java.util.ArrayList;
import dto.Photo;
import dto.User;

import java.sql.Connection;
import java.sql.SQLException;

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

	public ArrayList<Photo> getPhotosByAuthor(int name, int pageNum) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.getPhotosByAuthor(con, name, pageNum);
	}

	public Photo getPhotoById(int id) throws Exception {
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.getPhotoById(con, id);		
	}

	public boolean updatePhoto(Photo photo) throws Exception {
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.updatePhoto(con, photo);
	}

	public ArrayList<Photo> getUnapproved() throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.getUnapproved(con);
		
	}

	public boolean addTag(String name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.addTag(con, name);
		
	}

	public ArrayList<Photo> getPhotosByTag(String name) throws Exception {
		// TODO Auto-generated method stub
		Database db = new Database();
		Connection con = db.getConnection();
		PhotoAccess access = new PhotoAccess();
		return access.getPhotosByTag(con, name);
	}
	
	
}