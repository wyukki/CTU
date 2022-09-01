/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author semenvol, danilrom
 */
public class ServerThread extends Thread {

    private final Socket player1;
    private final int player1Port;
    private final Socket player2;
    private InputStream inputStream;
    private OutputStream outputStream;
    private PrintWriter writer;
    private Scanner sc;
    private final String playerNick;
    /**
     * 
     * @param player1 socket of first player
     * @param player2 socket of second player
     * @param player1Port port number of first player
     * @throws IOException 
     */
    ServerThread(Socket player1, Socket player2,
            int player1Port) throws IOException {
        System.out.println(player1.getPort() + " is connected");
        System.out.println(player2.getPort() + " is connected");
        this.player1 = player1;
        this.player1Port = player1Port;
        this.player2 = player2;
        inputStream = player1.getInputStream();
        sc = new Scanner(new InputStreamReader(inputStream));
        String[] msg = sc.nextLine().split("-");
        playerNick = msg[1];
        System.out.println(playerNick);
        outputStream = player1.getOutputStream();
        writer = new PrintWriter(outputStream, true);
        writer.println("START");
    }
    /**
     * Thread for one game
     */
    @Override
    public void run() {
        try {
            inputStream = player1.getInputStream();
            sc = new Scanner(new InputStreamReader(inputStream));
            outputStream = player2.getOutputStream();
            writer = new PrintWriter(outputStream, true);
            while (sc.hasNextLine()) {
                String receivedMSG = sc.nextLine();
                if (receivedMSG.startsWith("MOVE") || receivedMSG.startsWith("PROM")
                        || receivedMSG.startsWith("AUTO") || receivedMSG.startsWith("SET")
                        || receivedMSG.startsWith("CAST") || receivedMSG.startsWith("FINISHED")) {
                    writer.println(receivedMSG);
                } else if (receivedMSG.startsWith("GETCOLOR")) {
                    if (player1.getPort() != this.player1Port) {
                        writer.println("WHITE");
                    } else {
                        writer.println("BLACK");
                    }
                } else if (receivedMSG.startsWith("GETNICK")) {
                    System.out.println(playerNick);
                    writer.println(playerNick);
                } else if (receivedMSG.startsWith("TIME")) {
                    String msg[] = receivedMSG.split("-", 2);
                    String newGame = msg[1];
                    String s[] = newGame.split(" ");
                    String[] minutesAndSeconds = s[3].split(":");
                    int minutes = Integer.parseInt(minutesAndSeconds[0]);
                    int seconds = Integer.parseInt(minutesAndSeconds[1]);
                    int newGameTime = minutes * 60 + seconds;
                    createTOP(newGame, newGameTime);
                } else if (receivedMSG.startsWith("QUIT")) {
                    writer.println(receivedMSG);
                    player1.close();
                } else {
                    System.err.println("Wrong command: " + receivedMSG);
                }
            }
        } catch (IOException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    /**
     * Creates TOP 10 file
     * @param newGame players nicknames, result of the game
     * @param newGameTime game's time
     */
    private void createTOP(String newGame, int newGameTime) {
        ArrayList<String> games = null;
        try {
            FileReader reader = new FileReader("TOP.txt");
            games = new ArrayList<String>();
            try {
                int c;
                int index = 0;
                int counter = 0;
                String game = new String();
                while ((c = reader.read()) != -1) {
                    counter++;
                    if (counter <= 3 || (counter == 4 && c == (int) ' ')) {
                        continue;
                    }
                    game += (char) c;
                    if ((char) c == '\n') {
                        games.add(game);
                        game = "";
                        index++;
                        counter = 0;
                    }
                }
                games.add(game);

            } catch (IOException ex) {
                System.err.println("Error!");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("File not found!");
        }
        games = isTopEmpty(games);
        String[] s;
        ArrayList<Integer> seconds = new ArrayList<Integer>(games.size());
        for (int i = 0; i < games.size(); ++i) {
            if (games.get(i) == null) {
                break;
            }
            s = games.get(i).split(" ");
            String[] minutesAndSeconds = s[3].split(":");
            int minutes = Integer.parseInt(minutesAndSeconds[0]);
            int second = Integer.parseInt(minutesAndSeconds[1]);

            seconds.add(minutes * 60 + second);
        }
        newGame += "\n";
        games.add(0, newGame);
        seconds.add(0, newGameTime);
        ArrayList<Integer> newIndicies = compareTime(seconds);
        FileWriter writer = null;
        try {
            writer = new FileWriter("TOP.txt");
            for (int i = 0; i < 10; ++i) {
                if (newIndicies.isEmpty()) {
                } else {
                    for (int j = 0; j < 10; ++j) {
                        if (newIndicies.get(j) == i) {
                            writer.write(i + 1 + ". " + games.get(j));
                            break;
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException ex) {
            System.err.println("File write error!");
        }
    }
    /**
     * Sorts time in TOP 10 file
     * @param seconds list of the games time
     * @return sorted list of the games time
     */
    private ArrayList<Integer> compareTime(ArrayList<Integer> seconds) {
        ArrayList<Integer> newIndecies = new ArrayList<Integer>(seconds.size());
        int[] times = new int[seconds.size()];
        for (int i = 0; i < seconds.size(); ++i) {
            times[i] = seconds.get(i);
        }
        Collections.sort(seconds);
        for (int i = 0; i < seconds.size(); ++i) {
            for (int j = 0; j < seconds.size(); ++j) {
                if (times[i] == seconds.get(j)) {
                    if (j == 10) {
                        break;
                    }
                    newIndecies.add(j);
                    break;
                }
            }
        }
        return newIndecies;
    }
    /**
     * Sets null if top is not full
     * @param games games in TOP file
     * @return list of played games
     */
    private ArrayList<String> isTopEmpty(ArrayList<String> games) {
        for (int i = 0; i < games.size(); ++i) {
            if (games.get(i) == null || games.get(i).isBlank()) {
                games.set(i, null);
            }
        }
        return games;
    }
}
