package org.menlorobotics.ev3;

public class Wire {
	private String from;
	private String to;
	private String id;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public String getCode() {
		return "<Wire Id=\"w24\" Joints=\"N("+from+":SequenceOut)  N("+to+":SequenceIn)\" />";
	}

}
