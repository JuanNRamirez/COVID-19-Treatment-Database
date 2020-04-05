package com.example.covid19treatmentdatabase;

public class Doctor {

    private String name, birthday, education, workPosition, workPlace, phoneNumber;
    private String email, pword;
    private String experiences, country, city;

    public Doctor(String name, String birthday, String education, String workPosition, String workPlace,
                  String phoneNumber, String email, String pword, String experiences, String country, String city) {
        this.name = name;
        this.birthday = birthday;
        this.education = education;
        this.workPosition = workPosition;
        this.workPlace = workPlace;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.pword = pword;
        this.experiences = experiences;
        this.country = country;
        this.city = city;
    }

}
