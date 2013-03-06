package com.vikinghammer.mlb.notifier.gae.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.Team;
import com.vikinghammer.mlb.notifier.gae.util.Inning;

/**
 * We want to know if the lead changes late in the game.
 * 
 * So if the game has passed a certain point (set in the constructor),
 * we'll check the difference between the current Game object and the
 * last once we knew about before this update. You'll be notified if
 * the lead changes:
 * 
 * - It was tied, and now it isn't any more.
 * - One team was winning, and now it's tied.
 * - One team was winning, and now the other team is winning.
 * 
 * @author sschulte
 */
public class LeadChangeEventDeterminer implements EventDeterminer {

	private int inningOrLater;
	
	public LeadChangeEventDeterminer(int inningOrLater) {
		this.inningOrLater = inningOrLater;
	}
	
	@Override
	public List<Event> determine(Game prev, Game next) {
		List<Event> events = new ArrayList<Event>();
		
		if ((prev == null) || (next == null)) {
			return events;
		}
		
		if ((next.getStatus() == null) || (next.getStatus().getInning() == null)) {
			return events;
		}
		
		int inning = 0;
		try {
			inning = Integer.valueOf(next.getStatus().getInning());
		} catch (Exception e) {
			return events;
		}
		if (inning < inningOrLater) {
			return events;
		}
		
		Team oldLeader = prev.currentLeader();
		Team newLeader = next.currentLeader();
		
		Event event = null;
		if ((oldLeader == null) && (newLeader != null)) {
			// it was tied, but now it isn't
			event = new Event();
			event.setGameId(next.getGameId());
			event.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			event.setHappenedAt(new Date());
			
			int winningScore = 0, losingScore = 0;
			if (newLeader.equals(next.getAwayTeam())) {
				winningScore = next.getScore().getRuns().getAway();
				losingScore = next.getScore().getRuns().getHome();
			} else {
				winningScore = next.getScore().getRuns().getHome();
				losingScore = next.getScore().getRuns().getAway();
			}
			event.setText(String.format("%s broke the tie in the %s, %s-%s", newLeader.getAbbreviation(), Inning.friendly(inning), winningScore, losingScore));
		} else if ((oldLeader != null) && (newLeader == null)) {
			// it wasn't tied, but now it is
			event = new Event();
			event.setGameId(next.getGameId());
			event.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			event.setHappenedAt(new Date());
			
			if (oldLeader.equals(next.getAwayTeam())) {
				newLeader = next.getHomeTeam();
			} else {
				newLeader = next.getAwayTeam();
			}
			
			int winningScore = 0, losingScore = 0;
			if ((next.getScore() != null) && (next.getScore().getRuns() != null)) {
				winningScore = next.getScore().getRuns().getHome();
				losingScore = next.getScore().getRuns().getAway();
			}
			event.setText(String.format("%s tied it up in the %s, %s-%s", newLeader.getAbbreviation(), Inning.friendly(inning), winningScore, losingScore));
		} else if ((oldLeader != null) && !oldLeader.equals(newLeader)) {
			// lead change! (it wasn't tied, but the leader changed)
			event = new Event();
			event.setGameId(next.getGameId());
			event.setTeams(String.format("%s @ %s", next.getAwayTeam().getAbbreviation(), next.getHomeTeam().getAbbreviation()));
			event.setHappenedAt(new Date());
			
			int winningScore = 0, losingScore = 0;
			if (newLeader.equals(next.getAwayTeam())) {
				winningScore = next.getScore().getRuns().getAway();
				losingScore = next.getScore().getRuns().getHome();
			} else {
				winningScore = next.getScore().getRuns().getHome();
				losingScore = next.getScore().getRuns().getAway();
			}
			event.setText(String.format("%s took the lead in the %s, %s-%s", newLeader.getAbbreviation(), Inning.friendly(inning), winningScore, losingScore));
		}
		
		if (event != null) {
			event.generateId();
			events.add(event);
		}
		
		return events;
	}

}
