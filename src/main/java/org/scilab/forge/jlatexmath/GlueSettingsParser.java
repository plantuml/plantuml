/* GlueSettingsParser.java
 * =========================================================================
 * This file is originally part of the JMathTeX Library - http://jmathtex.sourceforge.net
 *
 * Copyright (C) 2004-2007 Universiteit Gent
 * Copyright (C) 2009 DENIZET Calixte
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * A copy of the GNU General Public License can be found in the file
 * LICENSE.txt provided with the source distribution of this program (see
 * the META-INF directory in the source jar). This license can also be
 * found on the GNU website at http://www.gnu.org/licenses/gpl.html.
 *
 * If you did not receive a copy of the GNU General Public License along
 * with this program, contact the lead developer, or write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 *
 * Linking this library statically or dynamically with other modules
 * is making a combined work based on this library. Thus, the terms
 * and conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce
 * an executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under terms
 * of your choice, provided that you also meet, for each linked independent
 * module, the terms and conditions of the license of that module.
 * An independent module is a module which is not derived from or based
 * on this library. If you modify this library, you may extend this exception
 * to your version of the library, but you are not obliged to do so.
 * If you do not wish to do so, delete this exception statement from your
 * version.
 *
 */

package org.scilab.forge.jlatexmath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Parses the glue settings (different types and rules) from an XML-file.
 */
public class GlueSettingsParser {

    private static final String RESOURCE_NAME = "GlueSettings.xml";

    private final Map<String,Integer> typeMappings = new HashMap<String,Integer>();
    private final Map<String,Integer> glueTypeMappings = new HashMap<String,Integer>();
    private Glue[] glueTypes;

    private final Map<String,Integer> styleMappings = new HashMap<String,Integer>();

    private Element root;

    public GlueSettingsParser() throws ResourceParseException {
        try {
            setTypeMappings();
            setStyleMappings();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            root = factory.newDocumentBuilder().parse(GlueSettingsParser.class.getResourceAsStream(RESOURCE_NAME)).getDocumentElement();
            parseGlueTypes();
        } catch (Exception e) { // JDOMException or IOException
            throw new XMLResourceParseException(RESOURCE_NAME, e);
        }
    }

    private void setStyleMappings() {
        styleMappings.put("display", TeXConstants.STYLE_DISPLAY / 2);
        styleMappings.put("text", TeXConstants.STYLE_TEXT / 2);
        styleMappings.put("script", TeXConstants.STYLE_SCRIPT / 2);
        styleMappings.put("script_script", TeXConstants.STYLE_SCRIPT_SCRIPT / 2); // autoboxing
    }

    private void parseGlueTypes() throws ResourceParseException {
        List<Glue> glueTypesList = new ArrayList<Glue> ();
        Element types = (Element)root.getElementsByTagName("GlueTypes").item(0);
        int defaultIndex = -1;
        int index = 0;
        if (types != null) { // element present
            NodeList list = types.getElementsByTagName("GlueType");
            for (int i = 0; i < list.getLength(); i++) {
                Element type = (Element)list.item(i);
                // retrieve required attribute value, throw exception if not set
                String name = getAttrValueAndCheckIfNotNull("name", type);
                Glue glue = createGlue(type, name);
                if (name.equalsIgnoreCase("default")) // default must have value
                    defaultIndex = index;
                glueTypesList.add(glue);
                index ++;
            }
        }
        if (defaultIndex < 0) {
            // create a default glue object if missing
            defaultIndex = index;
            glueTypesList.add(new Glue(0,0,0,"default"));
        }

        glueTypes = glueTypesList.toArray(new Glue[glueTypesList.size()]);

        // make sure default glue is at the front
        if (defaultIndex > 0) {
            Glue tmp = glueTypes[defaultIndex];
            glueTypes[defaultIndex] = glueTypes[0];
            glueTypes[0] = tmp;
        }

        // make reverse map
        for (int i = 0; i < glueTypes.length; i++) {
            glueTypeMappings.put(glueTypes[i].getName(), i);
        }
    }

