package org.menlorobotics.handlers;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.menlorobotics.comps.ComponentFactory;
import org.menlorobotics.ev3.IActivity;
import org.menlorobotics.ev3.Plan;
import org.menlorobotics.model.LineSegmentInfo;
import org.menlorobotics.model.shape.Point;
import org.menlorobotics.model.shape.Point.PointType;
import org.menlorobotics.model.shape.Shape;
import org.menlorobotics.utils.CommonUtil;
import org.menlorobotics.utils.EV3Utils;
import org.menlorobotics.utils.ZipUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineSegment;

/*
 * http://localhost:8080/getPlan?pid=mytest
 * http://localhost:8080/downloadProgram?pid=mytest2
 * 
 * */
@RestController
public class GeneratePlan {
	EV3Utils eu = new EV3Utils();
	ComponentFactory compFactory = new ComponentFactory();

	@PostMapping(value = "/getPath")
	public void getPlanFromGraph(HttpServletResponse response, @RequestParam("graph") String json) throws Exception {
		System.out.println(json);
		Shape[] ss = CommonUtil.toObject(json, Shape[].class);
		System.out.println(ss);
		String type = "simple1";
		String baseTarget = "C:\\projects\\rvrwork\\rvr\\ev3\\tmp\\";
		String id = "random1";
		File fdir = new File(baseTarget + id + "\\");
		File projectTemplateDir = new File("C:\\projects\\rvrwork\\rvr\\ev3\\templates\\projects\\" + type + "\\");
		FileUtils.copyDirectory(projectTemplateDir, fdir);
		FileUtils.forceMkdir(fdir);
		StringBuilder sb = new StringBuilder();
		for (Shape s : ss) {
			if(s.getId().startsWith("path")||s.getId().startsWith("shape")) {
				printShapeAngle(s, baseTarget, id, s.getId());
				String srcFileRef = eu.getSrcFileRef(s.getId());
				sb.append(srcFileRef + "\n");
			}
		}
		String fcode = FileUtils.readFileToString(new File("C:\\projects\\rvrwork\\rvr\\ev3\\templates\\projects\\" + type + "\\Project.lvprojx"));
		fcode = StringUtils.replace(fcode, "${addfilehere}", sb.toString());
		FileUtils.writeStringToFile(new File(fdir.getAbsolutePath() + "\\Project.lvprojx"), fcode);

		String progId = "random1";
		byte[] bytes = ZipUtils.generateEv3("test", "C:\\projects\\rvrwork\\rvr\\ev3\\tmp\\" + progId);
		FileUtils.writeByteArrayToFile(new File("c:\\temp\\random2.ev3"), bytes);
	}

	public Point getPointFromGraphShape(String id, Shape b) {
		List<Point> c = b.getPts();
		for (Point p : c) {
			if (p.getId().equals(id)) {
				return p;
			}
		}
		return null;
	}

	public void printShapeAngle(Shape b, String baseTarget, String id, String name) throws IOException {
		Plan plan = new Plan();
		Set<LineSegmentInfo> reverse = Sets.newHashSet();
		List<String> c = b.getFacets().get(0).getConnectors().get(0).getPts();
		List<Point> pts = Lists.newLinkedList();
		for (String pid : c) {
			Point pt = getPointFromGraphShape(pid, b);
			if (pt != null) {
				pts.add(pt);
			}
		}
		List<LineSegmentInfo> lines = Lists.newLinkedList();
		for (int i = 0; i <= pts.size() - 2; i++) {
			Coordinate p1 = new Coordinate((pts.get(i).getX()), (pts.get(i).getY()));
			Coordinate p2 = new Coordinate((pts.get(i + 1).getX()), (pts.get(i + 1).getY()));
			if (pts.get(i).getPointType().equals(PointType.Control)) {
				LineSegment ls = new LineSegment(p2, p1);
				LineSegmentInfo info = new LineSegmentInfo();
				info.setLs(ls);
				for (String s : pts.get(i+1).getTags()) {
					info.getTags().add(s);
				}
				Collections.sort(info.getTags());
				lines.add(info);
				reverse.add(info);
			} else {
				LineSegment ls = new LineSegment(p1, p2);
				LineSegmentInfo info = new LineSegmentInfo();
				info.setLs(ls);
				for (String s : pts.get(i + 1).getTags()) {
					info.getTags().add(s);
				}
				Collections.sort(info.getTags());
				lines.add(info);

			}

		}
		int lastAngle = 0;
		for (int i = 0; i < lines.size(); i++) {
			LineSegmentInfo lineSegment = lines.get(i);
			double a1 = lineSegment.getLs().angle();
			double d1 = toDegrees(a1);
			String gyroReverse = "mod_gyro_reverse";
			String modGyro = "mod_gyro_1";
			if (d1 < 0) {
				if (Math.abs(d1) > 1) {
					int angleToMove = (int) d1 - lastAngle;
					lastAngle = (int) d1;
					if (angleToMove != 0) {
						plan.getA().add(getComponent(getRotateActionName(angleToMove), "" + angleToMove, "50", "5"));
					}
				}
				plan.getA().add(getComponent(reverse.contains(lineSegment)?gyroReverse:modGyro, "0", "50", "" + lineSegment.getLs().getLength() / 60));
				addTagComponents(plan, lineSegment);
			} else {
				if (Math.abs(d1) > 1) {
					int angleToMove = (int) d1 - lastAngle;
					lastAngle = (int) d1;
					if (angleToMove != 0) {
						plan.getA().add(getComponent(getRotateActionName(angleToMove), "" + angleToMove, "50", "5"));
					}
				}
				plan.getA().add(getComponent(reverse.contains(lineSegment)?gyroReverse:modGyro, "0", "50", "" + lineSegment.getLs().getLength() / 60));
				addTagComponents(plan, lineSegment);
				
			}
		}
		String fTemplate = "C:\\projects\\rvrwork\\rvr\\ev3\\templates\\code\\file\\template1.ev3p";
		File fdir = new File(baseTarget + id + "\\");
		String code = eu.generateCode(name, fTemplate, plan);
		FileUtils.writeStringToFile(new File(fdir.getAbsolutePath() + "\\" + name + ".ev3p"), code);

	}

