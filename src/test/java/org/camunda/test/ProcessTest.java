package org.camunda.test;


import com.camunda.example.service.LoggerDelegate;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.camunda.spin.plugin.impl.SpinProcessEnginePlugin;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@Deployment(resources = "process.bpmn")
public class ProcessTest extends AbstractProcessEngineRuleTest {

  @Before
  public void setUp() {
    // processEngine.getProcessEngineConfiguration().setProcessEnginePlugins(List.of(new SpinProcessEnginePlugin()));
    Mocks.register("logger", new LoggerDelegate());
  }

  @Test
  public void testHappyPath() {

    ProcessInstance pi = runtimeService().startProcessInstanceByKey("example-process", withVariables("myVar", "myVarValue"));
    assertThat(pi).isEnded();
  }

}
