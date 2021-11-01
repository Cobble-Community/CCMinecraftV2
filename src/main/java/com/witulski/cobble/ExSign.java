package com.witulski.cobble;

import org.bukkit.Location;

public class ExSign {
    private Location location;
    private String resource;
    private String owner;
    private int signFee;
    private Exchange associatedExchange;

    public ExSign(Location location, String resource, int signFee, String owner, Exchange associatedExchange){
        this.location = location;
        this.resource=resource;
        this.owner=owner;
        this.signFee=signFee;
        this.associatedExchange=associatedExchange;
    }

    public String getOwner(){
        return this.owner;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getResource(){
        return this.resource;
    }

    public int getSignFee() {return this.signFee; }

    public Exchange getAssociatedExchange(){return associatedExchange;}
}
