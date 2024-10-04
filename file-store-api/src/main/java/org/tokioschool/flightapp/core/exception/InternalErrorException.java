package org.tokioschool.flightapp.core.exception;

public class InternalErrorException extends RuntimeException {
  public InternalErrorException(String message) {
    super(message);
  }
}
