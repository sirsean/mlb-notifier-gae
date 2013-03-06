package com.vikinghammer.mlb.notifier.gae.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * A single game. It will be updated several times, once with each update.
 * 
 * @author sschulte
 */
@Entity
@NoArgsConstructor
@Data
public class Game {
	
	public static Key<Game> key(String id) {
		return Key.create(Game.class, id);
	}
	
	@Id
	private String gameId;
	
	@Embedded
	private Status status;
	
	@Embedded
	private Team awayTeam;
	
	@Embedded
	private Team homeTeam;
	
	@Embedded
	private List<Pitcher> awayPitchers;
	
	@Embedded
	private List<Pitcher> homePitchers;
	
	@Embedded
	private LineScore score;
	
	public Team currentLeader() {
		if (score == null) {
			return null;
		}
		if (score.getRuns() == null) {
			return null;
		}
		if (score.getRuns().getHome() > score.getRuns().getAway()) {
			return homeTeam;
		} else if (score.getRuns().getAway() > score.getRuns().getHome()) {
			return awayTeam;
		} else {
			return null;
		}
	}
	
	public Game dup() {
		Game game = new Game();
		game.setGameId(new String(gameId));
		if (status != null) {
			game.setStatus(status.dup());
		}
		if (awayTeam != null) {
			game.setAwayTeam(awayTeam.dup());
		}
		if (homeTeam != null) {
			game.setHomeTeam(homeTeam.dup());
		}
		List<Pitcher> dupAwayPitchers = new ArrayList<Pitcher>();
		if (awayPitchers != null) {
			for (Pitcher pitcher : awayPitchers) {
				dupAwayPitchers.add(pitcher.dup());
			}
		}
		game.setAwayPitchers(dupAwayPitchers);
		List<Pitcher> dupHomePitchers = new ArrayList<Pitcher>();
		if (homePitchers != null) {
			for (Pitcher pitcher : homePitchers) {
				dupHomePitchers.add(pitcher.dup());
			}
		}
		game.setHomePitchers(dupHomePitchers);
		if (score != null) {
			game.setScore(score.dup());
		}
		return game;
	}
	
	public void merge(Map<String, Object> map) {
		gameId = (String)map.get("id");
		
		if (map.containsKey("status") && (map.get("status") instanceof Map)) {
			if (status == null) {
				status = new Status();
			}
			status.merge((Map<String, Object>)map.get("status"));
		}
		
		if (map.containsKey("linescore") && (map.get("linescore") instanceof Map)) {
			if (score == null) {
				score = new LineScore();
			}
			score.merge((Map<String, Object>)map.get("linescore"));
		}
		
		if (awayTeam == null) {
			awayTeam = new Team();
		}
		awayTeam.setAbbreviation((String)map.get("away_name_abbrev"));
		awayTeam.setCity((String)map.get("away_team_city"));
		awayTeam.setName((String)map.get("away_team_name"));
		
		if (homeTeam == null) {
			homeTeam = new Team();
		}
		homeTeam.setAbbreviation((String)map.get("home_name_abbrev"));
		homeTeam.setCity((String)map.get("home_team_city"));
		homeTeam.setName((String)map.get("home_team_name"));
	}
	
	public void mergeBoxscore(Map<String, Object> map) {
		awayPitchers = new ArrayList<Pitcher>();
		homePitchers = new ArrayList<Pitcher>();
		if (map.containsKey("pitching")) {
			List<Map<String, Object>> pitching = (List<Map<String, Object>>)map.get("pitching");
			if (pitching.size() >= 1) {
				List<Map<String, Object>> pitchers = getPitchers(pitching.get(0));
				for (Map<String, Object> pitcherMap : pitchers) {
					Pitcher pitcher = new Pitcher();
					pitcher.merge(pitcherMap);
					awayPitchers.add(pitcher);
				}
			}
			if (pitching.size() >= 2) {
				List<Map<String, Object>> pitchers = getPitchers(pitching.get(1));
				for (Map<String, Object> pitcherMap : pitchers) {
					Pitcher pitcher = new Pitcher();
					pitcher.merge(pitcherMap);
					homePitchers.add(pitcher);
				}
			}
		}
	}

	private List<Map<String, Object>> getPitchers(Map<String, Object> pitching) {
		List<Map<String, Object>> pitchers;
		if (pitching.get("pitcher") instanceof List) {
			pitchers = (List<Map<String, Object>>)pitching.get("pitcher");
		} else {
			pitchers = new ArrayList<Map<String, Object>>();
			pitchers.add((Map<String, Object>)pitching.get("pitcher"));
		}
		return pitchers;
	}
	
}
