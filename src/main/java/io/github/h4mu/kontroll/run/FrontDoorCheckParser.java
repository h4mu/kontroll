/**
 * 
 */
package io.github.h4mu.kontroll.run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author hamu
 *
 */
public class FrontDoorCheckParser {

	/**
	 * @param args
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws XPathExpressionException 
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		if (args.length < 1) {
			System.out.println("Usage: FrontDoorCheckParser <URL of wiki page>");
			return;
		}
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document document = builder.parse(new URL(args[0]).openStream());
		XPath path = XPathFactory.newInstance().newXPath();
		File file = new File("blacklist.txt");
		System.out.println(file.getAbsolutePath());
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			write(writer, "M1");
//			write(writer, "M2");
			write(writer, "M3");
			for (int i = 0; i < 3; i++) {
				XPathExpression expression = path.compile("//*[@id=\"mw-content-text\"]/p[" + (6 + i) + "]/a");
				NodeList nl = (NodeList) expression.evaluate(document, XPathConstants.NODESET);
				for (int j = 0; j < nl.getLength(); j++) {
					write(writer, nl.item(j).getTextContent());
				}
			}
		}
	}

	private static void write(BufferedWriter writer, String string) throws IOException {
		System.out.println(string);
		writer.write(string);
		writer.newLine();
	}

}
