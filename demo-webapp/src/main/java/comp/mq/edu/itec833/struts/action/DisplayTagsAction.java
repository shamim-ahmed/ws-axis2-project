package comp.mq.edu.itec833.struts.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.opensymphony.xwork2.ActionSupport;
import comp.mq.edu.itec833.struts.action.util.PortalFacade;

/**
 * The action that deals with display tag functionality
 * @author Shamim Ahmed
 *
 */
public class DisplayTagsAction extends ActionSupport implements SessionAware, RequestAware {
  private static final long serialVersionUID = 1L;
  
  @SuppressWarnings("rawtypes")
  private Map sessionMap;
  
  @SuppressWarnings("rawtypes")
  private Map requestMap;
  
  @Override
  public void setSession(@SuppressWarnings("rawtypes") Map sessionMap) {
	this.sessionMap = sessionMap;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public String execute() {
	String sessionId = (String) sessionMap.get("sessionId");
	
	if (StringUtils.isBlank(sessionId)) {
	  addActionError("User is not logged in");
	  return ERROR;
	}
	
	ServletContext appContext = ServletActionContext.getServletContext();
	PortalFacade facade = (PortalFacade) appContext.getAttribute("portalFacade");
	String tagXml = facade.getTags(sessionId);
	
	if (StringUtils.isNotBlank(tagXml)) {
	  requestMap.put("tagList", getTagList(tagXml));
	}
	
	return SUCCESS; 
  }
  
  private List<String> getTagList(String tagXml) {
	if (StringUtils.isBlank(tagXml)) {
	  return Collections.emptyList();
	}
	
	List<String> tagList = new ArrayList<String>();
	SAXBuilder builder = new SAXBuilder();
	ByteArrayInputStream in = null;
	
	try {
	  in = new ByteArrayInputStream(tagXml.getBytes());	
	  Document document = builder.build(in);
	  Element rootElement = document.getRootElement();
	  
	  @SuppressWarnings("unchecked")
      List<Element> childList = rootElement.getChildren("tag");
	  
	  for (Element elem : childList) {
		tagList.add(elem.getText());
	  }	  
	} catch (IOException ex) {
	  System.err.println(ex);
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	}
	
	return tagList;
  }

  @Override
  public void setRequest(@SuppressWarnings("rawtypes") Map requestMap) {
	this.requestMap = requestMap;
  } 
}
