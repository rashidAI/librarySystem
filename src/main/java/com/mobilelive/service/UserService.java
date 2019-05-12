package com.mobilelive.service;

import com.mobilelive.helper.Utils;
import com.mobilelive.model.User;

import javax.rmi.CORBA.Util;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService {

    private static List<User> users = new ArrayList<>();

    static {
        users = Utils.readUsersFile.get();
        if (users.size()  == 0  || !users.get(0).getUserType().equals( "Admin" )){
            throw new RuntimeException( "Admin user is required." );
        }
    }

    public Optional<User> getUser(Long id) {
        return users.stream()
            .filter(user -> user.getId()
                    .equals(id))
            .findFirst();
    }

    public void createLibrarian(User user) {
        users.add(user);
        Utils.updateUserFile(users );
    }

    public List<User> getUsers(){
        List<User> updatedUser = users.stream().filter( user ->
            !user.getUserType().equals("Admin")
        ).collect( Collectors.toList());
        return updatedUser;
    }

    public void updateLibrarian(User user){
        users = users.stream().map(userObj -> {
            if(userObj.getId() == user.getId()){
                return user;
            }
            return userObj;
        }).collect( Collectors.toList());
        Utils.updateUserFile(users);
    }

    public boolean deleteUser(Long id){
         users.removeIf(user -> user.getId().equals(id));
         Utils.updateUserFile(users);
         return true;
    }

    public String LoginUser(User user){
        Optional<User> user1 = users.stream().filter( userObj ->
                userObj.getEmail().equals( user.getEmail())
        ).findFirst();
        if (user1.isPresent()  && user1.get().getPassword().equals(  user.getPassword() )){
            return "User is Authenticated as " + user1.get().getUserType();
        }
        return "Email or Passwrod does not match";
    }

}
