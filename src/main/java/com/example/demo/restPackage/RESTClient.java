package com.example.demo.restPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

public class RESTClient {

	private Client client;
	private String devToken;

	public RESTClient() {
		this.client = new Client();
	}

	public void createConnection(String devToken) {
		this.devToken = devToken;
	}

	public String getFolderItems(String folderId) {
		WebResource resource = client.resource("https://api.box.com/2.0/folders/" + folderId + "/items");
		Builder request = resource.header("Authorization", "Bearer " + devToken).header("Content-Type",
				MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = request.get(ClientResponse.class);
		String entity = response.getEntity(String.class);
		return entity;
	}

	public String getFolder(String folderId) {
		WebResource resource = client.resource("https://api.box.com/2.0/folders/" + folderId);
		Builder request = resource.header("Authorization", "Bearer " + devToken).header("Content-Type",
				MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = request.get(ClientResponse.class);
		String entity = response.getEntity(String.class);
		return entity;
	}

	public String getFile(String fileId) {
		WebResource resource = client.resource("https://api.box.com/2.0/files/" + fileId);
		Builder request = resource.header("Authorization", "Bearer " + devToken).header("Content-Type",
				MediaType.APPLICATION_JSON_TYPE);
		ClientResponse response = request.get(ClientResponse.class);
		String entity = response.getEntity(String.class);
		return entity;
	}

	public void downloadFile(String fileId, String location) {
		JSONObject file = new JSONObject(getFile(fileId));
		String fileName = (String) file.get("name");
		WebResource resource = client.resource("https://api.box.com/2.0/files/" + fileId + "/content");
		Builder request = resource.header("Authorization", "Bearer " + devToken).header("Content-Type",
				MediaType.APPLICATION_JSON_TYPE);
		InputStream response = request.get(InputStream.class);
		try {
			byte[] buffer = new byte[response.available()];
			response.read(buffer);
			File newFile = new File(location + "//" + fileName);
			OutputStream outStream = new FileOutputStream(newFile);
			outStream.write(buffer);
			outStream.close();
		} catch (IOException e) {
			throw new RuntimeException("Could not download file due to " + e.getMessage());
		}
	}

	public void downloadFolder(String folderId, String location) {
		JSONObject jsonObj = new JSONObject(getFolderItems(folderId));
		JSONArray jsonArray = jsonObj.getJSONArray("entries");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject obj = (JSONObject) jsonArray.get(i);
			String type = obj.getString("type");
			String objId = obj.getString("id");
			String name = obj.getString("name");

			if (type.equals("folder")) {
				int count = new JSONObject(getFolder(objId)).getJSONObject("item_collection").getInt("total_count");
				File dir = new File(location + "//" +name);
				dir.mkdir();
				if(count != 0) {
					downloadFolder(objId, location + "//" + name);
				}
			} else {
				downloadFile(objId, location);
			}
		}
	}

	public String uploadFile2(String folderId, File file) {

		JSONObject entity = new JSONObject();
		JSONObject parentEntity = new JSONObject();
		entity.put("name", file.getName());
		parentEntity.put("id", folderId);
		entity.put("parent", parentEntity);

		WebResource resource = client.resource("https://upload.box.com/api/2.0/files/content");
		// HttpPost httppost = new HttpPost(baseURl + "/files/content");
		HttpEntity reqEntity = MultipartEntityBuilder.create()
				.addTextBody("attributes", entity.toString(), ContentType.APPLICATION_JSON).addBinaryBody("file", file)
				.build();
		Builder request = resource.header("Authorization", "Bearer " + devToken).entity(reqEntity.toString());
		ClientResponse response = request.post(ClientResponse.class);
		String entityResponse = response.getEntity(String.class);
		return entityResponse;
	}

	@Deprecated
	/**
	 * @param folderId
	 * @param file
	 * @return
	 */
	public JSONObject uploadFile(String folderId, File file) {
		JSONObject entity = new JSONObject();
		JSONObject parentEntity = new JSONObject();
		entity.put("name", file.getName());
		parentEntity.put("id", folderId);
		entity.put("parent", parentEntity);
		// entity.put("size", file.getUsableSpace());

		WebResource resource = client.resource("https://api.box.com/2.0/files/content");
		Builder request = resource.header("Authorization", "Bearer " + devToken)
				.header("Content-Type", MediaType.APPLICATION_JSON_TYPE).accept(MediaType.APPLICATION_JSON_TYPE)
				.entity(entity.toString());
		ClientResponse response = request.options(ClientResponse.class);
		String uploadData = response.getEntity(String.class);
		JSONObject uploadDataURL = new JSONObject(uploadData);

		WebResource resource2 = client.resource(uploadDataURL.getString("upload_url"));
		Builder request2 = resource2.header("Authorization", "Bearer " + devToken);
		ClientResponse response2 = request2.options(ClientResponse.class);
		if (response2.getStatus() != 200)
			return null;

		WebResource resource3 = client.resource(uploadDataURL.getString("upload_url"));
		String entityUpload = "";
		InputStream is = null;
		byte[] buffer = null;
		try {
			is = new FileInputStream(file);
			buffer = new byte[is.available()];
			is.read(buffer);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String entityStr = "";
		try {
			InputStream is2 = new FileInputStream(file);
			entityStr = convertInputStreamToString(is2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		entityUpload = "------WebKitFormBoundary5uqlDCAt0qrvLp3M\n"
				+ "Content-Disposition: form-data; name=\"attributes\"\n" + "\r\n" + entity.toString()
				+ "------WebKitFormBoundary5uqlDCAt0qrvLp3M\n"
				+ "Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n"
				+ "Content-Type:application/pdf\n" + "\r\n" + entityStr + "\r\n"
				+ "------WebKitFormBoundary5uqlDCAt0qrvLp3M--";
		Builder request3 = resource3.header("Authorization", "Bearer " + devToken)
				.header("Content-Type", "multipart/form-data; boundary=----WebKitFormBoundary5uqlDCAt0qrvLp3M")
				.accept("application/json, text/plain").entity(entityUpload);
		ClientResponse response3 = request3.post(ClientResponse.class);
		String responseEntity = response3.getEntity(String.class);

		JSONObject ans = new JSONObject(responseEntity);
		return ans;

	}

	@Deprecated
	/**
	 * @param stream
	 * @return
	 */
	private static String convertInputStreamToString(InputStream stream) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(stream, writer, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return writer.toString();
	}

}
