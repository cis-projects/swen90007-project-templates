# React Example API

This project implements a simple API with JavaEE Servlet API, and is designed to be served from the Tomcat web server.

## Install

The project is managed with Maven, install dependencies with:

```shell
mvn clean compile
```

And build a `.war` with:

```shell
mvn clean package -DwarName=react-example-api
```

## Start (deployment to Tomcat)

To start the application, build with Maven, and then deploy to a running Tomcat installation.

```shell
mvn clean package -DwarName=react-example-api
cp target/react-example-api.war <tomcat-install>/webapps
```

### Dependencies

#### PostgreSQL

The API requires access to a running PostgreSQL server, and appropriately configured/initialised database, user and schema, to function correctly.

Initialise the database by running, in order, both of `./db/init.sql` and `./db/load.sql` against the database.

#### Java system properties

The API expects a number of java system properties to be made available by the Tomcat web server.

| System property | Value |
| --------------- | ----- |
| jdbc.uri        | the JDBC connection string for the database, should look something like `jdbc:postgesql://<hostname>:<port>/<database name>`|
| jdbc.username   | username of user authorised to connect to the database |
| jdbc.password   | password for authenticating connections to the database |
| cors.origins.ui | the location of the running UI component |
| jwt.secret      | `7zRrC0HsdVyzOAUrH+1FyN2+Iqv7QlM9A6oU0q7Qi+k=` (you should change this in production - you can generate your own sufficiently secure key, on Unix-like systems, with `sudo head -c32 < /dev/random \| base64`) |
| jwt.timeToLive.seconds     | `60`               |
| jwt.issuer                 | `react-example-api.unimelb.edu.au` (you should also change this for production)                          |
| cookies.timeToLive.seconds | `360000`                                                                                               |
| cookies.domain             | `localhost`                                                                         |
| cookies.secure             | `false`          |
| admin.username             | `admin`                                            |
| admin.password             | `admin` (again, make sure you change this for production)                                                    |

The easiest way to ensure Tomcat sets these system properties prior to starting the API is to launch Tomcat with the `JAVA_OPTS` environment variable set.

On Unix systems (with values for `<uri>` etc substituted appropriately):

```shell
export JAVA_OPTS='-Djdbc.uri=<uri> -Djdbc.username=<username> -Djdbc.password=<password> -Dcors.origins.ui=<origin> -Djwt.secret=7zRrC0HsdVyzOAUrH+1FyN2+Iqv7QlM9A6oU0q7Qi+k= -Djwt.timeToLive.seconds=60 -Djwt.issuer=react-example-api.unimelb.com -Dcookies.timeToLive.seconds=360000 -Dcookies.domain=localhost -Dcookies.secure=false -Dadmin.username=admin -Dadmin.password=admin'
```

### Docker

Build an image

```shell
docker build --rm -t swen90007-react-example-api .
```

And then run (assumes a containerised PostgreSQL is available on a Docker network `swen90007-react-example`, see primer documentation for further details):

```shell
docker run --network=swen90007-react-example -dti -p 8080:8080 -e JAVA_OPTS='-Djdbc.uri=<uri> -Djdbc.username=<username> -Djdbc.password=<pasword> -Dcors.origins.ui=<origin> -Djwt.secret=7zRrC0HsdVyzOAUrH+1FyN2+Iqv7QlM9A6oU0q7Qi+k= -Djwt.timeToLive.seconds=60 -Djwt.issuer=react-example-api.unimelb.com -Dcookies.timeToLive.seconds=360000 -Dcookies.domain=localhost -Dcookies.secure=false -Dadmin.username=admin -Dadmin.password=admin' swen90007-react-example-api
```
