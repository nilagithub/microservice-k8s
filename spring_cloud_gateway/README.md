# Introduction
https://stackoverflow.com/questions/60462831/metrics-like-response-time-of-an-api-in-spring-cloud-gateway

# Spring Cloud Gateway Sample
https://github.com/spring-cloud-samples/spring-cloud-gateway-sample

Sample that shows a few different ways to route and showcases some filters.

Run `DemogatewayApplication`

## Eureka registration
	in main springboot file - @EnableEurekaClient
	pom.xml- <groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	application.yml-
		spring:
		  application:
		    name: ACCOUNT-SERVICE	(similarli GATEWAY-SERVICE)
		    							
		eureka:
		  instance:
		    preferIpAddress: true
		    #hostname: eureka-0
		  client:
		    registerWithEureka: true
		    fetchRegistry: true
		    serviceUrl:
		      #defaultZone: http://eureka-0.eureka.default.svc.cluster.local:8761/eureka
		      defaultZone: http://localhost:8761/eureka/ # Replace with your Eureka server URL

Routing in gateway as below using lb://<service-name-registered>
	cloud:
	    gateway:
	      routes:
	      - id: ACCOUNT-SERVICE
	        uri: lb://ACCOUNT-SERVICE
	        #uri: http://localhost:8091
 
http://localhost:8085/account  ==> worked with eureka configuration

## Inter service communication
https://medium.com/@mohammedsafir17/java-microservices-architecture-inter-service-communication-211217ef0809

Important Note: While Spring Cloud Gateway facilitates client-to-microservice interaction, direct inter-service communication between microservices themselves often utilizes service discovery mechanisms (like Spring Cloud Eureka or Consul) and direct calls, rather than routing every internal call through the Gateway. The Gateway's primary role is managing external access and cross-cutting concerns for client-facing APIs.

In this scenario, we have two microservices: the Trip Management service and the Passenger Management service.

1. The passenger’s smartphone initiates communication by making a POST request to the Trip Management service to request a trip.
2. The Trip Management service, in turn, communicates with the Passenger Management service to verify the passenger’s authorization before creating the trip.

eureka resgistration of passenger
spring.application.name=passenger-management-service
eureka.client.service-url.default-zone=http://eureka-server-host:port/eureka/


trip service calls passenger service using rest template
private final String passengerServiceUrl = "http://passenger-management-service/passengers";
RestTemplate restTemplate = new RestTemplate();
        Passenger passenger = restTemplate.getForObject(passengerServiceUrl + "/" + tripRequest.getId(), Passenger.class);

     
## Circuit Breaker
Simple:
https://medium.com/@uptoamir/implementing-circuit-breaker-pattern-in-spring-cloud-gateway-b56b86697498
Advanced:
https://medium.com/@okan.ardic/spring-cloud-microservices-part-5-implementing-circuit-breaker-with-resilience4j-b543f49e68d3

Configure resillence in gateway itself for a router & a fallback endpoint for each routing service
Then manageent endpoint are configured for gateway & on below it gives circuit breaker status as well

http://localhost:8085/actuator/health
http://localhost:8085/actuator/circuitbreakers
http://localhost:8085/actuator/circuitbreakerevents    [Monitoring Circuit Breaker Events]

If downstream is up ==>  Circuit breaker  status as 'Closed' i.e all good. Then down the service & heat for 2(or 3) times as configured-minimumNumberOfCalls, 
circuit breaker will go to 'Open' state.Then hit again with service down , it will go to half open , continous hit will go to Open state. .

Another scenario- in Half open state, make the service 'UP' & hit a request,  circuit breaker goes to 'Half Open'. Then 3-4 calls hit no of times as configured (permittedNumberOfCallsInHalfOpenState), circuit breaker will come to 'Closed state' i.e healthy

Half Open means it tests & in recovery state

## Rate Limiting
Ratelimiting- advanced
https://medium.com/@dinesharney/pluggable-rate-limiter-microservice-with-spring-cloud-gateway-3ab8c4423950
https://github.com/dinesharney/api-gateway-ratelimiter

