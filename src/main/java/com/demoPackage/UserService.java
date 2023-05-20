package com.demoPackage;

import java.util.ArrayList;

public class UserService {
	private ArrayList<User> users = new ArrayList<User>();

    public boolean createUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            System.out.println(String.format("User with email %s existed", user.getEmail()));
            return false;
        }
        users.add(user);
        System.out.println(String.format("User %s %s %s created", user.getFirstName(), user.getLastName(), user.getEmail()));
        return true;
    }

	public String getAllUsers() {
		String userData = "";
		for (User user : users) {
			userData += String.format("%s, %s, %s%n",user.getFirstName(), user.getLastName(), user.getEmail());
		}
		if(userData.isEmpty())
			return "No users in the DataBase !!";
		else
			return userData;
	}

    public boolean deleteUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
        	 users.remove(user);
             System.out.println(String.format("User %s %s %s deleted!!", user.getFirstName(), user.getLastName(), user.getEmail()));
        	return true;
        }
        System.out.println(String.format("User with email id %s does not exists in the database !!", user.getEmail()));
        return false;
    }

    public User getUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        return null;
    }

}
