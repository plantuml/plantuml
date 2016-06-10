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


/**
 * 
 * @author Efstathios Sideris
 */
public class ConversionOptions {
	
//	public ProcessingOptions processingOptions =
//		new ProcessingOptions();
	public RenderingOptions renderingOptions =
		new RenderingOptions();
		
	public void setDebug(boolean value){
		// processingOptions.setPrintDebugOutput(value);
		renderingOptions.setRenderDebugLines(value);
	}
	
	public ConversionOptions(){}

	public void setDropShadows(boolean dropShadows) {
		renderingOptions.setDropShadows(dropShadows);
	}
	
//	public ConversionOptions(CommandLine cmdLine) throws UnsupportedEncodingException{
//		
//		processingOptions.setVerbose(cmdLine.hasOption("verbose"));
//		renderingOptions.setDropShadows(!cmdLine.hasOption("no-shadows"));
//		this.setDebug(cmdLine.hasOption("debug"));
//		processingOptions.setOverwriteFiles(cmdLine.hasOption("overwrite"));
//		
//		if(cmdLine.hasOption("scale")){
//			Float scale = Float.parseFloat(cmdLine.getOptionValue("scale"));
//			renderingOptions.setScale(scale.floatValue());
//		}
//		
//		processingOptions.setAllCornersAreRound(cmdLine.hasOption("round-corners"));
//		processingOptions.setPerformSeparationOfCommonEdges(!cmdLine.hasOption("no-separation"));
//		renderingOptions.setAntialias(!cmdLine.hasOption("no-antialias"));
//
//
//
//		if(cmdLine.hasOption("tabs")){
//			Integer tabSize = Integer.parseInt(cmdLine.getOptionValue("tabs"));
//			int tabSizeValue = tabSize.intValue();
//			if(tabSizeValue < 0) tabSizeValue = 0;
//			processingOptions.setTabSize(tabSizeValue);
//		}
//
//		String encoding = (String) cmdLine.getOptionValue("encoding");
//		if(encoding != null){
//			new String(new byte[2], encoding);
//			processingOptions.setCharacterEncoding(encoding);
//		}
//		
//		ConfigurationParser configParser = new ConfigurationParser();
//		try {
//			for (Option curOption : cmdLine.getOptions()) {
//				if(curOption.getLongOpt().equals("config")) {
//					String configFilename = curOption.getValue();
//					System.out.println("Parsing configuration file "+configFilename);
//					File file = new File(configFilename);
//					if(file.exists()){
//						configParser.parseFile(file);
//						HashMap<String, CustomShapeDefinition> shapes = configParser.getShapeDefinitionsHash();
//						processingOptions.putAllInCustomShapes(shapes);
//					} else {
//						System.err.println("File "+file+" does not exist, skipping");
//					}
//				}
//			}
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}


// may be supported at a later date:
//String exportFormat = (String) cmdLine.getOptionValue("format");
//if(exportFormat != null){
//	exportFormat = exportFormat.toLowerCase();
//	if(exportFormat == "jpeg" || exportFormat == "jpg"){
//		processingOptions.setExportFormat(ProcessingOptions.FORMAT_JPEG);
//	} else if(exportFormat == "png"){
//		processingOptions.setExportFormat(ProcessingOptions.FORMAT_PNG);
//	} else if(exportFormat == "gif"){
//		processingOptions.setExportFormat(ProcessingOptions.FORMAT_GIF);
//	}
//}
//
//String colorCodeMode = (String) cmdLine.getOptionValue("color-codes");
//if(colorCodeMode != null){
//	if(colorCodeMode.equals("use"))
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.USE_COLOR_CODES);
//	else if(colorCodeMode.equals("ignore"))
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.IGNORE_COLOR_CODES);
//	else if(colorCodeMode.equals("render"))
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.RENDER_COLOR_CODES);
//}
//
//String tagsMode = (String) cmdLine.getOptionValue("tags");
//if(tagsMode != null){
//	if(tagsMode.equals("use"))
//		processingOptions.setTagProcessingMode(ProcessingOptions.USE_TAGS);
//	else if(tagsMode.equals("ignore"))
//		processingOptions.setTagProcessingMode(ProcessingOptions.IGNORE_TAGS);
//	else if(tagsMode.equals("render"))
//		processingOptions.setTagProcessingMode(ProcessingOptions.RENDER_TAGS);
//}
//
//
//String markupMode = (String) cmdLine.getOptionValue("markup");
//if(markupMode != null){
//	if(markupMode.equals("use")){
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.USE_COLOR_CODES);
//		processingOptions.setTagProcessingMode(ProcessingOptions.USE_TAGS);
//	} else if(markupMode.equals("ignore")){
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.IGNORE_COLOR_CODES);
//		processingOptions.setTagProcessingMode(ProcessingOptions.IGNORE_TAGS);
//	} else if(markupMode.equals("render")){
//		processingOptions.setColorCodesProcessingMode(ProcessingOptions.RENDER_COLOR_CODES);
//		processingOptions.setTagProcessingMode(ProcessingOptions.RENDER_TAGS);
//	}
//}