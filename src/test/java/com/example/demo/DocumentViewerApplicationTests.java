package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.connectionUtils.ConnectionImpl;
import com.example.demo.restPackage.RESTClient;

@SpringBootTest
class DocumentViewerApplicationTests {

	private static DocViewer docViewer;
	private static RESTClient restClient;
	private static ConnectionImpl conn;

	// Change the devToken upon expiration
	private static final String devToken = "tFwyNRjKYfTOYhdwNtgfr7D7kk3GHdTq";

	// Change the file path to your requirement
	private static final String filePath = "C://Users//rbhatnag//Desktop";

	@BeforeAll
	static void init_Project() {
		docViewer = new DocViewer(devToken);
		restClient = docViewer.getRestClient();
		conn = docViewer.getConn();
	}

	@Test
	public void nullChecks() {
		assertThat(docViewer).isNotNull();
		assertThat(restClient).isNotNull();
		assertThat(conn).isNotNull();
	}

	@Test
	public void fileCheck() {
		String file = docViewer.getFile("305221877832");
		assertThat(file).isNotBlank();
		assertThat(file).isNotNull();
	}

	@Test
	public void folderCheck() {
		String file = docViewer.getFile("0");
		assertThat(file).isNotBlank();
		assertThat(file).isNotNull();
	}

	@Test
	public void downloadFile() {
		docViewer.downloadFile("305221877832", filePath);
	}
	
	@Test
	public void downloadFolders() {
		docViewer.downloadFolders("0", filePath);
	}

	@Test
	void contextLoads() {
		assertThat(new ProjectTest()).isNotNull();
	}

}
