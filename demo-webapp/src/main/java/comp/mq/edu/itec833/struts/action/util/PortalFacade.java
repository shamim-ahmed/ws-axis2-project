package comp.mq.edu.itec833.struts.action.util;

import java.util.Iterator;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.lang.StringUtils;

/**
 * The class that invokes the web service
 * @author Shamim Ahmed
 *
 */
public class PortalFacade {
  private static final String[] EMPTY_ARRAY = {};
  private String serviceUri;
  private String configPath;

  /**
   * class constructor
   * @param serviceUri the URI of the web service
   * @param configPath the path to the axis2 configuration file
   */
  public PortalFacade(String serviceUri, String configPath) {
	if (StringUtils.isBlank(serviceUri) || StringUtils.isBlank(configPath)) {
	  throw new IllegalArgumentException("service URI or config path cannot be blank");
	}

	this.serviceUri = serviceUri;
	this.configPath = configPath;
  }

  public String getServiceUri() {
	return serviceUri;
  }

  public String getConfigPath() {
	return configPath;
  }

  public boolean register(String userName, String password) {
	String result = invoke("register", null, new String[] { userName, password });
	return Boolean.valueOf(result);
  }

  public String login(String userName, String password) {
	return invoke("login", null, new String[] { userName, password });
  }

  public boolean logout(String sessionId) {
	String result = invoke("logout", sessionId, EMPTY_ARRAY);
	return Boolean.valueOf(result);
  }

  public boolean addTag(String tag, String sessionId) {
	String result = invoke("addTag", sessionId, new String[] { tag });
	return Boolean.valueOf(result);
  }

  public boolean removeTag(String tag, String sessionId) {
	String result = invoke("removeTag", sessionId, new String[] { tag });
	return Boolean.valueOf(result);
  }

  public String getTags(String sessionId) {
	return invoke("getTags", sessionId, EMPTY_ARRAY);
  }

  public String getTweets(int max, String sessionId) {
	return invoke("getTweets", sessionId, new String[] { Integer.toString(max) });
  }

  public String getVideos(int max, String sessionId) {
	return invoke("getVideos", sessionId, new String[] { Integer.toString(max) });
  }

  public String getNews(int max, String sessionId) {
	return invoke("getNews", sessionId, new String[] { Integer.toString(max) });
  }

  public String getPhotos(int max, String sessionId) {
	return invoke("getPhotos", sessionId, new String[] { Integer.toString(max) });
  }

  public String getForecast(String city, String country, String sessionId) {
	return invoke("getForecast", sessionId, new String[] { city, country });
  }

  private String invoke(String methodName, String sessionId, String[] args) {
	String result = "";

	try {
	  ConfigurationContext ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(configPath,
		  configPath + "/conf/axis2.xml");
	  ServiceClient client = new ServiceClient(ctx, null);
	  Options options = new Options();
	  options.setAction("urn:" + methodName);
	  options.setTo(new EndpointReference(serviceUri));
	  client.setOptions(options);

	  if (StringUtils.isNotBlank(sessionId)) {
		client.addHeader(getHeader(sessionId));
	  }

	  OMElement response = client.sendReceive(getPayload(methodName, args));
	  @SuppressWarnings("rawtypes")
	  Iterator iter = response.getChildElements();

	  if (iter.hasNext()) {
		result = ((OMElement) iter.next()).getText();
	  }

	  System.out.println(result);
	} catch (AxisFault ex) {
	  System.err.println(ex);
	  ex.printStackTrace();
	}

	return result;
  }

  private OMElement getPayload(String methodName, String[] args) {
	OMFactory factory = OMAbstractFactory.getOMFactory();
	OMNamespace namespace = factory.createOMNamespace("http://itec833.edu.mq.comp", "ns1");
	OMElement element = factory.createOMElement(methodName, namespace);

	for (int i = 0; i < args.length; i++) {
	  OMElement param = factory.createOMElement("param" + i, null);
	  param.setText(args[i]);
	  element.addChild(param);
	}

	return element;
  }

  private OMElement getHeader(String sessionId) {
	OMFactory factory = OMAbstractFactory.getOMFactory();
	OMNamespace namespace = factory.createOMNamespace("http://itec833.edu.mq.comp", "ns1");
	OMElement header = factory.createOMElement("sessionId", namespace);
	header.setText(sessionId);
	return header;
  }
}
