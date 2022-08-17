package org.menlorobotics.ev3;

import java.util.List;

import com.google.common.collect.Lists;

public class IActivity {
	private String name;
	private List<String> params = Lists.newLinkedList();
	private List<String>tags = Lists.newLinkedList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

}
