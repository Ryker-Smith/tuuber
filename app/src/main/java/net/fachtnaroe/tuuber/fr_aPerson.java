package net.fachtnaroe.tuuber;

import static java.lang.System.out;

public class fr_aPerson {
    public String First;
    public String Family;
    public String email;
    public String phone;
    public Integer pID;

    public fr_aPerson() {
        // The constructor
    }

    public boolean validEmail () {
        String temp;
        temp = email;
        // first split the email by the @ symbol
//        temp=temp.split("@");
        // check that there are 2 parts returned
        // with the RHS use a 'occurrences'function to count how many '.' are present
        // or better, use a for loop to count
        //
        // return true IF (there are 2 parts) AND (there are >=1 occurrences)
        Integer loop;


        for (loop = 0; loop <= email.length(); loop++) {
            System.out.print(temp.toCharArray()[loop]);
        }
        return true;
    }

    public  boolean validPhone () {
        return true;
    }
}
