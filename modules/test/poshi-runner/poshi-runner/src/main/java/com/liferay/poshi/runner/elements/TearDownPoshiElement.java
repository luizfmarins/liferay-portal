/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.poshi.runner.elements;

import org.dom4j.Element;

/**
 * @author Kenji Heigel
 */
public class TearDownPoshiElement extends CommandPoshiElement {

	@Override
	public PoshiElement clone(Element element) {
		if (isElementType(_ELEMENT_NAME, element)) {
			return new TearDownPoshiElement(element);
		}

		return null;
	}

	@Override
	public PoshiElement clone(
		PoshiElement parentPoshiElement, String readableSyntax) {

		if (_isElementType(readableSyntax)) {
			return new TearDownPoshiElement(readableSyntax);
		}

		return null;
	}

	protected TearDownPoshiElement() {
	}

	protected TearDownPoshiElement(Element element) {
		super(_ELEMENT_NAME, element);
	}

	protected TearDownPoshiElement(String readableSyntax) {
		super(_ELEMENT_NAME, readableSyntax);
	}

	@Override
	protected String getReadableCommandTitle() {
		return "tearDown";
	}

	private static boolean _isElementType(String readableSyntax) {
		readableSyntax = readableSyntax.trim();

		if (!isBalancedReadableSyntax(readableSyntax)) {
			return false;
		}

		if (!readableSyntax.endsWith("}")) {
			return false;
		}

		for (String line : readableSyntax.split("\n")) {
			line = line.trim();

			if (line.startsWith("@")) {
				continue;
			}

			if (!line.equals("tearDown {")) {
				return false;
			}

			break;
		}

		return true;
	}

	private static final String _ELEMENT_NAME = "tear-down";

}