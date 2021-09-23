package UserManagement.View;

import UserManagement.Controller.SignUpViewController;

import javax.swing.*;
import java.awt.*;

public class SignUpView extends JFrame {
    public JButton btn_signUp, btn_cancel;
    public JTextField field_email, field_nickName;
    public JPasswordField field_password, field_checkPassword;
    public JPanel signUpPanel;

    public SignUpView(){
        setTitle("회원가입");
        setResizable(false);
        setLocationRelativeTo(null);

        signUpPanel = new JPanel();
        placePanel(signUpPanel);

        add(signUpPanel);
        pack();

        setVisible(true);

        new SignUpViewController(this);
    }

    private void placePanel(JPanel signUpPanel){
        signUpPanel.setLayout(new BoxLayout(signUpPanel, BoxLayout.Y_AXIS));
        signUpPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("회원가입");
        signUpPanel.add(title);

        JPanel panel_email = new JPanel();
        field_email = new JTextField();
        field_email.setPreferredSize(new Dimension(200, 40));

        panel_email.setLayout(new FlowLayout());
        panel_email.add(new JLabel("E-Mail"));
        panel_email.add(field_email);

        JPanel panel_password = new JPanel();
        field_password = new JPasswordField();
        field_password.setPreferredSize(new Dimension(200, 40));
        panel_password.setLayout(new FlowLayout());
        panel_password.add(new JLabel("비밀번호"));
        panel_password.add(field_password);

        JPanel panel_checkPW = new JPanel();
        field_checkPassword = new JPasswordField();
        field_checkPassword.setPreferredSize(new Dimension(200, 40));
        panel_checkPW.setLayout(new FlowLayout());
        panel_checkPW.add(new JLabel("한번 더"));
        panel_checkPW.add(field_checkPassword);

        JPanel panel_nickName = new JPanel();
        field_nickName = new JTextField();
        field_nickName.setPreferredSize(new Dimension(200, 40));

        panel_nickName.setLayout(new FlowLayout());
        panel_nickName.add(new JLabel("닉네임"));
        panel_nickName.add(field_nickName);

        JPanel panel_userInteraction = new JPanel();
        panel_userInteraction.setLayout(new FlowLayout());

        btn_signUp = new JButton("회원가입");
        btn_cancel = new JButton("취소");

        panel_userInteraction.add(btn_cancel);
        panel_userInteraction.add(btn_signUp);

        signUpPanel.add(panel_email);
        signUpPanel.add(panel_password);
        signUpPanel.add(panel_checkPW);
        signUpPanel.add(panel_nickName);
        signUpPanel.add(panel_userInteraction);
    }
}
