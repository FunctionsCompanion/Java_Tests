String Jsonpayload = '{"memoryConsumptionBytes":"10000000","taskDurationSeconds":"60","cpuUsageScale":"10","sleepInterval":"3000"}';
FCFunction.getAndInvoke('Autoscale_Tests.autoscalingtester', Jsonpayload, 'FCCallback');
