package com.vikinghammer.mlb.notifier.gae.model;

import java.util.Map;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;

/**
 * An individual piece of the LineScore. There will be one of these
 * for each of Runs, Hits, and Errors.
 * 
 * @author sschulte
 */
@Embed
@Data
public class HomeAway {
	
	private int away;
	private int home;
	
	public void merge(Map<String, Object> map) {
		away = Integer.valueOf((String)map.get("away"));
		home = Integer.valueOf((String)map.get("home"));
	}
	
	public HomeAway dup() {
		HomeAway dup = new HomeAway();
		dup.setAway(away);
		dup.setHome(home);
		return dup;
	}

}
