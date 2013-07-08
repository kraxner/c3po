package com.petpet.c3po.adaptor.rules.drools;

import java.util.Comparator;
import java.util.List;

import com.petpet.c3po.api.model.Source;
import com.petpet.c3po.api.model.helper.MetadataRecord;
import com.petpet.c3po.dao.MetadataUtil;

public class LogCollector {

  public static int TRACE = 0;
  public static int DEBUG = 1;
  public static int INFO = 2;

  private static Comparator<MetadataRecord> sorter;

  private StringBuilder stringBuilder = new StringBuilder();

  private int minimumLoglevel;

  public LogCollector(int minimumLoglevel) {
    super();
    this.minimumLoglevel = minimumLoglevel;
  }

  public static Comparator<MetadataRecord> getMetadataSorter() {
    if (LogCollector.sorter == null) {

      sorter = new java.util.Comparator<MetadataRecord>() {

        @Override
        public int compare(MetadataRecord o1, MetadataRecord o2) {
          int compare = o1.getProperty().getId()
              .compareTo(o2.getProperty().getId());
          if (compare != 0) {
            return compare;
          }

          compare = o1.getValue().compareTo(o2.getValue());
          if (compare != 0) {
            return compare;
          }

          return o1.hashCode() - o2.hashCode();

        }
      };
    }
    return sorter;
  }

  public synchronized void log(int loglevel, String text) {
    if (this.minimumLoglevel <= loglevel) {
      this.stringBuilder.append(text);
      this.stringBuilder.append("\n");
    }
  }

  public synchronized void logMetadataRecord(int loglevel, MetadataRecord record) {
    this.logMetadataRecordValue(loglevel, record);
    List<String> sourceIDs = record.getSources();
    this.logMetadataRecordSources(loglevel, sourceIDs);
  }

  public synchronized void logMetadataRecordSources(int loglevel, List<String> sourceIDs) {
    for (String sourceID : sourceIDs) {
      Source source = MetadataUtil.resolveSourceID(sourceID);
      this.log(loglevel,
          "        Source: " + source.getName() + " " + source.getVersion()
              + " [" + source.getId() + "]");
    }
  }

  public synchronized void logMetadataRecordValue(int loglevel, MetadataRecord record) {
    this.log(loglevel,
        "    " + record.getProperty().getId() + " : \'" + record.getValue()
            + "\'");
  }

  public synchronized String reset() {
    String string = this.stringBuilder.toString();
    this.stringBuilder.setLength(0);
    return string;
  }
}
