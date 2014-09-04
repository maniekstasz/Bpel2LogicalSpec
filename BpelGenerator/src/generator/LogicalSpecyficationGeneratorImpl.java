package generator;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import deserializers.DeserializerContext;
import deserializers.DeserializerFactory;
import deserializers.DeserializerContext.MessageFlowPair;

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
		try {
			this.stream = new PrintStream(outputStream, false, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException();
		}
	}

	public void initGeneration() {
		// stream.print(LOGICAL_SPECIFICATION_BEGINNING);
		firstLineWritten = false;
	}

	public void finishGeneration() {
		// stream.print(LOGICAL_SPECIFICATION_ENDING);
		// stream.print(result);
		printMessageFlows();
		stream.close();
	}

	public void generateLogicalSpecification(Activity activity) {
		// initGeneration();
		activity.parseActivity(this);
		// finishGeneration();
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
				stream.print(" ∧\r\n");
			}
			stream.print("(" + part + ")");
		}
	}

	private void printMessageFlows() {
		DeserializerContext deserializerContext = DeserializerFactory
				.getDesrializerContext();

		SpecificationPattern specificationPattern = specificationPatternHolder
				.getPattern("messageFlow");

		for (MessageFlowPair pair : deserializerContext.getPairs()) {
			String pairLetters[] = new String[] {
					pair.invoke.activity.getActivities().get(0).getLetter(),
					pair.receive.activity.getActivities().get(0).getLetter() };
			stream.print(" ∧\r\n");
			stream.print("("
					+ specificationPattern.getSpecification(pairLetters) + ")");
		}
	}

	@Override
	public String getDelimetedSign() {
		return DISJUNCTION_SIGN;
	}

}
