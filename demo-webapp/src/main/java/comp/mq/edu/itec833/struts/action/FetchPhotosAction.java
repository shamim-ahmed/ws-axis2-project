package comp.mq.edu.itec833.struts.action;

import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import comp.mq.edu.itec833.struts.action.util.PortalFacade;
import comp.mq.edu.itec833.struts.action.util.XSLTUtil;
import comp.mq.edu.itec833.util.ContentType;

/**
 * The action that deals with fetch photo functionality
 * @author Poopak Alaeifar
 *
 */
public class FetchPhotosAction extends ActionSupport implements RequestAware, SessionAware {
  private static final long serialVersionUID = 1L;

  private int max;
  
  @SuppressWarnings("rawtypes")
  private Map sessionMap;
  
  @SuppressWarnings("rawtypes")
  private Map requestMap;

  @Override
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }
  
  @Override
  public void validate() {
	if (max <= 0) {
	  addFieldError("max", "max must be positive");
	}
  }

  @SuppressWarnings("unchecked")
  @Override
  public String execute() {
	String sessionId = (String) sessionMap.get("sessionId");

	if (StringUtils.isBlank(sessionId)) {
	  addActionError("user is not logged in");
	  return ERROR;
	}
	
	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	String photoXml = facade.getPhotos(max, sessionId);

	if (StringUtils.isNotBlank(photoXml)) {
	  String photoHtml = XSLTUtil.transform(photoXml, ContentType.PHOTO);
	  requestMap.put("photoHtml", photoHtml);
	}

	return SUCCESS;
  }

  @Override
  public void setRequest(@SuppressWarnings("rawtypes") Map requestMap) {
	this.requestMap = requestMap;
  }
}
