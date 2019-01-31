Spring Data MongoDB + JPA
=========================

To run with JPA

`
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=jpa"
`
To run with MongoDB

`
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=mongo"
`

PS: Default profile is JPA

The Controller/Service will not be affected regarding the Data Provider.

Access http://localhost:8080 then will see the 'provider field' what is the implementation(JPA or MongoDB) according to the profile.
