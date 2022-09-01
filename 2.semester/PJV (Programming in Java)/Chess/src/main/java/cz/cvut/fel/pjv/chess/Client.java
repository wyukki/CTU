/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author semenvol, danilrom
 */
public class Client {

    private final Socket socket = new Socket();
    private final String nickname;
    private final InetAddress address = InetAddress.getByName("192.168.30.11");
    private final int port = 10001;
    private final int timeout = 30000;
    private boolean canStartGame = false;
    private InputStream is;
    private Scanner sc;
    private PrintWriter writer;

    Client(String nick) throws IOException {
        this.nickname = nick;
        SocketAddress socketAddress = new InetSocketAddress(address, port);

        try {
            socket.connect(socketAddress, timeout);
            is = socket.getInputStream();
            sc = new Scanner(new InputStreamReader(is));
            writer = new PrintWriter(socket.getOutputStream(), true);
            writer.println("NICK-" + this.nickname + "\n");
            String str = sc.nextLine();
            if (str.startsWith("START")) {
                this.canStartGame = true;
            }
        } catch (SocketTimeoutException e) {
            JOptionPane.showMessageDialog(null, "Opponent is not found, try again later");
        } catch (ConnectException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Closes socket
     */
    public void close() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    /**
     *
     * @return true if two player are connected to server (pair created)
     */
    public boolean getCanStartGame() {
        return canStartGame;
    }

    /**
     *
     * @return player's color
     */
    public String getColor() {
        String color = null;
        writer.println("GETCOLOR");
        color = sc.nextLine();
        return color;
    }

    /**
     *
     * @return opponent nickname
     */
    public String getOppPlayerName() {
        String name = null;
        writer.println("GETNICK");
        name = sc.nextLine();
        return name;
    }

    public String getNick() {
        return this.nickname;
    }

    /**
     * Sends message to server
     *
     * @param s message to be sent
     */
    public void writeMSG(String s) {
        System.out.println("Sending: " + s);
        writer.println(s);
    }

    /**
     * Waits for reply
     *
     * @return recieved message
     */
    public String waitForMSG() {
        String s = null;
        boolean recieved = false;
        while (!recieved) {
            try {
                s = sc.nextLine();
                System.out.println("Recieved: " + s);
                recieved = true;
            } catch (NoSuchElementException ex) {
                System.out.println("Game finished");
                break;
            }
        }
        return s;
    }
}
