package com.careerforge.user;

public abstract class User {
    protected String userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    
    public User(String userId, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "First Name: " + firstName + "\n Last Name: " + lastName + "\n Email: " + email + "\n";
    }
}

