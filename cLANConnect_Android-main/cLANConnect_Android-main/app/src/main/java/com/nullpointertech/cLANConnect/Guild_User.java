/*
 * A simple User class for the Guilds app.
 *
 * @author Oscar Lopez
 * Date Last Modified: Saturday May 4, 2019.
 */
package com.nullpointertech.cLANConnect;

import java.util.ArrayList;

public class Guild_User {
    private String username,name,email,phone,description;
    private ArrayList<String> listOfGames;

    public Guild_User(){
        //Empty Constructor.
    }

    public Guild_User(String username,
                      String name,
                      String email,
                      String phone,
                      String description){

        setUsername(username);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setDescription(description);
        listOfGames = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getListOfGames(){
        return listOfGames;
    }

    public void setListOfGames(ArrayList<String> gameList){
        listOfGames = gameList;
    }


    //Following methods in charge of managing the user's list of games.

    public void addGame(String gameTitle){
        listOfGames.add(gameTitle);
    }

    public boolean removeGame(String gameTitle){
        return listOfGames.remove(gameTitle);
    }

    public boolean hasGame(String gameTitle){
        return listOfGames.contains(gameTitle);
    }

    public String listGames(){
        StringBuilder sb = new StringBuilder();

        for(String game: listOfGames){
            sb.append("\n" + game);
        }
        return sb.toString();
    }
}
