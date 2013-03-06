package com.vikinghammer.mlb.notifier.gae.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.Pitcher;

/**
 * We want to know if a pitcher is having a good game. Once the game passes
 * a certain inning (set in the constructor), we'll notify if one (or both)
 * of the pitchers in the game has:
 * 
 * - A perfect game
 * - A no-hitter
 * - A shutout
 * 
 * Note that if the pitcher has a perfect game, they also have both a
 * no-hitter and a shutout, but only ONE event is created (the one for
 * the perfect game).
 * 
 * @author sschulte
 */
public class PitcherEventDeterminer implements EventDeterminer {
	
	private int inningOrLater;
	
	public PitcherEventDeterminer(int inningOrLater) {
		this.inningOrLater = inningOrLater;
	}

	@Override
	public List<Event> determine(Game prev, Game next) {
		List<Event> events = new ArrayList<Event>();
		
		if ((prev == null) || (next == null)) {
			return events;
		}
		
		Event away = processPitchers(next.getAwayPitchers());
		if (away != null) {
			away.setGameId(next.getGameId());
			away.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			away.setHappenedAt(new Date());
			away.generateId();
			events.add(away);
		}
		
		Event home = processPitchers(next.getHomePitchers());
		if (home != null) {
			home.setGameId(next.getGameId());
			home.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			home.setHappenedAt(new Date());
			home.generateId();
			events.add(home);
		}
		
		return events;
	}
	
	private Event processPitchers(List<Pitcher> pitchers) {
		if ((pitchers == null) || (pitchers.size() != 1)) {
			return null;
		}
		
		Pitcher pitcher = pitchers.get(0);
		
		int minimumOuts = inningOrLater * 3;
		
		// have they been pitching long enough?
		if (pitcher.getOuts() < minimumOuts) {
			return null;
		}
		
		int innings = (pitcher.getOuts() / 3);
		
		Event event = null;
		
		// figure out if it's a perfect game (outs equals batters faced)
		if (pitcher.getOuts() == pitcher.getBf()) {
			event = new Event();
			event.setText(String.format("%s is perfect through %s", pitcher.getName(), innings));
		}
		
		// if it's not a perfect game, is it a no-hitter?
		if ((event == null) && (pitcher.getH() == 0)) {
			event = new Event();
			event.setText(String.format("%s has a no-hitter through %s", pitcher.getName(), innings));
		}
		
		// if it's neither perfect nor no-hitter, is it a shutout?
		if ((event == null) && (pitcher.getR() == 0)) {
			event = new Event();
			event.setText(String.format("%s has a shutout through %s", pitcher.getName(), innings));
		}
		
		return event;
	}

}
