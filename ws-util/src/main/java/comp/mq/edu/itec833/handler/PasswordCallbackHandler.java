package comp.mq.edu.itec833.handler;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import java.io.IOException;

/**
 * An axis2 specific class that is used for security related features 
 * @author Shamim Ahmed
 *
 */
public class PasswordCallbackHandler implements CallbackHandler {
  private static final String CLIENT_ID = "client";
  private static final String CLIENT_PASSWORD = "apache";
  private static final String SERVICE_ID = "service";
  private static final String SERVICE_PASSWORD = "apache";

  @Override
  public void handle(Callback[] cbArray)throws IOException, UnsupportedCallbackException {
	for (int i = 0; i < cbArray.length; i++) {
	  WSPasswordCallback callback = (WSPasswordCallback) cbArray[i];

	  @SuppressWarnings("deprecation")
	  String identifier = callback.getIdentifer();
	  
	  if (CLIENT_ID.equals(identifier)) {
		callback.setPassword(CLIENT_PASSWORD);
	  } else if (SERVICE_ID.equals(identifier)) {
		callback.setPassword(SERVICE_PASSWORD);
	  }
	}
  }
}