package com.vikinghammer.mlb.notifier.gae.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vikinghammer.mlb.notifier.gae.model.Batter;
import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;

/**
 * We want to know if a batter is having a good game. So we'll
 * generate a notification if any of the batters in the game:
 * 
 * - Gets 4 or more H
 * - Gets 2 or more HR
 * - Gets 4 or more RBI
 * - Gets 4 or more R
 * - Gets 3 or more SB
 * 
 * @author sschulte
 */
public class BatterEventDeterminer implements EventDeterminer {

	private int minH = 4;
	private int minHr = 2;
	private int minRbi = 4;
	private int minR = 4;
	private int minSb = 3;
	
	@Override
	public List<Event> determine(Game prev, Game next) {
		List<Event> events = new ArrayList<Event>();
		
		if ((prev == null) || (next == null)) {
			return events;
		}
		
		List<Event> awayEvents = processBatters(next.getAwayBatters());
		for (Event event : awayEvents) {
			event.setGameId(next.getGameId());
			event.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			event.setHappenedAt(new Date());
			event.generateId();
			events.add(event);
		}
		
		List<Event> homeEvents = processBatters(next.getHomeBatters());
		for (Event event : homeEvents) {
			event.setGameId(next.getGameId());
			event.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			event.setHappenedAt(new Date());
			event.generateId();
			events.add(event);
		}
		
		return events;
	}
	
	private List<Event> processBatters(List<Batter> batters) {
		List<Event> events = new ArrayList<Event>();
		
		if (batters == null) {
			return events;
		}
		
		for (Batter batter : batters) {
			boolean hr = batter.getHr() >= minHr;
			boolean h = batter.getH() >= minH;
			boolean rbi = batter.getRbi() >= minRbi;
			boolean r = batter.getR() >= minR;
			boolean sb = batter.getSb() >= minSb;
			
			if (hr || h || rbi || r || sb) {
				StringBuilder text = new StringBuilder();
				text.append(String.format("%s has ", batter.getName()));
				
				int numItems = 0;
				
				if (hr) {
					text.append(String.format("%s HR", batter.getHr()));
					numItems++;
				}
				
				if (h) {
					if (numItems > 0) {
						text.append(", ");
					}
					text.append(String.format("%s H", batter.getH()));
					numItems++;
				}
				
				if (rbi) {
					if (numItems > 0) {
						text.append(", ");
					}
					text.append(String.format("%s RBI", batter.getRbi()));
					numItems++;
				}
				
				if (r) {
					if (numItems > 0) {
						text.append(", ");
					}
					text.append(String.format("%s R", batter.getR()));
					numItems++;
				}
				
				if (sb) {
					if (numItems > 0) {
						text.append(", ");
					}
					text.append(String.format("%s SB", batter.getSb()));
					numItems++;
				}
				
				Event event = new Event();
				event.setText(text.toString());
				events.add(event);
			}
		}
		
		return events;
	}

}
