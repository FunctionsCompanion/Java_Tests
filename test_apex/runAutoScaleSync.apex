String Jsonpayload = '{"memoryConsumptionBytes":1024,"taskDurationSeconds":10,"cpuUsageScale":2,"sleepInterval":100}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Java_Tests.autoscalingtester', Jsonpayload);
String jsonResponse = invocation.getResponse();
system.debug(jsonResponse);
