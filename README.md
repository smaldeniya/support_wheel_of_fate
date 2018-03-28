# support_wheel_of_fate

### Support Wheel Of Fate Java Implementation

This code sample has a an implementation of support wheel of fate with following functionality.



 - Maven has used as the build too.
 - To run this locally you need to update mongo url in "src/main/resources/default.config.properties"
 - This implementation store Engineer details and Schedule for a day in MongoDB collections called 'engineer' and 'day_schedule'
 - DB feature has been implemented using repository design pattern and queries has implemented as specifications.
 - This implementation consist of AWS lambda handler function, so once the jar is built, you can upload it to AWS Lambda and configure it to a REST API using API manager.
 - A test server with REST API implemented using [msf4j java microservice framework](https://github.com/wso2/msf4j) and can be started using following mvn command. (once started this runs in 8082 port)
> mvn clean test-compile exec:java
- This test server has following endpoints exposed.
1. To list all schedules in db
> HTTP GET  http://localhost:8082/api/v1/schedule/getAllSchedules
2. To get schedule for a given date
> curl -X POST \
  http://localhost:8082/api/v1/schedule/getSchedule \
  -H 'Content-Type: application/json' \
  -d '{
  "date": "2018-03-28T17:32:30.183Z"
}'
- This code has 100% test coverage using jUnit
- Once the jar is build run below code to populate set of 10 test Engineers to the DB
> mvn test -Dtest=com.astro.prod.PopulateTestData
