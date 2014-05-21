package parameters;

import generator.SpecificationPattern;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Szymon Konicki
 * 
 */
public class ActivitySpecificationHolder {
	private Map<String, SpecificationPattern> specificationsPatterns = new HashMap<String, SpecificationPattern>();

	public void addPattern(SpecificationPattern pattern, String name) {
		specificationsPatterns.put(name, pattern);
	}

	public SpecificationPattern getPattern(String name) {
		return specificationsPatterns.get(name);
	}

}
