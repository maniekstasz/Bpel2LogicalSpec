package deserializers;

import generator.Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import deserializers.DeserializerFactory.DeserializerParametersHolder;

public class DefaultDeserializer extends XmlDeserializer {

	public final DeserializerParametersHolder parametersHolder;

	public DefaultDeserializer(DeserializerParametersHolder parametersHolder) {
		this.parametersHolder = parametersHolder;
	}

	@Override
	public List<Activity> deserialize(Node node) {
		List<Activity> activities = new ArrayList<Activity>(
				parametersHolder.activityCount);
		if (node.hasAttributes()) {
			for (int i = node.getAttributes().getLength() - 1; i >= 0; i--) {
				Attr attr = (Attr) node.getAttributes().item(i);
				if (DeserializerFactory.supportsAttribute(attr.getName())) {
					XmlDeserializer deserializer = DeserializerFactory
							.getDeserializer(attr.getName());
					List<Activity> toAdd = deserializer.deserialize(attr);
					if (toAdd != null)
						activities.addAll(toAdd);
				}
			}
		}
		if (node.hasChildNodes()) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				List<Activity> toAdd = processNode(node.getChildNodes().item(i));
				if (toAdd != null)
					activities.addAll(toAdd);
			}
		}
		if (activities.size() != parametersHolder.activityCount)
			throw new IllegalStateException("Too many activities specified at "
					+ node.getNodeName());
		if (parametersHolder.wrapper)
			return activities;
		return Arrays.asList(new Activity(activities, parametersHolder.name));
	}

}
