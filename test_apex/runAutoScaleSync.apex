String Jsonpayload = '{"memoryConsumptionBytes":"50000000","taskDurationSeconds":"60","cpuUsageScale":"1","sleepInterval":"3000"}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Autoscale_Tests.autoscalingtester', Jsonpayload);
