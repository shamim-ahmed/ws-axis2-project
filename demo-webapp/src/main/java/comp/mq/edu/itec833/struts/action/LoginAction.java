package comp.mq.edu.itec833.struts.action;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with login functionality
 * @author Poopak Alaeifar
 *
 */
public class LoginAction extends ActionSupport implements SessionAware {
  private static final long serialVersionUID = 1L;
  private String userName;
  private String password;
  
  @SuppressWarnings("rawtypes")
  private Map sessionMap;

  public String getUserName() {
	return userName;
  }

  public void setUserName(String userName) {
	this.userName = userName;
  }

  public String getPassword() {
	return password;
  }

  public void setPassword(String password) {
	this.password = password;
  }

  @SuppressWarnings("unchecked")
  @Override
  public String execute() {
	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	String sessionId = facade.login(userName, password);
	
	if (StringUtils.isBlank(sessionId)) {
	  return INPUT;
	}
	
	sessionMap.put("sessionId", sessionId);
	sessionMap.put("userName", userName);
	return SUCCESS;
  }
  
  @Override
  public void validate() {
	if (StringUtils.isBlank(userName)) {
	  addFieldError("userName", "Username cannot be null");
	}
	
	if (StringUtils.isBlank(password)) {
	  addFieldError("password", "Password cannot be null");
	}
  }

  @Override
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }
}
