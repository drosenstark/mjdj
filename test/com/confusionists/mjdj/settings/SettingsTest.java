package com.confusionists.mjdj.settings;

import java.io.IOException;

import junit.framework.TestCase;


public class SettingsTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		Settings.FILENAME = "test.xml";
	}
	
	public void testCreateNewMorphSetting() throws IOException {
		Settings settings = Settings.getInstance(false, true);
		settings.save();
		settings = Settings.getInstance(true, false);
	} 

	/*
	public void testNonExistentProp() throws Exception {
		Settings settings = Settings.getInstance(true, false);
		Object thing = settings.morphs.get("adfdasf");
		assertEquals(null, thing);
	}
	public void testZ() throws Exception {
		Settings settings = Settings.getInstance(true, false);
		Object thing = settings.morphs.get("blah");
		assertEquals("hello!", thing);
	}
	*/
	public void testSaveReceiver() throws Exception {
		Settings settings = Settings.getInstance(true, false);
		settings.receivers.put("what", true);
		settings.save();
	}
	
	public void testGetReceiver() {
		Settings settings = Settings.getInstance(true, false);
		boolean thinger = Settings.getBoolean(settings.receivers, "what");
		assertTrue(thinger);
	}
	

}
