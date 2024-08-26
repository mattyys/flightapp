package org.tokioschool.flightapp.core.repository;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@IdGeneratorType(TimestampIdGenerator.class)//se indica que el generador de identificadores será TimestampIdGenerator
@Target({ElementType.METHOD,ElementType.FIELD})//se indica que la anotación se puede aplicar a métodos y atributos
@Retention(RetentionPolicy.RUNTIME)//se indica que la anotación estará disponible en tiempo de ejecución
public @interface TsidGenerator {}
