package basic;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class MyHttpClientGET {
	public static void main(String[] args) {
		System.out.println("start");
		String testUrlString = "http://10.10.10.3:2375/containers/f145ded020ed9bdf7ef30feeede2285c2be63f2ec151abfa15ffef27fd70e2de/json";
		HttpClient httpClient = new HttpClient();
		GetMethod postMethod = new GetMethod(testUrlString);
		// NameValuePair[] data = { new NameValuePair("name", "tt"),
		// new NameValuePair("password", "ttpwd"),
		// new NameValuePair("mail", "tt@tt.com")
		// };
		// postMethod.setRequestBody(data);
		try {
			httpClient.executeMethod(postMethod);
			String text = postMethod.getResponseBodyAsString();
			System.out.println("response:" + text);
			System.out.println("headers:");
			for (int i = 0; i < postMethod.getResponseHeaders().length; i++)
				System.out.println(postMethod.getResponseHeaders()[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
	}

	public static String exeGet(String url) {
		HttpClient httpClient = new HttpClient();
		GetMethod postMethod = new GetMethod(url);
		String text = null;
		try {
			httpClient.executeMethod(postMethod);
			text = postMethod.getResponseBodyAsString();
			System.out.println("response:" + text);
			System.out.println("headers:");
			for (int i = 0; i < postMethod.getResponseHeaders().length; i++)
				System.out.println(postMethod.getResponseHeaders()[i]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("finish");
		return text;
	}
}
