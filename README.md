SCBTask

## Env specification
- Java 17.0.7
- Cucumber jvm 7.14.1
- JUnit 5.10.1 and JUnit 4.13.1 (for execute RunTest)
- Maven
- Intellij Idea Community edition 2023.3.2 (or higher) with next plugins: maven, cucumber, lombok

## How to run tests
1. Clone the project
2. Open Project in `Intellij Idea`
3. In Maven tab click on button `Lifecycle` and double-click on `install`
4. Open Class RunTest.java (use shortcut: twice press Shift key) and type RunTest
5. In line 16 click on Run button to run class: `public class Runtest()`
6. Wait until test run will be finished
7. To read report navigate to: `target/cucumber-reports` and open file `cucumber-pretty` in browser (also, you can rename file to add extension `.html`)

## Reports
Report can be find in directory `report`.  

## Author's comment
For solving the task were used: Stream API, Collections, REST API, Lombok, Cucumber and Cucumber's Pretty reports
Project could be and will be refactored in the future.  
As minimum: needs to make RestAPI request builder and split BooksDefinition class 
Will be good to add some comments for some methods.

Good luck and have fun!
