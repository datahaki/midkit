// code by jph
package sys;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class ProcessManager {
  private final List<ProcessBuilder> list;

  /** @param list will not be modified (but contents might) */
  public ProcessManager(List<ProcessBuilder> list) {
    this.list = list;
    // ---
    list.forEach(this::safe);
  }

  private void safe(ProcessBuilder processBuilder) {
    switch (OperatingSystem.TYPE) {
    case WINDOWS:
      List<String> command = processBuilder.command();
      String exec = command.get(0);
      if (!exec.startsWith("\"") && exec.contains(" "))
        command.set(0, '\"' + exec + '\"');
      break;
    default:
      break;
    }
  }

  public ProcessManager(ProcessBuilder processBuilder) {
    this(List.of(processBuilder));
  }

  public Process executeSafeWaitFor(Path file) throws Exception {
    Process process = executeSafe(file);
    process.waitFor();
    return process;
  }

  public boolean bat = true;
  public boolean log = true;

  public void setSilent() {
    bat = false;
    log = false;
  }

  public Process executeSafe(Path file) {
    Filename filename = new Filename(file);
    Path directory = file.getParent();
    Path batchFile = filename.withExtension("bat");
    if (bat)
      toBatchFile(batchFile);
    Process process = null;
    try {
      switch (OperatingSystem.TYPE) {
      case WINDOWS: {
        // execute as batch
        toBatchFile(batchFile);
        if (!bat)
          batchFile.toFile().deleteOnExit();
        ProcessBuilder processBuilder = new ProcessBuilder(batchFile.toAbsolutePath().toString());
        processBuilder.directory(directory.toFile());
        if (log)
          processBuilder.redirectError(filename.withExtension("log").toFile());
        process = processBuilder.start();
        break;
      }
      case LINUX:
        int count = 0;
        for (ProcessBuilder processBuilder : list) {
          processBuilder.directory(directory.toFile());
          if (log)
            processBuilder.redirectError(filename.withExtension("log").toFile()); // meaningful for list of tasks!?
          process = processBuilder.start();
          ++count;
          if (count < list.size())
            process.waitFor();
        }
        break;
      default:
        break;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return process;
  }

  @Override
  public String toString() {
    StringBuilder stringBuffer = new StringBuilder();
    for (ProcessBuilder processBuilder : list) {
      StringBuffer commandBuffer = new StringBuffer();
      processBuilder.command().forEach(s -> commandBuffer.append(" " + s));
      stringBuffer.append(commandBuffer.substring(1) + '\n');
    }
    return stringBuffer.toString();
  }

  public void toBatchFile(Path file) {
    try {
      Files.write(file, toString().getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
