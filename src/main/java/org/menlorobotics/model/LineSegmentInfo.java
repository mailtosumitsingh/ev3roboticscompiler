package org.menlorobotics.model;

import java.util.List;

import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.LineSegment;

public class LineSegmentInfo {
	private List<String> tags = Lists.newArrayList();
	private LineSegment ls;

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public LineSegment getLs() {
		return ls;
	}

	public void setLs(LineSegment ls) {
		this.ls = ls;
	}

}
