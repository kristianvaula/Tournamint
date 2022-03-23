package edu.ntnu.idatt1002.k1g01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    @Nested
    @DisplayName("Tests the constructor(s)")
    public class ConstructorTest{

        @Test
        @DisplayName("Tests initiation with correct name")
        public void InitiatesWithName(){
            String name = "Testname";

            Team team = new Team(name);

            assertEquals(team.getName(),name);
        }
    }

    @Nested
    @DisplayName("Tests Equals and HashCode methods")
    public class EqualsAndHashCode{

        @Test
        @DisplayName("Tests that two teams with same name are equal")
        public void TwoTeamsWithSameNameAreEqual(){
            String testName = "TestName";
            Team team1 = new Team(testName);
            Team team2 = new Team(testName);

            boolean equality = team1.equals(team2);

            assertTrue(equality);
        }

        @Test
        @DisplayName("Tests that two teams with different names are not equal")
        public void TwoTeamsWithDifferentNameAreNotEqual(){
            String testName = "TestName";
            String otherTestName = "otherTestName";
            Team team1 = new Team(testName);
            Team team2 = new Team(otherTestName);

            boolean equality = team1.equals(team2);

            assertFalse(equality);
        }

        @Test
        @DisplayName("Tests that two equal teams have the same hashcode")
        public void TwoEqualTeamsHaveTheSameHashCode(){
            String testName = "TestName";
            Team team1 = new Team(testName);
            Team team2 = new Team(testName);

            assertEquals(team1.hashCode(),team2.hashCode());
        }
    }
}
