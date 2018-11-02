package com.ls.eureka;

import java.util.List;

public class Application {
	private String name;
	private List<InstanceInfo> instance;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<InstanceInfo> getInstance() {
		return instance;
	}
	public void setInstance(List<InstanceInfo> instance) {
		this.instance = instance;
	}
}
