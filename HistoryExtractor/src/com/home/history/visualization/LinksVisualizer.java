package com.home.history.visualization;

import java.util.List;

import com.home.history.entries.LinkEntry;

public abstract class LinksVisualizer {
   
   protected List<LinkEntry> entries;

   public LinksVisualizer(List<LinkEntry> entries) {
      this.entries = entries;
   }
   
   public abstract void visualizeLinks();
}