    private Glue createGlue(Element type, String name) throws ResourceParseException {
        final String[] names = { "space", "stretch", "shrink" };
        float[] values = new float[names.length];
        for (int i = 0; i < names.length; i++) {
            double val = 0; // default value if attribute not present
            String attrVal = null;
            try {
                attrVal = type.getAttribute(names[i]);
                if (!attrVal.equals("")) // attribute present
                    val = Double.parseDouble(attrVal);
            } catch (NumberFormatException e) {
                throw new XMLResourceParseException(RESOURCE_NAME, "GlueType",
                                                    names[i], "has an invalid real value '" + attrVal + "'!");
            }
            values[i] = (float) val;
        }
        return new Glue(values[0], values[1], values[2], name);
    }

    private void setTypeMappings() {
        typeMappings.put("ord",   TeXConstants.TYPE_ORDINARY);
        typeMappings.put("op",    TeXConstants.TYPE_BIG_OPERATOR);
        typeMappings.put("bin",   TeXConstants.TYPE_BINARY_OPERATOR);
        typeMappings.put("rel",   TeXConstants.TYPE_RELATION);
        typeMappings.put("open",  TeXConstants.TYPE_OPENING);
        typeMappings.put("close", TeXConstants.TYPE_CLOSING);
        typeMappings.put("punct", TeXConstants.TYPE_PUNCTUATION);
        typeMappings.put("inner", TeXConstants.TYPE_INNER); // autoboxing
    }

    public Glue[] getGlueTypes() {
        return glueTypes;
    }

    public int[][][] createGlueTable() throws ResourceParseException {
        int size = typeMappings.size();
        int[][][] table = new int[size][size][styleMappings.size()];
        Element glueTable = (Element)root.getElementsByTagName("GlueTable").item(0);
        if (glueTable != null) { // element present
            // iterate all the "Glue"-elements
            NodeList list = glueTable.getElementsByTagName("Glue");
            for (int i = 0; i < list.getLength(); i++) {
                Element glue = (Element)list.item(i);
                // retrieve required attribute values and throw exception if they're not set
                String left = getAttrValueAndCheckIfNotNull("lefttype", glue);
                String right = getAttrValueAndCheckIfNotNull("righttype", glue);
                String type = getAttrValueAndCheckIfNotNull("gluetype", glue);
                // iterate all the "Style"-elements
                NodeList listG = glue.getElementsByTagName("Style");
                for (int j = 0; j < listG.getLength(); j++) {
                    Element style = (Element)listG.item(j);
                    String styleName = getAttrValueAndCheckIfNotNull("name", style);
                    // retrieve mappings
                    Object l = typeMappings.get(left);
                    Object r = typeMappings.get(right);
                    Object st = styleMappings.get(styleName);
                    Object val = glueTypeMappings.get(type);
                    // throw exception if unknown value set
                    checkMapping(l, "Glue", "lefttype", left);
                    checkMapping(r, "Glue", "righttype", right);
                    checkMapping(val, "Glue", "gluetype", type);
                    checkMapping(st, "Style", "name", styleName);
                    // put value in table
                    table[((Integer) l).intValue()][((Integer) r).intValue()][((Integer) st).intValue()] = ((Integer) val).intValue();
                }
            }
        }
        return table;
    }

    private static void checkMapping(Object val, String elementName,
                                     String attrName, String attrValue) throws ResourceParseException {
        if (val == null)
            throw new XMLResourceParseException(RESOURCE_NAME, elementName,
                                                attrName, "has an unknown value '" + attrValue + "'!");
    }

    private static String getAttrValueAndCheckIfNotNull(String attrName,
            Element element) throws ResourceParseException {
        String attrValue = element.getAttribute(attrName);
        if (attrValue.equals(""))
            throw new XMLResourceParseException(RESOURCE_NAME, element.getTagName(),
                                                attrName, null);
        return attrValue;
    }
}
