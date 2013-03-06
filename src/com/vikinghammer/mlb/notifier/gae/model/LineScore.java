package com.vikinghammer.mlb.notifier.gae.model;

import java.util.Map;

import javax.persistence.Embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.googlecode.objectify.annotation.Embed;

/**
 * The full game runs/hits/errors score.
 * 
 * @author sschulte
 */
@Embed
@NoArgsConstructor
@Data
public class LineScore {

	@Embedded
	private HomeAway runs;
	
	@Embedded
	private HomeAway hits;
	
	@Embedded
	private HomeAway errors;
	
	public void merge(Map<String, Object> map) {
		if (runs == null) {
			runs = new HomeAway();
		}
		runs.merge((Map<String, Object>)map.get("r"));
		if (hits == null) {
			hits = new HomeAway();
		}
		hits.merge((Map<String, Object>)map.get("h"));
		if (errors == null) {
			errors = new HomeAway();
		}
		errors.merge((Map<String, Object>)map.get("e"));
	}
	
	public LineScore dup() {
		LineScore dup = new LineScore();
		if (runs != null) {
			dup.setRuns(runs.dup());
		}
		if (hits != null) {
			dup.setHits(hits.dup());
		}
		if (errors != null) {
			dup.setErrors(errors.dup());
		}
		return dup;
	}
	
}
