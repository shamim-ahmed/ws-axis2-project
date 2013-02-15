package comp.mq.edu.itec833.struts.action;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with add tag functionality
 * @author Poopak Alaeifar
 *
 */
public class AddTagAction extends ActionSupport implements SessionAware {
  private static final long serialVersionUID = 1L;
  private String tag;
  
  @SuppressWarnings("rawtypes")
  private Map sessionMap;

  public String getTag() {
	return tag;
  }

  public void setTag(String tag) {
	this.tag = tag;
  }
  
  @Override
  public void validate() {
	if (StringUtils.isBlank(tag)) {
	  addFieldError("tag", "tag cannot be empty");
	}
  }
  
  @Override
  public String execute() {
	// retrieve the PortalFacade object from servlet context
	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	
	// check if user is logged in
	String sessionId = (String) sessionMap.get("sessionId");
	
	if (StringUtils.isBlank(sessionId)) {
	  addActionError("User is not logged in");
	  return ERROR;
	}
	
	// invoke the web service via facade
	boolean result = facade.addTag(tag, sessionId);
	
	if (!result) {
	  addActionError(String.format("The tag %s is already present in tag list", tag));
	}
	
	return result ? SUCCESS : ERROR;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public void setSession(Map sessionMap) {
	this.sessionMap = sessionMap;
  }
}
