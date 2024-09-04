package org.tokioschool.flightapp.base.service;

import java.io.IOException;

public interface BaseTransactionalService {

  void nonTransactionalWithOneCommit();

  void nonTransactionalWithMultipleCommit();

  void transactionalWithOneCommit();

  void nonTransactionalWithException();

  void transactionalWithUncheckedRollback();

  void transactionalWithCheckedCommit() throws IOException;

  void transactionalWithCheckedRollback() throws IOException;
}
