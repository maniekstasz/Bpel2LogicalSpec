package tests;

import java.util.Arrays;

import parameters.ActivitySpecificationHolder;

import generator.Activity;
import generator.LogicalSpecyficationGeneratorImpl;
import generator.SpecificationPattern;
/**
 * 
 * @author Szymon Konicki
 *
 */
public class MainTest {

	public static void main(String[] args) {
		ActivitySpecificationHolder activitySpecificationHolder = new ActivitySpecificationHolder();
		activitySpecificationHolder.addPattern(SpecificationPattern
				.fromSource("0 ⇒ \u25C71 ∧ \u25C72,\u25A1¬(0 ∧ (1 ∨ 2))"),
				"flow");
		activitySpecificationHolder
				.addPattern(
						SpecificationPattern
								.fromSource("0∧ c(0) ⇒ \u25C71,0 ∧¬c(0) ⇒ \u25C72 ,\u25A1¬((0 ∧ 1)∨ (0 ∧2 ) ∨ (1 ∧ 2))"),
						"switch");
		activitySpecificationHolder.addPattern(
				SpecificationPattern.fromSource("0 ⇒ \u25C71,\u25C7¬(0 ∧1)"),
				"seq");

		Activity a = new Activity("a");
		Activity b = new Activity("b");
		Activity c = new Activity("c");
		Activity d = new Activity("d");
		Activity e = new Activity("e");
		Activity f = new Activity("f");
		Activity flow = new Activity(Arrays.asList(a, b, c), "flow");
		Activity sw = new Activity(Arrays.asList(d, e, f), "switch");
		Activity seq = new Activity(Arrays.asList(flow, sw), "seq");

		LogicalSpecyficationGeneratorImpl generator = new LogicalSpecyficationGeneratorImpl(
				activitySpecificationHolder, System.out);
		generator.generateLogicalSpecification(seq);
	}

}
