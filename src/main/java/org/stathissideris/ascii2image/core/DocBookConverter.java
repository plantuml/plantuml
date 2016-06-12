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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

// using SAX
public class DocBookConverter {

	class HowToHandler extends DefaultHandler {
    	boolean title = false;
    	boolean url   = false;

    	public void startElement(
    		String nsURI,
    		String strippedName,
			String tagName,
			Attributes attributes)
       			throws SAXException {
     		if (tagName.equalsIgnoreCase("title"))
        	title = true;
     		if (tagName.equalsIgnoreCase("url"))
        		url = true;
    		}

    	public void characters(char[] ch, int start, int length) {
     		if (title) {
       			System.out.println("Title: " + new String(ch, start, length));
       			title = false;
       		} else if (url) {
       			System.out.println("Url: " + new String(ch, start,length));
       			url = false;
			}
		}
    }

    public void list( ) throws Exception {
		XMLReader parser =
			XMLReaderFactory.createXMLReader
            	("org.apache.crimson.parser.XMLReaderImpl");
		parser.setContentHandler(new HowToHandler( ));
		parser.parse("howto.xml");
	}

	public static void main(String[] args) throws Exception {
		new DocBookConverter().list( );
	}
}
