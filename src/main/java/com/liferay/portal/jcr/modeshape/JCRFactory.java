package com.liferay.portal.jcr.modeshape;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;


public interface JCRFactory {

	public static final String WORKSPACE_NAME = PropsUtil.get(PropsKeys.JCR_WORKSPACE_NAME);

	public static final String NODE_DOCUMENTLIBRARY = PropsUtil.get(PropsKeys.JCR_NODE_DOCUMENTLIBRARY);

	public Session createSession(String workspaceName) throws RepositoryException;

	public void initialize() throws RepositoryException;

	public void prepare() throws RepositoryException;

	public void shutdown();
}