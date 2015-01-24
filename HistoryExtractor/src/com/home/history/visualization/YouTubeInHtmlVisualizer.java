package com.home.history.visualization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import com.home.history.entries.LinkEntry;

public class YouTubeInHtmlVisualizer extends LinksVisualizer {

   private static final String NEW_LINE = "\n";
   private String outputPath;

   public YouTubeInHtmlVisualizer(List<LinkEntry> entries, String outputPath) {
      super(entries);
      this.outputPath = outputPath;
   }

   @Override
   public void visualizeLinks() {
      File htmlOutput = new File(outputPath);
      try {
         FileOutputStream outputStream = new FileOutputStream(htmlOutput);
         OutputStreamWriter fileWriter = new OutputStreamWriter(outputStream,
               Charset.forName("UTF8"));

         fileWriter.append("<!DOCTYPE html>");
         fileWriter.append(NEW_LINE);
         fileWriter.append("<head>");
         fileWriter.append(NEW_LINE);
         fileWriter.append("<meta charset=\"UTF-8\">");
         fileWriter.append(NEW_LINE);
         fileWriter.append("</head>");
         fileWriter.append(NEW_LINE);
         fileWriter.append("<html>");
         fileWriter.append(NEW_LINE);
         fileWriter.append("<body>");
         fileWriter.append(NEW_LINE);

         int counter = 0;
         for (LinkEntry tubeEntry : entries) {
            if (counter > 10) {
               break;
            }

            addCommentsBefore(fileWriter, tubeEntry);
            fileWriter.append("<br>");
            addVideoInFrame(fileWriter, tubeEntry);
            fileWriter.append("<br>");
            addCommentsAfter(fileWriter, tubeEntry);

            fileWriter.append("<br>");
            fileWriter.append("<hr>");
            fileWriter.append(NEW_LINE);
            fileWriter.append("<br>");

            counter++;
         }

         fileWriter.append(NEW_LINE);
         fileWriter.append("</body>");
         fileWriter.append(NEW_LINE);
         fileWriter.append("</html>");

         fileWriter.close();

      } catch (IOException e) {
         System.err.println("Error while visualizing links");
         e.printStackTrace();
      }
   }

   private void addCommentsBefore(OutputStreamWriter writer, LinkEntry tubeEntry)
         throws IOException {
      for (String comment : tubeEntry.getCommentsBefore()) {
         writer.append(comment);
         writer.append(NEW_LINE);
         writer.append("<br>");
      }
   }

   private void addVideoInFrame(OutputStreamWriter writer, LinkEntry entry)
         throws IOException {
      writer.append("<iframe width=\"420\" height=\"345\"");
      writer.append(NEW_LINE);
      writer.append("src=\"http://www.youtube.com/embed/");
      writer.append(entry.getEntryId());
      writer.append("\">");
      writer.append(NEW_LINE);
      writer.append("</iframe>");
      writer.append("<br>");
      writer.append(NEW_LINE);
   }

   private void addCommentsAfter(OutputStreamWriter writer, LinkEntry tubeEntry)
         throws IOException {
      for (String comment : tubeEntry.getCommentsAfter()) {
         writer.append(comment);
         writer.append(NEW_LINE);
         writer.append("<br>");
      }
   }
}
