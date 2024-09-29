import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application
{
    private static String tapSoundFile;
    private static String streakSoundFile;
    private static boolean isGetting = false;
    private static boolean tapChain = false;
    private static int chainCount = 1;
    private static Clip tapClip;
    private static Clip streakClip;
    private static final int SCENE_WIDTH = 1400;
    private static final int SCENE_HEIGHT = 800;
    private static String filename;
    private boolean sceneRunning = false;
    private FloatControl playingMusicControl =
            (FloatControl) Audio.getMainClip().getControl(FloatControl.Type.MASTER_GAIN);
    private static double totalTap=0;
    private static double sphereTap=0;
    private static double accuracyScore=100;
    private DecimalFormat df = new DecimalFormat("#.#");
    private String accuracyString;
    private static boolean timerStarted=false;
    private static double mainScore=0;
    private static double finalScore = mainScore*(1+(accuracyScore/100))*10;
    private static double eachPoint=100;
    private static boolean isMuted=false;
    private static long clipTime;
    private static boolean gameMode2Running=false;
    private static boolean gameMode3Running=false;
    private static double mainScoreFinal;
    private static double accuracyScoreFinal;
    private static double sphereTapScoreFinal;
    private static PhongMaterial material = new PhongMaterial();
    private static PhongMaterial boxmaterial = new PhongMaterial();
    private double orgX = 0;
    private double orgY = 0;


    public static class RotateCamera extends Group {

        private final Camera camera;
        private final Rotate xRotate = new Rotate(-90, Rotate.X_AXIS);
        private final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        private final Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);


        public RotateCamera() {
            buildObjects();
            camera = new PerspectiveCamera(true);
            camera.setFarClip(10000);
            camera.setNearClip(1);
            camera.setTranslateY(-2000);
            camera.getTransforms().addAll(xRotate, yRotate, zRotate);
        }

        public void buildObjects() {
            ArrayList<Sphere> sphereArrayList = new ArrayList<Sphere>();
            material.setDiffuseColor(Color.RED);
            material.setSpecularColor(Color.SILVER);

            boxmaterial.setDiffuseColor(Color.BLUE);
//            boxmaterial.setSpecularColor(Color.SILVER);

//            Rectangle rectangleMenu = new Rectangle();
//            rectangleMenu.setTranslateX(-100);
//            rectangleMenu.setTranslateZ(470);
//            rectangleMenu.setHeight(700);
//            rectangleMenu.setWidth(800);


            Box score = new Box();
            score.setTranslateX(0);
            score.setTranslateZ(470);
            score.setHeight(700);
            score.setWidth(800);
            score.setMaterial(boxmaterial);

            Sphere sphere1 = new Sphere(50);
            sphere1.setMaterial(material);
            sphereArrayList.add(sphere1);

            Sphere sphere2 = new Sphere(50);
            sphere2.setMaterial(material);
            sphereArrayList.add(sphere2);

            Sphere sphere3 = new Sphere(50);
            sphere3.setMaterial(material);
            sphereArrayList.add(sphere3);

            Sphere sphere4 = new Sphere(50);
            sphere4.setMaterial(material);
            sphereArrayList.add(sphere4);

            Sphere sphere5 = new Sphere(50);
            sphere5.setMaterial(material);
            sphereArrayList.add(sphere5);

            /*MAX COORDINATES FOR FIELD OF VIEW
            Z_UP = 470, Z_DOWN = -470 ; X_LEFT = -880  ; X_RIGHT = 880 ;  Y_CLOSE = ; Y_BACK ;
            Z_UP To 340 due to score board
             */


            sphere1.setTranslateX(ranXCoor());
            sphere1.setTranslateZ(ranZCoor());

            sphere2.setTranslateX(ranXCoor());
            sphere2.setTranslateZ(ranZCoor());

            sphere3.setTranslateX(ranXCoor());
            sphere3.setTranslateZ(ranZCoor());

            sphere4.setTranslateX(ranXCoor());
            sphere4.setTranslateZ(ranZCoor());
            sphere5.setTranslateX(ranXCoor());
            sphere5.setTranslateZ(ranZCoor());


            Group axisGroup = new Group();
            axisGroup.getChildren().addAll(sphereArrayList);
            this.getChildren().add(axisGroup);

//            Group rectangleGroup = new Group();
//            rectangleGroup.getChildren().add(rectangleMenu);
//            this.getChildren().add(rectangleGroup);

            Group boxes = new Group();
            boxes.getChildren().addAll(score);
            this.getChildren().add(boxes);


            tapSoundFile = "Tap_Sound_Osu.wav";



           sphere1.setOnMouseClicked(event-> {
               sphereTap++;
               tapChain = true;
               System.out.println(chainCount);
               sphereArrayList.remove(sphere1);
               sphere1.setTranslateX(ranXCoor());
               sphere1.setTranslateZ(ranZCoor());
               sphereArrayList.add(sphere1);
               if(chainCount==10)
               {
                   eachPoint=100*1.3;
                   streakSoundFile = "Tap_Sound_RGX1.wav";
                   try
                   {
                       streakClip = Audio.audioPlayer(streakSoundFile);
                   }
                   catch(Exception e)
                   {
                       System.out.println("Error: Audio");
                   }
                   streakClip.stop();
                   streakClip.start();
               }
               else if(chainCount==20)
               {
                   eachPoint=100*1.3*1.3;
                   streakSoundFile = "Tap_Sound_RGX2.wav";
                   try
                   {
                       streakClip = Audio.audioPlayer(streakSoundFile);
                   }
                   catch(Exception e)
                   {
                       System.out.println("Error: Audio");
                   }
                   streakClip.stop();
                   streakClip.start();
               }
               else if(chainCount==30)
               {
                   eachPoint=100*1.3*1.3*1.3;
                   streakSoundFile = "Tap_Sound_RGX3.wav";
                   try
                   {
                       streakClip = Audio.audioPlayer(streakSoundFile);
                   }
                   catch(Exception e)
                   {
                       System.out.println("Error: Audio");
                   }
                   streakClip.stop();
                   streakClip.start();
               }
               else if(chainCount==40)
               {
                   eachPoint=100*1.3*1.3*1.3*1.3;
                   streakSoundFile = "Tap_Sound_RGX4.wav";
                   try
                   {
                       streakClip = Audio.audioPlayer(streakSoundFile);
                   }
                   catch(Exception e)
                   {
                       System.out.println("Error: Audio");
                   }
                   streakClip.stop();
                   streakClip.start();
               }
               else if (chainCount>=50 && chainCount%10==0)
               {
                   eachPoint=100*1.3*1.3*1.3*1.3*1.3;
                   streakSoundFile = "Tap_Sound_RGX5.wav";
                   try
                   {
                       streakClip = Audio.audioPlayer(streakSoundFile);
                   }
                   catch(Exception e)
                   {
                       System.out.println("Error: Audio");
                   }
                   streakClip.stop();
                   streakClip.start();
               }
               try
               {
                   tapClip = Audio.audioPlayer(tapSoundFile);

               }
               catch(Exception e) {
                   System.out.println("Error: Audio");
               }
               mainScore+=eachPoint;
               tapClip.stop();
               tapClip.start();
               chainCount++;

           });

            sphere2.setOnMouseClicked(event-> {
                sphereTap++;
                tapChain = true;
                System.out.println(chainCount);
                sphereArrayList.remove(sphere2);
                sphere2.setTranslateX(ranXCoor());
                sphere2.setTranslateZ(ranZCoor());
                sphereArrayList.add(sphere2);
                if(chainCount==10)
                {
                    eachPoint=100*1.3;
                    streakSoundFile = "Tap_Sound_RGX1.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==20)
                {
                    eachPoint=100*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX2.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==30)
                {
                    eachPoint=100*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX3.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==40)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX4.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if (chainCount>=50 && chainCount%10==0)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX5.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                try {
                    tapClip = Audio.audioPlayer(tapSoundFile);
                }
                catch(Exception e)
                {
                    System.out.println("Error: Audio");
                }
                mainScore+=eachPoint;
                tapClip.stop();
                tapClip.start();
                chainCount++;
            });

            sphere3.setOnMouseClicked(event-> {
                sphereTap++;
                tapChain = true;
                System.out.println(chainCount);
                sphereArrayList.remove(sphere3);
                sphere3.setTranslateX(ranXCoor());
                sphere3.setTranslateZ(ranZCoor());
                sphereArrayList.add(sphere3);
                if(chainCount==10)
                {
                    eachPoint=100*1.3;
                    streakSoundFile = "Tap_Sound_RGX1.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==20)
                {
                    eachPoint=100*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX2.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==30)
                {
                    eachPoint=100*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX3.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==40)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX4.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if (chainCount>=50 && chainCount%10==0)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX5.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                try {
                    tapClip = Audio.audioPlayer(tapSoundFile);
                }
                catch(Exception e)
                {
                    System.out.println("Error: Audio");
                }
                mainScore+=eachPoint;
                tapClip.stop();
                tapClip.start();
                chainCount++;
            });

            sphere4.setOnMouseClicked(event-> {
                sphereTap++;
                tapChain = true;
                System.out.println(chainCount);
                sphereArrayList.remove(sphere4);
                sphere4.setTranslateX(ranXCoor());
                sphere4.setTranslateZ(ranZCoor());
                sphereArrayList.add(sphere4);
                if(chainCount==10)
                {
                    eachPoint=100*1.3;
                    streakSoundFile = "Tap_Sound_RGX1.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==20)
                {
                    eachPoint=100*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX2.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==30)
                {
                    eachPoint=100*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX3.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==40)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX4.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if (chainCount>=50 && chainCount%10==0)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX5.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                try {
                    tapClip = Audio.audioPlayer(tapSoundFile);
                }
                catch(Exception e)
                {
                    System.out.println("Error: Audio");
                }
                mainScore+=eachPoint;
                tapClip.stop();
                tapClip.start();
                chainCount++;
            });

            sphere5.setOnMouseClicked(event-> {
                sphereTap++;
                tapChain = true;
                System.out.println(chainCount);
                sphereArrayList.remove(sphere5);
                sphere5.setTranslateX(ranXCoor());
                sphere5.setTranslateZ(ranZCoor());
                sphereArrayList.add(sphere5);
                if(chainCount==10)
                {
                    eachPoint=100*1.3;
                    streakSoundFile = "Tap_Sound_RGX1.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==20)
                {
                    eachPoint=100*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX2.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==30)
                {
                    eachPoint=100*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX3.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if(chainCount==40)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX4.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                else if (chainCount>=50 && chainCount%10==0)
                {
                    eachPoint=100*1.3*1.3*1.3*1.3*1.3;
                    streakSoundFile = "Tap_Sound_RGX5.wav";
                    try
                    {
                        streakClip = Audio.audioPlayer(streakSoundFile);
                    }
                    catch(Exception e)
                    {
                        System.out.println("Error: Audio");
                    }
                    streakClip.stop();
                    streakClip.start();
                }
                try {
                    tapClip = Audio.audioPlayer(tapSoundFile);
                }
                catch(Exception e)
                {
                    System.out.println("Error: Audio");
                }
                mainScore+=eachPoint;
                tapClip.stop();
                tapClip.start();
                chainCount++;
            });

//            if(chainCount<11)
//            {
//                tapSoundFile = "Tap_Sound_RGX1.wav";
//            }
//            else if(chainCount<21)
//            {
//                tapSoundFile = "Tap_Sound_RGX2.wav";
//            }
//            else if(chainCount<31)
//            {
//                tapSoundFile = "Tap_Sound_RGX3.wav";
//            }
//            else if(chainCount<41)
//            {
//                tapSoundFile = "Tap_Sound_RGX4.wav";
//            }
//            else if (chainCount<51)
//            {
//                tapSoundFile = "Tap_Sound_RGX5.wav";
//            }


        }
    }


    public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        //MAIN STUFF
        WindowManager windows = new WindowManager();
        windows.openMenu();
        launch(args);

        YoungPlayer youngPlayer1 = new YoungPlayer(5, 0.03);
        YoungPlayer youngPlayer2 = new YoungPlayer(5, 0.04);
        YoungPlayer youngPlayer3 = new YoungPlayer(5, 0.05);
        Player[] arr = {youngPlayer1, youngPlayer2, youngPlayer3};
        ArrayList<Player> noviceList = new ArrayList<Player>();
        noviceList.add(youngPlayer1);
        noviceList.add(youngPlayer2);
        noviceList.add(youngPlayer3);




    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        final Rotate xRotate = new Rotate(-90, Rotate.X_AXIS);
        final Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        final Rotate zRotate = new Rotate(0, Rotate.Z_AXIS);

        Image shapeshotImage = new Image(new FileInputStream("C:\\Users\\Dtang\\CS PROJECTS\\click game\\shape_shot_logo.png"));
        ImageView imageView = new ImageView(shapeshotImage);
        Group imageRoot = new Group(imageView);
        imageView.setFocusTraversable(true);
        imageView.setFitHeight(500);
        imageView.setFitWidth(700);
        imageView.setTranslateY(-100);

        Image sphereImage = new Image(new FileInputStream("C:\\Users\\Dtang\\CS PROJECTS\\click game\\click_sphere.png"));
        ImageView sphereView = new ImageView(sphereImage);
        Group sphereimageRoot = new Group(sphereView);
        sphereView.setFocusTraversable(true);
        sphereView.setFitHeight(450);
        sphereView.setFitWidth(600);
        sphereView.setTranslateY(-150);

        Image meme1Image = new Image(new FileInputStream("C:\\Users\\Dtang\\CS PROJECTS\\click game\\hydrameme_done.png"));
        ImageView meme1ImageView = new ImageView(meme1Image);
        Group meme1ImageRoot = new Group(meme1ImageView);
        meme1ImageView.setFocusTraversable(true);
        meme1ImageView.setFitHeight(450);
        meme1ImageView.setFitWidth(600);
        meme1ImageView.setTranslateY(120);
        meme1ImageView.setPreserveRatio(true);

        Image meme2Image = new Image(new FileInputStream("C:\\Users\\Dtang\\CS PROJECTS\\click game\\athomememe_done.png"));
        ImageView meme2ImageView = new ImageView(meme2Image);
        Group meme2ImageRoot = new Group(meme2ImageView);
        meme2ImageView.setFocusTraversable(true);
        meme2ImageView.setFitHeight(450);
        meme2ImageView.setFitWidth(600);
        meme2ImageView.setTranslateY(120);
        meme2ImageView.setPreserveRatio(true);








        //Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);



        Text mainText = new Text("HOW TO PLAY: ");
        mainText.setTranslateX(-300);
        mainText.setTranslateY(-350);
        mainText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text instructionsText = new Text("CLICK: CLICK TO SHOOT THE SHAPES TO GET POINTS");
        instructionsText.setTranslateX(0);
        instructionsText.setTranslateY(-270);
        instructionsText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        Text movementText = new Text("W/A/S/D: USE THESE KEYS TO MOVE");
        movementText.setTranslateX(0);
        movementText.setTranslateY(-220);
        movementText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        Text rotateText = new Text("ARROW KEYS: USE THESE KEYS TO ROTATE");
        rotateText.setTranslateX(0);
        rotateText.setTranslateY(-170);
        rotateText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        Text focusText = new Text("Q: USE THIS KEY TO REFOCUS");
        focusText.setTranslateX(0);
        focusText.setTranslateY(-120);
        focusText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        Text backtosettingsText = new Text("BACKSPACE: USE THIS KEY TO GO BACK TO SETTINGS");
        backtosettingsText.setTranslateX(0);
        backtosettingsText.setTranslateY(-70);
        backtosettingsText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        Text sphereGamemodeText = new Text("Sphere Gamemode");
        sphereGamemodeText.setTranslateX(10);
        sphereGamemodeText.setTranslateY(-200);
        sphereGamemodeText.setFont(Font.font("verdana", FontPosture.REGULAR, 40));

        StackPane instructionsPane = new StackPane();
        Scene instructionsScene = new Scene(instructionsPane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);
        instructionsPane.getChildren().addAll(mainText,instructionsText,movementText,rotateText,focusText,backtosettingsText);

        StackPane settingsPane = new StackPane();
        Button playButton = new Button("Play!");
        Button instructionsButton = new Button("Instructions");
        Button settingsButton = new Button("Settings");
        instructionsButton.setTranslateX(220);
        instructionsButton.setTranslateY(270);
        settingsButton.setTranslateX(0);
        settingsButton.setTranslateY(270);
        playButton.setTranslateX(-220);
        playButton.setTranslateY(270);
        playButton.setMinHeight(100);
        playButton.setMinWidth(200);
        settingsButton.setMinHeight(100);
        settingsButton.setMinWidth(200);
        instructionsButton.setMinHeight(100);
        instructionsButton.setMinWidth(200);

        StackPane gamemodePane = new StackPane();
        Button gamemode1Button = new Button("Free Play!");
        gamemode1Button.setTranslateX(0);
        gamemode1Button.setTranslateY(150);
        gamemode1Button.setMinWidth(300);
        gamemode1Button.setMinHeight(50);

        Button gamemode2Button = new Button("Frenzy: 30 Seconds");
        gamemode2Button.setTranslateX(0);
        gamemode2Button.setTranslateY(225);
        gamemode2Button.setMinWidth(300);
        gamemode2Button.setMinHeight(50);

        Button gamemode3Button = new Button("Frenzy: 60 Seconds");
        gamemode3Button.setTranslateX(0);
        gamemode3Button.setTranslateY(300);
        gamemode3Button.setMinWidth(300);
        gamemode3Button.setMinHeight(50);

        Button gamemode2again = new Button("AGAIN?");
        gamemode2again.setTranslateX(500);
        gamemode2again.setTranslateY(-350);
        gamemode2again.setMinWidth(300);
        gamemode2again.setMinHeight(50);

        Button gamemode3again = new Button("AGAIN?");
        gamemode3again.setTranslateX(500);
        gamemode3again.setTranslateY(-350);
        gamemode3again.setMinWidth(300);
        gamemode3again.setMinHeight(50);

        Button defaultSphereButton = new Button("Default Spheres");
        defaultSphereButton.setTranslateX(-450);
        defaultSphereButton.setTranslateY(-250);
        defaultSphereButton.setMinWidth(300);
        defaultSphereButton.setMinHeight(50);

        Button blueSphereButton = new Button("Blue Spheres");
        blueSphereButton.setTranslateX(-450);
        blueSphereButton.setTranslateY(-150);
        blueSphereButton.setMinWidth(300);
        blueSphereButton.setMinHeight(50);

        Button yellowSphereButton = new Button("Yellow Spheres");
        yellowSphereButton.setTranslateX(-450);
        yellowSphereButton.setTranslateY(-50);
        yellowSphereButton.setMinWidth(300);
        yellowSphereButton.setMinHeight(50);

        Button purpleSphereButton = new Button("Purple Spheres");
        purpleSphereButton.setTranslateX(-450);
        purpleSphereButton.setTranslateY(50);
        purpleSphereButton.setMinWidth(300);
        purpleSphereButton.setMinHeight(50);

        Button greenSphereButton = new Button("Green Spheres");
        greenSphereButton.setTranslateX(-450);
        greenSphereButton.setTranslateY(150);
        greenSphereButton.setMinWidth(300);
        greenSphereButton.setMinHeight(50);

        Button cyanSphereButton = new Button("Cyan Spheres");
        cyanSphereButton.setTranslateX(-450);
        cyanSphereButton.setTranslateY(250);
        cyanSphereButton.setMinWidth(300);
        cyanSphereButton.setMinHeight(50);

        Button defaultBackgroundColorbutton = new Button("Default Background");
        defaultBackgroundColorbutton.setTranslateX(270);
        defaultBackgroundColorbutton.setTranslateY(-250);
        defaultBackgroundColorbutton.setMinWidth(300);
        defaultBackgroundColorbutton.setMinHeight(50);

        Button whiteBackgroundColorbutton = new Button("White Background");
        whiteBackgroundColorbutton.setTranslateX(270);
        whiteBackgroundColorbutton.setTranslateY(-150);
        whiteBackgroundColorbutton.setMinWidth(300);
        whiteBackgroundColorbutton.setMinHeight(50);

        Button blackBackgroundColorbutton = new Button("Black Background");
        blackBackgroundColorbutton.setTranslateX(270);
        blackBackgroundColorbutton.setTranslateY(-50);
        blackBackgroundColorbutton.setMinWidth(300);
        blackBackgroundColorbutton.setMinHeight(50);

        Button skyblueBackgroundColorbutton = new Button("Sky Blue Background");
        skyblueBackgroundColorbutton.setTranslateX(270);
        skyblueBackgroundColorbutton.setTranslateY(50);
        skyblueBackgroundColorbutton.setMinWidth(300);
        skyblueBackgroundColorbutton.setMinHeight(50);

        Button niceRedBackgroundColorbutton = new Button("Nice Red Background");
        niceRedBackgroundColorbutton.setTranslateX(270);
        niceRedBackgroundColorbutton.setTranslateY(150);
        niceRedBackgroundColorbutton.setMinWidth(300);
        niceRedBackgroundColorbutton.setMinHeight(50);

        Button pinkBackgroundColorbutton = new Button("Pink Background");
        pinkBackgroundColorbutton.setTranslateX(270);
        pinkBackgroundColorbutton.setTranslateY(250);
        pinkBackgroundColorbutton.setMinWidth(300);
        pinkBackgroundColorbutton.setMinHeight(50);

        Text gamemode2ScoreText = new Text("Total Score: " + mainScore);
        gamemode2ScoreText.setTranslateX(-271);
        gamemode2ScoreText.setTranslateY(-350);
        gamemode2ScoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode2AccuracyText = new Text("Accuracy: " + accuracyScore);
        gamemode2AccuracyText.setTranslateX(-270);
        gamemode2AccuracyText.setTranslateY(-280);
        gamemode2AccuracyText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode2TimeTakenText = new Text("Time Taken: 30 Seconds");
        gamemode2TimeTakenText.setTranslateX(-95);
        gamemode2TimeTakenText.setTranslateY(-210);
        gamemode2TimeTakenText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode2NumTapsText = new Text("Number Of Hits: " +(int)sphereTapScoreFinal);
        gamemode2NumTapsText.setTranslateX(-250);
        gamemode2NumTapsText.setTranslateY(-140);
        gamemode2NumTapsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));


        Text gamemode3ScoreText = new Text("Total Score: " + mainScore);
        gamemode3ScoreText.setTranslateX(-271);
        gamemode3ScoreText.setTranslateY(-350);
        gamemode3ScoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode3AccuracyText = new Text("Accuracy: " + accuracyScore);
        gamemode3AccuracyText.setTranslateX(-270);
        gamemode3AccuracyText.setTranslateY(-280);
        gamemode3AccuracyText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode3TimeTakenText = new Text("Time Taken: 60 Seconds");
        gamemode3TimeTakenText.setTranslateX(-95);
        gamemode3TimeTakenText.setTranslateY(-210);
        gamemode3TimeTakenText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text gamemode3NumTapsText = new Text("Number Of Hits: " +(int)sphereTapScoreFinal);
        gamemode3NumTapsText.setTranslateX(-250);
        gamemode3NumTapsText.setTranslateY(-140);
        gamemode3NumTapsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 75));

        Text sphereColorsText = new Text("Sphere Colors: ");
        sphereColorsText.setTranslateX(-450);
        sphereColorsText.setTranslateY(-350);
        sphereColorsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));

        Text backgroundColorsText = new Text("Background Colors: ");
        backgroundColorsText.setTranslateX(270);
        backgroundColorsText.setTranslateY(-350);
        backgroundColorsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));


        StackPane gamemode2endingPane = new StackPane();

        Scene gamemode2endingScene = new Scene(gamemode2endingPane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);

        StackPane gamemode3endingPane = new StackPane();

        Scene gamemode3endingScene = new Scene(gamemode3endingPane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);

        gamemode2endingPane.getChildren().addAll(gamemode2ScoreText,gamemode2AccuracyText,gamemode2TimeTakenText,gamemode2again,meme1ImageRoot,meme1ImageView);
        gamemode3endingPane.getChildren().addAll(gamemode3ScoreText,gamemode3AccuracyText,gamemode3TimeTakenText,gamemode3again,meme2ImageRoot,meme2ImageView);

        StackPane settingContentsPane = new StackPane();

        Scene settingsContentsScene = new Scene(settingContentsPane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);

        settingContentsPane.getChildren().addAll(sphereColorsText,backgroundColorsText,blueSphereButton,yellowSphereButton,defaultSphereButton,purpleSphereButton,greenSphereButton,cyanSphereButton,defaultBackgroundColorbutton,blackBackgroundColorbutton,whiteBackgroundColorbutton,skyblueBackgroundColorbutton,niceRedBackgroundColorbutton,pinkBackgroundColorbutton);






        Scene settingScene = new Scene(settingsPane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);
        settingsPane.getChildren().addAll(instructionsButton,settingsButton,imageRoot,imageView,playButton);

        Scene gamemodesScene = new Scene(gamemodePane,SCENE_WIDTH,SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);
        gamemodePane.getChildren().addAll(gamemode1Button,sphereimageRoot,sphereView,sphereGamemodeText,gamemode3Button,gamemode2Button);



        RotateCamera rotateCamera = new RotateCamera();

        Group root= new Group();
        Group game1Group = new Group();
        game1Group.getChildren().add(rotateCamera);
        root.getChildren().add(game1Group);


        Text scoreText = new Text("Score:");
        scoreText.setTranslateZ(440);
        scoreText.setTranslateX(-290);
        scoreText.setTranslateY(-250);
        scoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));

        Text timerText = new Text("Timer:");
        timerText.setTranslateZ(440);
        timerText.setTranslateX(-50);
        timerText.setTranslateY(-250);
        timerText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));

        Text percentageText = new Text("Acc %:");
        percentageText.setTranslateZ(440);
        percentageText.setTranslateX(200);
        percentageText.setTranslateY(-250);
        percentageText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 25));

        Text timerContentsText = new Text(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
        timerContentsText.setTranslateZ(380);
        timerContentsText.setTranslateX(-120);
        timerContentsText.setTranslateY(-250);
        timerContentsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));

        Text accContentsText = new Text(((int)accuracyScore) + "%");
        accContentsText.setTranslateZ(380);
        accContentsText.setTranslateX(160);
        accContentsText.setTranslateY(-250);
        accContentsText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));

        Text mainScoreText = new Text(((int)mainScore) + "");
        mainScoreText.setTranslateZ(380);
        mainScoreText.setTranslateX(-310);
        mainScoreText.setTranslateY(-250);
        mainScoreText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));





        root.getChildren().addAll(scoreText,timerText,percentageText,timerContentsText,accContentsText,mainScoreText);

        accContentsText.setFill(Color.WHITE);
        scoreText.setFill(Color.WHITE);
        timerText.setFill(Color.WHITE);
        percentageText.setFill(Color.WHITE);
        timerContentsText.setFill(Color.WHITE);
        mainScoreText.setFill(Color.WHITE);

        scoreText.getTransforms().addAll(xRotate, yRotate, zRotate);
        timerText.getTransforms().addAll(xRotate,yRotate,zRotate);
        percentageText.getTransforms().addAll(xRotate,yRotate,zRotate);
        timerContentsText.getTransforms().addAll(xRotate,yRotate,zRotate);
        accContentsText.getTransforms().addAll(xRotate,yRotate,zRotate);
        mainScoreText.getTransforms().addAll(xRotate,yRotate,zRotate);



        Scene scene = new Scene(root,SCENE_WIDTH, SCENE_HEIGHT,true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.LIGHTGRAY);
        primaryStage.setFullScreen(false);
        scene.setCamera(rotateCamera.camera);
        primaryStage.setScene(settingScene);
        scene.getCursor();


        gamemode2again.setOnMouseClicked(mouseEvent ->
        {
            gameMode2Running=true;
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
            gameMode3Running=false;
            Time.reset();
            tapChain=false;
            mainScore=0;
            accuracyScore=100;
            sphereTap=0;
            totalTap=0;
            eachPoint=100;
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            mainScoreText.setText(mainScore+"");
            accContentsText.setText(accuracyScore+"%");
            scene.setCursor(Cursor.CROSSHAIR);
        });

        gamemode3again.setOnMouseClicked(mouseEvent ->
        {
            gameMode2Running=false;
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
            gameMode3Running=true;
            Time.reset();
            tapChain=false;
            mainScore=0;
            accuracyScore=100;
            sphereTap=0;
            totalTap=0;
            eachPoint=100;
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            mainScoreText.setText(mainScore+"");
            accContentsText.setText(accuracyScore+"%");
            scene.setCursor(Cursor.CROSSHAIR);
        });

        playButton.setOnMouseClicked(mouseEvent ->
        {
            primaryStage.setScene(gamemodesScene);
            primaryStage.setFullScreen(false);
            scene.setCursor(Cursor.CROSSHAIR);
//            thread.run();
        });



        gamemode1Button.setOnMouseClicked(mouseEvent ->
        {
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            scene.setCursor(Cursor.CROSSHAIR);

        });

        gamemode2Button.setOnMouseClicked(mouseEvent ->
        {
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            gameMode2Running=true;
            scene.setCursor(Cursor.CROSSHAIR);

        });

        gamemode3Button.setOnMouseClicked(mouseEvent ->
        {
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);
            gameMode3Running=true;
        });

        instructionsButton.setOnMouseClicked(mouseEvent ->
        {
            primaryStage.setScene(instructionsScene);
            primaryStage.setFullScreen(false);
        });



        //sets cursor to a crosshair
        scene.setCursor(Cursor.CROSSHAIR);

        scene.setOnMouseMoved(event-> {
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            if(gameMode2Running&&Time.getSeconds_string().equals("30"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode2endingScene);
                primaryStage.setFullScreen(false);
                gamemode2ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode2AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
                //mainScore*(1+(accuracyScore/100))*10;//
            }
            if(gameMode3Running&&Time.getMinutes_string().equals("01"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode3endingScene);
                primaryStage.setFullScreen(false);
                gamemode3ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode3AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
            }

        });

        scene.setOnMouseClicked(event-> {
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            if(gameMode2Running&&Time.getSeconds_string().equals("30"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode2endingScene);
                primaryStage.setFullScreen(false);
                gamemode2ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode2AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
                //mainScore*(1+(accuracyScore/100))*10;//
            }
            if(gameMode3Running&&Time.getMinutes_string().equals("01"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode3endingScene);
                primaryStage.setFullScreen(false);
                gamemode3ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode3AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
            }

        });

        scene.setOnMouseReleased(event-> {
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            if(gameMode2Running&&Time.getSeconds_string().equals("30"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode2endingScene);
                primaryStage.setFullScreen(false);
                gamemode2ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode2AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
                //mainScore*(1+(accuracyScore/100))*10;//
            }
            if(gameMode3Running&&Time.getMinutes_string().equals("01"))
            {
                scene.setCursor(Cursor.CROSSHAIR);
                primaryStage.setScene(gamemode3endingScene);
                primaryStage.setFullScreen(false);
                gamemode3ScoreText.setText("Total Score: "+(int)(mainScoreFinal*(1+(accuracyScoreFinal/100)))+"K");
                gamemode3AccuracyText.setText("Accuracy: "+(int)(accuracyScoreFinal)+"%");
            }

        });


        scene.setOnMouseClicked(event-> {
            Time.start();
            Time.setStarted(true);
            totalTap++;
            timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
            accuracyString=df.format((((sphereTap/totalTap)*100)/10)*10);
            accuracyScore = Double.parseDouble(accuracyString);
            accuracyScoreFinal = accuracyScore;
            accContentsText.setText(((int)accuracyScore)+"%");
            mainScoreFinal = mainScore;
            mainScoreText.setText(((int)mainScore)+"");
            System.out.println(totalTap);
            scene.setCursor(Cursor.CROSSHAIR);
            if(!tapChain)
            {
                eachPoint=100;
                eachPoint=100;
                chainCount=1;
            }
            tapChain=false;
        });

        scene.setOnMouseEntered(event -> {
            orgX = event.getSceneX();
            orgY = event.getSceneX();
        });
