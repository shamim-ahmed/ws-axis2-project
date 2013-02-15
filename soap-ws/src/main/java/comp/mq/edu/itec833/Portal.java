package comp.mq.edu.itec833;

import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPHeader;
import org.apache.axis2.context.MessageContext;

import comp.mq.edu.itec833.content.ForecastManager;
import comp.mq.edu.itec833.manager.AuthenticationManager;
import comp.mq.edu.itec833.manager.SearchManager;
import comp.mq.edu.itec833.manager.SessionManager;
import comp.mq.edu.itec833.manager.TagManager;

/**
 * The class from which the WSDL is generated
 * This is the class that is registered in Axis2 configuration file along with its methods.
 * @author Shamim Ahmed
 *
 */
public class Portal {
  private static final int MAX_NO_OF_ITEMS = 100;
  private AuthenticationManager authenticationManager = AuthenticationManager.getInstance();
  private SearchManager searchManager = SearchManager.getInstance();
  private TagManager tagManager = TagManager.getInstance();
  private ForecastManager forecastManager = ForecastManager.getInstance();
  
  /**
   * Performs registration
   * @param userName the username
   * @param password the password
   * @return true if registration is successful, false otherwise
   */
  public boolean register(String userName, String password) {
	return authenticationManager.register(userName, password);
  }

  /**
   * performs login
   * @param userName the username
   * @param password the password
   * @return true if login operation is successful, false otherwise
   */
  public String login(String userName, String password) {
	return authenticationManager.login(userName, password);
  }
  
  /**
   * performs logout
   * @return true if logout succeeds, false otherwise
   */
  public boolean logout() {
	String sessionId = getSessionIdFromHeader();
	return authenticationManager.logout(sessionId);
  }
  
  /**
   * adds a tag to the profile of the currently logged in user 
   * @param tag the keyword to be added
   * @return true if the operation is successful, false otherwise
   */
  public boolean addTag(String tag) {
	String sessionId = getSessionIdFromHeader();
	return tagManager.addTag(tag, sessionId);
  }
  
  /**
   * removes a tag from the profile of the currently logged in user
   * @param tag the keyword to be removed
   * @return true if the operation is successful, false otherwise
   */
  public boolean removeTag(String tag) {
	String sessionId = getSessionIdFromHeader();
	return tagManager.removeTag(tag, sessionId);
  }
  
  /**
   * returns the XML containing all the tags of the currently logged in user
   * @return an XML string
   */
  public String getTags() {
	String sessionId = getSessionIdFromHeader();
	return tagManager.getTagsAsXml(sessionId);
  }
  
  /**
   * returns the XML representing the tweet search result.
   * @param maxPerTag max number of tweets per tag
   * @return an XML string
   */
  public String getTweets(int maxPerTag) {
	String sessionId = getSessionIdFromHeader();
	return searchManager.getTweets(validate(maxPerTag), sessionId);
  }
  
  /**
   * returns the XML representing the video search result.
   * @param maxPerTag max number of videos per tag
   * @return an XML string
   */
  public String getVideos(int maxPerTag) {
	String sessionId = getSessionIdFromHeader();
	return searchManager.getVideos(validate(maxPerTag), sessionId);
  }
  
  /**
   * returns the XML representing the news search result.
   * @param maxPerTag max number of news per tag
   * @return an XML string
   */
  public String getNews(int maxPerTag) {
	String sessionId = getSessionIdFromHeader();
	return searchManager.getNews(validate(maxPerTag), sessionId);
  }
  
  /**
   * returns the XML representing the photo search result.
   * @param maxPerTag max number of photos per tag
   * @return an XML string
   */
  public String getPhotos(int maxPerTag) {
	String sessionId = getSessionIdFromHeader();
	return searchManager.getPhotos(validate(maxPerTag), sessionId);
  }
  
  
  /**
   * Returns the XML representing the forecast
   * @param city the city
   * @param country the country
   * @return an XML string
   */
  public String getForecast(String city, String country) {
	String sessionId = getSessionIdFromHeader();
	int userId = SessionManager.getInstance().getUserId(sessionId);

	if (userId == -1) {
	  return "";
	}
	
	return forecastManager.getForecast(city, country);
  }
  
  /**
   * extracts the session id from the SOAP header
   * @return the extracted session id, or an empty string if no such id exists
   */
  private String getSessionIdFromHeader() {
	String sessionId = "";
	MessageContext context = MessageContext.getCurrentMessageContext();
	SOAPEnvelope envelope = context.getEnvelope();
	SOAPHeader header = envelope.getHeader();
	@SuppressWarnings("unchecked")
	Iterator<OMElement> iter = header.getChildrenWithName(new QName("http://itec833.edu.mq.comp", "sessionId", "ns1"));
	
	if (iter.hasNext()) {
	  OMElement element = iter.next();
	  sessionId = element.getText();
	}
	
	return sessionId;
  }
  
  /**
   * Performs sanity check on the input
   * @param maxPerTag max no of items per tag
   * @return
   */
  private int validate(int maxPerTag) {
	return maxPerTag <= MAX_NO_OF_ITEMS ? maxPerTag : MAX_NO_OF_ITEMS;	
  }
}
