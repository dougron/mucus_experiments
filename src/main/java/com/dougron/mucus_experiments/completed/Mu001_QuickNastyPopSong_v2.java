/**
 * 
 */
package main.java.com.dougron.mucus_experiments.completed;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import main.java.da_utils.time_signature_utilities.time_signature.TimeSignature;
import main.java.da_utils.time_signature_utilities.time_signature_map_generator.TSGenInterface;
import main.java.da_utils.time_signature_utilities.time_signature_map_generator.TSMGenerator;
import main.java.da_utils.time_signature_utilities.time_signature_old.SubDivItem;

/**
 * @author dougr
 * 
 * reads xml file quicknasty_v2.xml and builds the song from instances of Mu class. 
 * 
 * testing the idea of a higher symbolic system to represent the music, but at the same time represent 
 * its internal state through (at this point, .musicxml score output) as well as playback output (via LiveClip)
 * 
 * 1st attempt will be to get the time signature thing working on one verse.
 *
 * as of 23 July 2020 (new TimeSignature class) this is BROKEN. Will not fix
 * as the whole idea of reading from an xml file is A WASTE OF TIME. Errors will
 * be commented out in a slapdash manner.
 * 
 */
public class Mu001_QuickNastyPopSong_v2 {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7206372914409009483L;
	private static boolean time_signature_item = false;
//	private TS ts;
	private static TimeSignature tempTimeSignature;
	static HashMap<Integer, TimeSignature> tsMap = new HashMap<Integer, TimeSignature>();
	static ArrayList<SubDivItem> subDivStack = new ArrayList<SubDivItem>();
	static int index;
static //	TimeSignatureMapGenerator tsmg = new TimeSignatureMapGenerator();	//TimeSignatureGenerator depreciated
	TSGenInterface tsg;
	private static boolean numerator = false;
	private static boolean denominator;
	private static boolean subdivision;
	private static boolean length;
	private static boolean priority;
//	private boolean time_signature_event;
	

	
	public static void main(String[] args) {
//		setSize(700, 1000);
		String path = "D:/Documents/miscForBackup/QuickNastyOutput/quicknasty_v2.xml";
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser(); 
			DefaultHandler handler = new DefaultHandler() {
				
				public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
					System.out.println("Start Element: " + qName);
					for (int i = 0; i < attributes.getLength(); i++) {
						System.out.println(attributes.getLocalName(i) + "=" + attributes.getValue(i));
					}
					//println(attributes.getValue("name"));
					//println("attributes count=" + attributes.getLength());
					
					doStartElement(qName, attributes);
								
				}
				
				public void endElement(String uri, String localName, String qName) throws SAXException {  
					System.out.println("End Element: " + qName);  
					doEndElement(qName);
				} 
				
				public void characters(char[] ch, int start, int length) throws SAXException{
					String x = new String(ch, start, length);
					System.out.println("start=" + start + " length=" + length + " " + x);
					doCharacters(x);
				}
			};
			
			
			saxParser.parse(path, handler);
			
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		printData();
	}


	protected static void doCharacters(String x) {
		if (numerator && time_signature_item) {
			tempTimeSignature.setNumerator(Integer.parseInt(x));
		} else if (denominator) {
			if (time_signature_item) {
				if (subdivision) {
					getTopOfSDIStack().setDenominator(Integer.parseInt(x));
				} else {
					tempTimeSignature.setDenominator(Integer.parseInt(x));
				}
			} 
		} else if (length) {
			if (time_signature_item) {
				if (subdivision) {
					getTopOfSDIStack().setLength(Integer.parseInt(x));
				}
			}	
		} else if (priority) {
			if (time_signature_item) {
				if (subdivision) {
					getTopOfSDIStack().setPriority(Integer.parseInt(x));
				}				
			}
		}
		
	}


	private static void printData() {
		System.out.println("TimeSignatureMap:");
		for (Integer i: tsMap.keySet()) {
			System.out.println("  index=" + i + " " + tsMap.get(i).toString());
		}
		
	}


	protected static void doEndElement(String qName) {
		switch (qName) {
		case "time_signature_item":
			time_signature_item = false;
			tsMap.put(index, tempTimeSignature);
			break;
		case "numerator":
			numerator = false;
			break;
		case "denominator":
			denominator = false;
			break;
		case "subdivision":
			if (subDivStack.size() == 1){
//				tempTimeSignature.setSubDivItem(subDivStack.get(0));
				subDivStack.clear();
				subdivision = false;
			} else {
				SubDivItem sdi = subDivStack.remove(subDivStack.size() - 1);
				getTopOfSDIStack().addToSDIList(sdi);
			}
			break;
		case "length":
			length = false;
			break;
		case "priority":
			priority = false;
			break;
		}
	}


	protected static void doStartElement(String qName, Attributes attributes) {
		switch (qName) {
		case "time_signature_item": 
			time_signature_item = true;
//			tempTimeSignature = new TimeSignature();
			index = Integer.parseInt(attributes.getValue("index"));
			break;
		case "numerator":
			numerator  = true;
			break;
		case "denominator":
			denominator = true;
			break;
		case "subdivision":
			subdivision = true;
			subDivStack.add(new SubDivItem());
			break;
		case "length":
			length = true;
			break;
		case "priority":
			priority = true;
			break;
		case "time_signature_event":
//			time_signature_event = true;
			tsg = new TSMGenerator();
			break;
		}
		
	}
	
	private static SubDivItem getTopOfSDIStack() {
		if (subDivStack.size() > 0) {
			return subDivStack.get(subDivStack.size() - 1);
		} 
		return null;
	}
}
