package org.menlorobotics.ev3;

import java.util.List;

import com.google.common.collect.Lists;

public class Plan {
	private List<IActivity> a = Lists.newLinkedList();
	private List<Wire> w = Lists.newLinkedList();

	public List<IActivity> getA() {
		return a;
	}

	public void setA(List<IActivity> a) {
		this.a = a;
	}

	public List<Wire> getW() {
		return w;
	}

	public void setW(List<Wire> w) {
		this.w = w;
	}

}
