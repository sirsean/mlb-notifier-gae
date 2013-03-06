package com.vikinghammer.mlb.notifier.gae.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.HomeAway;
import com.vikinghammer.mlb.notifier.gae.model.LineScore;
import com.vikinghammer.mlb.notifier.gae.model.Status;
import com.vikinghammer.mlb.notifier.gae.model.Team;

public class LeadChangeEventDeterminerTest {

	@Test
	public void wasScorelessHomeScores() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		
		Game two = one.dup();
		two.getStatus().setInning("6");
		two.getScore().getRuns().setHome(2);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("HOME broke the tie in the 6th, 2-0", event.getText());
	}
	
	@Test
	public void wasScorelessAwayScores() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		
		Game two = one.dup();
		two.getStatus().setInning("6");
		two.getScore().getRuns().setAway(4);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("AWAY broke the tie in the 6th, 4-0", event.getText());
	}
	
	@Test
	public void staysScoreless() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		
		Game two = one.dup();
		two.getStatus().setInning("6");
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(0, events.size());
	}
	
	@Test
	public void tooEarly() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		one.getStatus().setInning("2");
		
		Game two = one.dup();
		two.getStatus().setInning("3");
		two.getScore().getRuns().setHome(5);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(0, events.size());
	}
	
	@Test
	public void awayTiesItUp() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		one.getStatus().setInning("6");
		one.getScore().getRuns().setAway(3);
		one.getScore().getRuns().setHome(5);
		
		Game two = one.dup();
		two.getStatus().setInning("7");
		two.getScore().getRuns().setAway(5);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("AWAY tied it up in the 7th, 5-5", event.getText());
	}
	
	@Test
	public void homeTiesItUp() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		one.getStatus().setInning("6");
		one.getScore().getRuns().setAway(3);
		one.getScore().getRuns().setHome(2);
		
		Game two = one.dup();
		two.getStatus().setInning("7");
		two.getScore().getRuns().setHome(3);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("HOME tied it up in the 7th, 3-3", event.getText());
	}
	
	@Test
	public void awayTakesTheLead() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		one.getStatus().setInning("6");
		one.getScore().getRuns().setAway(3);
		one.getScore().getRuns().setHome(5);
		
		Game two = one.dup();
		two.getStatus().setInning("7");
		two.getScore().getRuns().setAway(6);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("AWAY took the lead in the 7th, 6-5", event.getText());
	}
	
	@Test
	public void homeTakesTheLead() {
		EventDeterminer determiner = new LeadChangeEventDeterminer(5);
		
		Game one = buildGame();
		one.getStatus().setInning("6");
		one.getScore().getRuns().setAway(3);
		one.getScore().getRuns().setHome(1);
		
		Game two = one.dup();
		two.getStatus().setInning("7");
		two.getScore().getRuns().setHome(5);
		
		List<Event> events = determiner.determine(one, two);
		assertEquals(1, events.size());
		Event event = events.get(0);
		assertNotNull(event.getId());
		assertEquals("one", event.getGameId());
		assertEquals("AWAY @ HOME", event.getTeams());
		assertEquals("HOME took the lead in the 7th, 5-3", event.getText());
	}
	
	private Game buildGame() {
		Game one = new Game();
		one.setGameId("one");
		Status status = new Status();
		status.setInning("4");
		one.setStatus(status);
		one.setAwayTeam(new Team());
		one.getAwayTeam().setAbbreviation("AWAY");
		one.setHomeTeam(new Team());
		one.getHomeTeam().setAbbreviation("HOME");
		LineScore score = new LineScore();
		score.setRuns(new HomeAway());
		score.getRuns().setAway(0);
		score.getRuns().setHome(0);
		one.setScore(score);
		
		return one;
	}

}
