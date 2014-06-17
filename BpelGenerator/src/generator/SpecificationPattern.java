package generator;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Szymon Konicki
 *
 */
public class SpecificationPattern {

	private static final String SPEC_LANG_CHARACTERS = "\u2228\u2227\u00AC\u21D2\u25C7\u25A1\\(\\) ,c";

	private final List<PatternFragment> patternFragments;

	public SpecificationPattern(List<PatternFragment> patternFragments) {
		this.patternFragments = patternFragments;
	}

	public String getSpecification(String parameters[]) {
		StringBuilder builder = new StringBuilder();
		for (PatternFragment frag : patternFragments) {
			Integer parameterNumber = frag.parameterNumber;
			String parameter = parameterNumber != null ? parameters[parameterNumber]
					: "";
			parameter = parameter.contains("\u2228") ? "(" + parameter + ")"
					: parameter;
			builder.append(frag.pasteParameter(parameter));
		}

		return builder.toString();
	}

	public static SpecificationPattern fromSource(String source) {
		boolean contains = false;
		boolean prevContains = true;
		String part = "";
		String number = "";
		List<PatternFragment> fragments = new ArrayList<PatternFragment>();
		char arr[] = source.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (!SPEC_LANG_CHARACTERS.contains("" + arr[i])) {
				contains = false;
				number += arr[i];
			} else {
				contains = true;
				part += arr[i];
			}
			if ((prevContains && !contains)
					|| (contains && i == arr.length - 1)) {
				fragments.add(new PatternFragment(part, number.isEmpty() ? null
						: Integer.valueOf(number)));
				part = "";
				number = "";

			}
			prevContains = contains;
		}
		return new SpecificationPattern(fragments);
	}

	public static class PatternFragment {
		private final String part;
		public final Integer parameterNumber;

		public PatternFragment(String part, Integer parameterNumber) {
			this.part = part;
			this.parameterNumber = parameterNumber;
		}

		public String pasteParameter(String parameter) {
			return part + parameter;
		}
	}

}
