// code by jph
package ch.alpine.midkit.put.rmi;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class RemoteMidiOutputServersTest {
  @Test
  void testSimple() {
    List<String> list = RemoteMidiPutClient.available(123);
    assertTrue(list.isEmpty());
  }
}
