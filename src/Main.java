import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private List<Goals> goals = new ArrayList<>();
    private VBox goalsContainer;

    @Override
    public void start(Stage stage) {

        // Goal Input
        TextField goalInput = new TextField();
        goalInput.setPromptText("Enter your goal");
        goalInput.setStyle("-fx-font-size: 16px; -fx-padding: 8px;");

        Button addGoalButton = new Button("Add Goal");
        addGoalButton.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: #d3d3d3;" +
                        "-fx-text-fill: black;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 8px;"
        );

        // Container for goals
        goalsContainer = new VBox(15);
        ScrollPane scrollPane = new ScrollPane(goalsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(600);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Load saved goals
        goals = DataStore.load();
        for (Goals goal : goals) {
            VBox goalBox = createGoalBox(goal);
            goalsContainer.getChildren().add(goalBox);
        }

        // Add goal action
        addGoalButton.setOnAction(e -> {
            String desc = goalInput.getText().trim();
            if (!desc.isEmpty()) {
                Goals goal = new Goals(desc);
                goals.add(goal);
                VBox goalBox = createGoalBox(goal);
                goalsContainer.getChildren().add(goalBox);
                goalInput.clear();

                // save after adding new goal
                DataStore.save(goals);
            }
        });

        // Root layout
        VBox root = new VBox(20, goalInput, addGoalButton, scrollPane);
        root.setStyle("-fx-padding: 20px; -fx-background-color: #4f4f4f;");

        Scene scene = new Scene(root, 800, 800);
        stage.setScene(scene);
        stage.setTitle("BHQ Goal Tracker");
        stage.show();
    }

    // Create goal UI box
    private VBox createGoalBox(Goals goal) {
        Label descLabel = new Label("Remember what you are fighting for: " + goal.getDescription());
        descLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #FFFFFF; -fx-wrap-text: true;");

        Label streakLabel = new Label("Streak: " + goal.getTracker().getStreak());
        streakLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #FFFFFF;");

        Label timerLabel = new Label("");
        timerLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #e6e6e6;");

        Button pressButton = new Button("One more day ✊");
        final String BUTTON_NORMAL = "-fx-font-size: 16px; -fx-background-color: #d3d3d3; -fx-text-fill: black; -fx-padding: 8px 16px; -fx-background-radius: 8px;";
        final String BUTTON_HOVER = "-fx-font-size: 16px; -fx-background-color: #ffcc99; -fx-text-fill: black; -fx-padding: 8px 16px; -fx-background-radius: 8px;";
        pressButton.setStyle(BUTTON_NORMAL);

        // Hover effect
        pressButton.setOnMouseEntered(ev -> pressButton.setStyle(BUTTON_HOVER));
        pressButton.setOnMouseExited(ev -> pressButton.setStyle(BUTTON_NORMAL));

        // Initial availability
        pressButton.setDisable(!goal.getTracker().canPress());

        pressButton.setOnAction(ev -> {
            goal.getTracker().recordPress();
            streakLabel.setText("Streak: " + goal.getTracker().getStreak());
            pressButton.setDisable(true);

            // save after updating streak
            DataStore.save(goals);
        });

        // Countdown animation
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long millisLeft = goal.getTracker().millisUntilNextPress();
                if (millisLeft > 0) {
                    long hours = millisLeft / 3600000;
                    long mins = (millisLeft % 3600000) / 60000;
                    long secs = (millisLeft % 60000) / 1000;
                    timerLabel.setText(String.format("Next press in: %02dh %02dm %02ds", hours, mins, secs));
                } else {
                    timerLabel.setText("You can press the button!");
                    pressButton.setDisable(false);
                }
            }
        };
        timer.start();

        VBox box = new VBox(10, descLabel, streakLabel, timerLabel, pressButton);
        box.setStyle("-fx-padding: 15px; -fx-background-color: #555555; -fx-background-radius: 8px;");
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
