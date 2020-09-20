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
public class Provider {
	/* storage access info */
	private final String storageIP;
	
	private final String storagePort;
	
	private final String storageSearchOrigAddr;
	
	private final String storageSearchProcAddr;
	
	private final String storageSearchAnalysisInputDataAddr;
	
	private final String storageKeyIndex;
	
	private final String storageKeydocType;
	
	private final String storageKeyValue;
	
	private final String storageKeySize;
	
	private final String storageKeySmsg;
	
	/* mapping table info for storage table accessing*/
	private final String origDataMapFile;
	
	private final String procDataMapFile;
	
	private final String mapKeyIndex;
	
	private final String mapKeyName;
	
	private final String mapKeyDocType;
	
	/* processing result info */
	private final int done;
	
	private final int none;
	
	/* web connection info */
	private final String webNone;
	
	private WebPostConnection searchOrigRequest;
	private WebPostConnection searchProcRequest;
	private WebPostConnection searchInputDataRequest;
	
	private JSONObject origDataTable;
	private JSONObject procDataTable;
	
	private JSONObject origDataDocTable;
	
	@Autowired
	public Provider(@Value("${properties.storage.ip}") String storageIP,
			@Value("${properties.storage.port}") String storagePort,
			@Value("${properties.storage.address.search.origData}") String storageSearchOrigAddr,
			@Value("${properties.storage.address.search.procData}") String storageSearchProcAddr,
			@Value("${properties.storage.address.search.analysisInputData}") String storageSearchAnalysisInputDataAddr,
			@Value("${properties.storage.req.param.index}") String storageKeyIndex,
			@Value("${properties.storage.req.param.docType}") String storageKeydocType,
			@Value("${properties.storage.req.param.value}") String storageKeyValue,
			@Value("${properties.storage.req.param.value}") String storageKeySize,
			@Value("${properties.storage.req.param.specialMessage}") String storageKeySmsg,
			
			@Value("${properties.map.origData.file}") String origDataMapFile,
			@Value("${properties.map.procData.file}") String procDataMapFile,
			@Value("${properties.map.origData.index}") String mapKeyIndex,
			@Value("${properties.map.origData.mapping_name}") String mapKeyName,
			@Value("${properties.map.origData.docType}") String mapKeyDocType,
			
			@Value("${properties.service.proc.done}") int done,
			@Value("${properties.service.proc.none}") int none,
			
			@Value("${properties.web.none}") String webNone) {
		// storage
		this.storageIP = storageIP;
		this.storagePort = storagePort;
		this.storageSearchOrigAddr = storageSearchOrigAddr;
		this.storageSearchProcAddr = storageSearchProcAddr;
		this.storageSearchAnalysisInputDataAddr = storageSearchAnalysisInputDataAddr;
		this.storageKeyIndex = storageKeyIndex;
		this.storageKeydocType = storageKeydocType;
		this.storageKeyValue = storageKeyValue;
		this.storageKeySize = storageKeySize;
		this.storageKeySmsg = storageKeySmsg;
		// mapping-table
		this.origDataMapFile = origDataMapFile;
		this.procDataMapFile = procDataMapFile;
		this.mapKeyIndex = mapKeyIndex;
		this.mapKeyName = mapKeyName;
		this.mapKeyDocType = mapKeyDocType;
		// processing result
		this.done = done;
		this.none = none;
		// web connection
		this.webNone = webNone;
	}
	
	@PostConstruct
	public void init() {
		String searchOrigDomain = storageIP+":"+storagePort+storageSearchOrigAddr;
		searchOrigRequest = new WebPostConnection(searchOrigDomain);
		
		String searchProcDomain = storageIP+":"+storagePort+storageSearchProcAddr;
		searchProcRequest = new WebPostConnection(searchProcDomain);
		
		String searchAnalysisDomain = storageIP+":"+storagePort+storageSearchAnalysisInputDataAddr;
		System.out.println("[Provider] queryAnalysisInputData - searchAnalysisDomain : "+ searchAnalysisDomain);
		searchInputDataRequest = new WebPostConnection(searchAnalysisDomain);
		
		JSONParser parser = new JSONParser();
		File fileMTorigData = new File(System.getProperty("user.dir") + origDataMapFile);
		File fileMTprocData = new File(System.getProperty("user.dir") + procDataMapFile);
		
		try {
			// parent folder & file create
			if (!fileMTorigData.getParentFile().exists())
				fileMTorigData.getParentFile().mkdirs();
			
			if (!fileMTorigData.exists())
				fileMTorigData.createNewFile();

			if (!fileMTprocData.getParentFile().exists())
				fileMTprocData.getParentFile().mkdirs();
			
			if (!fileMTprocData.exists())
				fileMTprocData.createNewFile();

			
			if (fileMTorigData.length() == 0)
				return;
			else {
				BufferedReader origReader = new BufferedReader(new FileReader(fileMTorigData));
			
				origDataTable = (JSONObject) parser.parse(origReader);
				origDataDocTable = (JSONObject) origDataTable.get(mapKeyDocType);
				
				origReader.close();
			}
			
			if (fileMTprocData.length() == 0)
				return;
			else {
				BufferedReader procReader = new BufferedReader(new FileReader(fileMTprocData));
				procDataTable = (JSONObject) parser.parse(procReader);
				
				procReader.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String query(String type, String size) {
		String result = "";
		searchOrigRequest.addParam(storageKeyIndex, (String) origDataTable.get(mapKeyIndex));
		searchOrigRequest.addParam(storageKeydocType, (String) origDataDocTable.get(type));
		searchOrigRequest.addParam(storageKeySize, size);
		
		result = searchOrigRequest.requestToURL();
		return result;
	}
	
	public String querySpecialData(String msgType) {
		String result = "";
		searchProcRequest.addParam(storageKeySmsg, msgType);
		result = searchOrigRequest.requestToURL();
		
		return result;
	}
	
	public String queryAnalysisInputData(String moduleId) {
		String result = "";
		searchInputDataRequest.addParam("moduleId", moduleId);
		result = searchInputDataRequest.requestToURL();
		System.out.println("[Provider] queryAnalysisInputData result : "+ result);
		return result;
	}
}