package org.tokioschool.flightapp.base.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tokioschool.flightapp.base.domain.Base;
import org.tokioschool.flightapp.base.repository.BaseDAO;
import org.tokioschool.flightapp.base.service.BaseTransactionalService;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BaseTransactionalServiceImpl implements BaseTransactionalService {

  private final BaseDAO baseDAO;

  @Override
  public void nonTransactionalWithOneCommit() { // 2
    Base base = Base.builder().counter(1).build();
    baseDAO.save(base); // se guarda el objeto base despues crearlo

    base.setCounter(base.getCounter() + 1);
    base.setCounter(base.getCounter() + 1); // se incrementa el contador pero no se guarda
  }

  @Override
  public void nonTransactionalWithMultipleCommit() { // 1
    Base base = Base.builder().counter(1).build();

    base.setCounter(base.getCounter() + 1);
    baseDAO.save(base); // se guarda el objeto base despues de incrementar el contador

    base.setCounter(base.getCounter() + 1);
    baseDAO.save(base); // se guarda el objeto base despues de incrementar el contador
  }

  @Override
  public void transactionalWithOneCommit() { // 3
    Base base = Base.builder().counter(1).build();
    baseDAO.save(
        base); // al ser transaccional se guarda el objeto base en su estado final en el metodo

    base.setCounter(base.getCounter() + 1);
    base.setCounter(base.getCounter() + 1);
  }

  @Override
  @Transactional
  public void nonTransactionalWithException() {
    Base base = Base.builder().counter(1).build();
    baseDAO.save(base);

    base.setCounter(base.getCounter() + 1);
    baseDAO.save(base);

    base.setCounter(base.getCounter() / 0); // generamos que se lance una excepcion
    baseDAO.save(base);
  }

  @Override
  @Transactional
  public void transactionalWithUncheckedRollback() {
    Base base = Base.builder().counter(1).build();
    baseDAO.save(base);

    base.setCounter(base.getCounter() + 1);

    base.setCounter(base.getCounter() / 0); // generamos que se lance una excepcion
  }

  @Override
  @Transactional
  public void transactionalWithCheckedCommit() throws IOException {
    Base base = Base.builder().counter(1).build();
    baseDAO.save(base);

    base.setCounter(base.getCounter() + 1);
    try {
      base.setCounter(base.getCounter() / 0);
    } catch (ArithmeticException e) {
      throw new IOException(e); // se lanza este tipo de excepcion para que sea checked
    }
  }

  @Override
  @Transactional(rollbackFor = IOException.class)
  public void transactionalWithCheckedRollback() throws IOException {

    Base base = Base.builder().counter(1).build();

    baseDAO.save(base);

    base.setCounter(base.getCounter() + 1);

    try {
      base.setCounter(base.getCounter() / 0);
    } catch (ArithmeticException e) {
      throw new IOException(e); // se lanza este tipo de excepcion para que sea checked
    }
  }
}
