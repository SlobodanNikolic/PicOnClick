package services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import com.google.gson.Gson;

import dao.Access;
import dao.AccessManager;
import dao.PhotoManager;
import dto.BaseObject;
import dto.BinaryPhoto;
import dto.Photo;
import dto.User;
import javafx.util.Pair;
import sun.misc.BASE64Encoder;
 
@Path("/photoService")
public class PhotoService
{
	
	private final String UPLOADED_FILE_PATH = "/Users/cobe/PicOnClickStorage/";
	
	@GET
	@Path("/photo/{name}")
	@Produces("application/json")
	public ArrayList<Photo> getPhotosByName(@PathParam("name") String name)
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Photo photo = new Photo();
		String photoString = "";
		
		try{
			photos = new PhotoManager().getPhotosByName(name);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return photos;
	}
	
	
	@GET
	@Path("/photo/id/{id}")
	@Produces("application/json")
	public Photo getPhotoById(@PathParam("id") int id)
	{
		Photo photo = new Photo();
		String photoString = "";
		
		try{
			photo = new PhotoManager().getPhotoById(id);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return photo;
	}
	
	@GET
	@Path("/photo/author/{id}/{pageNum}")
	@Produces("application/json")
	public ArrayList<BinaryPhoto> getPhotosByAuthor(@PathParam("id") int name, @PathParam("pageNum") int pageNum)
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Photo photo = new Photo();
		String photoString = "";
		Map<String, Object> mapa = new HashMap<String, Object>();
		ArrayList<BinaryPhoto> binaryPhotos = new ArrayList<BinaryPhoto>();
		
		try{
			photos = new PhotoManager().getPhotosByAuthor(name, pageNum);
//			Gson gson = new Gson();
//			photoString = gson.toJson(photos);
			for(int i = 0; i < photos.size(); i++) {
//				images.add(getImageAsString(photos.get(i)));
				BinaryPhoto bp = new BinaryPhoto(photos.get(i), getImageAsString(photos.get(i)));
//				mapa.put(getImageAsString(photos.get(i)), photos.get(i));
				binaryPhotos.add(bp);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return binaryPhotos;
	}
	
	public String getImageAsString(Photo photo) {
		int width;    //width of the image
	    int height;   //height of the image
	    String photoName = photo.getName();
	    int indexOfDot = photoName.lastIndexOf('.');
	    String type = photoName.substring(indexOfDot+1, photoName.length());
	    		
	    if(photo.getRes() == 0) {
	    	width = 1280;
	    	height = 720;
	    }
	    else if(photo.getRes() == 1) {
	    	width = 1920;
	    	height = 1080;
	    }
	    else {
	    	width = 3840;
	    	height = 2160;
	    }
	    
	    BufferedImage image = null;
	    File f = null;

	    //read image
	    try{
	      f = new File(photo.getPath()); //image file path
	      image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	      image = ImageIO.read(f);
	      System.out.println("Reading complete.");
	    }catch(IOException e){
	      System.out.println("Error: "+e);
	    }
	    
	    String imageAsString = encodeToString(image, type);
	    return imageAsString;
	}
	
	 public static String encodeToString(BufferedImage image, String type) {
	        String imageString = null;
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();

	        try {
	            ImageIO.write(image, type, bos);
	            byte[] imageBytes = bos.toByteArray();

	            BASE64Encoder encoder = new BASE64Encoder();
	            imageString = encoder.encode(imageBytes);

	            bos.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return imageString;
	    }
	
    public boolean addPhoto(BaseObject[] photoAndUser){
		
		Photo photo = (Photo) photoAndUser[0];
		User user = (User) photoAndUser[1];
		
		System.out.println(photo.getPath());
		
    	boolean value = false;
		try {
			value = new PhotoManager().addPhoto(photo, user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return value;
    }
	
	
	
	@POST
	@Path("/photo/user/{name}/upload/{priceHD}/{priceFullHD}/{price4K}/{description}/{location}/{tags}")
	@Consumes("multipart/form-data")
	public String uploadFile(MultipartFormDataInput input, @PathParam("name") String name, 
			@PathParam("priceHD") int priceHD, @PathParam("priceFullHD") int priceFullHD,
			@PathParam("price4K") int price4K, @PathParam("description") String description,
			@PathParam("location") String location, @PathParam("tags") String tags) {
		
		String fileName = "";
		String path ="";
		User user = new User();
		try {
			user = new AccessManager().getUserByName(name);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("uploadedFile");
		
		
		for (InputPart inputPart : inputParts) {
		 try {

			MultivaluedMap<String, String> header = inputPart.getHeaders();
			fileName = getFileName(header);

			//convert the uploaded file to inputstream
			InputStream inputStream = inputPart.getBody(InputStream.class,null);

			byte [] bytes = IOUtils.toByteArray(inputStream);
				
			//constructs upload file path
			path = UPLOADED_FILE_PATH + user.getName() + "/" + fileName;
			System.out.println(path);

			writeFile(bytes, path);
				
			System.out.println("Done");

		  } catch (IOException e) {
			e.printStackTrace();
		  }

		}
		
		Date d = new Date();
		java.sql.Date date = new java.sql.Date(d.getTime());
		
		System.out.println("PhotoService: " + path);
		Photo photoObj = new Photo(0, date, 0, priceHD, priceFullHD, price4K, 2, 0, user.getId(), fileName, description, location, path, false, tags, 0);
		System.out.println("Photo object: " + photoObj.getPath());
		BaseObject[] photoAndUser = new BaseObject[] {photoObj, user};
		addPhoto(photoAndUser);
		
		
		return "OK";

	}
	
	private String getFileName(MultivaluedMap<String, String> header) {

		String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
		
		for (String filename : contentDisposition) {
			if ((filename.trim().startsWith("filename"))) {

				String[] name = filename.split("=");
				
				String finalFileName = name[1].trim().replaceAll("\"", "");
				return finalFileName;
			}
		}
		return "unknown";
	}

	//save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		System.out.println(filename);

		File file = new File(filename);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	
	@GET
	@Path("/photo/rate/{id}/{rating}")
	@Produces("application/json")
	public boolean ratePhoto(@PathParam("id") int id, @PathParam("rating") int rating) throws Exception
	{
		PhotoManager m = new PhotoManager();
		Photo photo = m.getPhotoById(id);
		if(photo!=null) {
			int currRating = photo.getRating();
			int timesRated = photo.getTimesRated();
			timesRated++;
			int newRating = (currRating+rating)/timesRated;
			photo.setRating(newRating);
			photo.setTimesRated(timesRated);
			return m.updatePhoto(photo);
		}
		else return false;
	}
	
	@GET
	@Path("/photo/approve/id/{id}")
	@Produces("application/json")
	public boolean approvePhoto(@PathParam("id") int id) throws Exception
	{
		PhotoManager m = new PhotoManager();
		Photo photo = m.getPhotoById(id);
		if(photo!=null) {
			photo.setApproved(true);
			return m.updatePhoto(photo);
		}
		else return false;
	}
	
	@GET
	@Path("/getunapproved")
	@Produces("application/json")
	public ArrayList<Photo> getUnapproved()
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		try{
			photos = new PhotoManager().getUnapproved();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return photos;
	}
	
	@GET
	@Path("/addtag/{tagname}")
	@Produces("application/json")
	public boolean addTag(@PathParam("tagname") String name)
	{
		
		try{
			return new PhotoManager().addTag(name);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
}