package com.liferay.portal.jcr.modeshape;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Credentials;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.modeshape.jcr.JcrRepository;
import org.modeshape.jcr.JcrRepositoryFactory;
import org.modeshape.jcr.api.AnonymousCredentials;

import com.liferay.portal.jcr.modeshape.utils.PortletPropsValues;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.SystemProperties;
import com.liferay.portal.kernel.util.Time;


public class JCRFactoryImpl implements JCRFactory {

	public static final String REPOSITORY_NAME = GetterUtil.getString(PortletPropsValues.JCR_MODESHAPE_REPOSITORY_NAME);

	public static final String REPOSITORY_ROOT = GetterUtil.getString(PortletPropsValues.JCR_MODESHAPE_REPOSITORY_ROOT);

	public static final String CONFIG_FILE_PATH = GetterUtil.getString(PortletPropsValues.JCR_MODESHAPE_CONFIG_FILE_PATH);

	public static final String CREDENTIALS_USERNAME = GetterUtil.getString(PortletPropsValues.JCR_MODESHAPE_CREDENTIALS_USERNAME);

	public static final char[] CREDENTIALS_PASSWORD = GetterUtil.getString(PropsUtil.get(PortletPropsValues.JCR_MODESHAPE_CREDENTIALS_PASSWORD))
			.toCharArray();

	public Session createSession(String workspaceName) throws RepositoryException {

		Credentials credentials = new AnonymousCredentials();

		Session session = null;

		try {
			session = repository.login(credentials, workspaceName);
		} catch (RepositoryException re) {
			_log.error("Could not login to the workspace " + workspaceName);
			throw re;
		}

		return session;
	}

	public void initialize() throws RepositoryException {

		Session session = null;

		try {
			session = createSession(null);
		} catch (RepositoryException re) {
			_log.error("Could not initialize Modeshape");

			throw re;
		} finally {
			if (session != null) {
				session.logout();
			}
		}

		_initialized = true;
	}

	public void prepare() throws RepositoryException {

		try {
			File repositoryRoot = new File(JCRFactoryImpl.REPOSITORY_ROOT);
			File config = new File(JCRFactoryImpl.CONFIG_FILE_PATH);

			if (config.exists()) {
				return;
			}

			repositoryRoot.mkdirs();

			File tempFile = new File(SystemProperties.get(SystemProperties.TMP_DIR) + File.separator + Time.getTimestamp());

			String repositoryXmlPath = "repository-ext.xml";

			ClassLoader classLoader = getClass().getClassLoader();

			if (classLoader.getResource(repositoryXmlPath) == null) {
				repositoryXmlPath = "repository.xml";
			}

			FileUtil.write(tempFile, classLoader.getResourceAsStream(repositoryXmlPath));

			FileUtil.copyFile(tempFile, new File(JCRFactoryImpl.CONFIG_FILE_PATH));

			tempFile.delete();

		} catch (IOException ioe) {
			_log.error("Could not prepare Modeshape directory");

			throw new RepositoryException(ioe);
		}
	}

	public void shutdown() {

		if (_initialized) {
			repositoryFactory.shutdown();
		}

		_initialized = false;
	}

	protected JCRFactoryImpl() throws RepositoryException {

		File config = new File(JCRFactoryImpl.CONFIG_FILE_PATH);
		if (!config.exists())
			prepare();

		repositoryFactory = new JcrRepositoryFactory();
		// repo name doesn't have to be specified if only one is defined in configuration
		repository = (JcrRepository) repositoryFactory.getRepository("file://" + CONFIG_FILE_PATH, REPOSITORY_NAME);

		if (repository == null)
			throw new RepositoryException("There is no repository with name : " + REPOSITORY_NAME + " or it cannot be started");

		if (_log.isInfoEnabled()) {
			_log.info("Modeshape JCR intialized with config file path " + CONFIG_FILE_PATH);
			_log.info("Repository name : " + REPOSITORY_NAME);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(JCRFactoryImpl.class);

	private boolean _initialized;
	private JcrRepositoryFactory repositoryFactory;
	private JcrRepository repository;

}