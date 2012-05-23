/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file NaiveBayesPredictor.java
 *
 */


package bayesian.naivebayes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import bayesian.common.Record;
import bayesian.common.Utility;

public class NaiveBayesPredictor {
	
	private Logger LOGGER = Logger.getLogger(NaiveBayesPredictor.class);
	
	// classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();

	// classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();

	// Unique Words in Training File
	private int vocabularySize;
	
	// Word Frequencies Given Class
	private Map<Integer, Map<Integer, Double>> modelParams = new HashMap<Integer, Map<Integer, Double>>();
	
	public void loadModelParams(String path) throws IOException {
		List<String> inputLines = FileUtils.readLines(new File(path));
		vocabularySize = Integer.parseInt(inputLines.get(0).trim());
		int classNum = Integer.parseInt(inputLines.get(1).trim());
		for (int idx=2; idx<2+classNum; ++idx) {
			String []fields = inputLines.get(idx).split("\t");
			int classId = Integer.parseInt(fields[0]);
			classIdToLength.put(classId, Integer.parseInt(fields[1]));
			classIdToFre.put(classId, Integer.parseInt(fields[2]));
		}
		for (int idx=2+classNum; idx<inputLines.size(); ++idx) {
			String []fields = inputLines.get(idx).split("\t");
			int classId = Integer.parseInt(fields[0].trim());
			int feaId = Integer.parseInt(fields[1].trim());
			double feaValue = Double.parseDouble(fields[2].trim());
			if (!modelParams.containsKey(classId))
				modelParams.put(classId, new HashMap<Integer, Double>());
			modelParams.get(classId).put(feaId, feaValue / classIdToLength.get(classId));
		}
		LOGGER.info("Vocabulary Size: " + vocabularySize);
		LOGGER.info("Class Number: " + classNum);
		LOGGER.info("Model Params Number: " + (inputLines.size()-classNum-1));
	}
	
	public List<Integer> predict(List<Record> records) {
		List<Integer> labels = new ArrayList<Integer>();
		for (Record record : records) {
			double maxScore = Double.NEGATIVE_INFINITY;
			int classId = -1;
			double score = 0;
			for (Integer candidate : classIdToLength.keySet()) {
				for (Integer feaId : record.features.keySet()) {
					Double wordPro = modelParams.get(candidate).get(feaId);
					wordPro = (wordPro==null)?0:wordPro;
					score *= wordPro;
				}
				if (maxScore < score) {
					score = maxScore;
					classId = candidate;
				}
				labels.add(classId);
			}
		}
		return labels;
	}
	
	public void outputRes(List<Integer> labels, String path) throws IOException {
		StringBuilder output = new StringBuilder();
		for (Integer label : labels)
			output.append(label).append("\n");
		FileUtils.write(new File(path), output);
	}
	
	public static void main(String []args) throws IOException {
		NaiveBayesPredictor nbpredict = new NaiveBayesPredictor();
		List<Record> testRecords = Utility.loadRecords("D:/bayes/predict.txt");
		nbpredict.loadModelParams("D:/bayes/model.txt");
		List<Integer> labels = nbpredict.predict(testRecords);
		nbpredict.outputRes(labels, "D:/bayes/result.txt");
	}
}
