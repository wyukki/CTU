package cz.cvut.fel.pjv;

public class Test {

    public void start() {
        String password = "king!1";
        BruteForceAttacker attacker = new BruteForceAttacker();
        attacker.init(new char[]{'k', 'i','n','g', '!', '1'}, password);
        System.out.println("Trying to break password...");
        attacker.breakPassword(password.length());

        if (attacker.isOpened()) {
            System.out.println("[VAULT] opened, password is " + password);
        } else {
            System.out.println("[VAULT] is still closed");
        }
    }
}
