package fr.dupont;

/**
 * This class is used to print colored logs.
 */
public class ColorLogger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";

    /**
     * The log level with color setup in parameter.
     */
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

    /**
     * Print a message with a color.
     * @param level The log level.
     * @param message The message to print.
     */
    public void print(Level level, String message){
        System.out.println(level.color + message);
    }

}
