package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Transient;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.*;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OCP_IO_One {

    // file hardcoded paths
    final File fileOne = new File("C:\\Test\\Files\\fileOne.data"); // no relative or things get hard here
    final File testDirectory = new File("C:/Test/Files");
    final File fileTwo = new File(testDirectory, "fileTwo.txt");
    final File fileThree = new File(testDirectory, "fileThree.txt");
    final File bufferStreamTestFile = new File(testDirectory, "bufferTestFile.txt");
    final File printStreamFile = new File(testDirectory, "printStreamFile.txt");

    private static boolean terminate = false;

    @BeforeAll
    public void doTerminate(){
        terminate = !fileOne.exists();
    }

    @Test
    public void testCreateFileOnFileAndDirectory(){

        if(terminate)
            return;
        // File One
        final File fileOne = new File("C:\\Test\\Files\\fileOne.bin"); // no relative or things get hard here
        System.out.println(fileOne.exists());
        System.out.println(fileOne.isFile());
        System.out.println(fileOne.length());
        System.out.println(Instant.ofEpochMilli(fileOne.lastModified()));

        // File Directory
        final File testDirectory = new File("C:/Test/Files");
        System.out.println(testDirectory.exists());
        System.out.println(testDirectory.isDirectory());

        for(final File subFile: testDirectory.listFiles())
            System.out.println(subFile.getName());

        // File Two with relative
        final File fileTwo = new File(testDirectory, "fileTwo.txt");
        System.out.println(fileTwo.exists());
        System.out.println(fileTwo.isAbsolute());
        //System.out.println(fileTwo.getPath());
    }

    @Test
    public void testFileStuff(){

        if(terminate)
            return;

        System.out.println(System.getProperty("file.separator"));
        System.out.println(java.io.File.separator);
        Arrays.stream(System.getProperty("line.separator").split(""))
                .unordered()
                .parallel()
                .map(s -> (int)s.charAt(0))
                .forEach(System.out::println);
    }

    @Test
    public void testFileStreamsOne(){

        if(terminate)
            return;

        try(final Closeable bufferedReader1 = new BufferedReader((Reader)new FileReader(fileOne.getPath()))){

            final BufferedReader bufferedReader = (BufferedReader)bufferedReader1;

            System.out.println(bufferedReader.readLine());
        }
        catch(FileNotFoundException e){
            System.out.println("oops");
        }
        catch(IOException e){
            System.out.println("oops again");
        }

        System.out.println("--------------");

        try(final ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileOne.getPath()));
            final ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileOne));){
            //objectOutputStream.writeObject(new ArrayList<Integer>(){{add(1); add(2); add(3);}});
            objectOutputStream.writeObject("hello again!");
            objectOutputStream.writeObject(10L);
            //objectOutputStream.writeObject(this.new SerializeMe());
            SerializeMe serializeMe = new OCP_IO_One.SerializeMe();
            serializeMe.setX(10);
            objectOutputStream.writeObject(serializeMe);
            objectOutputStream.writeObject(new ArrayList<Integer>(Arrays.<Integer>asList(1,2)));
            objectOutputStream.flush();
            // filter
            // !* = reject all
            // * = accept all
            // no match = accept (see their excellent doc on create filter for more)
//            ObjectInputFilter objectInputFilter = ObjectInputFilter.Config.createFilter(
//                     "com.example.demo.OCP_IO_One.SerializeMe;"
//                             + "!java.util.ArrayList"
//                   // + "!*"
//                    );
//            objectInputStream.setObjectInputFilter(objectInputFilter);

            ObjectInputFilter objectInputFilter = new ObjectInputFilter() {
                @Override
                public Status checkInput(FilterInfo filterInfo) {
                    try {
                        // can get stream bytes any time
                        if(filterInfo.serialClass() != null)
                            System.out.println("deserializing: " + filterInfo.serialClass().getName());
                        else {
                            System.out.println(
                                    "\treferences: " + filterInfo.references() + // members
                                            "\n\tdepth: " + filterInfo.depth() + // nested object (non-primitive)
                                            "\n\tarray length: " + filterInfo.arrayLength()
                            );
                            return Status.ALLOWED;
                        }
                    }
                    catch(Exception ex){
                        System.out.println("inner oops: " + ex);
                    }

                    if(filterInfo.serialClass() == ArrayList.class)
                        System.out.println("REJECTED: " + filterInfo.serialClass().getName());

                    return filterInfo.serialClass() != ArrayList.class ? Status.ALLOWED : Status.REJECTED;
                }
            };

            objectInputStream.setObjectInputFilter(objectInputFilter);

            System.out.println("reading");
            String object = (String)objectInputStream.readObject();
            Long object1 = (Long)objectInputStream.readObject();
            OCP_IO_One.SerializeMe object2 = (OCP_IO_One.SerializeMe)objectInputStream.readObject();

            // test filter
            Assertions.assertThrows(InvalidClassException.class, () -> objectInputStream.readObject());


            System.out.println("read is: " + object);
            System.out.println("read is: " + object1);
            System.out.println("read is: " + object2);

            System.out.println("tran vars null? " + (object2.getTranName() == null));
        }
        catch(FileNotFoundException | ClassNotFoundException | EOFException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops " + e.getLocalizedMessage());
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    public static class SerializeMe implements Serializable{


        // be public or have public getter and setter
        private int x;

        public MySubClassInSerialization mySubClassInSerialization = new MySubClassInSerialization();

        public int getX(){return this.x;}

        public void setX(int x){this.x = x;}

        private transient String tranName = "George";

        public String getTranName() {
            return tranName;
        }

        public String y = "hello";

        @JsonIgnore // only valid in a context where JacksonJson is default serialization
        public boolean z = true;

//        public RealOcpTest.A getTranVar() {
//            return tranVar;
//        } // if getter is set then defaulted to null value and won't be set when de-serialize (default persists)

        public static int ignoreMe = (int)3e1;

        private final int a = 100;

        public String name = "myName";

        static {}

        {
            name = "myNameButInitialized";
        }

        @Override
        public String toString(){
            try {
               // return OCP_IO_One.this.objectMapper.writeValueAsString(this);
                return new ObjectMapper().writeValueAsString(this);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "oops";
            }
        }

        public static class MySubClassInSerialization implements Serializable{
            public String subObjectString = "subObjectStringValue";
            public Double someDouble = 3.0;
            public HashSet<Integer> set = new HashSet<>(){{
                add(1);
            }};
        }

    }

    @Test
    public void testMarkInputStreams(){

        if(terminate)
            return;

        try(final InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(fileThree))){

            System.out.println(inputStreamReader.read());
            if(!inputStreamReader.markSupported()){
                int value;
                do{
                    value = inputStreamReader.read();
                    System.out.println((char)value);
                }while(value != -1);

                return;
            }

            inputStreamReader.mark(1);
            System.out.println((char)inputStreamReader.read());
            inputStreamReader.reset();
            System.out.println((char)inputStreamReader.read());
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }

    }

    @Test
    public void testMarkInputStreamsTwo(){

        if(terminate)
            return;

        int value = 0;

        try(final Reader inputStreamReader = new BufferedReader(new FileReader(fileThree))){

            System.out.println((char)inputStreamReader.read());
            if(!inputStreamReader.markSupported()){ ;
                do{
                    value = inputStreamReader.read();
                    System.out.println((char)value);
                }while(value != -1);

                return;
            }

            inputStreamReader.mark(1000); // inclusive and fault tolerant
            System.out.println((char)inputStreamReader.read()); // B
            System.out.println((char)inputStreamReader.read()); // C
            System.out.println((char)inputStreamReader.read()); // D
            System.out.println((char)inputStreamReader.read()); // E
            System.out.println((char)inputStreamReader.read()); // F
            inputStreamReader.reset();
            System.out.println("reset -- iteration");
            while((value = inputStreamReader.read()) != -1){
                //value = inputStreamReader.read();
                if(value == 'B') {
                    System.out.println((char)value);
                    Assertions.assertEquals(4, inputStreamReader.skip(200)); // CDEF skipped
                    value = -1;
                }
                System.out.println((char)value);
            }

        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }

    }

    @Test
    public void testBufferStreams(){

        if(terminate)
            return;

        try (
                InputStream in = new BufferedInputStream(new FileInputStream(bufferStreamTestFile.getPath()));
                OutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(bufferStreamTestFile.getParent(), "bufferTestFileCopy.txt")))) {
            byte[] buffer = new byte[2];
            int lengthRead;
            while ((lengthRead = in.read(buffer)) > 0) {
                System.out.println(lengthRead);
                out.write(buffer,0,lengthRead);
                out.flush();
            }
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }
    }

    @Test
    public void testBufferedReaderAndWriter(){

        if(terminate)
            return;

        try(
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(bufferStreamTestFile));
                final BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(bufferStreamTestFile.getParent(), "bufferTestFileCopy.txt")))
                ){

            String line;

            while( (line = bufferedReader.readLine()) != null ) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

            System.out.println("done:)");
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }
    }

    @Test
    public void testPrintWriterAndReader(){

        if(terminate)
            return;

        try(final PrintWriter printWriter = new PrintWriter(new FileWriter(printStreamFile));){

            printWriter.println("This is my amazing book!");
            printWriter.println("I hope you like it very much.");
            //printWriter.flush();
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }

        try(final PrintStream printStream = new PrintStream(new FileOutputStream(printStreamFile));){
            printStream.print("The author of this great book is (<insert name here>) and he is (");
            printStream.print(22);
            printStream.print(") years old!!");
            printStream.println();
            //printStream.flush();
            System.out.println("The end :)");

        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println("oops: " + e.getLocalizedMessage());
        }
    }

    @Test
    public void testConsole(){

        if(terminate)
            return;

        final Console console = System.console();

        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(System.in)
        );

        System.out.println("input here\u001a");
        System.out.flush();

        StringBuilder input = new StringBuilder();
        int iterationsLimit = 5;

