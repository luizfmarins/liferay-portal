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

package com.liferay.layout.content.page.editor.web.internal.display.context;

import com.liferay.asset.display.contributor.AssetDisplayContributor;
import com.liferay.asset.display.contributor.AssetDisplayContributorTracker;
import com.liferay.asset.kernel.AssetRendererFactoryRegistryUtil;
import com.liferay.asset.kernel.model.AssetRendererFactory;
import com.liferay.asset.kernel.model.ClassType;
import com.liferay.asset.kernel.model.ClassTypeReader;
import com.liferay.layout.content.page.editor.constants.ContentPageEditorWebKeys;
import com.liferay.layout.page.template.model.LayoutPageTemplateEntry;
import com.liferay.layout.page.template.service.LayoutPageTemplateEntryServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.template.soy.util.SoyContext;
import com.liferay.portal.template.soy.util.SoyContextFactoryUtil;

import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Jürgen Kappler
 */
public class ContentPageEditorLayoutPageTemplateDisplayContext
	extends ContentPageEditorDisplayContext {

	public ContentPageEditorLayoutPageTemplateDisplayContext(
		HttpServletRequest request, RenderResponse renderResponse,
		String className, long classPK, boolean showMapping) {

		super(request, renderResponse, className, classPK);

		_showMapping = showMapping;

		_assetDisplayContributorTracker =
			(AssetDisplayContributorTracker)request.getAttribute(
				ContentPageEditorWebKeys.ASSET_DISPLAY_CONTRIBUTOR_TRACKER);
	}

	@Override
	public SoyContext getEditorContext() throws Exception {
		if (_editorSoyContext != null) {
			return _editorSoyContext;
		}

		SoyContext soyContext = super.getEditorContext();

		soyContext.put(
			"getAssetDisplayContributorsURL",
			getFragmentEntryActionURL(
				"/content_layout/get_asset_display_contributors"));
		soyContext.put(
			"getAssetClassTypesURL",
			getFragmentEntryActionURL("/content_layout/get_asset_class_types"));
		soyContext.put("lastSaveDate", StringPool.BLANK);

		if (_showMapping) {
			soyContext.put(
				"mappingFieldsURL",
				getFragmentEntryActionURL(
					"/content_layout/get_mapping_fields"));
		}

		soyContext.put(
			"sidebarPanels", getSidebarPanelSoyContexts(_showMapping, false));

		if (classNameId == PortalUtil.getClassNameId(
				LayoutPageTemplateEntry.class)) {

			soyContext.put(
				"publishLayoutPageTemplateEntryURL",
				getFragmentEntryActionURL(
					"/content_layout/publish_layout_page_template_entry"));
		}

		if (_showMapping) {
			soyContext.put("selectedMappingTypes", _getSelectedMappingTypes());
		}

		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		if ((layoutPageTemplateEntry != null) &&
			(layoutPageTemplateEntry.getStatus() !=
				WorkflowConstants.STATUS_APPROVED)) {

			String statusLabel = WorkflowConstants.getStatusLabel(
				layoutPageTemplateEntry.getStatus());

			soyContext.put("status", LanguageUtil.get(request, statusLabel));
		}

		soyContext.put(
			"updateLayoutPageTemplateEntryAssetTypeURL",
			getFragmentEntryActionURL(
				"/content_layout" +
					"/update_layout_page_template_entry_asset_type"));

		_editorSoyContext = soyContext;

		return _editorSoyContext;
	}

	private LayoutPageTemplateEntry _getLayoutPageTemplateEntry()
		throws PortalException {

		if (_layoutPageTemplateEntry != null) {
			return _layoutPageTemplateEntry;
		}

		_layoutPageTemplateEntry =
			LayoutPageTemplateEntryServiceUtil.fetchLayoutPageTemplateEntry(
				classPK);

		return _layoutPageTemplateEntry;
	}

	private String _getMappingSubtypeLabel() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		AssetRendererFactory assetRendererFactory =
			AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(
				layoutPageTemplateEntry.getClassName());

		if (assetRendererFactory == null) {
			return null;
		}

		ClassTypeReader classTypeReader =
			assetRendererFactory.getClassTypeReader();

		ClassType classType = classTypeReader.getClassType(
			layoutPageTemplateEntry.getClassTypeId(), themeDisplay.getLocale());

		return classType.getName();
	}

	private String _getMappingTypeLabel() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		AssetDisplayContributor assetDisplayContributor =
			_assetDisplayContributorTracker.getAssetDisplayContributor(
				layoutPageTemplateEntry.getClassName());

		if (assetDisplayContributor == null) {
			return null;
		}

		return assetDisplayContributor.getLabel(themeDisplay.getLocale());
	}

	private SoyContext _getSelectedMappingTypes() throws PortalException {
		LayoutPageTemplateEntry layoutPageTemplateEntry =
			_getLayoutPageTemplateEntry();

		if ((layoutPageTemplateEntry == null) ||
			(layoutPageTemplateEntry.getClassNameId() <= 0)) {

			return SoyContextFactoryUtil.createSoyContext();
		}

		SoyContext soyContext = SoyContextFactoryUtil.createSoyContext();

		SoyContext typeSoyContext = SoyContextFactoryUtil.createSoyContext();

		typeSoyContext.put("id", layoutPageTemplateEntry.getClassNameId());
		typeSoyContext.put("label", _getMappingTypeLabel());

		soyContext.put("type", typeSoyContext);

		if (layoutPageTemplateEntry.getClassTypeId() > 0) {
			SoyContext subtypeSoyContext =
				SoyContextFactoryUtil.createSoyContext();

			subtypeSoyContext.put(
				"id", layoutPageTemplateEntry.getClassTypeId());
			subtypeSoyContext.put("label", _getMappingSubtypeLabel());

			soyContext.put("subtype", subtypeSoyContext);
		}

		return soyContext;
	}

	private final AssetDisplayContributorTracker
		_assetDisplayContributorTracker;
	private SoyContext _editorSoyContext;
	private LayoutPageTemplateEntry _layoutPageTemplateEntry;
	private final boolean _showMapping;

}