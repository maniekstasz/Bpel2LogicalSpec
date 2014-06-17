package deserializers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.dataformat.xml.XmlAnnotationIntrospector.Pair;

import deserializers.DeserializerContext.MessageFlowElem.MessageType;

import generator.Activity;

public class DeserializerContext {

	private Map<String, MessageFlowPair> messagesFlowPairs = new HashMap<String, MessageFlowPair>();

	public void reset() {
		messagesFlowPairs.clear();
	}

	public Collection<MessageFlowPair> getPairs() {
		return messagesFlowPairs.values();
	}

	public void addMessageFlowElem(String link, Activity activity,
			MessageType type) {
		MessageFlowPair pair = messagesFlowPairs.get(link);
		if (pair == null)
			pair = new MessageFlowPair();
		messagesFlowPairs
				.put(link,
						addToMessageFlowPair(pair,
								new MessageFlowElem(activity), type));
	}

	public void exchangeAtomicActivities(String link) {
		if (!isPairCompleted(link))
			return;
		MessageFlowPair pair = messagesFlowPairs.get(link);
		pair.invoke.activity.getActivities().add(
				pair.receive.activity.getActivities().get(0));
		pair.receive.activity.getActivities().add(
				pair.invoke.activity.getActivities().get(0));
	}

	private boolean isPairCompleted(String link) {
		MessageFlowPair pair = messagesFlowPairs.get(link);
		if (pair == null)
			return false;
		return pair.invoke != null && pair.receive != null;
	}

	public MessageFlowPair getPair(String link) {
		MessageFlowPair pair = messagesFlowPairs.get(link);
		if (pair == null)
			throw new IllegalStateException();
		return pair;
	}

	private MessageFlowPair addToMessageFlowPair(MessageFlowPair pair,
			MessageFlowElem elem, MessageType type) {
		if (type == MessageType.INVOKE)
			pair.invoke = elem;
		else
			pair.receive = elem;
		return pair;
	}

	public static class MessageFlowPair {
		public MessageFlowElem invoke;
		public MessageFlowElem receive;
	}

	public static class MessageFlowElem {
		public enum MessageType {
			INVOKE, RECEIVE
		}

		public final Activity activity;

		public MessageFlowElem(Activity activity) {
			super();
			this.activity = activity;
		}
	}
}
