package com.vikinghammer.mlb.notifier.gae.event;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.vikinghammer.mlb.notifier.gae.model.Batter;
import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.Team;

public class BatterEventDeterminerTest {
	
	EventDeterminer determiner;
	
	@Before
	public void setup() {
		determiner = new BatterEventDeterminer();
	}
	
	@Test
	public void noBatters() {
		Game game = new Game();
		
		List<Event> events = determiner.determine(game, game);
		assertTrue(events.isEmpty());
	}

	@Test
	public void hasHrHRbiRSb() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(2);
		b0.setH(4);
		b0.setRbi(5);
		b0.setR(4);
		b0.setSb(3);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 2 HR, 4 H, 5 RBI, 4 R, 3 SB", e0.getText());
	}
	
	@Test
	public void hasHrRbi() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(3);
		b0.setH(3);
		b0.setRbi(5);
		b0.setR(3);
		b0.setSb(2);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 3 HR, 5 RBI", e0.getText());
	}
	
	@Test
	public void hasHR() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(5);
		b0.setRbi(3);
		b0.setR(4);
		b0.setSb(2);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 5 H, 4 R", e0.getText());
	}
	
	@Test
	public void hasHSb() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(4);
		b0.setRbi(3);
		b0.setR(3);
		b0.setSb(3);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 4 H, 3 SB", e0.getText());
	}
	
	@Test
	public void hasHr() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(2);
		b0.setH(2);
		b0.setRbi(2);
		b0.setR(2);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 2 HR", e0.getText());
	}
	
	@Test
	public void hasH() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(4);
		b0.setRbi(2);
		b0.setR(2);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 4 H", e0.getText());
	}
	
	@Test
	public void hasRbi() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(3);
		b0.setRbi(4);
		b0.setR(3);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 4 RBI", e0.getText());
	}
	
	@Test
	public void hasR() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(3);
		b0.setRbi(3);
		b0.setR(4);
		b0.setSb(2);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 4 R", e0.getText());
	}
	
	@Test
	public void hasSb() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		b0.setHr(1);
		b0.setH(3);
		b0.setRbi(3);
		b0.setR(3);
		b0.setSb(3);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B0 has 3 SB", e0.getText());
	}
	
	@Test
	public void multipleBatters() {
		Game game = buildGame();
		
		Batter b0 = new Batter();
		b0.setName("B0");
		Batter b1 = new Batter();
		b1.setName("B1");
		b1.setH(4);
		Batter b2 = new Batter();
		b2.setName("B2");
		Batter b3 = new Batter();
		b3.setName("B3");
		b3.setHr(3);
		b3.setR(5);
		Batter b4 = new Batter();
		b4.setName("B4");
		Batter b5 = new Batter();
		b5.setName("B5");
		b5.setRbi(4);
		b5.setSb(3);
		
		game.setAwayBatters(Arrays.asList(new Batter[]{b0,b1,b2}));
		game.setHomeBatters(Arrays.asList(new Batter[]{b3,b4,b5}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(3, events.size());
		Event e0 = events.get(0);
		assertEquals("one", e0.getGameId());
		assertEquals("AWAY @ HOME", e0.getTeams());
		assertEquals("B1 has 4 H", e0.getText());
		Event e1 = events.get(1);
		assertEquals("one", e1.getGameId());
		assertEquals("AWAY @ HOME", e1.getTeams());
		assertEquals("B3 has 3 HR, 5 R", e1.getText());
		Event e2 = events.get(2);
		assertEquals("one", e2.getGameId());
		assertEquals("AWAY @ HOME", e2.getTeams());
		assertEquals("B5 has 4 RBI, 3 SB", e2.getText());
	}
	
	private Game buildGame() {
		Game one = new Game();
		one.setGameId("one");
		one.setAwayTeam(new Team());
		one.getAwayTeam().setAbbreviation("AWAY");
		one.setHomeTeam(new Team());
		one.getHomeTeam().setAbbreviation("HOME");
		
		return one;
	}

}
