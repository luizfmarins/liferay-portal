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

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.change.tracking.CTEngineManager;
import com.liferay.change.tracking.CTManager;
import com.liferay.change.tracking.constants.CTConstants;
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.model.CTEntry;
import com.liferay.document.library.constants.DLPortletKeys;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
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
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portletfilerepository.PortletFileRepository;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.ByteArrayInputStream;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Luiz Marins
 */
@RunWith(Arquillian.class)
public class DLFileEntryChangesRegisterCTEntityTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		ServiceTestUtil.setUser(TestPropsValues.getUser());

		_group = GroupTestUtil.addGroup();

		_userId = TestPropsValues.getUserId();

		_ctEngineManager.enableChangeTracking(
			TestPropsValues.getCompanyId(), _userId);

		Optional<CTCollection> ctCollectionOptional =
			_ctEngineManager.createCTCollection(
				_userId, _CT_COLLECTION_NAME, StringPool.BLANK);

		_ctCollection = ctCollectionOptional.get();

		_ctEngineManager.checkoutCTCollection(
			_userId, _ctCollection.getCtCollectionId());
	}

	@After
	public void tearDown() throws PortalException {
		_deleteCTCollections();

		_deleteFileTypes();

		_deleteStructures();

		_ctEngineManager.disableChangeTracking(TestPropsValues.getCompanyId());
	}

	@Test
	public void testAddFileEntryCreatesCTEntry() throws PortalException {
		FileEntry fileEntry = _addFileEntry();

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 1,
			modelChangeCTEntries.size());

		_assertCTEntry(
			modelChangeCTEntries.get(0), fileEntry, "1.0",
			CTConstants.CT_CHANGE_TYPE_ADDITION);
	}

	@Test
	public void testDeleteFileEntryDeletesAllCTEntries()
		throws PortalException {

		FileEntry fileEntry = _addFileEntry();

		fileEntry = _dlAppService.updateFileEntry(
			fileEntry.getFileEntryId(), StringPool.BLANK,
			ContentTypes.TEXT_PLAIN, "file updated.txt", StringPool.BLANK,
			StringPool.BLANK, DLVersionNumberIncrease.AUTOMATIC, null, 0,
			_getServiceContext());

		_dlAppService.deleteFileEntry(fileEntry.getFileEntryId());

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 0,
			modelChangeCTEntries.size());
	}

	@Test
	public void testPortletFileRepositoryDoNotCreateCTEntry()
		throws PortalException {

		FileEntry fileEntry = _portletFileRepository.addPortletFileEntry(
			_group.getGroupId(), _userId, User.class.getName(), _userId,
			DLPortletKeys.DOCUMENT_LIBRARY_ADMIN,
			DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, _createInputStream(),
			"file.txt", "text/plain", false);

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 0,
			modelChangeCTEntries.size());

		FileEntry fetchedFileEntry = _portletFileRepository.getPortletFileEntry(
			fileEntry.getFileEntryId());

		Assert.assertNotNull("File entry not fetched", fetchedFileEntry);
	}

	@Test
	public void testRegisterAggregateChangesForDDMStructure()
		throws PortalException {

		DLFileEntryType fileType = _createFileTypeWithStructure();

		FileEntry fileEntry = _addFileEntry(fileType);

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 1,
			modelChangeCTEntries.size());

		CTEntry fileCtEntry = modelChangeCTEntries.get(0);

		List<CTEntry> relatedCTEntries = _ctManager.getRelatedCTEntries(
			fileCtEntry, _ctCollection);

		Assert.assertEquals(
			"Amount of related CTEntries is incorrect", 1,
			relatedCTEntries.size());

		CTEntry relatedCtEntry = relatedCTEntries.get(0);

		List<com.liferay.dynamic.data.mapping.kernel.DDMStructure>
			ddmStructures = fileType.getDDMStructures();

		com.liferay.dynamic.data.mapping.kernel.DDMStructure ddmStructure =
			ddmStructures.get(0);

		Assert.assertEquals(
			"Incorrect related CTEntry", ddmStructure.getStructureId(),
			relatedCtEntry.getModelResourcePrimKey());
	}

	@Test
	public void testUpdateAssetTagRegisterChange() throws PortalException {
		FileEntry fileEntry = _addFileEntry();

		ServiceContext serviceContext = _getServiceContext();

		serviceContext.setAssetTagNames(new String[] {"tag1"});

		fileEntry = _dlAppService.updateFileEntry(
			fileEntry.getFileEntryId(), StringPool.BLANK,
			ContentTypes.TEXT_PLAIN, StringPool.BLANK, StringPool.BLANK,
			StringPool.BLANK, DLVersionNumberIncrease.AUTOMATIC, null, 0,
			serviceContext);

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 2,
			modelChangeCTEntries.size());
	}

	@Test
	public void testUpdateFileEntryCreatesCTEntry() throws PortalException {
		FileEntry fileEntry = _addFileEntry();

		fileEntry = _dlAppService.updateFileEntry(
			fileEntry.getFileEntryId(), StringPool.BLANK,
			ContentTypes.TEXT_PLAIN, "file updated.txt", StringPool.BLANK,
			StringPool.BLANK, DLVersionNumberIncrease.AUTOMATIC, null, 0,
			_getServiceContext());

		List<CTEntry> modelChangeCTEntries = _ctManager.getModelChangeCTEntries(
			_userId, fileEntry.getPrimaryKey());

		Assert.assertEquals(
			"Amount of model change CTEntry is incorrect", 2,
			modelChangeCTEntries.size());

		_assertCTEntry(
			modelChangeCTEntries.get(0), fileEntry, "1.0",
			CTConstants.CT_CHANGE_TYPE_ADDITION);

		_assertCTEntry(
			modelChangeCTEntries.get(1), fileEntry, "1.1",
			CTConstants.CT_CHANGE_TYPE_MODIFICATION);
	}

	private FileEntry _addFileEntry() throws PortalException {
		return _addFileEntry(null);
	}

	private FileEntry _addFileEntry(DLFileEntryType fileType)
		throws PortalException {

		ByteArrayInputStream inputStream = _createInputStream();

		return _dlAppService.addFileEntry(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			"file.txt", ContentTypes.TEXT_PLAIN, "file.txt", StringPool.BLANK,
			StringPool.BLANK, inputStream, 0, _getServiceContext(fileType));
	}

	private void _assertCTEntry(
			CTEntry ctEntry, FileEntry fileEntry, String version,
			int changeType)
		throws PortalException {

		Assert.assertEquals(
			"Incorrect change type", changeType, ctEntry.getChangeType());

		Assert.assertEquals(
			"Incorrect resource primary key", fileEntry.getFileEntryId(),
			ctEntry.getModelResourcePrimKey());

		FileVersion fileVersion = fileEntry.getFileVersion(version);

		Assert.assertEquals(
			"Incorrect classPK", fileVersion.getFileVersionId(),
			ctEntry.getModelClassPK());
	}

	private DLFileEntryType _createFileTypeWithStructure()
		throws PortalException {

		DDMForm ddmForm = _ddmBeanTranslator.translate(
			_ddm.getDDMForm(_STRUCTURE_DEFINITION));

		ServiceContext serviceContext = _getServiceContext();

		serviceContext.setAttribute("ddmForm", ddmForm);

		return _dlFileEntryTypeLocalService.addFileEntryType(
			_userId, _group.getGroupId(), _FILE_TYPE_NAME,
			_getLocalized(_FILE_TYPE_NAME), _getLocalized(_FILE_TYPE_NAME),
			new long[0], serviceContext);
	}

	private ByteArrayInputStream _createInputStream() {
		String content = StringUtil.randomString();

		return new ByteArrayInputStream(content.getBytes());
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
			if (_FILE_TYPE_NAME.equals(ft.getName())) {
				_dlFileEntryTypeLocalService.deleteDLFileEntryType(ft);
			}
		}
	}

	private void _deleteStructures() {
		List<DDMStructure> structures = _ddmStructureLocalService.getStructures(
			0, Integer.MAX_VALUE);

		for (DDMStructure s : structures) {
			if (_STRUCTURE_KEY.equals(s.getStructureKey())) {
				_ddmStructureLocalService.deleteDDMStructure(s);
			}
		}
	}

	private Map<Locale, String> _getLocalized(String value) {
		Map<Locale, String> map = new HashMap<>();

		map.put(LocaleUtil.getDefault(), value);

		return map;
	}

	private ServiceContext _getServiceContext() throws PortalException {
		return _getServiceContext(null);
	}

	private ServiceContext _getServiceContext(DLFileEntryType fileType)
		throws PortalException {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(
				_group.getGroupId(), _userId);

		if (fileType != null) {
			serviceContext.setAttribute(
				"fileEntryTypeId", fileType.getFileEntryTypeId());
		}

		Locale defaultLocale = LocaleUtil.getDefault();

		serviceContext.setAttribute(
			"defaultLanguageId", defaultLocale.toString());

		return serviceContext;
	}

	private static final String _CT_COLLECTION_NAME = "CTCollection";

	private static final String _FILE_TYPE_NAME = "Test Type";

	private static final String _STRUCTURE_DEFINITION =
		"{\"availableLanguageIds\":[\"en_US\"],\"successPage\":{\"body\":" +
			"{},\"title\":{},\"enabled\":false},\"defaultLanguageId\":" +
				"\"en_US\",\"fields\":[{\"dataType\":\"boolean\"," +
					"\"predefinedValue\":{\"en_US\":\"\"},\"readOnly\":false," +
						"\"label\":{\"en_US\":\"\"},\"type\":\"checkbox\"," +
							"\"required\":false,\"showLabel\":true," +
								"\"fieldNamespace\":\"\",\"indexType\":" +
									"\"keyword\",\"repeatable\":false," +
										"\"name\":\"Boolean35a9\"," +
											"\"localizable\":true,\"tip\":" +
												"{\"en_US\":\"\"}}]}";

	private static final String _STRUCTURE_KEY = "MY-KEY";

	private CTCollection _ctCollection;

	@Inject
	private CTEngineManager _ctEngineManager;

	@Inject
	private CTManager _ctManager;

	@Inject
	private DDM _ddm;

	@Inject
	private DDMBeanTranslator _ddmBeanTranslator;

	@Inject
	private DDMStructureLocalService _ddmStructureLocalService;

	@Inject
	private DLAppService _dlAppService;

	@Inject
	private DLFileEntryTypeLocalService _dlFileEntryTypeLocalService;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private PortletFileRepository _portletFileRepository;

	private long _userId;

}