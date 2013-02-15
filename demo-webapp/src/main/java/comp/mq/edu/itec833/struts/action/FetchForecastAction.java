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
 * The action that deals with fetch forecast functionality
 * @author Shamim Ahmed
 *
 */
public class FetchForecastAction extends ActionSupport implements SessionAware, RequestAware {
  private static final long serialVersionUID = 1L;
  private String city;
  private String country;
  
  @SuppressWarnings("rawtypes")
  private Map sessionMap;

  @SuppressWarnings("rawtypes")
  private Map requestMap;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
  
  @Override
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }

  @Override
  public void validate() {
	if (StringUtils.isBlank("city")) {
	  addFieldError("city", "city cannot be blank");
	}
	
	if (StringUtils.isBlank("country")) {
	  addFieldError("country", "country cannot be blank");
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
	String forecastXml = facade.getForecast(city, country, sessionId);

	if (StringUtils.isNotBlank(forecastXml)) {
	  String forecastHtml = XSLTUtil.transform(forecastXml, ContentType.FORECAST);
	  requestMap.put("forecastHtml", forecastHtml);
	}

	return SUCCESS;
  }

  @Override
  public void setRequest(@SuppressWarnings("rawtypes") Map requestMap) {
	this.requestMap = requestMap;
  }
}
