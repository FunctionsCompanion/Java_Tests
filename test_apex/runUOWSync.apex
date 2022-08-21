String Jsonpayload = '{"firstName": "Kylie","lastName": "Hudson","accountName": "Universal Containers","website": "www.universalcontainers.com","subject": "Faulty Container","description": "My container spilled into the ocean and you have lost all of my possessions!"}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Java_Tests.unitofworkjava', Jsonpayload);
String jsonResponse = invocation.getResponse();
system.debug(jsonResponse);
