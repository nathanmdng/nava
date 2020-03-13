# nava
simple program that takes a schema and data file and posts json to an endpoint

- To run this program, you will need to have maven and a java 1.8+ installed.

- To download and run
```
git clone https://github.com/nathanmdng/nava.git
mvn clean install
mvn spring-boot:run
```

The console output should contain the following lines which is the response and status code of the posted JSON

```
received response {"measure_id":"IA_PCMH","performance_year":2017,"is_required":true,"minimum_score":0} status code 201
received response {"measure_id":"ACI_LVPP","performance_year":2017,"is_required":false,"minimum_score":-1} status code 201
received response {"measure_id":"CAHPS_1","performance_year":2017,"is_required":false,"minimum_score":10} status code 201
```
