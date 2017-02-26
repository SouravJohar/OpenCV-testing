import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javax.management.remote.JMXConnectorFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aakash on 1/14/2017.
 */
public class MainFrame {

    static VideoCapture capture;
    static JLabel videoDisplay, binary;
    JFrame mainFrame;
    JButton startVideo, stopVideo, histoCapture;
    JPanel buttonHolder;

    MainFrame() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        mainFrame = new JFrame("Compter Vision");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        videoDisplay = new JLabel();
        buttonHolder = new JPanel();
        binary=new JLabel();
        startVideo = new JButton("Start Video");
        stopVideo = new JButton("Stop Video");
        histoCapture = new JButton("Capture Histogram");
        buttonHolder.add(startVideo);
        buttonHolder.add(histoCapture);
        buttonHolder.add(stopVideo);
        mainFrame.add(videoDisplay, BorderLayout.WEST);
        mainFrame.add(binary, BorderLayout.EAST);
        mainFrame.add(buttonHolder, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
        mainFrame.setSize(1316, 560);
        mainFrame.setLocationRelativeTo(null);




        startVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VideoCapturer vc = new VideoCapturer();
                videoDisplay.setVisible(true);
                binary.setVisible(true);
                vc.execute();
            }
        });


        stopVideo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VideoCapturer.clicked = false;
                File file = new File("check.txt");                
                try(FileWriter writer = new FileWriter(file.getAbsolutePath(), true)){
                	String a = "blahblah";                	
                	writer.write(a);                	               	
                } catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                System.out.println(file.getAbsolutePath());
                VideoCapturer.webCamImage=null;
                VideoCapturer.toShow = null;
                VideoCapturer.toShowContour = null;
                videoDisplay.setVisible(false);
                binary.setVisible(false);
                capture.release();
                try {
					TimeUnit.SECONDS.sleep(1);
					file.delete();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                //file.delete();
            }
        });

        histoCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VideoCapturer.clicked=true;
                ImageProcessor a = new ImageProcessor();
                Mat histogram = a.captureHistogram(VideoCapturer.webCamImage);
                VideoCapturer.histogram = histogram;
                binary.setVisible(true);
                File bri = new File("BrightnessScript.ps1");
                File vol = new File("VolumeScript.ps1");
                try {
					Runtime.getRuntime().exec("cmd /c powershell -noexit \"& \"\""+bri.getAbsolutePath() +"\"\"\"");
					Runtime.getRuntime().exec("cmd /c powershell -noexit \"& \"\""+vol.getAbsolutePath() +"\"\"\"");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
        });

    }
}