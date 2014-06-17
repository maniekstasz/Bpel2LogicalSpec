package generator;


import java.util.List;

public class Activity {

	protected List<Activity> activities;

	protected String letter;
	
	protected final String activityName;

	public Activity(String letter) {
		this.letter = letter;
		this.activities = null;
		this.activityName = null;
	}

	public Activity(List<Activity> activities, String activityName) {
		this.letter = null;
		this.activities = activities;
		this.activityName = activityName;
	}

	public String getLettersDisjunction(String delimetedSign) {
		if (letter != null)
			return letter;
		String lettersDisjunction = null;
		for (Activity act : activities) {
			String subActivityLettersDisjunction = act
					.getLettersDisjunction(delimetedSign);
			if (subActivityLettersDisjunction != null
					&& !subActivityLettersDisjunction.isEmpty()) {
				if (lettersDisjunction != null) {
					lettersDisjunction += delimetedSign
							+ subActivityLettersDisjunction;
				}else{
					lettersDisjunction = subActivityLettersDisjunction;
				}
			}
		}
		return lettersDisjunction;
	}

	public void parseActivity(LogicalSpecyficationGenerator generator) {
		if (letter != null)
			return;
		for (Activity act : activities) {
			act.parseActivity(generator);
		}
		String parametersLetters[] = new String[activities.size()];
		for (int i = 0; i < activities.size(); i++) {
			String letters = activities.get(i).getLettersDisjunction(
					generator.getDelimetedSign());
			parametersLetters[i] = letters;
		}
		generator
				.generateActivitySpecyfication(parametersLetters, activityName);
	}
	
	
	@Override
	public String toString() {
		String result = activityName + " ";
		if(activities != null){
			for(Activity act : activities){
				result += act.toString() + " ";
			}
		}else{
			result += letter + " ";
		}
		return result;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public List<Activity> getActivities() {
		return activities;
	}

//	public void setActivities(List<Activity> activities) {
//		this.activities = activities;
//	}

}
