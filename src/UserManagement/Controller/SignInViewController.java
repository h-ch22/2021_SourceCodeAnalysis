package UserManagement.Controller;

import UserManagement.Helper.UserManagement;
import UserManagement.View.SignInView;
import UserManagement.View.SignUpView;
import main.moon_lander.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignInViewController extends UserManagement implements ActionListener {
    private final SignInView signInView;
    private String email, password;

    public SignInViewController(SignInView signInView) {
        this.signInView = signInView;

        if (signInView != null){
            signInView.btn_signIn.addActionListener(this);
            signInView.btn_signUp.addActionListener(this);
        }

        if(!signInPrefs.get("email", "").equals("") && !signInPrefs.get("password", "").equals("")){
            try{
                email = decrypt(signInPrefs.get("email", ""));
                password = decrypt(signInPrefs.get("password", ""));

                doSignIn();
            }  catch(Exception e){
                e.printStackTrace();

                JOptionPane.showMessageDialog(null, "정보를 복호화하는 중 오류가 발생했습니다.", "복호화 오류", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void doSignIn(){
        try {
            int result = signIn(email, password);

            if(result != 200){
                signInView.btn_signIn.setEnabled(true);
                signInView.signInPanel.setCursor(Cursor.getDefaultCursor());
                JOptionPane.showMessageDialog(null, "입력한 정보와 일치하는 계정이 없습니다.", "로그인 오류", JOptionPane.WARNING_MESSAGE);
            }

            else{
                if (signInView.checkBox_autoSignIn.isSelected()){
                    registerAutoSignIn(encrypt(email), encrypt(password));
                }

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

                doSignIn();
            }
        }

        else{
            new SignUpView();
        }

    }
}
