package com.ssu.ISCOM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssu.ISCOM.service.*;

@Controller
public class InputController {
	
	@Autowired
	Loader storageLoader;
	Provider provider;

	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	@ResponseBody
	public String hello() {
		return "hello world! ";
	}
	
	@RequestMapping(value="/SilverCM/collector" , method = RequestMethod.POST)
	@ResponseBody
	public String collect(String id, String type, String value) {
		int isLoad = storageLoader.load(type, value);
		System.out.println("[InputCollector] load result : "+ isLoad);
		return ""+isLoad;
	}
	
	@RequestMapping(value="/SilverCM/register", method = RequestMethod.POST)
	@ResponseBody
	public String registerCollector(String id, String type, String value) {
		
		return "";
	}
}
