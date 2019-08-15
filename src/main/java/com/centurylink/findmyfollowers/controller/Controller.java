package com.centurylink.findmyfollowers.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.centurylink.findmyfollowers.models.Model;
import com.centurylink.findmyfollowers.service.BusinessServiceDelegate;

@RestController
@RequestMapping(path = "/user")
public class Controller {

	private BusinessServiceDelegate businessServiceDelegate;
	
	public Controller(@Autowired BusinessServiceDelegate businessServiceDelegate) {
		this.businessServiceDelegate = businessServiceDelegate;
	}

	@RequestMapping(value = "/{id}/followers", method = RequestMethod.GET)
	public List<Model> findAll(@PathVariable(value="id") String id,
			@RequestParam(value="startlevel",defaultValue = "1") int startLevel,
			@RequestParam(value="maxlevel",defaultValue = "5") int maxLevel,
			@RequestParam(value="sizeperlevel",defaultValue = "5") int sizePerLevel
			) {
		 return businessServiceDelegate.findAll(id, startLevel, maxLevel, sizePerLevel);
	}

}
