package com.vikinghammer.mlb.notifier.gae.model;

import java.util.Map;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;

/**
 * The info about a pitcher, loaded from the boxscore. Each game will
 * have at least one of these for both the home team and the away team.
 * 
 * @author sschulte
 */
@Embed
@Data
public class Pitcher {
	
	private String name;
	private int outs;
	private int bf;
	private int bb;
	private int k;
	private int h;
	private int er;
	private int r;
	private int pitches;
	private int strikes;
	
	public void merge(Map<String, Object> map) {
		name = (String)map.get("name_display_first_last");
		outs = Integer.valueOf((String)map.get("out"));
		bf = Integer.valueOf((String)map.get("bf"));
		bb = Integer.valueOf((String)map.get("bb"));
		k = Integer.valueOf((String)map.get("so"));
		h = Integer.valueOf((String)map.get("h"));
		er = Integer.valueOf((String)map.get("er"));
		r = Integer.valueOf((String)map.get("r"));
		pitches = Integer.valueOf((String)map.get("np"));
		strikes = Integer.valueOf((String)map.get("s"));
	}
	
	public Pitcher dup() {
		Pitcher dup = new Pitcher();
		dup.setName(new String(name));
		dup.setBf(bf);
		dup.setBb(bb);
		dup.setK(k);
		dup.setH(h);
		dup.setEr(er);
		dup.setR(r);
		dup.setPitches(pitches);
		dup.setStrikes(strikes);
		return dup;
	}
}
