package com.vikinghammer.mlb.notifier.gae;

import static com.vikinghammer.mlb.notifier.gae.data.OfyService.ofy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vikinghammer.mlb.notifier.gae.email.EventSender;
import com.vikinghammer.mlb.notifier.gae.event.CurrentEvents;
import com.vikinghammer.mlb.notifier.gae.model.Event;
import com.vikinghammer.mlb.notifier.gae.model.Game;
import com.vikinghammer.mlb.notifier.gae.net.Downloader;
import com.vikinghammer.mlb.notifier.gae.parse.Parser;

/**
 * Handle the /notify call, which is called via cron every five minutes.
 * 
 * This downloads today's master scoreboard and parses it, then downloads the
 * boxscore file for each game and merges the Game objects together and either
 * saves or updates them.
 * 
 * It then determines if there are any noteworthy events to send, and sends them.
 * 
 * @author sschulte
 */
@SuppressWarnings("serial")
public class NotifyServlet extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(NotifyServlet.class.getName());
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		List<Map<String, Object>> games = Parser.games(Downloader.todayScoreboard());
		
		log.info(String.format("Games: %s", games.size()));
		
		List<Game> gamesToSave = new ArrayList<Game>();
		List<Event> events = new ArrayList<Event>();
		for (Map<String, Object> map : games) {
			String gameId = (String)map.get("id");
			log.info(String.format("%s", gameId));
			
			Game game = ofy().load().key(Game.key(gameId)).get();
			Game original = null;
			if (game == null) {
				game = new Game();
			} else {
				original = game.dup();
			}
			
			game.merge(map);
			game.mergeBoxscore(Parser.boxscore(Downloader.boxscore(game)));
			
			gamesToSave.add(game);
			
			// now figure out what events happened since last time
			if (original != null) {
				events.addAll(CurrentEvents.current(original, game));
			}
		}
		if (!gamesToSave.isEmpty()) {
			ofy().save().entities(gamesToSave).now();
		}
		
		log.info(String.format("Events: %s", events.size()));
		if (!events.isEmpty()) {
			EventSender.sendAll(events);
			ofy().save().entities(events).now();
		}
        
		response.setContentType("text/plain");
		response.getWriter().println("OK");
	}

}
