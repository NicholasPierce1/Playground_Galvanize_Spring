package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.UserPrincipal;
import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OCP_IO_Two {

    final Path pathOne = Paths.get("C://Test/Files/NIO/fileOne.txt");

    final Path pathTwo = Paths.get("C://Test/Files/NIO/fileTwo.txt");

    final Path pathDirectory = Paths.get(pathOne.getParent().toFile().getPath(), "\\SubFolder/");

    private static boolean terminate = false;

    @BeforeAll
    public void doTerminate(){
        terminate = !Files.exists(pathOne);
    }

    @Test
    public void createPaths() throws URISyntaxException {

        if(terminate)
            return;

        final Path pathOne = Paths.get("C://Test/Files/NIO/fileOne.txt");

        final Path pathTwo = Paths.get("C://Test/Files/NIO/fileTwo.txt");

        final Path pathDirectory = Paths.get(pathOne.getParent().toFile().getPath(), "\\SubFolder/");

        System.out.printf("path one exist? %s\n", String.valueOf(pathOne.toFile().exists()));
        System.out.println("path two exist? " + pathTwo.toFile().exists());
        System.out.println("path directory exist? " + pathDirectory.toFile().exists());

        // same but with file uri

        final Path pathOneFile = Paths.get(new URI("file:///C:/Test/Files/NIO/fileOne.txt"));

        final Path pathTwoFile = Paths.get(URI.create("file:///C://Test/Files/NIO/fileTwo.txt"));

        final Path pathDirectoryFile = Paths.get(URI.create("file:///C:/Test/Files/NIO/SubFolder"));

        System.out.printf("path one file exist? %s\n", String.valueOf(pathOneFile.toFile().exists()));
        System.out.println(pathOneFile.toFile().getPath());
        System.out.println("path two file exist? " + pathTwoFile.toFile().exists());
        System.out.println("path directory file exist? " + pathDirectoryFile.toFile().exists());
        List<Path> pathList = new ArrayList<>(){{
            add(pathOneFile);
            add(pathDirectory);
            add(pathDirectoryFile);
        }};

        System.out.println(pathOne.getNameCount());
        for(int i = 0; i < pathOne.getNameCount(); i++)
            System.out.println(pathOne.getName(i));

        System.out.println(pathOne);

        // using default and remote file systems

//        Path fileSystemPathOne = FileSystems.getDefault().getPath("c:","zooinfo","November",
//                "employees.txt");
//
//        FileSystem fileSystem = FileSystems.getFileSystem(
//                new URI("http://www.selikoff.net"));
//
//        Path remoteFileSystemPath = fileSystem.getPath("duck.txt");
    }

    @Test
    public void testPathComponents(){

        if(terminate)
            return;

        System.out.println(pathOne.getParent());
        System.out.println(pathOne.getFileName());
        System.out.println(pathOne.getRoot());
        System.out.println(pathOne.getFileSystem());

        Path currentPath = pathOne;
        while((currentPath = currentPath.getParent()) != null)
            System.out.println(currentPath.getFileName());

        /*
        relative paths traverse only to relative's working directory of program
        root is NULL
         */

        System.out.println();

        Path path1 = Paths.get("C:\\birds\\egret.txt");
        System.out.println("Path1 is Absolute? "+path1.isAbsolute());
        System.out.println("Absolute Path1: "+path1.toAbsolutePath());
        Path path2 = Paths.get("birds/condor.txt");
        System.out.println("Path2 is Absolute? "+path2.isAbsolute());
        System.out.println("Absolute Path2 "+path2.toAbsolutePath());

        System.out.println();

        Path path = Paths.get("/mammal/carnivore/raccoon.image");
        System.out.println("Path is: "+path);
        System.out.println("Path count is: " + path.getNameCount());
        System.out.println("Subpath from 0 to 3 is: "+path.subpath(0,3));
        System.out.println("Subpath from 1 to 3 is: "+path.subpath(1,3));
        System.out.println("Subpath from 1 to 2 is: "+path.subpath(1,2));
        /*
        upper bound exclusive
        upperbound cannot be higher than count.
        Upper and lower bound cannot be equal or any other silly combo
         */

        System.out.println();

        Path pathR1 = Paths.get("/a/fish/fileOne.txt"); // interpreted as directories (NOT FILES)
        Path pathR2 = Paths.get("/a/birds/");
        System.out.println(pathR1.relativize(pathR2));
        System.out.println(pathR2.relativize(pathR1));

        Path pathR3 = Paths.get("a");
        Path pathR4 = Paths.get("b");
        System.out.println(pathR3.relativize(pathR4)); // ../b
        System.out.println(pathR4.relativize(pathR3)); // ../a

        Path pathR5 = Paths.get("a/mamaaaaa/..").normalize(); // a
        Path pathR6 = Paths.get("b");
        System.out.println(pathR5.relativize(pathR6)); // ../b
        System.out.println(pathR6.relativize(pathR5)); // ../a

        /*
        system independent rules:
        directory root is unknown (linux: / && windows: C://)
        if one path has one, so must the other -- does not take into account what the runtime root is.
        Why no relative and abs mix? Because relative cannot recurse beyond working directory.
         */

        System.out.println();
        path1 = Paths.get("/cats/../panther");
        path2 = Paths.get("food");
        System.out.println(path1.resolve(path2)); // cats/../panther/food
        System.out.println(path2.resolve(path1)); // no crash but just returns the first path since mergedTo is relative

        path1 = Paths.get("/cats/panthers");
        path2 = Paths.get("/food");
        System.out.println(path1.resolve(path2)); // /food
        System.out.println(path2.resolve(path1)); // /cats/panther

        path1 = Paths.get("cats/panthers");
        path2 = Paths.get("food");
        System.out.println(path1.resolve(path2)); // cats/panthers/food
        System.out.println(path2.resolve(path1)); // food/cats/panthers

        System.out.println();
        Path path3 = Paths.get("E:\\data");
        Path path4 = Paths.get("E:\\user\\home");
        Path relativePath = path3.relativize(path4);
        System.out.println(relativePath);
        System.out.println(path3.resolve(relativePath));
        System.out.println(relativePath.resolve(path3));
    }
    // 470

    @Test
    public void testFiles(){

        if(terminate)
            return;

        System.out.println("exists: " + Files.exists(pathOne));
        System.out.println("don't exists: " + Files.notExists(Paths.get("C:/Test/Files/IAmNotHere.txt")));

        try {
            System.out.println("same? " + Files.isSameFile(pathOne, pathDirectory));
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }

        System.out.println();

        try {
            if(!Files.exists(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate/CreateAgain" + "/OneMoreTime"))) {
                Files.createDirectories(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate/CreateAgain"));
                Files.createDirectory(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate/CreateAgain" + "/OneMoreTime"));
            }
            else{
                Files.delete(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate/CreateAgain" + "/OneMoreTime"));
                Files.delete(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate/CreateAgain"));
                Files.delete(Paths.get(pathDirectory.toFile().getPath() + "/TestCreate"));

            }
        }
        catch(IOException e){
            System.out.println("oops: " + e);
        }

        if(Files.exists(Paths.get("/panda"))){
            try {
                /*
                with no copy options then:
                files are not copied if it already exists (exception)
                symbolic link's target are copied
                file attributes (creator, last modified) are copied over
                 */
                Files.copy(Paths.get("/panda"), Paths.get("/panda-save"));
                Files.copy(Paths.get("/panda/bamboo.txt"),
                        Paths.get("/panda-save/bamboo.txt"));
            } catch (IOException e) {
                // Handle file I/O exception...
            }
        }

        try{
            if(Files.notExists(Paths.get(pathDirectory + "/../newFileOne.txt").normalize())) {
                System.out.println("copying file");
                Files.copy(
                        pathOne,
                        Paths.get(pathDirectory + "/../newFileOne.txt").normalize()
                );
            }
            else{
                System.out.println("copying file with replace options");
                Files.copy(
                        pathOne,
                        Paths.get(pathDirectory + "/../newFileOne.txt").normalize(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }
        }
        catch(IOException e){
            System.out.println("oops: " +e);
        }

        /*
        try (InputStream is = new FileInputStream("source-data.txt");
        OutputStream out = new FileOutputStream("output-data.txt")) {
            // Copy stream data to file
            Files.copy(is, Paths.get("c:\\mammals\\wolf.txt"));
            // Copy file data to stream
            Files.copy(Paths.get("c:\\fish\\clown.xsl"), out);
        } catch (IOException e) {
            // Handle file I/O exception...
        }

        try {
            Files.move(Paths.get("c:\\zoo"), Paths.get("c:\\zoo-new"));
            Files.move(Paths.get("c:\\user\\addresses.txt"),
            Paths.get("c:\\zoo-new\\addresses.txt"));
        } catch (IOException e) {
                // Handle file I/O exception...
        }
         */

        System.out.println();
        try (BufferedReader reader = Files.newBufferedReader(pathOne, StandardCharsets.US_ASCII)) {
            System.out.println("reading");
            // can use Standard charsets (US || UTF8) -- default is UTF8 if no charset given
            // Read from the stream
            String currentLine = null;
            while((currentLine = reader.readLine()) != null)
                System.out.println(currentLine);

            List<String> lines = Files.readAllLines(pathOne);
            for(final String line : lines)
                System.out.println("read all: " + line);
        } catch (IOException e) {
            System.out.println("oops: " + e);
        }


        try (BufferedWriter writer = Files.newBufferedWriter(pathTwo) ){
            System.out.println("\nwriting");

            List<String> list = new ArrayList<String>(){{
                add("hello");
                add("world");
                add("!!!");
            }};

            for(final String line : list)
                writer.write(line);
        } catch (IOException e) {
            System.out.println("oops: " + e);
        }

        try {
            System.out.println("\ngetting size, reading modified time, and setting it to now");
            System.out.println(Files.size(pathOne));
            FileTime fileTime = Files.getLastModifiedTime(pathOne);
            System.out.println(fileTime.toInstant());
            Files.setLastModifiedTime(pathOne, FileTime.fromMillis(System.currentTimeMillis()));
        } catch (IOException e) {
            System.out.println("oops: " + e);
        }

        try{
            System.out.println("\ngetting user info system about a file");
            UserPrincipal userPrincipal = Files.getOwner(pathOne);
            UserPrincipal userPrincipal1 = FileSystems.getDefault().getUserPrincipalLookupService()
                    .lookupPrincipalByName(userPrincipal.getName());

            UserPrincipal userPrincipal2 = pathOne
                    .getFileSystem()
                    .getUserPrincipalLookupService()
                    .lookupPrincipalByName(Files.getOwner(pathOne).getName());
            System.out.println(userPrincipal.getName());
            Assertions.assertEquals(userPrincipal, userPrincipal1);
        }
        catch(IOException e){
            System.out.println("oops: " + e);
        }

        try{
            System.out.println("reading attributes with NIO's improved performance paradigm (FileAttribute class)");

            final BasicFileAttributeView basicFileAttributeView =
                    Files.getFileAttributeView(
                            pathOne,
                            BasicFileAttributeView.class
                    ); // LinkOptions given (LinkOption...)

            System.out.println(basicFileAttributeView.name());

            final BasicFileAttributes basicFileAttributes1 =
                    Files.readAttributes(
                            pathOne,
                            BasicFileAttributes.class
                    );


            /*
            info:
            file type (file, directory, link, other)
            creation and last update times
            last accessed times
            key
            size
             */
            BasicFileAttributes basicFileAttributes = basicFileAttributeView.readAttributes();

            // modified
            basicFileAttributeView.setTimes(
                    FileTime.fromMillis(Instant.now().toEpochMilli()), // last modified
                    null, // last accessed
                    null // create time
            );
        }
        catch(IOException e){
            System.out.println("oops: " + e);
        }
    }

    @Test
    public void testDirectoryTraversal(){

        if(terminate)
            return;

        /*
        note:
        defaults -- depth first with Integer.MAX_VALUE as the range of layer traversals
         */
        try {
            final Stream<Path> pathStream = Files.walk(
                    Paths.get(pathDirectory + "/../"),
                    1,
                    FileVisitOption.FOLLOW_LINKS // throws exception is circular path detected (optional)
            );
            // all files that are not directories
            pathStream.filter( p -> !Files.isDirectory(p))
                    .forEach(System.out::println);

            System.out.println("finding files");
            Files.find(
                    Paths.get(pathDirectory + "/../").normalize(),
                    1,
                    (p, a) ->
                            !a.isDirectory() &&
                                p.getFileName().toString().endsWith(".txt") &&
                                    !p.getName(p.getNameCount() -1).toString().contains("new")
            )
                    .forEach(System.out::println);

            System.out.println("list directory contents");
            Files.find(
                    Paths.get("."),
                    Integer.MAX_VALUE,
                    (p, a) -> !Files.isDirectory(p) && p.toAbsolutePath().toString().contains("\\target\\")
            )
                    .map(Path::toAbsolutePath)
                    .forEach(System.out::println);
        }
        catch(IOException e){
            System.out.println("oops: " + e);
        }
    }

    @Test
    public void testStreamLinesForPath(){

        if(terminate)
            return;

        try{
            Files.walk(Paths.get(pathDirectory + "/../").normalize())
                    .filter(p -> p.getFileName().toString().toLowerCase().startsWith("file"))
                    .map(Path::toString)
                    .sorted(Comparator.reverseOrder())
                    .map(Paths::get)
                    .forEach(
                            p -> {
                                try {
                                    Files.lines(p)
                                            .forEach(
                                                    line -> System.out.println("file (" + p.getFileName().toString() + ") " + line )
                                            );
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

            List<String> list = new ArrayList<>(){{
                add("1");
                add("A");
                add("a");
            }};
            list.sort(Comparator.naturalOrder());
            System.out.println(list);

//            Files.copy(
//                    pathOne,
//                    Paths.get("/Test/Files/NIO/mule.jpg"),
//                    StandardCopyOption.COPY_ATTRIBUTES
//            );
        }
        catch(IOException e){
            System.out.println("oops: " + e);
        }
    }

    @Test
    public void testRandom() {

        if(terminate)
            return;

        try {
            Files.copy(
                    pathOne,
                    Paths.get("/Test/Files/NIO/mule.jpg"),
                    StandardCopyOption.COPY_ATTRIBUTES,
                    StandardCopyOption.REPLACE_EXISTING
                    //,StandardCopyOption.ATOMIC_MOVE
            );

            Paths.get(new URI("file:///cheetah.txt"));
            Path path = Paths.get("/monkeys");

            Files.walk(pathDirectory, 0).forEach(p -> System.out.println(pathDirectory.equals(p)));
            System.out.println(
                    Files.isSameFile(
                            Paths.get("src"),
                            Paths.get("./src")
                    )
            );

            for(final Path localPath: Files.list(Paths.get(pathDirectory + "/..")).collect(Collectors.toList()))
                System.out.println(localPath);

            System.out.println(Paths.get(".").normalize().toAbsolutePath().equals(Paths.get("").toAbsolutePath()));

            Path path1 = Paths.get("./goat.txt").normalize();
            System.out.println(path1);

            System.out.println(Paths.get("..").toRealPath());

        } catch (IOException | URISyntaxException e) {
            System.out.println("oops: " + e);
        }
//        System.out.println(Period.ofDays(1));
//        System.out.println(Duration.ofDays(1));
    }

    @Test
    public void testRandomTwo() throws Exception{

        if(terminate)
            return;

        System.out.println(
                Paths.get("/./DirectoryA").equals(
                        Paths.get("/DirectoryA")
                )
        );

        System.out.println(
                Paths.get("/./DirectoryA").normalize().equals(
                Paths.get("/DirectoryA").normalize())
        );

        Paths.get("../A").getParent().getRoot();

        Path p = Paths.get(".");
        Files.walk(p)
                .map(z -> z.toAbsolutePath().toString())
                .filter(s -> s.endsWith(".java")).limit(10)
                .collect(Collectors.toList()).forEach(System.out::println);
        Files.find(p,Integer.MAX_VALUE,
                (w,a) -> w.toAbsolutePath().toString().endsWith(".java")).limit(10)
                .collect(Collectors.toList()).forEach(System.out::println);

        Files.newBufferedReader(pathOne);
        Files.newBufferedWriter(pathOne);
        Files.newInputStream(pathOne);
        Files.newOutputStream(pathOne);

        BufferedWriter writer= Files.newBufferedWriter(Paths.get(pathDirectory.toString(), "CreateMeWithWriter.txt"),StandardOpenOption.APPEND);
        writer.write("\nhello!!");
        writer.flush();
        writer.close();

        // default: will always override if exists or create new
        OutputStream outputStream = Files.newOutputStream(Paths.get(pathDirectory.toString(), "CreateMeWithStream.txt"));
        outputStream.write(new byte[]{'h','i','!'});
        outputStream.flush();
        outputStream.close();

        System.out.println(Paths.get(pathDirectory.toString() + "../././").toRealPath());
        System.out.println(Paths.get("someRandomStuff/" + "../././").toAbsolutePath());

        Stream.of().forEach(o -> ++x);

        Assertions.assertNotEquals(
                Paths.get("./"),
                makeAbsolute(Paths.get("./"))
        );

        Assertions.assertTrue(
                Files.isSameFile(
                        Paths.get("./"),
                        makeAbsolute(Paths.get("./"))));


        System.out.println(makeAbsolute(Paths.get("./")));

        System.out.println(Paths.get(".").toAbsolutePath());

    }
    int x = 3;

    public Path makeAbsolute(Path p) {
        if(p!=null && !p.isAbsolute())
            return p.toAbsolutePath();
        return p;
        }

    /*
    Files.copy takes either two paths, an input stream + path, or a path + output stream
    Files.isSameFile requires handling of IOExceptions
     */
}
