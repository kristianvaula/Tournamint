package edu.ntnu.idatt1002.k1g01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    @Nested
    public class constructorTest{

        @Test
        @DisplayName("Initiates with correct name")
        public void InitiatesWithName(){
            String name = "Testname";

            Team team = new Team(name);

            assertEquals(team.getName(),name);
        }

    }
}
