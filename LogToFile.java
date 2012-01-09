// This work is licensed under the GNU Public License (GPL).
// To view a copy of this license, visit http://www.gnu.org/copyleft/gpl.html

// Written by Abd Allah Diab (mpcabd)
// Email: mpcabd ^at^ gmail ^dot^ com
// Website: http://mpcabd.igeex.biz

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class LogToFile {
	private static final String LOG_TO_FILE_ADDRESS = "http://192.168.1.3";
	private static final String LOG_TO_FILE_URL = "/";
	private static final String LOG_TO_FILE_LOG_PARAMETER = "log";
	private static final int LOG_TO_FILE_PORT = 5500;
	
	private static HttpResponse ExecuteRequest(HttpUriRequest request) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(request);
			return response;
		} catch (Exception e) {
			Log.e("", e.getClass().getName() + "\n" + e.getMessage());
		}
		return null;
	}
	
	private static HttpPost Post(String url, HashMap<String, String> params, HashMap<String, ArrayList<String>> arrayParams) {
		try {
			if (!url.endsWith("/"))
				url += "/";
			List<NameValuePair> params_list = null;
			if (params != null) {
				params_list = new LinkedList<NameValuePair>();
				for (Entry<String, String> entry : params.entrySet())
					params_list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			if (arrayParams != null) {
				if (params_list == null)
					params_list = new LinkedList<NameValuePair>();
				for (Entry<String, ArrayList<String>> entry : arrayParams.entrySet())
					for (String value : entry.getValue())
						params_list.add(new BasicNameValuePair(entry.getKey(), value));
			}
			HttpPost request = new HttpPost(url);
			if (params != null)
				request.setEntity(new UrlEncodedFormEntity(params_list, "utf-8"));
			return request;
		} catch (Exception e) {
			Log.e("", e.getClass().getName() + "\n" + e.getMessage());
		}
		return null;
	}
	
	public static void LogToFile(String log) {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(LOG_TO_FILE_LOG_PARAMETER, log);
			HttpPost request = Post(String.format("%s:%d%s", LOG_TO_FILE_ADDRESS, LOG_TO_FILE_PORT, LOG_TO_FILE_URL), params, null);
			HttpResponse response = ExecuteRequest(request);
			response.getEntity().consumeContent();
		} catch (Exception e) {
			Log.e("", e.getClass().getName() + "\n" + e.getMessage());
		}
	}
}