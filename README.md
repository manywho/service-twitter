ManyWho Twitter Service
==========================

#### Running

The service is a Jersey JAX-RS application, that by default is run under the Grizzly2 server on port 8080 (if you use 
the packaged JAR).

To build the service, you will need to have Apache Ant, Maven 3 and a Java 8 implementation installed.

You will need to generate a configuration file for the service by running the provided build.xml script with Ant.

# Twitter-ManyWho

$ ant -Doauth2.clientId=xxx -Doauth2.clientSecret=xxx -Dredis.url=xxx
Now you can build the runnable shaded JAR

##### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/twitter/1`:

```bash
$ java -jar target/twitter-1.0-SNAPSHOT.jar
```

##### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/twitter/1`):

```bash
$ java -Dserver.port=9090 -jar target/twitter-1.0-SNAPSHOT.jar
```

## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
