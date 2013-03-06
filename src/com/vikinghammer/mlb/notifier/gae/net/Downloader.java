package com.vikinghammer.mlb.notifier.gae.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Calendar;

import com.vikinghammer.mlb.notifier.gae.model.Game;

/**
 * Download the master scoreboard for the day, and/or the boxscore file for each game.
 * 
 * @author sschulte
 */
public class Downloader {
	
	public static Reader todayScoreboard() throws IOException {
		return masterScoreboard(Calendar.getInstance());
	}
	
	public static Reader masterScoreboard(Calendar cal) throws IOException {
		URL url = new URL(String.format("http://gdx.mlb.com/components/game/mlb/year_%04d/month_%02d/day_%02d/master_scoreboard.json", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)));
		return new BufferedReader(new InputStreamReader(url.openStream()));
	}
	
	public static Reader boxscore(Game game) throws IOException {
		String[] parts = game.getGameId().split("\\/");
		String year = parts[0];
		String month = parts[1];
		String day = parts[2];
		String translatedGameId = game.getGameId().replaceAll("[\\/-]", "_");
		String href = String.format("http://gdx.mlb.com/components/game/mlb/year_%s/month_%s/day_%s/gid_%s/boxscore.json", year, month, day, translatedGameId);
		URL url = new URL(href);
		return new BufferedReader(new InputStreamReader(url.openStream()));
	}

}
