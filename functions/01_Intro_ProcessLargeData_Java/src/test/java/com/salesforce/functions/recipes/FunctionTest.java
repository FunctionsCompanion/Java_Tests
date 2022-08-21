package com.salesforce.functions.recipes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.salesforce.functions.jvm.sdk.Context;
import com.salesforce.functions.jvm.sdk.InvocationEvent;
import org.junit.Test;

public class FunctionTest {

  @Test
  public void testSuccess() throws Exception {
    ProcessLargeDataFunction function = new ProcessLargeDataFunction();
    InvocationEvent<Object> eventMock = createEventMock(36.169090, -115.140579, 5);
    int length = ((FunctionInput) eventMock.getData()).getLength();
    FunctionOutput functionOutput = function.apply(eventMock, createContextMock());
    assertEquals(functionOutput.getSchools().size(), length);
  }

  @Test
  public void testNoParams() throws Exception {
    ProcessLargeDataFunction function = new ProcessLargeDataFunction();
    InvocationEvent<Object> eventMock = createEventMock();
    FunctionOutput functionOutput = function.apply(eventMock, createContextMock());
    assertEquals(functionOutput.getSchools().size(), 0);
  }

  private Context createContextMock() {
    return mock(Context.class);
  }

  @SuppressWarnings("unchecked")
  private InvocationEvent<Object> createEventMock(
      double latitude, double longitude, int length) {
    InvocationEvent<Object> eventMock = mock(InvocationEvent.class);
    when(eventMock.getData()).thenReturn(new FunctionInput(latitude, longitude, length));
    return eventMock;
  }

  @SuppressWarnings("unchecked")
  private InvocationEvent<Object> createEventMock() {
    InvocationEvent<Object> eventMock = mock(InvocationEvent.class);
    when(eventMock.getData()).thenReturn(new FunctionInput());
    return eventMock;
  }
}
