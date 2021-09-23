package UserManagement.Controller;

import UserManagement.Helper.UserManagement;
import UserManagement.View.SignUpView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SignUpViewController implements ActionListener {
    private SignUpView view;
    private String email, password, check_password, nickName;
    private UserManagement userManagement = new UserManagement();

    public SignUpViewController(SignUpView view){
        this.view = view;

        if(view != null){
            view.btn_cancel.addActionListener(this);
            view.btn_signUp.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton request = (JButton) e.getSource();

        if(request.getText().equals("회원가입")){
            this.email = view.field_email.getText();
            this.password = String.valueOf(view.field_password.getPassword());
            this.check_password = String.valueOf(view.field_checkPassword.getPassword());
            this.nickName = view.field_nickName.getText();

            if(email.equals("") || password.equals("") || check_password.equals("") || nickName.equals("")){
                JOptionPane.showMessageDialog(null, "모든 요구사항을 충족시켜주십시오.", "공백 필드", JOptionPane.WARNING_MESSAGE);
            }

            else{
                if(!password.equals(check_password)){
                    JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다.", "비밀번호 불일치", JOptionPane.WARNING_MESSAGE);
                }

                else{
                    if(password.length() < 6){
                        JOptionPane.showMessageDialog(null, "보안을 위해 6자리 이상의 비밀번호를 입력하십시오.", "비밀번호 조건 오류", JOptionPane.WARNING_MESSAGE);
                    }

                    else{
                        try {
                            view.btn_signUp.setEnabled(false);
                            view.signUpPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                            boolean signUpResult = userManagement.signUp(email, password, nickName);

                            if(signUpResult){
                                view.signUpPanel.setCursor(Cursor.getDefaultCursor());
                                view.setVisible(false);

                                JOptionPane.showMessageDialog(null, "정상 처리되었습니다.", "작업 완료", JOptionPane.INFORMATION_MESSAGE);
                            }

                            else{
                                view.btn_signUp.setEnabled(true);
                                view.signUpPanel.setCursor(Cursor.getDefaultCursor());
                                JOptionPane.showMessageDialog(null, "이미 가입된 E-Mail이거나, 네트워크 상태가 불안정합니다.", "오류", JOptionPane.WARNING_MESSAGE);
                            }
                        } catch (Exception ex) {
                            view.btn_signUp.setEnabled(true);
                            view.signUpPanel.setCursor(Cursor.getDefaultCursor());

                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        else{
            view.setVisible(false);
        }
    }
}
