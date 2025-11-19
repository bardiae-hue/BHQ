import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private static final String FILE_NAME = "bhq_data.txt";

    public static void save(List<Goals> goals) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Goals goal : goals) {
                out.println(goal.getDescription());
                out.println(goal.getTracker().getStreak());
                out.println(goal.getTracker().getLastPressTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Goals> load() {
        List<Goals> goals = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String desc;
            while ((desc = br.readLine()) != null) {
                int streak = Integer.parseInt(br.readLine());
                long last = Long.parseLong(br.readLine());
                Goals goal = new Goals(desc);
                goal.getTracker().setStreak(streak);
                goal.getTracker().setLastPressTime(last);
                goals.add(goal);
            }
        } catch (Exception e) {
            // first run — return empty list
        }
        return goals;
    }
}
