String Jsonpayload = '{"memoryConsumptionBytes":1000000,"taskDurationSeconds":60,"cpuUsageScale":10,"sleepInterval":3000}';
functions.FunctionInvocation invocation = FCFunction.getAndInvoke('Autoscale_Tests.autoscalingtester', Jsonpayload);
