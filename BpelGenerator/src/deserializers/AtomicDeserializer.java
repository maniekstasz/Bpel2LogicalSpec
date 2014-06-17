package deserializers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Node;

import generator.Activity;

public class AtomicDeserializer extends XmlDeserializer {

	private char sign = 'a';
	private static final int aInt = 97;
	private static final int zInt = 122;
	private int currIndex = 0;
	List<Integer> letters = new ArrayList<Integer>(Arrays.asList(aInt));

	@Override
	public List<Activity> deserialize(Node node) {
		String letter = getLetter();
		updateLetters(currIndex);
		return Arrays.asList(new Activity(letter));
	}

	private String getLetter() {
		String res = "";
		for (Integer s : letters) {
			res += (char) (int) s;
		}
		return res;
	}

	private boolean updateLetters(int index) {
		if (index >= 0) {
			if (letters.get(index) == zInt) {
				boolean updated = updateLetters(index - 1);
				if (updated) {
					letters.set(index, aInt);
				} else {
					letters.set(index, aInt);
					if (!updated && index == currIndex) {
						currIndex++;
						letters.add(aInt);
					}
					return false;
				}
				
			} else {
				letters.set(index, letters.get(index) + 1);
				return true;
			}
		} else {
			return false;
		}
		return true;
	}

}
