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

package com.liferay.dynamic.data.mapping.change.tracking.internal.service;

import com.liferay.change.tracking.CTEngineManager;
import com.liferay.change.tracking.CTManager;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalService;
import com.liferay.dynamic.data.mapping.service.DDMStructureVersionLocalServiceWrapper;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceWrapper;

import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luiz Marins
 */
@Component(immediate = true, service = ServiceWrapper.class)
public class CTDDMStructureVersionLocalServiceWrapper
	extends DDMStructureVersionLocalServiceWrapper {

	public CTDDMStructureVersionLocalServiceWrapper() {
		super(null);
	}

	public CTDDMStructureVersionLocalServiceWrapper(
		DDMStructureVersionLocalService ddmStructureVersionLocalService) {

		super(ddmStructureVersionLocalService);
	}

	@Override
	public DDMStructureVersion getLatestStructureVersion(long structureId)
		throws PortalException {

		DDMStructureVersion version = super.getLatestStructureVersion(
			structureId);

		if (!_ctEngineManager.isChangeTrackingEnabled(version.getCompanyId())) {
			return version;
		}

		Optional<DDMStructureVersion> versionOptional =
			_getLatestStructureVersion(structureId);

		return versionOptional.orElse(version);
	}

	private Optional<DDMStructureVersion> _getLatestStructureVersion(
		long structureId) {

		Optional<CTEntry> ctEntryOptional =
			_ctManager.getLatestModelChangeCTEntryOptional(
				PrincipalThreadLocal.getUserId(), structureId);

		return ctEntryOptional.map(
			CTEntry::getModelClassPK
		).map(
			_ddmStructureVersionLocalService::fetchDDMStructureVersion
		);
	}

	@Reference
	private CTEngineManager _ctEngineManager;

	@Reference
	private CTManager _ctManager;

	@Reference
	private DDMStructureVersionLocalService _ddmStructureVersionLocalService;

}