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
                out.println(goal.getTracker().getTargetDays());
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

                String daysLine = br.readLine();
                int numDays = (daysLine != null) ? Integer.parseInt(daysLine) : 30;

                Goals goal = new Goals(desc, numDays);
                goal.getTracker().setStreak(streak);
                goal.getTracker().setLastPressTime(last);

                goals.add(goal);
            }
        } catch (Exception e) {
            // First run or corrupt file → return empty list
        }

        return goals;
    }

}
