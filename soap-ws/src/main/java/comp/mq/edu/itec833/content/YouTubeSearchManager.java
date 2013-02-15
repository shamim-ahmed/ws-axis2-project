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
 * This class fetches content from the YouTube API
 * @author Poopak Alaeifar
 *
 */
public class YouTubeSearchManager implements ContentManager {
  private static final YouTubeSearchManager INSTANCE = new YouTubeSearchManager();
  private static final String HTTP_PROTOCOL = "http";
  private static final String YOUTUBE_SERVER_NAME = "gdata.youtube.com";
  private static final String YOUTUBE_RELATIVE_PATH = "/feeds/api/videos";
  private static final String ENCODING = "UTF-8";
  
  public static YouTubeSearchManager getInstance() {
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
   * constructs the URI for YouTube API search
   * @param tag the keyword
   * @param maxPerTag max number of items per tag
   * @return the URI that is used to initiate the search
   */
  private URI getTargetURI(String tag, int maxPerTag) {
    URI uri = null;
    
    List<NameValuePair> parameters = new ArrayList<NameValuePair>();
    parameters.add(new BasicNameValuePair("q", tag));    
    parameters.add(new BasicNameValuePair("orderby", "published"));
    parameters.add(new BasicNameValuePair("start-index", Integer.toString(1)));
    parameters.add(new BasicNameValuePair("max-results", Integer.toString(maxPerTag)));
    
    try {
      uri = URIUtils.createURI(HTTP_PROTOCOL, YOUTUBE_SERVER_NAME, -1, YOUTUBE_RELATIVE_PATH, URLEncodedUtils.format(parameters, ENCODING) , null);
    } catch (URISyntaxException ex) {
      System.err.println(ex);
    }
     
    return uri;
  }
  
  // private constructor to prevent instantiation
  private YouTubeSearchManager() {
  }
}
