/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file Record.java
 *
 */


package bayes.common;

import java.util.HashMap;
import java.util.Map;

public class Record {
	public int classId;
	public Map<Integer, Double> features = new HashMap<Integer, Double>();
}
