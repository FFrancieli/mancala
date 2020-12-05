## Kalah Game


In order to run the application, run `docker-compose build` followed by `docker-compose up` from the terminal.
The game will be available through the url: http://localhost:3002/. The game will be ready to be played once open
and the row assigned to the current player will highlighted. 

![alt text](https://gist.githubusercontent.com/FFrancieli/a2061468869af464b208717608be68fc/raw/c2920a390ec4185ccda615d13ee4ff21a79d8852/Screenshot%25202020-12-05%2520at%252018.01.00.png "Kalah board")


Find bellow the distribution order of pits on the board accordingly to the code implementation. The terms
`south` and  `north` are used throught the code in order to refer to `bottom` and `top` of the board respectively. 

```
       <--- North
 ------------------------    
   12   11   10   9   8   7     
                             
13                          6    
                            
   0     1    2   3   4   5      
 ------------------------     
         South --->
```

### Dependencies

- [Java 14](https://www.oracle.com/java/technologies/javase/jdk14-archive-downloads.html)
- [Spring boot](https://spring.io/projects/spring-boot)
- [Gradle](https://gradle.org/)
- [Redis](https://redis.io/)
- [Vuejs](https://vuejs.org/)
- [Yarn](https://yarnpkg.com/)
- [Docker compose](https://docs.docker.com/compose/install/)

#### Building the application

##### project setup
##### API
```
gradle build
```

##### UI
```
cd client
yarn install 
```

#### Tests
```
./gradlew test
./gradlew integrationTest
```
