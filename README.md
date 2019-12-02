# MoneyTransfer - A  simple Java REST API(without Spring) <br> to transfer money between accounts <br>
### Requirements
> Java 8+
### Quick Start - Run
1.In Project Root Directory,type <br/>
```$xslt
 
 java -jar money-transfer-demo-1.0-SNAPSHOT.jar (App will be running on http://localhost:8080 ,if nothing is running on 8080)
```
or
```$xslt
1) ./gradlew build
2) ./gradlew startApp (App will be running on http://localhost:8080 ,if nothing is running on 8080)
```
### Run All Tests[Make sure nothing is running on port 8080]
```$xslt
./gradlew test
```

### Problem
```text
Design and implement a RESTful API (including data model and the backing implementation)
for money transfers between accounts.
Explicit requirements:
1. You can use Java, Scala or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require
a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.
Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.
```
### Assumptions 
######>Assumption 1 : Java was chosen. 
######>Assumption 2 : Keep it simple . What's simpler than Guice+Spark . No ORM was used to keep things on a simple level and data was stored in a concurrent hashmap
######>Assumption 3: API invoked by multiple systems & services? <br> Multithreading is enabled by using guice, stateless microservices and concurrent hashmaps while care was taken to use thread safety while accessing data
######> Assumption 4: Validations are handled at appropriate levels i.e input at controller, business at service and data at repository level.
## Application usage



## Application usage
### Accounts
#### Create an account
The following request creates an account and returns the account  with 201 status code
```
    POST localhost:8080/accounts
    { 
    "name":"ace", 
    "phone":"ace@g.com", 
    "initBalance":5000 
    }
```
The name should be of more than three letters and name and email combination should be unique.<br>
Also the initial balanbce should be positive<br/>
Response:
```
    HTTP 201 Created
    { 
        "name":"ace", 
        "phone":"ace@g.com", 
        "initBalance":5000 
        "id":1234
    }
```
#### Get Accounts details
The following request gets account details:
```
    GET http://localhost:8080/accounts/:id
    
```
in case the account is not found in the system 404 error code will be thrown <br>
Response:
```
    HTTP 200 OK
     {
       "name":"ace", 
        "phone":"ace@g.com", 
        "initBalance":5000 
        "id":1234
     }
   
```


#### Create Money Transfer Transaction
Transfer money from one account to another:
```
    POST http://localhost:8080/accounts/1/transaction [Account with id =1 will be debited] 
    
    { 
        "destinationAccountId":123
        "amount":13
    }
```
If the accounts are  not found in the system 404 will be returned or else if sufficient amount is not present 400 will be returned
Response:
```
    HTTP 204 Accepted
```
