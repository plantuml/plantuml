/* 	This file is taken from
	https://github.com/andreas1327250/argon2-java

	Original Author: Andreas Gadermaier <up.gadermaier@gmail.com>
 */
package net.sourceforge.plantuml.argon2;

import static java.nio.charset.StandardCharsets.UTF_8;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.LANES_DEF;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.LOG_M_COST_DEF;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.OUTLEN_DEF;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.TYPE_DEF;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.T_COST_DEF;
import static net.sourceforge.plantuml.argon2.Constants.Defaults.VERSION_DEF;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import net.sourceforge.plantuml.argon2.algorithm.FillMemory;
import net.sourceforge.plantuml.argon2.algorithm.Finalize;
import net.sourceforge.plantuml.argon2.algorithm.Initialize;
import net.sourceforge.plantuml.argon2.model.Argon2Type;
import net.sourceforge.plantuml.argon2.model.Instance;

public class Argon2 {

	private byte[] output;
	private int outputLength; // -l N
	private double duration;

	private byte[] password;
	private byte[] salt;
	private byte[] secret;
	private byte[] additional;

	private int iterations; // -t N
	private int memory; // -m N
	private int lanes; // -p N

	private int version; // -v (10/13)
	private Argon2Type type;

	private boolean clearMemory = true;

	private boolean encodedOnly = false;
	private boolean rawOnly = false;

	Argon2() {
		this.lanes = LANES_DEF;
		this.outputLength = OUTLEN_DEF;
		this.memory = 1 << LOG_M_COST_DEF;
		this.iterations = T_COST_DEF;
		this.version = VERSION_DEF;
		this.type = TYPE_DEF;
	}

	private static byte[] toByteArray(char[] chars, Charset charset) {
		assert chars != null;

		CharBuffer charBuffer = CharBuffer.wrap(chars);
		ByteBuffer byteBuffer = charset.encode(charBuffer);
		byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
		Arrays.fill(byteBuffer.array(), (byte) 0);
		return bytes;
	}

	public void hashNow() {
		try {
			Validation.validateInput(this);

			long start = System.nanoTime();

			Instance instance = new Instance(this);

			Initialize.initialize(instance, this);
			FillMemory.fillMemoryBlocks(instance);
			Finalize.finalize(instance, this);

			duration = (System.nanoTime() - start) / 1000000000.0;
		} finally {
			clear();
		}
	}

	public void clear() {
		if (password != null)
			Arrays.fill(password, 0, password.length - 1, (byte) 0);

		if (salt != null)
			Arrays.fill(salt, 0, salt.length - 1, (byte) 0);

		if (secret != null)
			Arrays.fill(secret, 0, secret.length - 1, (byte) 0);

		if (additional != null)
			Arrays.fill(additional, 0, additional.length - 1, (byte) 0);
	}

	void printSummary() {
		if (encodedOnly)
			System.out.println(getEncoded());
		else if (rawOnly)
			System.out.println(getOutputString());
		else {
			System.out.println("Type:\t\t" + type);
			System.out.println("Iterations:\t" + iterations);
			System.out.println("Memory:\t\t" + memory + " KiB");
			System.out.println("Parallelism:\t" + lanes);
			System.out.println("Hash:\t\t" + getOutputString());
			System.out.println("Encoded:\t " + getEncoded());
			System.out.println(duration + " seconds");
		}
	}

	public Argon2 setMemoryInKiB(int memory) {
		this.memory = memory;
		return this;
	}

	public Argon2 setParallelism(int parallelism) {
		this.lanes = parallelism;
		return this;
	}

	public Argon2 setPassword(char[] password) {
		return setPassword(toByteArray(password, UTF_8));
	}

	public Argon2 setSalt(String salt) {
		return setSalt(salt.getBytes(UTF_8));
	}

	public byte[] getOutput() {
		return output;
	}

	public void setOutput(byte[] finalResult) {
		this.output = finalResult;
	}

	public String getOutputString() {
		return Util.bytesToHexString(output);
	}

	public int getOutputLength() {
		return outputLength;
	}

	public Argon2 setOutputLength(int outputLength) {
		this.outputLength = outputLength;
		return this;
	}

	public byte[] getPassword() {
		return password;
	}

	public Argon2 setPassword(byte[] password) {
		this.password = password;
		return this;
	}

	public int getPasswordLength() {
		return password.length;
	}

	public byte[] getSalt() {
		return salt;
	}

	public Argon2 setSalt(byte[] salt) {
		this.salt = salt;
		return this;
	}

	public int getSaltLength() {
		return salt.length;
	}

	public byte[] getSecret() {
		return secret;
	}

	public Argon2 setSecret(byte[] secret) {
		this.secret = secret;
		return this;
	}

	public int getSecretLength() {
		return secret != null ? secret.length : 0;
	}

	public byte[] getAdditional() {
		return additional;
	}

	public Argon2 setAdditional(byte[] additional) {
		this.additional = additional;
		return this;
	}

	public int getAdditionalLength() {
		return additional != null ? additional.length : 0;
	}

	public int getIterations() {
		return iterations;
	}

	public Argon2 setIterations(int iterations) {
		this.iterations = iterations;
		return this;
	}

	public int getMemory() {
		return memory;
	}

	public Argon2 setMemory(int memory) {
		this.memory = 1 << memory;
		return this;
	}

	public int getLanes() {
		return lanes;
	}

	public int getVersion() {
		return version;
	}

	public Argon2 setVersion(int version) {
		this.version = version;
		return this;
	}

	public Argon2Type getType() {
		return type;
	}

	public Argon2 setType(Argon2Type type) {
		this.type = type;
		return this;
	}

	public boolean isClearMemory() {
		return clearMemory;
	}

	public void setClearMemory(boolean clearMemory) {
		this.clearMemory = clearMemory;
	}

	public Charset getCharset() {
		return UTF_8;
	}

	public void setEncodedOnly(boolean encodedOnly) {
		this.encodedOnly = encodedOnly;
	}

	public void setRawOnly(boolean rawOnly) {
		this.rawOnly = rawOnly;
	}

	public String getEncoded() {
		return ""; // TODO
	}
}
