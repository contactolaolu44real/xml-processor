package com.olaolu.XmlApplication.exception;

import java.util.UUID;

public class BadRequestException extends AbstractException {

  public BadRequestException(String message) {
    super(message);
    this.setGuid(UUID.randomUUID().toString());
  }

  public BadRequestException(String code, String message) {
    super(code, UUID.randomUUID().toString(), message);
  }
}
