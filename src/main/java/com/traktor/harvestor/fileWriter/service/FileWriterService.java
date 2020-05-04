package com.traktor.harvestor.fileWriter.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.traktor.harvestor.fileWriter.model.Metadata;
import com.traktor.harvestor.fileWriter.model.Track;

@Component
public class FileWriterService {

	private static final Logger log = LoggerFactory.getLogger(FileWriterService.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${harvestor.url}")
	private String url;
	
	@Value("${harvestor.port}")
	private String port;
	
	@Value("${harvestor.guid}")
	private String guid;
	
	@Value("${file.location}")
	private String fileLocation;
	
	private Track getTrackInfo() {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();        
		//Add the Jackson Message converter
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

		// Note: here we are making this converter to process any kind of response, 
		// not only application/*json, which is the default behaviour
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));        
		messageConverters.add(converter);  
		restTemplate.setMessageConverters(messageConverters); 
		
		Metadata meta = restTemplate.getForObject(
				"http://" + url + ":" + port + "/api/" + guid + "/current", Metadata.class);
		log.info(meta.getTrack().getArtist() + " - " + meta.getTrack().getTitle());
		
		return meta.getTrack();
	}
	
	@Scheduled(fixedRate=30000)
	public void writeToFile() throws IOException {
		
		Track track = getTrackInfo();
		
		File file = new File(fileLocation);
		FileWriter fileWriter = new FileWriter(file, false);
		
		fileWriter.write(track.getArtist() + " - " + track.getTitle() + " ");
		fileWriter.close();
	}
	
}
