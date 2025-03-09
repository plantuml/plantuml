package net.sourceforge.plantuml.picoweb;

import net.sourceforge.plantuml.json.Json;
import net.sourceforge.plantuml.json.JsonArray;
import net.sourceforge.plantuml.json.JsonObject;

/**
 * POJO of the json sent to "POST /render"
 */
public class RenderRequest {

	private final String[] options;

	private final String source;

	public RenderRequest(String[] options, String source) {
		this.options = options;
		this.source = source;
	}

	public String[] getOptions() {
		return options;
	}

	public String getSource() {
		return source;
	}

	public static RenderRequest fromJson(String json) {
		final JsonObject parsed = Json.parse(json).asObject();
		final String[] options;

		if (parsed.contains("options")) {
			final JsonArray jsonArray = parsed.get("options").asArray();
			options = new String[jsonArray.size()];
			for (int i = 0; i < jsonArray.size(); i++) {
				options[i] = jsonArray.get(i).asString();
			}
		} else {
			options = new String[0];
		}

		return new RenderRequest(options, parsed.get("source").asString());
	}
}
