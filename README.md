# TOMCAT 8 + JERSEY 2 + GUICE 4.1

with Json Web Token Authentication

### REQUIREMENTS
* Java 8
* Tomcat 8
* Maven 3.5.2 or higher
* GIT

### Installation
```
  > git pull git@github.com:fibanez6/tomcat8-jersey2-guice4.git
  > cd tomcat8-jersey2-guice4
  > mvn clean install
```

Then, deploy the .war file in tomcat.

### SWAGGER DOCUMENTATION
```
[HOSTNAME]:[PORT]/api/swagger.json
[HOSTNAME]:[PORT]/api/swagger.yaml
```

### END POINTS
```
[/api/auth], methods=[GET], produces=[application/json]
[/api/user/:subject], methods=[GET], produces=[application/json]
```

### JWT GENERATION
**ENCODE**
```
example: 
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjQiLCJ0aW1lc3RhbXAiOjE1MTU0MzA2NjEyMDd9.yYxHVpwSyGb3Q7g2FxTI2SJcBV6KwTnt2gCGn7UyLcHWVJwoCc-VS8TA2DYdudAZdG2rv0RkPc6Q2ImZGde0MA

with:
subject: fibanez
timestamp: 1515430661207 = "Mon Jan 08 2018 16:57:41" [UTC]
secret: aaaabbbbccccdddd
```
**DECODE**
```
HEADER:ALGORITHM & TOKEN TYPE
{
  "alg": "HS512"
}
PAYLOAD:DATA
{
  "sub": "fibanez",
  "timestamp": 1515430661207
}
VERIFY SIGNATURE
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload), 
  base64Encode(aaaabbbbccccdddd)
)
```

**[Java code](/src/main/java/com/fibanez/jersey2/service/TokenServiceImpl.java) to generate the JWT**
```
String token = Jwts.builder()
                .setSubject({subject=fibanez})
                .claim("timestamp", LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
                .signWith(SignatureAlgorithm.HS512, {secret=aaaabbbbccccdddd})
                .compact();
```

more info visit https://jwt.io/


### EXAMPLE

#### Get Token Authentication
```
$ curl [HOSTNAME]:[PORT]/api/auth?subject=fibanez
{  
   "type":"TOKEN",
   "status":"OK",
   "message":"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJ0aW1lc3RhbXAiOjE1MTUyNDgyMzYxMTJ9.xuNfvauKQOdHMkHTXIdhGvXxDqBRVcgjexKcNYdSbXzqO0eoPRwxblmUdye4O6o-fyBzL0N3OZteLPTM4kZ9bw"
}
```

#### 200 Ok
```
$ curl --request GET \
  --url [HOSTNAME]:[PORT]/api/user/fibanez \
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
  --url [HOSTNAME]:[PORT]/api/user/fibanez \
  --header 'authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMjMiLCJ0aW1lc3RhbXAiOjE1MTUyNDg3NDQzNjN9.eLTFYZeviVA-HJdIpRm9MOEZrwfyPo3dV6SVsYu-Y_KT1RehWeD1rYJL9-KSz_or2I3FMwV7UIDBscBcNW7wwQ'
You cannot access this resource
```

### REFERENCES
https://github.com/caberger/jerseyguice
