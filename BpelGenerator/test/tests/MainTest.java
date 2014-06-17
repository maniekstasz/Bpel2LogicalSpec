package tests;

import generator.Activity;
import generator.LogicalSpecyficationGeneratorImpl;
import generator.SpecificationPattern;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import parameters.ActivitySpecificationHolder;

import deserializers.XmlDeserializer;

/**
 * 
 * @author Szymon Konicki
 * 
 */
public class MainTest {

	public static void main(String[] args) {
		ActivitySpecificationHolder activitySpecificationHolder = new ActivitySpecificationHolder();
		activitySpecificationHolder.addPattern(SpecificationPattern
				.fromSource("0 ⇒ \u25C71 ∧ \u25C72,\u25A1¬(0 ∧ (1 ∨ 2))"),
				"flow");
		activitySpecificationHolder
				.addPattern(
						SpecificationPattern
								.fromSource("0∧ c(0) ⇒ \u25C71,0 ∧¬c(0) ⇒ \u25C72 ,\u25A1¬((0 ∧ 1)∨ (0 ∧2 ) ∨ (1 ∧ 2))"),
						"switch");
		activitySpecificationHolder.addPattern(
				SpecificationPattern.fromSource("0 ⇒ \u25C71,\u25C7¬(0 ∧1)"),
				"seq");
		activitySpecificationHolder
				.addPattern(
						SpecificationPattern
								.fromSource("0 ∧ c(0)⇒  \u25C71 ,0 ∧ ¬c(0)⇒ ¬\u25C7 1\u25C7¬(0 ∧ 1)"),
						"while");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File("C:/informatyka/test.xml"));
			doc.getDocumentElement().normalize();
			XmlDeserializer deserializer = new XmlDeserializer();
			List<Activity> activities = deserializer.deserialize(doc);

			LogicalSpecyficationGeneratorImpl generator = new LogicalSpecyficationGeneratorImpl(
					activitySpecificationHolder, System.out);
			for (Activity act : activities)
				generator.generateLogicalSpecification(act);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void print(Node element) {
		System.out.println(element.getNodeName());
		if (element.hasAttributes()) {
			for (int i = 0; i < element.getAttributes().getLength(); i++) {
				System.out.println(element.getAttributes().item(i)
						.getNodeName());
			}
		}
		if (element.hasChildNodes())
			for (int i = 0; i < element.getChildNodes().getLength(); i++) {
				print(element.getChildNodes().item(i));
			}
	}

}
