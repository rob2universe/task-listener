/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camunda.example.service;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.model.bpmn.BpmnModelException;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperties;
import org.camunda.bpm.model.bpmn.instance.camunda.CamundaProperty;
import org.springframework.stereotype.Component;

@Slf4j
@Component("listener")
public class LoggerTaskListener implements TaskListener {

  @Override
  public void notify(DelegateTask task) {

    log.info("\n\n TaskListener invoked by processDefinitionId: {}, taskDefinitionId: {}, Name: '{}'," +
            " processInstanceId: {}, executionId: {} \n",
        task.getProcessDefinitionId(),
        task.getTaskDefinitionKey(),
        task.getName().replaceAll("\n", " "),
        task.getProcessInstanceId(),
        task.getExecutionId());

    // log extension properties on Task
    var taskExtElements = task.getBpmnModelElementInstance().getExtensionElements();
    if (taskExtElements != null) {
      try {
        var camProps = taskExtElements.getElementsQuery()
            .filterByType(CamundaProperties.class).singleResult();
        if (camProps != null) {
          for (CamundaProperty prop : camProps.getCamundaProperties())
            log.info("Camunda property {} on task with value {}", prop.getCamundaName(), prop.getCamundaValue());
        }
      } catch (BpmnModelException e) {
        log.error(e.getMessage());
      }
    }

    // log extension properties on Process
    for (Process p : task.getBpmnModelInstance().getModelElementsByType(Process.class)) {
      var processExtElements = p.getExtensionElements();
      if (processExtElements != null) {
        try {
          var camProps = processExtElements
              .getElementsQuery().filterByType(CamundaProperties.class).singleResult();
          if (camProps != null) {
            for (CamundaProperty prop : camProps.getCamundaProperties())
              log.info("Camunda property {} on process with value {}", prop.getCamundaName(), prop.getCamundaValue());
          }
        } catch (BpmnModelException e) {
          log.error(e.getMessage());
        }
      }
    }
  }
}