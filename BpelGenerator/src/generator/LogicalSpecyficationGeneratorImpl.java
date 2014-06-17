package generator;

import java.io.OutputStream;
import java.io.PrintStream;

import parameters.ActivitySpecificationHolder;

public class LogicalSpecyficationGeneratorImpl implements
		LogicalSpecyficationGenerator {

	private final static String DISJUNCTION_SIGN = " \u2228 ";
	// private final static String LOGICAL_SPECIFICATION_BEGINNING = "L={";
	// private final static String LOGICAL_SPECIFICATION_ENDING = "}";
	private final ActivitySpecificationHolder specificationPatternHolder;
	private boolean firstLineWritten = false;
	private final PrintStream stream;

	public LogicalSpecyficationGeneratorImpl(
			ActivitySpecificationHolder specificationHolder,
			OutputStream outputStream) {
		this.specificationPatternHolder = specificationHolder;
		this.stream = new PrintStream(outputStream);
	}

	private void initGeneration() {
		// stream.print(LOGICAL_SPECIFICATION_BEGINNING);
		firstLineWritten = false;
	}

	private void finishGeneration() {
		// stream.print(LOGICAL_SPECIFICATION_ENDING);
		// stream.print(result);
		stream.close();
	}

	public void generateLogicalSpecification(Activity activity) {
		initGeneration();
		activity.parseActivity(this);
		finishGeneration();
	}

	@Override
	public void generateActivitySpecyfication(String[] parametersLetters,
			String activityName) {
		SpecificationPattern specificationPattern = specificationPatternHolder
				.getPattern(activityName);
		String spec = specificationPattern.getSpecification(parametersLetters);
		String[] parts = spec.split(",");
		for (String part : parts) {
			if (!firstLineWritten) {
				firstLineWritten = true;
			} else {
				stream.print(" ∧\n");
			}
			stream.print(part);
		}
	}

	@Override
	public String getDelimetedSign() {
		return DISJUNCTION_SIGN;
	}

}
