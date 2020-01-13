package com.example.demo.connectionUtils;

import com.example.demo.restPackage.RESTClient;

public class ConnectionImpl {

	private String devToken;
	private RESTClient restClient;
	
	public ConnectionImpl() {}
	
	public ConnectionImpl(String devToken) {
		this.devToken = devToken;
		this.restClient = new RESTClient();
		restClient.createConnection(this.devToken);
	}

	public RESTClient getRestClient() {
		return restClient;
	}
}

