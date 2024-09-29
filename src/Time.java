import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

public class Time implements ActionListener{

    private static int elapsedTime = 0;
    private static int seconds =0;
    private static int minutes =0;
    private static int hours =0;
    private static boolean started = false;
    private static String seconds_string = String.format("%02d", seconds);
    private static String minutes_string = String.format("%02d", minutes);
    private static String hours_string = String.format("%02d", hours);

    private static Timer timer = new Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent e) {

            elapsedTime=elapsedTime+1000;
            hours = (elapsedTime/3600000);
            minutes = (elapsedTime/60000) % 60;
            seconds = (elapsedTime/1000) % 60;
            seconds_string = String.format("%02d", seconds);
            minutes_string = String.format("%02d", minutes);
            hours_string = String.format("%02d", hours);
        }

    });


    public static void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public static void reset() {
        timer.stop();
        elapsedTime=0;
        seconds =0;
        minutes=0;
        hours=0;
        seconds_string = String.format("%02d", seconds);
        minutes_string = String.format("%02d", minutes);
        hours_string = String.format("%02d", hours);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
    }

    public static String getSeconds_string() {
        return seconds_string;
    }

    public static String getMinutes_string() {
        return minutes_string;
    }

    public static String getHours_string() {
        return hours_string;
    }

    public static boolean isStarted() {
        return started;
    }

    public static void setStarted(boolean started) {
        Time.started = started;
    }
}