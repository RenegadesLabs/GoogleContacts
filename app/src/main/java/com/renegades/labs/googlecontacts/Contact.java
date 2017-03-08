package com.renegades.labs.googlecontacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Виталик on 07.03.2017.
 */

public class Contact {
    private String firstName;
    private String lastName;
    private List<String> emails;
    private List<String> phoneNumbers;

    public Contact() {
        emails = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
    }

    public void addEmail(String email) {
        emails.add(email);
    }

    public void addPhone(String phoneNumber){
        phoneNumbers.add(phoneNumber);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
