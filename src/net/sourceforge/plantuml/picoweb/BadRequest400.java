package net.sourceforge.plantuml.picoweb;

import java.io.IOException;

public class BadRequest400 extends IOException {
    // ::remove folder when __HAXE__

	public BadRequest400(String message) {
		super(message);
	}

	public BadRequest400(String message, Throwable cause) {
		super(message, cause);
	}
}
