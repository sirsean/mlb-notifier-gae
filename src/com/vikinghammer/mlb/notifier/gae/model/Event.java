package com.vikinghammer.mlb.notifier.gae.model;

import java.security.MessageDigest;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Data;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * A noteworthy event that happened in a game. These are detected after
 * downloading/parsing/merging the games, and are emailed to the specified
 * recipient.
 * 
 * You MUST call generateId() before attempting to save it.
 * 
 * @author sschulte
 */
@Entity
@Data
public class Event {
	
	private static final Logger log = Logger.getLogger(Event.class.getName());
	
	public static Key<Event> key(String id) {
		return Key.create(Event.class, id);
	}
	
	/**
	 * The id is a hash of a concatenation of the gameId
	 * and the text.
	 * 
	 * You should never set it directly, but call generateId()
	 * after setting both the underlying fields.
	 */
	@Id
	private String id;
	
	private String gameId;
	private String teams;
	private String text;
	private Date happenedAt;
	
	public void generateId() {
		try {
			if ((gameId != null) && (text != null)) {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				md.reset();
				md.update((gameId + text).getBytes());
				id = bytesToHex(md.digest());
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "Failed to generate Event id", e);
		}
	}
	
	private String bytesToHex(byte[] b) {
	      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
	                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
	      StringBuffer buf = new StringBuffer();
	      for (int j=0; j<b.length; j++) {
	         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
	         buf.append(hexDigit[b[j] & 0x0f]);
	      }
	      return buf.toString();
	   }

}
