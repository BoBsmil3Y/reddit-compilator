package fr.dupont;

import fr.dupont.localfiles.FolderUtils;

public class Main {
    private static final String OUTPUT_FOLDER = "./output/downloaded/";

    public static void main(String[] args) {

        ColorLogger logger = new ColorLogger();
        logger.print(ColorLogger.Level.LOG, "Starting >> Reddit compilator project rewritten in Java.");

        FolderUtils.createFolder(OUTPUT_FOLDER);

    }
}