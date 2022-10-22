String Jsonpayload = '{"latitude": "36.169090","longitude": "-115.140579","length": 5}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Java_Tests.processlargedatajava', Jsonpayload);
//String jsonResponse = invocation.getResponse();
//system.debug(jsonResponse);
