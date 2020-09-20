package com.ssu.ISCOM.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ssu.ISCOM.WebPostConnection;

@Service
public class Loader {
	/* storage access info */
	private final String storageIP;
	
	private final String storagePort;
	
	private final String storageLoadAddr;
	
	private final String storageKeyIndex;
	
	private final String storageKeydocType;
	
	private final String storageKeyValue;

	/* mapping table info for storage table accessing*/
	private final String origDataMapFile;
	
	private final String mapKeyIndex;
	
	private final String mapKeyName;
	
	private final String mapKeyDocType;
	
	/* processing result info */
	private final int done;
	
	private final int none;
	
	/* web connection info */
	private final String webNone;
	
	private WebPostConnection storageRequest;
	private JSONObject origDataTable;
	private JSONObject origDataDocTable;
	
	@Autowired
	public Loader(@Value("${properties.storage.ip}") String storageIP,
			@Value("${properties.storage.port}") String storagePort,
			@Value("${properties.storage.address.load}") String storageLoadAddr,
			@Value("${properties.storage.req.param.index}") String storageKeyIndex,
			@Value("${properties.storage.req.param.docType}") String storageKeydocType,
			@Value("${properties.storage.req.param.value}") String storageKeyValue,
			@Value("${properties.map.origData.file}") String origDataMapFile,
			@Value("${properties.map.origData.index}") String mapKeyIndex,
			@Value("${properties.map.origData.mapping_name}") String mapKeyName,
			@Value("${properties.map.origData.docType}") String mapKeyDocType,
			@Value("${properties.service.proc.done}") int done,
			@Value("${properties.service.proc.none}") int none,
			@Value("${properties.web.none}") String webNone) {
		this.storageIP = storageIP;
		this.storagePort = storagePort;
		this.storageLoadAddr = storageLoadAddr;
		this.storageKeyIndex = storageKeyIndex;
		this.storageKeydocType = storageKeydocType;
		this.storageKeyValue = storageKeyValue;
		this.origDataMapFile = origDataMapFile;
		this.mapKeyIndex = mapKeyIndex;
		this.mapKeyName = mapKeyName;
		this.mapKeyDocType = mapKeyDocType;
		this.done = done;
		this.none = none;
		this.webNone = webNone;
	}
	
	@PostConstruct
	public void init() {
		String loadDomain = storageIP+":"+storagePort+storageLoadAddr;
		System.out.println("loadDomain : "+loadDomain);
		storageRequest = new WebPostConnection(loadDomain);
		
		File fileMTorigData = null;
		
		try {
			fileMTorigData = new File(System.getProperty("user.dir") + origDataMapFile);
			if (!fileMTorigData.getParentFile().exists())
				fileMTorigData.getParentFile().mkdirs();
			
			if (!fileMTorigData.exists())
				fileMTorigData.createNewFile();

			if (fileMTorigData.length() == 0)
				return;
			else {
				BufferedReader bfReader = new BufferedReader(new FileReader(fileMTorigData));
				JSONParser parser = new JSONParser();
				origDataTable = (JSONObject) parser.parse(bfReader);
				origDataDocTable = (JSONObject) origDataTable.get(mapKeyDocType);
				System.out.println("[Loader] origData table : " +origDataTable);
				
				bfReader.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int load(String type, String value) {
		int isLoad = 0;
		storageRequest.addParam(storageKeyIndex, (String) origDataTable.get(mapKeyIndex));
		storageRequest.addParam(storageKeydocType, (String) origDataDocTable.get(type));
		storageRequest.addParam(storageKeyValue, value);
		
		System.out.println("[Loader] load - type :"+ (String) origDataDocTable.get(type) +", value : "+ value);
		if(storageRequest.requestToURL().equals(webNone)) {
			isLoad = none;
		} else {
			isLoad = done;
		}
		
		return isLoad;
	}
}
