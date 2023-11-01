import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.Objects;

public class Terminal {
  Parser parser;

  private void echo() {}
  private String pwd() {return "";}
  private void cd(String[] args) {}
  private void ls() {}
  private void mkdir() {
    String[] args = parser.getArgs();
    for(String arg : args) {
      try {
        Path path = Paths.get(arg);
        if (!Files.exists(path)) {
          Files.createDirectory(path);
          System.out.println("Directory created");
        } else {
          System.out.println("Directory already exists");
        }
      } catch (InvalidPathException | IOException e) {
        throw new RuntimeException(e);
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
                throw new RuntimeException(e);
              }
            })
            .forEach(path -> {
              try {
                Files.delete(path);
                System.out.println("directory: "+ path + " deleted");
              } catch (InvalidPathException | IOException e) {
                throw new RuntimeException(e);
              }
            });
      } else {
        Path currPath = Paths.get(arg);
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(currPath);
        if (Files.isDirectory(currPath) && !dirStream.iterator().hasNext()) {
          Files.delete(currPath);
          System.out.println(currPath + "deleted");
        } else {
          System.out.println("error this is not a directory or directory is not empty");
        }
      }
    } catch (InvalidPathException | IOException e) {
      throw new RuntimeException(e);
    }
  }
  private void touch() {}
  private void cp() {}
  private void rm() {}
  private void cat() {}
  private void history() {}

  // TODO cp -r, ls -r
  public void chooseCommandAction(){

  }
  public static void main(String[] args){}
}
