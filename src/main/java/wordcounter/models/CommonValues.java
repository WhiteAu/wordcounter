package wordcounter.models;

public class CommonValues {
    private static final int millisecondsToSeconds = 1000;
    private static final int secondsInMinute = 60;
    private static final int secondsInHour = 3600;

    public static int getSecondsInMinute() {
        return secondsInMinute;
    }

    public static int getMillisecondsToSeconds() {
        return millisecondsToSeconds;
    }

    public static int getSecondsInHour() {
        return secondsInHour;
    }
}
