package com.home.history.test;

import java.io.IOException;
import java.util.List;

import com.home.history.entries.LinkEntry;
import com.home.history.extract.HistoryExtractor;
import com.home.history.extract.YouTubeExtractor;
import com.home.history.visualization.LinksVisualizer;
import com.home.history.visualization.YouTubeInHtmlVisualizer;

public class ExtractLinksTest {
   public static void main(String[] args) throws IOException {
      
//      String filePath = "F:\\Dev\\HistoryExtractor\\Work-history.txt";
      String filePath = "F:\\Dev\\HistoryExtractor\\test.txt";
      
      HistoryExtractor extractor = new YouTubeExtractor(filePath);
      List<LinkEntry> youTubeLinks = extractor.extractLinks();
      
      
      String outputPath = "history_links.html";
      
      LinksVisualizer visualizer = new YouTubeInHtmlVisualizer(youTubeLinks,outputPath);
      visualizer.visualizeLinks();
   }
}
