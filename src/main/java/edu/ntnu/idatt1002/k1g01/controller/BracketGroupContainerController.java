package edu.ntnu.idatt1002.k1g01.controller;

import edu.ntnu.idatt1002.k1g01.model.Group;
import edu.ntnu.idatt1002.k1g01.model.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import static javafx.geometry.Pos.CENTER;

public class BracketGroupContainerController implements Initializable {

    @FXML private VBox BracketGroupContainer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public HBox createTitleGroupHBox() {
        HBox hbox = new HBox();
        hbox.setStyle("-fx-background-color: gray");
        hbox.prefHeight(22.0);
        Label teams = new Label("Teams");
        teams.prefHeight(22.0);
        teams.setMinWidth((BracketGroupContainer.getPrefWidth()*60)/100);
        teams.setAlignment(CENTER);
        Label results = new Label("Points");
        results.prefHeight(22.0);
        results.setMinWidth((BracketGroupContainer.getPrefWidth()*40)/100);
        results.setAlignment(CENTER);
        results.setStyle("-fx-background-color: gray");
        hbox.getChildren().add(teams);
        hbox.getChildren().add(results);
        return hbox;
    }
    public void displayGroup(Group group) {
        VBox groupBox = new VBox();
        groupBox.setMaxWidth(420);
        groupBox.getChildren().add(createTitleGroupHBox());
        LinkedHashMap<Team,Integer> standings = group.getStanding(group.size());
        Set<Team> keySet = standings.keySet();
        ArrayList<Team> teams = new ArrayList<>(keySet.stream().collect(Collectors.toList()));
        int i = 1;
        for(Team team : teams){
            Label teamName = new Label(team.getName());
            Label points = new Label(String.valueOf(standings.get(team)));
            HBox hbox = new HBox();
            hbox.setPrefHeight(30.0);

            teamName.setPrefHeight(30.0);
            teamName.getStyleClass().add("label-groupStage");
            teamName.setPadding(new Insets(5,0,5,10));
            teamName.setAlignment(CENTER);
            teamName.setMinWidth((BracketGroupContainer.getPrefWidth()*60)/100);

            points.setPrefHeight(30.0);
            points.getStyleClass().add("label-groupStage");
            points.setPadding(new Insets(5,0,5,0));
            points.setAlignment(CENTER);
            points.setMinWidth((BracketGroupContainer.getPrefWidth()*40)/100);

            String background = null;
            if(i%2==0){
                background = "-fx-background-color: #ffffff";
            }else {
                background = "-fx-background-color: #f2f2f2";
            }
            hbox.setStyle(background);
            points.setStyle(background);
            hbox.getChildren().add(teamName);
            hbox.getChildren().add(points);
            groupBox.setPadding(new Insets(10,10,10,10));
            groupBox.getChildren().add(hbox);
            i++;
        }
        this.BracketGroupContainer.getChildren().add(groupBox);
    }

}
