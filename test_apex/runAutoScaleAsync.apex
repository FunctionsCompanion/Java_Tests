String Jsonpayload = '{"memoryConsumptionBytes":"50000000","taskDurationSeconds":"8","cpuUsageScale":"1","sleepInterval":"3000"}';
FCFunction.getAndInvoke('Autoscale_Tests.autoscalingtester', Jsonpayload, 'FCCallback');
