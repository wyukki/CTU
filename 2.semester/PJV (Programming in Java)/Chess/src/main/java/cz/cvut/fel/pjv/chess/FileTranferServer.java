package cz.cvut.fel.pjv.chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author semenvol, danilrom
 */

public class FileTranferServer {

    private final static int port = 10002;
    private final static String filename = "TOP.txt";
    private static boolean run = true;

    /**
     * Server for transporting TOP 10 games
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        OutputStream outputStream;
        PrintWriter writer;
        Scanner sc;
        ServerSocket serverSocket = null;
        Socket sock = null;
        serverSocket = new ServerSocket(port);
        while (run) {
            try {
                sock = serverSocket.accept();
                File myFile = new File(filename);
                sc = new Scanner(myFile);
                String s = "";
                while (sc.hasNext()) {
                    s += sc.nextLine() + "\n";
                    System.out.println(s);
                }
                outputStream = sock.getOutputStream();
                writer = new PrintWriter(outputStream, true);
                writer.println(s);
                writer.close();
                sc.close();
                outputStream.close();
            } catch (FileNotFoundException ex) {
                System.out.println("File \"TOP.txt\" is not found");
            }
        }
    }
}
