package net.fachtnaroe.tuuber;

import static java.lang.System.out;

public class ak_aPerson {
    public String First;
    public String Family;
    public String email;
    public String phone;
    public Integer pID;

    public ak_aPerson() {
        // The constructor
    }

    public boolean validEmail () {
        String temp;
        temp = email;
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

