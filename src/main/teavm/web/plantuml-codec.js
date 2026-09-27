const PLANTUML_ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-_";

function codec() {
	const implementation = globalThis.fflate;
	if (implementation == null) {
		throw new Error("The bundled DEFLATE codec did not load");
	}
	return implementation;
}

function append3Bytes(byte1, byte2, byte3) {
	const char1 = byte1 >> 2;
	const char2 = ((byte1 & 0x03) << 4) | (byte2 >> 4);
	const char3 = ((byte2 & 0x0f) << 2) | (byte3 >> 6);
	const char4 = byte3 & 0x3f;
	return PLANTUML_ALPHABET[char1]
		+ PLANTUML_ALPHABET[char2]
		+ PLANTUML_ALPHABET[char3]
		+ PLANTUML_ALPHABET[char4];
}

function encode64(bytes) {
	let encoded = "";
	for (let index = 0; index < bytes.length; index += 3) {
		encoded += append3Bytes(
			bytes[index],
			bytes[index + 1] ?? 0,
			bytes[index + 2] ?? 0
		);
	}
	return encoded;
}

function decode6Bit(character) {
	const value = PLANTUML_ALPHABET.indexOf(character);
	if (value === -1) {
		throw new Error(`Invalid PlantUML URL character: ${character}`);
	}
	return value;
}

function decode64(encoded) {
	if (encoded.length % 4 !== 0) {
		throw new Error("Invalid PlantUML URL fragment length");
	}

	const bytes = new Uint8Array(encoded.length / 4 * 3);
	let outputIndex = 0;
	for (let index = 0; index < encoded.length; index += 4) {
		const char1 = decode6Bit(encoded[index]);
		const char2 = decode6Bit(encoded[index + 1]);
		const char3 = decode6Bit(encoded[index + 2]);
		const char4 = decode6Bit(encoded[index + 3]);
		bytes[outputIndex++] = (char1 << 2) | (char2 >> 4);
		bytes[outputIndex++] = ((char2 & 0x0f) << 4) | (char3 >> 2);
		bytes[outputIndex++] = ((char3 & 0x03) << 6) | char4;
	}
	return bytes;
}

/** Encode UTF-8 text using PlantUML's raw-DEFLATE and URL-safe alphabet. */
export function encodePlantUml(source) {
	const input = new TextEncoder().encode(source);
	const compressed = codec().deflateSync(input, {level: 9});
	return encode64(compressed);
}

/** Decode a standard PlantUML URL payload back to its UTF-8 source text. */
export function decodePlantUml(fragment) {
	const compressed = decode64(fragment);
	const decompressed = codec().inflateSync(compressed);
	return new TextDecoder("utf-8", {fatal: true}).decode(decompressed);
}