//        InputStream is = new BufferedInputStream(new FileInputStream(fileOne));
//        InputStream a = new BufferedInputStream(is);
//        a = new ObjectInputStream(is);
//        String inputt;
//        while( (inputt = console.readLine()) != null )
//            console.writer().append(inputt);
//        console.flush();
//        for(final byte b: System.in.readAllBytes()){
//            input.append(String.valueOf((char)b));
//        }
//
//        System.out.println("out?");
    }

//    @Test
//    public void testInteractiveConsole() throws IOException {
//        Console console = System.console();
//        if (console == null) {
//            throw new RuntimeException("Console not available");
//        } else {
//            console.writer().print("How excited are you about your trip today? ");
//            console.flush();
//            String excitementAnswer = console.readLine();
//            String name = console.readLine("Please enter your name: ");
//            Integer age = null;
//            console.writer().print("What is your age? ");
//            console.flush();
//            BufferedReader reader = new BufferedReader(console.reader());
//            String value = reader.readLine();
//            age = Integer.valueOf(value);
//            console.writer().println();
//            console.format("Your name is " + name);
//            console.writer().println();
//            console.format("Your age is " + age);
//            console.printf("Your excitement level is: " + excitementAnswer);
//        }
//    }

    @Test
    public void testRandom() throws IOException{
        //Files.readAttributes(fileOne.toPath(), BasicFileAttributes.class).size();
        //new PrintWriter(fileOne);
        //new FileInputStream(fileThree).mark(1);
        System.out.println(File.pathSeparator);
        new BufferedReader(new FileReader(fileThree)).skip(1); // gives back how many chars were skipped
        Console console = System.console();
        //new FileInputStream(fileThree).read(new byte[3]);
        new FileWriter(testDirectory.getPath() + "/testCreate.txt").write("");
        System.out.println(new File("relative.txt").getPath());
        System.out.println(new File("").getAbsolutePath());
    }

    class A implements  AutoCloseable{
        public void close(){}
    }

    /*
    1) cannot create console's directly
     */
}
