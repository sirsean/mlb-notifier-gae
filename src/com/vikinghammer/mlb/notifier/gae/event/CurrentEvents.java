package com.vikinghammer.mlb.notifier.gae.event;

import static com.vikinghammer.mlb.notifier.gae.data.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;

/**
 * Determine all the current events that should be sent, as long as they
 * haven't already been sent.
 * 
 * @author sschulte
 */
public class CurrentEvents {
	
	private static EventDeterminer[] determiners;
	
	static {
		determiners = new EventDeterminer[] {
				new LeadChangeEventDeterminer(7),
				new PitcherEventDeterminer(5)
		};
	}
	
	public static List<Event> current(Game prev, Game next) {
		List<Event> events = new ArrayList<Event>();
		for (EventDeterminer determiner : determiners) {
			List<Event> determined = determiner.determine(prev, next);
			for (Event event : determined) {
				Event existing = ofy().load().key(Event.key(event.getId())).get();
				if (existing == null) {
					events.add(event);
				}
			}
		}
		return events;
	}

}
