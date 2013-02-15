package comp.mq.edu.itec833.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The context listener for demo webapp. It adds the PortalFacade object to the application context, which
 * is then accessed by all struts actions.
 * @author Shamim Ahmed
 *
 */
public class CustomContextListener implements ServletContextListener {
  private static final String SERVICE_URI_PARAM = "service-uri";
  private static final String CONFIG_DIR_PARAM = "config-dir";
  private static final String PORTAL_FACADE_ATTRIBUTE = "portalFacade";
  
  @Override
  public void contextInitialized(ServletContextEvent sce) {
	ServletContext appContext = sce.getServletContext();
	String serviceUri = appContext.getInitParameter(SERVICE_URI_PARAM);
	String configDirRelative = appContext.getInitParameter(CONFIG_DIR_PARAM);
	String configDirAbsolute = appContext.getRealPath(configDirRelative);
	PortalFacade facade = new PortalFacade(serviceUri, configDirAbsolute);
	appContext.setAttribute(PORTAL_FACADE_ATTRIBUTE, facade);
  }
  
  @Override
  public void contextDestroyed(ServletContextEvent sce) {
	ServletContext appContext = sce.getServletContext();
	appContext.removeAttribute(PORTAL_FACADE_ATTRIBUTE);
  }
}
