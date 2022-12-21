package com.nullpointertech.cLANConnect;

import java.util.ArrayList;

/**
 * Author: Cameron Hozouri
 * Chat is not implemented yet
 */
public class LanChatGroup {

    private ArrayList<String> groupMembers;
    private String ChatID;

    public LanChatGroup()
    {

    }

    public LanChatGroup(ArrayList<String> groupMembers, String chatID)
    {
        this.groupMembers = groupMembers;

        ChatID = chatID;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public String getChatID() {
        return ChatID;
    }

    public void setChatID(String chatID) {
        ChatID = chatID;
    }

}
