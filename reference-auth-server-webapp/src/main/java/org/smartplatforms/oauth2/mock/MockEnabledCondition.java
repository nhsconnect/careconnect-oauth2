package org.smartplatforms.oauth2.mock;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MockEnabledCondition implements Condition {

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

    if (context.getEnvironment().containsProperty("${mock.endpoints.enabled}") ) {
      return context.getEnvironment().getProperty("${mock.endpoints.enabled}").equalsIgnoreCase("true");
    }
    return true;
  }
}
