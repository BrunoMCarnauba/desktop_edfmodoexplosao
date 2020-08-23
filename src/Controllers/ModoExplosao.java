/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import static Controllers.Conexao.setStage;
import Models.ConexaoSerial;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Núcleo de robótica - Cesmac
 */
public class ModoExplosao extends Application {
    //JAVAFX Tutorial: https://www.youtube.com/watch?v=P13ycvI-17Q&list=PLWd_VnthxxLejQ9CcHrsT5HCFn-10kquZ
    //Arduino + Java - Tutorial: https://youtu.be/AFUBjqdpK_4

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/ModoExplosao/FXMLModoExplosao.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Educação física - Modo explosão"); //https://stackoverflow.com/questions/29055792/javafx-window-settitle
        
        stage.show();
        setStage(stage);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ModoExplosao.stage = stage;
    }
    
}
