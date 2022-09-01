package cz.cvut.fel.pjv.chess;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author semenvol, danilrom
 */

public class FileTransferClient {

    private final static int port = 10002;
    private final static String ipAddr = "192.168.30.14";
    private final static String filename = "TOP2.txt";
    /**
     * Recieves file with TOP 10 games
     * @throws IOException 
     */
    public static void createTOP() throws IOException {
        Socket sock = null;
        Scanner sc = null;
        FileWriter writer = null;
        try {
            sock = new Socket(ipAddr, port);
            InputStream is = sock.getInputStream();
            sc = new Scanner(is);
            String s = "";
            while (sc.hasNext()) {
                s += sc.nextLine() + "\n";
            }
            writer = new FileWriter(filename);
            writer.write(s);
        } finally {
            if (sc != null) {
                sc.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (sock != null) {
                sock.close();
            }
        }
    }
}
