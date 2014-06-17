package main;

import generator.Activity;
import generator.LogicalSpecyficationGeneratorImpl;
import generator.SpecificationPattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import deserializers.XmlDeserializer;

import parameters.ActivitySpecificationHolder;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ActivitySpecificationHolder activitySpecificationHolder = geteActivitySpecificationHolder(args);
		List<Activity> activities = deserializeBpel(args);
		generateSpecification(activities, activitySpecificationHolder);
	}

	private static void generateSpecification(List<Activity> activities,
			ActivitySpecificationHolder activitySpecificationHolder) {
		LogicalSpecyficationGeneratorImpl generator = new LogicalSpecyficationGeneratorImpl(
				activitySpecificationHolder, System.out);
		for (Activity act : activities)
			generator.generateLogicalSpecification(act);
	}

	private static List<Activity> deserializeBpel(String[] args) {
		String bpelFileName = args[0];
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder;
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(bpelFileName));
			doc.getDocumentElement().normalize();
			XmlDeserializer deserializer = new XmlDeserializer();
			List<Activity> activities = deserializer.deserialize(doc);
			return activities;
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
		return null;
	}

	private static ActivitySpecificationHolder geteActivitySpecificationHolder(
			String[] args) {
		String parameterFilename;
		if (args.length == 1) {
			parameterFilename = "C:/informatyka/p.txt";
		} else {
			parameterFilename = args[1];
		}
		ActivitySpecificationHolder activitySpecificationHolder = new ActivitySpecificationHolder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(parameterFilename));
			String line;

			while ((line = br.readLine()) != null) {
				int index = line.indexOf("=");
				String activityName = line.substring(0, index);
				String spec = line.substring(index + 1);
				activitySpecificationHolder.addPattern(
						SpecificationPattern.fromSource(getTranslatedPatternSource(spec)), activityName);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return activitySpecificationHolder;
	}
	
	private static String getTranslatedPatternSource(String source){
		String result = source.replaceAll("<>", "\u25C7");
		result = result.replaceAll("\\[\\]", "\u25A1");
		result = result.replaceAll("&", "∧");
		result = result.replaceAll("\\|", "∨");
		result = result.replaceAll("~", "¬");
		result = result.replaceAll("=>", "⇒");
		System.out.println(result);
		return result;
	}

}
