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
 * This class fetches content from Twitter API
 * @author Shamim Ahmed
 *
 */
public class TwitterSearchManager implements ContentManager {
  private static final TwitterSearchManager INSTANCE = new TwitterSearchManager();
  private static final String HTTP_PROTOCOL = "http";
  private static final String TWITTER_SERVER_NAME = "search.twitter.com";
  private static final String TWITTER_RELATIVE_PATH = "/search.atom";
  private static final String ENCODING = "UTF-8";
  
  public static TwitterSearchManager getInstance() {
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
   * constructs the URI that will be used for twitter search
   * @param tag the keyword
   * @param maxPerTag max number of items per tag
   * @return
   */
  private URI getTargetURI(String tag, int maxPerTag) {
    URI uri = null;
    
    // these are the parameters from twitter search API
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("q", tag));
    parameters.add(new BasicNameValuePair("rpp", Integer.toString(maxPerTag)));
    parameters.add(new BasicNameValuePair("result-type", "mixed"));
    parameters.add(new BasicNameValuePair("lang", "en"));
    
    try {
      uri = URIUtils.createURI(HTTP_PROTOCOL, TWITTER_SERVER_NAME, -1, TWITTER_RELATIVE_PATH, URLEncodedUtils.format(parameters, ENCODING) , null);
    } catch (URISyntaxException ex) {
      System.err.println(ex);
    }
     
    return uri;
  }
  
  // private constructor to prevent instantiation
  private TwitterSearchManager() {
  }
}
