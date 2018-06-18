package dto;

import java.sql.Date;

public class Photo extends BaseObject{
	
	protected int id;
	protected Date date;
	protected int numOfSales;
	protected int price;
	protected int res;
	protected int rating;
	protected int ownerId;
	protected String name;
	protected String description;
	protected String place;
	protected String path;
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getNumOfSales() {
		return numOfSales;
	}
	public void setNumOfSales(int numOfSales) {
		this.numOfSales = numOfSales;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPath() {
		return description;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public Photo(int id, Date date, int numOfSales, int price, int res, int rating, int ownerId, String name,
			String description, String place, String path) {
		super();
		this.id = id;
		this.date = date;
		this.numOfSales = numOfSales;
		this.price = price;
		this.res = res;
		this.rating = rating;
		this.ownerId = ownerId;
		this.name = name;
		this.description = description;
		this.place = place;
		this.path = path;
	}
	
	public Photo() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
