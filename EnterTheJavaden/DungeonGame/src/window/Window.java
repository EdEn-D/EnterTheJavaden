// This class is used to create the game window

package window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window {
    private boolean winClosed;
    public JFrame frame;
    public static boolean closeCheck = false;

    public Window(int w, int h, String title, Object game ){
//        JFrame frame = new JFrame(title);
        this.frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(w,h));
        frame.setMaximumSize(new Dimension(w,h));
        frame.setMinimumSize(new Dimension(w,h));

        this.winClosed = false;



        frame.add((Component) game);
        frame.pack();
        // Setting up some frame configs
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE  );
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        // call terminate
                        exitProcedure();
                    }
                }
        );
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

//        if (MainGameMenu.finished)
//        {
//            frame.setVisible(false); //you can't see me!
//            frame.dispose(); //Destroy the JFrame object
//        }
        //game.start();
    }

    public boolean isWinClosed() {
        return winClosed;
    }

    // Runs when users closes window
    private void exitProcedure() {
        closeCheck = true;
        winClosed = true;
        frame.dispose();
//        System.exit(0);
    }
}
