package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import com.salesforce.functions.jvm.sdk.SalesforceFunction;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import logger.FCLogger;

import java.util.Map;
import java.util.Optional;

/**
 * Describe autoscalingtesterFunction here.
 */
public class AutoscalingtesterFunction implements SalesforceFunction<Object, FunctionOutput> {
  private static final Logger LOGGER = LoggerFactory.getLogger(AutoscalingtesterFunction.class);

  @Override
  public FunctionOutput apply(InvocationEvent<Object> event, Context context)
      throws Exception {

    FCLogger fc = new FCLogger(event, context, LOGGER);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    Map<String, Object> eventMap = objectMapper.convertValue(event, Map.class);
    // first allocate however much memory is necessary
    // assume memory is in bytes. each int takes up 4 bytes.
    long memoryConsumptionBytes = PropertyUtils.getProperty(eventMap, "data.memoryConsumptionBytes") != null ? Long.valueOf(BeanUtils.getNestedProperty(eventMap, "data.memoryConsumptionBytes")) : 0;
    int size = (int)(memoryConsumptionBytes / 4);
    int[] l = new int[size];

    long taskDurationSeconds = PropertyUtils.getProperty(eventMap, "data.taskDurationSeconds") != null ? Long.valueOf(BeanUtils.getNestedProperty(eventMap, "data.taskDurationSeconds")) : 0;
    long durationMilliseconds = Optional.ofNullable(taskDurationSeconds)
                                        .map(tds -> tds * 1000)
                                        .orElse(60000l);
    long endTime = System.currentTimeMillis() + durationMilliseconds;
    // how to split the array
    int numChunks = 10;
    // controls how many chunks should be processed vs. slept on
    int mCpuUsageScale = PropertyUtils.getProperty(eventMap, "data.cpuUsageScale") != null ? Integer.valueOf(BeanUtils.getNestedProperty(eventMap, "data.cpuUsageScale")) : 0;
    int cpuUsageScale = Optional.ofNullable(mCpuUsageScale)
                                .map(cus -> Math.min(cus, numChunks))
                                .orElse(numChunks);
    int sizeChunk = l.length / numChunks;

    long mSleepInterval = PropertyUtils.getProperty(eventMap, "data.sleepInterval") != null ? Long.valueOf(BeanUtils.getNestedProperty(eventMap, "data.sleepInterval")) : 0;
    long sleepInterval = Optional.ofNullable(mSleepInterval)
                                  .map(val -> Math.min(val, durationMilliseconds)) // prevent (reduce likelihood of) timeouts
                                  .orElse(1000l);
    double countExecutions = 0;
    // refactor to do a large, fixed quantity of work....
    for (int q = 0; q < mCpuUsageScale; ++q) {
      // populateRandomNumbers(l);

      int startIdx = 0;
      for (int i = 0; i < numChunks; ++i) {
        // alternate between sleeping and performing some activity
        // if (i > cpuUsageScale) {
        //   Thread.sleep(sleepInterval);
        //   continue;
        // }

        for (int j = startIdx; j < startIdx + sizeChunk; ++j) {
          l[j] = (int)(Math.pow((l[j] * 2), 3));
        }
        startIdx += sizeChunk;
        countExecutions += sizeChunk;
      }
    }

    fc.fc_log_invocation_data("");
    return new FunctionOutput(System.getenv("HOSTNAME"), countExecutions);
  }

  //private void populateRandomNumbers(int[] l) {
  //  SecureRandom sr = new SecureRandom();
  //  for (int i = 0; i < l.length; ++i) {
  //    l[i] = sr.nextInt();
  //  }
  //}
}