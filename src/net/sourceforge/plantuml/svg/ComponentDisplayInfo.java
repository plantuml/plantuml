package net.sourceforge.plantuml.svg;

public class ComponentDisplayInfo {

	private String displayName;
	private String uniqueName;;
	private String color;
	private String type;
	
	public ComponentDisplayInfo(String displayName, String uniqueName, String color, String type) {
		this.displayName =displayName;
		this.uniqueName = uniqueName;
		this.color = color;
		if (type.equals("class") || type.equals("enum") || type.equals("interface") || type.equals("annotation")) {
			this.type = "base";
		} else if (type.equals("method") || type.equals("constructor")) {
			this.type = "method";
		} else {
			this.type = "variable";
		}
	}

	public String uniqueName() {
		return uniqueName;
	}
	
	public String displayName() {
		return displayName;
	}
	
	public String type() {
		return type;
	}
	
	public String color() {
		return color;
	}
}
