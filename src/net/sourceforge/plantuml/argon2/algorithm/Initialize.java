/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2.algorithm;

import static net.sourceforge.plantuml.argon2.Constants.ARGON2_BLOCK_SIZE;
import static net.sourceforge.plantuml.argon2.Constants.ARGON2_PREHASH_DIGEST_LENGTH;
import static net.sourceforge.plantuml.argon2.Constants.ARGON2_PREHASH_SEED_LENGTH;

import net.sourceforge.plantuml.argon2.Argon2;
import net.sourceforge.plantuml.argon2.Util;
import net.sourceforge.plantuml.argon2.model.Instance;

public class Initialize {


    public static void initialize(Instance instance, Argon2 argon2) {
        byte[] initialHash = Functions.initialHash(
                Util.intToLittleEndianBytes(argon2.getLanes()),
                Util.intToLittleEndianBytes(argon2.getOutputLength()),
                Util.intToLittleEndianBytes(argon2.getMemory()),
                Util.intToLittleEndianBytes(argon2.getIterations()),
                Util.intToLittleEndianBytes(argon2.getVersion()),
                Util.intToLittleEndianBytes(argon2.getType().ordinal()),
                Util.intToLittleEndianBytes(argon2.getPasswordLength()),
                argon2.getPassword(),
                Util.intToLittleEndianBytes(argon2.getSaltLength()),
                argon2.getSalt(),
                Util.intToLittleEndianBytes(argon2.getSecretLength()),
                argon2.getSecret(),
                Util.intToLittleEndianBytes(argon2.getAdditionalLength()),
                argon2.getAdditional()
        );
        fillFirstBlocks(instance, initialHash);
    }

    /**
     * (H0 || 0 || i) 72 byte -> 1024 byte
     * (H0 || 1 || i) 72 byte -> 1024 byte
     */
    private static void fillFirstBlocks(Instance instance, byte[] initialHash) {

        final byte[] zeroBytes = {0, 0, 0, 0};
        final byte[] oneBytes = {1, 0, 0, 0};

        byte[] initialHashWithZeros = getInitialHashLong(initialHash, zeroBytes);
        byte[] initialHashWithOnes = getInitialHashLong(initialHash, oneBytes);

        for (int i = 0; i < instance.getLanes(); i++) {

            byte[] iBytes = Util.intToLittleEndianBytes(i);

            System.arraycopy(iBytes, 0, initialHashWithZeros, ARGON2_PREHASH_DIGEST_LENGTH + 4, 4);
            System.arraycopy(iBytes, 0, initialHashWithOnes, ARGON2_PREHASH_DIGEST_LENGTH + 4, 4);

            byte[] blockhashBytes = Functions.blake2bLong(initialHashWithZeros, ARGON2_BLOCK_SIZE);
            instance.memory[i * instance.getLaneLength() + 0].fromBytes(blockhashBytes);

            blockhashBytes = Functions.blake2bLong(initialHashWithOnes, ARGON2_BLOCK_SIZE);
            instance.memory[i * instance.getLaneLength() + 1].fromBytes(blockhashBytes);
        }
    }

    private static byte[] getInitialHashLong(byte[] initialHash, byte[] appendix) {
        byte[] initialHashLong = new byte[ARGON2_PREHASH_SEED_LENGTH];

        System.arraycopy(initialHash, 0, initialHashLong, 0, ARGON2_PREHASH_DIGEST_LENGTH);
        System.arraycopy(appendix, 0, initialHashLong, ARGON2_PREHASH_DIGEST_LENGTH, 4);

        return initialHashLong;
    }

}
