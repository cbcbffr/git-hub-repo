/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file NaiveBayesTrain.java
 *
 */


package bayesian.naivebayes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import bayesian.common.Record;
import bayesian.common.Utility;

public class NaiveBayesTrain {
	
	//classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();
	
	//classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();
	
	//Unique Words in Training File
	private Set<Integer> vocabulary = new HashSet<Integer>();

	//Word Frequencies Given Class
	private Map<Integer, Map<Integer, Double>> modelParams = new HashMap<Integer, Map<Integer, Double>>();
	
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
				if (!modelParams.containsKey(record.classId))
					modelParams.put(record.classId, new HashMap<Integer, Double>());
				Double wordFreGivenClass = modelParams.get(record.classId).get(feaId);
				modelParams.get(record.classId).put(feaId, (wordFreGivenClass==null)?feaFre:feaFre+wordFreGivenClass);
				length += feaFre;
			}
			classIdToLength.put(record.classId, classIdToLength.get(record.classId) + length);
		}
	}
	
	public void outputModel(String path) throws IOException {
		StringBuilder output = new StringBuilder();
		output.append(vocabulary.size()).append("\n");
		output.append(classIdToLength.size()).append("\n");
		for (Integer classId : classIdToLength.keySet())
			output.append(classId).append("\t").append(classIdToLength.get(classId)).append("\t").append(classIdToFre.get(classId)).append("\n");
		for (Integer classId : modelParams.keySet()) {
			Map<Integer, Double> wordFre = modelParams.get(classId);
			for (Integer feaId : wordFre.keySet()) {
				output.append(classId).append("\t").append(feaId).append("\t").append(wordFre.get(feaId)).append("\n");
			}
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
