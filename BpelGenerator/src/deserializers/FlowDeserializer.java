package deserializers;

import generator.Activity;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

public class FlowDeserializer extends XmlDeserializer {

	@Override
	public List<Activity> deserialize(Node node) {
		XmlDeserializer des = DeserializerFactory.getDeserializer("flowStart");
		List<Activity> activities = new ArrayList<Activity>(3);
		activities.addAll(des.deserialize(node));
		XmlDeserializer des2 = DeserializerFactory.getDeserializer("flowInner");
		List<Activity> act = des2.deserialize(node);
		act.get(0).getActivities().addAll(0, activities);
		return act;
	}
}
