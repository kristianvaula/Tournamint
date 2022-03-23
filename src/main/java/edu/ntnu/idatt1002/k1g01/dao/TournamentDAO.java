package edu.ntnu.idatt1002.k1g01.dao;
import edu.ntnu.idatt1002.k1g01.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 * Used for saving and loading a tournament type Object.
 * Stores path to target file as object variable.
 * Tournaments are saved as ".qxz" files. Dumb extension name, but definitely unique.
 * //TODO Possibly change extension to something pronounceable.
 * //TODO Possibly change file format to something more human readable.
 * @author Martin Dammerud
 */
public class TournamentDAO {
    private static final String fileExtension = ".qxz";
    private final String filePath;

    /**
     * Constructor for Data Access Object.
     * Adds extension automatically if missing in input
     * @param filePath path where file will be found. Ex: "C:\\users\\user\\documents\\My Tournaments\\summerFun"
     */
    public TournamentDAO(String filePath) {
        if (!filePath.endsWith(fileExtension)) {
            filePath += fileExtension;
        }
        this.filePath = filePath;
    }

    //Dumb getters
    public String getFilePath() { return filePath; }
    public static String getFileExtension() { return fileExtension; }

    /**
     * Checks if given .qxz file exists.
     * Automatically adds File extension if missing.
     * @param filePath Path to check.
     * @return true if file is found.
     */
    public static boolean fileExists(String filePath) {
        if (!filePath.endsWith(fileExtension)) {
            filePath += fileExtension;
        }
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    /**
     * Saves given tournament to file.
     * @param tournament of type Tournament.
     * @see Tournament
     * @throws IOException if saving failed.
     */
    public void save(Tournament tournament) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tournament);
        }
    }

    /**
     * Loads a tournament from file.
     * @return object of type Tournament.
     * @see Tournament
     * @throws IOException if Loading failed.
     */
    public Tournament load() throws IOException{
        try (FileInputStream fis = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Tournament)ois.readObject();
        }
        catch (ClassNotFoundException cnfException) {
            throw new IOException("Class not found: " + cnfException.getMessage());
        }
    }

    /**
     * Deletes the file that this DAO is connected to.
     * @throws IOException with explanation if deletion fails.
     */
    public void deleteSelf() throws IOException {
        Files.delete(Paths.get(filePath));
    }
}
