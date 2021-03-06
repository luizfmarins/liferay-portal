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

package com.liferay.portal.kernel.servlet;

import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.portlet.PortletRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Julio Camarero
 * @author Brian Wing Shun Chan
 */
public class MultiSessionMessages {

	public static void add(PortletRequest portletRequest, Class<?> clazz) {
		SessionMessages.add(portletRequest, clazz.getName());

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.add(httpServletRequest, clazz.getName());
	}

	public static void add(
		PortletRequest portletRequest, Class<?> clazz, Object value) {

		SessionMessages.add(portletRequest, clazz.getName(), value);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.add(httpServletRequest, clazz.getName(), value);
	}

	public static void add(PortletRequest portletRequest, String key) {
		SessionMessages.add(portletRequest, key);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.add(httpServletRequest, key);
	}

	public static void add(
		PortletRequest portletRequest, String key, Object value) {

		SessionMessages.add(portletRequest, key, value);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.add(httpServletRequest, key, value);
	}

	public static void clear(PortletRequest portletRequest) {
		SessionMessages.clear(portletRequest);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.clear(httpServletRequest);
	}

	public static boolean contains(
		PortletRequest portletRequest, Class<?> clazz) {

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		if (SessionMessages.contains(portletRequest, clazz.getName()) ||
			SessionMessages.contains(httpServletRequest, clazz.getName())) {

			return true;
		}

		return false;
	}

	public static boolean contains(PortletRequest portletRequest, String key) {
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		if (SessionMessages.contains(portletRequest, key) ||
			SessionMessages.contains(httpServletRequest, key)) {

			return true;
		}

		return false;
	}

	public static Object get(PortletRequest portletRequest, Class<?> clazz) {
		Object value = SessionMessages.get(portletRequest, clazz.getName());

		if (value != null) {
			return value;
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		return SessionMessages.get(httpServletRequest, clazz.getName());
	}

	public static Object get(PortletRequest portletRequest, String key) {
		Object value = SessionMessages.get(portletRequest, key);

		if (value != null) {
			return value;
		}

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		return SessionMessages.get(httpServletRequest, key);
	}

	public static boolean isEmpty(PortletRequest portletRequest) {
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		if (SessionMessages.isEmpty(portletRequest) &&
			SessionMessages.isEmpty(httpServletRequest)) {

			return true;
		}

		return false;
	}

	public static Iterator<String> iterator(PortletRequest portletRequest) {
		Set<String> set = keySet(portletRequest);

		return set.iterator();
	}

	public static Set<String> keySet(PortletRequest portletRequest) {
		Set<String> set = new HashSet<>();

		set.addAll(SessionMessages.keySet(portletRequest));

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		set.addAll(SessionMessages.keySet(httpServletRequest));

		return Collections.unmodifiableSet(set);
	}

	public static void print(PortletRequest portletRequest) {
		SessionMessages.print(portletRequest);

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		SessionMessages.print(httpServletRequest);
	}

	public static int size(PortletRequest portletRequest) {
		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(portletRequest);

		return SessionMessages.size(portletRequest) +
			SessionMessages.size(httpServletRequest);
	}

}