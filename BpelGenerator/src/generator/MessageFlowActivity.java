package generator;

import java.util.List;

import parameters.ActivitySpecificationHolder;

public class MessageFlowActivity extends Activity {
	
	
	public MessageFlowActivity(List<Activity> activities, String activityName) {
		super(activities, activityName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLettersDisjunction(String delimetedSign) {
		String letters[] = new String[]{activities.get(0).getLetter(), activities.get(1).getLetter()};
		SpecificationPattern specificationPattern = ActivitySpecificationHolder.getPattern(activityName);
		return "(" + specificationPattern.getSpecification(letters) + ")";
	}
	@Override
	public void parseActivity(LogicalSpecyficationGenerator generator) {
		
	}
}
