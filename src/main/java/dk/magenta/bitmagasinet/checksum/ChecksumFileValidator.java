package dk.magenta.bitmagasinet.checksum;

import org.apache.commons.lang.StringUtils;

public class ChecksumFileValidator {

	static boolean isLinevalid(String line) {

		String[] values = line.split("\\t");
		if (values.length != 3) return false;
		
		String filename = values[0];
		String salt = values[1];
		String checksum = values[2];
		
		if (StringUtils.isBlank(filename)) return false;
		if (StringUtils.isBlank(salt) || salt.length() % 2 != 0) return false;
		if (!salt.matches("[0-9a-f]*")) return false;

		if (!checksum.matches("[0-9a-f]{32}")) return false;
		
		return true;
	}
}
