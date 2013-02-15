package comp.mq.edu.itec833.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class performs date conversion.
 * This is necessary because different external sources represent date in different ways.
 * This class converts them to a standard format that is understood by clients
 * 
 * @author Shamim Ahmed
 *
 */
public class DateConverter {
  private static final String TWEET_DATE_SOURCE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private static final String YOUTUBE_DATE_SOURCE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static final String GUARDIAN_DATE_SOURCE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
  private static final String TARGET_FORMAT = "dd-MM-yyyy HH:mm";
  private static final String GMT = "GMT";
  
  /**
   * 
   * @param sourceDateStr the input string representing the date
   * @param contentType the type of content
   * @return the converted date
   */
  public static String convertDate(String sourceDateStr, ContentType contentType) {
	String result = "";
	TimeZone gmtTimeZone = TimeZone.getTimeZone(GMT);
	DateFormat sourceFormat = null;
	
	switch (contentType) {
	  case TWEET:
		sourceFormat = new SimpleDateFormat(TWEET_DATE_SOURCE_FORMAT);
		break;

	  case VIDEO:
		sourceFormat = new SimpleDateFormat(YOUTUBE_DATE_SOURCE_FORMAT);
		break;

	  case NEWS:
		sourceFormat = new SimpleDateFormat(GUARDIAN_DATE_SOURCE_FORMAT);
		break;

	  default:
		throw new IllegalArgumentException(String.format("Unknown contentType : %s", contentType));
	}
		
	sourceFormat.setTimeZone(gmtTimeZone);
	Date date = null;
	
	try {
	  date = sourceFormat.parse(sourceDateStr);
	  DateFormat targetFormat = new SimpleDateFormat(TARGET_FORMAT);
	  result = targetFormat.format(date);
	} catch (ParseException ex) {
	  System.err.println(ex);
	}
	
	return result;
  }
  
  // private constructor to prevent instantiation
  private DateConverter() {
  }
}
