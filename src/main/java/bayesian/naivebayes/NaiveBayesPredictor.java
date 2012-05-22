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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import bayesian.common.Record;

public class NaiveBayesPredictor {
	private Logger LOGGER = Logger.getLogger(NaiveBayesPredictor.class);
	
	//classId to Class Length
	private Map<Integer, Integer> classIdToLength = new HashMap<Integer, Integer>();
	
	//classId to Class Frequency
	private Map<Integer, Integer> classIdToFre = new HashMap<Integer, Integer>();
	
	//Number of Unique Words in Training File
	private int vocabularySize;

	//Word Probabilities Given Class
	private Map<Integer, Map<Integer, Double>> modelParams = new HashMap<Integer, Map<Integer, Double>>();
	
	private void predict(List<Record> records) {
		
	}
	
	private void loadModel(String path) throws IOException {
		List<String> inputLines = FileUtils.readLines(new File(path));
	}
}
