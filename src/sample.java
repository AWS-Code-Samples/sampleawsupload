/*import java.io.FileInputStream;
import java.security.Security;

import org.jets3t.service.utils.ServiceUtils;



import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;






public class sample {

    public static void main(String[] args) throws Exception {
        String existingBucketName = "*** Provide existing bucket name ***";
        String keyName            = "*** Provide object key ***";
        String filePath           = "*** Path to and name of the file to upload ***";  
        String accessKey = "AKIAI76EYRNDSVQ5TA6A";
		String secretKey = "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";
		
		AWSCredentials credentials = null;
		try{
			  String cloudFrontDesc = "";
			     String cloudFrontS3Origin= "aws";
			  //  long cloudFrontMinTTL=30;
			
		 credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3 s3 = new AmazonS3Client(credentials);
		 AmazonCloudFrontClient cloudfront = new AmazonCloudFrontClient(credentials);
		    //cloudfront.setEndpoint(endPoint);
		   // cloudfront.setRegion(region);
		 System.out.println(cloudfront);

		    DistributionConfig dc = new DistributionConfig();
		    dc.withCallerReference(System.currentTimeMillis() + "");
		    dc.withAliases(new Aliases().withQuantity(0));
		    dc.withDefaultRootObject("");
		 
		dc.withOrigins(new Origins().withItems(
		      new Origin().withId(cloudFrontS3Origin).withDomainName(cloudFrontS3Origin+ ".s3.amazonaws.com").withS3OriginConfig(new S3OriginConfig().withOriginAccessIdentity("")))
		      .withQuantity(1));
		    Long cloudFrontMinTTL=(long) 0;
			dc.withDefaultCacheBehavior(new DefaultCacheBehavior()
		      .withTargetOriginId(cloudFrontS3Origin)
		      .withForwardedValues(new ForwardedValues().withQueryString(false).withCookies(new CookiePreference().withForward("none")))
		      .withTrustedSigners(new TrustedSigners().withQuantity(0).withEnabled(false))
		      .withViewerProtocolPolicy(ViewerProtocolPolicy.AllowAll)
		      .withMinTTL(cloudFrontMinTTL));
		    dc.withCacheBehaviors(new CacheBehaviors().withQuantity(0));
		    dc.withComment(cloudFrontDesc);
		    dc.withLogging(new LoggingConfig().withEnabled(false).withBucket("").withPrefix("").withIncludeCookies(false));
		    dc.withEnabled(true);
		    dc.withPriceClass(PriceClass.PriceClass_All);

		    CreateDistributionRequest cdr = new CreateDistributionRequest().withDistributionConfig(dc);

		    CreateDistributionResult distribution = cloudfront.createDistribution(cdr);
		    System.out.println(distribution);

		    boolean isWait = true;
		    while (isWait) {
		   //  Thread.sleep(5000);
		     GetDistributionResult gdr = cloudfront.getDistribution(new GetDistributionRequest(distribution.getDistribution().getId()));
		     String status = gdr.getDistribution().getStatus();
		 System.out.println("Status :" + status);
		     if (status.equals("Deployed")) {
		      isWait = false;
		   System.out.println("Domain Name :" + gdr.getDistribution().getDomainName());
		     }
		  
		    }

		 // Signed URLs for a private distribution
		 // Note that Java only supports SSL certificates in DER format, 
		 // so you will need to convert your PEM-formatted file to DER format. 
		 // To do this, you can use openssl:
		 // openssl pkcs8 -topk8 -nocrypt -in origin.pem -inform PEM -out new.der 
//		     -outform DER 
		 // So the encoder works correctly, you should also add the bouncy castle jar
		 // to your project and then add the provider.

		 Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		 String distributionDomain = "d1pkamsyolbwbo.cloudfront.net";
		 String privateKeyFilePath = "/path/to/rsa-private-key.der";
		 String s3ObjectKey = "s3/object/key.txt";
		 String policyResourcePath = "http://" + distributionDomain + "/" + s3ObjectKey;
		 
		Object keyPairId="";
		 
		 // Convert your DER file into a byte array.

		 byte[] derPrivateKey = ServiceUtils.readInputStreamToBytes(new FileInputStream(privateKeyFilePath));

		 // Generate a "canned" signed URL to allow access to a 
		 // specific distribution and object

		 String signedUrlCanned = CloudFrontService.signUrlCanned(
		     "http://" + distributionDomain + "/" + s3ObjectKey, // Resource URL or Path
		     keyPairId,     // Certificate identifier, 
		                    // an active trusted signer for the distribution
		     derPrivateKey, // DER Private key data
		     ServiceUtils.parseIso8601Date("2011-11-14T22:20:00.000Z") // DateLessThan
		     );
		 System.out.println(signedUrlCanned);

		 // Build a policy document to define custom restrictions for a signed URL.

		 String policy = CloudFrontService.buildPolicyForSignedUrl(
		     // Resource path (optional, may include '*' and '?' wildcards)
		     policyResourcePath, 
		     // DateLessThan
		     ServiceUtils.parseIso8601Date("2011-11-14T22:20:00.000Z"), 
		     // CIDR IP address restriction (optional, 0.0.0.0/0 means everyone)
		     "0.0.0.0/0", 
		     // DateGreaterThan (optional)
		     ServiceUtils.parseIso8601Date("2011-10-16T06:31:56.000Z")
		     );

		 // Generate a signed URL using a custom policy document.

		 String signedUrl = CloudFrontService.signUrl(
		     // Resource URL or Path
		     "http://" + distributionDomain + "/" + s3ObjectKey, 
		     // Certificate identifier, an active trusted signer for the distribution
		     keyPairId,     
		     // DER Private key data
		     derPrivateKey, 
		     // Access control policy
		     policy 
		     );
		 System.out.println(signedUrl);
		    
		    
		  } catch (Exception e) {
		   e.printStackTrace();
		   System.exit(0);
		  }
		 }
}*/