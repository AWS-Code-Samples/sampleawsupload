import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudfront.AmazonCloudFrontClient;
import com.amazonaws.services.cloudfront.model.CreateStreamingDistributionRequest;
import com.amazonaws.services.cloudfront.model.StreamingDistributionConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;


public class streamingdistribution {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		  String accessKey = "AKIAI76EYRNDSVQ5TA6A";
			String secretKey = "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";

		AWSCredentials credentials = null;
		
			 // String cloudFrontDesc = "";
			   //  String cloudFrontS3Origin= "aws";
			  //  long cloudFrontMinTTL=30;
			
		 credentials = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3 s3 = new AmazonS3Client(credentials);
		 AmazonCloudFrontClient cloudfront = new AmazonCloudFrontClient(credentials);
		    //cloudfront.setEndpoint(endPoint);
		   // cloudfront.setRegion(region);
		 System.out.println(cloudfront);
		StreamingDistributionConfig streamingDistributionConfig = new StreamingDistributionConfig();
		//include the with parameters 
		CreateStreamingDistributionRequest streamingDistribution = new CreateStreamingDistributionRequest()
		        .withStreamingDistributionConfig(streamingDistributionConfig);          
		        cloudfront.createStreamingDistribution(streamingDistribution);
		        System.out.println(streamingDistribution);
		        

	}

}
