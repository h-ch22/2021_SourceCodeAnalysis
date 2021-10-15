package Score.Helper;

import UserManagement.Helper.UserManagement;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.EventListener;
import com.google.firebase.cloud.FirestoreClient;
import main.moon_lander.Framework;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreManagement extends UserManagement {
    private final SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd kk:mm:ss.SSSS");

    private BufferedImage ic_goldCup, ic_silverCup, ic_bronzeCup;
    private String score_1st, score_2nd, score_3rd;
    private Graphics2D g2d;

    private Vector<Vector> scoreData = new Vector<>();
    protected HashMap<String, Long> rankData = new HashMap<>();

    public ScoreManagement() {
        super();
    }

    protected void updateScore(Long score, String stage){
        Map<String, Object> scoreData = new HashMap<>();
        Date current = new Date();

        scoreData.put("score", score);
        scoreData.put("date", format.format(current));
        scoreData.put("stage", stage);

        db.collection("Users").document(userRecord.getUid()).collection("Score").add(scoreData);
    }

    protected Vector<Vector> getScores(){
        ApiFuture<QuerySnapshot> future = db.collection("Users").document(userRecord.getUid()).collection("Score").get();

        try{
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            for(QueryDocumentSnapshot document : documents){
                Vector<String> data = new Vector<>();
                data.addElement(document.get("date").toString());
                data.addElement(document.get("score").toString());
                data.addElement(document.get("stage").toString());

                scoreData.addElement(data);
            }

        }catch(Exception e){
            e.printStackTrace();
        }


        return scoreData;
    }

    public void getRank(){
        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                if (error != null){
                    return;
                }

                assert value != null;
                for(DocumentChange change : value.getDocumentChanges()){
                    switch(change.getType()){
                        case ADDED:
                        case MODIFIED :
                            CollectionReference rankRef = db.collection("Users");

                            Query query = rankRef.orderBy("maxScore", Query.Direction.DESCENDING).limit(3);
                            ApiFuture<QuerySnapshot> queryResult = query.get();

                            try {
                                for(DocumentSnapshot document : queryResult.get().getDocuments()){
                                    if(document.get("maxScore") != null){
                                        rankData.put(Objects.requireNonNull(document.get("nickName")).toString(), Long.parseLong(Objects.requireNonNull(document.get("maxScore")).toString()));
                                    }
                                }

                            } catch (InterruptedException | ExecutionException e) {
                                e.printStackTrace();
                            }


                        case REMOVED :
                            rankData.get(change.getDocument().getId());

                        default :
                            break;
                    }

                }

                drawRank(g2d);
            }
        });
    }

    public void drawRank(Graphics2D g2d){
        this.g2d = g2d;
        loadImage();

        Font font_title = new Font("default", Font.BOLD, 20);
        Font font_contents = new Font("default", Font.PLAIN, 25);

        try{
            score_1st = String.valueOf(rankData.values().toArray()[0]);
            score_2nd = String.valueOf(rankData.values().toArray()[1]);
            score_3rd = String.valueOf(rankData.values().toArray()[2]);
        }

        catch(ArrayIndexOutOfBoundsException e){
            switch(rankData.values().toArray().length){
                case 0:
                    score_1st = "-";

                case 1:
                    score_2nd = "-";

                case 2:
                    score_3rd = "-";
            }
        }

        g2d.setColor(new Color(1f, 1f, 1f, .3f));
        g2d.fillRoundRect(Framework.frameWidth / 2 - 280, Framework.frameHeight - 230, 530, 150, 50, 50);

        g2d.drawImage(ic_goldCup, Framework.frameWidth / 2 - 250, Framework.frameHeight - 200, 100, 100, null);
        g2d.setColor(Color.ORANGE);
        g2d.setFont(font_title);
        g2d.drawString("1ST", Framework.frameWidth / 2 - 150, Framework.frameHeight - 170);
        g2d.setFont(font_contents);
        g2d.drawString(score_1st, Framework.frameWidth / 2 - 150, Framework.frameHeight - 130);

        g2d.drawImage(ic_silverCup, Framework.frameWidth / 2 - 100, Framework.frameHeight - 200, 100, 100, null);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.setFont(font_title);
        g2d.drawString("2ND", Framework.frameWidth / 2, Framework.frameHeight - 170);
        g2d.setFont(font_contents);
        g2d.drawString(score_2nd, Framework.frameWidth / 2, Framework.frameHeight - 130);

        g2d.drawImage(ic_bronzeCup, Framework.frameWidth / 2 + 50, Framework.frameHeight - 200, 100, 100, null);
        g2d.setColor(Color.PINK);
        g2d.setFont(font_title);
        g2d.drawString("3RD", Framework.frameWidth / 2 + 150, Framework.frameHeight - 170);
        g2d.setFont(font_contents);
        g2d.drawString(score_3rd, Framework.frameWidth / 2 + 150, Framework.frameHeight - 130);
    }

    private void loadImage(){
        try{
            URL goldCupUrl = this.getClass().getClassLoader().getResource("ic_goldcup.png");
            URL silverCupUrl = this.getClass().getClassLoader().getResource("ic_silvercup.png");
            URL bronzeCupUrl = this.getClass().getClassLoader().getResource("ic_bronzecup.png");

            ic_goldCup = ImageIO.read(Optional.ofNullable(goldCupUrl).orElseThrow());
            ic_silverCup = ImageIO.read(Optional.ofNullable(silverCupUrl).orElseThrow());
            ic_bronzeCup = ImageIO.read(Optional.ofNullable(bronzeCupUrl).orElseThrow());
        }

        catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}