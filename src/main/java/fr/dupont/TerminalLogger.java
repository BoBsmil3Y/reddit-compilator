package fr.dupont;

public class TerminalLogger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    public enum Level {
        INFO(ANSI_CYAN + "[Info] " + ANSI_RESET),
        SUCCESS(ANSI_GREEN + "[Success] " + ANSI_RESET),
        ERROR(ANSI_RED + "[Error] " + ANSI_RESET),
        WARNING(ANSI_YELLOW + "[Warning] " + ANSI_RESET),
        LOG(ANSI_PURPLE + "[Log] " + ANSI_RESET);

        public final String color;
        Level(String color) {
            this.color = color;
        }
    }

    public void print(Level level, String message){
        System.out.println(level.color + message);
    }

}
