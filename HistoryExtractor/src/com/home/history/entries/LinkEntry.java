package com.home.history.entries;

import java.util.List;

public interface LinkEntry {
   public String getHttpAddress();
   
   public List<String> getCommentsAfter();
   
   public List<String> getCommentsBefore();
   
   /**
    * Specific entry in the source site (for example the entry of the video in YouTube)
    * 
    * @return
    */
   public String getEntryId();
}
