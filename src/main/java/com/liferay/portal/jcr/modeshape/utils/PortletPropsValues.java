package com.liferay.portal.jcr.modeshape.utils;

import com.liferay.util.portlet.PortletProps;


public class PortletPropsValues {

	public static final String JCR_MODESHAPE_CONFIG_FILE_PATH = PortletProps.get(
			PortletPropsKeys.JCR_MODESHAPE_CONFIG_FILE_PATH);

		public static final String JCR_MODESHAPE_CREDENTIALS_PASSWORD = PortletProps.get(
			PortletPropsKeys.JCR_MODESHAPE_CREDENTIALS_PASSWORD);

		public static final String JCR_MODESHAPE_CREDENTIALS_USERNAME = PortletProps.get(
			PortletPropsKeys.JCR_MODESHAPE_CREDENTIALS_USERNAME);

		public static final String JCR_MODESHAPE_REPOSITORY_ROOT = PortletProps.get(
			PortletPropsKeys.JCR_MODESHAPE_REPOSITORY_ROOT);
		
		public static final String JCR_MODESHAPE_REPOSITORY_NAME = PortletProps.get(
				PortletPropsKeys.JCR_MODESHAPE_REPOSITORY_NAME);

}