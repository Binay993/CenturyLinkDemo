package com.centurylink.findmyfollowers.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

public class Model {

	/* This is write only because it is not needed in the response json */
	@JsonProperty(access = Access.WRITE_ONLY)
	private String login;

	/* This will keep track of the depth but ignored in the response */
	@JsonIgnore
	private int level;

	/* This is the id/username */
	private String id;

	private List<Model> followers;
	
	public Model() {
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Model> getFollowers() {
		return followers;
	}

	public void setFollowers(List<Model> followers) {
		this.followers = followers;
	}
	
	

}
