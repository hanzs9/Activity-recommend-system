package com.yiibai.springmvc;

import java.util.HashMap;

public class ActivityInformation {
    private HashMap<String,HashMap<String,String>> map = new HashMap<String,HashMap<String,String>>();

	public HashMap<String, HashMap<String, String>> getMap() {
		return map;
	}

	public void setMap(HashMap<String, HashMap<String, String>> map) {
		this.map = map;
	}
    
}
