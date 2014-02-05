package com.salatiga.code;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * Class ini digunakan untuk memparsing data xml gempa 
 * @author Hendro Steven Tampake
 * @version 1.0
 *
 */
public class GempaXmlParser {
	private static final String ns = null;

	public List<Gempa> parse(InputStream in) throws XmlPullParserException,
			IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(in, null);
			parser.nextTag();
			return readFeed(parser);
		} finally {
			in.close();
		}
	}

	private List<Gempa> readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		List<Gempa> entries = new ArrayList<Gempa>();

		parser.require(XmlPullParser.START_TAG, ns, "Infogempa");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			// Starts by looking for the gempa tag
			if (name.equals("Gempa")) {
				entries.add(readGempa(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}


	private Gempa readGempa(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "Gempa");
		String tanggal = null;
		String latitude = null;
		String longitude = null;
		String magnitude = null;
		String kedalaman = null;
		String keterangan = null;
		String dirasakan = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equalsIgnoreCase("Tanggal")) {
				tanggal = readTags(parser, "Tanggal");
			} else if (name.equalsIgnoreCase("point")) {
				while (parser.next() != XmlPullParser.END_TAG) {
					if (parser.getEventType() != XmlPullParser.START_TAG) {
						continue;
					}
					String temp = parser.getName();
					if (temp.equalsIgnoreCase("coordinates")) {
						String[] coordinates = readTags(parser, "coordinates")
								.split(",");
						latitude = coordinates[0].trim();
						longitude = coordinates[1].trim();
					} else {
						skip(parser);
					}
				}
			} else if (name.equalsIgnoreCase("Magnitude")) {
				magnitude = readTags(parser, "Magnitude");
			} else if (name.equalsIgnoreCase("Kedalaman")) {
				kedalaman = readTags(parser, "Kedalaman");
			} else if (name.equalsIgnoreCase("Keterangan")) {
				keterangan = readTags(parser, "Keterangan");
			} else if (name.equalsIgnoreCase("Dirasakan")) {
				dirasakan = readTags(parser, "Dirasakan");
			} else {
				skip(parser);
			}
		}
		Gempa gempa = new Gempa(tanggal, latitude, longitude, magnitude,
				kedalaman, keterangan, dirasakan);
		return gempa;
	}

	// Processes title tags in the feed.
	private String readTags(XmlPullParser parser, String tag)
			throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, tag);
		String temp = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, tag);
		return temp;
	}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	// Skips tags the parser isn't interested in. Uses depth to handle nested
	// tags. i.e.,
	// if the next tag after a START_TAG isn't a matching END_TAG, it keeps
	// going until it
	// finds the matching END_TAG (as indicated by the value of "depth" being
	// 0).
	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
