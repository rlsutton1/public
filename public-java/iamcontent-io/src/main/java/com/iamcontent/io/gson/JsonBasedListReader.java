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
package com.iamcontent.io.gson;

import static com.iamcontent.core.LangUtils.listType;

import java.util.List;

import com.google.gson.Gson;

/**
 * Creates {@link List}s according to JSON file resources.
 * @author Greg Elderfield
 */
public class JsonBasedListReader<E> extends JsonResourceReader<List<E>> {

	public JsonBasedListReader(String name, Class<E> elementClass, Gson gson) {
		super(name, listType(elementClass), gson);
	}

	public static <E> List<E> list(String name, Class<E> elementClass) {
		return list(name, elementClass, new Gson());
	}

	public static <E> List<E> list(String name, Class<E> elementClass, Gson gson) {
		return newInstance(name, elementClass, gson).read();
	}

	private static <E> JsonBasedListReader<E> newInstance(String name, Class<E> elementClass, Gson gson) {
		return new JsonBasedListReader<E>(name, elementClass, gson);
	}
}
