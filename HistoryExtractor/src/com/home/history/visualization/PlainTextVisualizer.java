package com.home.history.visualization;

import java.util.List;

import com.home.history.entries.LinkEntry;

public class PlainTextVisualizer extends LinksVisualizer {

   public PlainTextVisualizer(List<LinkEntry> entries) {
      super(entries);
   }

   @Override
   public void visualizeLinks() {
      int counter = 1;
      for(LinkEntry currentLink: entries)
      {
         System.out.println(counter + ") " + currentLink.getHttpAddress());
         counter++;
      }
   }

}
