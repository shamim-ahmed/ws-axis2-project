package comp.mq.edu.itec833.struts.action;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with logout functionality
 * @author Poopak Alaeifar
 *
 */
public class LogoutAction extends ActionSupport implements SessionAware {
  private static final long serialVersionUID = 1L;
  @SuppressWarnings("rawtypes")
  private Map sessionMap;

  @Override
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }

  @Override
  public String execute() {
	String sessionId = (String) sessionMap.get("sessionId");

	if (StringUtils.isBlank(sessionId)) {
	  addActionError("user is not logged in");
	  return ERROR;
	}

	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	boolean result = facade.logout(sessionId);
	
	if (!result) {
	  addActionError("Logout did not succeed");
	  return ERROR;
	}

	// this is crucial
	sessionMap.remove("sessionId");
	sessionMap.remove("userName");
	
	return SUCCESS;
  }
}
