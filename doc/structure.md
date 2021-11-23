Project structure and organization
==================================

[<< Go back](../README.md)

The codebase is organized around the key business domain areas of the problem presented:

* `users`: contains code for managing _users_
* `units`: contains code for managing _units_
* `bookings`: contains code for managing _bookings_
* `reminders`: contains code for managing booking _reminders_
* `security`: implements _security_ features (mainly authentication using jwt tokens)

> Note that a similar top level organization has been followed also in the test packages.

In those packages, code has been further organized based on its functional purpose as follows:
* `model`: contains code that implements the core domain model logic constructs, such as _entities_, _value objects_,
  _repositories_, and _services_.
* `controller`: contains code that publishes the core API methods and handles user's interactions.
* `view`: contains code manages the representation of the domain model to the application's users.

> Note that although I am fairly consistent in conventions I'm using, you may find that I deviate in certain cases when 
> I feel that the codebase is more _readable_ when structured otherwise. In general, I tend to lean towards clarity 
> whenever possible.

In addition to these, you will find the following top level packages:

* `errors`: contains code that manages the application errors, and their display to end users
* `commons`: classes which are meant to be used across the various packages

[<< Go back](../README.md)
