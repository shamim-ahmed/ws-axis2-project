package comp.mq.edu.itec833.manager;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * This class facilitates the sending/receiving of HTTP requests 
 * @author Shamim Ahmed
 *
 */
public class HttpManager {
  private static final int HTTP_OK = 200;
  private static final String HTTP_PROTOCOL = "http";
  private static final int HTTP_PORT = 80;
  private final HttpClient httpClient;
  
  private static final HttpManager INSTANCE = new HttpManager();
  
  /**
   * accessor method of the singleton object
   * @return the HttpManager instance
   */
  public static HttpManager getInstance() {
	return INSTANCE;
  }
  
  /**
   * Invokes a GET request on the given URI
   * @param uri the URI on which to perform the GET operation
   * @return the response string
   */
  public String doGet(URI uri) {
	String result = "";

	if (uri == null) {
	  return result;
	}

	HttpEntity entity = null;
	
	try {
	  HttpGet httpGet = new HttpGet(uri);
	  HttpResponse response = httpClient.execute(httpGet);
	  StatusLine statusLine = response.getStatusLine();

	  if (statusLine.getStatusCode() == HTTP_OK) {
		entity = response.getEntity();
		result = EntityUtils.toString(entity);
	  } else {
		System.err.printf("The status code is %s%n", statusLine.getStatusCode());
	  }
	} catch (ParseException ex) {
	  System.err.println(ex);
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  consumeEntity(entity);
	}

	return result;
  }
  
  /**
   * A utility method for properly deallocating resources held by HttpEntity objects
   * @param entity the HttpEntity object to be consumed
   */
  public static void consumeEntity(HttpEntity entity) {
	if (entity != null) {
	  try {
		entity.consumeContent();
	  } catch (IOException ex) {
		System.err.println("HttpEntity could not be consumed properly. This could lead to resource leak");
	  }
	}
  }

  // private constructor to prevent instantiation
  private HttpManager() {
	HttpParams params = new BasicHttpParams();
	SchemeRegistry registry = new SchemeRegistry();
	registry.register(new Scheme(HTTP_PROTOCOL, PlainSocketFactory.getSocketFactory(), HTTP_PORT));
	ClientConnectionManager cm = new ThreadSafeClientConnManager(params, registry);
	httpClient = new DefaultHttpClient(cm, params);
  }
}
