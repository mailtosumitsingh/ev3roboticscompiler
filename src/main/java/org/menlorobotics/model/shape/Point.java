package org.menlorobotics.model.shape;

import java.util.List;

import com.google.common.collect.Lists;

public class Point {
	private int x = 0;
	private int y = 0;
	private int z = 0;
	private String id;
	private String type;
	private String[]tags = new String[0];
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public enum PointType{
		Position, Control
	} ;
	private PointType pointType;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

	public PointType getPointType() {
		return pointType;
	}

	public void setPointType(PointType pointType) {
		this.pointType = pointType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Point(int x, int y, int z, String id) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
	}

	public Point(int x, int y, String id) {
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public Point(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Point() {
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}


}
