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
 *
 * //TODO Possibly change extension to something pronounceable.
 * //TODO Possibly change file format to something more human readable.
 * @author Martin Dammerud
 */
public class TournamentDAO {
    private static final String fileExtension = ".qxz";
    //private final String filePath;
    private final File file;
    private Tournament tournament = null;

    /**
     * Constructor for Data Access Object.
     * Adds extension automatically if missing in input
     * @param file Object representing file where data will be saved.
     */
    public TournamentDAO(File file) {
        this.file = file;
    }

    /**
     * @return the path to the file.
     */
    public String getFilePath() { return file.getPath(); }

    /**
     * @return the extension of tournamint files as a String.
     */
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
        System.out.println("Saved tournament: " + tournament.getTournamentName() + " to: " + file.getPath());
        this.tournament = tournament;
        try (FileOutputStream fos = new FileOutputStream(file.getPath());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tournament);
        }
    }

    /**
     * Saves previously given or loaded tournament to file.
     * Intended to easily save changes in state for a tournament object.
     * @see Tournament
     * @throws IOException if saving failed.
     * @throws NullPointerException if this DAO has never previously loaded or been given a Tournament.
     */
    public void save() throws IOException, NullPointerException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tournament);
            System.out.println("Saved tournament: " + tournament.getTournamentName() + " to: " + file.getPath());
        }
    }

    /**
     * Loads a tournament from file if needed. Loads from memory if possible.
     * @return object of type Tournament.
     * @see Tournament
     * @throws IOException if Loading failed.
     */
    public Tournament load() throws IOException{
        if (tournament != null) { return this.tournament; } //Data in memory is up-to-date. No load from file needed.
        System.out.println("loading tournament from: " +file.getPath());
        try (FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            this.tournament = (Tournament)ois.readObject();
            return tournament;
        }
        catch (ClassNotFoundException cnfException) {
            throw new IOException("Class not found: " + cnfException.getMessage());
        }
    }

    /**
     * Deletes the file that this DAO is connected to.
     * Does not reset this.tournament pointer.
     * @throws IOException with explanation if deletion fails.
     */
    public void deleteFile() throws IOException {
        if(!file.delete()) {
            throw new IOException("Failed to delete: " + file.getPath());
        }
    }
}
