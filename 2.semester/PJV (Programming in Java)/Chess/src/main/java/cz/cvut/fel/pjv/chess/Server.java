/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author semenvol, danilrom
 */
public class Server {

    private ServerSocket serverSocket;
    private boolean run = true;
    private int playersCounter = 0;
    private Map<Integer, Integer> pairs = new HashMap<Integer, Integer>();
    private Integer firstPlayerPort;
    private Integer secondPlayerPort;
    private Socket player1;
    private Socket player2;

    /**
     * Starts server
     *
     * @param ip server's IP address
     * @param port server's port number
     * @throws IOException
     */
    public void runServer(String ip, int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server is running");
        while (run) {
            Socket cs;
            try {
                cs = serverSocket.accept();
                playersCounter++;
                if (playersCounter % 2 == 1) {
                    firstPlayerPort = cs.getPort();
                    player1 = cs;
                } else {
                    secondPlayerPort = cs.getPort();
                    player2 = cs;
                    pairs.put(firstPlayerPort, secondPlayerPort);
                    ServerThread t1 = new ServerThread(player1, player2,
                            player1.getPort());
                    ServerThread t2 = new ServerThread(player2, player1,
                            player1.getPort());
                    t1.start();
                    t2.start();
                }
                if (cs.isClosed()) {
                    stopServer();
                }
            } catch (SocketException ex) {
                System.out.println("Client left");
            }
        }
        serverSocket.close();
        System.out.println("Server is closed");
    }

    /**
     * Stops server
     *
     */
    public void stopServer() {
        run = false;
    }

    public boolean getIsRunning() {
        return run;
    }

    public int getPlayersCount() {
        return playersCounter;
    }

    /**
     * Creates server
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.runServer("192.168.30.17", 10001);
        } catch (IOException ex) {
            System.out.println("Server is unreachable");
        }
    }
}
