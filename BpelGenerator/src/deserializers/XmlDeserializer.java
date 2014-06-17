package deserializers;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import generator.Activity;

public class XmlDeserializer {

	public List<Activity> deserialize(Node node) {
		return processNode(node);
	}

	// public static List<Activity> getActivity(FromXmlParser xmlParser) throws
	// JsonParseException, IOException{
	// JsonToken t = xmlParser.nextToken();
	// if(t != JsonToken.FIELD_NAME && t != JsonToken.START_OBJECT && t !=
	// JsonToken.END_OBJECT)
	// xmlParser.nextToken();
	// String name = xmlParser.getCurrentName();
	// boolean started = false;
	// if(xmlParser.nextToken() == JsonToken.START_OBJECT){
	// started = true;
	// }
	// XmlDeserializer xmlDeserializer =
	// DeserializerFactory.getDeserializer(name);
	// List<Activity> activities = xmlDeserializer.deserialize(xmlParser);
	// if(started)
	// xmlParser.nextToken();
	// return activities;
	// }

	protected List<Activity> processNode(Node node) {
		if (node.getNodeType() == Node.DOCUMENT_NODE && node.hasChildNodes()) {
			List<Activity> activities = new ArrayList<Activity>();
			for (int i = 0; i < node.getChildNodes().getLength(); i++) {
				List<Activity> toAdd = processNode(node.getChildNodes().item(i));
				if(toAdd != null)
					activities.addAll(toAdd);
			}
			return activities;
		}
//		System.out.println(node.getNodeName());
//		System.out.println(node.getNodeType());
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return null;
		String nodeName = node.getNodeName();
		XmlDeserializer deserializer = DeserializerFactory
				.getDeserializer(nodeName);
		return deserializer.deserialize(node);
	}
}
