Project architecture
====================

[<< Go back](../README.md)

Architecture decision records
-----------------------------

I typically employ application decision records (ADRs) to capture key architecturally significant decisions during the
evolution of an application. I've included some decisions i've made for this project here for illustration purposes.
You will find them [here](./adrs/adrs.md).

Domain model
------------

See [here](./assets/DomainModel.png) for an overview of the key domain classes (excluding repositories).

Tech stack
----------

Key technologies used for this project are:

* `spring`, as the core dependency injection framework used
* `spring-boot`, as the core application framework
* `jpa` as the ORM framework
* `embedded-h2` as the application's primary persistence store
* `swagger` to document the API
* `liquibase` to manage database migrations


[<< Go back](../README.md)
