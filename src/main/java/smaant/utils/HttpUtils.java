package smaant.utils;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

  public static String readUrl(String url) {
    final HttpClient httpClient = HttpClientBuilder.create().build();
    final HttpGet httpGet = new HttpGet(url);
    final String html;
    try {
      final HttpResponse response = httpClient.execute(httpGet);
      html = EntityUtils.toString(response.getEntity());
      EntityUtils.consumeQuietly(response.getEntity());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return html;
  }

}