	private void addTagComponents(Plan plan, LineSegmentInfo lineSegment) {
		if (lineSegment.getTags() != null && lineSegment.getTags().size() > 0) {
			lineSegment.getTags().forEach(item -> {
				addAction(plan, item);
			});
		}
	}

	private String getRotateActionName(int angleToMove) {
		String rotateAccurate = "rotateaccurate";
		String rotateAnticlock = "rotate_anticlock";
		String compName;
		if (angleToMove > 0) {
			compName = rotateAccurate;
		} else {
			compName = rotateAnticlock;
		}
		return compName;
	}

	private IActivity getComponent(String compName, String... params) {
		IActivity e4 = new IActivity();
		e4.setName(compName);
		e4.getParams().add(params[0]);// 50
		e4.getParams().add(params[1]);// 5
		e4.getParams().add(params[2]);
		return e4;
	}

	private void addAction(Plan plan, String item) {
		IActivity e = new IActivity();
		e.setName(item);
		plan.getA().add(e);
	}

	double toDegrees(double angle) {
		return angle * (180 / Math.PI);
	}

	@RequestMapping("/getPlan")
	public String getPlan(@RequestParam("pid") String progId) throws IOException {
		String name = "a";
		String type = "simple1";
		String baseTarget = "C:\\projects\\rvrwork\\rvr\\ev3\\tmp\\";
		String fTemplate = "C:\\projects\\rvrwork\\rvr\\ev3\\templates\\code\\file\\template1.ev3p";
		File projectTemplateDir = new File("C:\\projects\\rvrwork\\rvr\\ev3\\templates\\projects\\" + type + "\\");

		String id = progId;// UUID.randomUUID().toString();
		File fdir = new File(baseTarget + id + "\\");
		FileUtils.copyDirectory(projectTemplateDir, fdir);
		FileUtils.forceMkdir(fdir);
		Plan plan = new Plan();
		IActivity e = new IActivity();
		e.setName("mod_gyro_1");
		e.getParams().add("90");
		e.getParams().add("50");
		e.getParams().add("5");
		plan.getA().add(e);
		IActivity e2 = new IActivity();
		e2.setName("rotateaccurate");
		e2.getParams().add("90");
		e2.getParams().add("50");
		e2.getParams().add("5");
		plan.getA().add(e2);
		IActivity e3 = new IActivity();
		e3.setName("mod_gyro_reverse");
		e3.getParams().add("90");
		e3.getParams().add("50");
		e3.getParams().add("5");
		plan.getA().add(e3);
		IActivity e4 = new IActivity();
		e4.setName("rotate_anticlock");
		e4.getParams().add("90");
		e4.getParams().add("50");
		e4.getParams().add("5");
		plan.getA().add(e4);
		IActivity e5 = new IActivity();
		e5.setName("rotate_anticlock");
		e5.getParams().add("90");
		e5.getParams().add("50");
		e5.getParams().add("5");
		plan.getA().add(e5);
		String code = eu.generateCode(name, fTemplate, plan);
		FileUtils.writeStringToFile(new File(fdir.getAbsolutePath() + "\\" + name + ".ev3p"), code);
		String srcFileRef = eu.getSrcFileRef(name);
		String fcode = FileUtils.readFileToString(new File("C:\\projects\\rvrwork\\rvr\\ev3\\templates\\projects\\" + type + "\\Project.lvprojx"));
		fcode = StringUtils.replace(fcode, "${addfilehere}", srcFileRef);
		FileUtils.writeStringToFile(new File(fdir.getAbsolutePath() + "\\Project.lvprojx"), fcode);
		return fcode;
	}

	@RequestMapping(value = "/downloadProgram", produces = "application/zip")
	public byte[] downloadProgram(HttpServletResponse response, @RequestParam("pid") String progId) throws Exception {
		response.addHeader("Content-Disposition", "attachment; filename=\"" + progId + ".ev3\"");
		byte[] bytes = ZipUtils.generateEv3("test", "C:\\projects\\rvrwork\\rvr\\ev3\\tmp\\" + progId);
		return bytes;
	}

	@RequestMapping(value = "/compileEv3")
	public String compilePath(HttpServletResponse response, @RequestParam("design") String design) throws Exception {
		System.out.println(design);
		return "Done";
	}
}
