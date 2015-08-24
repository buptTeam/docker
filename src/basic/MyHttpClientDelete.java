package basic;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class MyHttpClientDelete {
	public static void main(String[] args) {
		System.out.println("start");
		String testUrlString = "http://166.111.143.224:8080/docker_new/container/1";
		HttpClient httpClient = new HttpClient();
		DeleteMethod postMethod = new DeleteMethod(testUrlString);
		try {
			httpClient.executeMethod(postMethod);
			String text = postMethod.getResponseBodyAsString();
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
