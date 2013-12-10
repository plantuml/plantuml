package com.ctreber.acearth.plugins.markers;

import java.util.Iterator;
import java.util.List;

import com.ctreber.acearth.plugins.Plugin;

/**
 * <p>
 * Renders markers to the render target. a
 * <p>
 * &copy; 2002 Christian Treber, ct@ctreber.com
 * 
 * @author Christian Treber, ct@ctreber.com
 * 
 */
public class PluginMarkers extends Plugin {
	private List fMarkers;

	// private String fMarkerFileName = ACearth.getHomeDir() +
	// "markersDefault.txt";

	public PluginMarkers(List<Marker> markers) {
		// ACearth.indent("AC.earth Markers plug-in");
		//
		// ACearth.log("Reading markers");
		//
		// if(fMarkerFileName == null)
		// {
		// throw new RuntimeException("Marker file name not set");
		// }

		// try
		// {
		// fMarkers = Marker.loadMarkerFile(fMarkerFileName);
		// } catch(IOException e)
		// {
		// ACearth.logError("Marker file not found");
		// return;
		// }
		
		fMarkers = markers;

		// ACearth.outdent();
	}

	public boolean hasGUIP() {
		return false;
	}

	public void render() {
		if (!fActiveP) {
			return;
		}

		// fRenderTarget.setTextFont(fRenderTarget.getTextFont().deriveFont(9.0f));
		Iterator lIt = fMarkers.iterator();
		while (lIt.hasNext()) {
			Marker lMarker = (Marker) lIt.next();
			lMarker.render(fRenderTarget, fProjection);
		}
	}

	// public void setMarkerFileName(String pMarkerFileName)
	// {
	// fMarkerFileName = pMarkerFileName;
	// }

	public String toString() {
		return "AC.earth Markers plug-in";
	}
}
