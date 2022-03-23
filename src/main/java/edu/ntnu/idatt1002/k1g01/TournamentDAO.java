package edu.ntnu.idatt1002.k1g01;

import edu.ntnu.idatt1002.k1g01.model.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class TournamentDAO {
    private final String filePath;

    public TournamentDAO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves this tournament to file.
     */
    public void save(Tournament tournament) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tournament);
        }
        catch (FileNotFoundException fnfException) {
            throw new IOException("Could not save tournament to " + filePath + "  File not found!");
        }
    }
}
