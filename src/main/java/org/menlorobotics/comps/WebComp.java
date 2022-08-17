package org.menlorobotics.comps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class WebComp extends Comp{

	@Override
	public String getHTML(String id) {
		try {
			String s =  FileUtils.readFileToString(new File("./src/main/resources/comps/web.html"));
			return StringUtils.replace(s, "{id}", id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

}
