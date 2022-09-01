package main.event;

public final class DayTimer {
    private DayTime partOfTheDay;
    private Weather weather;
    private final Season season;
    private int time;
    private int dayCounter = 1;

    public DayTimer(String season) {
        this.partOfTheDay = DayTime.MORNING;
        this.weather = Weather.COLD;
        if ("Winter".equals(season)) {
            this.season = Season.WINTER;
        } else {
            this.season = Season.SUMMER;
        }
        this.time = 6;
    }

    public DayTime getDayTime() {
        return this.partOfTheDay;
    }

    public Weather getWeather() {
        return this.weather;
    }

    private void setPartOfTheDay(int time) {
        if (time >= 0 && time < 6) {
            this.partOfTheDay = DayTime.NIGHT;
            this.weather = Weather.COLD;
        } else if (time >= 6 && time < 12) {
            this.partOfTheDay = DayTime.MORNING;
            this.weather = Weather.COLD;
        } else if (time >= 12 && time < 18) {
            this.partOfTheDay = DayTime.AFTERNOON;
            this.weather = season == Season.WINTER ? Weather.COLD : Weather.HOT;
        } else {
            this.partOfTheDay = DayTime.EVENING;
            this.weather = season == Season.WINTER ? Weather.COLD : Weather.HOT;
        }
    }

    /**
     * Moves time in simulation, time moves by daytime.
     */
    public void incrementTime() {
        int nextPartOfTheDay = 6;
        this.time = (this.time + nextPartOfTheDay) % 24;
        setPartOfTheDay(this.time);
        if (this.partOfTheDay == DayTime.NIGHT) {
            dayCounter++;
        }
        System.out.println("\nDay time: " + this.partOfTheDay);
    }

    public int getDayCounter() {
        return dayCounter;
    }

    public Season getSeason() {
        return this.season;
    }
}
