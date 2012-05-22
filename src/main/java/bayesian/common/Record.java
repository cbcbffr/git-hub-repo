/**
 * @author bing.cheng
 * @date 2012-5-22
 * @project bayesian
 * @file Record.java
 *
 */


package bayesian.common;

import java.util.HashMap;
import java.util.Map;

public class Record {
	int label;
	Map<Integer, Double> features = new HashMap<Integer, Double>();
}
