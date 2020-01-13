package com.example.demo;

import java.io.File;

public class ProjectTest {

	// Change the devToken upon expiration
	private static final String devToken = "tFwyNRjKYfTOYhdwNtgfr7D7kk3GHdTq";

	// Change the file path to your requirement
	private static final String filePath = "C://Users//rbhatnag//Desktop";

	public static void main(String[] args) {

		DocViewer project = new DocViewer(devToken);

		project.downloadFolders("0", filePath);

		System.out.println(project.getFolder("0"));
		System.out.println(project.getFolderItems("0"));
		System.out.println(project.getFile("588444996057"));
		project.downloadFile("588444996057", filePath);

		File file = new File(filePath + "//Clean-Code-Cheat-Sheet-V1.3.pdf");
		project.uploadFile("0", file);

	}

}
