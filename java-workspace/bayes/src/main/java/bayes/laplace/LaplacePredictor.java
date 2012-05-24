/**
 * @author bing.cheng
 * @date 2012-5-24
 * @project bayesian
 * @file LaplacePredictor.java
 *
 */


package bayes.laplace;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import bayes.common.Constants;
import bayes.common.NumericPair;
import bayes.common.Record;
import bayes.common.Utility;
import bayes.naivebayes.NaiveBayesPredictor;

public class LaplacePredictor {
	
	private static double SMOOTHING_FACTOR = 1;
	
	private Logger LOGGER = Logger.getLogger(NaiveBayesPredictor.class);
	
	// classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();

	// classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();
	
	// classId to Class ratio
	private Map<Integer, Double> classIdToRatio = new HashMap<Integer, Double>();

	// Key is ClassId and Feature Id. Value is Feature Frequency
	private Map<NumericPair.KeyPair, Double> knownFeaFre = new HashMap<NumericPair.KeyPair, Double>();
		
	// Key is ClassId and Feature Id. Value is Feature Probability
	private Map<NumericPair.KeyPair, Double> knownFeaPro = new HashMap<NumericPair.KeyPair, Double>();
	
	// Feature Probability for Unknown Features
	private Map<Integer, Double> unknownFeaPro = new HashMap<Integer, Double>();
	
	// Unique Words in Training File
	private int vocabularySize;
	
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
			double feaFre = Double.parseDouble(fields[2].trim());
			NumericPair.KeyPair keyPair = new NumericPair.KeyPair(classId, feaId);
			knownFeaFre.put(keyPair, feaFre);
		}
		LOGGER.info("Vocabulary Size: " + vocabularySize);
		LOGGER.info("Class Number: " + classNum);
		LOGGER.info("Model Params Number: " + (inputLines.size()-classNum-2));
	}
	
	public void initModelParams() {
		// Compute classId Ratio
		int trainingNum = 0;
		for (Integer classId : classIdToFre.keySet())
			trainingNum += classIdToFre.get(classId);
		for (Integer classId : classIdToFre.keySet()) {
			double classRatio = classIdToFre.get(classId) * 1.0 / trainingNum;
			classRatio *= Constants.MULTIPLIER;
			classIdToRatio.put(classId, classRatio);
		}
			
		for (NumericPair.KeyPair keyPair : knownFeaFre.keySet()) {
			int classId = (Integer)keyPair.getFirst();
			double feaFre = knownFeaFre.get(keyPair);
			double feaPro = (feaFre + SMOOTHING_FACTOR) / (classIdToLength.get(classId) + vocabularySize);
			feaPro *= Constants.MULTIPLIER;
			knownFeaPro.put(keyPair, feaPro);
		}
		for (Integer classId : classIdToLength.keySet()) {
			double pro = SMOOTHING_FACTOR / (classIdToLength.get(classId) + vocabularySize);
			pro *= Constants.MULTIPLIER;
			unknownFeaPro.put(classId, pro);
		}
	}
	
	private double getFeaProba(int classId, int feaId) {
		NumericPair.KeyPair pair = new NumericPair.KeyPair(classId, feaId);
		if (knownFeaPro.containsKey(pair))
			return knownFeaPro.get(pair);
		return unknownFeaPro.get(classId);
	}
	
	public List<NumericPair.KeyValuePair> predict(List<Record> records) {
		List<NumericPair.KeyValuePair> labels = new ArrayList<NumericPair.KeyValuePair>();
		for (Record record : records) {
			NumericPair.KeyValuePair label = new NumericPair.KeyValuePair(-1, Double.NEGATIVE_INFINITY);
			double totalScore = 0;
			for (Integer candidate : classIdToLength.keySet()) {
				double score = classIdToRatio.get(candidate);
				for (Integer feaId : record.features.keySet()) {
					double feaPro = getFeaProba(candidate, feaId);
					score *= Math.pow(feaPro, record.features.get(feaId));
				}
				if ((Double)(label.getSecond()) < score) {
					label.setFirst(candidate);
					label.setSecond(score);
				}
				totalScore += score;
			}
			label.setSecond((Double)label.getSecond() / totalScore);
			labels.add(label);
		}
		return labels;
	}
	
	public void outputRes(List<NumericPair.KeyValuePair> labels, String path) throws IOException {
		StringBuilder output = new StringBuilder();
		for (NumericPair.KeyValuePair label : labels)
			output.append(label.getFirst()).append("\t").append(label.getSecond()).append("\n");
		FileUtils.write(new File(path), output);
	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LaplacePredictor nbpredict = new LaplacePredictor();
		nbpredict.loadModelParams("D:/bayes/model.txt");
		nbpredict.initModelParams();
		List<Record> testRecords = Utility.loadRecords("D:/bayes/predict.txt");
		List<NumericPair.KeyValuePair> labels = nbpredict.predict(testRecords);
		nbpredict.outputRes(labels, "D:/bayes/result.txt");
		int errorNum = 0;
		for (int idx=0; idx<testRecords.size(); ++idx)
			if (testRecords.get(idx).classId != labels.get(idx).getFirst().intValue())
				++errorNum;
		System.out.println("Error Rate: " + errorNum * 1.0 / testRecords.size());
	}

}
