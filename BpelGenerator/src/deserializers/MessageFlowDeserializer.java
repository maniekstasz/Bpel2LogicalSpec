package deserializers;

import generator.Activity;
import generator.MessageFlowActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import deserializers.DeserializerContext.MessageFlowElem.MessageType;
import deserializers.DeserializerFactory.DeserializerParametersHolder;

public class MessageFlowDeserializer extends XmlDeserializer {

	private final DeserializerContext deserializerContext;
	private final MessageType type;
	private final String name;

	public MessageFlowDeserializer(String name, MessageType type) {
		this.deserializerContext = DeserializerFactory.getDesrializerContext();
		this.type = type;
		this.name = name;
	}

	@Override
	public List<Activity> deserialize(Node node) {
		List<Activity> activities = new ArrayList<Activity>(2);
		MessageFlowActivity currActivity = new MessageFlowActivity(activities,name );
		if (node.hasAttributes()) {
			for (int i = node.getAttributes().getLength() - 1; i >= 0; i--) {
				Attr attr = (Attr) node.getAttributes().item(i);
				if (attr.getName().equals("partnerLink")) {
					XmlDeserializer deserializer = DeserializerFactory
							.getDeserializer(attr.getName());
					List<Activity> toAdd = deserializer.deserialize(attr);
					if (toAdd != null) {
						activities.addAll(toAdd);
						deserializerContext.addMessageFlowElem(attr.getValue(), currActivity, type);
						deserializerContext.exchangeAtomicActivities(attr.getValue());
							
					}
				}
			}
		}
		return Arrays.asList((Activity)currActivity);
	}
}
