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

package com.liferay.dynamic.data.mapping.service.base;

import com.liferay.asset.kernel.service.persistence.AssetEntryPersistence;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordService;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceFinder;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstancePersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceRecordFinder;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceRecordPersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceRecordVersionPersistence;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.service.persistence.WorkflowInstanceLinkPersistence;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the ddm form instance record remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.dynamic.data.mapping.service.impl.DDMFormInstanceRecordServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.dynamic.data.mapping.service.impl.DDMFormInstanceRecordServiceImpl
 * @generated
 */
public abstract class DDMFormInstanceRecordServiceBaseImpl
	extends BaseServiceImpl
	implements DDMFormInstanceRecordService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>DDMFormInstanceRecordService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordServiceUtil</code>.
	 */

	/**
	 * Returns the ddm form instance record local service.
	 *
	 * @return the ddm form instance record local service
	 */
	public
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService
				getDDMFormInstanceRecordLocalService() {

		return ddmFormInstanceRecordLocalService;
	}

	/**
	 * Sets the ddm form instance record local service.
	 *
	 * @param ddmFormInstanceRecordLocalService the ddm form instance record local service
	 */
	public void setDDMFormInstanceRecordLocalService(
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService
				ddmFormInstanceRecordLocalService) {

		this.ddmFormInstanceRecordLocalService =
			ddmFormInstanceRecordLocalService;
	}

	/**
	 * Returns the ddm form instance record remote service.
	 *
	 * @return the ddm form instance record remote service
	 */
	public DDMFormInstanceRecordService getDDMFormInstanceRecordService() {
		return ddmFormInstanceRecordService;
	}

	/**
	 * Sets the ddm form instance record remote service.
	 *
	 * @param ddmFormInstanceRecordService the ddm form instance record remote service
	 */
	public void setDDMFormInstanceRecordService(
		DDMFormInstanceRecordService ddmFormInstanceRecordService) {

		this.ddmFormInstanceRecordService = ddmFormInstanceRecordService;
	}

	/**
	 * Returns the ddm form instance record persistence.
	 *
	 * @return the ddm form instance record persistence
	 */
	public DDMFormInstanceRecordPersistence
		getDDMFormInstanceRecordPersistence() {

		return ddmFormInstanceRecordPersistence;
	}

	/**
	 * Sets the ddm form instance record persistence.
	 *
	 * @param ddmFormInstanceRecordPersistence the ddm form instance record persistence
	 */
	public void setDDMFormInstanceRecordPersistence(
		DDMFormInstanceRecordPersistence ddmFormInstanceRecordPersistence) {

		this.ddmFormInstanceRecordPersistence =
			ddmFormInstanceRecordPersistence;
	}

	/**
	 * Returns the ddm form instance record finder.
	 *
	 * @return the ddm form instance record finder
	 */
	public DDMFormInstanceRecordFinder getDDMFormInstanceRecordFinder() {
		return ddmFormInstanceRecordFinder;
	}

	/**
	 * Sets the ddm form instance record finder.
	 *
	 * @param ddmFormInstanceRecordFinder the ddm form instance record finder
	 */
	public void setDDMFormInstanceRecordFinder(
		DDMFormInstanceRecordFinder ddmFormInstanceRecordFinder) {

		this.ddmFormInstanceRecordFinder = ddmFormInstanceRecordFinder;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the ddm form instance local service.
	 *
	 * @return the ddm form instance local service
	 */
	public com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService
		getDDMFormInstanceLocalService() {

		return ddmFormInstanceLocalService;
	}

	/**
	 * Sets the ddm form instance local service.
	 *
	 * @param ddmFormInstanceLocalService the ddm form instance local service
	 */
	public void setDDMFormInstanceLocalService(
		com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService
			ddmFormInstanceLocalService) {

		this.ddmFormInstanceLocalService = ddmFormInstanceLocalService;
	}

	/**
	 * Returns the ddm form instance remote service.
	 *
	 * @return the ddm form instance remote service
	 */
	public com.liferay.dynamic.data.mapping.service.DDMFormInstanceService
		getDDMFormInstanceService() {

		return ddmFormInstanceService;
	}

	/**
	 * Sets the ddm form instance remote service.
	 *
	 * @param ddmFormInstanceService the ddm form instance remote service
	 */
	public void setDDMFormInstanceService(
		com.liferay.dynamic.data.mapping.service.DDMFormInstanceService
			ddmFormInstanceService) {

		this.ddmFormInstanceService = ddmFormInstanceService;
	}

	/**
	 * Returns the ddm form instance persistence.
	 *
	 * @return the ddm form instance persistence
	 */
	public DDMFormInstancePersistence getDDMFormInstancePersistence() {
		return ddmFormInstancePersistence;
	}

	/**
	 * Sets the ddm form instance persistence.
	 *
	 * @param ddmFormInstancePersistence the ddm form instance persistence
	 */
	public void setDDMFormInstancePersistence(
		DDMFormInstancePersistence ddmFormInstancePersistence) {

		this.ddmFormInstancePersistence = ddmFormInstancePersistence;
	}

	/**
	 * Returns the ddm form instance finder.
	 *
	 * @return the ddm form instance finder
	 */
	public DDMFormInstanceFinder getDDMFormInstanceFinder() {
		return ddmFormInstanceFinder;
	}

	/**
	 * Sets the ddm form instance finder.
	 *
	 * @param ddmFormInstanceFinder the ddm form instance finder
	 */
	public void setDDMFormInstanceFinder(
		DDMFormInstanceFinder ddmFormInstanceFinder) {

		this.ddmFormInstanceFinder = ddmFormInstanceFinder;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {

		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the workflow instance link local service.
	 *
	 * @return the workflow instance link local service
	 */
	public com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService
		getWorkflowInstanceLinkLocalService() {

		return workflowInstanceLinkLocalService;
	}

	/**
	 * Sets the workflow instance link local service.
	 *
	 * @param workflowInstanceLinkLocalService the workflow instance link local service
	 */
	public void setWorkflowInstanceLinkLocalService(
		com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService
			workflowInstanceLinkLocalService) {

		this.workflowInstanceLinkLocalService =
			workflowInstanceLinkLocalService;
	}

	/**
	 * Returns the workflow instance link persistence.
	 *
	 * @return the workflow instance link persistence
	 */
	public WorkflowInstanceLinkPersistence
		getWorkflowInstanceLinkPersistence() {

		return workflowInstanceLinkPersistence;
	}

	/**
	 * Sets the workflow instance link persistence.
	 *
	 * @param workflowInstanceLinkPersistence the workflow instance link persistence
	 */
	public void setWorkflowInstanceLinkPersistence(
		WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence) {

		this.workflowInstanceLinkPersistence = workflowInstanceLinkPersistence;
	}

	/**
	 * Returns the asset entry local service.
	 *
	 * @return the asset entry local service
	 */
	public com.liferay.asset.kernel.service.AssetEntryLocalService
		getAssetEntryLocalService() {

		return assetEntryLocalService;
	}

	/**
	 * Sets the asset entry local service.
	 *
	 * @param assetEntryLocalService the asset entry local service
	 */
	public void setAssetEntryLocalService(
		com.liferay.asset.kernel.service.AssetEntryLocalService
			assetEntryLocalService) {

		this.assetEntryLocalService = assetEntryLocalService;
	}

	/**
	 * Returns the asset entry remote service.
	 *
	 * @return the asset entry remote service
	 */
	public com.liferay.asset.kernel.service.AssetEntryService
		getAssetEntryService() {

		return assetEntryService;
	}

	/**
	 * Sets the asset entry remote service.
	 *
	 * @param assetEntryService the asset entry remote service
	 */
	public void setAssetEntryService(
		com.liferay.asset.kernel.service.AssetEntryService assetEntryService) {

		this.assetEntryService = assetEntryService;
	}

	/**
	 * Returns the asset entry persistence.
	 *
	 * @return the asset entry persistence
	 */
	public AssetEntryPersistence getAssetEntryPersistence() {
		return assetEntryPersistence;
	}

	/**
	 * Sets the asset entry persistence.
	 *
	 * @param assetEntryPersistence the asset entry persistence
	 */
	public void setAssetEntryPersistence(
		AssetEntryPersistence assetEntryPersistence) {

		this.assetEntryPersistence = assetEntryPersistence;
	}

	/**
	 * Returns the ddm form instance record version local service.
	 *
	 * @return the ddm form instance record version local service
	 */
	public com.liferay.dynamic.data.mapping.service.
		DDMFormInstanceRecordVersionLocalService
			getDDMFormInstanceRecordVersionLocalService() {

		return ddmFormInstanceRecordVersionLocalService;
	}

	/**
	 * Sets the ddm form instance record version local service.
	 *
	 * @param ddmFormInstanceRecordVersionLocalService the ddm form instance record version local service
	 */
	public void setDDMFormInstanceRecordVersionLocalService(
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordVersionLocalService
				ddmFormInstanceRecordVersionLocalService) {

		this.ddmFormInstanceRecordVersionLocalService =
			ddmFormInstanceRecordVersionLocalService;
	}

	/**
	 * Returns the ddm form instance record version remote service.
	 *
	 * @return the ddm form instance record version remote service
	 */
	public
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordVersionService
				getDDMFormInstanceRecordVersionService() {

		return ddmFormInstanceRecordVersionService;
	}

	/**
	 * Sets the ddm form instance record version remote service.
	 *
	 * @param ddmFormInstanceRecordVersionService the ddm form instance record version remote service
	 */
	public void setDDMFormInstanceRecordVersionService(
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordVersionService
				ddmFormInstanceRecordVersionService) {

		this.ddmFormInstanceRecordVersionService =
			ddmFormInstanceRecordVersionService;
	}

	/**
	 * Returns the ddm form instance record version persistence.
	 *
	 * @return the ddm form instance record version persistence
	 */
	public DDMFormInstanceRecordVersionPersistence
		getDDMFormInstanceRecordVersionPersistence() {

		return ddmFormInstanceRecordVersionPersistence;
	}

	/**
	 * Sets the ddm form instance record version persistence.
	 *
	 * @param ddmFormInstanceRecordVersionPersistence the ddm form instance record version persistence
	 */
	public void setDDMFormInstanceRecordVersionPersistence(
		DDMFormInstanceRecordVersionPersistence
			ddmFormInstanceRecordVersionPersistence) {

		this.ddmFormInstanceRecordVersionPersistence =
			ddmFormInstanceRecordVersionPersistence;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return DDMFormInstanceRecordService.class.getName();
	}

	protected Class<?> getModelClass() {
		return DDMFormInstanceRecord.class;
	}

	protected String getModelClassName() {
		return DDMFormInstanceRecord.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				ddmFormInstanceRecordPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService.class
	)
	protected
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService ddmFormInstanceRecordLocalService;

	@BeanReference(type = DDMFormInstanceRecordService.class)
	protected DDMFormInstanceRecordService ddmFormInstanceRecordService;

	@BeanReference(type = DDMFormInstanceRecordPersistence.class)
	protected DDMFormInstanceRecordPersistence ddmFormInstanceRecordPersistence;

	@BeanReference(type = DDMFormInstanceRecordFinder.class)
	protected DDMFormInstanceRecordFinder ddmFormInstanceRecordFinder;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService.class
	)
	protected
		com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService
			ddmFormInstanceLocalService;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceService.class
	)
	protected com.liferay.dynamic.data.mapping.service.DDMFormInstanceService
		ddmFormInstanceService;

	@BeanReference(type = DDMFormInstancePersistence.class)
	protected DDMFormInstancePersistence ddmFormInstancePersistence;

	@BeanReference(type = DDMFormInstanceFinder.class)
	protected DDMFormInstanceFinder ddmFormInstanceFinder;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserService.class
	)
	protected com.liferay.portal.kernel.service.UserService userService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService.class
	)
	protected com.liferay.portal.kernel.service.WorkflowInstanceLinkLocalService
		workflowInstanceLinkLocalService;

	@ServiceReference(type = WorkflowInstanceLinkPersistence.class)
	protected WorkflowInstanceLinkPersistence workflowInstanceLinkPersistence;

	@ServiceReference(
		type = com.liferay.asset.kernel.service.AssetEntryLocalService.class
	)
	protected com.liferay.asset.kernel.service.AssetEntryLocalService
		assetEntryLocalService;

	@ServiceReference(
		type = com.liferay.asset.kernel.service.AssetEntryService.class
	)
	protected com.liferay.asset.kernel.service.AssetEntryService
		assetEntryService;

	@ServiceReference(type = AssetEntryPersistence.class)
	protected AssetEntryPersistence assetEntryPersistence;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionLocalService.class
	)
	protected com.liferay.dynamic.data.mapping.service.
		DDMFormInstanceRecordVersionLocalService
			ddmFormInstanceRecordVersionLocalService;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordVersionService.class
	)
	protected
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordVersionService
				ddmFormInstanceRecordVersionService;

	@BeanReference(type = DDMFormInstanceRecordVersionPersistence.class)
	protected DDMFormInstanceRecordVersionPersistence
		ddmFormInstanceRecordVersionPersistence;

}