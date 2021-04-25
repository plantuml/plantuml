/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package ext.plantuml.com.at.gadermaier.argon2.exception;

/* dislike checked exceptions */
class Argon2Exception extends RuntimeException {
    Argon2Exception(String message) {
        super(message);
    }
}
