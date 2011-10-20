package com.liferay.portal.jcr.modeshape;

import javax.jcr.RepositoryException;
import javax.jcr.Session;


public class JCRFactoryUtil {

	public static JCRFactory getJCRFactory() throws RepositoryException  {
		_jcrFactory =_jcrFactory != null ? _jcrFactory : new JCRFactoryImpl();
		return _jcrFactory;
	}

	public static Session createSession(String workspaceName)
		throws RepositoryException {

		if (workspaceName == null) {
			workspaceName = JCRFactory.WORKSPACE_NAME;
		}

		return getJCRFactory().createSession(workspaceName);
	}

	public static Session createSession() throws RepositoryException {
		return createSession(null);
	}

	public static void initialize() throws RepositoryException {
		getJCRFactory().initialize();
	}

	public static void prepare() throws RepositoryException {
		getJCRFactory().prepare();
	}

	public static void shutdown() throws RepositoryException {
		getJCRFactory().shutdown();
	}

	private static JCRFactory _jcrFactory;

}
