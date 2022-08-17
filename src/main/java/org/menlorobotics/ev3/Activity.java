package org.menlorobotics.ev3;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public abstract class Activity {
	protected String id;
	protected String templateCode;
	protected List<String> params = Lists.newLinkedList();

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public abstract String getTemplateCode();

	public final String getCode() throws IOException {
		String code  = FileUtils.readFileToString(new File(getTemplateCode()));
		int i = 0;
		for(String s:params) {
			code = StringUtils.replace(code, "${param"+i+"}", s);
			i++;
		}
		code = StringUtils.replace(code, "${id}", getId());
		return code;
	}

}
