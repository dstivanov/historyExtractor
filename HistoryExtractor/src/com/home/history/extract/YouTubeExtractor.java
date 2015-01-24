package com.home.history.extract;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.home.history.entries.LinkEntry;
import com.home.history.entries.YouTubeLinkEntry;

public class YouTubeExtractor extends HistoryExtractor {
   private static final String YOUTUBE_PREFIX = "http://www.youtube.com/watch";

   public YouTubeExtractor(String filePath) {
      super(filePath);
   }
   
   @Override
   protected boolean isLineRecognized(String line) {
      boolean isYouTubeLIne = line.contains(YOUTUBE_PREFIX);
      return isYouTubeLIne;
   }

   @Override
   protected LinkEntry processSingleLine(String line) {
      int httpStart = line.indexOf("http");
      int whitespaceIndex = line.indexOf(" ", httpStart);

      String address = "";
      if (whitespaceIndex != -1) {
         address = line.substring(httpStart, whitespaceIndex);
      } else {
         address = line.substring(httpStart);
      }

      String parameters = address.split("\\?")[1];

      String videoId = null;
      for (String part : parameters.split("&")) {
         if (part.startsWith("v=")) {
            int end = part.contains("#") ? part.indexOf("#") : part.length();
            videoId = part.substring(2, end);
         }
      }

      YouTubeLinkEntry entry = new YouTubeLinkEntry(address,
            videoId, new LinkedList<String>());
      
      boolean isValidEntry = false;
      try {
         isValidEntry = isTubeEntryValid(entry);
      } catch (IOException e) {
         System.err.println("Error occured while verifying the you tube entry " + entry.getHttpAddress());
         e.printStackTrace();
      }

      if (isValidEntry) {
         return entry;
      } else {
         return null;
      }
   }

   private boolean isTubeEntryValid(YouTubeLinkEntry entry)
         throws ClientProtocolException, IOException {
      HttpClient httpclient = HttpClientBuilder.create().build();

      String url = "http://gdata.youtube.com/feeds/api/videos/"
            + URLEncoder.encode(entry.getEntryId(), "UTF-8");
      HttpGet validityRequest = new HttpGet(url);
      HttpResponse response = httpclient.execute(validityRequest);

      if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
         return true;
      } else {
         InputStream content = response.getEntity().getContent();
         String contentString = IOUtils.toString(content, "UTF-8");

         if (contentString.contains("<code>too_many_recent_calls</code>")) {
            System.out
                  .println("Sleeping 30 seconds because youtube is overloaded with requests... ");
            try {
               Thread.sleep(30000);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }

            System.out
                  .println("Rechecking entry and continue the extraction...");
            return isTubeEntryValid(entry);
         }

         System.err.println("Error retrieving the youtube video  "
               + entry.getHttpAddress() + ": "
               + response.getStatusLine().getReasonPhrase() + ", "
               + contentString);
         return false;
      }
   }
}
