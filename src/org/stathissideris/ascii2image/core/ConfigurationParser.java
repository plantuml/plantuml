/*
 * DiTAA - Diagrams Through Ascii Art
 * 
 * Copyright (C) 2004 Efstathios Sideris
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *   
 */
package org.stathissideris.ascii2image.core;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.stathissideris.ascii2image.graphics.CustomShapeDefinition;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConfigurationParser {

	private static final boolean DEBUG = false;
	
	private static final String INLCUDE_TAG_NAME = "include";
	private static final String SHAPE_TAG_NAME = "shape";
	private static final String SHAPE_GROUP_TAG_NAME = "shapes";
	
	private String currentDir = "";
	private File configFile;
	
	private HashMap<String, CustomShapeDefinition> shapeDefinitions =
		new HashMap<String, CustomShapeDefinition>();
	
	public Collection<CustomShapeDefinition> getShapeDefinitions() {
		return shapeDefinitions.values();
	}

	public HashMap<String, CustomShapeDefinition> getShapeDefinitionsHash() {
		return shapeDefinitions;
	}
	
	public void parseFile(File file)
		throws ParserConfigurationException, SAXException, IOException
	{
		configFile = file;
		
		DefaultHandler handler = new XMLHandler();
		
		// Use the default (non-validating) parser
		SAXParserFactory factory = SAXParserFactory.newInstance();

		SAXParser saxParser = factory.newSAXParser();
		saxParser.parse(file, handler);
	}
	
	private class XMLHandler extends DefaultHandler{
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if(qName.equals(SHAPE_GROUP_TAG_NAME)){
				if(attributes.getLength() == 1){
					currentDir = attributes.getValue(0).trim();
					if(currentDir.equals("")) currentDir = configFile.getParentFile().getAbsolutePath();
				} else {
					//the dir that contains the config file:
					currentDir = configFile.getParentFile().getAbsolutePath();
				}
			}
			if(qName.equals(SHAPE_TAG_NAME)){
				CustomShapeDefinition definition = new CustomShapeDefinition();
				
				int len = attributes.getLength();
				for(int i = 0; i < len; i++){
					String name = attributes.getQName(i);
					String value = attributes.getValue(i);
					
					if(name.equals("tag")){
						definition.setTag(value);
					} else if(name.equals("stretch")){
						definition.setStretches(getBooleanFromAttributeValue(value));
					} else if(name.equals("border")){
						definition.setHasBorder(getBooleanFromAttributeValue(value));
					} else if(name.equals("shadow")){
						definition.setDropsShadow(getBooleanFromAttributeValue(value));
					} else if(name.equals("comment")){
						definition.setComment(value);
					} else if(name.equals("filename")){
						File file = new File(value);
						if(file.isAbsolute()){
							definition.setFilename(value);
						} else { //relative to the location of the config file or to the group's base dir
							definition.setFilename(createFilename(currentDir, value));
						}
					}
				}
				
				if(shapeDefinitions.containsKey(definition.getTag())){
					CustomShapeDefinition oldDef = shapeDefinitions.get(definition.getTag());
					System.err.println(
						"*** Warning: shape \""+oldDef.getTag()+"\" (file: "
						+oldDef.getFilename()+") has been redefined as file: "
						+definition.getFilename()
					);
				}
				
				File file = new File(definition.getFilename());
				if(file.exists()){
					shapeDefinitions.put(definition.getTag(), definition);
					if(DEBUG) System.out.println(definition);
				} else {
					System.err.println("File "+file+" does not exist, skipping tag "+definition.getTag());
				}
				
			}
			if(qName.equals(INLCUDE_TAG_NAME)){
				if(attributes.getLength() == 1){
					File includedFile = new File(attributes.getValue(0).trim());
					
					if(!includedFile.isAbsolute()){
						includedFile = new File(
								createFilename(
									configFile.getParentFile().getAbsolutePath(),
									includedFile.getPath()));
					}
					
					if(!includedFile.exists()){
						System.err.println("Included file "+includedFile+" does not exist, skipping");
						return;
					}
					
					ConfigurationParser configParser = new ConfigurationParser();
					try {
						configParser.parseFile(includedFile);
					} catch (ParserConfigurationException e) {
						e.printStackTrace();
					} catch (SAXException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					HashMap<String, CustomShapeDefinition> shapes = configParser.getShapeDefinitionsHash();
					shapeDefinitions.putAll(shapes);
				}
			}
		}
	}
	
	private String createFilename(String baseDir, String filename){
		if(baseDir == null || baseDir.trim().equals("")){
			return filename;
		}
		if(baseDir.endsWith(File.separator)){
			return baseDir + filename;
		}
		return baseDir + File.separator + filename;
	}
	
	private boolean getBooleanFromAttributeValue(String value){
		value = value.toLowerCase();
		if("no".equals(value)) return false;
		if("false".equals(value)) return false;
		if("yes".equals(value)) return true;
		if("true".equals(value)) return true;
		throw new IllegalArgumentException("value "+value+" cannot be interpreted as a boolean");
	}
	
	public static void main(String argv[]) throws ParserConfigurationException, SAXException, IOException {
		ConfigurationParser parser = new ConfigurationParser();
		parser.parseFile(new File("config.xml"));
		parser.getShapeDefinitions();
	}
	

	
}
