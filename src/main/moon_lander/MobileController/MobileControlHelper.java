package main.moon_lander.MobileController;

import UserManagement.Helper.UserManagement;
import com.google.cloud.firestore.*;
import main.moon_lander.Framework;
import main.moon_lander.MobileController.Observer.mobileControllerObservable;
import main.moon_lander.MobileController.Observer.mobileControllerObserver;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MobileControlHelper extends UserManagement implements mobileControllerObservable {
    protected int axisX = 0, axisY = 0;
    private List<mobileControllerObserver> observerList = new ArrayList<>();

    public void init(mobileControllerObserver o, Framework.GameState state){
        switch(state){
            case GAME_CONTENT_LOADING -> addObserver(o);
            case GAMEOVER -> disconnect(o);
        }
    }

    public void getUserControl(){
        try{
            db.collection("Users")
                    .whereEqualTo("mail", encrypt(userRecord.getEmail()))
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                            if (error != null){
                                System.out.println("Failed Listen : " + error);
                            }

                            for(DocumentSnapshot doc : value){
                                axisX = Integer.parseInt(Objects.requireNonNullElse(doc.get("xAxis"), 0).toString());
                                axisY = Integer.parseInt(Objects.requireNonNullElse(doc.get("yAxis"), 0).toString());
                            }

                            updateAxis();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addObserver(mobileControllerObserver o) {
        observerList.add(o);

        getUserControl();
    }

    @Override
    public void disconnect(mobileControllerObserver o) {
        observerList.remove(o);
    }

    @Override
    public void updateAxis() {
        observerList.forEach(mobileControllerObserver -> mobileControllerObserver.update(new Point(axisX, axisY)));
    }
}
