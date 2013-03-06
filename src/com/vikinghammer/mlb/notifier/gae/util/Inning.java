package com.vikinghammer.mlb.notifier.gae.util;

/**
 * Translate an inning number into a friendler format.
 * 
 * For example, we don't want to say "inning 2", we want to say "the 2nd".
 * 
 * @author sschulte
 */
public class Inning {
	
	public static String friendly(int inning) {
		switch(inning) {
		case 1:
			return "1st";
		case 2:
			return "2nd";
		case 3:
			return "3rd";
		case 21:
			return "21st";
		case 22:
			return "22nd";
		case 23:
			return "23rd";
		case 31:
			return "31st";
		case 32:
			return "32nd";
		case 33:
			return "33rd";
		default:
			return String.format("%sth", inning);
		}
	}

}
