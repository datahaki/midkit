// code by jph
package ch.alpine.midkit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StringMatchTest {
  @Test
  void test() {
    assertTrue("eX [hw:0,0,1]".matches("eX \\[hw:\\d+,0,1\\]"));
    assertTrue("eX [hw:12,0,1]".matches("eX \\[hw:\\d+,0,1\\]"));
  }
}
