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

package com.liferay.sharing.service.http;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import com.liferay.sharing.security.permission.SharingEntryAction;
import com.liferay.sharing.service.SharingEntryServiceUtil;

import java.rmi.RemoteException;

/**
 * Provides the SOAP utility for the
 * {@link SharingEntryServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 *
 * <p>
 * ServiceBuilder follows certain rules in translating the methods. For example,
 * if the method in the service utility returns a {@link java.util.List}, that
 * is translated to an array of {@link com.liferay.sharing.model.SharingEntrySoap}.
 * If the method in the service utility returns a
 * {@link com.liferay.sharing.model.SharingEntry}, that is translated to a
 * {@link com.liferay.sharing.model.SharingEntrySoap}. Methods that SOAP cannot
 * safely wire are skipped.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at http://localhost:8080/api/axis. Set the
 * property <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SharingEntryServiceHttp
 * @see com.liferay.sharing.model.SharingEntrySoap
 * @see SharingEntryServiceUtil
 * @generated
 */
@ProviderType
public class SharingEntryServiceSoap {
	public static com.liferay.sharing.model.SharingEntrySoap addOrUpdateSharingEntry(
		long toUserId, long classNameId, long classPK, long groupId,
		boolean shareable,
		java.util.Collection<SharingEntryAction> sharingEntryActions,
		java.util.Date expirationDate,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.sharing.model.SharingEntry returnValue = SharingEntryServiceUtil.addOrUpdateSharingEntry(toUserId,
					classNameId, classPK, groupId, shareable,
				sharingEntryActions, expirationDate, serviceContext);

			return com.liferay.sharing.model.SharingEntrySoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.sharing.model.SharingEntrySoap addSharingEntry(
		long toUserId, long classNameId, long classPK, long groupId,
		boolean shareable,
		java.util.Collection<SharingEntryAction> sharingEntryActions,
		java.util.Date expirationDate,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.sharing.model.SharingEntry returnValue = SharingEntryServiceUtil.addSharingEntry(toUserId,
					classNameId, classPK, groupId, shareable,
				sharingEntryActions, expirationDate, serviceContext);

			return com.liferay.sharing.model.SharingEntrySoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static com.liferay.sharing.model.SharingEntrySoap updateSharingEntry(
		long sharingEntryId,
		java.util.Collection<SharingEntryAction> sharingEntryActions,
		boolean shareable, java.util.Date expirationDate,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws RemoteException {
		try {
			com.liferay.sharing.model.SharingEntry returnValue = SharingEntryServiceUtil.updateSharingEntry(sharingEntryId,
				sharingEntryActions, shareable, expirationDate,
					serviceContext);

			return com.liferay.sharing.model.SharingEntrySoap.toSoapModel(returnValue);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(SharingEntryServiceSoap.class);
}