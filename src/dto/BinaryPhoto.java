package dto;

import java.sql.Date;

public class BinaryPhoto extends BaseObject{
	
	protected int id;
	protected Date date;
	protected int numOfSales;
	protected int priceHD;
	protected int priceFullHD;
	protected int price4K;
	protected int res;
	protected int rating;
	protected int ownerId;
	protected String name;
	protected String description;
	protected String place;
	protected String path;
	protected boolean approved;
	protected String tags;
	protected String bin;
	protected int timesRated;
	
	public int getTimesRated() {
		return timesRated;
	}
	public void setTimesRated(int timesRated) {
		this.timesRated = timesRated;
	}
	public String getBin() {
		return bin;
	}
	public void setBin(String bin) {
		this.bin = bin;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public int getPriceHD() {
		return priceHD;
	}
	public void setPriceHD(int priceHD) {
		this.priceHD = priceHD;
	}
	public int getPriceFullHD() {
		return priceFullHD;
	}
	public void setPriceFullHD(int priceFullHD) {
		this.priceFullHD = priceFullHD;
	}
	public int getPrice4K() {
		return price4K;
	}
	public void setPrice4K(int price4k) {
		price4K = price4k;
	}
	
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
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	public BinaryPhoto(Photo photo, String bin) {
		super();
		this.id = photo.id;
		this.date = photo.date;
		this.numOfSales = photo.numOfSales;
		this.priceHD = photo.priceHD;
		this.priceFullHD = photo.priceFullHD;
		price4K = photo.price4K;
		this.res = photo.res;
		this.rating = photo.rating;
		this.ownerId = photo.ownerId;
		this.name =	photo.name;
		this.description = photo.description;
		this.place = photo.place;
		this.path = photo.path;
		this.approved = photo.approved;
		this.tags = photo.tags;
		this.timesRated = photo.timesRated;
		this.bin = bin;
	}
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public BinaryPhoto() {
		// TODO Auto-generated constructor stub
	}
	
	
	
}
