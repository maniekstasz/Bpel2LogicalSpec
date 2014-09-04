package main;

import generator.Activity;
import generator.LogicalSpecyficationGeneratorImpl;
import generator.SpecificationPattern;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.Box;
import javax.swing.JButton;

import java.awt.GridLayout;

import javax.swing.SwingConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import parameters.ActivitySpecificationHolder;
import deserializers.DeserializerFactory;
import deserializers.XmlDeserializer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	static String pFilePath, dictionaryFilePath, bpelFilePath, outputFilePath;
	static JButton btnSelectPFile, btnSelectDictionaryFile, btnSelectBpelFile, btnGenerateLogicalSpec;
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 210, 210);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		Box verticalBox = Box.createVerticalBox();
		frame.getContentPane().add(verticalBox);
		
		btnSelectPFile = new JButton("Select p file");
		btnSelectPFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pFilePath = getFilePath();
				reloadButtonNames();
			}
		});
		btnSelectPFile.setHorizontalAlignment(SwingConstants.RIGHT);
		verticalBox.add(btnSelectPFile);
		
		btnSelectDictionaryFile = new JButton("Select dictionary file");
		btnSelectDictionaryFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dictionaryFilePath = getFilePath();
				reloadButtonNames();
			}
		});
		verticalBox.add(btnSelectDictionaryFile);
		
		btnSelectBpelFile = new JButton("Select BPEL file");
		btnSelectBpelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bpelFilePath = getFilePath();
				reloadButtonNames();
			}
		});
		verticalBox.add(btnSelectBpelFile);
		
		btnGenerateLogicalSpec = new JButton("Generate logical spec");
		btnGenerateLogicalSpec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					outputFilePath = getFilePath();
					generateBPEL(pFilePath, dictionaryFilePath, bpelFilePath, outputFilePath);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnGenerateLogicalSpec);
		reloadButtonNames();
	}
	
	public static void convertBpel() throws FileNotFoundException {
		generateBPEL(bpelFilePath, dictionaryFilePath, pFilePath, "/log_spec.txt");
	}
	
	public static void reloadButtonNames() {
		btnSelectPFile.setText(pFilePath != null ? pFilePath : "Select p file");
		btnSelectDictionaryFile.setText(dictionaryFilePath != null ? dictionaryFilePath : "Select dictionary file");
		btnSelectBpelFile.setText(bpelFilePath != null ? bpelFilePath : "Select BPEL file");
		btnGenerateLogicalSpec.setEnabled(pFilePath != null && dictionaryFilePath != null && bpelFilePath != null);
	}
	
	public static String getFilePath() {
		JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

	    if( fc.showOpenDialog( null ) == JFileChooser.APPROVE_OPTION )
	    {
	        return fc.getSelectedFile().getAbsolutePath();
	    }
	    return null;
	}
	
	public static void generateBPEL(String pFilePath, String dictionaryFilePath, String bpelFilePath, String resultFileName) throws FileNotFoundException {
		System.out.println(pFilePath + "\n" + dictionaryFilePath + "\n" + bpelFilePath + "\n" + resultFileName);
		ActivitySpecificationHolder activitySpecificationHolder = geteActivitySpecificationHolder(pFilePath);
		processDictionaryFile(dictionaryFilePath);
		List<Activity> activities = deserializeBpel(bpelFilePath);
		OutputStream stream = new FileOutputStream(new File(resultFileName));
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
