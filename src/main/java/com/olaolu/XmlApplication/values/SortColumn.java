package com.olaolu.XmlApplication.values;

public enum SortColumn {
  FILE_NAME("fileName"),
  NEWS_PAPER_NAME("newspaperName");

  private String column;

  SortColumn(String column){
    this.column=column;
  }

  public String getColumn() {
    return column;
  }
}
