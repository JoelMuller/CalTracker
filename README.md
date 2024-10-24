# About

This is an application which allows you to track the amount of calories you consume during the day/week. It uses an Angular front-end and a Java Spring boot back-end.

## How to run the project
### Prerequisites
- JDK 17
- Gradle
- Node.js
- Angular CLI
- MySQL Database

### Back-end
Create a MySQL database and make sure you use the correct properties in your application.properties file.
Then you just need to use the command `./gradlew bootRun` (make sure you are in the back-end directory when executing this command) to start the API on port `8080`.

### Front-end
Make sure to be in the front-end folder of the project and execute `npm install`. After the dependencies have been installed you can do `ng serve` to start the front-end on port `4200`.

If you want to configure SSL for localhost you can create your own certificate with openSSL in your terminal if you have it installed on your system. Then you need to point the fields "sslKey" and "sslCert" in the `angular.json` file to the correct file in your file system.

## Testing
As of this moment only the back-end has tests written. These can be run with the command `./gradlew test`

## Swagger API documentation
When you start the back-end there is a swagger page that shows you the available endpoints and the requests you can send to them. You can also see what the response would be. The URL is: http://localhost:8080/api/swagger-ui/index.html