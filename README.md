BHQ
A minimalist habit and discipline tracker built in Java. Add goals, track your daily streaks, and let the app remove them automatically once you hit your target — no clutter, no noise.

Features

Goal tracking — add personal goals with a target streak count
Daily streaks — mark goals complete each day to build your streak
Auto-removal — once a goal's target streak is reached, it's cleared from the list automatically
Persistent data — all goals and streaks are saved to bhq_data.txt and restored on next launch
Distraction-free — intentionally minimal UI; the focus is on the goals, not the app

Stack
LayerTechLanguageJavaUIJavaFXPersistencePlain text file (bhq_data.txt)IDEIntelliJ IDEA

Setup
Requirements

Java 17+
IntelliJ IDEA (recommended)

Run
bashgit clone https://github.com/bardiae-hue/BHQ.git
cd BHQ
Open in IntelliJ and run Main.java, or from the command line:
bashjavac -sourcepath src -d out src/Main.java
java -cp out Main
Your data is saved automatically to bhq_data.txt in the project root.

Project structure
BHQ/
├── src/              # Java source files
├── bhq_data.txt      # Persistent goal and streak data
├── image.png         # App screenshot
├── BHQ.iml           # IntelliJ project file
└── README.md
