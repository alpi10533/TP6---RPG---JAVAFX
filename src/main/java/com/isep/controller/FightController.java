package com.isep.controller;

import com.isep.MainApp;
import com.isep.model.History;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

/**
 * Class FightController
 */
public class FightController {

    //
    // @FXML Fields
    //

    // on y stockera un tableau d'historiques
    @FXML
    private TableView<History> historyTable;

    // on y stockera une colonne de contenus d'historiques
    @FXML
    private TableColumn<History, String> historyColumn;

    // variables nécessaires pour afficher les détails du combat
    @FXML
    private Label fightLabel;
    @FXML
    private Label nameHeroLabel;
    @FXML
    private Label typeHeroLabel;
    @FXML
    private Label lifePointsHeroLabel;
    @FXML
    private Label weaponDamagesHeroLabel;
    @FXML
    private Label foodsHeroLabel;
    @FXML
    private Label potionsHeroLabel;
    @FXML
    private Label nameEnemyLabel;
    @FXML
    private Label typeEnemyLabel;
    @FXML
    private Label lifePointsEnemyLabel;
    @FXML
    private Label weaponDamagesEnemyLabel;

    // variables nécessaires pour afficher les actions possibles du héro
    @FXML
    private Button buttonPlay;
    @FXML
    private Button buttonAttack;
    @FXML
    private Button buttonHeal;
    @FXML
    private Button buttonEat;
    @FXML
    private Button buttonEnd;

    //
    // Fields
    //

    // on y stockera les données de l'application
    private MainApp mainApp;

    // on y stockera l'index du héros combattant
    private int fightingHeroIndex;

    // on y stockera l'index de l'ennemi combattant
    private int fightingEnemyIndex;

    // on y stockera le joueur dont s'est le tour
    private int playerTurn; // 0 = le héro | 1 = l'ennemi

    // on y stockera si la partie semble terminée
    private int userShouldEnd; // 0 = non | 1 = oui, il a perdu | 2 = oui, il a gagné

    // on y stockera le numéro du tour
    private int round;

    //
    // @FXML Methods
    //

    // cette méthode est appelée automatiquement après le chargement de la vue (cf. "FightLayout")
    // elle permet l'initialisation des champs du tableau
    @FXML
    private void initialize() {
        historyColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
    }

