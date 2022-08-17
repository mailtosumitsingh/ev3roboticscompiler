package org.menlorobotics.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

public class ZipUtils {

	public static byte[] generateEv3( String project,String base)throws Exception {
	 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
     ZipOutputStream zipStream = new ZipOutputStream(bufferedOutputStream);

     
	List<File> b = Lists.newArrayList();
	for(File f: new File(base).listFiles()) {
		if(!f.isDirectory()) {
			if(f.getName().endsWith("mbxml")|| f.getName().endsWith("ev3p")|| f.getName().endsWith("lvprojx")) {
				//u.add(f);
				b.add(f);
			}else {
				b.add(f);
			}
		System.out.println(f.getAbsolutePath());
		}
	}

	
	for(File f: b) {
		System.out.println(f.getAbsolutePath());
		addFileToZip(zipStream,f.getName(),f);
	}

	
	zipStream.finish();
	zipStream.flush();
	IOUtils.closeQuietly(zipStream);
	IOUtils.closeQuietly(bufferedOutputStream);
    IOUtils.closeQuietly(byteArrayOutputStream);
    return byteArrayOutputStream.toByteArray();
	
	}
	private static void addFileToZip(ZipOutputStream zos, String path, File file) throws IOException
    {
        if (!file.canRead())
        {
            System.out.println("Cannot read " + file.getCanonicalPath() + " (maybe because of permissions)");
            return;
        }
        System.out.println("Compressing " + file.getName());
        zos.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4092];
        int byteCount = 0;
        while ((byteCount = fis.read(buffer)) != -1)
        {
            zos.write(buffer, 0, byteCount);
            System.out.print('.');
            System.out.flush();
        }
        System.out.println();
        fis.close();
        zos.closeEntry();
    }
	
	private static void addUtf8FileToZip(ZipOutputStream zipStream,String sourcePath,String destName,BufferedWriter writer ) throws FileNotFoundException, IOException {
		File inputFile = new File(sourcePath);
		ZipEntry entry = new ZipEntry(destName);
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));


		zipStream.putNextEntry(entry);

		String line = null;
		while ((line = reader.readLine()) != null) {
		    writer.append(line).append('\n');
		}
		writer.flush();
		reader.close();
		zipStream.closeEntry();
	}
}
