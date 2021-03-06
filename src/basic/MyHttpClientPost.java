package basic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyHttpClientPost {
	

	
	public static void main(String[] args) {
		System.out.println("start");
		String testUrlString = "http://166.111.143.224:8080/docker_new/api/image";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(testUrlString);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
    	NameValuePair[] data = { new NameValuePair("dockerFilePath", "file1.txt"),
				new NameValuePair("repertory", "xiaoping"), //这个长度为4-20
				new NameValuePair("userId", "1"),
				new NameValuePair("imageName", "ubuntu"),
				new NameValuePair("imageTag", "xp1"),
				new NameValuePair("isPublic", "1")
    	
		};
    	
    	
		postMethod.setRequestBody(data);
	
		try {
			httpClient.executeMethod(postMethod);
			String text = postMethod.getResponseBodyAsString();
			int StatusCode= postMethod.getStatusCode();
			System.out.println("StatusCode"+StatusCode);
			System.out.println("response:" + text);
			System.out.println("headers:");
			for(int i=0;i<postMethod.getResponseHeaders().length;i++)
				System.out.println(postMethod.getResponseHeaders()[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}
	
	
	
}
