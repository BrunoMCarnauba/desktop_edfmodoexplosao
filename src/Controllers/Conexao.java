/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

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
public class Conexao extends Application {
    
    private static Stage stage;
    
    //Tela para conectar-se com a porta serial.
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Views/FXMLConexao.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Efetuar conexão com o equipamento"); //https://stackoverflow.com/questions/29055792/javafx-window-settitle
        
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
        Conexao.stage = stage;
    }
    
    @Override
    public void stop(){ //Chamado ao fechar a aplicação no "X" - https://stackoverflow.com/questions/26619566/javafx-stage-close-handler
        //Também é chamado ao fechar o FXMLModoExplosao. Provavelmente por essa ser a classe da aplicação (a que é aberta primeiro).
        ConexaoSerial.desconectarSerial();
        System.out.println("Porta serial desconectada.");
    }
    
}
