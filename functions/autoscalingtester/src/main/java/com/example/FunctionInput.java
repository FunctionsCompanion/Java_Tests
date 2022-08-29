package com.example;

public class FunctionInput {
  private long memoryConsumptionBytes;
  private long taskDurationSeconds;
  private int cpuUsageScale;
  private int sleepInterval;

  public FunctionInput() {}

  public FunctionInput(long memoryConsumptionBytes, int taskDurationSeconds, int cpuUsageScale, int sleepInterval) {
    this.memoryConsumptionBytes = memoryConsumptionBytes;
    this.taskDurationSeconds = taskDurationSeconds;
    this.cpuUsageScale = cpuUsageScale;
    this.sleepInterval = sleepInterval;
  }

  public long getMemoryConsumptionBytes() {
    return memoryConsumptionBytes;
  }

  public long getTaskDurationSeconds() {
    return taskDurationSeconds;
  }

  public int getCpuUsageScale() {
    return cpuUsageScale;
  }

  public int getSleepInterval() {
    return sleepInterval;
  }
}
