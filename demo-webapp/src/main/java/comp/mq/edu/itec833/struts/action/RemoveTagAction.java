package comp.mq.edu.itec833.struts.action;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with remove tag functionality
 * @author Poopak Alaeifar
 *
 */
public class RemoveTagAction extends ActionSupport implements SessionAware {
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
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }

  @Override
  public void validate() {
	if (StringUtils.isBlank(tag)) {
	  addFieldError("tag", "tag cannot be null");
	}
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
	boolean result = facade.removeTag(tag, sessionId);
	
	if (!result) {
	  addActionError(String.format("The tag %s is not in tag list", tag));
	  return ERROR;
	}

	return SUCCESS;
  }
}
