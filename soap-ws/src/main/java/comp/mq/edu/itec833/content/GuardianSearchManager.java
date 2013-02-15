package comp.mq.edu.itec833.content;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import comp.mq.edu.itec833.manager.HttpManager;

/**
 * This class fetches content from the Guardian API
 * @author Shamim Ahmed
 *
 */
public class GuardianSearchManager implements ContentManager {
  private static final GuardianSearchManager INSTANCE = new GuardianSearchManager();
  private static final String GUARDIAN_SERVER_NAME = "content.guardianapis.com";
  private static final String GUARDIAN_RELATIVE_PATH = "/search";
  private static final String ENCODING = "UTF-8";
  private static final String HTTP_PROTOCOL = "http";  

  public static GuardianSearchManager getInstance() {
	return INSTANCE;
  }
  
  @Override
  public String search(String tag, int maxPerTag) {
	String result = "";
	
	if (StringUtils.isBlank(tag) || maxPerTag < 0) {
	  return result;
	}
	
	URI uri = getTargetURI(tag, maxPerTag);
	
	if (uri == null) {
	  return result;
	}
	
	result = HttpManager.getInstance().doGet(uri);
	return result;
  }
  
  /**
   * constructs the URI	for the search
   * @param tag the keyword
   * @param maxPerTag max number of items per tag
   * @return the URI that will be used to search content
   */
  private URI getTargetURI(String tag, int maxPerTag) {
    URI uri = null;
    
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("q", tag));
    parameters.add(new BasicNameValuePair("format", "xml"));
    parameters.add(new BasicNameValuePair("page-size", Integer.toString(maxPerTag)));
    
    try {
      uri = URIUtils.createURI(HTTP_PROTOCOL, GUARDIAN_SERVER_NAME, -1, GUARDIAN_RELATIVE_PATH, URLEncodedUtils.format(parameters, ENCODING) , null);
    } catch (URISyntaxException ex) {
      System.err.println(ex);
    }
     
    return uri;
  }
  
  // private constructor to prevent instantiation
  private GuardianSearchManager() {
  }
}
