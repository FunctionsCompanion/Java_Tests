String Jsonpayload = '{"name": "MyAccount","accountNumber": "123456789","industry": "Technology","type": "Prospect","website": "www.salesforce.com"}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Java_Tests.salesforcesdkjava', Jsonpayload);
String jsonResponse = invocation.getResponse();
system.debug(jsonResponse);
