package com.home.history.entries;

import java.util.LinkedList;
import java.util.List;

public class YouTubeLinkEntry implements LinkEntry {

   private String httpAddress;
   private List<String> commentsBefore;
   private List<String> commentsAfter;
   private String videoId;

   public YouTubeLinkEntry(String httpAddress, String videoId,
         List<String> commentsBefore, List<String> commentsAfter) {
      this.httpAddress = httpAddress.trim();
      this.videoId = videoId.trim();
      this.commentsBefore = commentsBefore;
      this.commentsAfter = commentsAfter;
   }

   public YouTubeLinkEntry(String httpAddress, String videoId,
         List<String> commentsAfter) {
      this(httpAddress, videoId, new LinkedList<String>(), commentsAfter);
   }

   @Override
   public String getHttpAddress() {
      return httpAddress;
   }

   @Override
   public List<String> getCommentsAfter() {
      return commentsAfter;
   }

   @Override
   public List<String> getCommentsBefore() {
      return commentsBefore;
   }

   @Override
   public String getEntryId() {
      return videoId;
   }
}
