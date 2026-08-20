package org.scivault.qf;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExpressionValidatorTest {
  @Test void acceptsArithmetic(){ExpressionValidator.Result r=ExpressionValidator.validate("v * t + 0.5 * a * t ** 2");assertTrue(r.isValid());assertTrue(r.getVariables().containsAll(java.util.Arrays.asList("v","t","a")));}
  @Test void rejectsFunctionCalls(){assertFalse(ExpressionValidator.validate("system(1)").isValid());}
}
