/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.cvut.fel.pjv.chess;

import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author semenvol, danilrom
 */
public class Board extends JPanel implements ActionListener {

    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final int windowWIDTH = screenSize.width;
    private final int windowHEIGHT = screenSize.height;
    private final int squareWIDTH = (windowHEIGHT / 8) - 10;
    private final int squareHEIGHT = squareWIDTH;
    private final JButton setPieceButton;
    private final JButton exitButton;
    private final JButton copyPGN = new JButton();
    private String txt = new String();
    private final TextArea out = new TextArea();
    private boolean boardIsSet = false;
    private boolean figuresAreSet = false;
    /**
     * All board's fields
     */
    static ArrayList<Field> fields = new ArrayList<Field>(64);
    private ArrayList<Field> poss_moves = new ArrayList<Field>();

    private String piece;
    private boolean pieceIsSelected = false;
    private int previousXSelection = 0;
    private int previousYSelection = 0;
    private int previousIndex = 0;
    private final int imageWidth = 65;
    private final int imageHeight = 65;
    private Piece p = null;
    private int pieceIndex = 0;
    private boolean gameRun = false;
    private boolean gameFinished = false;
    /**
     * First player
     */
    static Player white;
    /**
     * Second player
     */
    static Player black;
    private int minutesWhite = 14, secondsWhite = 59;
    private int minutesBlack = 14, secondsBlack = 59;
    private String result;
    private JLabel timer = new JLabel();
    private Thread playerCurrentTime = new Thread() {
        Player player;

        /**
         * Starts timer thread
         */
        @Override
        public void run() {
            String secondsDF, minutesDF;
            DecimalFormat df = new DecimalFormat("00");
            while (!gameFinished) {
                while (white.getIsMyMove()) {
                    try {
                        secondsDF = df.format(secondsWhite);
                        minutesDF = df.format(minutesWhite);
                        timer.setText(white.getPlayerName() + ":" + minutesDF + ":" + secondsDF);
                        Thread.sleep(1000);
                        secondsWhite--;
                        if (minutesWhite == 0 && secondsWhite == -1) {
                            gameFinished = true;
                            out.append("\nTimeout! Player " + black.getPlayerName() + " won");
                            return;
                        }
                        if (secondsWhite == -1) {
                            minutesWhite -= 1;
                            secondsWhite = 59;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                while (black.getIsMyMove()) {
                    try {
                        secondsDF = df.format(secondsBlack);
                        minutesDF = df.format(minutesBlack);
                        timer.setText(black.getPlayerName() + ":" + minutesDF + ":" + secondsDF);
                        Thread.sleep(1000);
                        secondsBlack--;
                        if (minutesBlack == 0 && secondsBlack == -1) {
                            gameFinished = true;
                            out.append("\nTimeout! Player " + white.getPlayerName() + " won");
                            return;
                        }
                        if (secondsBlack == -1) {
                            minutesBlack -= 1;
                            secondsBlack = 59;
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    };

    private final JButton promotionQueen;
    private final JButton promotionKnight;
    private final JButton promotionRook;
    private final JButton promotionBishop;
    private boolean promotion = false;
    private String promoting = null;
    /**
     * Current game state
     */
    static States state = Game.state;
    /**
     * Used for server game
     */
    static Player sessionPlayer;
    private String receivedMSG;
    private boolean waiting = false;
    private boolean timeSent = false;

    /**
     * Creates JPanel
     *
     * @param client connected player
     */
    Board(Client client) {
        if (state == States.Server) {
            if (client.getColor().startsWith("WHITE")) {
                white = new Player(1, client.getNick(), client);
                sessionPlayer = white;
                black = new Player(0, "black");
            } else {
                black = new Player(0, client.getNick(), client);
                sessionPlayer = black;
                white = new Player(1, "white");
            }
        } else if (state == States.LOCAL) {
            white = new Player(1, "white");
            black = new Player(0, "black");
        }
        System.out.println("Players created");
        this.setPreferredSize(new Dimension(windowWIDTH, windowHEIGHT));
        this.setMinimumSize(new Dimension(windowWIDTH, windowHEIGHT));
        this.setMaximumSize(new Dimension(windowWIDTH, windowHEIGHT));
        this.setLayout(null);
        this.setBackground(Color.white);

        timer.setBounds(windowWIDTH - 300, 20, 300, 200);
        timer.setHorizontalAlignment(JLabel.CENTER);
        timer.setFont(new Font("Arial", Font.PLAIN, 25));

        this.add(timer);

        setPieceButton = new JButton("Auto");
        setPieceButton.setBounds(windowWIDTH - 300, 150, 200, 50);
        setPieceButton.setBackground(new Color(0x22dd55));
        setPieceButton.addActionListener(this);
        this.add(setPieceButton);
        if (state == States.Server) {
            if (sessionPlayer.getColor() == 0) {
                if (!white.getPiecesAreSet()) {
                    setPieceButton.setEnabled(false);
                } else {
                    setPieceButton.setEnabled(true);
                }
            }
        }
        exitButton = new JButton();
        exitButton.setBounds(windowWIDTH - 100, 50, 50, 50);
        exitButton.setIcon(new ImageIcon("exit_button.png"));
        exitButton.setBorder(null);
        exitButton.setBackground(Color.white);
        exitButton.addActionListener(this);

        promotionQueen = new JButton();
        promotionKnight = new JButton();
        promotionRook = new JButton();
        promotionBishop = new JButton();

        promotionQueen.setBounds(imageWidth + imageWidth / 3, 15, imageWidth, imageHeight);
        promotionQueen.addActionListener(this);
        promotionQueen.setBorder(null);
        promotionQueen.setBackground(Color.white);
        promotionQueen.setVisible(false);

        promotionKnight.setBounds(imageWidth / 3, imageHeight + imageHeight / 2, imageWidth, imageHeight);
        promotionKnight.addActionListener(this);
        promotionKnight.setBorder(null);
        promotionKnight.setBackground(Color.white);
        promotionKnight.setVisible(false);

        promotionRook.setBounds(imageWidth + imageWidth / 3, imageHeight + imageHeight / 2, imageWidth, imageHeight);
        promotionRook.addActionListener(this);
        promotionRook.setBorder(null);
        promotionRook.setBackground(Color.white);
        promotionRook.setVisible(false);

        promotionBishop.setBounds(2 * imageWidth + imageWidth / 3, imageHeight + imageHeight / 2, imageWidth, imageHeight);
        promotionBishop.addActionListener(this);
        promotionBishop.setBorder(null);
        promotionBishop.setBackground(Color.white);
        promotionBishop.setVisible(false);

        this.add(promotionQueen);
        this.add(promotionKnight);
        this.add(promotionRook);
        this.add(promotionBishop);
        this.add(exitButton);
        copyPGN.setBounds(0, windowHEIGHT / 3 + windowHEIGHT / 2, windowWIDTH / 5, 100);
        copyPGN.addActionListener(this);
        copyPGN.setVisible(false);
        out.setBounds(windowWIDTH - 300, windowHEIGHT / 3, (windowWIDTH - (windowWIDTH - 300)) - 20, windowHEIGHT / 3);
        out.setEditable(false);
        out.setFont(new Font("Arial", Font.PLAIN, 12));
        this.add(out);
        this.add(copyPGN);
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (gameFinished && !timeSent) {
                    if (state == States.Server && sessionPlayer.getColor() == 1) {
                        String time = getGameDuration();
                        String s = "TIME-" + sessionPlayer.getPlayerName()
                                + " - " + client.getOppPlayerName() + " " + time + " " + result + "\n";
                        white.writeMSG(s);
                        timeSent = true;
                    }
                }
                if (!white.getPiecesAreSet() || !black.getPiecesAreSet()) {
                    if (piece == null) {
                        selectPieceForManualSetting(e.getX(), e.getY());
                    } else {
                        setPiece(e.getX(), e.getY());
                    }
                } else {
                    if (!gameRun) {
                        checkIsFree();
                    }
                    if (promotion) {
                        if (!white.getIsMyMove()) {
                            white.setIsMyMove(true);
                        } else {
                            black.setIsMyMove(true);
                        }
                    }
                    if (!gameFinished) {
                        tryMove(e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        });
        this.repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (!boardIsSet) {
            createDesk(g2d);
            if (state == States.Server) {
            }
            piece = null;
        }
    }

    /**
     * To choose action during game (setting piece, exit etc.)
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == setPieceButton) {
            if (figuresAreSet) {
                out.append("Figures are already set!");
                return;
            }
            if (state == States.LOCAL) {
                if (white.getPiecesAreSet()) {
                    setFigures(0);
                } else {
                    setFigures(1);
                }

                if (white.getPiecesAreSet() && black.getPiecesAreSet()) {
                    figuresAreSet = true;
                    setPieceButton.setBackground(new Color(0xff0000));
                    setPieceButton.setEnabled(false);
                    white.setIsMyMove(true);
                    black.setIsMyMove(false);
                }
                if (black.getPiecesAreSet()) {
                    removeBlackFiguresForManualSetting();
                } else if (white.getPiecesAreSet()) {
                    removeWhiteFiguresForManualSetting();
                }
            } else if (state == States.Server) {
                if (sessionPlayer.getColor() == 0) {
                    if (!black.getPiecesAreSet()) {
                        setFigures(0);
                        black.setPiecesAreSet(true);
                        black.writeMSG("AUTO-BLACK");
                        black.setIsMyMove(false);
                        removeBlackFiguresForManualSetting();
                    }
                    if (white.getPiecesAreSet() && black.getPiecesAreSet()) {
                        figuresAreSet = true;
                        setPieceButton.setBackground(new Color(0xff0000));
                        setPieceButton.setEnabled(false);
                        white.setIsMyMove(true);
                        sessionPlayer.setIsMyMove(false);
                        removeWhiteFiguresForManualSetting();
                        waiting = true;
                        waitForMSG();
                    }
                } else if (sessionPlayer.getColor() == 1) {
                    if (!white.getPiecesAreSet()) {
                        setFigures(1);
                        white.setPiecesAreSet(true);
                        white.writeMSG("AUTO-WHITE");
                        removeWhiteFiguresForManualSetting();
                        white.setIsMyMove(false);
                        waitForMSG();
                    }
                }
            }
        } else if (e.getSource() == exitButton) {
            if (state == States.Server) {
                sessionPlayer.writeMSG("QUIT");
            }
            System.exit(0);
        } else if (e.getSource() == promotionQueen) {
            if (white.getIsMyMove()) {
                promoting = "qw";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            } else {
                promoting = "qb";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            }
        } else if (e.getSource() == promotionKnight) {
            if (white.getIsMyMove()) {
                promoting = "nw";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            } else {
                promoting = "nb";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            }
        } else if (e.getSource() == promotionRook) {
            if (white.getIsMyMove()) {
                promoting = "rw";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            } else {
                promoting = "rb";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            }
        } else if (e.getSource() == promotionBishop) {
            if (white.getIsMyMove()) {
                promoting = "bw";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            } else {
                promoting = "bb";
                promote(fields.get(previousIndex).getX_from(), fields.get(previousIndex).getY_from());
            }
        } else if (gameFinished && e.getSource() == copyPGN) {
            FileWriter w = null;
            try {
                w = new FileWriter("game.txt");
                w.write(txt);
            } catch (IOException ex) {
                Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (w != null) {
                        w.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * Creates board
     *
     * @param g2d graphics
     */
    private void createDesk(Graphics2D g2d) {
        int numberOfFields = 0;
        int x = (windowWIDTH - (squareWIDTH * 8)) / 2;
        int y = (windowHEIGHT - (squareHEIGHT * 8)) / 2;
        int number = 8;
        int letter = 65;
        int fieldColor;
        int pieceColor;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (i == 0) {
                    g2d.setPaint(Color.black);
                    g2d.setFont(new Font("arial", Font.PLAIN, 20));
                    g2d.drawString("" + (char) letter++, x + (squareWIDTH / 2) - 10, y - 10);
                }
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        fieldColor = 1;
                        g2d.setPaint(new Color(0xaa5533));
                    } else {
                        fieldColor = 0;
                        g2d.setPaint(Color.black);
                    }
                } else {
                    if (j % 2 != 0) {
                        fieldColor = 1;
                        g2d.setPaint(new Color(0xaa5533));
                    } else {
                        fieldColor = 0;
                        g2d.setPaint(Color.black);
                    }
                }
                if (numberOfFields == 0 || numberOfFields == 7) {
                    piece = "rb";
                    pieceColor = 0;
                } else if (numberOfFields == 56 || numberOfFields == 63) {
                    piece = "rw";
                    pieceColor = 1;
                } else if (numberOfFields == 1 || numberOfFields == 6) {
                    piece = "nb";
                    pieceColor = 0;
                } else if (numberOfFields == 57 || numberOfFields == 62) {
                    piece = "nw";
                    pieceColor = 1;
                } else if (numberOfFields == 2 || numberOfFields == 5) {
                    piece = "bb";
                    pieceColor = 0;
                } else if (numberOfFields == 58 || numberOfFields == 61) {
                    piece = "bw";
                    pieceColor = 1;
                } else if (numberOfFields == 3) {
                    piece = "qb";
                    pieceColor = 0;
                } else if (numberOfFields == 59) {
                    piece = "qw";
                    pieceColor = 1;
                } else if (numberOfFields == 4) {
                    piece = "kb";
                    pieceColor = 0;
                } else if (numberOfFields == 60) {
                    piece = "kw";
                    pieceColor = 1;
                } else if (numberOfFields > 7 && numberOfFields < 16) {
                    piece = "pb";
                    pieceColor = 0;
                } else if (numberOfFields > 47 && numberOfFields < 56) {
                    piece = "pw";
                    pieceColor = 1;
                } else {
                    pieceColor = 2;
                    piece = null;
                }
                Field square = new Field(x, y, x + squareWIDTH, y + squareHEIGHT, pieceColor, fieldColor, numberOfFields);
                fields.add(numberOfFields, square);
                fields.get(numberOfFields).setPiece(piece);
                g2d.fillRect(x, y, squareWIDTH, squareWIDTH);
                x += squareWIDTH;
                numberOfFields++;
            }
            x = (windowWIDTH - (squareWIDTH * 8)) / 2;
            g2d.setPaint(Color.black);
            g2d.setFont(new Font("arial", Font.PLAIN, 20));
            g2d.drawString("" + number--, x - 15, y + (squareHEIGHT / 2));
            y += squareHEIGHT;
        }
        Image image = null;
        int x1 = 0;
        int y1 = 0;
        Player player;
        for (int i = 0; i < 12; ++i) {
            if (i < 6) {
                player = white;
                if (i == 0) {
                    y1 = windowHEIGHT - squareHEIGHT * 2 - squareHEIGHT / 2 + 15;
                    x1 = ((windowWIDTH - squareWIDTH * 8) / 2) + (squareWIDTH * 8) + squareWIDTH / 2;
                    image = new ImageIcon("w_pawn.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new Pawn(1, x1, y1, "pw", i));
                    }
                    piece = "pw";
                } else if (i == 1) {
                    image = new ImageIcon("w_rook.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new Rook(1, x1, y1, "rw", i));
                    }
                    piece = "rw";
                } else if (i == 2) {
                    image = new ImageIcon("w_knight.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new Knight(1, x1, y1, "nw", i));
                    }
                    piece = "nw";
                } else if (i == 3) {
                    x1 -= squareWIDTH * 3;
                    y1 += squareHEIGHT;
                    image = new ImageIcon("w_bishop.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new Bishop(1, x1, y1, "bw", i));
                    }
                    piece = "bw";
                } else if (i == 4) {
                    image = new ImageIcon("w_queen.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new Queen(1, x1, y1, "qw", i));
                    }
                    piece = "qw";
                } else if (i == 5) {
                    image = new ImageIcon("w_king.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (white.getColor() == 1) {
                        player.getPiecesForManualSetting().add(new King(1, x1, y1, "kw", i));
                    }
                    piece = "kw";
                }
                if (white.getColor() == 1) {
                    player.getFieldsForManualSetting().add(new Field(x1 - 15, y1 - 15, x1 - 15 + squareWIDTH, y1 - 15 + squareHEIGHT, 1, 1, i));
                    player.getFieldsForManualSetting().get(i).setPiece(piece);
                }
            } else {
                player = black;
                if (i == 6) {
                    x1 = (((windowWIDTH - squareWIDTH * 8) / 2) / 65) + 10;
                    y1 = squareHEIGHT / 2 + 15;
                    image = new ImageIcon("b_pawn.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new Pawn(0, x1, y1, "pb", i % 6));
                    }
                    piece = "pb";
                } else if (i == 7) {
                    image = new ImageIcon("b_rook.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new Rook(0, x1, y1, "rb", i % 6));
                    }
                    piece = "rb";
                } else if (i == 8) {
                    image = new ImageIcon("b_knight.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new Knight(0, x1, y1, "nb", i % 6));
                    }
                    piece = "nb";
                } else if (i == 9) {
                    x1 -= (squareWIDTH * 3);
                    y1 += squareHEIGHT;
                    image = new ImageIcon("b_bishop.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new Bishop(0, x1, y1, "bb", i % 6));
                    }
                    piece = "bb";
                } else if (i == 10) {
                    image = new ImageIcon("b_queen.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new Queen(0, x1, y1, "qb", i % 6));
                    }
                    piece = "qb";
                } else if (i == 11) {
                    image = new ImageIcon("b_king.png").getImage();
                    g2d.drawRect(x1 - 15, y1 - 15, squareWIDTH - 5, squareHEIGHT - 5);
                    if (black.getColor() == 0) {
                        player.getPiecesForManualSetting().add(new King(0, x1, y1, "kb", i % 6));
                    }
                    piece = "kb";
                }
                if (black.getColor() == 0) {
                    player.getFieldsForManualSetting().add(new Field(x1 - 15, y1 - 15, x1 - 15 + squareWIDTH, y1 - 15 + squareHEIGHT, 0, 0, i));
                    player.getFieldsForManualSetting().get(i % 6).setPiece(piece);
                }
            }
            g2d.drawImage(image, x1, y1, this);
            x1 += squareWIDTH;
        }
        boardIsSet = true;
        if (state == States.Server) {
            if (sessionPlayer.getColor() == 0) {
                waitForMSG();
            }
        }
    }

    /**
     * Sets pieces on the board if auto setting chosen
     *
     * @param color player's color
     */
    private void setFigures(int color) {
        int yFirst = ((windowHEIGHT - (squareHEIGHT * 8)) / 2) + 7;
        int xFirst = ((windowWIDTH - (squareWIDTH * 8)) / 2) + 17;
        Image image = null;
        if (state == States.Server) {

        } else {
            sessionPlayer = white.getIsMyMove() ? white : black;
            if (sessionPlayer.getColor() == 0) {
                color = 0;
            } else {
                color = 1;
            }
        }
        String piece = null;
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int numberOfPieces = 0;
        for (int y = 0; y < 8; ++y) {
            int yPosition = yFirst + y * squareHEIGHT;
            for (int x = 0; x < 8; ++x) {
                int xPosition = xFirst + x * squareWIDTH;
                if (y < 2) {
                    if (y == 0) {
                        switch (x) {
                            case 0:
                            case 7:
                                image = new ImageIcon("b_rook.png").getImage();
                                piece = "rb";
                                break;
                            case 1:
                            case 6:
                                image = new ImageIcon("b_knight.png").getImage();
                                piece = "nb";
                                break;
                            case 2:
                            case 5:
                                image = new ImageIcon("b_bishop.png").getImage();
                                piece = "bb";
                                break;
                            case 3:
                                image = new ImageIcon("b_queen.png").getImage();
                                piece = "qb";
                                break;
                            case 4:
                                image = new ImageIcon("b_king.png").getImage();
                                piece = "kb";
                                break;
                        }
                    } else if (y == 1) {
                        image = new ImageIcon("b_pawn.png").getImage();
                        piece = "pb";
                    }
                    if (color == 0) {
                        g2d.drawImage(image, xPosition, yPosition, this);
                        switch (piece) {
                            case "pb":
                                black.getMyPieces().add(numberOfPieces, new Pawn(0,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "rb":
                                black.getMyPieces().add(numberOfPieces, new Rook(0,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "nb":
                                black.getMyPieces().add(numberOfPieces, new Knight(0, xPosition,
                                        yPosition, piece, numberOfPieces));
                                break;
                            case "bb":
                                black.getMyPieces().add(numberOfPieces, new Bishop(0,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "qb":
                                black.getMyPieces().add(numberOfPieces, new Queen(0,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "kb":
                                black.getMyPieces().add(numberOfPieces, new King(0,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                        }
                        fields.get(numberOfPieces).setIsFree(false);
                    }
                    fields.get(numberOfPieces).setPiece(piece);
                } else if (y > 5) {
                    if (y == 6) {
                        image = new ImageIcon("w_pawn.png").getImage();
                        piece = "pw";
                    } else if (y == 7) {
                        switch (x) {
                            case 0:
                            case 7:
                                image = new ImageIcon("w_rook.png").getImage();
                                piece = "rw";
                                break;
                            case 1:
                            case 6:
                                image = new ImageIcon("w_knight.png").getImage();
                                piece = "nw";
                                break;
                            case 2:
                            case 5:
                                image = new ImageIcon("w_bishop.png").getImage();
                                piece = "bw";
                                break;
                            case 3:
                                image = new ImageIcon("w_queen.png").getImage();
                                piece = "qw";
                                break;
                            case 4:
                                image = new ImageIcon("w_king.png").getImage();
                                piece = "kw";
                                break;
                        }
                    }
                    if (color == 1) {
                        g2d.drawImage(image, xPosition, yPosition, this);
                        switch (piece) {
                            case "pw":
                                white.getMyPieces().add(numberOfPieces % 16, new Pawn(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "rw":
                                white.getMyPieces().add(numberOfPieces % 16, new Rook(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "nw":
                                white.getMyPieces().add(numberOfPieces % 16, new Knight(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "bw":
                                white.getMyPieces().add(numberOfPieces % 16, new Bishop(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "qw":
                                white.getMyPieces().add(numberOfPieces % 16, new Queen(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                            case "kw":
                                white.getMyPieces().add(numberOfPieces % 16, new King(1,
                                        xPosition, yPosition, piece, numberOfPieces));
                                break;
                        }
                        fields.get(numberOfPieces).setIsFree(false);
                    }
                    fields.get(numberOfPieces).setPiece(piece);
                }
                numberOfPieces++;
            }
        }
        if (state == States.LOCAL) {
            if (color == 0) {
                black.setPiecesAreSet(true);
            } else {
                white.setPiecesAreSet(true);
                white.setIsMyMove(false);
            }
        }
    }

    /**
     * Removes picture if black pieces are set
     */
    private void removeBlackFiguresForManualSetting() {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int width = (squareWIDTH * 3) + imageWidth;
        int height = (squareHEIGHT * 2) + imageHeight;
        g2d.setPaint(Color.white);
        g2d.fillRect(0, 0, width, height);
    }

    /**
     * Removes picture if white pieces are set
     */
    private void removeWhiteFiguresForManualSetting() {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int width = (squareWIDTH * 3) + imageWidth;
        int height = (squareHEIGHT * 2) + imageHeight;
        g2d.setPaint(Color.white);
        g2d.fillRect(windowWIDTH - width, windowHEIGHT - height, width, height);

    }

    /**
     * Draws rectangle around selected field
     *
     * @param index of selected field
     */
    private void changeColor(int index) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int fieldColor;
        fieldColor = fields.get(index).getFieldColor();
        if (fields.get(index).getIsSelected()) {
            g2d.setPaint(new Color(0x22dd55));
            g2d.drawRect(fields.get(index).getX_from() + 5, fields.get(index).getY_from() + 5, squareWIDTH - 10, squareHEIGHT - 10);
        } else {
            if (fieldColor == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint(new Color(0xaa5533));
            }
            g2d.fillRect(fields.get(index).getX_from(), fields.get(index).getY_from(), squareWIDTH, squareHEIGHT);
            String s = fields.get(index).getPiece();
            if (s == null) {
                return;
            }
            g2d.drawImage(getImage(s), fields.get(index).getX_from() + 17, fields.get(index).getY_from() + 7, this);
        }
    }

    /**
     * Selectes piece for manual setting
     *
     * @param x coordinate of pressed picture
     * @param y coordinate of pressed picture
     */
    protected void selectPieceForManualSetting(int x, int y) {
        Player curr_player = null;
        if (state == States.Server) {
            curr_player = sessionPlayer;
        } else {
            curr_player = white.getIsMyMove() ? white : black;
        }
        ArrayList<Field> fieldsManual = curr_player.getFieldsForManualSetting();
        for (Field f : fieldsManual) {
            if (f != null && f.getX_from() <= x && f.getX_to() >= x && f.getY_from() <= y && f.getY_to() >= y && f.getPiece() != null) {
                if (f.getPieceColor() != curr_player.getColor()) {
                    out.append("Not your piece!\n");
                    return;
                }
                piece = f.getPiece();
                out.append("Piece " + piece + " is selected!\n");
            }
        }
    }

    /**
     * Sets piece on board Used for manual setting
     *
     * @param x coordinate of pressed picture
     * @param y coordinate of pressed picture
     */
    protected void setPiece(int x, int y) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        for (Field f : fields) {
            if (f.getX_from() <= x && f.getX_to() >= x && f.getY_from() <= y && f.getY_to() >= y && f.getPiece() != null) {
                if (piece.equals(f.getPiece()) && f.getIsFree()) {
                    g2d.drawImage(getImage(piece), f.getX_from() + 17, f.getY_from() + 7, this);
                    out.append("Piece " + piece + " is set!\n");
                    f.setIsFree(false);
                } else if (!piece.equals(f.getPiece()) || !f.getIsFree()) {
                    out.append("Wrong place!\n");
                }
            }
        }
        checkNumberOfPiece();
        piece = null;
    }

    /**
     * Checks number of piece that are already set
     */
    private void checkNumberOfPiece() {
        int x = 0;
        int y = 0;
        Player curr_player = white.getIsMyMove() ? white : black;
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        if (curr_player.getCounter("pawn") == 8 && curr_player.getFieldsForManualSetting().get(0) != null) {
            x = curr_player.getFieldsForManualSetting().get(0).getX_from();
            y = curr_player.getFieldsForManualSetting().get(0).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(0, null);
        }
        if (curr_player.getCounter("rook") >= 2 && curr_player.getFieldsForManualSetting().get(1) != null) {
            x = curr_player.getFieldsForManualSetting().get(1).getX_from();
            y = curr_player.getFieldsForManualSetting().get(1).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(1, null);
        }
        if (curr_player.getCounter("knight") >= 2 && curr_player.getFieldsForManualSetting().get(2) != null) {
            x = curr_player.getFieldsForManualSetting().get(2).getX_from();
            y = curr_player.getFieldsForManualSetting().get(2).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(2, null);
        }
        if (curr_player.getCounter("bishop") >= 2 && curr_player.getFieldsForManualSetting().get(3) != null) {
            x = curr_player.getFieldsForManualSetting().get(3).getX_from();
            y = curr_player.getFieldsForManualSetting().get(3).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(3, null);
        }
        if (curr_player.getCounter("queen") >= 1 && curr_player.getFieldsForManualSetting().get(4) != null) {
            x = curr_player.getFieldsForManualSetting().get(4).getX_from();
            y = curr_player.getFieldsForManualSetting().get(4).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(4, null);
        }
        if (curr_player.getCounter("king") >= 1 && curr_player.getFieldsForManualSetting().get(5) != null) {
            x = curr_player.getFieldsForManualSetting().get(5).getX_from();
            y = curr_player.getFieldsForManualSetting().get(5).getY_from();
            g2d.clearRect(x, y, squareWIDTH, squareHEIGHT);
            curr_player.getFieldsForManualSetting().set(5, null);
        }
        if (curr_player.getCounter("pawn") == 8 && curr_player.getCounter("rook") == 2
                && curr_player.getCounter("knight") == 2 && curr_player.getCounter("bishop") == 2
                && curr_player.getCounter("king") == 1 && curr_player.getCounter("queen") == 1) {
            curr_player.setPiecesAreSet(true);
            if (curr_player.getPlayerName().equals("white")) {
                setFigures(1);

            } else if (state == States.Server && curr_player.getColor() == 1) {
                if (state == States.Server) {
                    white.writeMSG("AUTO-WHITE");
                    waitForMSG();
                }
            }
        }
        if (white.getPiecesAreSet() && black.getPiecesAreSet()) {
            setPieceButton.setBackground(new Color(0xff0000));
            setPieceButton.setEnabled(false);
            setFigures(0);
            black.setIsMyMove(false);
            white.setIsMyMove(true);
            if (state == States.Server) {
                black.writeMSG("AUTO-BLACK");
                figuresAreSet = true;
                if (sessionPlayer.getColor() == 0) {
                    waiting = true;
                    waitForMSG();
                }
            }
        }

    }

    /**
     *
     * @param s selected piece
     * @return image of the piece
     */
    private Image getImage(String s) {
        Image image = null;
        Player curr_player = white.getIsMyMove() ? white : black;
        if (curr_player.getColor() == 0) {
            switch (s) {
                case "rb":
                    image = new ImageIcon("b_rook.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "nb":
                    image = new ImageIcon("b_knight.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "bb":
                    image = new ImageIcon("b_bishop.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "qb":
                    image = new ImageIcon("b_queen.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "kb":
                    image = new ImageIcon("b_king.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "pb":
                    image = new ImageIcon("b_pawn.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
            }
        } else {
            switch (s) {
                case "rw":
                    image = new ImageIcon("w_rook.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "nw":
                    image = new ImageIcon("w_knight.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "bw":
                    image = new ImageIcon("w_bishop.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "qw":
                    image = new ImageIcon("w_queen.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "kw":
                    image = new ImageIcon("w_king.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
                case "pw":
                    image = new ImageIcon("w_pawn.png").getImage();
                    curr_player.incrementPieceCounter(s);
                    break;
            }
        }
        return image;
    }

    /**
     * 1. Moves piece 2. Calls castling fucntion 3. Calls promotion function 4.
     * Checks end of the game
     *
     * @param x clicked coordinate on the screen
     * @param y clicked coordinate on the screen
     */
    private void tryMove(int x, int y) {
        if (gameFinished) {
            return;
        }
        int index = 0;
        boolean killed = false;
        boolean moveIsValid = false;
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        Player player;
        if (state == States.Server) {
            player = sessionPlayer;
            if (waiting) {
                return;
            }
        } else {
            player = white.getIsMyMove() ? white : black;
        }
        Player oppPlayer = player.getColor() == 0 ? white : black;

        for (Field f : fields) {
            if (f.getX_from() <= x && f.getX_to() >= x && f.getY_from() <= y && f.getY_to() >= y) {
                if (!f.getIsSelected() && !pieceIsSelected && f.getPiece() != null
                        && f.getPieceColor() == player.getColor()) {
                    Piece king = player.getMyPieces().get(player.getKing());
                    ((King) king).isCheck(fields);
                    piece = f.getPiece();
                    f.setIsSelected(true);
                    pieceIsSelected = true;
                    changeColor(index);
                    previousXSelection = f.getX_from();
                    previousYSelection = f.getY_from();
                    previousIndex = index;
                    piece = f.getPiece();
                    p = findPiece(f.getX_from(), f.getY_from(), f.getX_to(), f.getY_to(), piece, player);
                    p.createPossibleMoves(fields, index);
                    p.createAttackMoves(fields, index);
                    poss_moves = p.checkPossibleMoves(fields, index);
                    p.checkAttackMoves(fields, index);
                    if (poss_moves != null) {
                        drawPossibleMoves();
                    } else if (p.getAttackMoves() != null) {
                        poss_moves = p.getAttackMoves();
                        drawPossibleMoves();
                    }
                } else if (f.getIsSelected() && f.getX_from() == previousXSelection) {
                    f.setIsSelected(false);
                    pieceIsSelected = false;
                    previousXSelection = 0;
                    previousYSelection = 0;
                    previousIndex = 0;
                    changeColor(index);
                    clearPossibleMoves();
                } else if (!f.getIsSelected() && previousXSelection != 0 && f.getPiece() != null
                        && f.getPieceColor() == player.getColor()) {
                    if ((p.getName().equals("kb") && fields.get(index).getPiece().equals("rb"))
                            || (p.getName().equals("kw") && fields.get(index).getPiece().equals("rw"))) {
                        Piece rook = player.getMyPieces().get(player.getIndexOfPiece(index));
                        if (((King) p).castling((Rook) rook)) {
                            clearPossibleMoves();
                            castling(((King) p).getCastlingSide());
                            ((King) p).setIsFirstMove(false);
                            ((Rook) rook).setIsFirstMove(false);
                            if (player.getColor() == 0) {
                                white.setIsMyMove(true);
                                black.setIsMyMove(false);
                                if (state == States.Server) {
                                    sessionPlayer.writeMSG("CAST-" + ((King) p).getCastlingSide());
                                    waitForMSG();
                                    waiting = true;
                                }
                            } else {
                                black.setIsMyMove(true);
                                white.setIsMyMove(false);
                                if (state == States.Server) {
                                    sessionPlayer.writeMSG("CAST-" + ((King) p).getCastlingSide());
                                    waitForMSG();
                                    waiting = true;
                                }
                            }
                        } else {
                            fields.get(previousIndex).setIsSelected(false);
                            changeColor(previousIndex);
                            clearPossibleMoves();
                            out.append("\nCastling is not possible!\n");
                        }
                        pieceIsSelected = false;
                        previousXSelection = 0;
                        previousYSelection = 0;
                        previousIndex = 0;
                        return;
                    }
                    fields.get(previousIndex).setIsSelected(false);
                    changeColor(previousIndex);
                    pieceIsSelected = true;
                    previousXSelection = f.getX_from();
                    previousYSelection = f.getY_from();
                    previousIndex = index;
                    f.setIsSelected(true);
                    changeColor(index);
                    piece = f.getPiece();
                    clearPossibleMoves();
                    Piece king = player.getMyPieces().get(player.getKing());
                    ((King) king).isCheck(fields);
                    p = findPiece(f.getX_from(), f.getY_from(), f.getX_to(), f.getY_to(), piece, player);
                    p.createPossibleMoves(fields, index);
                    p.createAttackMoves(fields, index);
                    poss_moves = p.checkPossibleMoves(fields, index);
                    p.checkAttackMoves(fields, index);
                    if (poss_moves != null) {
                        drawPossibleMoves();
                    } else if (p.getAttackMoves() != null) {
                        poss_moves = p.getAttackMoves();
                        drawPossibleMoves();
                    }
                } else if (pieceIsSelected) {
                    if (p == null) {
                        return;
                    }
                    if (p.move(fields, index)) {
                        if (fields.get(index).getFieldColor() == 0) {
                            g2d.setPaint(Color.black);
                        } else {
                            g2d.setPaint(new Color(0xaa5533));
                        }
                        clearPossibleMoves();
                        g2d.fillRect(f.getX_from(), f.getY_from(), squareWIDTH, squareHEIGHT);
                        g2d.drawImage(getImage(p.toString()), f.getX_from() + 17, f.getY_from() + 7, this);
                        if (fields.get(previousIndex).getFieldColor() == 0) {
                            g2d.setPaint(Color.black);
                        } else {
                            g2d.setPaint(new Color(0xaa5533));
                        }
                        g2d.fillRect(previousXSelection, previousYSelection, squareWIDTH, squareHEIGHT);
                        moveIsValid = true;
                        if ((player.getColor() == 0 && f.getPieceColor() == 1)
                                || (player.getColor() == 1 && f.getPieceColor() == 0)) {
                            oppPlayer.setToMyPieces(null, oppPlayer.getIndexOfPiece(index));
                        }
                        if ((p.getName().equals("pw") || p.getName().equals("pb"))
                                && ((Pawn) p).getIsEnPassant()) {
                            if (p.getColor() == 0) {
                                if (fields.get(index - 8).getFieldColor() == 0) {
                                    g2d.setPaint(Color.black);
                                } else {
                                    g2d.setPaint(new Color(0xaa5533));
                                }
                                g2d.fillRect(fields.get(index - 8).getX_from(),
                                        fields.get(index - 8).getY_from(), squareWIDTH, squareHEIGHT);
                                oppPlayer.setToMyPieces(null, oppPlayer.getIndexOfPiece(index - 8));
                                fields.get(index - 8).setPiece(null);
                                fields.get(index - 8).setPieceColor(2);
                                fields.get(index - 8).setIsFree(true);
                                ((Pawn) p).setIsEnPassant();
                            } else {
                                if (fields.get(index + 8).getFieldColor() == 0) {
                                    g2d.setPaint(Color.black);
                                } else {
                                    g2d.setPaint(new Color(0xaa5533));
                                }
                                g2d.fillRect(fields.get(index + 8).getX_from(),
                                        fields.get(index + 8).getY_from(), squareWIDTH, squareHEIGHT);
                                oppPlayer.setToMyPieces(null, oppPlayer.getIndexOfPiece(index + 8));
                                fields.get(index + 8).setPiece(null);
                                fields.get(index + 8).setPieceColor(2);
                                fields.get(index + 8).setIsFree(true);
                                ((Pawn) p).setIsEnPassant();
                            }
                        }
                    } else {
                        p = null;
                        fields.get(previousIndex).setIsSelected(false);
                        changeColor(previousIndex);
                        if (poss_moves != null) {
                            clearPossibleMoves();
                        }
                        out.setForeground(Color.red);
                        out.append("\nNot legal move: " + piece + " from " + previousXSelection + ", "
                                + previousYSelection + " to " + f.getX_from() + ", " + f.getY_from());
                    }
                    if (moveIsValid) {
                        fields.get(index).setPiece(p.getName());
                        fields.get(index).setPieceColor(player.getColor());
                        fields.get(index).setIsFree(false);
                        fields.get(previousIndex).setIsFree(true);
                        fields.get(previousIndex).setPiece(null);
                        fields.get(previousIndex).setPieceColor(2);
                        p.setIndexInFields(index);
                        if ((p.getName().equals("pw") || p.getName().equals("pb")) && ((Pawn) p).promotion()) {
                            if (player.getColor() == 0) {
                                promotionQueen.setIcon(new ImageIcon("b_queen.png"));
                                promotionKnight.setIcon(new ImageIcon("b_knight.png"));
                                promotionRook.setIcon(new ImageIcon("b_rook.png"));
                                promotionBishop.setIcon(new ImageIcon("b_bishop.png"));
                            } else {
                                promotionQueen.setIcon(new ImageIcon("w_queen.png"));
                                promotionKnight.setIcon(new ImageIcon("w_knight.png"));
                                promotionRook.setIcon(new ImageIcon("w_rook.png"));
                                promotionBishop.setIcon(new ImageIcon("w_bishop.png"));
                            }
                            promotionQueen.setVisible(true);
                            promotionKnight.setVisible(true);
                            promotionRook.setVisible(true);
                            promotionBishop.setVisible(true);
                            promotion = true;
                        }
                        if (white.getIsMyMove()) {
                            white.getMyPieces().get(pieceIndex).setX(f.getX_from());
                            white.getMyPieces().get(pieceIndex).setY(f.getY_from());
                            Piece king = black.getMyPieces().get(black.getKing());
                            ((King) king).isCheck(fields);
                            if (((King) king).isCheckMate()) {
                                result = white.getPlayerName() + " won";
                                out.append("\nCheckmate! player " + white.getPlayerName() + " won\n");
                                gameFinished = true;
                            }
                            if ((!((King) king).isCheck(fields)) && isStalemate(black)) {
                                result = "Draw\n";
                                gameFinished = true;
                            }
                            if (!promotion) {
                                white.setIsMyMove(false);
                            }
                            if (state == States.Server && sessionPlayer.getColor() == 1) {
                                white.writeMSG("MOVE-" + previousIndex + "-" + index);
                                if (!gameFinished) {
                                    waitForMSG();
                                    if (white.getIsFirstMove()) {
                                        playerCurrentTime.start();
                                    }
                                    waiting = true;
                                }
                            }
                            white.setIsFirstMove(false);
                            if (!gameFinished) {
                                black.setIsMyMove(true);
                            }
                        } else {
                            black.getMyPieces().get(pieceIndex).setX(f.getX_from());
                            black.getMyPieces().get(pieceIndex).setY(f.getY_from());
                            Piece king = white.getMyPieces().get(white.getKing());
                            ((King) king).isCheck(fields);
                            if (((King) king).isCheckMate()) {
                                result = black.getPlayerName() + " won";
                                out.append("\nCheckmate! player " + black.getPlayerName() + " won\n");
                                gameFinished = true;
                            }
                            if (!((King) king).isCheck(fields) && isStalemate(white)) {
                                result = "Draw\n";
                                gameFinished = true;
                            }
                            if (state == States.LOCAL) {
                                if (!white.getIsFirstMove() && !playerCurrentTime.isAlive()) {
                                    playerCurrentTime.start();
                                }
                            }
                            if (!promotion) {
                                black.setIsMyMove(false);
                            }
                            if (state == States.Server && sessionPlayer.getColor() == 0) {
                                black.writeMSG("MOVE-" + previousIndex + "-" + index);
                                if (!gameFinished) {
                                    waitForMSG();
                                    if (black.getIsFirstMove()) {
                                        playerCurrentTime.start();
                                    }
                                    waiting = true;
                                }
                            }
                            black.setIsFirstMove(false);
                            if (!gameFinished) {
                                white.setIsMyMove(true);
                            }
                        }
                    }
                    poss_moves = null;
                    fields.get(previousIndex).setIsSelected(false);
                    pieceIsSelected = false;
                    if (p != null) {
                        p.clearPossibleMoves();
                    }
                    if (promotion) {
                        if (player.getColor() == 0) {
                            black.setIsMyMove(true);
                            white.setIsMyMove(false);
                        } else {
                            white.setIsMyMove(true);
                            black.setIsMyMove(false);
                        }
                        previousIndex = index;
                    }
                }
            }
            index += 1;
        }
        if (gameFinished) {
            if (playerCurrentTime.isAlive()) {
                try {
                    playerCurrentTime.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            white.setIsMyMove(false);
            black.setIsMyMove(false);
        }
    }

    /**
     * Draws possible moves
     */
    private void drawPossibleMoves() {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        for (Field f : poss_moves) {
            if (f == null) {
                continue;
            }
            Color color = new Color(0, 255, 0, 125);
            g2d.setPaint(color);
            g2d.fillRect(f.getX_from() + 5, f.getY_from() + 5, squareWIDTH - 10, squareHEIGHT - 10);
        }
    }

    /**
     * Clears possible moves
     */
    private void clearPossibleMoves() {
        if (poss_moves == null) {
            return;
        }
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        for (Field f : poss_moves) {
            if (f.getFieldColor() == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint(new Color(0xaa5533));
            }
            g2d.fillRect(f.getX_from(), f.getY_from(), squareWIDTH, squareHEIGHT);
            if (f.getPiece() != null) {
                Image image = null;
                switch (f.getPiece()) {
                    case "pw":
                        image = new ImageIcon("w_pawn.png").getImage();
                        break;
                    case "rw":
                        image = new ImageIcon("w_rook.png").getImage();
                        break;
                    case "nw":
                        image = new ImageIcon("w_knight.png").getImage();
                        break;
                    case "bw":
                        image = new ImageIcon("w_bishop.png").getImage();
                        break;
                    case "qw":
                        image = new ImageIcon("w_queen.png").getImage();
                        break;
                    case "kw":
                        image = new ImageIcon("w_king.png").getImage();
                        break;
                    case "pb":
                        image = new ImageIcon("b_pawn.png").getImage();
                        break;
                    case "rb":
                        image = new ImageIcon("b_rook.png").getImage();
                        break;
                    case "nb":
                        image = new ImageIcon("b_knight.png").getImage();
                        break;
                    case "bb":
                        image = new ImageIcon("b_bishop.png").getImage();
                        break;
                    case "qb":
                        image = new ImageIcon("b_queen.png").getImage();
                        break;
                    case "kb":
                        image = new ImageIcon("b_king.png").getImage();
                        break;
                }
                g2d.drawImage(image, f.getX_from() + 17, f.getY_from() + 7, this);
            }
        }
    }

    /**
     *
     * @param xFrom starting coordinate of field
     * @param yFrom starting coordinate of field
     * @param xTo final coordinate of field
     * @param yTo final coordinate of field
     * @param pp piece to be moved
     * @param player current player
     * @return piece from myPieces list
     */
    private Piece findPiece(int xFrom, int yFrom, int xTo, int yTo, String pp, Player player) {
        Piece piece = null;
        pieceIndex = 0;
        int len = player.getMyPieces().size();
        for (int i = 0; i < len; ++i) {
            Piece p = player.getMyPieces().get(i);
            if (p == null) {
                continue;
            }
            if (xFrom <= p.getX() && xTo >= p.getX() && yFrom <= p.getY() && yTo >= p.getY() && pp.equals(p.getName())) {
                piece = p;
                pieceIndex = i;
                break;
            }
        }
        return piece;
    }

    /**
     * Occupies free field after manual setting
     */
    private void checkIsFree() {
        for (Field f : fields) {
            if (f.getPiece() != null && f.getIsFree()) {
                f.setIsFree(false);
            }
        }
        gameRun = true;
    }

    /**
     * Promotion
     *
     * @param x coordinate of pawn to be promoted
     * @param y coordinate of pawn to be promoted
     */
    private void promote(int x, int y) {
        Image image = null;
        Piece p = null;
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        switch (promoting) {
            case "qw":
                image = new ImageIcon("w_queen.png").getImage();
                p = new Queen(1, x, y, promoting, previousIndex);
                white.getMyPieces().set(pieceIndex, p);
                break;
            case "bw":
                image = new ImageIcon("w_bishop.png").getImage();
                p = new Bishop(1, x, y, promoting, previousIndex);
                white.getMyPieces().set(pieceIndex, p);
                break;
            case "rw":
                image = new ImageIcon("w_rook.png").getImage();
                p = new Rook(1, x, y, promoting, previousIndex);
                white.getMyPieces().set(pieceIndex, p);
                break;
            case "nw":
                image = new ImageIcon("w_knight.png").getImage();
                p = new Knight(1, x, y, promoting, previousIndex);
                white.getMyPieces().set(pieceIndex, p);
                break;
            case "qb":
                image = new ImageIcon("b_queen.png").getImage();
                p = new Queen(0, x, y, promoting, previousIndex);
                black.getMyPieces().set(pieceIndex, p);
                break;
            case "bb":
                image = new ImageIcon("b_bishop.png").getImage();
                p = new Bishop(0, x, y, promoting, previousIndex);
                black.getMyPieces().set(pieceIndex, p);
                break;
            case "rb":
                image = new ImageIcon("b_rook.png").getImage();
                p = new Rook(0, x, y, promoting, previousIndex);
                black.getMyPieces().set(pieceIndex, p);
                break;
            case "nb":
                image = new ImageIcon("b_knight.png").getImage();
                p = new Knight(0, x, y, promoting, previousIndex);
                black.getMyPieces().set(pieceIndex, p);
                break;
        }
        if (white.getIsMyMove()) { // white pawn is promoting
            white.setToMyPieces(p, pieceIndex);
            white.setIsMyMove(false);
            black.setIsMyMove(true);
        } else {                    // black pawn is promoting
            black.setToMyPieces(p, pieceIndex);
            white.setIsMyMove(true);
            black.setIsMyMove(false);
        }
        fields.get(previousIndex).setPiece(promoting);
        fields.get(previousIndex).setPieceColor(p.getColor());
        if (fields.get(previousIndex).getFieldColor() == 1) {
            g2d.setPaint(new Color(0xaa5533));
        } else {
            g2d.setPaint(Color.black);
        }
        g2d.fillRect(x, y, squareWIDTH, squareHEIGHT);
        g2d.drawImage(image, x + 17, y + 7, this);
        promotion = false;
        promotionBishop.setVisible(false);
        promotionQueen.setVisible(false);
        promotionRook.setVisible(false);
        promotionKnight.setVisible(false);
        if (state == States.Server) {
            sessionPlayer.writeMSG("PROM-" + promoting + "-" + previousIndex);
            waitForMSG();
            waiting = true;
        }
    }

    /**
     * Castling
     *
     * @param castlingSide 'q' - queen side, 'k' - king side
     */
    private void castling(char castlingSide) {
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        int rooksX = 0, rooksY = 0, rooksNewX = 0, rooksNewY = 0, rooksPreviousIndex = 0, rooksNewIndex = 0;
        int kingsX = 0, kingsY = 0, kingsNewX = 0, kingsNewY = 0, kingsPreviousIndex = 0, kingsNewIndex = 0;
        Player curr_player = white.getIsMyMove() ? white : black;
        ArrayList<Piece> myPieces = null;
        if (!white.getIsMyMove()) {
            myPieces = black.getMyPieces();
            switch (castlingSide) {
                case 'q':
                    rooksPreviousIndex = 0;
                    rooksNewIndex = 3;
                    kingsPreviousIndex = 4;
                    kingsNewIndex = 2;
                    rooksX = fields.get(rooksPreviousIndex).getX_from();
                    rooksY = fields.get(rooksPreviousIndex).getY_from();
                    rooksNewX = fields.get(rooksNewIndex).getX_from();
                    rooksNewY = fields.get(rooksNewIndex).getY_from();
                    kingsX = fields.get(kingsPreviousIndex).getX_from();
                    kingsY = fields.get(kingsPreviousIndex).getY_from();
                    kingsNewX = fields.get(kingsNewIndex).getX_from();
                    kingsNewY = fields.get(kingsNewIndex).getY_from();
                    g2d.setPaint(new Color(0xaa5533));
                    g2d.fillRect(rooksX, rooksY, squareWIDTH, squareHEIGHT);
                    g2d.fillRect(kingsX, kingsY, squareWIDTH, squareHEIGHT);
                    g2d.drawImage(new ImageIcon("b_king.png").getImage(),
                            kingsNewX + 17, kingsNewY + 7, this);
                    g2d.drawImage(new ImageIcon("b_rook.png").getImage(),
                            rooksNewX + 17, rooksNewY + 7, this);
                    break;
                case 'k':
                    rooksPreviousIndex = 7;
                    rooksNewIndex = 5;
                    kingsPreviousIndex = 4;
                    kingsNewIndex = 6;
                    rooksX = fields.get(rooksPreviousIndex).getX_from();
                    rooksY = fields.get(rooksPreviousIndex).getY_from();
                    rooksNewX = fields.get(rooksNewIndex).getX_from();
                    rooksNewY = fields.get(rooksNewIndex).getY_from();
                    kingsX = fields.get(kingsPreviousIndex).getX_from();
                    kingsY = fields.get(kingsPreviousIndex).getY_from();
                    kingsNewX = fields.get(kingsNewIndex).getX_from();
                    kingsNewY = fields.get(kingsNewIndex).getY_from();
                    g2d.setPaint(Color.black);
                    g2d.fillRect(rooksX, rooksY, squareWIDTH, squareHEIGHT);
                    g2d.setPaint(new Color(0xaa5533));
                    g2d.fillRect(kingsX, kingsY, squareWIDTH, squareHEIGHT);
                    g2d.drawImage(new ImageIcon("b_king.png").getImage(),
                            kingsNewX + 17, kingsNewY + 7, this);
                    g2d.drawImage(new ImageIcon("b_rook.png").getImage(),
                            rooksNewX + 17, rooksNewY + 7, this);
                    break;
            }
        } else {
            myPieces = white.getMyPieces();
            switch (castlingSide) {
                case 'q':
                    rooksPreviousIndex = 56;
                    rooksNewIndex = 59;
                    kingsPreviousIndex = 60;
                    kingsNewIndex = 58;
                    rooksX = fields.get(rooksPreviousIndex).getX_from();
                    rooksY = fields.get(rooksPreviousIndex).getY_from();
                    rooksNewX = fields.get(rooksNewIndex).getX_from();
                    rooksNewY = fields.get(rooksNewIndex).getY_from();
                    kingsX = fields.get(kingsPreviousIndex).getX_from();
                    kingsY = fields.get(kingsPreviousIndex).getY_from();
                    kingsNewX = fields.get(kingsNewIndex).getX_from();
                    kingsNewY = fields.get(kingsNewIndex).getY_from();

                    g2d.setPaint(Color.black);
                    g2d.fillRect(rooksX, rooksY, squareWIDTH, squareHEIGHT);
                    g2d.fillRect(kingsX, kingsY, squareWIDTH, squareHEIGHT);
                    g2d.drawImage(new ImageIcon("w_king.png").getImage(),
                            kingsNewX + 17, kingsNewY + 7, this);
                    g2d.drawImage(new ImageIcon("w_rook.png").getImage(),
                            rooksNewX + 17, rooksNewY + 7, this);
                    break;
                case 'k':
                    rooksPreviousIndex = 63;
                    rooksNewIndex = 61;
                    kingsPreviousIndex = 60;
                    kingsNewIndex = 62;
                    rooksX = fields.get(rooksPreviousIndex).getX_from();
                    rooksY = fields.get(rooksPreviousIndex).getY_from();
                    rooksNewX = fields.get(rooksNewIndex).getX_from();
                    rooksNewY = fields.get(rooksNewIndex).getY_from();
                    kingsX = fields.get(kingsPreviousIndex).getX_from();
                    kingsY = fields.get(kingsPreviousIndex).getY_from();
                    kingsNewX = fields.get(kingsNewIndex).getX_from();
                    kingsNewY = fields.get(kingsNewIndex).getY_from();
                    g2d.setPaint(new Color(0xaa5533));
                    g2d.fillRect(rooksX, rooksY, squareWIDTH, squareHEIGHT);
                    g2d.setPaint(Color.black);
                    g2d.fillRect(kingsX, kingsY, squareWIDTH, squareHEIGHT);
                    g2d.drawImage(new ImageIcon("w_king.png").getImage(),
                            kingsNewX + 17, kingsNewY + 7, this);
                    g2d.drawImage(new ImageIcon("w_rook.png").getImage(),
                            rooksNewX + 17, rooksNewY + 7, this);
                    break;
            }
        }
        //Rook
        int rooksIndexInMyPieces = curr_player.getIndexOfPiece(rooksPreviousIndex);
        myPieces.get(rooksIndexInMyPieces).setIndexInFields(rooksNewIndex);
        myPieces.get(rooksIndexInMyPieces).setX(rooksNewX);
        myPieces.get(rooksIndexInMyPieces).setY(rooksNewY);
        fields.get(rooksPreviousIndex).setIsFree(true);
        fields.get(rooksPreviousIndex).setPiece(null);
        fields.get(rooksPreviousIndex).setPieceColor(2);
        fields.get(rooksNewIndex).setIsFree(false);
        if (curr_player.getColor() == 0) {
            fields.get(rooksNewIndex).setPiece("rb");
        } else {
            fields.get(rooksNewIndex).setPiece("rw");
        }
        fields.get(rooksNewIndex).setPieceColor(curr_player.getColor());
        //King
        int kingsIndexInMyPieces = curr_player.getIndexOfPiece(kingsPreviousIndex);
        myPieces.get(kingsIndexInMyPieces).setIndexInFields(kingsNewIndex);
        myPieces.get(kingsIndexInMyPieces).setX(kingsNewX);
        myPieces.get(kingsIndexInMyPieces).setY(kingsNewY);
        fields.get(kingsPreviousIndex).setIsFree(true);
        fields.get(kingsPreviousIndex).setPiece(null);
        fields.get(kingsPreviousIndex).setPieceColor(2);
        fields.get(kingsNewIndex).setIsFree(false);
        if (curr_player.getColor() == 0) {
            fields.get(kingsNewIndex).setPiece("kb");
        } else {
            fields.get(kingsNewIndex).setPiece("kw");
        }
        fields.get(kingsNewIndex).setPieceColor(curr_player.getColor());

    }

    /**
     * Waits for reply (tryMove function for server game)
     *
     */
    public void waitForMSG() {
        if (state != States.Server && gameFinished) {
            return;
        }
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        Thread t = new Thread() {
            @Override
            public void run() {
                while (!sessionPlayer.getIsMyMove()) {
                    out.append("\nwaiting for msg...");
                    System.out.println("waiting for msg...");
                    receivedMSG = sessionPlayer.waitForMSG();
                    waiting = false;
                    if (gameFinished) {
                        return;
                    }
                    if (receivedMSG == null) {
                        return;
                    }
                    if (receivedMSG.startsWith("MOVE")) {
                        if (sessionPlayer.getColor() == 0) {
                            String indecies[] = receivedMSG.split("-");
                            int index_from = Integer.parseInt(indecies[1]);
                            int index_to = Integer.parseInt(indecies[2]);
                            paintRecievedMove(index_from, index_to);
                            receivedMSG = null;
                            Piece p = white.getMyPieces().get(white.getIndexOfPiece(index_from));
                            if (p.getName().equals("pw")) {
                                if (index_to == index_from - 16) {
                                    Piece pawn = white.getMyPieces().get(white.getIndexOfPiece(index_from));
                                    ((Pawn) pawn).setMoveOnSecondField(true);
                                } else if ((index_to == index_from - 9 || index_to == index_from - 7)
                                        && fields.get(index_to).getIsFree()) {
                                    sessionPlayer.setToMyPieces(null, sessionPlayer.getIndexOfPiece(index_to + 8));
                                    fields.get(index_to + 8).setIsFree(true);
                                    fields.get(index_to + 8).setPiece(null);
                                    fields.get(index_to + 8).setPieceColor(2);
                                    if (fields.get(index_to + 8).getFieldColor() == 0) {
                                        g2d.setPaint(Color.black);
                                    } else {
                                        g2d.setPaint(new Color(0xaa5533));
                                    }
                                    g2d.fillRect(fields.get(index_to + 8).getX_from(), fields.get(index_to + 8).getY_from(),
                                            squareWIDTH, squareHEIGHT);
                                }
                            }
                            fields.get(index_to).setPiece(fields.get(index_from).getPiece());
                            fields.get(index_from).setPiece(null);
                            fields.get(index_to).setPieceColor(1);
                            fields.get(index_from).setPieceColor(2);
                            fields.get(index_to).setIsFree(false);
                            fields.get(index_from).setIsFree(true);
                            white.getMyPieces().get(white.getIndexOfPiece(index_from)).setX(fields.get(index_to).getX_from());
                            white.getMyPieces().get(white.getIndexOfPiece(index_from)).setY(fields.get(index_to).getY_from());
                            white.getMyPieces().get(white.getIndexOfPiece(index_from)).setIndexInFields(index_to);
                            if (p.getName().equals("pw") && (index_to >= 0 && index_to <= 7)) {
                                sessionPlayer.setIsMyMove(false);
                                waiting = true;
                            } else {
                                white.setIsMyMove(false);
                                Piece king = black.getMyPieces().get(black.getKing());
                                ((King) king).isCheck(fields);
                                if (((King) king).isCheckMate()) {
                                    out.append("\nCheckmate! White won\n");
                                    sessionPlayer.writeMSG("FINISHED");
                                    gameFinished = true;
                                }
                                if ((!((King) king).getIsCheck()) && isStalemate(black)) {
                                    sessionPlayer.writeMSG("FINISHED");
                                    gameFinished = true;
                                }
                                if (!gameFinished) {
                                    sessionPlayer.setIsMyMove(true);
                                }
                            }
                        } else {
                            String indecies[] = receivedMSG.split("-");
                            int index_from = Integer.parseInt(indecies[1]);
                            int index_to = Integer.parseInt(indecies[2]);
                            paintRecievedMove(index_from, index_to);
                            receivedMSG = null;
                            Piece p = black.getMyPieces().get(black.getIndexOfPiece(index_from));
                            if (p.getName().equals("pb")) {
                                if (index_to == index_from + 16) {
                                    Piece pawn = black.getMyPieces().get(black.getIndexOfPiece(index_from));
                                    ((Pawn) pawn).setMoveOnSecondField(true);
                                } else if ((index_to == index_from + 9 || index_to == index_from + 7)
                                        && fields.get(index_to).getIsFree()) {
                                    sessionPlayer.setToMyPieces(null, sessionPlayer.getIndexOfPiece(index_to - 8));
                                    fields.get(index_to - 8).setIsFree(true);
                                    fields.get(index_to - 8).setPiece(null);
                                    fields.get(index_to - 8).setPieceColor(2);
                                    if (fields.get(index_to - 8).getFieldColor() == 0) {
                                        g2d.setPaint(Color.black);
                                    } else {
                                        g2d.setPaint(new Color(0xaa5533));
                                    }
                                    g2d.fillRect(fields.get(index_to - 8).getX_from(), fields.get(index_to - 8).getY_from(),
                                            squareWIDTH, squareHEIGHT);
                                }
                            }
                            fields.get(index_to).setPiece(fields.get(index_from).getPiece());
                            fields.get(index_from).setPiece(null);
                            fields.get(index_to).setPieceColor(0);
                            fields.get(index_from).setPieceColor(2);
                            fields.get(index_to).setIsFree(false);
                            fields.get(index_from).setIsFree(true);
                            black.getMyPieces().get(black.getIndexOfPiece(index_from)).setX(fields.get(index_to).getX_from());
                            black.getMyPieces().get(black.getIndexOfPiece(index_from)).setY(fields.get(index_to).getY_from());
                            black.getMyPieces().get(black.getIndexOfPiece(index_from)).setIndexInFields(index_to);
                            if (p.getName().equals("pb") && (index_to >= 56 && index_to <= 63)) {
                                sessionPlayer.setIsMyMove(false);
                                waiting = true;
                            } else {
                                black.setIsMyMove(false);
                                Piece king = white.getMyPieces().get(white.getKing());
                                ((King) king).isCheck(fields);
                                if (((King) king).isCheckMate()) {
                                    out.append("\nCheckmate! Black won\n");
                                    sessionPlayer.writeMSG("FINISHED");
                                    gameFinished = true;
                                    white.setIsMyMove(false);
                                }
                                if (!((King) king).getIsCheck() && isStalemate(white)) {
                                    sessionPlayer.writeMSG("FINISHED");
                                    gameFinished = true;
                                    white.setIsMyMove(false);

                                }
                                if (!gameFinished) {
                                    sessionPlayer.setIsMyMove(true);
                                }
                            }
                        }
                    } else if (receivedMSG.startsWith("CAST")) {
                        String move[] = receivedMSG.split("-");
                        char castlingSide = move[1].charAt(0);
                        castling(castlingSide);
                        if (sessionPlayer.getColor() == 0) {
                            white.setIsMyMove(false);
                            sessionPlayer.setIsMyMove(true);
                        } else {
                            sessionPlayer.setIsMyMove(true);
                            black.setIsMyMove(false);
                        }
                    } else if (receivedMSG.startsWith("PROM")) {
                        String promote[] = receivedMSG.split("-");
                        String promotingPiece = promote[1];
                        int index = Integer.parseInt(promote[2]);
                        Image image = null;
                        Piece p = null;
                        if (sessionPlayer.getColor() == 0) {
                            switch (promotingPiece) {
                                case "qw":
                                    image = new ImageIcon("w_queen.png").getImage();
                                    p = new Queen(1, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "rw":
                                    image = new ImageIcon("w_rook.png").getImage();
                                    p = new Rook(1, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "nw":
                                    image = new ImageIcon("w_knight.png").getImage();
                                    p = new Knight(1, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "bw":
                                    image = new ImageIcon("w_bishop.png").getImage();
                                    p = new Bishop(1, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                            }
                        } else {
                            switch (promotingPiece) {
                                case "qb":
                                    image = new ImageIcon("b_queen.png").getImage();
                                    p = new Queen(0, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "rb":
                                    image = new ImageIcon("b_rook.png").getImage();
                                    p = new Rook(0, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "nb":
                                    image = new ImageIcon("b_knight.png").getImage();
                                    p = new Knight(0, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                                case "bb":
                                    image = new ImageIcon("b_bishop.png").getImage();
                                    p = new Bishop(0, fields.get(index).getX_from(), fields.get(index).getY_from(),
                                            promotingPiece, index);
                                    break;
                            }
                        }
                        drawPromotion(image, index, p);
                    } else if (receivedMSG.equals("AUTO-WHITE") && sessionPlayer.getColor() == 0) {
                        setFigures(1);
                        removeWhiteFiguresForManualSetting();
                        white.setPiecesAreSet(true);
                        setPieceButton.setEnabled(true);
                        if (black.getPiecesAreSet()) {
                            figuresAreSet = true;
                            setPieceButton.setBackground(new Color(0xff0000));
                            setPieceButton.setEnabled(false);
                        }
                        waiting = true;
                        black.setIsMyMove(true);
                        white.setIsMyMove(false);
                    } else if (receivedMSG.equals("AUTO-BLACK") && sessionPlayer.getColor() == 1) {
                        setFigures(0);
                        removeBlackFiguresForManualSetting();
                        black.setPiecesAreSet(true);
                        if (white.getPiecesAreSet()) {
                            figuresAreSet = true;
                            setPieceButton.setBackground(new Color(0xff0000));
                            setPieceButton.setEnabled(false);
                        }
                        white.setIsMyMove(true);
                    } else if (receivedMSG.startsWith("QUIT")) {
                        gameFinished = true;
                        out.append("\nOpponent left the game!");
                    } else if (receivedMSG.startsWith("FINISHED")) {
                        gameFinished = true;
                        white.setIsMyMove(false);
                        black.setIsMyMove(false);
                    }
                }
            }
        };
        if (!gameFinished) {
            t.start();
        }
    }

    /**
     * Paints recieved move
     *
     * @param indexFrom previous index of piece
     * @param indexTo current index of piece
     */
    private void paintRecievedMove(int indexFrom, int indexTo) {
        if (state != States.Server) {
            return;
        }
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        if (sessionPlayer.getColor() == 0) {
            if (fields.get(indexTo).getPieceColor() == 0) { // white killed black piece
                sessionPlayer.setToMyPieces(null, sessionPlayer.getIndexOfPiece(indexTo));
            }
            if (fields.get(indexTo).getFieldColor() == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint((new Color(0xaa5533)));
            }
            g2d.fillRect(fields.get(indexTo).getX_from(), fields.get(indexTo).getY_from(),
                    squareWIDTH, squareHEIGHT);

            g2d.drawImage(getImage(fields.get(indexFrom).getPiece()),
                    fields.get(indexTo).getX_from() + 17,
                    fields.get(indexTo).getY_from() + 7, this);
            if (fields.get(indexFrom).getFieldColor() == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint((new Color(0xaa5533)));
            }
            g2d.fillRect(fields.get(indexFrom).getX_from(), fields.get(indexFrom).getY_from(),
                    squareWIDTH, squareHEIGHT);
        } else if (sessionPlayer.getColor() == 1) {
            if (fields.get(indexTo).getPieceColor() == 1) { // black killed white piece
                sessionPlayer.setToMyPieces(null, sessionPlayer.getIndexOfPiece(indexTo));
            }
            if (fields.get(indexTo).getFieldColor() == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint((new Color(0xaa5533)));
            }
            g2d.fillRect(fields.get(indexTo).getX_from(), fields.get(indexTo).getY_from(),
                    squareWIDTH, squareHEIGHT);
            g2d.drawImage(getImage(fields.get(indexFrom).getPiece()),
                    fields.get(indexTo).getX_from() + 17,
                    fields.get(indexTo).getY_from() + 7, this);
            if (fields.get(indexFrom).getFieldColor() == 0) {
                g2d.setPaint(Color.black);
            } else {
                g2d.setPaint((new Color(0xaa5533)));
            }
            g2d.fillRect(fields.get(indexFrom).getX_from(), fields.get(indexFrom).getY_from(),
                    squareWIDTH, squareHEIGHT);
        }
    }

    /**
     * Draws promotion for server game
     *
     * @param image of the new piece
     * @param index position of the pawn
     * @param p chosen Piece (Queen, Bishop, Knight or Rook)
     */
    private void drawPromotion(Image image, int index, Piece p) {
        if (state != States.Server) {
            return;
        }
        Graphics2D g2d = (Graphics2D) this.getGraphics();
        if (fields.get(index).getFieldColor() == 0) {
            g2d.setPaint(Color.black);
        } else {
            g2d.setPaint(new Color(0xaa5533));
        }
        g2d.fillRect(fields.get(index).getX_from(), fields.get(index).getY_from(),
                squareWIDTH, squareHEIGHT);
        g2d.drawImage(image, fields.get(index).getX_from() + 17,
                fields.get(index).getY_from() + 7, this);
        if (sessionPlayer.getColor() == 0) {
            white.setToMyPieces(p, white.getIndexOfPiece(index));
            white.setIsMyMove(false);
            sessionPlayer.setIsMyMove(true);
        } else {
            black.setToMyPieces(p, black.getIndexOfPiece(index));
            sessionPlayer.setIsMyMove(true);
            black.setIsMyMove(false);
        }
        fields.get(index).setPiece(p.getName());
        fields.get(index).setPieceColor(p.getColor());
    }

    /**
     * Checks stalemate situation
     *
     * @param player current player
     * @return true if is stalemate else false
     */
    private boolean isStalemate(Player player) {
        int checkPiece = 0;
        boolean ret = false;
        if (player.getColor() == 0) {
            for (Piece oppPiece : black.getMyPieces()) {
                if (oppPiece == null) {
                    checkPiece++;
                    continue;
                }
                oppPiece.createPossibleMoves(fields, oppPiece.getIndexInFields());
                poss_moves = oppPiece.checkPossibleMoves(fields, oppPiece.getIndexInFields());
                if (poss_moves == null) {
                    oppPiece.createAttackMoves(fields, oppPiece.getIndexInFields());
                    poss_moves = oppPiece.checkAttackMoves(fields, oppPiece.getIndexInFields());
                }
                if (poss_moves == null || poss_moves.isEmpty()) {
                    checkPiece++;
                }
            }
            if (checkPiece == 16) {
                out.append("\nStalemate! Black has no possible moves");
                ret = true;
            }
        } else {
            for (Piece oppPiece : white.getMyPieces()) {
                if (oppPiece == null) {
                    checkPiece++;
                    continue;
                }
                oppPiece.createPossibleMoves(fields, oppPiece.getIndexInFields());
                poss_moves = oppPiece.checkPossibleMoves(fields, oppPiece.getIndexInFields());
                if (poss_moves == null) {
                    oppPiece.createAttackMoves(fields, oppPiece.getIndexInFields());
                    poss_moves = oppPiece.checkAttackMoves(fields, oppPiece.getIndexInFields());
                }
                if (poss_moves == null || poss_moves.isEmpty()) {
                    checkPiece++;
                }
            }
            if (checkPiece == 16) {
                out.append("\nStalemate! White has no possible moves");
                ret = true;
            }
        }
        return ret;
    }

    /**
     *
     * @return game duration
     */
    private String getGameDuration() {
        int whiteGameDuration = 900 - (minutesWhite * 60 + secondsWhite);
        int blackGameDuration = 900 - (minutesBlack * 60 + secondsBlack);
        int minutes = (whiteGameDuration + blackGameDuration) / 60;
        int seconds = (whiteGameDuration + blackGameDuration) % 60;
        DecimalFormat df = new DecimalFormat("00");
        String secondsDF = df.format(seconds);
        String minutesDF = df.format(minutes);
        return minutesDF + ":" + secondsDF;
    }
}
