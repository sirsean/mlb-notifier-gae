package com.vikinghammer.mlb.notifier.gae.event;

import java.util.List;

import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;

/**
 * Determine whether any noteworthy events have occurred between the given
 * copies of the Game.
 * 
 * If no noteworthy events have happened, return an empty list. Any Event 
 * objects that are returned *must* have a gameId and a text, and you
 * *must* call generateId() on them before returning.
 * 
 * @author sschulte
 */
public interface EventDeterminer {
	
	public List<Event> determine(Game prev, Game next);

}
