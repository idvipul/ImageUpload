package text;

import javax.activation.MimetypesFileTypeMap;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

//        File file = new File("/Users/vipulkaranjkar/Desktop/test.txt");

//        String text = "";
//
//        String[] words = readArray("/Users/vipulkaranjkar/Desktop/test.txt");

//        Scanner s = new Scanner(new FileReader("/Users/vipulkaranjkar/Desktop/test.txt"));

// key
//        for (int i = 0; i < words.length ; i = i+2) {
//            System.out.println("Key: " +words[i]);
//            System.out.println("a1: "+Arrays.toString(words));
//        }

// value
//        for (int i = 1; i < words.length ; i = i+2) {
//            System.out.println("Value: " +words[i]);
//            System.out.println("a2: "+Arrays.toString(words));
//        }

//        System.out.println(Arrays.toString(words));

//        try {
//            Scanner sc = new Scanner(file);
//
//            while (sc.hasNextLine()) {
//                text = text + sc.next() + "";
//            }
//            System.out.println(text );
//        } catch (FileNotFoundException e) {
//            System.out.println("File Not Found");
//        }
//

//---
        //// ORIGINAL PROGRAM

        if (args.length == 0) {
            System.out.println("Text File not specified");
        } else {
            if (new File(args[0]).isFile()) {
                String filePath = args[0];
                System.out.println("File Exists");

                // check text file
                String mimetype = new MimetypesFileTypeMap().getContentType(filePath);

                if (mimetype.startsWith("text/")) {
                    System.out.println("It's a text File");

                    try {
                        List<String> l1 = new ArrayList<String>();
                        List<String> l2 = new ArrayList<String>();

                        Scanner s = new Scanner(new File(filePath));

                        while (s.hasNext()) {
                            l1.add(s.next());
                            l2.add(s.next());
                        }

                        s.close();

                        System.out.println("Key: " + l1);
                        System.out.println("Value: " + l2);

//                        FileReader fileReader = new FileReader(filePath);
//                        BufferedReader bufferedReader = new BufferedReader(fileReader);
//                        StringBuilder stringBuilder = new StringBuilder();
//                        String line;
//                        while ((line = bufferedReader.readLine()) != null) {
//                            stringBuilder.append(line);
//                            stringBuilder.append("\n");
//                        }

//                        fileReader.close();
//                        System.out.println("Contents of file:");
//                        System.out.println(stringBuilder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Text File Only!");
                }
            } else {
                System.out.println("File does not Exist!");
            }
        }
    }

    public static String[] readArray(String file) throws FileNotFoundException {
        int count = 0;
        try {
            Scanner s1 = new Scanner(new File(file));
            while (s1.hasNextLine()) {
                count++;
                s1.next();
            }
            String[] words = new String[count];
            Scanner s2 = new Scanner(new File(file));
            for (int i = 0; i < count; i++) {
                words[i] = s2.next();
            }

            return words;

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found!");
        }
        return null;
    }
}