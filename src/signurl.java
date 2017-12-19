import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.util.Date;

import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.utils.ServiceUtils;

import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner;
import com.amazonaws.services.cloudfront.CloudFrontUrlSigner.Protocol;


public class signurl {   
 public static void main(String args[]) throws S3ServiceException, InvalidKeySpecException, IOException
 {
	 
	 String accessKey = "AKIAI76EYRNDSVQ5TA6A";
		String secretKey = "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";
	   AWSCredentials credentials1 = new AWSCredentials(accessKey, secretKey);
	    Date date=new Date();
	    System.out.println(date);
	    long oneDay = 1 * 24 * 60 * 60 * 1000;
	  /* String url=S3Service.createSignedGetUrl(
	    		"qliktestbucket1", "development/1/Single-Repository.mp4",  credentials1, new Timestamp(date.getTime()+oneDay));
	    System.out.println(url);*/
	    
	    String url=S3Service.createSignedGetUrl(
	    		"qliktestbucket1", "development/1/Single-Repository.mp4",  credentials1, new Timestamp(date.getTime()+oneDay));
	    
	    System.out.println(url);
	    
	    
	    
	    String distributionDomainName = "d1orud2kax86f5.cloudfront.net";   
		 // the private key you created in the  Management ConsAWSole
		 File cloudFrontPrivateKeyFile = new File("C:\\Users\\swapna.r\\Desktop\\pk-APKAJJGKLK7C7Q32DTVA.pem");
		 // the unique ID assigned to your CloudFront key pair in the console    
		 String cloudFrontKeyPairId = "EH14ZTU7117NO";   
		 Date expirationDate = new Date(System.currentTimeMillis() + 60 * 1000);
		  String path="development/1/Single-Repository.mp4";
		  
		 String signedUrl = CloudFrontUrlSigner.getSignedURLWithCannedPolicy(
				 Protocol.https,  
		          distributionDomainName, 
		          cloudFrontPrivateKeyFile,   
		          path, // the resource path to our content
		          cloudFrontKeyPairId, 
		          expirationDate);
		    
		    
		    System.out.println(signedUrl);
		    
		    
		    
		    
		    InputStream in = new URL(url ).openStream();
		    try {
		      InputStreamReader inR = new InputStreamReader( in );
		      BufferedReader buf = new BufferedReader( inR );
		      String line;
		      while ( ( line = buf.readLine() ) != null ) {
		       // System.out.println( line );
		      }
		      
		      
		      
		   
			    //cloudfront.setEndpoint(endPoint);
			   // cloudfront.setRegion(region);
			// System.out.println(cloudfront);
		    } finally {
		      in.close();
		    }

	 
	    
	   
	    
   }
}
