/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Models for pieces are taken from https://opengameart.org/content/chess-pieces-and-board-squares
 */
package cz.cvut.fel.pjv.chess;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author semenvol, danilrom
 */
public class Game extends JFrame implements ActionListener {

    /**
     * Different states of game : user in menu, user in top, user chose local
     * game and user chose server game
     */
    public static States state = States.MENU;
    private JRadioButton localGame;
    private JRadioButton serverGame;
    private JRadioButton top;
    private JButton back;

    /**
     * Creates JFrame with menu
     */
    private Game() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new FlowLayout());
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        localGame = new JRadioButton("Local Game");
        localGame.addActionListener(this);
        serverGame = new JRadioButton("Game on server");
        serverGame.addActionListener(this);
        top = new JRadioButton("TOP 10");
        top.addActionListener(this);

        ButtonGroup group = new ButtonGroup();
        group.add(localGame);
        group.add(serverGame);
        group.add(top);

        this.add(localGame);
        this.add(serverGame);
        this.add(top);

        this.pack();
        this.setVisible(true);
    }

    /**
     * Creates JFrame with board
     *
     * @param board
     */
    private Game(Board board) {
        this.setTitle("Chess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.white);
        this.add(board);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Creates JFrame with TOP 10 games
     *
     * @param top
     */
    private Game(JPanel top) {
        this.setTitle("TOP");
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 600);
        this.getContentPane().setBackground(Color.white);
        top.setBounds(0, 0, 600, 550);
        this.add(top);
        back = new JButton("Back");
        back.addActionListener(this);
        back.setSize(50, 50);
        back.setBounds(225, 500, 150, 50);
        this.add(back);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Starts game with menu creating
     *
     * @param args
     */
    public static void main(String[] args) {
        new Game();
    }

    /**
     * To choose action in menu (local game, server game and TOP 10)
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == localGame) {
            this.setVisible(false);
            this.dispose();
            state = States.LOCAL;
            Board board = new Board(null);
            new Game(board);
        } else if (e.getSource() == serverGame) {
            this.setVisible(false);
            this.dispose();
            state = States.Server;
            String nick = JOptionPane.showInputDialog("Enter your nickname: ");
            if (nick == null) {
                System.exit(0);
            }
            while (nick.length() < 1 || nick.length() > 16) {
                JOptionPane.showConfirmDialog(null, "Try another nick!", "", JOptionPane.DEFAULT_OPTION);
                nick = JOptionPane.showInputDialog("Enter your nickname: ");
            }
            try {
                JOptionPane.showConfirmDialog(null, "Waiting for a opponent...", "", JOptionPane.DEFAULT_OPTION);
                Client client = new Client(nick);
                if (client.getCanStartGame()) {
                    System.out.println("Two players connecnted");
                    Board board = new Board(client);
                    new Game(board);
                }
            } catch (IOException ex) {
                System.out.println("Cannot link to server");
            }
        } else if (e.getSource() == top) {
            state = States.TOP10;
            this.setVisible(false);
            this.dispose();
            try {
                FileTransferClient.createTOP();
                JPanel panel = new JPanel(new GridLayout(13, 1));
                File file = new File("TOP2.txt");
                Scanner sc = new Scanner(file);
                String s = "";
                for (int i = 0; i < 10; ++i) {
                    JLabel label = new JLabel();
                    s = sc.nextLine() + "\n";
                    label.setText(s);
                    panel.add(label);

                }
                panel.setBackground(Color.white);
                sc.close();
                file.delete();
                new Game(panel);
            } catch (IOException ex) {
                System.out.println("Cannot link to server");
            }
        } else if (e.getSource() == back) {
            this.setVisible(false);
            this.dispose();
            new Game();
        }

    }
}