    // cette méthode est également appelée automatiquement
    // elle permet d'écouter les 5 boutons (PLAY, ATTACK, EAT, HEAL, END) (cf. "FightLayout")
    @FXML
    void actionButtonEvent(ActionEvent event) throws IOException {
        if (event.getSource().equals(buttonPlay)) { // si le bouton PLAY est cliqué
            if (round != 0) { // si la partie a commencé
                if (userShouldEnd == 1) { // s'il a perdu
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(mainApp.getStage());
                    alert.setTitle("ERROR");
                    alert.setHeaderText("The fight is finished : you loose");
                    alert.setContentText("Please click on END !");
                    alert.showAndWait();
                } else if (userShouldEnd == 2) { // s'il a gagné
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(mainApp.getStage());
                    alert.setTitle("ERROR");
                    alert.setHeaderText("The fight is finished : you won");
                    alert.setContentText("Please click on END !");
                    alert.showAndWait();
                } else { // sinon
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initOwner(mainApp.getStage());
                    alert.setTitle("ERROR");
                    alert.setHeaderText("The fight has already started !");
                    alert.showAndWait();
                }
            } else { // si la partie n'a pas commencé
                launch(); // on lance le premier tour
            }
        } else if (event.getSource().equals(buttonAttack)) { // si le bouton ATTACK est cliqué
            if (round == 0) { // si la partie n'a pas commencé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight has not already started");
                alert.setContentText("Please click on PLAY !");
                alert.showAndWait();
            } else if (userShouldEnd == 1) { // s'il a perdu
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you loose");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (userShouldEnd == 2) { // s'il a gagné
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you won");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (playerTurn != 0) { // si ce n'est pas encore à son tour de jouer
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("It is not your turn yet !");
                alert.showAndWait();
            } else if (mainApp.getHeroData().size() != 0) { // s'il reste encore des héros
                if (mainApp.getHeroData().get(fightingHeroIndex).getLifePoints() <= 0) { // si le héros combattant n'a plus de points de vie
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " eliminated ..."));
                    mainApp.getHeroData().remove(fightingHeroIndex); // on supprime ce héro
                    if (mainApp.getHeroData().size() != 0) { // s'il reste encore d'autres héros
                        launch(); // on lance un autre tour
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                } else { // sinon
                    // le héros attaque
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " attacks ..."));
                    mainApp.getEnemyData().get(fightingEnemyIndex).deleteLifePoints(mainApp.getHeroData().get(fightingHeroIndex).attack());
                    update(); // on met à jour les données des combattants
                    playerTurn = 1; // c'est au tour de l'ennemi
                    if (mainApp.getEnemyData().size() != 0) { // s'il reste encore des ennemis
                        if (mainApp.getEnemyData().get(fightingEnemyIndex).getLifePoints() <= 0) { // si l'ennemi combattant n'a plus de points de vie
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " eliminated ..."));
                            mainApp.getEnemyData().remove(fightingEnemyIndex); // on supprime cet ennemi
                            getAward(); // le héro récupère sa récompense
                            if (mainApp.getEnemyData().size() != 0) { // s'il reste encore des ennemis
                                launch(); // on lance un autre tour
                            } else { // sinon
                                update(); // on met à jour les données des combattants
                            }
                        } else { // sinon
                            // l'ennemi attaque
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " attacks ..."));
                            mainApp.getHeroData().get(fightingHeroIndex).deleteLifePoints(mainApp.getEnemyData().get(fightingEnemyIndex).attack());
                            update(); // on met à jour les données des combattants
                            playerTurn = 0; // c'est au tour du héro
                        }
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                }
            } else { // sinon
                update(); // on met à jour les données des combattants
            }
        } else if (event.getSource().equals(buttonHeal)) { // si le bouton HEAL est cliqué
            if (round == 0) { // si la partie n'a pas commencé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight has not already started");
                alert.setContentText("Please click on PLAY !");
                alert.showAndWait();
            } else if (userShouldEnd == 1) { // s'il a perdu
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you loose");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (userShouldEnd == 2) { // s'il a gagné
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you won");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (playerTurn != 0) { // si ce n'est pas encore à son tour de jouer
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("It is not your turn yet !");
                alert.showAndWait();
            } else if (mainApp.getHeroData().get(fightingHeroIndex).getSizeOfPotions() == 0){ // s'il n'a pas de potion
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("You don't have any potions");
                alert.setContentText("Please chose an other option !");
                alert.showAndWait();
            } else if (mainApp.getHeroData().size() != 0) { // s'il reste des héros
                if (mainApp.getHeroData().get(fightingHeroIndex).getLifePoints() <= 0) { // si le héro combattant n'a plus de points de vie
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " eliminated ..."));
                    mainApp.getHeroData().remove(fightingHeroIndex); // on supprime ce héro
                    if (mainApp.getHeroData().size() != 0) { // s'il reste encore d'autres héros
                        launch(); // on lance un autre tour
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                } else { // sinon
                    // le héro se heal
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " heals ..."));
                    mainApp.getHeroData().get(fightingHeroIndex).heal();
                    update(); // on met à jour les données des combattants
                    playerTurn = 1; // c'est au tour de l'ennemi
                    if (mainApp.getEnemyData().size() != 0) { // s'il reste des ennemis
                        if (mainApp.getEnemyData().get(fightingEnemyIndex).getLifePoints() <= 0) { //  si l'ennemi combattant n'a plus de points de vie
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " eliminated ..."));
                            mainApp.getEnemyData().remove(fightingEnemyIndex); // on supprime cet ennemi
                            getAward(); // le héro récupère sa récompense
                            if (mainApp.getEnemyData().size() != 0) { // s'il reste encore des ennemis
                                launch(); // on lance un autre tour
                            } else {
                                update(); // on met à jour les données des combattants
                            }
                        } else { // sinon
                            // l'ennemi attaque
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " attacks ..."));
                            mainApp.getHeroData().get(fightingHeroIndex).deleteLifePoints(mainApp.getEnemyData().get(fightingEnemyIndex).attack());
                            update(); // on met à jour les données des combattants
                            playerTurn = 0; // c'est au tour du héro
                        }
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                }
            } else { // sinon
                update(); // on met à jour les données des combattants
            }
        } else if (event.getSource().equals(buttonEat)) { // si le bouton EAT est cliqué
            if (round == 0) { // si la partie n'a pas commencé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight has not already started");
                alert.setContentText("Please click on PLAY !");
                alert.showAndWait();
            } else if (userShouldEnd == 1) { // s'il a perdu
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you loose");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (userShouldEnd == 2) { // s'il a gagné
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("The fight is finished : you won");
                alert.setContentText("Please click on END !");
                alert.showAndWait();
            } else if (playerTurn != 0) { // si ce n'est pas encore à son tour de jouer
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("It is not your turn yet !");
                alert.showAndWait();
            } else if (mainApp.getHeroData().get(fightingHeroIndex).getSizeOfFoods() == 0){ // s'il n'a pas de food
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(mainApp.getStage());
                alert.setTitle("ERROR");
                alert.setHeaderText("You don't have any foods");
                alert.setContentText("Please chose an other option !");
                alert.showAndWait();
            } else if (mainApp.getHeroData().size() != 0) { // s'il reste des héros
                if (mainApp.getHeroData().get(fightingHeroIndex).getLifePoints() <= 0) { // si le héro combattant n'a plus de points de vie
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " eliminated ..."));
                    mainApp.getHeroData().remove(fightingHeroIndex); // on supprime cet héro
                    if (mainApp.getHeroData().size() != 0) { // s'il reste encore des héros
                        launch(); // on lance un autre tour
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                } else { // sinon
                    // le héro eat
                    mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " heals ..."));
                    mainApp.getHeroData().get(fightingHeroIndex).eat();
                    update(); // on met à jour les données des combattants
                    playerTurn = 1; // c'est au tour de l'ennemi
                    if (mainApp.getEnemyData().size() != 0) { // s'il reste des ennemis
                        if (mainApp.getEnemyData().get(fightingEnemyIndex).getLifePoints() <= 0) { // si l'ennemi combattant n'a plus de points de vie
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " eliminated ..."));
                            mainApp.getEnemyData().remove(fightingEnemyIndex); // on supprime cet ennemi
                            getAward(); // le héro récupère sa récompense
                            if (mainApp.getEnemyData().size() != 0) { // s'il reste encore des ennemis
                                launch(); // on lance un autre tour
                            } else { // sinon
                                update(); // on met à jour les données des combattants
                            }
                        } else { // sinon
                            // l'ennemi attaque
                            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " attacks ..."));
                            mainApp.getHeroData().get(fightingHeroIndex).deleteLifePoints(mainApp.getEnemyData().get(fightingEnemyIndex).attack());
                            update(); // on met à jour les données des combattants
                            playerTurn = 0; // c'est au tour du héro
                        }
                    } else { // sinon
                        update(); // on met à jour les données des combattants
                    }
                }
            } else { // sinon
                update(); // on met à jour les données des combattants
            }
        } else if (event.getSource().equals(buttonEnd)){ // si le bouton END est cliqué
            System.exit(0); // on ferme l'application
        }
    }

    //
    // Methods
    //

    // cette méthode est appelée dans le "MainApp" pour récupérer les données importantes de l'application (notamment "heroData", "enemyData" et historyData")
    // elle permet également l'initialisation de plusieurs variables
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        round = 0;
        userShouldEnd = 0;
        historyTable.setItems(mainApp.getHistoryData());
    }

    // cette méthode est appelée à chaque nouveau combat
    public void launch() {
        round++;
        generateCombat();
        update();
        mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " against " + mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " ..."));
        if (playerTurn == 0) { // le héros commence
            mainApp.getHistoryData().add(new History(mainApp.getHeroData().get(fightingHeroIndex).getName() + " starts ..."));
        } else if (playerTurn == 1) { // l'énemi commence
            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " starts ..."));
            mainApp.getHistoryData().add(new History(mainApp.getEnemyData().get(fightingEnemyIndex).getName() + " attacks ..."));
            mainApp.getHeroData().get(fightingHeroIndex).deleteLifePoints(mainApp.getEnemyData().get(fightingEnemyIndex).attack());
            playerTurn = 0;
            update();
        }
    }

    // cette méthode permet la génération aléatoire de dux combattants : un héro et un ennemi
    public void generateCombat() {
        playerTurn = (int) (Math.random() * 2);
        fightingHeroIndex = (int)(Math.random() * mainApp.getHeroData().size());
        fightingEnemyIndex = (int)(Math.random() * mainApp.getEnemyData().size());
    }

    // cette méthode permet de synchroniser la vue et les données de l'application
    public void update() {
        if (mainApp.getHeroData().size() == 0 || mainApp.getEnemyData().size() == 0) { // s'il n'y a plus héro ou d'ennemi
            if (mainApp.getHeroData().size() == 0) { // s'il n'y a plus de héro
                nameHeroLabel.setText("");
                typeHeroLabel.setText("");
                lifePointsHeroLabel.setText("");
                weaponDamagesHeroLabel.setText("");
                foodsHeroLabel.setText("");
                potionsHeroLabel.setText("");
                nameEnemyLabel.setText(mainApp.getEnemyData().get(fightingEnemyIndex).getName());
                typeEnemyLabel.setText(mainApp.getEnemyData().get(fightingEnemyIndex).getType());
                lifePointsEnemyLabel.setText(Integer.toString(mainApp.getEnemyData().get(fightingEnemyIndex).getLifePoints()));
                weaponDamagesEnemyLabel.setText(Integer.toString(mainApp.getEnemyData().get(fightingEnemyIndex).getWeaponDamages()));
                fightLabel.setText("YOU LOSE");
                userShouldEnd = 1;
            } else if (mainApp.getEnemyData().size() == 0) { // s'il n'a plus d'ennemi
                nameHeroLabel.setText(mainApp.getHeroData().get(fightingHeroIndex).getName());
                typeHeroLabel.setText(mainApp.getHeroData().get(fightingHeroIndex).getType());
                lifePointsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getLifePoints()));
                weaponDamagesHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getWeaponDamages()));
                foodsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getSizeOfFoods()));
                potionsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getSizeOfPotions()));
                nameEnemyLabel.setText("");
                typeEnemyLabel.setText("");
                lifePointsEnemyLabel.setText("");
                weaponDamagesEnemyLabel.setText("");
                fightLabel.setText("YOU WIN");
                userShouldEnd = 2;
            }
        } else { // sinon
            nameHeroLabel.setText(mainApp.getHeroData().get(fightingHeroIndex).getName());
            typeHeroLabel.setText(mainApp.getHeroData().get(fightingHeroIndex).getType());
            lifePointsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getLifePoints()));
            weaponDamagesHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getWeaponDamages()));
            foodsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getSizeOfFoods()));
            potionsHeroLabel.setText(Integer.toString(mainApp.getHeroData().get(fightingHeroIndex).getSizeOfPotions()));
            nameEnemyLabel.setText(mainApp.getEnemyData().get(fightingEnemyIndex).getName());
            typeEnemyLabel.setText(mainApp.getEnemyData().get(fightingEnemyIndex).getType());
            lifePointsEnemyLabel.setText(Integer.toString(mainApp.getEnemyData().get(fightingEnemyIndex).getLifePoints()));
            weaponDamagesEnemyLabel.setText(Integer.toString(mainApp.getEnemyData().get(fightingEnemyIndex).getWeaponDamages()));
            fightLabel.setText("FIGHT N°" + round);
        }
    }

    // cette méthode est appelée automatiquement lorsqu'un héro gagne un combat
    // elle permet au héro de récupérer sa récompense
    private void getAward() {
        mainApp.showAwardEditDialog(mainApp.getHeroData().get(fightingHeroIndex));
        update();
    }

}