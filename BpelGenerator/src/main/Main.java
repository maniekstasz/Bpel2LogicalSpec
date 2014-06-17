package main;

import generator.Activity;
import generator.LogicalSpecyficationGeneratorImpl;
import generator.SpecificationPattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import deserializers.DeserializerFactory;
import deserializers.XmlDeserializer;

import parameters.ActivitySpecificationHolder;

public class Main {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		ActivitySpecificationHolder activitySpecificationHolder = geteActivitySpecificationHolder(args[0]);
		processDictionaryFile(args[1]);
		List<Activity> activities = deserializeBpel(args[2]);
		OutputStream stream = System.out;
		if (args.length == 4)
			stream = new FileOutputStream(new File(args[3]));
		generateSpecification(activities, activitySpecificationHolder, stream);
	}

	private static void generateSpecification(List<Activity> activities,
			ActivitySpecificationHolder activitySpecificationHolder,
			OutputStream stream) {
		LogicalSpecyficationGeneratorImpl generator = new LogicalSpecyficationGeneratorImpl(
				activitySpecificationHolder, stream);
		generator.initGeneration();
		for (Activity act : activities)
			generator.generateLogicalSpecification(act);
		generator.finishGeneration();
	}

	private static List<Activity> deserializeBpel(String bpelFileName) {
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

	private static void processDictionaryFile(String dictionaryFilePath) {
		BufferedReader br = null;
		Set<String> bpelWords = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader(dictionaryFilePath));
			String line;
			while ((line = br.readLine()) != null) {
				bpelWords.add(line);
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
		DeserializerFactory.bpelWords = bpelWords;
	}

	private static ActivitySpecificationHolder geteActivitySpecificationHolder(
			String parameterFileName) {
		ActivitySpecificationHolder activitySpecificationHolder = new ActivitySpecificationHolder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(parameterFileName));
			String line;

			while ((line = br.readLine()) != null) {
				int index = line.indexOf("=");
				String activityName = line.substring(0, index);
				String spec = line.substring(index + 1);
				activitySpecificationHolder.addPattern(SpecificationPattern
						.fromSource(getTranslatedPatternSource(spec)),
						activityName);
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

	private static String getTranslatedPatternSource(String source) {
		String result = source.replaceAll("<>", "\u25C7");
		result = result.replaceAll("\\[\\]", "\u25A1");
		result = result.replaceAll("&", "∧");
		result = result.replaceAll("\\|", "∨");
		result = result.replaceAll("~", "¬");
		result = result.replaceAll("=>", "⇒");
		return result;
	}

}
