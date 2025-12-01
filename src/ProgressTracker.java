public class ProgressTracker {
    private int streak;
    private long lastPressTime;

    public ProgressTracker() {
        streak = 0;
        lastPressTime = 0;
    }

    public boolean canPress() {
        return System.currentTimeMillis() - lastPressTime >= 86400000;
    }

    public void recordPress() {
        if (canPress()) {
            streak++;
            lastPressTime = System.currentTimeMillis();
        }
    }

    public long millisUntilNextPress() {
        long elapsed = System.currentTimeMillis() - lastPressTime;
        return Math.max(0, 86400000 - elapsed);
    }

    public int getStreak() { return streak; }
    public long getLastPressTime() { return lastPressTime; }

    public void setStreak(int s) { streak = s; }
    public void setLastPressTime(long t) { lastPressTime = t; }

    public double getProgress() {
        long elapsed = System.currentTimeMillis() - lastPressTime;
        double progress = (double) elapsed / 86400000;
        return Math.min(1.0, progress);
    }
}
