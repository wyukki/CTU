# EAR e-shop

EAR e-shop is a demonstration application for the purpose of teaching the Enterprise Architectures (EAR) course.

It is a simplified e-shop, concentrating on the backend part of the application. The following technologies are used/required
for development and running:

* JDK 11 or later
* PostgreSQL 9 or later (a different database can be configured)
* Apache Maven 3.x

The application is developed using Spring Boot 2.7.3. We are using the `spring-boot-starter-parent` POM which contains most of the dependencies a typical
Boot project would need. This application's POM only declares the ones we are interested in.

The most important dependencies include:
* Spring Framework
* Spring Security
* JPA 2.2 (Eclipselink)
* Jackson 2.13
* Spring-based REST API
* JUnit 5, Mockito 4, Spring test, H2 database (for tests)
* SLF4J + Logback

The following technologies can further simplify application development, but it was decided not to use them as they would obscure
the underlying principles important for the students. However, they can be useful for developing semester works.

* Spring JPA repositories
* Lombok

### Frontend

Application front end is written in **React**. It exists only to provide a showcase for the implemented backend, 
i.e., so that the students can see effects of the backend implementation. Since the students
will not be learning frontend technologies in EAR, it is not necessary for them to understand how the UI works.

They should also not need to install Node or any JS-building technology, as the application will use a pre-built bundle.

The frontend is written in plain JS (instead of TypeScript) to simplify understanding for those who will be interested in it. More detailed
description can be found the `README.md` file in `src/main/webapp`.

### Data

The system generates an admin user on first start. Its credentials are `ear-admin@kbss.felk.cvut.cz/adm1n`. See `SystemInitializer`
for details.

`data.sql` contains some sample data which can be inserted into the database. It contains an addition user - *Sam Fisher* - who is a
guest, so he cannot modify categories or products. His credentials are **fisher@example.org/a**.


### License

LGPLv3
