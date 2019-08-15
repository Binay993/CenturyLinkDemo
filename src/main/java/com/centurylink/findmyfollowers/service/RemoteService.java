package com.centurylink.findmyfollowers.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.centurylink.findmyfollowers.models.Model;

@Component
public class RemoteService {

	private String authorization;
	private String githubUrl;
	private HttpEntity<String> header;
	private String cliendId;
	private String clientSecret;
	

	public RemoteService(@Autowired Environment env) {
		
		this.authorization = env.getProperty("github.oauth.id");
		this.githubUrl = env.getProperty("github.api.url");
		this.cliendId = env.getProperty("github.api.client.id");
		this.clientSecret = env.getProperty("github.api.client.secret");				
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authorization);
		headers.add("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		header = new HttpEntity<String>(headers);


	}

	public Model[] get(String id) {
		RestTemplate restTemplate = new RestTemplate();
		String requestUrl = githubUrl + "/" + id + "/followers" + "?client_id="+cliendId+"&client_secret="+clientSecret;
		ResponseEntity<Model[]> response = restTemplate.exchange(requestUrl, HttpMethod.GET, header, Model[].class);
		if (response.getStatusCode() == HttpStatus.OK) {
			return response.getBody();
		}
		return null;
	}
}
