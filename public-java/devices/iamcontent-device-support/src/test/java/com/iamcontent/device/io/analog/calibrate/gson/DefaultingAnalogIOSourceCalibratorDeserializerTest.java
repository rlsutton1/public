/**
  IAmContent Public Libraries.
  Copyright (C) 2015 Greg Elderfield
  @author Greg Elderfield, support@jarchitect.co.uk
 
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
package com.iamcontent.device.io.analog.calibrate.gson;

import static com.iamcontent.device.io.analog.calibrate.gson.DefaultingAnalogIOSourceCalibratorDeserializer.defaultingAnalogIOSourceCalibratorGsonBuilder;
import static com.iamcontent.device.io.analog.calibrate.gson.ProportionalAnalogIOCalibratorDeserializerTest.sourceRange;
import static com.iamcontent.device.io.analog.calibrate.gson.ProportionalAnalogIOCalibratorDeserializerTest.targetRange;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.iamcontent.core.math.DoubleRange;
import com.iamcontent.device.io.analog.calibrate.DefaultingAnalogIOSourceCalibrator;
import com.iamcontent.device.io.analog.calibrate.ProportionalAnalogIOCalibrator;
import com.iamcontent.device.io.analog.calibrate.AnalogIOSourceCalibrator;

public class DefaultingAnalogIOSourceCalibratorDeserializerTest {

	private static final String JSON_VALUES = "{"
			+ "'defaultCalibrator':"
			+ calibratorJson("1.1", "2.2")
			+ ","
			+ "'perChannelCalibrators':{"
			+ channelAndCalibratorJson(2, "3.3", "4.4")
			+ "}}";

	private static final String JSON_DEFAULT_VALUE_ONLY = "{"
			+ "'defaultCalibrator':"
			+ calibratorJson("1.1", "2.2")
			+ "}";

	private Gson gson;

	@Before
	public void setUp() throws Exception {
		gson = defaultingAnalogIOSourceCalibratorGsonBuilder(Integer.class, ProportionalAnalogIOCalibrator.class).create();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeserialize() {
		final AnalogIOSourceCalibrator<Integer> actual = gson.fromJson(JSON_VALUES, DefaultingAnalogIOSourceCalibrator.class);
		checkDefaultCalibrator(actual, 0);
		checkDefaultCalibrator(actual, 1);
		checkCalibrator(actual, 2, 0.0, 1.0, 3.3, 4.4);
		checkDefaultCalibrator(actual, 3);
		checkDefaultCalibrator(actual, 4);
		checkDefaultCalibrator(actual, 5);
		checkDefaultCalibrator(actual, 321);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testDeserialize_defaultValueOnly() {
		final AnalogIOSourceCalibrator<Integer> actual = gson.fromJson(JSON_DEFAULT_VALUE_ONLY, DefaultingAnalogIOSourceCalibrator.class);
		for (int c=0; c<6; c++)
			checkDefaultCalibrator(actual, c);
		checkDefaultCalibrator(actual, 321);
	}

	private static void checkDefaultCalibrator(AnalogIOSourceCalibrator<Integer> actual, int channel) {
		checkCalibrator(actual, channel, 0.0, 1.0, 1.1, 2.2);
	}

	public static <C> void checkCalibrator(AnalogIOSourceCalibrator<Integer> actual, int channel, double expectedFromLimit1, double expectedFromLimit2, double expectedToLimit1, double expectedToLimit2) {
		final DoubleRange source = sourceRange(actual.forChannel(channel).getValueConverter());
		final DoubleRange target = targetRange(actual.forChannel(channel).getValueConverter());
		final String message = "Channel " + channel;
		assertExactlyEquals(message, expectedFromLimit1, source.getLimit1());
		assertExactlyEquals(message, expectedFromLimit2, source.getLimit2());
		assertExactlyEquals(message, expectedToLimit1, target.getLimit1());
		assertExactlyEquals(message, expectedToLimit2, target.getLimit2());
	}

	private static String channelAndCalibratorJson(int channel, String limit1, String limit2) {
		return "'" + channel + "':" + calibratorJson(limit1, limit2);
	}

	public static String calibratorJson(String limit1, String limit2) {
		return "{" + converterJson("valueConverter", limit1, limit2) + "}";
	}
	
	public static String converterJson(String fieldName, String limit1, String limit2) {
		return fieldName +":{" + toRangeJson(limit1, limit2) + "}";
	}

	public static String toRangeJson(String limit1, String limit2) {
		return "'toRange':" + rangeJson(limit1, limit2);
	}

	public static String rangeJson(String limit1, String limit2) {
		return "{'limit1':" + limit1 + ",'limit2':" + limit2 + "}";
	}
	
	public static void assertExactlyEquals(String message, double expected, double actual) {
		assertEquals(message, expected, actual, 0.0);
	}
}