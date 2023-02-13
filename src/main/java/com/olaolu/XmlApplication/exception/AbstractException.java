package com.olaolu.XmlApplication.exception;

public class AbstractException extends RuntimeException {

  private String code;
  private String guid;
  public AbstractException(String message) {
    super(message);
  }

  public AbstractException(String code, String message) {
    super(message);
    this.setCode(code);
  }

  public AbstractException(String message, Throwable cause) {
    super(message, cause);
  }

  public AbstractException(String code, String guid, String message) {
    super(message);
    this.setCode(code);
    this.setGuid(guid);
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }
}
