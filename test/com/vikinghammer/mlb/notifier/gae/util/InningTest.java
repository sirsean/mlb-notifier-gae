package com.vikinghammer.mlb.notifier.gae.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class InningTest {

	@Test
	public void inning1() {
		assertEquals("1st", Inning.friendly(1));
	}
	
	@Test
	public void inning2() {
		assertEquals("2nd", Inning.friendly(2));
	}
	
	@Test
	public void inning3() {
		assertEquals("3rd", Inning.friendly(3));
	}
	
	@Test
	public void inning4() {
		assertEquals("4th", Inning.friendly(4));
	}
	
	@Test
	public void inning10() {
		assertEquals("10th", Inning.friendly(10));
	}

}
