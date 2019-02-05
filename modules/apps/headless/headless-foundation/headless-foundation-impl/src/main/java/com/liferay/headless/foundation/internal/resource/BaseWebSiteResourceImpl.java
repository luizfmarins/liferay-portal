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

package com.liferay.headless.foundation.internal.resource;

import com.liferay.headless.foundation.dto.WebSite;
import com.liferay.headless.foundation.resource.WebSiteResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.context.Pagination;
import com.liferay.portal.vulcan.dto.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.annotation.Generated;

/**
 * @author Javier Gamarra
 * @generated
 */
@Generated("")
public abstract class BaseWebSiteResourceImpl implements WebSiteResource {

	@Override
	public Page<WebSite> getMyUserAccountWebSitePage(
			Long myUserAccountId, Company company, Pagination pagination)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	public Page<WebSite> getUserAccountWebSitePage(
			Long userAccountId, Company company, Pagination pagination)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	@Override
	public WebSite getWebSite(Long webSiteId, Company company)
		throws Exception {

		return new WebSite();
	}

	@Override
	public Page<WebSite> getWebSiteWebSitePage(
			Long webSiteId, Company company, Pagination pagination)
		throws Exception {

		return Page.of(Collections.emptyList());
	}

	protected <T, R> List<R> transform(
		List<T> list, Function<T, R> transformFunction) {

		return TransformUtil.transform(list, transformFunction);
	}

}