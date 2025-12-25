import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BHQ extends Application {

    private List<Goals> goals = new ArrayList<>();
    private VBox goalsContainer;
    private final Random random = new Random();

    // -------------------------------------------------------------
    // MOTIVATION LIST
    // -------------------------------------------------------------
    private final List<String> MOTIVATION_LINES = List.of(
            "Stay focused. Your future self is watching.",
            "Consistency beats intensity — every time.",
            "Small steps every day build massive results.",
            "If you don’t quit, you win.",
            "You’re doing better than you think.",
            "Success is built on boring, repeated habits.",
            "One more day. One more step.",
            "You’re closer than you were yesterday.",
            "Momentum is everything. Keep going.",
            "You don’t need motivation — you need discipline.",
            "Even 1% better each day compounds massively.",
            "Your only competition is who you were yesterday.",
            "Do it for the future you.",
            "You’ve survived 100% of your hardest days.",
            "Tiny progress is still progress.",
            "The grind is what makes you stronger.",
            "You’re building someone unstoppable.",
            "This is how you separate yourself.",
            "Every streak is a promise kept to yourself.",
            "Small wins turn into big wins.",
            "You won’t regret the work — only giving up.",
            "Believe in the process. It’s working.",
            "One decision can change everything.",
            "You are capable of far more than you think.",
            "Success is quiet. Let your actions talk.",
            "Keep going — your persistence is your power."
    );

    // Color palette
    private final String BG_MAIN = "#121212";
    private final String CARD_BG = "#1E1E1E";
    private final String TEXT_LIGHT = "#E0E0E0";
    private final String TEXT_DIM = "#B0B0B0";

    @Override
    public void start(Stage stage) {

        TextField goalInput = new TextField();
        goalInput.setPromptText("Enter your goal...");
        goalInput.setFont(Font.font("Segoe UI", 16));
        goalInput.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-background-color: #1E1E1E;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-prompt-text-fill: #777;" +
                        "-fx-padding: 10;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        TextField goalDays = new TextField();
        goalDays.setPromptText("Enter target number of days...");
        goalDays.setFont(Font.font("Segoe UI", 16));
        goalDays.setStyle(
                "-fx-background-radius: 10;" +
                        "-fx-background-color: #1E1E1E;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-prompt-text-fill: #777;" +
                        "-fx-padding: 10;" +
                        "-fx-font-family: 'Segoe UI';"
        );

        Button addGoalButton = new Button("Add Goal");
        addGoalButton.setStyle(buttonStyle("#2E2E2E"));
        addGoalButton.setOnMouseEntered(e -> addGoalButton.setStyle(buttonStyle("#3A3A3A")));
        addGoalButton.setOnMouseExited(e -> addGoalButton.setStyle(buttonStyle("#2E2E2E")));

        goalsContainer = new VBox(20);
        goalsContainer.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(goalsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        goals = DataStore.load();
        for (Goals goal : goals) {
            goalsContainer.getChildren().add(createGoalBox(goal));
        }

        addGoalButton.setOnAction(e -> {
            String desc = goalInput.getText().trim();
            String daysText = goalDays.getText().trim();

            if (!desc.isEmpty() && !daysText.isEmpty()) {
                int numDays;

                try {
                    numDays = Integer.parseInt(daysText);
                    if (numDays <= 0) return;
                } catch (NumberFormatException ex) {
                    return;
                }

                Goals goal = new Goals(desc, numDays);
                goals.add(goal);
                goalsContainer.getChildren().add(createGoalBox(goal));
                goalInput.clear();
                goalDays.clear();
                DataStore.save(goals);

            }
        });

        VBox root = new VBox(20, goalInput,goalDays ,addGoalButton, scrollPane);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: " + BG_MAIN + ";");

        Scene scene = new Scene(root, 800, 800);
        stage.setTitle("BHQ Goal Tracker");
        stage.setScene(scene);
        stage.show();
    }

    // -------------------------------------------------------------
    // CREATE GOAL CARD
    // -------------------------------------------------------------
    private VBox createGoalBox(Goals goal) {
        Label streakLabel = new Label(
                "Streak: " + goal.getTracker().getStreak() +
                        " / " + goal.getTracker().getTargetDays()
        );
        streakLabel.setFont(Font.font("Segoe UI", 15));
        streakLabel.setTextFill(Color.web(TEXT_DIM));

        Label timerLabel = new Label("");
        timerLabel.setFont(Font.font("Segoe UI", 14));
        timerLabel.setTextFill(Color.web(TEXT_DIM));

        Label descLabel = new Label(goal.getDescription());
        descLabel.setFont(Font.font("Segoe UI", 18));
        descLabel.setTextFill(Color.web(TEXT_LIGHT));
        Button pressButton = new Button("One more day ✊");
        pressButton.setStyle(buttonStyle("#333333"));
        pressButton.setOnMouseEntered(ev -> pressButton.setStyle(buttonStyle("#444444")));
        pressButton.setOnMouseExited(ev -> pressButton.setStyle(buttonStyle("#333333")));
        pressButton.setDisable(!goal.getTracker().canPress());

        if (goal.getTracker().isComplete()) {
            pressButton.setDisable(true);
            timerLabel.setText("Goal completed 🎉");
        }
        streakLabel.setFont(Font.font("Segoe UI", 15));
        streakLabel.setTextFill(Color.web(TEXT_DIM));

        ProgressBar progressBar = new ProgressBar(goal.getTracker().getProgress());
        progressBar.setStyle("-fx-accent: #4cd137;");

        // -------------------------------------------------------------
        // MOTIVATION LINE + THREE DOT ANIMATION
        // -------------------------------------------------------------
        Label motivationLabel = new Label(getRandomMotivation());
        motivationLabel.setFont(Font.font("Segoe UI", 14));
        motivationLabel.setWrapText(true);
        motivationLabel.setTextFill(Color.web("#AAAAAA"));

        Label dotsLabel = new Label("");
        dotsLabel.setFont(Font.font("Segoe UI", 14));
        dotsLabel.setTextFill(Color.web("#888888"));

        HBox motivationBox = new HBox(5, motivationLabel, dotsLabel);

        // refresh motivation every 30 seconds
        AnimationTimer motivationTimer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate >= 30_000_000_000L) {
                    motivationLabel.setText(getRandomMotivation());
                    lastUpdate = now;
                }
            }
        };
        motivationTimer.start();

        // dot animation loop
        AnimationTimer dotTimer = new AnimationTimer() {
            private long lastDot = 0;
            private int state = 0;

            @Override
            public void handle(long now) {
                if (now - lastDot >= 500_000_000L) {
                    state = (state + 1) % 4;
                    switch (state) {
                        case 0 -> dotsLabel.setText("");
                        case 1 -> dotsLabel.setText(".");
                        case 2 -> dotsLabel.setText("..");
                        case 3 -> dotsLabel.setText("...");
                    }
                    lastDot = now;
                }
            }
        };
        dotTimer.start();

        // -------------------------------------------------------------
        // BUTTON + TIMER
        // -------------------------------------------------------------

        pressButton.setOnAction(ev -> {
            goal.getTracker().recordPress();

            streakLabel.setText("Streak: " + goal.getTracker().getStreak() + " / " + goal.getTracker().getTargetDays());
            progressBar.setProgress(goal.getTracker().getProgress());
            pressButton.setDisable(true);
            DataStore.save(goals);
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long millisLeft = goal.getTracker().millisUntilNextPress();
                if (goal.getTracker().isComplete()) {
                    timerLabel.setText("Goal completed 🎉");
                    pressButton.setDisable(true);
                } else if (millisLeft > 0) {
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

        VBox box = new VBox(10, descLabel, streakLabel, progressBar, timerLabel, pressButton, motivationBox);
        box.setPadding(new Insets(15));
        box.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-color: #2A2A2A;" +
                        "-fx-border-width: 2;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 8, 0.4, 0, 0);"
        );

        box.setOnMouseEntered(e -> {
            box.setScaleX(1.02);
            box.setScaleY(1.02);
        });
        box.setOnMouseExited(e -> {
            box.setScaleX(1.0);
            box.setScaleY(1.0);
        });

        return box;
    }

    private String getRandomMotivation() {
        return MOTIVATION_LINES.get(random.nextInt(MOTIVATION_LINES.size()));
    }

    private String buttonStyle(String color) {
        return String.format(
                "-fx-font-size: 16px;" +
                        "-fx-background-color: %s;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-family: 'Segoe UI';" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;", color);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
