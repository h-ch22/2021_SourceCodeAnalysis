package main.moon_lander.Settings.View;

import Frameworks.Models.placePanel;
import UserManagement.Helper.UserManagement;
import main.moon_lander.Settings.Controller.SettingsViewController;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JFrame implements placePanel {
    public JPanel settingsPanel;
    public JButton btn_save, btn_cancel;
    public JCheckBox playSound, useAutoSignIn, useStoryMode;

    private final UserManagement settings = new UserManagement();
    private final Dimension dimension = new Dimension(200, 170);

    public SettingsView(){
        settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setTitle("설정");
        setPreferredSize(dimension);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        placePanel(settingsPanel);

        new SettingsViewController(this);

        pack();
        add(settingsPanel);
        setVisible(true);
    }

    @Override
    public void placePanel(JPanel panel) {
        btn_save = new JButton("저장");
        btn_cancel = new JButton("취소");

        btn_save.setName("btn_save");
        btn_cancel.setName("btn_cancel");

        String soundSet = settings.getUserPrefs().get("playSound", "true");
        String storyModeSet = settings.getUserPrefs().get("useStoryMode", "true");

        if(soundSet.equals("true")){
            playSound = new JCheckBox("소리 재생", true);

        }

        else{
            playSound = new JCheckBox("소리 재생", false);
        }

        if(storyModeSet.equals("true")){
            useStoryMode = new JCheckBox("스토리 모드 사용", true);
        }

        else{
            useStoryMode = new JCheckBox("스토리 모드 사용", false);
        }

        String autoSignInSet = settings.getAutoSignInPrefs().get("email", "");

        if(autoSignInSet.equals("")){
            useAutoSignIn = new JCheckBox("자동 로그인", false);
            useAutoSignIn.disable();
        }

        else{
            useAutoSignIn = new JCheckBox("자동 로그인", true);
        }

        settingsPanel.add(playSound);
        settingsPanel.add(useAutoSignIn);
        settingsPanel.add(useStoryMode);
        settingsPanel.add(btn_save);
        settingsPanel.add(btn_cancel);
    }
}
