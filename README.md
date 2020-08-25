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
    1. Check application [startup](http://localhost:8080/actuator/health)
    2. [Swagger](http://localhost:8080/swagger-ui/index.html)
5. Run unit test
```sh
./gradlew test
```
    1. [Test report](http://localhost:63342/demo/build/reports/tests/test/index.html)
    2. [Coverage report](http://localhost:63342/demo/build/reports/jacoco/test/html/index.html)