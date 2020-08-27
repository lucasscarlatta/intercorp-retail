# Getting Started
### Pre requisites
* [Java](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Gradle](https://gradle.org/install/)

### Installation
1. Clone the repo
```sh
git clone https://github.com/lucasscarlatta/intercorp-retail.git
```
2. Go to the project
```sh
cd intercorp-retail
```
3. Build project locally
```sh
./gradlew build
```
4. Run project locally
```sh
./gradlew run -Dspring.profiles.active=local
```
* Check application [startup](http://localhost:8080/actuator/health)
* [Swagger](http://localhost:8080/swagger-ui/index.html)
5. Run unit test
```sh
./gradlew test
```
* [Test report](http://localhost:63342/demo/build/reports/tests/test/index.html)
* [Coverage report](http://localhost:63342/demo/build/reports/jacoco/test/html/index.html)

### Deploy
Push to develop branch run git action and deploy in digital ocean droplet.

* Check application [startup](http://68.183.124.83/actuator/health) prod
* [Swagger prod](http://68.183.124.83/swagger-ui/index.htmll)
