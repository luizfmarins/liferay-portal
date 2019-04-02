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

package com.liferay.document.library.change.tracking.service.test;

import com.liferay.change.tracking.CTEngineManager;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLFileEntryTypeLocalService;
import com.liferay.dynamic.data.mapping.kernel.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.dynamic.data.mapping.util.DDMBeanTranslator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.ByteArrayInputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Luiz Marins
 */
@Component(service = DLFileCTTestHelper.class)
public class DLFileCTTestHelper {

	public static final String FILE_TYPE_NAME = "Test Type";

	public static final String STRUCTURE_KEY = "MY-KEY";

	public FileEntry addFileEntry(DLFileEntryType fileType, Group group)
		throws PortalException {

		ByteArrayInputStream inputStream = createInputStream();

		return _dlAppService.addFileEntry(
			group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"file.txt", ContentTypes.TEXT_PLAIN, "file.txt", StringPool.BLANK,
			StringPool.BLANK, inputStream, 0,
			getServiceContext(
				fileType, group, PrincipalThreadLocal.getUserId()));
	}

	public FileEntry addFileEntry(Group group) throws PortalException {
		return addFileEntry(null, group);
	}

	public DLFileEntryType createFileTypeWithStructure(Group group, long userId)
		throws PortalException {

		DDMForm ddmForm = _ddmBeanTranslator.translate(
			_ddm.getDDMForm(getStructureDefinitionOneField()));

		ServiceContext serviceContext = getServiceContext(group, userId);

		serviceContext.setAttribute("ddmForm", ddmForm);

		return _dlFileEntryTypeLocalService.addFileEntryType(
			userId, group.getGroupId(), FILE_TYPE_NAME,
			_getLocalized(FILE_TYPE_NAME), _getLocalized(FILE_TYPE_NAME),
			new long[0], serviceContext);
	}

	public ByteArrayInputStream createInputStream() {
		String content = StringUtil.randomString();

		return new ByteArrayInputStream(content.getBytes());
	}

	public ServiceContext getServiceContext(
			DLFileEntryType fileType, Group group, long userId)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				group.getGroupId(), userId);

		if (fileType != null) {
			serviceContext.setAttribute(
				"fileEntryTypeId", fileType.getFileEntryTypeId());
		}

		Locale defaultLocale = LocaleUtil.getDefault();

		serviceContext.setAttribute(
			"defaultLanguageId", defaultLocale.toString());

		return serviceContext;
	}

	public ServiceContext getServiceContext(Group group, long userId)
		throws PortalException {

		return getServiceContext(null, group, userId);
	}

	public String getStructureDefinitionOneField() {
		return String.format(_STRUCTURE_DEFINITION, _STRUCTURE_CHECKBOX_FIELD);
	}

	public String getStructureDefinitionTwoFields() {
		return String.format(
			_STRUCTURE_DEFINITION,
			_STRUCTURE_CHECKBOX_FIELD + "," + _STRUCTURE_TEXT_FIELD);
	}

	public void tearDown() throws PortalException {
		_deleteCTCollections();

		_deleteFileTypes();

		_deleteStructures();

		_ctEngineManager.disableChangeTracking(TestPropsValues.getCompanyId());
	}

	private void _deleteCTCollections() throws PortalException {
		List<CTCollection> ctCollections = _ctEngineManager.getCTCollections(
			TestPropsValues.getCompanyId());

		for (CTCollection c : ctCollections) {
			_ctEngineManager.deleteCTCollection(c.getCtCollectionId());
		}
	}

	private void _deleteFileTypes() {
		List<DLFileEntryType> fileTypes =
			_dlFileEntryTypeLocalService.getDLFileEntryTypes(
				0, Integer.MAX_VALUE);

		for (DLFileEntryType ft : fileTypes) {
			if (DLFileCTTestHelper.FILE_TYPE_NAME.equals(ft.getName())) {
				_dlFileEntryTypeLocalService.deleteDLFileEntryType(ft);
			}
		}
	}

	private void _deleteStructures() {
		List<DDMStructure> structures = _ddmStructureLocalService.getStructures(
			0, Integer.MAX_VALUE);

		for (DDMStructure s : structures) {
			if (STRUCTURE_KEY.equals(s.getStructureKey())) {
				_ddmStructureLocalService.deleteDDMStructure(s);
			}
		}
	}

	private Map<Locale, String> _getLocalized(String value) {
		Map<Locale, String> map = new HashMap<>();

		map.put(LocaleUtil.getDefault(), value);

		return map;
	}

	private static final String _STRUCTURE_CHECKBOX_FIELD =
		"{\"dataType\":\"boolean\",\"predefinedValue\":{\"en_US\":\"\"}," +
			"\"readOnly\":false,\"label\":{\"en_US\":\"\"},\"type\":" +
				"\"checkbox\",\"required\":false,\"showLabel\":true," +
					"\"fieldNamespace\":\"\",\"indexType\":\"keyword\"," +
						"\"repeatable\":false,\"name\":\"Boolean35a9\"," +
							"\"localizable\":true,\"tip\":{\"en_US\":\"\"}}";

	private static final String _STRUCTURE_DEFINITION =
		"{\"availableLanguageIds\":[\"en_US\"],\"successPage\":{\"body\":" +
			"{},\"title\":{},\"enabled\":false},\"defaultLanguageId\":" +
				"\"en_US\",\"fields\":[%s]}";

	private static final String _STRUCTURE_TEXT_FIELD =
		"{\"indexType\":\"keyword\",\"repeatable\":false,\"dataType\":" +
			"\"string\",\"predefinedValue\":{\"en_US\":\"\"},\"name\":" +
				"\"Text6q9t\",\"localizable\":true,\"readOnly\":false," +
					"\"tip\":{\"en_US\":\"\"},\"label\":{\"en_US\":\"Text\"}," +
						"\"type\":\"text\",\"required\":false,\"showLabel\":" +
							"true}";

	@Reference
	private CTEngineManager _ctEngineManager;

	@Reference
	private DDM _ddm;

	@Reference
	private DDMBeanTranslator _ddmBeanTranslator;

	@Reference
	private DDMStructureLocalService _ddmStructureLocalService;

	@Reference
	private DLAppService _dlAppService;

	@Reference
	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

}