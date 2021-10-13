package UserManagement.View;

import UserManagement.Controller.SignInViewController;
import Frameworks.Models.placePanel;

import javax.swing.*;
import java.awt.*;

public class SignInView extends JFrame implements placePanel {
    public JButton btn_signIn, btn_signUp;
    public JTextField field_email;
    public JPasswordField field_password;
    public JPanel signInPanel;
    public JCheckBox checkBox_autoSignIn;

    public SignInView() {
        setTitle("로그인");
        setPreferredSize(new Dimension(400, 200));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        signInPanel = new JPanel();
        placePanel(signInPanel);

        add(signInPanel);

        pack();
        setVisible(true);

        new SignInViewController(this);
    }

    public void placePanel(JPanel signInPanel){
        signInPanel.setLayout(new BoxLayout(signInPanel, BoxLayout.Y_AXIS));
        signInPanel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel title = new JLabel("Moon Lander");
        signInPanel.add(title);

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

        JPanel panel_autoSignIn = new JPanel();
        panel_autoSignIn.setLayout(new FlowLayout());

        checkBox_autoSignIn = new JCheckBox("자동 로그인");

        panel_autoSignIn.add(checkBox_autoSignIn);

        JPanel panel_userInteraction = new JPanel();
        panel_userInteraction.setLayout(new FlowLayout());

        btn_signIn = new JButton("로그인");

        btn_signUp = new JButton("회원가입");

        panel_userInteraction.add(btn_signIn);
        panel_userInteraction.add(btn_signUp);

        signInPanel.add(panel_email);
        signInPanel.add(panel_password);
        signInPanel.add(panel_autoSignIn);
        signInPanel.add(panel_userInteraction);
    }
}
