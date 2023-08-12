# build stage
FROM maven:3.9-amazoncorretto-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DwarName=react-example-api

# run stage
FROM tomcat:10.0.27-jre17

COPY --from=build /app/target/react-example-api.war $CATALINA_HOME/webapps/