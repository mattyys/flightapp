package org.tokioschool.flightapp.base.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tokioschool.flightapp.base.domain.Base;
import org.tokioschool.flightapp.base.repository.BaseDAO;
import org.tokioschool.flightapp.base.service.BaseTransactionalService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BaseTransactionalServiceImpl implements BaseTransactionalService {

  private final BaseDAO baseDAO;

  @Override
  public void nonTransactionalWithOneCommit() {
    Base base = Base.builder().counter(1).build();
    baseDAO.save(base);

  }

  @Override
  public void nonTransactionalWithMultipleCommit() {
    Base base = Base.builder().counter(1).build();

    base.setCounter(base.getCounter() + 1);
    baseDAO.save(base);

    base.setCounter(base.getCounter() + 1);
    baseDAO.save(base);
  }

  @Override
  public void transactionalWithOneCommit() {}

  @Override
  public void nonTransactionalWithException() {}

  @Override
  public void transactionalWithUncheckedRollback() {}

  @Override
  public void transactionalWithCheckedCommit() throws IOException {}

  @Override
  public void transactionalWithCheckedRollback() throws IOException {}
}
