import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * BlackjackView class.
 * Implements the UI of the blackjack game.
 */
public class BlackjackView extends JFrame {
    private BlackjackModel blackjackModel;
    /**
     * JLabel array holding the cards assets.
     */
    private JLabel[][] deckLabel; // holds all pictures of cards
    /**
     * Linked list holding the cards that are already dealt.
     */
    private LinkedList<JLabel> cards; // holds the currently dealt cards

    private JLabel cardBack; // back of card picture
    private JLabel background; // background picture
    private JLabel[] betLabels; // chips pictures

    private JLabel balanceTotal;
    private JLabel betTotal;
    private JLabel playerTotal;
    private JLabel dealerTotal;
    private JLabel dealerWin;
    private JLabel playerWin;
    private JLabel draw;
    private JLabel chip5;
    private JLabel chip10;
    private JLabel chip15;
    private JLabel chip20;
    private JLabel chip25;

    private JButton dealBtn;
    private JButton standBtn;
    private JButton hitBtn;
    private JButton exitBtn;
    private JButton dblDownBtn;

    private int x_player; // x position for player cards
    private int x_dealer; // x position for dealer cards
    private final int Y_DEALER = 70; // y position for dealer cards
    private final int Y_PLAYER = 304; // y position for player cards
    public boolean running; // while animation is playing running is true

