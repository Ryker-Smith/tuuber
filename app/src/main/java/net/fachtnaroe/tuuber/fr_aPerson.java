package net.fachtnaroe.tuuber;

import static java.lang.System.out;

public class fr_aPerson {
    public String first;
    public String family;
    public String email;
    public String phone;
    public Integer pID;

    public fr_aPerson() {
        // The constructor
    }

    public boolean validEmail () {
        // validate an email. Apply a series of tests, if any
        // fails return false immediately, otherwise return true at end
        String temp = new String();
        temp = this.email;
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
        return true;
    }

    public  boolean validPhone () {
        return true;
    }
}
