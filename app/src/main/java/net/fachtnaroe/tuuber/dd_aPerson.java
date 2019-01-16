package net.fachtnaroe.tuuber;

public class dd_aPerson {
    public String First;
    public String Family;
    public String email;
    public String phone;
    public Integer pID;

    public dd_aPerson() {

    }

    public boolean validemail () {
        String temp;
        temp = email;
        Integer loop;

        for (loop = 0; loop <= email.length(); loop++) {
            System.out.print(temp.toCharArray()[loop]);
        }
        return true;
    }

    public boolean validphone () {
        String temp;
        temp = phone;
        Integer loop;

        for (loop = 0; loop <= phone.length(); loop++) {
            System.out.print(temp.toCharArray()[loop]);
        }

        return true;
    }
}
