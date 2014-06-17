package deserializers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Node;

import generator.Activity;

public class XmlDeserializer {

	public List<Activity> deserialize(Node node) {
		return processNode(node);
	}

	protected List<Activity> processNode(Node node) {
		List<Activity> activities = new ArrayList<Activity>();
		if ((node.getNodeType() == Node.DOCUMENT_NODE || !DeserializerFactory.bpelWords.contains(node.getNodeName())) && node.hasChildNodes()) {
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				List<Activity> toAdd = processNode(node.getChildNodes().item(i));
				if (toAdd != null)
					activities.addAll(toAdd);
			}
			return activities;
		}
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return null;
		String nodeName = node.getNodeName();
		if (!DeserializerFactory.bpelWords.contains(nodeName))
			return activities;
		XmlDeserializer deserializer = DeserializerFactory
				.getDeserializer(nodeName);
		return deserializer.deserialize(node);
	}


}
