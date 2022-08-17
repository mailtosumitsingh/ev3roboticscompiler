package org.menlorobotics.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.menlorobotics.ev3.Activity;
import org.menlorobotics.ev3.IActivity;
import org.menlorobotics.ev3.Plan;
import org.menlorobotics.ev3.Wire;
import org.menlorobotics.ev3.impl.ModGyro1;
import org.menlorobotics.ev3.impl.ModGyroReverse;
import org.menlorobotics.ev3.impl.MoveHandUp;
import org.menlorobotics.ev3.impl.RotateAccurate;
import org.menlorobotics.ev3.impl.RotateAntiClock;

import com.google.common.collect.Maps;

public class EV3Utils {
	Map<String, Activity> activities = Maps.newHashMap();
	{
		activities.put("mod_gyro_1", new ModGyro1());
		activities.put("rotateaccurate", new RotateAccurate());
		activities.put("rotate_anticlock", new RotateAntiClock());
		activities.put("mod_gyro_reverse", new ModGyroReverse());
		activities.put("action1", new MoveHandUp());
	}
	public String generateCode(String name,String template,Plan input) throws IOException {
		int xorigin = 2699 -163;
		int startx = xorigin ;
		int starty = 450;
		int width = 163;
		int height = 91;
		int xdelta = 20;
		int ydelta = 20;
		int maxinrow = 5;
		int  start= 1;
		String prefix = "n";
		StringBuilder acode = new StringBuilder();
		StringBuilder wcode = new StringBuilder();
		int i=1;
		for(IActivity s: input.getA()) {
			
			Wire w = new Wire();
			w.setFrom(prefix+start);
			start++;
			Activity aa = activities.get(s.getName());
			if(aa!=null) {
			aa.setParams(s.getParams());
			aa.setId(prefix +start);
			String code = aa.getCode();
			//***********************************
			code = StringUtils.replace(code, "${x}", ""+startx);
			code = StringUtils.replace(code, "${y}", ""+starty);
			startx = startx - width - xdelta ;
			
			if( i % maxinrow == 0 ) {
				starty=starty-height-ydelta;
				startx = xorigin ;
			}
			acode.append(code);
			acode.append("\n");
			w.setTo(prefix+start);
			w.setId("w"+start);
			wcode.append(w.getCode());
			wcode.append("\n");
			}
			i++;
		}
		String s = acode.toString()+"\n"+wcode.toString()+"\n";
		String code = FileUtils.readFileToString(new File(template));
		code = StringUtils.replace(code, "${addcodehere}", s);

		
		return code;
		 
	}
	public String getSrcFileRef(String name) {
		return " <SourceFileReference StoragePath=\""+name+".ev3p\" RelativeStoragePath=\""+name+".ev3p\" OverridingDocumentTypeIdentifier=\"X3VIDocument\" DocumentTypeIdentifier=\"NationalInstruments.LabVIEW.VI.Modeling.VirtualInstrument\" Name=\""+name+"\\.ev3p\" Bindings=\"Envoy,DefinitionReference,SourceFileReference,X3VIDocument\" />";
	}
}
