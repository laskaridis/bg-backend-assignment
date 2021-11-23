Avoid using sequence primary keys as entity identifiers
=======================================================

Status
------

Accepted (2022-01-01)

Context
-------

Keys derived from a sequence are not secure (i.e. are monotonically increasing) opposed to randomply generated keys
like UUIDs. Also, they tend to be less easy to migrate in certain cirquimstances, causing the same record(s) to change
its primary key, which may result to inconsistencies in case of distributed applications.


Decision
--------

Use either _natural_ identifiers for domain model identifiers, or randomply generated "UUID"s.

Consequences
------------

A more secure and flexible domain model identifiers.