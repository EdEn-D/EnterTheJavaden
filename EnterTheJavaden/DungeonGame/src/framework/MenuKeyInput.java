// Keyboard input handling in the menu

package framework;


import window.Handler;
import window.MainGameMenu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MenuKeyInput extends KeyAdapter {
    MainGameMenu m;

    public MenuKeyInput(MainGameMenu m){
        this.m = m;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_ENTER)
                m.setMenuSelection(0);
        if (key == KeyEvent.VK_1)
                m.setMenuSelection(1);
        if (key == KeyEvent.VK_2)
                m.setMenuSelection(2);
        if (key == KeyEvent.VK_3)
                m.setMenuSelection(3);

    }
}
