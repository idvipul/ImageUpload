package com.company;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.activation.MimetypesFileTypeMap;

public class Main {
    private static final String UPLOAD_URL = "http://localhost:3000/upload";
    private static final int BUFFER_SIZE = 1000000; // 1MB

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Directory Name not Specified!");
        } else {
            // check if file path exists
            if (new File(args[0]).isDirectory()) {
                System.out.println("Directory Exists!");

                // accept directory name through command line args
                File directory = new File(args[0]);
                File[] contents = directory.listFiles();

                // initialize count
                int count = 0;

                checkImageCount(directory, count);
            } else {
                System.out.println("Directory does not Exist!");
            }
        }
    }

    private static void checkImageCount(File directory, int count) throws IOException {
        File[] contents = directory.listFiles();

        for (File fi : contents) {
            String mimetype = new MimetypesFileTypeMap().getContentType(fi);

            // return mimetpe of png image as image
            if (fi.getAbsolutePath().endsWith("png")) {
                // set png images's mimetype from application/octet-stream to image/png
                mimetype = "image/png";
            }

            // check image count
            if (mimetype.startsWith("image/")) {
                count++;
            }
        }
        // upload image
        uploadImage(directory, count);
    }

    private static void uploadImage(File directory, int count) throws IOException {
        File[] contents = directory.listFiles();

        if (count <= 10) {
            for (File fi : contents) {
                String mimetype = new MimetypesFileTypeMap().getContentType(fi);
                // check mimetype
                if (mimetype.startsWith("image/")) {
                    System.out.println("Image Uploaded! - " + fi.getAbsolutePath());
                    // upload image to the server
                    postImage(fi.getAbsolutePath());
                } else {
                    System.out.println("Error: Images Only! - " + fi.getAbsolutePath());
                }
            }
            System.out.println(count + " Images Uploaded!");
        } else {
            System.out.println("Error: 10 Images Only!");
        }
    }

    private static void postImage(String filePath) throws IOException {

        URL url = new URL(UPLOAD_URL);
        String charset = "UTF-8";
        String param = "value";
        File uploadFile = new File(filePath);
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (OutputStream output = connection.getOutputStream(); PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {
            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"param\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();

            // Send file.

            FileInputStream inputStream = new FileInputStream(uploadFile);
//            System.out.println("Input Stream:" + inputStream);
            int bytesRead;
            byte[] buffer = new byte[BUFFER_SIZE];

//            System.out.println("Start writing data...");

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + uploadFile.getName() + "\"").append(CRLF);
                writer.append("Content-Type: " + uploadFile.getName()).append(CRLF);
                writer.append(CRLF).flush();

                output.write(buffer, 0, bytesRead);

                output.flush(); // Important before continuing with writer!
                writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                // End of multipart/form-data.
                writer.append("--" + boundary + "--").append(CRLF).flush();
            }

        }

        // check HTTP response code from server
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // reads server's response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.readLine();
            System.out.println("Server's response code: " + responseCode);
        } else {
            System.out.println("Server returned non-OK code: " + responseCode);
        }
    }
}
