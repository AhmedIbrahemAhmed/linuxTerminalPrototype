import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;

public class Terminal {
  Parser parser;
  Terminal() {
    parser = new Parser();
  }
  private void echo() {}
  private String pwd() {return "";}
  private void cd(String[] args) {}
  private void ls() {}
  private void mkdir() {
    String[] args = parser.getArgs();
    for(String arg : args) {
      Path path = Paths.get(arg);
      try {
        if (!Files.exists(path)) {
          Files.createDirectory(path);
          System.out.println("Directory created");
        } else {
          System.out.println("Directory already exists");
        }
      } catch (InvalidPathException | IOException e) {
        System.out.println("Invalid path "+ path);
      }
    }
  }
  private void rmdir() {
    String[] args = parser.getArgs();
    String arg = args[0];
    try {
      if (Objects.equals(arg, "*")) {
        String userDirectory = Paths.get(System.getProperty("user.dir")).toString();
        Path currPath = Paths.get(userDirectory);
        Files.walk(currPath)
            .sorted(Comparator.reverseOrder())
            .filter(Files::isDirectory)// check if the path belongs to a dir
            .filter(path -> {// check if dir is empty
              try {
                DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);
                return !dirStream.iterator().hasNext();
              } catch (IOException e) {
                System.out.println("dir "+ path +" is not empty");
              }
              return false;
            })
            .forEach(path -> {
              try {
                Files.delete(path);
                System.out.println("directory: "+ path + " deleted");
              } catch (InvalidPathException | IOException e) {
                System.out.println("Invalid path "+ path);
              }
            });
      } else {
        Path currPath = Paths.get(arg);
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(currPath);
        if (Files.isDirectory(currPath) && !dirStream.iterator().hasNext()) {
          Files.delete(currPath);
          System.out.println(currPath + " deleted");
        } else {
          System.out.println("error this is not a directory or directory is not empty");
        }
      }
    } catch (InvalidPathException | IOException e) {
      System.out.println("Invalid path "+ arg);
    }
  }
  private void touch() {
    String[] args = parser.getArgs();
    Path path = Paths.get(args[0]);
    try {
      Files.createFile(path);
      System.out.println("your file: "+ path + " created successfully");
    } catch (InvalidPathException | IOException e) {
      System.out.println("Invalid path "+ path);
    }
  }
  private void cp() {}
  private void rm() {}
  private void cat() {}
  private void history() {}

  // TODO cp -r, ls -r
  public void chooseCommandAction(){
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;

    while (!exit) {
      System.out.print(">> ");
      String command = scanner.nextLine();
      parser.parse(command);
      switch (parser.getCommandName()) {
        case "mkdir":
          mkdir();
          break;
        case "rmdir":
          rmdir();
          break;
        case "touch":
          touch();
          break;
        case "exit":
          exit = true;
          break;
      }
    }
  }
  public static void main(String[] args){}
}
