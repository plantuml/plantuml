package net.sourceforge.plantuml.svg;

public class ComponentDisplayInfo {

    private String displayName;
    private String uniqueName;;
    private String color;
    private String type;

    public ComponentDisplayInfo(String displayName, String uniqueName, String color, String type) {
        this.displayName = displayName;
        this.uniqueName = uniqueName;
        this.color = color;
        if (type.equals("class") || type.equals("enum") || type.equals("interface") || type.equals("annotation")) {
            this.type = "base";
        } else if (type.equals("method") || type.equals("constructor")) {
            this.type = "method";
        } else if (type.equals("enum_constant") || type.equals("interface_constant") || type.equals("field_variable")
                || type.equals("local_variable") || type.equals("constructor_parameter")
                || type.equals("method_parameter")) {
            this.type = "variable";
        } else {
            throw new IllegalArgumentException("Did not recognize type: " + type);
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
