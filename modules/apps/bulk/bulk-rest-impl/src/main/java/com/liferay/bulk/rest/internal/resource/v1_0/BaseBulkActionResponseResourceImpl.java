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

package com.liferay.bulk.rest.internal.resource.v1_0;

import com.liferay.bulk.rest.dto.v1_0.BulkActionResponse;
import com.liferay.bulk.rest.dto.v1_0.BulkAssetEntryAction;
import com.liferay.bulk.rest.dto.v1_0.BulkAssetEntryCommonCategories;
import com.liferay.bulk.rest.dto.v1_0.BulkAssetEntryCommonTags;
import com.liferay.bulk.rest.dto.v1_0.BulkAssetEntryUpdateCategoriesAction;
import com.liferay.bulk.rest.dto.v1_0.BulkAssetEntryUpdateTagsAction;
import com.liferay.bulk.rest.resource.v1_0.BulkActionResponseResource;
import com.liferay.petra.function.UnsafeFunction;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.accept.language.AcceptLanguage;
import com.liferay.portal.vulcan.util.TransformUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import java.net.URI;

import java.util.List;

import javax.annotation.Generated;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author Alejandro Tardín
 * @generated
 */
@Generated("")
@Path("/v1.0")
public abstract class BaseBulkActionResponseResourceImpl
	implements BulkActionResponseResource {

	@Override
	@Consumes("application/json")
	@POST
	@Path("/categories/{class-name-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "BulkActionResponse")})
	public BulkActionResponse postCategoryClassName(
			@PathParam("class-name-id") Long classNameId,
			BulkAssetEntryUpdateCategoriesAction
				bulkAssetEntryUpdateCategoriesAction)
		throws Exception {

		return new BulkActionResponse();
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/categories/{content-space-id}/{class-name-id}/common")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "BulkActionResponse")})
	public BulkAssetEntryCommonCategories
			postCategoryContentSpaceClassNameCommon(
				@PathParam("content-space-id") Long contentSpaceId,
				@PathParam("class-name-id") Long classNameId,
				BulkAssetEntryAction bulkAssetEntryAction)
		throws Exception {

		return new BulkAssetEntryCommonCategories();
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/tags/{class-name-id}")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "BulkActionResponse")})
	public BulkActionResponse postTagClassName(
			@PathParam("class-name-id") Long classNameId,
			BulkAssetEntryUpdateTagsAction bulkAssetEntryUpdateTagsAction)
		throws Exception {

		return new BulkActionResponse();
	}

	@Override
	@Consumes("application/json")
	@POST
	@Path("/tags/{content-space-id}/{class-name-id}/common")
	@Produces("application/json")
	@Tags(value = {@Tag(name = "BulkActionResponse")})
	public BulkAssetEntryCommonTags postTagContentSpaceClassNameCommon(
			@PathParam("content-space-id") Long contentSpaceId,
			@PathParam("class-name-id") Long classNameId,
			BulkAssetEntryAction bulkAssetEntryAction)
		throws Exception {

		return new BulkAssetEntryCommonTags();
	}

	public void setContextCompany(Company contextCompany) {
		this.contextCompany = contextCompany;
	}

	protected String getJAXRSLink(String methodName, Object... values) {
		String baseURIString = String.valueOf(contextUriInfo.getBaseUri());

		if (baseURIString.endsWith(StringPool.FORWARD_SLASH)) {
			baseURIString = baseURIString.substring(
				0, baseURIString.length() - 1);
		}

		URI resourceURI = UriBuilder.fromResource(
			BaseBulkActionResponseResourceImpl.class
		).build();

		URI methodURI = UriBuilder.fromMethod(
			BaseBulkActionResponseResourceImpl.class, methodName
		).build(
			values
		);

		return baseURIString + resourceURI.toString() + methodURI.toString();
	}

	protected void preparePatch(BulkActionResponse bulkActionResponse) {
	}

	protected <T, R> List<R> transform(
		List<T> list, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transform(list, unsafeFunction);
	}

	protected <T, R> R[] transform(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transform(array, unsafeFunction, clazz);
	}

	protected <T, R> R[] transformToArray(
		List<T> list, UnsafeFunction<T, R, Exception> unsafeFunction,
		Class<?> clazz) {

		return TransformUtil.transformToArray(list, unsafeFunction, clazz);
	}

	protected <T, R> List<R> transformToList(
		T[] array, UnsafeFunction<T, R, Exception> unsafeFunction) {

		return TransformUtil.transformToList(array, unsafeFunction);
	}

	@Context
	protected AcceptLanguage contextAcceptLanguage;

	@Context
	protected Company contextCompany;

	@Context
	protected UriInfo contextUriInfo;

}