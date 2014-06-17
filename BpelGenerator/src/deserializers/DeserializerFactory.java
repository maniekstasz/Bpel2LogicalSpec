package deserializers;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import deserializers.DeserializerContext.MessageFlowElem.MessageType;

public class DeserializerFactory {

	private static final Map<String, XmlDeserializer> deserializers;
	private static final Map<String, DeserializerParametersHolder> deserializerParametersMap;
	private static final Set<String> attributes;
	private static final AtomicDeserializer atomicDeserializer = new AtomicDeserializer();
	private static final DeserializerContext desrializerContext = new DeserializerContext();
	public static Set<String> bpelWords;
	static {
		Map<String, DeserializerParametersHolder> desParameters = new HashMap<String, DeserializerParametersHolder>();
		desParameters.put("sequence", new DeserializerParametersHolder(2, 2,
				"seq"));
		desParameters.put("flowInner",
				new DeserializerParametersHolder(2, 2, "flow"));
		desParameters.put("switch", new DeserializerParametersHolder(2, 3,
				"switch"));
		desParameters.put("case", new DeserializerParametersHolder(2, 2));
		desParameters.put("otherwise", new DeserializerParametersHolder(1, 1));
		desParameters.put("while", new DeserializerParametersHolder(2, 2,
				"while"));

		Set<String> tempAttr = new HashSet<String>();
		tempAttr.add("condition");

		Map<String, XmlDeserializer> des = new HashMap<String, XmlDeserializer>();
		des.put("flow", new FlowDeserializer());
		des.put("invoke", new MessageFlowDeserializer("invoke", MessageType.INVOKE));
		des.put("receive", new MessageFlowDeserializer("receive", MessageType.RECEIVE));
		deserializerParametersMap = Collections.unmodifiableMap(desParameters);
		deserializers = Collections.unmodifiableMap(des);
		attributes = Collections.unmodifiableSet(tempAttr);
	}

	public static XmlDeserializer getDeserializer(String name) {
		XmlDeserializer customDeserializer = deserializers.get(name);
		if (customDeserializer != null)
			return customDeserializer;
		DeserializerParametersHolder parametersHolder = deserializerParametersMap
				.get(name);
		return parametersHolder == null ? atomicDeserializer
				: new DefaultDeserializer(parametersHolder);
	}

	public static DeserializerParametersHolder getDeserializerParametersHolder(
			String name) {
		return deserializerParametersMap.get(name);
	}

	public static boolean supportsAttribute(String attrName) {
		return attributes.contains(attrName);
	}

	public static class DeserializerParametersHolder {
		public final int iterationCount;
		public final int activityCount;
		public final boolean wrapper;
		public final String name;

		public DeserializerParametersHolder(int count, int activityCount) {
			this.iterationCount = count;
			this.activityCount = activityCount;
			this.wrapper = true;
			this.name = null;
		}

		public DeserializerParametersHolder(int count, int activityCount,
				String name) {
			this.iterationCount = count;
			this.activityCount = activityCount;
			this.name = name;
			this.wrapper = false;
		}

	}

	public static DeserializerContext getDesrializerContext() {
		return desrializerContext;
	}

}
