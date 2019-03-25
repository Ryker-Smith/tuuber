package net.fachtnaroe.tuuber;

import com.google.appinventor.components.runtime.TextBox;

public class aPerson {
    public String first;
    public String family;
    public String email;
    public String phone;
    public Integer pID;

    private final Integer minPhoneSize=8;

    public aPerson() {
        // The default constructor
    }

    public boolean validData() {
        if (    validName()
                && validEmail()
                && validPhone()) {
            return true;
        }
        else {
            return false;
        }
    }
    public boolean validName() {
        if (first.equals("") || family.equals("")) {
            return false;
        }
        return true;
    }

    public boolean validEmail () {
        // validate an email. Apply a series of tests, if any
        // fails return false immediately, otherwise return true at end
        String temp = this.email;
        // first split the email by the @ symbol
        // check that there are 2 parts returned
        // with the RHS use a 'occurrences'function to count how many '.' are present
        // or better, use a for loop to count
        //
        // return true IF (there are 2 parts) AND (there are >=1 occurrences)
        Integer loop;
        String[] parts=temp.split("@");
        if (parts.length != 2) {
            // only 2 parts allowed
            return false;
        }
        // call them right and left for convenience
        String left=parts[0];
        String right=parts[1];
        if (left.length() < 1) {
            // left part too short
            return false;
        }
        if (right.length() < 3) {
            // right part too short
            return false;
        }
        Integer occurrenceCount=0;
        for (loop = 0; loop <= right.length()-1; loop++) {
            if (right.toCharArray()[loop] == '.') {
                occurrenceCount++;
            };
        }
        if (occurrenceCount < 1) {
            return false;
        }
        // if we made it this far, all is good
        return true;
    }

    public  boolean validPhone () {
        String temp = this.phone;
        // first enforce a minimum length (arbitrary!)
        if (temp.length() <= minPhoneSize) {
            return false;
        }
        Integer loop;
        // check if any non-digit characters are present
        // (will have ASCII values outside of 48 ... 57
        for (loop=0; loop <= temp.length()-1; loop++) {
            if ((temp.toCharArray()[loop] < 48) ||
                (temp.toCharArray()[loop] > 57) )
                {
                return false; // found non-numeric
            }
        }

        // if we made it this far, all is good
        return true;
    }
} 
