/**
  IAmContent Public Libraries.
  Copyright (C) 2015 Greg Elderfield
  @author Greg Elderfield, iamcontent@jarchitect.co.uk
 
  This program is free software; you can redistribute it and/or modify it under the terms of the
  GNU General Public License as published by the Free Software Foundation; either version 2 of 
  the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this program;
  if not, write to the Free Software Foundation, Inc., 
  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.iamcontent.io.license;

import static com.google.common.io.CharStreams.copy;
import static com.iamcontent.io.util.Readers.inputStreamReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Writes out the license.
 * @author Greg Elderfield
 */
public class LicenseWriter {
	private static final Logger LOGGER = Logger.getLogger(LicenseWriter.class.getName());
	
	private static final String FOLDER = "/license/";
	private static final String INSTRUCTIONS = FOLDER + "LICENSE_INSTRUCTIONS.TXT";
	private static final String TERMS_AND_CONDITIONS = FOLDER + "LICENSE_TERMS_AND_CONDITIONS.TXT";
	private static final String WARRANTY = FOLDER + "LICENSE_WARRANTY.TXT";
	
	private static final String PRODUCT_NAME = "IAmContent Public Software";
	private static final String YEARS = "2015";
	private static final String COPYRIGHT_HOLDER = "G. Elderfield.";
	
	static final String[] MINIMAL_TERMS = {
		"This software comes with ABSOLUTELY NO WARRANTY.",
		"This is free software, and you are welcome to redistribute it",
		"under the terms of the GNU GENERAL PUBLIC LICENSE Version 2."
	};
	
	final Appendable out;
	final String productName;
	final String years;
	final String copyrightHolders;
	
	public LicenseWriter() {
		this(System.out, PRODUCT_NAME, YEARS, COPYRIGHT_HOLDER);
	}

	public LicenseWriter(Appendable out, String productName, String years, String copyrightHolders) {
		this.out = out;
		this.productName = productName;
		this.years = years;
		this.copyrightHolders = copyrightHolders;
	}

	public void printInstructions() {
		printFile(INSTRUCTIONS);
	}

	public void printTermsAndConditions() {
		printFile(TERMS_AND_CONDITIONS);
	}
	
	public void printWarranty() {
		printFile(WARRANTY);
	}
	
	private void printMinimalTerms() {
		for (String s : MINIMAL_TERMS)
			append(s);
	}

	void printFile(String file) {
		printHeader();
		try(final InputStream in = getStream(file)) {
			printInputStream(in);
		} catch (IOException e) {
			printMinimalTerms();
		};
	}

	private void printHeader() {
		append(String.format("%s, Copyright (C) %s %s.", productName, years, copyrightHolders));
	}

	private void printInputStream(InputStream in) throws IOException {
		copy(inputStreamReader(in), out);
	}

	private void append(String s) {
		if (s!=null)
			try {
				out.append(s + "\n");
			} catch (IOException e) {
				LOGGER.severe(s + "\n");
			}
	}
	
	private InputStream getStream(String file) throws FileNotFoundException {
		final InputStream result = getClass().getResourceAsStream(file);
		if (result==null)
			throw new FileNotFoundException();
		return result;
	}
}