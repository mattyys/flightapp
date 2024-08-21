# Flight App
## Introduction
Es una aplicación que permite a los usuarios buscar vuelos y hoteles, además de poder reservarlos.

## Proceso de desarrollo
Para el desarrollo de la aplicación se utilizó el lenguaje de programación Java y el Framework de desarrollo Spring Boot..

Dentro del package del proyecto creamo un package base que es donde vamos a realizar las primeras pruebas de configuracion.
Creamos 2 clases BaseSErvice1 y BaseService2, las dos clase con el mismo metodo que retorna un String.

```java
package com.flightapp.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseService1 {
    public String say() {
        return "Hello from BaseService1";
    }
}
```

```java

package com.flightapp.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Servcie
@Slf4j
public class BaseService2 {
    public String say() {
        return "Hello from BaseService2";
    }
}
```

En la clase base service 1 vamos a inyectar la clase BaseService2

```java
package com.flightapp.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseService1 {
    @Autowired
    private BaseService2 baseService2;

    public String say() {
        return "Hello from BaseService1";
    }

    public String sayFromBaseService2() {
        return baseService2.say();
    }
}
```
Tenemos 3 formas de inyectar una dependencia en Spring a través de la anotación @Autowired, por medio de los Setter y Contructures.
    
```java
package com.flightapp.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseService1 {
    private BaseService2 baseService2;

    @Autowired
    public void setBaseService2(BaseService2 baseService2) {
        this.baseService2 = baseService2;
    }

    public String say() {
        return "Hello from BaseService1";
    }

    public String sayFromBaseService2() {
        return baseService2.say();
    }
}
```

```java
package com.flightapp.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BaseService1 {
    private BaseService2 baseService2;

    @Autowired
    public BaseService1(BaseService2 baseService2) {
        this.baseService2 = baseService2;
    }

    public String say() {
        return "Hello from BaseService1";
    }

    public String sayFromBaseService2() {
        return baseService2.say();
    }
}
```
Tambien podemos utilizar la anotacion @RequiredArgsConstructor de la libreria Lombok, para inyectar las dependencias.

```java
package com.flightapp.base;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BaseService1 {
    private final BaseService2 baseService2;

    public String say() {
        return "Hello from BaseService1";
    }

    public String sayFromBaseService2() {
        return baseService2.say();
    }
}
```
