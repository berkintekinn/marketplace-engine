package com.marketplace.services;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer o) { observers.add(o); }
    
    public void notify(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}