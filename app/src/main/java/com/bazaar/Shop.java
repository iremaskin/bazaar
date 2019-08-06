package com.bazaar;

/**
 * Created by gsoganci on 14.09.2017.
 */

public class Shop {
    private String name;
    private String isAnswered;

    public Shop(){

    }

    public Shop(String name, String isAnswered) {
        this.name = name;
        this.isAnswered = isAnswered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsAnswered() {
        return isAnswered;
    }

    public void setIsAnswered(String isAnswered) {
        this.isAnswered = isAnswered;
    }
}
