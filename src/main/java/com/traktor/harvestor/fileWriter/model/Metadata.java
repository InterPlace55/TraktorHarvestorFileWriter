package com.traktor.harvestor.fileWriter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
	private Track track;

	public Metadata() {
		
	}
	
	public Metadata(Track track) {
		this.track = track;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}
	
	
}
