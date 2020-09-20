package com.ssu.ISCOM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ssu.ISCOM.service.Loader;
import com.ssu.ISCOM.service.Provider;

@Controller
public class DataProvidingController {
	
	@Autowired
	Loader storageLoader;
	
	@Autowired
	Provider provider;
	
	@RequestMapping(value = "/helloProvider", method = RequestMethod.GET)
	@ResponseBody
	public String hello() {
		return "hello world! provider";
	}
	
	@RequestMapping(value="/SilverCM/get/origData", method = RequestMethod.POST)
	@ResponseBody
	public String provideData(String id, String type, String size) {
		/* provide raw data and analysis and processed data */
		// need to setting
		String result = provider.query(type, size);
		
		return result;
	}
	
	@RequestMapping(value="/SilverCM/get/procData", method = RequestMethod.POST)
	@ResponseBody
	public String provideProcData(String procId) {
		// provide to need that operation of storage
		// need to setting
		String result = provider.querySpecialData(procId); 
		
		return result;
	}
	
	@RequestMapping(value="/SilverCM/get/inputData", method = RequestMethod.POST)
	@ResponseBody
	public String provideAnalysisInputData(String moduleId) {
		// provide to need that operation of storage
		String result = "";
		System.out.println("["+this.getClass()+"] request inputData - moduleId : "+ moduleId);
		result = provider.queryAnalysisInputData(moduleId); 
		
		return result;
	}
}
