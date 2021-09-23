package UserManagement.Controller;

import UserManagement.Helper.UserManagement;
import UserManagement.View.SignInView;
import UserManagement.View.SignUpView;
import main.moon_lander.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInViewController implements ActionListener {
    private SignInView signInView;
    private String email, password;
    private UserManagement userManagement = new UserManagement();

    public SignInViewController(SignInView signInView) {
        this.signInView = signInView;

        if (signInView != null){
            signInView.btn_signIn.addActionListener(this);
            signInView.btn_signUp.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton requestBtn = (JButton) e.getSource();

        if(requestBtn.getText().equals("로그인")){
            this.email = signInView.field_email.getText();
            this.password = String.valueOf(signInView.field_password.getPassword());
            if (email.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(null, "모든 요구사항을 충족시켜주십시오.", "공백 필드", JOptionPane.WARNING_MESSAGE);
            }

            else{
                signInView.btn_signIn.setEnabled(false);

                signInView.signInPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    int result = userManagement.signIn(email, password);

                    if(result != 200){
                        signInView.btn_signIn.setEnabled(true);
                        signInView.signInPanel.setCursor(Cursor.getDefaultCursor());
                        JOptionPane.showMessageDialog(null, "입력한 정보와 일치하는 계정이 없습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
                    }

                    else{
                        signInView.signInPanel.setCursor(Cursor.getDefaultCursor());
                        signInView.setVisible(false);

                        new Window();
                    }
                } catch (Exception ex) {
                    signInView.btn_signIn.setEnabled(true);
                    signInView.signInPanel.setCursor(Cursor.getDefaultCursor());

                    ex.printStackTrace();
                }
            }
        }

        else{
            new SignUpView();
        }

    }
}
