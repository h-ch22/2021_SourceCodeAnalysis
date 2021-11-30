package main.moon_lander.Home.Controller;

import main.moon_lander.Framework;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeViewController implements ActionListener {
    private final Framework framework;
    private int stage = 0;

    public HomeViewController(Framework framework){
        this.framework = framework;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton request = (JButton) e.getSource();
        String btnName = request.getName();

        switch (btnName) {
            case "btn_stage_0" -> stage = 0;
            case "btn_stage_1" -> stage = 1;
            case "btn_stage_2" -> stage = 2;
            case "btn_stage_3" -> stage = 3;
            case "btn_stage_4" -> stage = 4;
        }

        if(btnName.contains("stage")){
            framework.startGAME(stage);
        }
    }
}
