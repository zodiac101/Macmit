package com.example.rohil.macmit;

/**
 * Created by Rohil on 12/30/2015.
 */
public class SearchFields {

    public String Item;
    public String Name;
    public String Address;
    public String MapLocationLatitude;
    public String MapLocationLongitude;
    public String Email;
    public String PhoneNumber;


    public void setSearchField (String name, String address, String email, String phoneNumber, String mapLocationLatitude, String mapLocationLongitude) {
        Name = name;
        Address = address;
        Email = email;
        PhoneNumber = phoneNumber;
        MapLocationLatitude = mapLocationLatitude;
        MapLocationLongitude = mapLocationLongitude;
    }
}