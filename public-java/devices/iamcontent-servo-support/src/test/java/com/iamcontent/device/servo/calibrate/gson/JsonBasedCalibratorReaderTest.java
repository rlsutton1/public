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
package com.iamcontent.device.servo.calibrate.gson;

import static com.iamcontent.device.servo.calibrate.gson.DefaultingServoSourceCalibratorDeserializerTest.checkCalibrator;

import org.junit.Test;

import com.iamcontent.device.servo.calibrate.ServoSourceCalibrator;
import com.iamcontent.io.IORuntimeException;

public class JsonBasedCalibratorReaderTest {

	@Test
	public void testDefaultCalibrator() {
		final ServoSourceCalibrator actual = JsonBasedCalibratorReader.calibrator("default");
		for (int c=0; c<6; c++)
			checkDefaultCalibrator(actual, c);
		checkDefaultCalibrator(actual, 321);
	}

	@Test
	public void testPerServoExampleCalibrator() {
		final ServoSourceCalibrator actual = JsonBasedCalibratorReader.calibrator("per-servo-example");
		checkCalibrator(actual, 0, 0.0, 1.0, 1000.0, 2000.0);
		checkDefaultCalibrator(actual, 0);
		checkDefaultCalibrator(actual, 1);
		checkCalibrator(actual, 2, 0.0, 1.0, 800, 1200);
		checkDefaultCalibrator(actual, 3);
		checkCalibrator(actual, 4, 0.5, -0.5, 900, 1100);
		checkDefaultCalibrator(actual, 5);
		checkDefaultCalibrator(actual, 321);
	}

	private void checkDefaultCalibrator(ServoSourceCalibrator actual, int channel) {
		checkCalibrator(actual, channel, 0.0, 1.0, 1000.0, 2000.0);
	}

	@Test(expected=IORuntimeException.class)
	public void testNonExistentCalibrator() {
		JsonBasedCalibratorReader.calibrator("non-existent");
	}
}