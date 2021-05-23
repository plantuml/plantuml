/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2;

import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_AD_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_ITERATIONS;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_PARALLELISM;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_PWD_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_SALT_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MAX_SECRET_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MIN_ITERATIONS;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MIN_PARALLELISM;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MIN_PWD_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Constraints.MIN_SALT_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.Messages.ADDITIONAL_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.M_MIN_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.PWD_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.PWD_MIN_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.P_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.P_MIN_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.SALT_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.SALT_MIN_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.SECRET_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.T_MAX_MSG;
import static net.sourceforge.plantuml.argon2.Constants.Messages.T_MIN_MSG;

import net.sourceforge.plantuml.argon2.exception.Argon2InvalidParameterException;

class Validation {

    static void validateInput(Argon2 argon2){
        String message = null;

        if (argon2.getLanes() < MIN_PARALLELISM)
            message = P_MIN_MSG;
        else if (argon2.getLanes() > MAX_PARALLELISM)
            message = P_MAX_MSG;
        else if(argon2.getMemory() < 2 * argon2.getLanes())
            message = M_MIN_MSG;
        else if(argon2.getIterations() < MIN_ITERATIONS)
            message = T_MIN_MSG;
        else if(argon2.getIterations() > MAX_ITERATIONS)
            message = T_MAX_MSG;
        else if(argon2.getPasswordLength() < MIN_PWD_LENGTH)
            message = PWD_MIN_MSG;
        else if(argon2.getPasswordLength() > MAX_PWD_LENGTH)
            message = PWD_MAX_MSG;
        else if(argon2.getSaltLength() < MIN_SALT_LENGTH)
            message = SALT_MIN_MSG;
        else if(argon2.getSaltLength() > MAX_SALT_LENGTH)
            message = SALT_MAX_MSG;
        else if(argon2.getSecretLength() > MAX_SECRET_LENGTH)
            message = SECRET_MAX_MSG;
        else if(argon2.getAdditionalLength() > MAX_AD_LENGTH)
            message = ADDITIONAL_MAX_MSG;

            if(message != null)
                throw new Argon2InvalidParameterException(message);
    }
}
