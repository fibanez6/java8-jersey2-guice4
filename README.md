# JERSEY 2 + GUICE 4.1

with Json Web Token Authentication

### REQUIREMENTS
* Java 8
* Tomcat 8
* Maven 3.5.2 or higher
* GIT

### Installation
```
  > git pull git@github.com:fibanez6/java8-jersey2-guice4.git
  > cd java8-jersey2-guice4
  > mvn clean install
```

Then, deploy the .war file in tomcat.

### END POINTS
```
[8080]
[/api/auth],methods=[GET],produces=[application/json]
[/api/{subject}],methods=[GET],produces=[application/json]
```

### EXAMPLE

#### Get Token Authentication
```
$ curl localhost:8080/api/auth?subject=123
{  
   "type":"TOKEN",
   "status":"OK",
   "message":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJ0aW1lc3RhbXAiOjE1MTUyNDgyMzYxMTJ9.xuNfvauKQOdHMkHTXIdhGvXxDqBRVcgjexKcNYdSbXzqO0eoPRwxblmUdye4O6o-fyBzL0N3OZteLPTM4kZ9bw"
}
```

#### 200 Ok
```
$ curl --request GET \
  --url http://localhost:8080/api/123 \
  --header 'authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJ0aW1lc3RhbXAiOjE1MTUyNDgyMTIzMTB9.aH6C0HM-TsMHivVQyXe7Cq9fxegBA7o_NYvTGMiG2c_ZuXTobB_4jARXNOF29qdUKER7yaRKMamxp23EDEUuyA'
{  
   "type":"TOKEN",
   "status":"OK",
   "message":"Authenticated"
}
```

#### 401 Unauthorized
```
$ curl --request GET \
  --url http://localhost:8080/api/123 \
  --header 'authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJ0aW1lc3RhbXAiOjE1MTUyNDg3NDQzNjN9.eLTFYZeviVA-HJdIpRm9MOEZrwfyPo3dV6SVsYu-Y_KT1RehWeD1rYJL9-KSz_or2I3FMwV7UIDBscBcNW7wwQ'
You cannot access this resource
```

### REFERENCES
[https://github.com/caberger/jerseyguice]
