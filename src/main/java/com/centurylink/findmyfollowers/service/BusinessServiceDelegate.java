package com.centurylink.findmyfollowers.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.centurylink.findmyfollowers.models.Model;

@Component
public class BusinessServiceDelegate {

	BusinessService businessService;

	public BusinessServiceDelegate(@Autowired BusinessService businessService) {
		this.businessService = businessService;
	}

	public List<Model> findAll(String id, int startLevel, int maxLevel, int sizePerLevel) {
		return  businessService.process(id, startLevel, maxLevel, sizePerLevel);
	}

}