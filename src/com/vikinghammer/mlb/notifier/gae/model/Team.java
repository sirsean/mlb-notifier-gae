package com.vikinghammer.mlb.notifier.gae.model;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;

/**
 * Each game has a home team and an away team.
 * 
 * @author sschulte
 */
@Embed
@Data
public class Team {
	
	private String abbreviation;
	private String city;
	private String name;
	
	public Team dup() {
		Team dup = new Team();
		if (abbreviation != null) {
			dup.setAbbreviation(new String(abbreviation));
		}
		if (city != null) {
			dup.setCity(new String(city));
		}
		if (name != null) {
			dup.setName(new String(name));
		}
		return dup;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abbreviation == null) ? 0 : abbreviation.hashCode());
		return result;
	}

}
