package Score.Controller;

import Score.Helper.ScoreManagement;
import Score.View.ScoreView;
import UserManagement.Helper.UserManagement;
import UserManagement.View.SignInView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Vector;

public class ScoreViewController extends ScoreManagement implements ActionListener {
    private ScoreView view;
    private UserManagement userManagement = new UserManagement();
    private JFrame gameWindow;

    public ScoreViewController(ScoreView view, JFrame gameWindow){
        this.view = view;
        this.gameWindow = gameWindow;

        if(view != null){
            view.btn_close.addActionListener(this);
            view.btn_signOut.addActionListener(this);

            insertData();
        }
    }

    private void insertData(){
        Vector<Vector> data = getScores();

        for(Vector element : data){
            view.model.addRow(element);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton request = (JButton) e.getSource();

        if(request.getText().equals("닫기")){
            view.dispose();
        }

        else if(request.getText().equals("로그아웃")){
            int option = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "로그아웃", JOptionPane.YES_NO_OPTION);

            if(option == JOptionPane.YES_OPTION){
                boolean signOutResult = userManagement.signOut();

                if(signOutResult){
                    view.dispose();
                    gameWindow.dispose();

                    new SignInView();
                }

                else{
                    JOptionPane.showMessageDialog(null, "로그아웃 처리 중 문제가 발생했습니다.", "로그아웃 오류", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
}
