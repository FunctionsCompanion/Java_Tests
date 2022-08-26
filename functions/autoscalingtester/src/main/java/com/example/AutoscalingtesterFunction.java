package com.example;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;
import com.salesforce.functions.jvm.sdk.data.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import logger.FCLogger;

/**
 * Describe autoscalingtestertesterFunction here.
 */
public class autoscalingtesterTesterFunction implements SalesforceFunction<FunctionInput, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(autoscalingtesterTesterFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<FunctionInput> event, Context context)
      throws Exception {
    // first allocate however much memory is necessary
    // assume memory is in bytes. each int takes up 4 bytes.
    long memoryConsumptionBytes = event.getData().getMemoryConsumptionBytes() == null ? 0 : event.getData().getMemoryConsumptionBytes();
    int size = (int)(memoryConsumptionBytes / 4);
    int[] l = new int[size];

    long durationMilliseconds = Optional.ofNullable(event.getData().getTaskDurationSeconds())
                                        .map(tds -> tds * 1000)
                                        .orElse(60000l);
    long endTime = System.currentTimeMillis() + durationMilliseconds;

    // how to split the array
    int numChunks = 10;
    // controls how many chunks should be processed vs. slept on
    int cpuUsageScale = Optional.ofNullable(event.getData().getCpuUsageScale())
                                .map(cus -> Math.min(cus, numChunks))
                                .orElse(numChunks);
    int sizeChunk = l.length / numChunks;
    
    long sleepInterval = Optional.ofNullable(event.getData().getSleepInterval())
                                  .map(val -> Math.min(val, durationMilliseconds)) // prevent (reduce likelihood of) timeouts
                                  .orElse(1000l);

    double countExecutions = 0;
    // then spin and perform some activity
    while (System.currentTimeMillis() < endTime) {
      populateRandomNumbers(l);

      int startIdx = 0;
      for (int i = 0; i < numChunks; ++i) {
        // alternate between sleeping and performing some activity
        if (i > cpuUsageScale) {
          Thread.sleep(sleepInterval);
          continue;
        }

        for (int j = startIdx; j < startIdx + sizeChunk; ++j) {
          l[j] = (int)(Math.pow((l[j] * 6) / 7, 3));
        }
        startIdx += sizeChunk;
        countExecutions += sizeChunk;
      }
    }
    
    return new FunctionOutput(System.getenv("HOSTNAME"), countExecutions);
  }

  private void populateRandomNumbers(int[] l) {
    SecureRandom sr = new SecureRandom();
    for (int i = 0; i < l.length; ++i) {
      l[i] = sr.nextInt();
    }
  }
}