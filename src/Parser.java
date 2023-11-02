import java.util.ArrayList;
import java.util.Objects;

public class Parser {
  private String commandName;
  private String[] args;
  private String[] flags;
  public Parser() {
    this.commandName = "";
  }
  public boolean parse(String input) {
    String[] array;
    array = input.split(" ");

    this.args = new String[array.length - 1];
    this.flags = new String[array.length - 1];
    this.commandName = array[0];

    for (int i=1, j = 0, k = 0; i < array.length; i++) {
      if (array[i].charAt(0) == '-') {
        flags[j++] = array[i];
      } else {
        args[k++] = array[i];
      }
    }
    return !Objects.equals(commandName, "");
  }
  public String getCommandName() {
    return commandName;
  }
  public String[] getArgs() {
    return args;
  }
  public String[] getFlags() {
    return flags;
  }
}
