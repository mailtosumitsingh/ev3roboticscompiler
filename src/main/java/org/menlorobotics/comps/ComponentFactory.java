package org.menlorobotics.comps;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

public class ComponentFactory {
	
	private Map<String,Comp> components = Maps.newHashMap();
	{
		components.put("web", new WebComp());
		components.put("file", new FileComp());
		components.put("default", new ArbitComp());
	}
	
	public Comp getComponent(String name) {
		return components.get(name);
	}
	
	public Set<String> getComponents(){
		return components.keySet();
	}
}
