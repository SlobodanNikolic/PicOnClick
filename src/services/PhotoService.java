package services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.jboss.resteasy.util.Base64.InputStream;
import com.google.gson.Gson;

import dao.PhotoManager;
import dto.BaseObject;
import dto.Photo;
import dto.User;
 
@Path("/photoService")
public class PhotoService
{
	
	private final String UPLOADED_FILE_PATH = "/Users/cobe/PicOnClickStorage/";
	
	@GET
	@Path("/photo/{name}")
	@Produces("application/json")
	public String getPhotosByName(@PathParam("name") String name)
	{
		ArrayList<Photo> photos = new ArrayList<Photo>();
		Photo photo = new Photo();
		String photoString = "";
		
		try{
			photos = new PhotoManager().getPhotosByName(name);
			Gson gson = new Gson();
			photoString = gson.toJson(photos);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return photoString;
	}
	
	@POST
	@Path("/photo")
    @Produces("text/json")
    @Consumes("application/json")
    public boolean addPhoto(BaseObject[] photoAndUser){
		
		Photo photo = (Photo) photoAndUser[0];
		User user = (User) photoAndUser[1];
		
		System.out.println(photo.getName());
		
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
	@Path("/photo/upload")
	@Consumes("multipart/form-data")
	public String uploadFile(MultipartFormDataInput input) {

		String fileName = "";
		
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
			fileName = UPLOADED_FILE_PATH + fileName;
				
			writeFile(bytes,fileName);
				
			System.out.println("Done");

		  } catch (IOException e) {
			e.printStackTrace();
		  }

		}

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

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
	
	
}