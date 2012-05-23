/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file NaiveBayesTrain.java
 *
 */


package bayesian.naivebayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import bayesian.common.Record;

public class NaiveBayesTrain {
	
	private Logger LOGGER = Logger.getLogger(NaiveBayesTrain.class);
	
	//classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();
	
	//classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();
	
	//Number of Unique Words in Training File
	private int vocabularySize;

	//Word Probabilities Given Class
	private Map<Integer, Map<Integer, Double>> modelParams = new HashMap<Integer, Map<Integer, Double>>();
	
	
	public void train(List<Record> trainRecords) {
		Set<Integer> vocabulary = new HashSet<Integer>();
		for (Record record : trainRecords) {
			Integer frequency = classIdToFre.get(record.classId);
			classIdToFre.put(record.classId, (frequency==null)?1:frequency+1);
			if (!classIdToLength.containsKey(record.classId))
				classIdToLength.put(record.classId, 0);
			if (!classIdToFre.containsKey(record.classId))
				classIdToFre.put(record.classId, 1);
			for (Integer feaId : record.features.keySet()) {
				vocabulary.add(feaId);
			}
		}
	}
	
	public void outputModel() {
		
	}
}
