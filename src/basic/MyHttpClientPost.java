package basic;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyHttpClientPost {
	

	
	public static void main(String[] args) {
		System.out.println("start");
		String testUrlString = "http://10.10.10.3:2375/containers/create?name=ubuntu14.04wjping13";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(testUrlString);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
//    	NameValuePair[] data = { new NameValuePair("containerName", "ubuntu14.04wjp"),
//				new NameValuePair("userId", "1"), //这个长度为4-20
//				new NameValuePair("cmd", "/bin/bash"),
//				new NameValuePair("imageName", "ubuntu:14.04")
//    	
//		};
    	String[] cmdarr ={"/bin/ping","localhost"};
    	ObjectMapper mapper = new ObjectMapper(); 
    	Map<Object, Object> paraMap=new HashMap<>();
    	paraMap.put("Image", "ubuntu:14.04");
    	paraMap.put("cmd", cmdarr);
    	String paraString = null;
    	try {
    		paraString=mapper.writeValueAsString(paraMap);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
		postMethod.setRequestBody(paraString);
	;
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