    /**
     * Constructor.
     * Initializes all the elements of the UI; assigns code for button interactions.
     * @param black A reference to the BlackjackModel object.
     */
    public BlackjackView(BlackjackModel black) {
        super("CasinoSimulator - Blackjack");
        blackjackModel = black;
        x_dealer = x_player = 30; // initial x position
        cards = new LinkedList<>();
        betLabels = new JLabel[5];
        running = false;

        // init window
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLayout(null);

        // Balance and bet labels
        JLabel balanceLabel = new JLabel("Balance: $");
        balanceLabel.setBounds(470, 10, 63, 30);
        balanceLabel.setForeground(Color.WHITE);

        balanceTotal = new JLabel("0");
        balanceTotal.setBounds(533, 10, 50, 30);
        balanceTotal.setForeground(Color.WHITE);

        JLabel betLabel = new JLabel("Bet: $");
        betLabel.setBounds(470, 50, 35, 30);
        betLabel.setForeground(Color.WHITE);

        betTotal = new JLabel("0");
        betTotal.setBounds(505, 50, 50, 30);
        betTotal.setForeground(Color.WHITE);

        // Dealer wins label
        dealerWin = new JLabel( "Dealer Wins!");
        dealerWin.setBounds(260, 500, 100, 70);
        dealerWin.setForeground(Color.WHITE);
        dealerWin.setVisible(false);

        // Player wins label
        playerWin = new JLabel("Player Wins!");
        playerWin.setBounds(260, 500, 100, 70);
        playerWin.setForeground(Color.WHITE);
        playerWin.setVisible(false);

        // It's a tie label
        draw = new JLabel("It's a Draw");
        draw.setBounds(260, 450, 100, 70);
        draw.setForeground(Color.WHITE);
        draw.setVisible(false);

        // Other labels
        JLabel dealerLabel = new JLabel("Dealer");
        dealerLabel.setBounds(275, 10, 50, 30);
        dealerLabel.setForeground(Color.WHITE);

        dealerTotal = new JLabel("0");
        dealerTotal.setBounds(330, 10, 50, 30);
        dealerTotal.setForeground(Color.WHITE);

        JLabel playerLabel = new JLabel("Player");
        playerLabel.setBounds(275, 234, 50, 30);
        playerLabel.setForeground(Color.WHITE);

        playerTotal = new JLabel("0");
        playerTotal.setBounds(330, 234, 50, 30);
        playerTotal.setForeground(Color.WHITE);

        // Chip labels
        chip5 = new JLabel("$5");
        chip5.setBounds(52, 570, 50, 30);
        chip5.setForeground(Color.WHITE);
        chip5.setFont(new Font("Dialog", Font.PLAIN, 14));
        chip5.setVisible(false);

        chip10 = new JLabel("$10");
        chip10.setBounds(118, 570, 50, 30);
        chip10.setForeground(Color.WHITE);
        chip10.setFont(new Font("Dialog", Font.PLAIN, 14));
        chip10.setVisible(false);

        chip15 = new JLabel("$15");
        chip15.setBounds(188, 570, 50, 30);
        chip15.setForeground(Color.WHITE);
        chip15.setFont(new Font("Dialog", Font.PLAIN, 14));
        chip15.setVisible(false);

        chip20 = new JLabel("$20");
        chip20.setBounds(258, 570, 50, 30);
        chip20.setForeground(Color.WHITE);
        chip20.setFont(new Font("Dialog", Font.PLAIN, 14));
        chip20.setVisible(false);

        chip25 = new JLabel("$25");
        chip25.setBounds(330, 570, 50, 30);
        chip25.setForeground(Color.WHITE);
        chip25.setFont(new Font("Dialog", Font.PLAIN, 14));
        chip25.setVisible(false);

        // Game buttons
        dealBtn = new JButton("Deal");
        dealBtn = new JButton("Deal");
        dealBtn.setBounds(95, 630, 100, 30);
        //dealBtn.setEnabled(false);

        standBtn = new JButton("Stand");
        standBtn.setBounds(200, 630, 100, 30);
        standBtn.setEnabled(false);

        hitBtn = new JButton("Hit");
        hitBtn.setBounds(305, 630, 100, 30);
        hitBtn.setEnabled(false);

        exitBtn = new JButton("Exit");
        exitBtn.setBounds(250, 730, 100, 30);

        dblDownBtn = new JButton("Double Down");
        dblDownBtn.setBounds(410,630,120,30);
        dblDownBtn.setEnabled(false);

        LoadAssets(); // load card images
        InitBetLabels(); // init mouse click events for each chip label

        if(MainMenuView.gamemode == "Simulated Casino") {
            ShowBetLabels(true);
            dealBtn.setEnabled(false);
            showChipValues();
        }
        else { //Gamemode is set to freeplay
            dealBtn.setEnabled(true);
            standBtn.setEnabled(false);
            hitBtn.setEnabled(false);
            ShowBetLabels(false);
            hideChipValues();
        }

        // Deal button code
        dealBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dealBtn.setEnabled(false);
                hideChipValues();
                ShowBetLabels(false);
                blackjackModel.deal();
                standBtn.setEnabled(true);
                hitBtn.setEnabled(true);
                if (blackjackModel.getBalance() >= blackjackModel.getBet()) dblDownBtn.setEnabled(true);
                repaint();
            }
        });

        // Stand button code
        standBtn.addActionListener(new ActionListener() {   //player hit stand button
            @Override
            public void actionPerformed(ActionEvent e) {
                hitBtn.setEnabled(false);
                standBtn.setEnabled(false);
                dblDownBtn.setEnabled(false);
                blackjackModel.playerStand();
                repaint();
            }
        });

        // Exit button code
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blackjackModel.exit();
                dispose();
            }
        });

        // Hit button code
        hitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dblDownBtn.setEnabled(false);
                blackjackModel.playerHit();
                repaint();
            }
        });

        //Double Down Button Code
        dblDownBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dblDownBtn.setEnabled(false);
                blackjackModel.doubleDown();
            }
        });

        // Add all assets
        add(background);
        background.add(cardBack);
        background.add(balanceLabel);
        background.add(betLabel);
        background.add(balanceTotal);
        background.add(betTotal);
        background.add(dealerLabel);
        background.add(dealerTotal);
        background.add(playerLabel);
        background.add(playerTotal);
        background.add(dealBtn);
        background.add(hitBtn);
        background.add(standBtn);
        background.add(exitBtn);
        background.add(dealerWin);
        background.add(playerWin);
        background.add(draw);
        background.add(dblDownBtn);
        background.add(chip5);
        background.add(chip10);
        background.add(chip15);
        background.add(chip20);
        background.add(chip25);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * hideChipValues method.
     * Hide the labels representing the values of the chips.
     */
    public void hideChipValues(){
        chip5.setVisible(false);
        chip10.setVisible(false);
        chip15.setVisible(false);
        chip20.setVisible(false);
        chip25.setVisible(false);
    }

    /**
     * showChipValues method.
     * Show the labels representing the values of the chips.
     */
    public void showChipValues(){
        chip5.setVisible(true);
        chip10.setVisible(true);
        chip15.setVisible(true);
        chip20.setVisible(true);
        chip25.setVisible(true);
    }

    /**
     * ShowPlayerCard method.
     * Shows the player's card on the screen after it was dealt.
     */
    public void ShowPlayerCard(int[] c) {
        deckLabel[c[0]][c[1]].setBounds(600, 0, 96, 144); // initial position upper-right corner
        background.add(deckLabel[c[0]][c[1]]);
        playerTotal.setText(String.valueOf(blackjackModel.getPlayerTotal()));
        cards.add(deckLabel[c[0]][c[1]]);

        // run animation
        running = true;
        animThread a = new animThread(deckLabel[c[0]][c[1]], x_player, Y_PLAYER);
        a.execute();

        while (running) {
            try { Thread.sleep(100); }
            catch (InterruptedException e) {}
        }

        x_player += 96;
    }

    /**
     * ShowDealerCard method.
     * Show the dealer's card on the screen after it was dealt.
     */
    public void ShowDealerCard(int[] c) {
        background.add(deckLabel[c[0]][c[1]]);
        dealerTotal.setText(String.valueOf(blackjackModel.getDealerTotal()));
        cards.add(deckLabel[c[0]][c[1]]);

        // if carBack is visible flip it; else run animation
        if (!cardBack.isVisible()) {
            deckLabel[c[0]][c[1]].setBounds(600, 0, 96, 144);

            // run animation
            running = true;
            animThread a = new animThread(deckLabel[c[0]][c[1]], x_dealer, Y_DEALER);
            a.execute();

            while (running) {
                try { Thread.sleep(100); }
                catch (InterruptedException e) {}
            }
        }
        else deckLabel[c[0]][c[1]].setBounds(x_dealer, Y_DEALER, 96, 144);

        x_dealer += 96;
    }

    /**
     * ShowBackCard method.
     * Show the back of a card on the screen.
     */
    public void ShowBackCard(boolean visible) {
        cardBack.setVisible(visible);
        if (visible) {
            // run animation
            running = true;
            animThread a = new animThread(cardBack, x_dealer, Y_DEALER);
            a.execute();

            while (running) {
                try { Thread.sleep(100); }
                catch (InterruptedException e) {}
            }
        }
    }

    /**
     * UpdateBalance method.
     * Update the balance label with current balance.
     */
    public void UpdateBalance() { balanceTotal.setText(String.valueOf(blackjackModel.getBalance())); }
    /**
     * UpdateBet method.
     * Update the bet label with current bet.
     */
    public void UpdateBet() { betTotal.setText(String.valueOf(blackjackModel.getBet())); }

    /**
     * Get variables ready for another game.
     */
    public void Reset(JLabel l) {
        ActionListener ac = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(blackjackModel.getBalance() <= 0){   //if player money is zero then exit
                    blackjackModel.exit();
                    dispose();
                }
                else {
                    l.setVisible(false);
                    dealerTotal.setText(String.valueOf(blackjackModel.getDealerTotal()));
                    playerTotal.setText(String.valueOf(blackjackModel.getPlayerTotal()));

                    for (JLabel j : cards)
                        background.remove(j);
                    cards.clear();

                    if(MainMenuView.gamemode == "Simulated Casino") {
                        ShowBetLabels(true);
                        dealBtn.setEnabled(false);
                        showChipValues();
                    }
                    else{
                        ShowBetLabels(false);
                        dealBtn.setEnabled(true);
                        hideChipValues();
                    }

                    repaint();
                }
            }
        };

        standBtn.setEnabled(false);
        hitBtn.setEnabled(false);
        dblDownBtn.setEnabled(false);
        x_dealer = x_player = 30;

        Timer t = new Timer(2000, ac); // wait 2 seconds before executing ac
        t.setRepeats(false);
        t.start();
    }

    /**
     * ShowDealerWin method.
     * Dealer won; update the label and reset UI.
     */
    public void ShowDealerWin() {
        dealerWin.setVisible(true);
        try {
            playLoseSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reset(dealerWin);
    }
  
    /**
     * ShowPlayerWin method.
     * Player won; update the label and reset UI.
     */
    public void ShowPlayerWin() {
        playerWin.setVisible(true);
        try {
            playWinSound();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Reset(playerWin);
    }
  
    /**
     * ShowDraw method.
     * It's a draw; update the label and reset UI.
     */
    public void ShowDraw() { draw.setVisible(true); Reset(draw); }

    /**
     * ShowBetLabels method.
     * Set each betLabel's visibility to visible.
     */
    public void ShowBetLabels(boolean visible) {
        for (JLabel l : betLabels) l.setVisible(visible);
    }

    /**
     * InitBetLabels method.
     * Assign a MouseListener for each betLabel, so it can be clicked.
     */
    private void InitBetLabels() {
        int bet = 5;
        for (JLabel l : betLabels) {
            int finalBet = bet;
            l.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    blackjackModel.playerBet(finalBet);
                    dealBtn.setEnabled(true);
                    try {
                        playChipSound();
                    } catch (LineUnavailableException ex) {
                        ex.printStackTrace();
                    } catch (UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            bet += 5;
        }
    }

    /**
     * LoadAsset method.
     * Load all pictures from Assets folder.
     */
    private void LoadAssets() {
        deckLabel = new JLabel[4][13]; // 4 rows & 13 columns; the rows are the suits
        BufferedImage img = null;
        String path = System.getProperty("user.dir"); // get main folder path
        String[] suit = new String[] { "clubs", "diamonds", "hearts", "spades" };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                try {
                    img = ImageIO.read(new File(path + "/Assets/Blackjack/" + "card-" + suit[i] + "-" + (j+1) + ".png"));
                }
                catch (Exception e) { System.out.println("Cannot load image!"); }

                deckLabel[i][j] = new JLabel(new ImageIcon(img));
            }
        }

        // Load back of card picture.
        try {
            img = ImageIO.read(new File(path + "/Assets/Blackjack/card-back2.png"));
        }
        catch (Exception e) { System.out.println("Cannot load card back image!"); }
        cardBack = new JLabel(new ImageIcon(img));
        cardBack.setBounds(600, 0, 96, 144);
        cardBack.setVisible(false);

        // Load background
        try {
            img = ImageIO.read(new File(path + "/Assets/Blackjack/background.jpg"));
        }
        catch (Exception e) { System.out.println("Cannot load background image!"); }
        background = new JLabel(new ImageIcon(img));
        background.setBounds(0, 0, 600, 800);

        // Load chips
        int index = 5;
        int x_chip = 30;
        for (int i = 0; i < 5; i++) {

            try {
                img = ImageIO.read(new File(path + "/Assets/Blackjack/chip" + index + ".png"));
            } catch (Exception e) {
                System.out.println("Cannot load chip5 image!");
            }
            JLabel l = new JLabel(new ImageIcon(img));
            l.setBounds(x_chip, 500, 63, 70);
            background.add(l);
            l.setVisible(false);
            betLabels[i] = l;
            index += 5;
            x_chip += 70;
        }
    }

    /**
     * playChipSound method.
     * Plays a wav file when selecting a bet.
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public void playChipSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        String path = System.getProperty("user.dir");
        File audioFile = new File(path + "/Sounds/ChipSound.wav").getAbsoluteFile();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        //Plays audio once
        clip.start();
    }

    /**
     * playWinSound method.
     * Plays a wav file when the player wins.
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public void playWinSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        String path = System.getProperty("user.dir");
        File audioFile = new File(path + "/Sounds/Win.wav").getAbsoluteFile();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        //Plays audio once
        clip.start();
    }

    /**
     * playLoseSound method.
     * Plays a wav file when the player loses.
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     * @throws IOException
     */
    public void playLoseSound() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        String path = System.getProperty("user.dir");
        File audioFile = new File(path + "/Sounds/Lose.wav").getAbsoluteFile();
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        //Plays audio once
        clip.start();
    }

    /**
     * Animation class.
     * Implements the logic for animating a card.
     * Extends SwingerWorker which starts a thread that runs in the background.
     */
    private class animThread extends SwingWorker {
        private JLabel j;
        private int dX, dY, finX, finY, currX, currY, speed, distance, moves, xUnits, yUnits;

        /**
         * Constructor.
         * Initializes the variables.
         * @param j A label representing the card to be animated.
         * @param x The final x position.
         * @param y The final y position.
         */
        public animThread(JLabel j, int x, int y) {
            this.j = j;
            finX = x; // final x position
            finY = y; // final y position
            currX = 600;
            currY = 0;
            speed = 25;
            dX = Math.abs(finX - currX);
            dY = finY - currY;
            distance = (int) Math.sqrt(dX * dX + dY * dY);
            moves = distance / speed; // how many moves it takes to get there
            // calculate one step
            xUnits = dX / moves;
            yUnits = dY / moves;
        }

        /**
         * doInBackground method.
         * The actual animation logic. Runs as a separate thread in the background.
         * @return Returns null.
         * @throws Exception
         */
        @Override
        protected Object doInBackground() throws Exception {
            while (moves > 0) {
                moves--;
                currX -= xUnits;
                currY += yUnits;
                j.setBounds(currX, currY, 96, 144);
                repaint();
                Thread.sleep(10);
            }

            return null;
        }

        /**
         * done method.
         * The code in this method runs after the doInBackground method finishes.
         */
        @Override
        protected void done() {
            j.setBounds(finX, finY, 96, 144); // adjust final position
            running = false;
        }
    }

    /*
     * For testing
     */
    public JButton getDealBtn() { return dealBtn; }
    public JButton getHitBtn() { return hitBtn; }
    public JButton getStandBtn() { return standBtn; }
}
