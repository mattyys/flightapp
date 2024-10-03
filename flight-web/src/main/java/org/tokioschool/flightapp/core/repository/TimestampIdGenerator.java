package org.tokioschool.flightapp.core.repository;

import com.github.f4b6a3.tsid.TsidCreator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class TimestampIdGenerator implements IdentifierGenerator {

  @Override
  public Object generate(
      SharedSessionContractImplementor sharedSessionContractImplementor, Object object) {
    return TsidCreator.getTsid256().toString();
  }
}
