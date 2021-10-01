package main.moon_lander.Controller;

import Score.Helper.ScoreManagement;
import Score.View.ScoreView;
import main.moon_lander.Canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CanvasViewController extends ScoreManagement implements ActionListener {
    private Canvas canvas;

    public CanvasViewController(Canvas canvas){
        this.canvas = canvas;

        if(canvas != null && canvas.btn_myPage != null){
            canvas.btn_myPage.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new ScoreView(canvas.gameWindow);
    }
}
