String Jsonpayload = '{"memoryConsumptionBytes":1024,"taskDurationSeconds":10,"cpuUsageScale":2,"sleepInterval":100}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Autoscale_Tests.autoscalingtester', Jsonpayload);
