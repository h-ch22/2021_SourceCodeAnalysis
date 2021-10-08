package main.moon_lander.MobileController;

import UserManagement.Helper.UserManagement;
import com.google.firebase.database.*;
import main.moon_lander.Framework;

import java.util.HashMap;
import java.util.Map;

public class MobileControlHelper extends UserManagement  {
    private final DatabaseReference controllerRef = FirebaseDatabase.getInstance().getReference("Controllers");
    private final DatabaseReference coordinateRef = FirebaseDatabase.getInstance().getReference("Coordinates").child(userRecord.getUid());
    private final DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("GameStatus");
    private String gameStatus;
    protected int axisX, axisY;

    public void updateCoordinates(int x, int y){
        Map<String, Object> updateInfo = new HashMap<>();

        updateInfo.put("x", x);
        updateInfo.put("y", y);

        coordinateRef.updateChildrenAsync(updateInfo);
    }

    public void updateGameStatus(Framework.GameState state){
        switch(state){
            case PLAYING :
                gameStatus = "PLAYING";
                break;

            case MAIN_MENU :
                gameStatus = "MAIN MENU";
                break;

            case GAME_CONTENT_LOADING :
            case OPTIONS :
            case STARTING :
            case DESTROYED :
            case GAMEOVER :
                gameStatus = "GAME OVER";
        }

        Map<String, Object> status = new HashMap<>();

        status.put("Status", gameStatus);

        statusRef.child(userRecord.getUid()).updateChildrenAsync(status);
    }

    public void receiveGameSTART(Framework game){
        statusRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.getKey().equals(userRecord.getUid())){
                    String status = String.valueOf(snapshot.child("Status").getValue());

                    if(status.equals("REQUEST TO START")){
                        if(Framework.gameState == Framework.GameState.MAIN_MENU){
                            game.newGame();
                        }

                        else if(Framework.gameState == Framework.GameState.GAMEOVER){
                            game.restartGame();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.getKey().equals(userRecord.getUid())){
                    String status = String.valueOf(snapshot.child("Status").getValue());

                    if(status.equals("REQUEST TO START")){
                        if(Framework.gameState == Framework.GameState.MAIN_MENU){
                            game.newGame();
                        }

                        else if(Framework.gameState == Framework.GameState.GAMEOVER){
                            game.restartGame();
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    public void getUserControl(){
        controllerRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.getKey().equals(userRecord.getUid())){
                    String xAxis = String.valueOf(snapshot.child("xAxis").getValue());
                    String yAxis = String.valueOf(snapshot.child("yAxis").getValue());

                    double axisX_Double = Double.parseDouble(xAxis);
                    double axisY_Double = Double.parseDouble(yAxis);

                    axisX = (int) axisX_Double;
                    axisY = (int) axisY_Double;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
                if(snapshot.getKey().equals(userRecord.getUid())){
                    String xAxis = String.valueOf(snapshot.child("xAxis").getValue());
                    String yAxis = String.valueOf(snapshot.child("yAxis").getValue());

                    double axisX_Double = Double.parseDouble(xAxis);
                    double axisY_Double = Double.parseDouble(yAxis);
                    axisX = (int) axisX_Double;
                    axisY = (int) axisY_Double;
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}
