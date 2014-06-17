package deserializers;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Node;

import generator.Activity;

public class AtomicDeserializer extends XmlDeserializer {

	private char sign = 'a';

	@Override
	public List<Activity> deserialize(Node node) {
		return Arrays.asList(new Activity(new String("" + sign++)));
	}
}
