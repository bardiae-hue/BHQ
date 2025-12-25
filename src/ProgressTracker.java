public class ProgressTracker {
    private int numDays;
    private int streak;
    private long lastPressTime;

    public ProgressTracker(int numDays) {
        streak = 0;
        lastPressTime = 0;
        this.numDays = numDays;
    }

    public boolean canPress() {
        if (isComplete()){
            return false;
        }
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

    public int getTargetDays(){
        return this.numDays;
    }

    public boolean isComplete(){
        return streak >= numDays;
    }
}
