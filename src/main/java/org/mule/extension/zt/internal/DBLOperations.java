package org.mule.extension.zt.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Password;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.values.OfValues;

import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.api.meta.ExpressionSupport;
import com.ztensor.datacrypt.*;
import com.ztensor.util.json.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class DBLOperations {

	private static String versionTag = "v3.0.0";
	
  @MediaType(value = ANY, strict = false)
  @Alias("EncryptJson")
  public String encryptJson(@Config DBLConfiguration configuration,
		  @DisplayName("Sensitive Fields") @Expression(ExpressionSupport.SUPPORTED) String sensitiveFields,
		  @DisplayName("Sensitive JSON") @Expression(ExpressionSupport.SUPPORTED) String sensitiveJson,
		  @DisplayName("Tweak") @Expression(ExpressionSupport.SUPPORTED) String tweak, 
  		  @DisplayName("OverRide Token") 
  		  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Optional(defaultValue = "NOTOKEN")
  		  @Placement(order = 1, tab="Advanced") String overRideToken, 
  		  @DisplayName("Pass Phrase") 
  	  	  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Password 
  		  @Optional(defaultValue = "NOPASSPHRASE")
  		  @Placement(order = 2, tab="Advanced") String passPhrase) {
    String response = "OperationFailed";
	System.out.println(versionTag + " DataBlind EncryptJson" );    	

    try {    
        KeyContext kc = new KeyContext("CipherWorks", "Admin", "1.0", configuration.getEncryptionKey().getBytes());
    	JsonDataCrypt jsonDataCrypt = new JsonDataCrypt(kc);
    	response = jsonDataCrypt.transform( "Encrypt", tweak, sensitiveJson, sensitiveFields, overRideToken, passPhrase);
    }
    catch (Exception e) {
    	System.out.println("Excception in Connector " + e);
    	e.printStackTrace();
    }
    return response;
  }
  
  @MediaType(value = ANY, strict = false)
  @Alias("EncryptJsonUsingNLP")
  public String encryptJsonUsingNLP(@Config DBLConfiguration configuration,
		  @DisplayName("Sensitive JSON") @Expression(ExpressionSupport.SUPPORTED) String sensitiveJson,
		  @DisplayName("Tweak") @Expression(ExpressionSupport.SUPPORTED) String tweak, 
  		  @DisplayName("OverRide Token") 
  		  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Optional(defaultValue = "NOTOKEN")
  		  @Placement(order = 1, tab="Advanced") String overRideToken, 
  		  @DisplayName("Pass Phrase") 
  	  	  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Password 
  		  @Optional(defaultValue = "NOPASSPHRASE")
  		  @Placement(order = 2, tab="Advanced") String passPhrase) {
    String response = "OperationFailed";
	System.out.println(versionTag + " DataBlind EncryptJsonUsingNLP" );    	
    try {    
    	
       
        String jsonPayload = String.format(
        	    "{\n" +
        	    "  \"key\": \"%s\",\n" +
        	    "  \"tweak\": \"%s\",\n" +
        	    "  \"data\": %s,\n" +
        	    "  \"overRideToken\": \"%s\",\n" +
        	    "  \"overRidePassPhrase\": \"%s\"\n" +
        	    "}", configuration.getEncryptionKey(), tweak, sensitiveJson, overRideToken, passPhrase);
    	
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(configuration.getApiUri()))
            .header("Content-Type", "application/json")
            .header("x-api-key", configuration.getApiKey()) // If using API key
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        response = httpResponse.body();
    }
    catch (Exception e) {
    	System.out.println("Excception in Connector " + e);
    	e.printStackTrace();
    }
    return response;
  }

  @MediaType(value = ANY, strict = false)
  @Alias("reduceJson")
  public String filterJson(@Config DBLConfiguration configuration,
		  @DisplayName("Sensitive Fields") @Expression(ExpressionSupport.SUPPORTED) String sensitiveFields,
		  @DisplayName("Sensitive JSON") @Expression(ExpressionSupport.SUPPORTED) String sensitiveJson,
		  @DisplayName("Operation") @Expression(ExpressionSupport.SUPPORTED) String operation,
  		  @DisplayName("OverRide Token") 
  		  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Optional(defaultValue = "NOTOKEN")
  		  @Placement(order = 1, tab="Advanced") String overRideToken, 
  		  @DisplayName("Pass Phrase") 
  	  	  @Expression(ExpressionSupport.SUPPORTED) 
  		  @Password 
  		  @Optional(defaultValue = "NOPASSPHRASE")
  		  @Placement(order = 2, tab="Advanced") String passPhrase) {
    String response = "OperationFailed";
	System.out.println(versionTag + " DataBlind ReduceJson" );    	
    try {    
        KeyContext kc = new KeyContext("CipherWorks", "Admin", "1.0", configuration.getEncryptionKey().getBytes());
        JsonDataCrypt jsonDataCrypt = new JsonDataCrypt(kc);
    	response = jsonDataCrypt.reduceJson( operation, sensitiveJson, sensitiveFields, overRideToken, passPhrase);
    }
    catch (Exception e) {
    	System.out.println("Excception in Connector " + e);
    	e.printStackTrace();
    }
    return response;
  }

 @MediaType(value = ANY, strict = false)
  @Alias("DecryptJson")
  public String decryptJson(@Config DBLConfiguration configuration,
		  @DisplayName("Sensitive Fields") @Expression(ExpressionSupport.SUPPORTED) String sensitiveFields,
		  @DisplayName("Encrypted JSON") @Expression(ExpressionSupport.SUPPORTED) String encryptedJson,
		  @DisplayName("Tweak") @Expression(ExpressionSupport.SUPPORTED) String tweak, 
		  @DisplayName("OverRide Token") 
		  @Expression(ExpressionSupport.SUPPORTED) 
		  @Optional(defaultValue = "NOTOKEN")
		  @Placement(order = 1, tab="Advanced") String overRideToken, 
		  @DisplayName("Pass Phrase") 
	  	  @Expression(ExpressionSupport.SUPPORTED) 
		  @Password 
		  @Optional(defaultValue = "NOPASSPHRASE")
		  @Placement(order = 2, tab="Advanced") String passPhrase) {
    String response = "OperationFailed";
	System.out.println(versionTag + " DataBlind DecryptJson" );    	
    try {  
        KeyContext kc = new KeyContext("CipherWorks", "Admin", "1.0", configuration.getEncryptionKey().getBytes());
    	JsonDataCrypt jsonDataCrypt = new JsonDataCrypt(kc);
    	response = jsonDataCrypt.transform( "Decrypt", tweak, encryptedJson, sensitiveFields, overRideToken, passPhrase);
    }
    catch (Exception e) {
    	System.out.println("Excception in Connector " + e);
    	e.printStackTrace();
    }
    return response;
  }
 @MediaType(value = ANY, strict = false)
 @Alias("OverrideToken")
 public String overrideToken(@Config DBLConfiguration configuration,
		  @DisplayName("Passphrase") @Expression(ExpressionSupport.SUPPORTED) String passPhrase,
		  @DisplayName("Expiration Seconds") @Expression(ExpressionSupport.SUPPORTED) Integer expirationSecs) {
   String response = "OperationFailed";
   System.out.println(versionTag + " DataBlind OverrideToken" );    	
   try {    
       KeyContext kc = new KeyContext("CipherWorks", "Admin", "1.0", configuration.getEncryptionKey().getBytes());
       HmacToken HmacToken = new HmacToken();
   	   response = HmacToken.generateToken( kc, passPhrase, expirationSecs.intValue());
   }
   catch (Exception e) {
   	System.out.println("Excception in Connector " + e);
   	e.printStackTrace();
   }
   return response;
 }
 @MediaType(value = ANY, strict = false)
 @Alias("OverrideTokenWithNewKey")
 public String overrideTokenWithNewKey(
		  @DisplayName("Key") @Expression(ExpressionSupport.SUPPORTED) String key,
		  @DisplayName("Passphrase") @Expression(ExpressionSupport.SUPPORTED) String passPhrase,
		  @DisplayName("Expiration Seconds") @Expression(ExpressionSupport.SUPPORTED) Integer expirationSecs) {
   String response = "OperationFailed";
   System.out.println(versionTag + " DataBlind OverrideTokenWithNewKey" );    	
   try {    
       KeyContext kc = new KeyContext("CipherWorks", "Admin", "1.0", key.getBytes());
       HmacToken HmacToken = new HmacToken();
   	   response = HmacToken.generateToken( kc, passPhrase, expirationSecs.intValue());
   }
   catch (Exception e) {
   	System.out.println("Excception in Connector " + e);
   	e.printStackTrace();
   }
   return response;
 }

}
