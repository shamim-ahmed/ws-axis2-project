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
 * This class contains the logic of invoking the Flickr API
 * @author Poopak Alaeifar
 *
 */
public class FlickrSearchManager implements ContentManager {
  private static final FlickrSearchManager INSTANCE = new FlickrSearchManager();
  private static final String HTTP_PROTOCOL = "http";
  private static final String FLICKR_SERVER_NAME = "api.flickr.com";
  private static final String FLICKR_RELATIVE_PATH = "/services/rest";
  private static final String ENCODING = "UTF-8";
  private static final String FLICKR_API_KEY = "9f47d14d2bd1e3e9141d7f2948f4a189";

  public static FlickrSearchManager getInstance() {
	return INSTANCE;
  }

  @Override
  public String search(String tag, int maxPerTag) {
	String result = "";

	if (StringUtils.isBlank(tag) || maxPerTag < 0) {
	  return result;
	}

	URI uri = getPhotoSearchURI(tag, maxPerTag);

	if (uri == null) {
	  return result;
	}

	result = HttpManager.getInstance().doGet(uri);
	return result;
  }

  /**
   * This method invokes a search for user information, given the user ID
   * @param userId the user Id
   * @return the XML containing user information
   */
  public String userSearch(String userId) {
	String result = "";

	if (StringUtils.isBlank(userId)) {
	  return result;
	}

	URI uri = getUserSearchURI(userId);

	if (uri == null) {
	  return result;
	}

	result = HttpManager.getInstance().doGet(uri);
	return result;
  }

  /**
   * constructs the URI for photo search
   * @param tag the keyword
   * @param maxPerTag number of items in the result
   * @return the URI that will be used to invoke the search
   */
  private URI getPhotoSearchURI(String tag, int maxPerTag) {
	URI uri = null;

	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	parameters.add(new BasicNameValuePair("format", "rest"));
	parameters.add(new BasicNameValuePair("method", "flickr.photos.search"));
	parameters.add(new BasicNameValuePair("tags", tag));
	parameters.add(new BasicNameValuePair("api_key", FLICKR_API_KEY));
	parameters.add(new BasicNameValuePair("per_page", Integer.toString(maxPerTag)));

	try {
	  uri = URIUtils.createURI(HTTP_PROTOCOL, FLICKR_SERVER_NAME, -1, FLICKR_RELATIVE_PATH,
		  URLEncodedUtils.format(parameters, ENCODING), null);
	} catch (URISyntaxException ex) {
	  System.err.println(ex);
	}

	return uri;
  }

  /**
   * constructs the URI for user search 
   * @param userId the user id
   * @return the URI that will be used for searching user info
   */
  private URI getUserSearchURI(String userId) {
	URI uri = null;

	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
	parameters.add(new BasicNameValuePair("format", "rest"));
	parameters.add(new BasicNameValuePair("method", "flickr.people.getInfo"));
	parameters.add(new BasicNameValuePair("api_key", FLICKR_API_KEY));
	parameters.add(new BasicNameValuePair("user_id", userId));

	try {
	  uri = URIUtils.createURI(HTTP_PROTOCOL, FLICKR_SERVER_NAME, -1, FLICKR_RELATIVE_PATH,
		  URLEncodedUtils.format(parameters, ENCODING), null);
	} catch (URISyntaxException ex) {
	  System.err.println(ex);
	}

	return uri;
  }

  // private constructor to prevent instantiation
  private FlickrSearchManager() {
  }
}
