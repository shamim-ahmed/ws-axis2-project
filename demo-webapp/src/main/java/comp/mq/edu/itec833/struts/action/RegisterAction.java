package comp.mq.edu.itec833.struts.action;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with registration functionality
 * @author Shamim Ahmed
 *
 */
public class RegisterAction extends ActionSupport {
  private static final long serialVersionUID = 1L;
  private String userName;
  private String password;
  
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
  
  @Override
  public void validate() {
	if (StringUtils.isBlank(userName)) {
	  addFieldError("userName", "userName cannot be null");
	}
	
	if (StringUtils.isBlank(password)) {
	  addFieldError("password", "password cannot be null");
	}
  }
  
  @Override
  public String execute() {
	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	boolean result = facade.register(userName, password);
	
	if (!result) {
	  addActionError("Registration failed");
	  return ERROR;
	}
	
	return SUCCESS;
  }
}
