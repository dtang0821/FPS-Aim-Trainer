
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Audio
{
    private static boolean audioRunning=false;
    private static String filename;
    private static Clip mainClip;
    private static boolean songChanged;

    public static void playAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        String[]LobbyMusic = {"Music-Backdoor.wav","Music-Thunderous.wav","Music-Menu.wav"};
        int rannum = ranmax3();
        filename=LobbyMusic[rannum];
        mainClip = audioPlayer(filename);
        mainClip.start();
        audioRunning=true;
        if(mainClip.getMicrosecondPosition() == mainClip.getMicrosecondLength())
        {
            if(rannum == 2)
            {
                rannum=0;
                mainClip.stop();
                mainClip = audioPlayer(LobbyMusic[rannum]);
                mainClip.start();
            }
            rannum++;
            mainClip.stop();
            mainClip = audioPlayer(LobbyMusic[rannum]);
            mainClip.start();
        }
        if(songChanged)
        {
            mainClip = audioPlayer("Playing_Music-OMG");
        }
    }

    public static Clip audioPlayer(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        File file = new File(filename);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    public static ImageIcon imageSet(String filename) throws java.io.IOException
    {
        BufferedImage image = ImageIO.read(new File(filename));
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }

    public static void changeSong(String filename) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        mainClip = audioPlayer(filename);
    }
    public static int ranmax3()
    {
        return (int)(Math.random()*(2+1));
    }

    public static Clip getMainClip() {
        return mainClip;
    }

    public static void setMainClip(Clip mainClip) {
        Audio.mainClip = mainClip;
    }
}