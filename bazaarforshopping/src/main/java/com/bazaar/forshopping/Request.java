package com.bazaar.forshopping;

/**
 * Created by gsoganci on 14.09.2017.
 */

public class Request {
    private String price;
    private String sender;

    public Request(){

    }

    public Request(String price, String sender) {
        this.price = price;
        this.sender = sender;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
