package net.sourceforge.plantuml.picoweb;

import java.io.IOException;

public class BadRequest400 extends IOException {

	public BadRequest400(String message) {
		super(message);
	}

	public BadRequest400(String message, Throwable cause) {
		super(message, cause);
	}
}
