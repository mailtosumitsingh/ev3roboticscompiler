package org.menlorobotics.model.shape;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

public class Shape {
	private String id;
	private String type;
	private Object xref;
	private List<Point>pts = Lists.newLinkedList();
	private boolean visible;
	private String compName;
	private Map<String,String>data;
	private String shapeType ;
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	public String getShapeType() {
		return shapeType;
	}

	public void setShapeType(String shapeType) {
		this.shapeType = shapeType;
	}

	public List<Point> getPts() {
		return pts;
	}

	public void setPts(List<Point> pts) {
		this.pts = pts;
	}

	private List<Facet> facets = Lists.newLinkedList();
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getXref() {
		return xref;
	}

	public void setXref(Object xref) {
		this.xref = xref;
	}

	private List<Point> points = Lists.newLinkedList();
		
	public List<Facet> getFacets() {
		return facets;
	}

	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}

	public void addFace(Facet face) {
		this.facets.add(face);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public List<Point> getPoints() {
		return points;
	}

	public void addPoint(Point point) {
		this.points.add(point);
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
