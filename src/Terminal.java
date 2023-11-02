import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;

public class Terminal {
  Parser parser;
  Vector<String>  History;
  Terminal() {
    parser = new Parser();
    History=new Vector<String>();
  }
  private void echo() {
    String[] args = parser.getArgs();
    for(int i=0;i<args.length;i++){
      System.out.print(args[i]+" ");
    }
    System.out.println();
  }
  private String pwd() {
    String currentPath = System.getProperty("user.dir");
    System.out.println(currentPath);
    return currentPath;
  }
  /**
   * @param args
   */
  private void cd(String[] args) {
    Path currentDir = Paths.get(System.getProperty("user.dir"));
    if(args.length==0){
      String userHome = System.getProperty("user.home");
      System.setProperty("user.dir", userHome);
    }
    else if(args[0].equals("..")) {

      Path parentDir = currentDir.getParent();
      if (parentDir != null) {
        System.setProperty("user.dir", parentDir.toString());
    } else {
        System.out.println("Already at the root. Cannot move to the previous directory.");
    }
    }
    else {
      try{
      Path basepath = Paths.get(System.getProperty("user.dir"));
      Path absoluteGivenPath = Paths.get(args[0]);
      Path resolvedAbsolute = basepath.resolve(absoluteGivenPath);
      boolean isDirectory = Files.isDirectory(resolvedAbsolute);

        if (isDirectory) {
          System.setProperty("user.dir", resolvedAbsolute.toString());
        } else {
            System.out.println("The path does not point to a directory: " + resolvedAbsolute);
        }
      
    }
    catch(Exception e) {
      System.out.println("Invalid path "+ args[0]);
     }
   }
  
  }
  private void ls() {}
  private void mkdir() {
    String[] args = parser.getArgs();
    for(String arg : args) {
      Path path = Paths.get(arg);
      try {
        if (!Files.exists(path)) {
          Files.createDirectory(path);
          System.out.println("dir created");
        } else {
          System.out.println("dir already exists");
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
                System.out.println("dir "+ path + " deleted");
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
        Path basepath = Paths.get(System.getProperty("user.dir"));
        Path resolvedAbsolute = basepath.resolve(path);
      Files.createFile(resolvedAbsolute);
      System.out.println(path + " created successfully");
    } catch (InvalidPathException | IOException e) {
      System.out.println("Invalid path "+ path);
    }
  }
  private void cp() {}
  private void rm() {
    String arg = parser.getArgs()[0];
    Path path = Paths.get(arg);
    try {
      Files.delete(path);
      System.out.println("file "+ path + " deleted");
    } catch (InvalidPathException | IOException e) {
      System.out.println("Invalid path "+ path);
    }
  }
  private void cat() {
    String arg = parser.getArgs()[0];
    Path path = Paths.get(arg);
    try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
      String line;
      while ((line = reader.readLine()) != null) {
          System.out.println(line);
      }
    }
     catch (InvalidPathException | IOException e) {
      System.out.println("Invalid path "+ path);
    }
    if(parser.getArgs().length>1){
     arg = parser.getArgs()[1];
     path = Paths.get(arg);
    try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
      String line;
      while ((line = reader.readLine()) != null) {
          System.out.println(line);
      }
    }
     catch (InvalidPathException | IOException e) {
      
    }
  }
  }
  private void history() {
    for (int i=0;i<History.size();i++) {
      System.out.println((i+1)+"  "+History.get(i));
  }
  }

  // TODO cp -r, ls -r
  public void chooseCommandAction(){
    Scanner scanner = new Scanner(System.in);
    boolean exit = false;
    while (!exit) {
      System.out.print(">> ");
      String command = scanner.nextLine();
      parser.parse(command);
      String commandName = parser.getCommandName();
      History.add(command);
      switch (commandName) {
        case "echo":
        echo();
        break;
        case "cd":
        cd(parser.getArgs());
        break;
        case "pwd":
        pwd();
        break;
        case "cat":
        cat();
        break;
        case "history":
        history();
        break;
        case "mkdir":
          mkdir();
          break;
        case "rmdir":
          rmdir();
          break;
        case "touch":
          touch();
          break;
        case "rm":
          rm();
          break;
        case "exit":
          exit = true;
          break;
        default:
          System.out.println("command " + command + " not found");
          History.remove(History.size() - 1);
          break;
      }
    }
  }
  public static void main(String[] args){}
}
