package com.vikinghammer.mlb.notifier.gae.model;

import java.util.Map;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;

/**
 * The game's status includes the inning, which we'll typically care about.
 * 
 * @author sschulte
 */
@Embed
@Data
public class Status {
	
	private String status;
	private String inning;
	private String inningState;
	
	public void merge(Map<String, Object> map) {
		status = (String)map.get("status");
		inning = (String)map.get("inning");
		inningState = (String)map.get("inning_state");
	}
	
	public Status dup() {
		Status dup = new Status();
		if (status != null) {
			dup.setStatus(new String(status));
		}
		if (inning != null) {
			dup.setInning(new String(inning));
		}
		if (inningState != null) {
			dup.setInningState(new String(inningState));
		}
		return dup;
	}

}
