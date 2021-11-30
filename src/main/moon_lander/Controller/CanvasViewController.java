package main.moon_lander.Controller;

import Score.Helper.ScoreManagement;
import Score.View.ScoreView;
import main.moon_lander.Canvas;
import main.moon_lander.Settings.View.SettingsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CanvasViewController extends ScoreManagement implements ActionListener {
    private Canvas canvas;

    public CanvasViewController(Canvas canvas){
        this.canvas = canvas;

        if(canvas != null && canvas.btn_myPage != null && canvas.btn_settings != null){
            canvas.btn_myPage.addActionListener(this);
            canvas.btn_settings.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton requestBtn = (JButton) e.getSource();
        String btnName = requestBtn.getName();

        switch(btnName){
            case "btn_settings":
                new SettingsView();

                break;

            case "btn_myPage":
                new ScoreView(canvas.gameWindow);

            default:
                break;
        }
    }
}
