import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

// Helper class extending JFrame class
public class WindowManager extends JFrame
{
    private static JLabel menuLabel = new JLabel();
    private static JFrame mainFrame = new JFrame("Click Game");
    private static JButton clickScreenButton = new JButton();
    // Creating a panel to add buttons

    private JPanel menuPanel = new JPanel(null);
    private static JPanel gamePanel = new JPanel(null);
    private JPanel settingsPanel = new JPanel(new GridBagLayout());
    private JPanel settingsButtonPanel = new JPanel(new GridLayout());
    private JLabel gameLabel = new JLabel("                    CLICK SCREEN WHEN READY");
    private GridBagConstraints gbc = new GridBagConstraints();



    public void openMenu() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {

        JButton startButton = new JButton("GAME");
        // Adding buttons and text-field to panel
        // using add() method

//        gbc.gridx = 0;
//        gbc.gridy = 4;
//        gbc.insets = new Insets(0,5,0,5);
//        menuPanel.add(startButton,gbc);
        menuPanel.add(startButton);
        startButton.setBounds(730,150,100,50);

        JButton settingsButton = new JButton("Settings");
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.anchor = GridBagConstraints.SOUTH;
//        gbc.insets = new Insets(5,5,5,5);
//        menuPanel.add(settingsButton,gbc);
        settingsButtonPanel.add(settingsButton);
        settingsButton.setBounds(450,450,100,50);

        JButton backButton = new JButton("Back");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        settingsPanel.add(backButton,gbc);

        menuLabel.setIcon(Audio.imageSet("click_game_logo.png"));
        menuLabel.setLocation(60, -20);
        menuLabel.setSize(1000, 700);
        menuPanel.add(menuLabel);

        settingsButtonPanel.setBounds(450,450,100,50);

//        menuPanel.add(settingsButtonPanel);




//        menuPanel.add(settingsButton,gbc);
        // set background of panel
        menuPanel.setBackground(Color.black);
        // Adding panel to frame
        mainFrame.add(menuPanel);
        // Setting the size of frame
        mainFrame.setSize(1000, 700);
        mainFrame.setVisible(true);
        settingsPanel.setVisible(false);
        settingsPanel.setBackground(Color.blue);
        mainFrame.add(settingsPanel);
        //start button actions
        Audio.playAudio();




        startButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openGame();
            }
        });
        settingsButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                openSettings();
            }
        });
        backButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                backToMenu();
            }
        });

        JSlider volumeBar = new JSlider(JSlider.VERTICAL, 0, 15, 0);
        gbc.gridx = 50;
        gbc.gridy = -10;
        gbc.gridheight = 10;
        gbc.fill = GridBagConstraints.VERTICAL;

        settingsPanel.add(volumeBar,gbc);
        volumeBar.setValue(50);
        volumeBar.setLocation(600, 500);
        volumeBar.setSize(100, 25);
        volumeBar.setBackground(new Color(0, 0, 100));
        volumeBar.setPaintTicks(true);
        volumeBar.setPaintLabels(true);
        volumeBar.setMinorTickSpacing(1);
        volumeBar.setMajorTickSpacing(4);
        FloatControl gainControl =
                (FloatControl) Audio.getMainClip().getControl(FloatControl.Type.MASTER_GAIN);


        try{
            volumeBar.addChangeListener(new ChangeListener()
            {
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    gainControl.setValue(volumeBar.getValue()-30);
                }
            });
        }
        catch(Exception e)
        {
            System.out.print("Max Volume");
        }

    }
    public void openGame()
    {
        menuPanel.setVisible(false);
        gamePanel.setBackground(Color.red);
        mainFrame.add(gamePanel);
        gameLabel.setFont(new Font("Verdana",1,30));


        clickScreenButton.setSize(1000,700);
        gamePanel.add(clickScreenButton);
        clickScreenButton.add(gameLabel);


        clickScreenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                Audio.getMainClip().close();
                try
                {
                    Audio.setMainClip(Audio.audioPlayer("Playing_Music-OMG.wav"));
                }
                catch(Exception B)
                {
                    System.out.println("ERROR: Audio");
                }
                Audio.getMainClip().loop(Clip.LOOP_CONTINUOUSLY);
//Commented this line out temporarily cuz kinda too loud
            }
        });

    }
    public void openSettings()
    {
        menuPanel.setVisible(false);
        settingsPanel.setVisible(true);

    }

    public void backToMenu()
    {
        settingsPanel.setVisible(false);
        menuPanel.setVisible(true);
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(JFrame mainFrame) {
        WindowManager.mainFrame = mainFrame;
    }

    public static JButton getClickScreenButton() {
        return clickScreenButton;
    }

    public static void setClickScreenButton(JButton clickScreenButton) {
        WindowManager.clickScreenButton = clickScreenButton;
    }


}
