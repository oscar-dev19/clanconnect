package com.nullpointertech.cLANConnect;

import java.util.ArrayList;

/**
 * Author: Cameron Hozouri
 * Lan party event object
 * holds all properties to create a party
 */
public class LanParty {
    private String PartyName;
    private String creatorUID;
    private String creatorName;
    private int numOfAttendess;
    private String Address;
    private boolean isPrivate;
    private double Lat;
    private double Long;
    private ArrayList<String> attendees;
    private ArrayList<String> games;
    private String description;
    private String ChatGroupID;
    private String platform;
    private ArrayList<String> partyTags;

    public LanParty()
    {

    }


    public LanParty(String partyName, String creatorUID, String creatorName, int numOfAttendess, String address, boolean isPrivate, double lat, double aLong, ArrayList<String> attendees, ArrayList<String> games, String description, String chatGroupID, String platform, ArrayList<String> partyTags) {
        PartyName = partyName;
        this.creatorUID = creatorUID;
        this.creatorName = creatorName;
        this.numOfAttendess = numOfAttendess;
        Address = address;
        this.isPrivate = isPrivate;
        Lat = lat;
        Long = aLong;
        this.attendees = attendees;
        this.games = games;
        this.description = description;
        ChatGroupID = chatGroupID;
        this.platform = platform;
        this.partyTags = partyTags;

    }

    public String getPartyName() {
        return PartyName;
    }

    public void setPartyName(String partyName) {
        PartyName = partyName;
    }

    public String getCreatorUID() {
        return creatorUID;
    }

    public void setCreatorUID(String creatorUID) {
        this.creatorUID = creatorUID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getNumOfAttendess() {
        return numOfAttendess;
    }

    public void setNumOfAttendess(int numOfAttendess) {
        this.numOfAttendess = numOfAttendess;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public ArrayList<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

    public ArrayList<String> getGames() {
        return games;
    }

    public void setGames(ArrayList<String> games) {
        this.games = games;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChatGroupID() {
        return ChatGroupID;
    }

    public void setChatGroupID(String chatGroupID) {
        ChatGroupID = chatGroupID;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public ArrayList<String> getPartyTags() {
        return partyTags;
    }

    public void setPartyTags(ArrayList<String> partyTags) {
        this.partyTags = partyTags;
    }

}
