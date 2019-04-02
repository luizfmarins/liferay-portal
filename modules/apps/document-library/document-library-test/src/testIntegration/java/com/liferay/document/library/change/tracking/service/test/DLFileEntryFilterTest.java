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
import com.liferay.change.tracking.model.CTCollection;
import com.liferay.document.library.kernel.model.DLFileEntryType;
import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.model.DLVersionNumberIncrease;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.dynamic.data.mapping.kernel.DDMStructure;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMStructureConstants;
import com.liferay.dynamic.data.mapping.service.DDMStructureLocalService;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.security.auth.PrincipalThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.repository.liferayrepository.model.LiferayFileEntry;
import com.liferay.portal.service.test.ServiceTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
public class DLFileEntryFilterTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		ServiceTestUtil.setUser(TestPropsValues.getUser());

		_group = GroupTestUtil.addGroup();

		_user1 = TestPropsValues.getUser();

		_user2 = UserTestUtil.addUser();

		_ctEngineManager.enableChangeTracking(
			TestPropsValues.getCompanyId(), _user1.getUserId());

		_ctCollectionUser1 = _createCtCollection(_user1);

		_createCtCollection(_user2);
	}

	@After
	public void tearDown() throws Exception {
		ServiceTestUtil.setUser(TestPropsValues.getUser());

		_dlFileCTTestHelper.tearDown();
	}

	@Test
	public void testFileWithSingleStructure() throws PortalException {
		_checkoutProductionCt(_user1);

		DLFileEntryType fileType =
			_dlFileCTTestHelper.createFileTypeWithStructure(
				_group, _user1.getUserId());

		String updatedStructureDefinition =
			_dlFileCTTestHelper.getStructureDefinitionTwoFields();

		_ctEngineManager.checkoutCTCollection(
			_user1.getUserId(), _ctCollectionUser1.getCtCollectionId());

		_updateStructure(fileType, updatedStructureDefinition);

		List<DDMFormField> fields = _getStructureFields(fileType);

		Assert.assertEquals(
			"Incorrect quantity of form fields", 2, fields.size());

		_checkoutProductionCt(_user1);

		List<DDMFormField> fieldsProduction = _getStructureFields(fileType);

		Assert.assertEquals(
			"Incorrect quantity of form fields", 1, fieldsProduction.size());
	}

	@Test
	public void testGetFileEntryProductionChangelistDoNotHavePendingChanges()
		throws PortalException {

		_checkoutProductionCt(_user1);

		FileEntry fileEntry = _addFileEntry(_user1);

		_ctEngineManager.checkoutCTCollection(
			_user1.getUserId(), _ctCollectionUser1.getCtCollectionId());

		_dlAppService.updateFileEntry(
			fileEntry.getFileEntryId(), "file.txt", fileEntry.getMimeType(),
			"testfile updated.txt", StringPool.BLANK, StringPool.BLANK,
			DLVersionNumberIncrease.MAJOR, _getInputStream(), 0,
			_getServiceContext(_user1));

		List<Object> filesCollection1 =
			DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), 0, -1, true, 0, Integer.MAX_VALUE);

		Assert.assertEquals(
			"Amount of files fetches is incorrect", 1, filesCollection1.size());

		LiferayFileEntry fileCollection1 =
			(LiferayFileEntry)filesCollection1.get(0);

		Assert.assertEquals(
			"Incorrect file title", "testfile updated.txt",
			fileCollection1.getTitle());

		_checkoutProductionCt(_user1);

		List<Object> filesProduction =
			DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), 0, -1, true, 0, Integer.MAX_VALUE);

		Assert.assertEquals(
			"Amount of files fetches is incorrect", 1, filesProduction.size());

		LiferayFileEntry fileProduction = (LiferayFileEntry)filesProduction.get(
			0);

		Assert.assertEquals(
			"Incorrect file title", "testfile.txt", fileProduction.getTitle());
	}

	@Test
	public void testGetListProductionChangelistDoNotHavePendingChanges()
		throws PortalException {

		_checkoutProductionCt(_user1);

		FileEntry fileEntry = _addFileEntry(_user1);

		_ctEngineManager.checkoutCTCollection(
			_user1.getUserId(), _ctCollectionUser1.getCtCollectionId());

		_dlAppService.updateFileEntry(
			fileEntry.getFileEntryId(), "file.txt", fileEntry.getMimeType(),
			"testfile updated.txt", StringPool.BLANK, StringPool.BLANK,
			DLVersionNumberIncrease.MAJOR, _getInputStream(), 0,
			_getServiceContext(_user1));

		FileEntry fileCollection1 = _dlAppService.getFileEntry(
			fileEntry.getFileEntryId());

		Assert.assertEquals(
			"Incorrect file title", "testfile updated.txt",
			fileCollection1.getTitle());

		_checkoutProductionCt(_user1);

		FileEntry fileProduction = _dlAppService.getFileEntry(
			fileEntry.getFileEntryId());

		Assert.assertEquals(
			"Incorrect file title", "testfile.txt", fileProduction.getTitle());
	}

	@Test
	public void testUsersDoNotSeeChangesOfEachOtherChangeList()
		throws PortalException {

		FileEntry fileUser1 = _addFileEntry(_user1);

		FileEntry fileUser2 = _addFileEntry(_user2);

		_assertUserFetchedFiles(_user1, fileUser1);

		_assertUserFetchedFiles(_user2, fileUser2);
	}

	@Test
	public void testUserSeeAnotherUsersPublishedFiles() throws PortalException {
		_addFileEntry(_user1);

		_addFileEntry(_user2);

		PrincipalThreadLocal.setName(_user1.getUserId());

		_ctEngineManager.publishCTCollection(
			_user1.getUserId(), _ctCollectionUser1.getCtCollectionId());

		PrincipalThreadLocal.setName(_user2.getUserId());

		List<Object> filesUser =
			DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), 0, -1, true, 0, Integer.MAX_VALUE);

		Assert.assertEquals(
			"Amount of files fetches is incorrect", 2, filesUser.size());
	}

	private FileEntry _addFileEntry(User user) throws PortalException {
		PrincipalThreadLocal.setName(user.getUserId());

		String fileName = user.getScreenName() + "file.txt";

		return _dlAppService.addFileEntry(
			_group.getGroupId(), DLFolderConstants.DEFAULT_PARENT_FOLDER_ID,
			fileName, ContentTypes.TEXT_PLAIN, fileName, StringPool.BLANK,
			StringPool.BLANK, _getInputStream(), 0, _getServiceContext(user));
	}

	private void _assertUserFetchedFiles(User user, FileEntry fileEntry)
		throws PortalException {

		PrincipalThreadLocal.setName(user.getUserId());

		List<Object> filesUser =
			DLAppServiceUtil.getFoldersAndFileEntriesAndFileShortcuts(
				_group.getGroupId(), 0, -1, true, 0, Integer.MAX_VALUE);

		Assert.assertEquals(
			"Amount of files fetches is incorrect", 1, filesUser.size());

		Assert.assertEquals(
			"Incorrect file fetched", fileEntry.getFileName(),
			((FileEntry)filesUser.get(0)).getFileName());
	}

	private void _checkoutProductionCt(User user) throws PortalException {
		Optional<CTCollection> productionCTCollectionOptional =
			_ctEngineManager.getProductionCTCollectionOptional(
				TestPropsValues.getCompanyId());

		CTCollection productionCtCollection =
			productionCTCollectionOptional.get();

		_ctEngineManager.checkoutCTCollection(
			user.getUserId(), productionCtCollection.getCtCollectionId());
	}

	private CTCollection _createCtCollection(User user) {
		Optional<CTCollection> ctCollectionOptional =
			_ctEngineManager.createCTCollection(
				user.getUserId(), user.getScreenName() + _CT_COLLECTION_NAME,
				StringPool.BLANK);

		CTCollection ctCollection = ctCollectionOptional.get();

		_ctEngineManager.checkoutCTCollection(
			user.getUserId(), ctCollection.getCtCollectionId());

		return ctCollection;
	}

	private InputStream _getInputStream() {
		String content = StringUtil.randomString();

		return new ByteArrayInputStream(content.getBytes());
	}

	private Map<Locale, String> _getLocalized(String value) {
		Map<Locale, String> map = new HashMap<>();

		map.put(LocaleUtil.getDefault(), value);

		return map;
	}

	private ServiceContext _getServiceContext(User user)
		throws PortalException {

		return ServiceContextTestUtil.getServiceContext(
			_group.getGroupId(), user.getUserId());
	}

	private List<DDMFormField> _getStructureFields(DLFileEntryType fileType)
		throws PortalException {

		List<DDMStructure> structures = fileType.getDDMStructures();

		Assert.assertEquals(
			"Incorrect quantity of structures", 1, structures.size());

		DDMStructure structure = structures.get(0);

		DDMForm form = _ddm.getDDMForm(
			_portal.getClassNameId(
				com.liferay.dynamic.data.mapping.model.DDMStructure.class),
			structure.getStructureId());

		return form.getDDMFormFields();
	}

	private void _updateStructure(DLFileEntryType fileType, String definition)
		throws PortalException {

		List<DDMStructure> structures = fileType.getDDMStructures();

		Assert.assertEquals(
			"Incorrect quantity of structures", 1, structures.size());

		DDMStructure structure = structures.get(0);

		DDMForm updatedForm = _ddm.getDDMForm(definition);

		_ddmStructureLocalService.updateStructure(
			_user1.getUserId(), structure.getStructureId(),
			DDMStructureConstants.DEFAULT_PARENT_STRUCTURE_ID,
			_getLocalized(DLFileCTTestHelper.FILE_TYPE_NAME),
			_getLocalized(DLFileCTTestHelper.FILE_TYPE_NAME), updatedForm,
			_ddm.getDefaultDDMFormLayout(updatedForm), new ServiceContext());
	}

	private static final String _CT_COLLECTION_NAME = "CTCollection";

	private CTCollection _ctCollectionUser1;

	@Inject
	private CTEngineManager _ctEngineManager;

	@Inject
	private DDM _ddm;

	@Inject
	private DDMStructureLocalService _ddmStructureLocalService;

	@Inject
	private DLAppService _dlAppService;

	@Inject
	private DLFileCTTestHelper _dlFileCTTestHelper;

	@DeleteAfterTestRun
	private Group _group;

	@Inject
	private Portal _portal;

	private User _user1;

	@DeleteAfterTestRun
	private User _user2;

}