I just made it simple used only TokenBucketRatelimiter & 5 calls in 60 secs for Account service
Used- CustomRateLimiterGatewayFilterFactory

## Sleuth- Distributed tracing
add depepndency in pom.xml
slf4j default comes via-  spring-boot-starter-webflux       for cloud gateway
							spring-boot-starter-web   for normal springbpoot app

Be;low is must:
	spring.reactor.context-propagation=AUTO

Log pattern in logback-spring.xml	
<encoder>
    <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} -- [TraceID: %X{traceId},SpanID: %X{spanId}] %msg%n</pattern>
</encoder>

## Samples
```
$ http :8080/get
HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Connection: keep-alive
Content-Length: 257
Content-Type: application/json
Date: Fri, 13 Oct 2017 15:36:12 GMT
Expires: 0
Pragma: no-cache
Server: meinheld/0.6.1
Via: 1.1 vegur
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Powered-By: Flask
X-Processed-Time: 0.00123405456543
X-XSS-Protection: 1 ; mode=block

{
    "args": {}, 
    "headers": {
        "Accept": "*/*", 
        "Accept-Encoding": "gzip, deflate", 
        "Connection": "close", 
        "Host": "httpbin.org", 
        "User-Agent": "HTTPie/0.9.8"
    }, 
    "origin": "207.107.158.66", 
    "url": "http://httpbin.org/get"
}


$ http :8080/headers Host:www.myhost.org
HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Connection: keep-alive
Content-Length: 175
Content-Type: application/json
Date: Fri, 13 Oct 2017 15:36:35 GMT
Expires: 0
Pragma: no-cache
Server: meinheld/0.6.1
Via: 1.1 vegur
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Powered-By: Flask
X-Processed-Time: 0.0012538433075
X-XSS-Protection: 1 ; mode=block

{
    "headers": {
        "Accept": "*/*", 
        "Accept-Encoding": "gzip, deflate", 
        "Connection": "close", 
        "Host": "httpbin.org", 
        "User-Agent": "HTTPie/0.9.8"
    }
}

$ http :8080/foo/get Host:www.rewrite.org
HTTP/1.1 200 OK
Access-Control-Allow-Credentials: true
Access-Control-Allow-Origin: *
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Connection: keep-alive
Content-Length: 257
Content-Type: application/json
Date: Fri, 13 Oct 2017 15:36:51 GMT
Expires: 0
Pragma: no-cache
Server: meinheld/0.6.1
Via: 1.1 vegur
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-Powered-By: Flask
X-Processed-Time: 0.000664949417114
X-XSS-Protection: 1 ; mode=block

{
    "args": {}, 
    "headers": {
        "Accept": "*/*", 
        "Accept-Encoding": "gzip, deflate", 
        "Connection": "close", 
        "Host": "httpbin.org", 
        "User-Agent": "HTTPie/0.9.8"
    }, 
    "origin": "207.107.158.66", 
    "url": "http://httpbin.org/get"
}

$ http :8080/delay/2 Host:www.circuitbreaker.org
HTTP/1.1 504 Gateway Timeout
Cache-Control: no-cache, no-store, max-age=0, must-revalidate
Expires: 0
Pragma: no-cache
X-Content-Type-Options: nosniff
X-Frame-Options: DENY
X-XSS-Protection: 1 ; mode=block
content-length: 0


```

## Websocket Sample

[install wscat](https://www.npmjs.com/package/wscat)

In one terminal, run websocket server:
```
wscat --listen 9000
``` 

In another, run a client, connecting through gateway:
```
wscat --connect ws://localhost:8080/echo
```

type away in either server and client, messages will be passed appropriately.

## Running Redis Rate Limiter Test

Make sure redis is running on localhost:6379 (using brew or apt or docker).

Then run `DemogatewayApplicationTests`. It should pass which means one of the calls received a 429 TO_MANY_REQUESTS HTTP status.