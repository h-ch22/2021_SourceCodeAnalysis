package main.moon_lander.MobileController.Observer;

public interface mobileControllerObservable {
    void addObserver(mobileControllerObserver o);
    void disconnect(mobileControllerObserver o);
    void updateAxis();
}
