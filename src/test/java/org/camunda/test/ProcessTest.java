package org.camunda.test;


import com.camunda.example.service.LoggerTaskListener;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.spin.plugin.variable.SpinValues;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@Deployment(resources = "process.bpmn")
public class ProcessTest  {

  @Rule
  public ProcessEngineRule processEngineRule = new ProcessEngineRule();


  @Before
  public void setUp() {
    // processEngine.getProcessEngineConfiguration().setProcessEnginePlugins(List.of(new SpinProcessEnginePlugin()));
    Mocks.register("listener", new LoggerTaskListener());
  }

  @Test
  public void testHappyPath() {

    String JSON = "[\"a\",\"b\",\"c\"]";

    ProcessInstance pi = runtimeService().startProcessInstanceByKey("example-process",
        withVariables("myList",SpinValues.jsonValue(JSON).create()));

    assertThat(pi).isWaitingAt("DoSomethingTask");
  }
}
