Liferay already uses Jackrabbit as a JCR repository. For scaling purposes mostly. The first idea to incorporate Modeshape was having JCR repository implementations swappable

https://github.com/l15k4/liferay-portal/commit/4743b0389d72bb5d36192a3f6093f268b46f9d58

http://www.liferay.com/community/forums/-/message_boards/message/11151950

But that was too intrusive, using hook is much more friendly. Although in Liferay there are a few ways how it might be done :

1. Using LiferayRepository and JCRStore (which is what is this hook about)

   In an ideal world JCRFactory interface would be public (part of portal-service.jar) and the hook would have to implement only JCRFactoryImpl  to obtain session from in JCRStore. But it isn't so it implements both custom JCRStore and JCRFactoryImpl + a few other classes.


2.  Implement a new Repository, like ModeshapeRepository (not using Modeshape in form of a JCRStore via LiferayRepository). Having a JCR repository implementation would make sense if you wanted to read/write it from somewhere else than Liferay doclib. It is just not possible via doclib because of indexing, permissioning & resources, assets etc., etc.



Usage :

After you clone the repository you need to modify deploy directory in pom.xml :

   <liferay.auto.deploy.dir>/opt/liferay/bundles/deploy</liferay.auto.deploy.dir>

There is a dl.store.impl property overriden to following, so that you LR repo will automatically use JCR instead of FileSystem.

   dl.store.impl=com.liferay.portal.jcr.modeshape.JCRStore

It has liferay 6.1.0-SNAPSHOT dependencies, snapshot from around 10/20/2011. Because the JCRStore must have been implemented, it wouldn't have to work with future/past snapshots. There are no Tags in Liferay github repository unfortunately.

It also has a compile time portal-impl dependency...Just for some JCRConstants that get inlined and won't be needed at runtime.

liferay-maven-plugin deploy goal is bound to package phase, so just run :

   mvn clean package
