package com.example.michael.bodyfatapp.Profile;

public class ProfileDetails {
    private String name;
    private String age;
    public String gender;
    private String height;
    private String weight;

    //constructor for the profile with all basic profile detail requirements
    public ProfileDetails(String name, String age, String gender, String height, String weight){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getHeight() {
        return height;
    }

    public String getWeight() {
        return weight;
    }

}
