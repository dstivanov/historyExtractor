package com.home.history.extract;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.buffer.CircularFifoBuffer;

import com.home.history.entries.LinkEntry;

public abstract class HistoryExtractor {
   private String path;
   
   private static final int COMMENTS_NUMBER = 10; 

   public HistoryExtractor(String filePath) {
      this.path = filePath;
   }

   public List<LinkEntry> extractLinks() {
      BufferedReader fileReader = initializeExtraction();

      List<LinkEntry> result = new LinkedList<>();
      
      CircularFifoBuffer beforeBuffer = new CircularFifoBuffer(COMMENTS_NUMBER);
      
      String line = "";
      try {
         LinkEntry pendingEntry = null;
         while ((line = fileReader.readLine()) != null) {
            if (isLineRecognized(line)) {
               pendingEntry = processSingleLine(line);
               if (pendingEntry != null) {
                  List<String> afterComments = pendingEntry.getCommentsAfter();
                  afterComments.add(line);
                  
                  List<String> beforeComments = pendingEntry.getCommentsBefore();
                  beforeComments.addAll(beforeBuffer);
                  beforeBuffer.clear();
                  
                  result.add(pendingEntry);
               }
            } else {
               beforeBuffer.add(line);
               if(pendingEntry != null)
               {
                  List<String> afterComments = pendingEntry.getCommentsAfter();
                  afterComments.add(line);
                  
                  if(afterComments.size() > COMMENTS_NUMBER)
                  {
                     pendingEntry = null;
                  }
               }
            }
         }
      } catch (IOException e) {
         System.err.println("Error while processing line " + line);
         e.printStackTrace();
      }

      finalizeExtraction(fileReader);
      return result;
   }

   private BufferedReader initializeExtraction() {
      File historyFile = new File(path);
      BufferedReader fileReader = null;
      try {
         fileReader = new BufferedReader(new InputStreamReader(
               new FileInputStream(historyFile), Charset.forName("UTF8")));
      } catch (FileNotFoundException e) {
         System.err
               .println("Error occured while initializing the history extraction");
         e.printStackTrace();
      }
      return fileReader;
   }

   private void finalizeExtraction(BufferedReader fileReader) {
      try {
         fileReader.close();
      } catch (IOException e) {
         System.err
               .println("Error occured while finalizing the history extraction");
         e.printStackTrace();
      }
   }

   protected abstract LinkEntry processSingleLine(String line);

   protected abstract boolean isLineRecognized(String line);
}
