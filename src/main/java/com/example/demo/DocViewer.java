package com.example.demo;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.connectionUtils.ConnectionImpl;
import com.example.demo.restPackage.RESTClient;

@RestController
@RequestMapping("/docviewer")
public class DocViewer {

	private String devToken;
	private RESTClient restClient;
	private ConnectionImpl conn;

	public DocViewer() {}

	@Autowired
	public DocViewer(@Value("${app.devToken}") String devToken) {
		this.devToken = devToken;
		conn = new ConnectionImpl(this.devToken);
		this.restClient = conn.getRestClient();
	}

	@GetMapping("/folder/{folderId}/items")
	public String getFolderItems(@PathVariable String folderId) {
		return restClient.getFolderItems(folderId);
	}

	@GetMapping("/folder/{folderId}")
	public String getFolder(@PathVariable String folderId) {
		return restClient.getFolder(folderId);
	}

	@GetMapping("/file/{fileId}")
	public String getFile(@PathVariable String fileId) {
		return restClient.getFile(fileId);
	}

	@PostMapping("/file")
	public String uploadFile(String folderId, File file) {
		return restClient.uploadFile2(folderId, file);
	}

	@GetMapping("/file/{fileId}/download")
	public void downloadFile(@PathVariable String fileId, @Value("${app.downloadLocation}") String location) {
		restClient.downloadFile(fileId, location);
	}
	
	@GetMapping("folder/{folderId}/download")
	public void downloadFolders(@PathVariable String folderId, @Value("${app.downloadLocation}") String location) {
		restClient.downloadFolder(folderId, location);
	}

	public String getDevToken() {
		return devToken;
	}

	public RESTClient getRestClient() {
		return restClient;
	}

	public ConnectionImpl getConn() {
		return conn;
	}
}