//        scene.setOnMouseMoved(event -> {
//            double changeInX = event.getSceneX() - orgX;
//            double changeInY = event.getSceneY() - orgY;
//            if (changeInX > 0) {
//                rotateCamera.yRotate.setAngle(rotateCamera.yRotate.getAngle() - 0.5);
//            }
//            else if (changeInX < 0) {
//                rotateCamera.yRotate.setAngle(rotateCamera.yRotate.getAngle() + 0.5);
//            }
//            if (changeInY > 0) {
//                rotateCamera.xRotate.setAngle(rotateCamera.xRotate.getAngle() - 0.5);
//            }
//            else if (changeInY < 0) {
//                rotateCamera.xRotate.setAngle(rotateCamera.xRotate.getAngle() + 0.5);
//            }
//
//            orgX = event.getSceneX();
//            orgY = event.getSceneY();
//
//        });

        /*root.setOnMouseEntered((event) -> {
            orgX = event.getSceneX();
            orgY = event.getSceneY();*/

        /*root.setOnMouseMoved((event) -> {
            double changeInX = event.getSceneX() - orgX;
            double changeInY = event.getSceneY() - orgY;
            //System.out.println(orgX / orgY);

            if (changeInX > 0) {
                System.out.println("moving right");
            }
            else if (changeInX < 0) {
                System.out.println("moving left");
            }
            if (changeInY > 0) {
                System.out.println("moving down");
            }
            else if (changeInY < 0) {
                System.out.println("moving up");
            }

            orgX = event.getSceneX();
            orgY = event.getSceneY();
        });*/


        scene.setOnKeyPressed((KeyEvent e) -> {
            System.out.println(rotateCamera.getTranslateY());
            KeyCode code = e.getCode();
            switch (code) {
                case W:
                    rotateCamera.translateZProperty().set(rotateCamera.getTranslateZ() + 10);
                    break;
                case S:
                    rotateCamera.translateZProperty().set(rotateCamera.getTranslateZ() - 10);
                    break;
                case A:
                    rotateCamera.translateXProperty().set(rotateCamera.getTranslateX() + 10);
                    break;
                case D:
                    rotateCamera.translateXProperty().set(rotateCamera.getTranslateX() - 10);
                    break;
                case LEFT:
                    rotateCamera.yRotate.setAngle(rotateCamera.yRotate.getAngle() + 10);
                    break;
                case RIGHT:
                    rotateCamera.yRotate.setAngle(rotateCamera.yRotate.getAngle() - 10);
                    break;
                case UP:
                    rotateCamera.xRotate.setAngle(rotateCamera.xRotate.getAngle() + 10);
                    break;
                case DOWN:
                    rotateCamera.xRotate.setAngle(rotateCamera.xRotate.getAngle() - 10);
                    break;
                case Q:
                    rotateCamera.xRotate.setAngle(-90);
                    rotateCamera.yRotate.setAngle(0);
                    rotateCamera.zRotate.setAngle(0);
                    rotateCamera.setTranslateX(0);
                    rotateCamera.setTranslateY(0);
                    rotateCamera.setTranslateZ(0);
                    break;
                case M:
                    if((!Audio.getMainClip().isOpen()))
                    {
                        try
                        {
                            Audio.setMainClip(Audio.audioPlayer("Playing_Music-OMG.wav"));
                            Audio.getMainClip().setMicrosecondPosition(clipTime);
                            Audio.getMainClip().start();
                            Audio.getMainClip().loop(Integer.MAX_VALUE);
                            System.out.println("hi");
                        }
                        catch(Exception B)
                        {
                            System.out.println("ERROR: Audio");
                        }
                    }
                    clipTime=Audio.getMainClip().getMicrosecondPosition();
                    Audio.getMainClip().stop();
                    Audio.getMainClip().close();
                    break;
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    gameMode3Running=false;
                    gameMode2Running=false;
                    Time.reset();
                    tapChain=false;
                    mainScore=0;
                    accuracyScore=100;
                    sphereTap=0;
                    totalTap=0;
                    eachPoint=100;
                    chainCount=0;
                    timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
                    mainScoreText.setText(mainScore+"");
                    accContentsText.setText(accuracyScore+"%");
                    scene.setCursor(Cursor.CROSSHAIR);
                    break;
                case B:
                    primaryStage.setScene(gamemode2endingScene);
                    primaryStage.setFullScreen(false);
                    scene.setCursor(Cursor.CROSSHAIR);
                    break;
                case C:
                    primaryStage.setScene(gamemode3endingScene);
                    primaryStage.setFullScreen(false);
                    scene.setCursor(Cursor.CROSSHAIR);
                    break;
                default:
                    break;
            }
        });

        instructionsScene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    scene.setCursor(Cursor.CROSSHAIR);
                    break;
                default:
                    break;
            }
        });

        gamemodesScene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    gameMode3Running=false;
                    gameMode2Running=false;
                    Time.reset();
                    tapChain=false;
                    mainScore=0;
                    accuracyScore=100;
                    sphereTap=0;
                    totalTap=0;
                    eachPoint=100;
                    chainCount=0;
                    timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
                    mainScoreText.setText(mainScore+"");
                    accContentsText.setText(accuracyScore+"%");
                    scene.setCursor(Cursor.CROSSHAIR);
                    break;
                default:
                    break;
            }
        });


        gamemode2endingScene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    gameMode3Running=false;
                    gameMode2Running=false;
                    Time.reset();
                    tapChain=false;
                    mainScore=0;
                    accuracyScore=100;
                    sphereTap=0;
                    totalTap=0;
                    eachPoint=100;
                    chainCount=0;
                    timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
                    mainScoreText.setText(mainScore+"");
                    accContentsText.setText(accuracyScore+"%");
                    scene.setCursor(Cursor.CROSSHAIR);

                    break;
                default:
                    break;
            }
        });

        gamemode3endingScene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    gameMode3Running=false;
                    gameMode2Running=false;
                    Time.reset();
                    tapChain=false;
                    mainScore=0;
                    accuracyScore=100;
                    sphereTap=0;
                    totalTap=0;
                    eachPoint=100;
                    chainCount=0;
                    timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
                    mainScoreText.setText(mainScore+"");
                    accContentsText.setText(accuracyScore+"%");
                    scene.setCursor(Cursor.CROSSHAIR);

                    break;
                default:
                    break;
            }
        });

        settingsButton.setOnMouseClicked(event-> {
            primaryStage.setScene(settingsContentsScene);
            primaryStage.setFullScreen(false);

        });

        settingsContentsScene.setOnKeyPressed((KeyEvent e) -> {
            KeyCode code = e.getCode();
            switch (code) {
                case BACK_SPACE:
                    sceneRunning=false;
//                    WindowManager.mainFrame.setVisible(true);
//                    WindowManager.gamePanel.setVisible(true);
                    primaryStage.setScene(settingScene);
                    gameMode3Running=false;
                    gameMode2Running=false;
                    Time.reset();
                    tapChain=false;
                    mainScore=0;
                    accuracyScore=100;
                    sphereTap=0;
                    totalTap=0;
                    eachPoint=100;
                    chainCount=0;
                    timerContentsText.setText(Time.getHours_string()+":"+Time.getMinutes_string()+":"+Time.getSeconds_string());
                    mainScoreText.setText(mainScore+"");
                    accContentsText.setText(accuracyScore+"%");
                    scene.setCursor(Cursor.CROSSHAIR);

                    break;
                default:
                    break;
            }
        });


        blueSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.BLUE);
            scene.setCursor(Cursor.CROSSHAIR);

        });
        yellowSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.GOLD);
            scene.setCursor(Cursor.CROSSHAIR);

        });
        defaultSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.RED);
            scene.setCursor(Cursor.CROSSHAIR);

        });


        purpleSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.PURPLE);
            scene.setCursor(Cursor.CROSSHAIR);

        });

        greenSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.GREEN);
            scene.setCursor(Cursor.CROSSHAIR);

        });

        cyanSphereButton.setOnMouseClicked(event-> {
            material.setDiffuseColor(Color.CYAN);
            scene.setCursor(Cursor.CROSSHAIR);

        });

        defaultBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.LIGHTGRAY);
            scene.setCursor(Cursor.CROSSHAIR);
        });

        whiteBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.WHITE);
            scene.setCursor(Cursor.CROSSHAIR);
        });

        blackBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.BLACK);
            scene.setCursor(Cursor.CROSSHAIR);
        });

        skyblueBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.SKYBLUE);
            scene.setCursor(Cursor.CROSSHAIR);
        });

        niceRedBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.CRIMSON);
            scene.setCursor(Cursor.CROSSHAIR);
        });

        pinkBackgroundColorbutton.setOnMouseClicked(event-> {
            scene.setFill(Color.PINK);
            scene.setCursor(Cursor.CROSSHAIR);
        });




        //(I SPENT 2 HOURS ON THIS) WITH PRESS OF BUTTON SHOWS JAVAFX STAGE

        WindowManager.getClickScreenButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        primaryStage.show();
                        sceneRunning=true;
                    }
                });
            }
        });
    }

    //(int)(Math.random() *(max - min + 1) + min)
    public static int ranXCoor()
    {
        return (int)(Math.random()*(880+880+1)-880);
    }
    public static int ranZCoor()
    {
        return (int)(Math.random()*(340+470+1)-470);
    }


}
