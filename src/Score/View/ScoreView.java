package Score.View;

import Score.Controller.ScoreViewController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

public class ScoreView extends JFrame {
    public JPanel scorePanel;
    public JPanel userInteractionPanel;
    public JButton btn_close, btn_signOut;
    public JTable tableView;
    public JScrollPane scrollPane;
    public Vector<String> column = new Vector<>();
    public DefaultTableModel model;

    private final Dimension dimension = new Dimension(600, 600);

    public ScoreView(JFrame gameWindow){
        scorePanel = new JPanel();

        scorePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        setTitle("마이 페이지");
        setPreferredSize(dimension);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        placePanel();

        new ScoreViewController(this, gameWindow);

        pack();
        add(scorePanel);
        setVisible(true);
    }

    private void placePanel(){
        scrollPane = new JScrollPane();
        userInteractionPanel = new JPanel();
        btn_close = new JButton("닫기");
        btn_signOut = new JButton("로그아웃");

        column.addElement("날짜");
        column.addElement("점수");

        model = new DefaultTableModel(column, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableView = new JTable(model);
        tableView.setAutoCreateRowSorter(true);

        userInteractionPanel.add(btn_signOut);
        userInteractionPanel.add(btn_close);

        scrollPane.add(tableView);
        scrollPane.setViewportView(tableView);
        scorePanel.add(scrollPane);
        scorePanel.add(userInteractionPanel);
    }
}
