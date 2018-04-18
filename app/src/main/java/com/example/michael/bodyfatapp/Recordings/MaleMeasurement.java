package com.example.michael.bodyfatapp.Recordings;

import com.google.firebase.database.PropertyName;

/**
 * Created by Michael on 17/03/2018.
 */

//male objcte that is pushed ot firebase
public class MaleMeasurement  {
    private String Subscapular, Abdominal, Tricep, WaistCircumference,Weight, BodyFatMass, BodyFatPercentage;
    private Long Date;

    public MaleMeasurement(String Tricep, String Abdominal, String Subscapular, String WaistCircumference,String Weight, String BodyFatMass,String BodyFatPercentage, Long Date) {
        this.Subscapular = Subscapular;
        this.WaistCircumference = WaistCircumference;
        this.Tricep = Tricep;
        this.Abdominal = Abdominal;
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

    @PropertyName("Abdominal")
    public String getAbdominal() {
        return Abdominal;
    }

    @PropertyName("Abdominal")
    public void setAbdominal(String abdominal) {
        Abdominal = abdominal;
    }

    @PropertyName("Tricep")
    public String getTricep() {
        return Tricep;
    }

    @PropertyName("Tricep")
    public void setTricep(String tricep) {
        Tricep = tricep;
    }

    @PropertyName("WaistCircumference")
    public String getWaistCircumference() {
        return WaistCircumference;
    }

    @PropertyName("WaistCircumference")
    public void setWaistCircumference(String waistCircumference) {
        WaistCircumference = waistCircumference;
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



