package com.vikinghammer.mlb.notifier.gae.model;

import java.util.Map;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;

/**
 * The info about a batter, loaded from the boxscore.
 * 
 * Each game will have a list of these for each of the home
 * team and the away team.
 * 
 * @author sschulte
 */
@Embed
@Data
public class Batter {
	
	private String name;
	private int ab;
	private int k;
	private int bb;
	private int hbp;
	private int h;
	private int hr;
	private int r;
	private int rbi;
	private int sb;
	private int cs;
	
	public void merge(Map<String, Object> map) {
		name = (String)map.get("name_display_first_last");
		ab = Integer.valueOf((String)map.get("ab"));
		k = Integer.valueOf((String)map.get("so"));
		bb = Integer.valueOf((String)map.get("bb"));
		hbp = Integer.valueOf((String)map.get("hbp"));
		h = Integer.valueOf((String)map.get("h"));
		hr = Integer.valueOf((String)map.get("hr"));
		r = Integer.valueOf((String)map.get("r"));
		rbi = Integer.valueOf((String)map.get("rbi"));
		sb = Integer.valueOf((String)map.get("sb"));
		cs = Integer.valueOf((String)map.get("cs"));
	}
	
	public Batter dup() {
		Batter dup = new Batter();
		dup.setName(new String(name));
		dup.setAb(ab);
		dup.setK(k);
		dup.setBb(bb);
		dup.setHbp(hbp);
		dup.setH(h);
		dup.setHr(hr);
		dup.setR(r);
		dup.setRbi(rbi);
		dup.setSb(sb);
		dup.setCs(cs);
		return dup;
	}

}
