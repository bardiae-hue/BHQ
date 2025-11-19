public class Goals {
    private String description;
    private ProgressTracker tracker;

    public Goals(String description) {
        this.description = description;
        this.tracker = new ProgressTracker();
    }

    public String getDescription() { return description; }
    public ProgressTracker getTracker() { return tracker; }
}