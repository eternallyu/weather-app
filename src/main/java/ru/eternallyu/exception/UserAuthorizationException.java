package ru.eternallyu.exception;

public class UserAuthorizationException extends RuntimeException {
  public UserAuthorizationException(String message) {
    super(message);
  }
}
