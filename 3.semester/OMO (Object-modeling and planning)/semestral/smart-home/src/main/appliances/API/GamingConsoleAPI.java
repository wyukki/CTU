package main.appliances.API;

import main.appliances.GamingConsole;
import main.appliances.components.GamingCD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GamingConsoleAPI {
    private final GamingConsole gamingConsole;
    private final List<GamingCD> games = new ArrayList<>();
    private final Random random = new Random();
    private GamingCD CD;

    public GamingConsoleAPI(GamingConsole gamingConsole) {
        this.gamingConsole = gamingConsole;
        createGamingDisks();
    }

    public void playConsole() {
        gamingConsole.turnOn();
        insertCD(getDisk());
    }

    public void stopPlaying() {
        ejectCD();
        gamingConsole.turnOff();
    }

    private void insertCD(GamingCD cd) {
        this.CD = cd;
    }

    private void ejectCD() {
        this.CD = null;
    }

    private void createGamingDisks() {
        games.add(new GamingCD("FIFA22"));
        games.add(new GamingCD("NBA2k20"));
        games.add(new GamingCD("GTA5"));
        games.add(new GamingCD("DOOM"));
        games.add(new GamingCD("Cyberpunk"));
    }

    private GamingCD getDisk() {
        int index = Math.abs(random.nextInt()) % 5;
        return games.get(index);
    }

    public String getCD() {
        return CD.getGameName();
    }
}
