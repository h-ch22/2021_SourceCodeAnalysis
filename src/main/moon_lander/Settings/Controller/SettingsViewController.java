package main.moon_lander.Settings.Controller;

import UserManagement.Helper.UserManagement;
import main.moon_lander.Settings.View.SettingsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsViewController extends UserManagement implements ActionListener {
    private SettingsView view;

    public SettingsViewController(SettingsView view){
        this.view = view;

        if(view != null){
            view.btn_cancel.addActionListener(this);
            view.btn_save.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton request = (JButton) e.getSource();
        String btnName = request.getName();

        switch(btnName){
            case "btn_save":
                if(!view.useAutoSignIn.isSelected()){
                    removeAutoSignIn();
                }

                if(view.playSound.isSelected()){
                    settingsPrefs.put("playSound", "true");
                }

                else{
                    settingsPrefs.put("playSound", "false");
                }

                if(view.useStoryMode.isSelected()){
                    settingsPrefs.put("useStoryMode", "true");
                }

                else{
                    settingsPrefs.put("useStoryMode", "false");
                }

                view.dispose();

            case "btn_cancel":
                view.dispose();
        }
    }
}
