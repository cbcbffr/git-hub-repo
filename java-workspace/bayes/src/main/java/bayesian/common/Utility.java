/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file Utility.java
 *
 */


package bayesian.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Utility {
	
	public static List<Record> loadRecords(String path) throws IOException {
		
		List<String> inputLines = FileUtils.readLines(new File(path));
		List<Record> trainRecords = new ArrayList<Record>();
		for (String inputLine : inputLines) {
			int endIndex = inputLine.indexOf('#');
			endIndex = (endIndex==-1)?inputLine.length():endIndex;
			inputLine = inputLine.substring(0, endIndex);
			String []fields = inputLine.trim().split("\\s");
			Record record = new Record();
			record.classId = Integer.parseInt(fields[0].trim());
			for (int idx=1; idx<fields.length; ++idx) {
				String []pair = fields[idx].split(":");
				record.features.put(Integer.parseInt(pair[0].trim()), Double.parseDouble(pair[1].trim()));
			}
			trainRecords.add(record);
		}
		return trainRecords;
	}
	
}
