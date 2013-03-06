package com.vikinghammer.mlb.notifier.gae.parse;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Parse the scoreboard into games, and parse the boxscore. This converts the JSON
 * into Java Map objects.
 * 
 * @author sschulte
 */
public class Parser {
	
	public static List<Map<String, Object>> games(Reader reader) throws IOException {
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> json = om.readValue(reader, Map.class);
		Map<String, Object> data = (Map<String, Object>)((Map<String, Object>)json.get("data")).get("games");
		
		List<Map<String, Object>> games;
		if (data.get("game") instanceof List) {
			games = (List<Map<String, Object>>)data.get("game");
			
		} else {
			games = new ArrayList<Map<String, Object>>();
			games.add((Map<String, Object>)data.get("game"));
		}
		
		return games;
	}
	
	public static Map<String, Object> boxscore(Reader reader) {
		try {
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> json = om.readValue(reader, Map.class);
			Map<String, Object> data = (Map<String, Object>)json.get("data");
			return (Map<String, Object>)data.get("boxscore");
		} catch (IOException e) {
			return new HashMap<String, Object>();
		}
	}

}
