public class Goals {
    private String description;
    private ProgressTracker tracker;

    public Goals(String description, int numDays) {
        this.description = description;
        this.tracker = new ProgressTracker(numDays);
    }

    public String getDescription() { return description; }
    public ProgressTracker getTracker() { return tracker; }

    public boolean goalIsCompleted(){return tracker.isComplete();}
}