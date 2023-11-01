import java.util.ArrayList;
import java.util.Objects;

public class Parser {
  private String commandName;
  private String[] args;
  private String[] flags;
  public Parser() {
    this.commandName = "";
    this.args = new String[10];
    this.flags = new String[10];
  }
  public boolean parse(String input) {
    String[] array;
    array = input.split(" ");
    this.commandName = array[0];
    for (int i=1; i < array.length; i++) {
      if (array[i].charAt(0) == '-') {
        flags[i] = array[i];
      } else {
        args[i] = array[i];
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
