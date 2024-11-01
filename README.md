# Flightapp

Proyecto de reserva de vuelos hecho en formación Java-Spring de TokioSchool

## Descripción

Este proyecto combina el uso de Spring Boot, Spring Security, Spring Data JPA y Spring Batch para crear una aplicación
web de reserva de vuelos. La aplicación permite a los usuarios registrarse, iniciar sesión, buscar vuelos, reservar
vuelos y ver sus reservas. Los administradores pueden ver y gestionar los vuelos y las reservas.

Es un proyecto en el cual se exploran los conceptos de autenticación, autorización mediante JWT, persistencia de datos,
API REST,
tareas programadas y plantillas HTML, endpoints de la aplicación, envío de correos electrónicos, test unitarios y de
integración.

El proyecto se divide en cuatro módulos: `flight-web` , `airport-batch`, `file-store-api` y `menu-service`.

## Descripción de los Módulos

### flight-web

Este módulo contiene la lógica de negocios de la aplicación de reserva de vuelos. Contiene los controladores, servicios,
repositorios y entidades de la aplicación. Además de las plantillas de Thymeleaf para la interfaz de usuario.
Gestiona los idiomas de la aplicación, validación de formularios, paginación, manejo de errores, envío de correos.
Muestra la lista de vuelos disponibles, permite a los usuarios registrarse, iniciar sesión, buscar vuelos, reservar.

### file-store-api

Este módulo se encarga de la gestión de archivos(imágenes) de la aplicación. Permite subir, descargar y eliminar
archivos.
También es el módulo encargado de la autenticación y autorización de los usuarios mediante JWT.
Este módulo se comunica con el módulo `flight-web` para la gestión de archivos mediante una API REST.

### airport-batch

Este módulo se encarga de la carga de datos de los aeropuertos en la base de datos. Utiliza Spring Batch para leer un
fichero csv que contiene el listado de aeropuertos y cargarlos en la base de datos. También exporta un fichero en
formato json con el listado de aeropuertos.

### menu-service

Este módulo es una aplicación del tipo "runner" que se ejecuta, realiza el proceso y se detiene. En este módulo se
utilizan base de datos NO-relacionales como MongoDB para almacenar los datos de los menus de la aplicación. Se utiliza
Spring Data MongoDB para la persistencia de datos y la dependencia de JavaFaker para generar datos de prueba.
Se muestran los datos en el log de la aplicación. Contiene métodos de búsquedas, ordenamientos, paginación y filtros.
También se aplicó la utilización de un documento referencial como `@DBRef` para relacionar dos colecciones.


## Instalación y ejecución

1. Clonar el repositorio

```bash
git clone https://github.com/mattyys/flightapp.git
```

2. Crear una base de datos MariaDB utilizando Docker-compose

Debemos de tener instalado Docker y Docker-compose en nuestra máquina.

```bash
cd flightapp
docker compose -f docker-compose-dev.yml up -d
```

3. Crear esquema de base de datos

Ya con el contenedor iniciado, ejecutamos los scripts SQL que están en la carpeta resources/database para crear las
tablas y cargar los datos de
prueba.

```bash
mysql -uroot -pdbpw --protocol tcp flights < src/main/resources/scripts/database/001-upgrade.sql
mysql -uroot -pdbpw --protocol tcp flights < src/main/resources/scripts/database/002-upgrade.sql
mysql -uroot -pdbpw --protocol tcp flights < src/main/resources/scripts/database/003-upgrade.sql
mysql -uroot -pdbpw --protocol tcp flights < src/main/resources/scripts/database/004-upgrade.sql
```

4. Ejecutar la aplicación

Ingresamos a la carpeta raíz del proyecto, luego ejecutamos el siguiente comando para descargar dependencias y compilar
el proyecto:

```bash
cd flight-app
./mvnw clean package
```

Luego ejecutamos el siguiente comando para iniciar la aplicación de file-store-api:

```bash
./mvnw spring-boot:run -pl :file-store-api -Dspring-boot.run.jvmArguments="-DAPPLICATION_CUSTOM=springboot-run -Dspring.profiles.active=dev"
```

Luego ejecutamos el siguiente comando para iniciar la aplicación de flight-web:

```bash
 ./mvnw -pl flight-web spring-boot:run
```

5. Acceder a la aplicación

```
http://localhost:8080/flight/flight
```

6. Detener la aplicación

```bash
./mvnw spring-boot:stop
```

7. Detener la base de datos

```bash
docker-compose down
```

7. Ejecutar menu-service

7.1. Iniciar MongoDB con docker-compose

```bash
docker compose -f docker-compose-dev.yml up -d
``` 

7.2. Ejecutar el menu-service

```bash
./mvnw -pl menu-service spring-boot:run
```

## Tecnologías

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Batch
- Thymeleaf
- Docker
- MariaDB
- MongoDB
- Bootstrap
- Maven
- IntelliJ IDEA

## Agradecimientos

Agradezco a TokioSchool por la formación en Java-Spring y a los profesores por el apoyo y la guía en el desarrollo de
esta aplicación en la cual se trabajaron muchos conceptos que se utilizan en el mundo laboral.


