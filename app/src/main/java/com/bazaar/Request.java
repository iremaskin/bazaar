package com.bazaar;

/**
 * Created by gsoganci on 14.09.2017.
 */

public class Request {
    private String foundShopCount;

    public Request(){

    }

    public Request(String foundShopCount) {
        this.foundShopCount = foundShopCount;
    }

    public String getFoundShopCount() {
        return foundShopCount;
    }

    public void setFoundShopCount(String foundShopCount) {
        this.foundShopCount = foundShopCount;
    }
}
