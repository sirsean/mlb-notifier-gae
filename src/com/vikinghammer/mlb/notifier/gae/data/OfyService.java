package com.vikinghammer.mlb.notifier.gae.data;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.model.Event;

public class OfyService {
	static {
		factory().register(Game.class);
		factory().register(Event.class);
	}
	
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}

}
