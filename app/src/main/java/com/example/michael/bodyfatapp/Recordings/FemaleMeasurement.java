package com.example.michael.bodyfatapp.Recordings;

import com.google.firebase.database.PropertyName;

/**
 * Created by Michael on 17/03/2018.
 */
//this is the object that is pushed to Firebase
public class FemaleMeasurement {
    private String Subscapular, Chin, Tricep, HipCircumference, KneeBreadth,Weight, BodyFatMass,BodyFatPercentage;
    private Long Date;

    //constructor for the list
    public FemaleMeasurement(String Tricep, String Chin, String Subscapular, String HipCircumference, String KneeBreadth,String Weight, String BodyFatMass, String BodyFatPercentage, Long Date) {
        this.Tricep = Tricep;
        this.Chin = Chin;
        this.Subscapular = Subscapular;
        this.HipCircumference = HipCircumference;
        this.KneeBreadth = KneeBreadth;
        this.BodyFatMass = BodyFatMass;
        this.BodyFatPercentage = BodyFatPercentage;
        this.Date = Date;
        this.Weight = Weight;
    }
    @PropertyName("Subscapular")
    public String getSubscapular() {
        return Subscapular;
    }

    @PropertyName("Subscapular")
    public void setSubscapular(String subscapular) {
        Subscapular = subscapular;
    }

    @PropertyName("Chin")
    public String getChin() {
        return Chin;
    }

    @PropertyName("Chin")
    public void setChin(String chin) {
        Chin = chin;
    }

    @PropertyName("Tricep")
    public String getTricep() {
        return Tricep;
    }

    @PropertyName("Tricep")
    public void setTricep(String tricep) {
        Tricep = tricep;
    }

    @PropertyName("HipCircumference")
    public String getHipCircumference() {
        return HipCircumference;
    }

    @PropertyName("HipCircumference")
    public void setHipCircumference(String hipCircumference) {
        HipCircumference = hipCircumference;
    }
    @PropertyName("KneeBreadth")
    public String getKneeBreadth() {
        return KneeBreadth;
    }

    @PropertyName("KneeBreadth")
    public void setKneeBreadth(String kneeBreadth) {
        KneeBreadth = kneeBreadth;
    }

    @PropertyName("BodyFatMass")
    public String getBodyFatMass() {
        return BodyFatMass;
    }

    @PropertyName("BodyFatMass")
    public void setBodyFatMass(String bodyFatMass) {
        BodyFatMass = bodyFatMass;
    }

    @PropertyName("BodyFatPercentage")
    public String getBodyFatPercentage() {
        return BodyFatPercentage;
    }

    @PropertyName("BodyFatPercentage")
    public void setBodyFatPercentage(String bodyFatPercentage) {
        BodyFatPercentage = bodyFatPercentage;
    }

    @PropertyName("Weight")
    public String getWeight() {
        return Weight;
    }

    @PropertyName("Weight")
    public void setWeight(String weight) {
        Weight = weight;
    }

    @PropertyName("Date")
    public Long getDate() {
        return Date;
    }

    @PropertyName("Date")
    public void setDate(Long date) {
        Date = date;
    }
}


