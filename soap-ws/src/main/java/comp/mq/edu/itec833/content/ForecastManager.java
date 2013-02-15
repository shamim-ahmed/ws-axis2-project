package comp.mq.edu.itec833.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import comp.mq.edu.itec833.manager.HttpManager;
import comp.mq.edu.itec833.util.XMLUtil;

/**
 * This class fetches weather forecast from external forecast provider (GlobalWeather)
 * @author Shamim Ahmed
 *
 */
public class ForecastManager {
  private static final String WEATHER_SERVICE_URL = "http://www.webservicex.com/globalweather.asmx";
  private static final Namespace SOAP_NAMESPACE = Namespace.getNamespace("soap",
	  "http://schemas.xmlsoap.org/soap/envelope/");
  private static final Namespace XSI_NAMESPACE = Namespace.getNamespace("xsi",
	  "http://www.w3.org/2001/XMLSchema-instance");
  private static final Namespace XSD_NAMESPACE = Namespace.getNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
  private static final Namespace WEATHER_NAMESPACE = Namespace.getNamespace("http://www.webserviceX.NET");
  private static final Namespace WEATHER_NAMESPACE_WITH_PREFIX = Namespace.getNamespace("w",
	  "http://www.webserviceX.NET");

  private static final int HTTP_OK = 200;
  private static final ForecastManager INSTANCE = new ForecastManager();

  public static ForecastManager getInstance() {
	return INSTANCE;
  }

  /**
   * retrieves the weather forecast for a particular city and country
   * @param city the city
   * @param country the country
   * @return the XML representing the forecast
   */
  public String getForecast(String city, String country) {
	String forecast = "";

	if (StringUtils.isBlank(city) || StringUtils.isBlank(country)) {
	  return forecast;
	}

	String soapRequest = getSoapRequest(city, country);
	String soapResponse = getSoapResponse(soapRequest);
	forecast = parseSoapResponse(soapResponse);

	return forecast;
  }

  /**
   * constructs the SOAP message to be sent to the external service 
   * @param city the city 
   * @param country the country
   * @return the XML representing the SOAP message
   */
  private String getSoapRequest(String city, String country) {
	String soapRequestXml = "";

	Document document = new Document();
	Element rootElement = new Element("Envelope", SOAP_NAMESPACE);
	rootElement.addNamespaceDeclaration(XSI_NAMESPACE);
	rootElement.addNamespaceDeclaration(XSD_NAMESPACE);
	document.setRootElement(rootElement);

	Element bodyElement = new Element("Body", SOAP_NAMESPACE);
	rootElement.addContent(bodyElement);

	Element weatherElement = new Element("GetWeather", WEATHER_NAMESPACE);
	bodyElement.addContent(weatherElement);

	Element cityElement = new Element("CityName", WEATHER_NAMESPACE);
	cityElement.setText(city);
	Element countryElement = new Element("CountryName", WEATHER_NAMESPACE);
	countryElement.setText(country);

	weatherElement.addContent(cityElement);
	weatherElement.addContent(countryElement);
	 ByteArrayOutputStream out = null;
	 
	try {
	  XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
	  out = new ByteArrayOutputStream();
	  outputter.output(document, out);
	  soapRequestXml = out.toString();
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(out);
	}

	return soapRequestXml;
  }

  /**
   * sends the SOAP request and returns the response
   * @param soapXml the SOAP xml
   * @return the XML representing the SOAP response
   */
  private String getSoapResponse(String soapXml) {
	String result = "";

	if (StringUtils.isBlank(soapXml)) {
	  return result;
	}

	HttpClient client = new DefaultHttpClient();
	HttpPost post = new HttpPost(WEATHER_SERVICE_URL);
	post.addHeader("Content-type", "text/xml; charset=utf-8");
	post.addHeader("SOAPAction", "http://www.webserviceX.NET/GetWeather");

	HttpEntity entity = null;
	
	try {
	  entity = new StringEntity(soapXml);
	  post.setEntity(entity);

	  HttpResponse response = client.execute(post);
	  StatusLine line = response.getStatusLine();

	  if (line.getStatusCode() == HTTP_OK) {
		HttpEntity resEntity = response.getEntity();
		result = EntityUtils.toString(resEntity);
	  }
	} catch (UnsupportedEncodingException ex) {
	  System.err.println(ex);
	} catch (ClientProtocolException ex) {
	  System.err.println(ex);
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  HttpManager.consumeEntity(entity);
	}

	return result;
  }

  /**
   * extracts weather forecast from the SOAP message body
   * @param soapResponse the XML representing the SOAP response message
   * @return the weather forecast XML
   */
  private String parseSoapResponse(String soapResponse) {
	String forecast = "";
	InputStream in = null;
	
	try {
	  in = new ByteArrayInputStream(soapResponse.getBytes());
	  SAXBuilder builder = new SAXBuilder();
	  Document document = builder.build(in);
	  forecast = XMLUtil.getValue("/soap:Envelope/soap:Body/w:GetWeatherResponse/w:GetWeatherResult",
		                          document.getRootElement(), 
		                          SOAP_NAMESPACE, XSI_NAMESPACE, XSD_NAMESPACE, WEATHER_NAMESPACE_WITH_PREFIX);
	  forecast = forecast.replaceAll("encoding=\"utf-16\"", "encoding=\"utf-8\"");
	} catch (JDOMException ex) {
	  System.err.println(ex);
	} catch (IOException ex) {
	  System.err.println(ex);
	} finally {
	  IOUtils.closeQuietly(in);
	}

	return forecast;
  }
  
  // private constructor to prevent instantiation
  private ForecastManager() {
  }
}
