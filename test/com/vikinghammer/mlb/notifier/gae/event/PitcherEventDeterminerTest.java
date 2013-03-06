package com.vikinghammer.mlb.notifier.gae.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.Pitcher;
import com.vikinghammer.mlb.notifier.gae.model.Team;

public class PitcherEventDeterminerTest {
	
	@Test
	public void noPitchers() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = new Game();
		
		List<Event> events = determiner.determine(game, game);
		assertTrue(events.isEmpty());
	}

	@Test
	public void notLongEnough() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = new Game();
		game.setGameId("one");
		Pitcher p0 = new Pitcher();
		p0.setOuts(0);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setOuts(6);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertTrue(events.isEmpty());
	}
	
	@Test
	public void neitherPitcher() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = new Game();
		game.setGameId("one");
		Pitcher p0 = new Pitcher();
		p0.setOuts(18);
		p0.setBf(20);
		p0.setH(2);
		p0.setR(1);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setOuts(20);
		p1.setBf(21);
		p1.setH(1);
		p1.setR(1);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertTrue(events.isEmpty());
	}
	
	@Test
	public void awayPerfect() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(20);
		p0.setBf(20);
		p0.setH(0);
		p0.setR(0);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setOuts(6);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event away = events.get(0);
		assertNotNull(away.getId());
		assertEquals("one", away.getGameId());
		assertEquals("AWAY @ HOME", away.getTeams());
		assertEquals("Cool Guy is perfect through 6", away.getText());
	}
	
	@Test
	public void homePerfect() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(22);
		p0.setBf(22);
		p0.setH(0);
		p0.setR(0);
		Pitcher p1 = new Pitcher();
		p1.setOuts(6);
		
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p1}));
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p0}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event home = events.get(0);
		assertNotNull(home.getId());
		assertEquals("one", home.getGameId());
		assertEquals("AWAY @ HOME", home.getTeams());
		assertEquals("Cool Guy is perfect through 7", home.getText());
	}
	
	@Test
	public void bothPerfect() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(20);
		p0.setBf(20);
		p0.setH(0);
		p0.setR(0);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setName("Other Dude");
		p1.setOuts(15);
		p1.setBf(15);
		p1.setH(0);
		p1.setR(0);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(2, events.size());
		Event away = events.get(0);
		assertNotNull(away.getId());
		assertEquals("one", away.getGameId());
		assertEquals("AWAY @ HOME", away.getTeams());
		assertEquals("Cool Guy is perfect through 6", away.getText());
		Event home = events.get(1);
		assertNotNull(home.getId());
		assertEquals("one", home.getGameId());
		assertEquals("AWAY @ HOME", home.getTeams());
		assertEquals("Other Dude is perfect through 5", home.getText());
	}
	
	@Test
	public void awayNoHitterHomeShutout() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(20);
		p0.setBf(22);
		p0.setH(0);
		p0.setR(0);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setName("Other Dude");
		p1.setOuts(15);
		p1.setBf(20);
		p1.setH(4);
		p1.setR(0);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(2, events.size());
		Event away = events.get(0);
		assertNotNull(away.getId());
		assertEquals("one", away.getGameId());
		assertEquals("AWAY @ HOME", away.getTeams());
		assertEquals("Cool Guy has a no-hitter through 6", away.getText());
		Event home = events.get(1);
		assertNotNull(home.getId());
		assertEquals("one", home.getGameId());
		assertEquals("AWAY @ HOME", home.getTeams());
		assertEquals("Other Dude has a shutout through 5", home.getText());
	}
	
	@Test
	public void homeNoHitter() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(20);
		p0.setBf(22);
		p0.setH(1);
		p0.setR(1);
		game.setAwayPitchers(Arrays.asList(new Pitcher[]{p0}));
		Pitcher p1 = new Pitcher();
		p1.setName("Other Dude");
		p1.setOuts(15);
		p1.setBf(17);
		p1.setH(0);
		p1.setR(0);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertEquals(1, events.size());
		Event home = events.get(0);
		assertNotNull(home.getId());
		assertEquals("one", home.getGameId());
		assertEquals("AWAY @ HOME", home.getTeams());
		assertEquals("Other Dude has a no-hitter through 5", home.getText());
	}
	
	@Test
	public void removedWithAPerfectGameGoing() {
		EventDeterminer determiner = new PitcherEventDeterminer(5);
		
		Game game = buildGame();
		
		Pitcher p0 = new Pitcher();
		p0.setName("Cool Guy");
		p0.setOuts(20);
		p0.setBf(20);
		p0.setH(0);
		p0.setR(0);
		Pitcher p1 = new Pitcher();
		p1.setName("Other Dude");
		p1.setOuts(2);
		p1.setBf(2);
		p1.setH(0);
		p1.setR(0);
		game.setHomePitchers(Arrays.asList(new Pitcher[]{p0, p1}));
		
		List<Event> events = determiner.determine(game, game);
		assertTrue(events.isEmpty());
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
