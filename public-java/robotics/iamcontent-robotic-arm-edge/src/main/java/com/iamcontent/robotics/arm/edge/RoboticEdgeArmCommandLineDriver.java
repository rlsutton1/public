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
package com.iamcontent.robotics.arm.edge;

import static com.iamcontent.io.util.BufferedReaderIterator.bufferedReaderIterator;
import static com.iamcontent.io.util.Readers.bufferedReader;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.iamcontent.robotics.arm.edge.action.Action;
import com.iamcontent.robotics.arm.edge.action.Actor;
import com.iamcontent.robotics.arm.edge.action.BaseAction;
import com.iamcontent.robotics.arm.edge.action.ElbowAction;
import com.iamcontent.robotics.arm.edge.action.GeneralAction;
import com.iamcontent.robotics.arm.edge.action.GripperAction;
import com.iamcontent.robotics.arm.edge.action.LedAction;
import com.iamcontent.robotics.arm.edge.action.ShoulderAction;
import com.iamcontent.robotics.arm.edge.action.WristAction;

/**
 * An example driver for the {@link RoboticEdgeArm}. Useful for manual testing.
 * @author Greg Elderfield
 */
public class RoboticEdgeArmCommandLineDriver implements Runnable {
	final RoboticEdgeArm arm = new RoboticEdgeArm();
	
	public static void main(String[] args) {
		final RoboticEdgeArmCommandLineDriver driver = new RoboticEdgeArmCommandLineDriver();
		driver.run();
	}
	
	@Override
	public void run() {
		for (Action a : actions()) {
			execute(a);
			if (a==DriverAction.QUIT)
				break;
		}
		hibernateDevice();
	}

	private void hibernateDevice() {
		execute(GeneralAction.STOP_ALL_MOVEMENT);
		execute(LedAction.OFF);
	}

	private Iterable<Action> actions() {
		return Iterables.transform(commandIterator(), parseStringIntoAction());
	}

	private Iterable<String> commandIterator() {
		return bufferedReaderIterator(bufferedReader(System.in));
	}

	private Function<String, Action> parseStringIntoAction() {
		return new ParsingFunction();
	}

	private void execute(Action action) {
		action.applyTo(arm);
	}

	/**
	 * A function to parse a String command into an Action.
	 */
	private static class ParsingFunction implements Function<String, Action> {

		@Override
		public Action apply(String command) {
			if (command==null)
				return DriverAction.QUIT;
			switch (tidied(command)) {
			
			case "base left":
			case "bl":
				return BaseAction.LEFT;
			case "base right":
			case "br":
				return BaseAction.RIGHT;
			case "base stop":
			case "bs":
				return BaseAction.STOP;
				
			case "shoulder forward":
			case "shoulder forwards":
			case "sf":
				return ShoulderAction.FORWARDS;
			case "shoulder backward":
			case "shoulder backwards":
			case "sb":
				return ShoulderAction.BACKWARDS;
			case "shoulder stop":
			case "ss":
				return ShoulderAction.STOP;
				
			case "elbow extend":
			case "ee":
				return ElbowAction.EXTEND;
			case "elbow flex":
			case "ef":
				return ElbowAction.FLEX;
			case "elbow stop":
			case "es":
				return ElbowAction.STOP;
				
			case "wrist extend":
			case "we":
				return WristAction.EXTEND;
			case "wrist flex":
			case "wf":
				return WristAction.FLEX;
			case "wrist stop":
			case "ws":
				return WristAction.STOP;
				
				
			case "grip open":
			case "gripper open":
			case "go":
				return GripperAction.OPEN;
			case "grip close":
			case "gripper close":
			case "gc":
				return GripperAction.CLOSE;
			case "grip stop":
			case "gripper stop":
			case "gs":
				return GripperAction.STOP;
				
			case "led on":
			case "light on":
			case "l+":
				return LedAction.ON;
			case "led off":
			case "light off":
			case "l-":
				return LedAction.OFF;

			case "stop":
			case "":
				return GeneralAction.STOP_ALL_MOVEMENT;
			case "q":
			case "quit":
			case "exit":
			case "bye":
				return DriverAction.QUIT;
			default:
				return DriverAction.UNKNOWN_COMMAND;
			}
		}

		private String tidied(String command) {
			return command.trim().toLowerCase();
		}
	}

	private enum DriverAction implements Action {
		UNKNOWN_COMMAND {
			@Override
			public void applyTo(Actor actor) {
				System.out.println("Unknown command, please try again.");
			}
		},
		QUIT {
			@Override
			public void applyTo(Actor actor) {
				System.out.println("Bye");
			}
		}
	}
}