// code by jph
package ch.alpine.midkit;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ch.alpine.tensor.Unprotect;

class MidiListingTest {
  @TempDir
  Path folder;

  @Test
  void testListing() throws Exception {
    Path file = Unprotect.resourcePath("/mid/kv467_v1.mid");
    MidiListing midiListing = MidiListing.of(file);
    midiListing.exportTo(folder.resolve("list.txt"));
    midiListing.exportToHtml(folder.resolve("list.htm"));
  }
}
