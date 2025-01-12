package com.alex.coupons.utils;

public class StatisticsUtils {

    private String userName;
    private String actionType;


    public StatisticsUtils(String userName, String actionType) {
        this.userName = userName;
        this.actionType = actionType;
    }


    public static void sendStatistics (String username, String action){
//        Thread thread = new Thread( new StatisticTask(username, action));

//        thread.start();

        new Thread(() -> {System.out.println(action + " " + username);}).start();
    }
}
