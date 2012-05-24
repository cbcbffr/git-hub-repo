/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file NaiveBayesTrain.java
 *
 */


package bayes.naivebayes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import bayes.common.NumericPair;
import bayes.common.Record;
import bayes.common.Utility;

public class NaiveBayesTrain {
	
	//classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();
	
	//classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();
	
	//Unique Words in Training File
	private Set<Integer> vocabulary = new HashSet<Integer>();

	//Word Frequencies Given Class
	private Map<NumericPair.KeyPair, Double> knownFeaFre = new HashMap<NumericPair.KeyPair, Double>();
	
	public void train(List<Record> trainRecords) {
		
		for (Record record : trainRecords) {
			Integer frequency = classIdToFre.get(record.classId);
			classIdToFre.put(record.classId, (frequency==null)?1:frequency+1);
			if (!classIdToLength.containsKey(record.classId))
				classIdToLength.put(record.classId, 0);
			int length = 0;
			for (Integer feaId : record.features.keySet()) {
				vocabulary.add(feaId);
				Double feaFre = record.features.get(feaId);
				length += feaFre;
				NumericPair.KeyPair keyPair = new NumericPair.KeyPair(record.classId, feaId);
				Double totalFeaFre = knownFeaFre.get(keyPair);
				knownFeaFre.put(keyPair, (totalFeaFre==null)?feaFre:(totalFeaFre+feaFre));
			}
			classIdToLength.put(record.classId, classIdToLength.get(record.classId) + length);
		}
	}
	
	public void outputModel(String path) throws IOException {
		StringBuilder output = new StringBuilder();
		output.append(vocabulary.size()).append("\n");
		output.append(classIdToLength.size()).append("\n");
		for (Integer classId : classIdToLength.keySet()) {
			output.append(classId).append("\t").append(classIdToLength.get(classId)).
				append("\t").append(classIdToFre.get(classId)).append("\n");
		}
		for (NumericPair.KeyPair keyPair : knownFeaFre.keySet()) {
			output.append(keyPair.getFirst()).append("\t").append(keyPair.getSecond()).
				append("\t").append(knownFeaFre.get(keyPair)).append("\n");
		}
		FileUtils.write(new File(path), output);
	}
	
	public static void main(String []args) throws IOException {
		NaiveBayesTrain nbtrain = new NaiveBayesTrain();
		List<Record> trainRecords = Utility.loadRecords("D:/bayes/train.txt");
		nbtrain.train(trainRecords);
		nbtrain.outputModel("D:/bayes/model.txt");
	}
}
