package org.tokioschool.flightapp.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tokioschool.flightapp.base.domain.Base;

public interface BaseDAO extends JpaRepository<Base, Long> {}
