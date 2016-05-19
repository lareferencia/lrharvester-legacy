package org.lareferencia.backend.util;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class InvalidXMLCharFilterReader extends FilterReader {

	public InvalidXMLCharFilterReader(Reader in) {
		super(in);
	}

	public int read() throws IOException {
		char[] buf = new char[1];
		int result = read(buf, 0, 1);
		if (result == -1)
			return -1;
		else
			return (int) buf[0];
	}

	public int read(char[] buf, int from, int len) throws IOException {
		int count = 0;
		while (count == 0) {
			count = in.read(buf, from, len);
			if (count == -1)
				return -1;

			int last = from;
			for (int i = from; i < from + count; i++) {
				if (!isBadXMLChar(buf[i])) {
					buf[last++] = buf[i];
				} else {
					System.out.println("Char XML inválido eliminado: "
							+ String.format("%04x", (int) buf[i]));
				}
			}

			count = last - from;
		}
		return count;
	}

	private boolean isBadXMLChar(char c) {

		if ((c == 0x9) || (c == 0xA) || (c == 0xD)
				|| ((c >= 0x20) && (c <= 0xD7FF))
				|| ((c >= 0xE000) && (c <= 0xFFFD))
				|| ((c >= 0x10000) && (c <= 0x10FFFF))) {
			return false;
		}
		return true;
	}

}