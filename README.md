# Read Me First

Getting Started
---------------

The project requires `java 11`. If you don't have set it up already, a quick and easy way to set it up is:
https://sdkman.io/

Start by building the project:

```bash
$ ./mvnw clean package
$ ./java -jar target/bg-backend-assignement-0.0.1-SNAPSHOT.jar
```

Point your browser [here](http://localhost:8080/swagger-ui) to read the API and start using it.

> For your convenience, I have included a collection of API calls that you can import in Postman
and use. You can find it in the project root, under directory `postman`.

Where to go next?
-----------------

* Check the [API documentation](http://localhost:8080/swagger-ui).
* Read more on how to [authenticate against the API to use it](doc/security.md).
* Read about the project's [architecture and design](doc/architecture.md).
* Read about [how the codebase is structured](doc/structure.md).
* Read more about [how the application is tested](doc/testing.md).

Before you go
-------------

Please be advised while reviewing the code that I've made every possible effort to put as much time as possible to 
address all points requested in the assignment. Unfortunately, due to lack of time, a few things have been inevitably
left unfinished or poorly implemented. I hope however, this codebase will give you a fairly good overview of my 
attitude towards software engineering and coding, which I assume is also your end goal.

I would be grateful for any feedback in case you have any recommendations on things I could improve. So, feel free to
leave your comments (or any questions you may have) directly on github. I'll make sure to go through them carefully.

Last but not least, _Thank you_ for the time you're spending on reviewing my code and considering my application.
