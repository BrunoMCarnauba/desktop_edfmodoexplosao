/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.ConexaoSerial;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Núcleo de robótica - Cesmac
 */
public class FXMLConexaoController implements Initializable {

    @FXML
    private Label lblPortaSerial;

    @FXML
    private ComboBox cbPortasSeriais;

    @FXML
    private Button btnConectar;  
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarPortas();   //Carrega as portas no combobox
    }    
    
    private void carregarPortas(){  //Preenche o combobox com as portas
        ArrayList<String> nomesPortas = ConexaoSerial.obterArrayPortas();
        for(String nomePorta: nomesPortas){
            cbPortasSeriais.getItems().add(nomePorta);
        }
    }
    
    @FXML
    void btnConectarPortaSerial(ActionEvent event) {
        boolean conexaoSerialStatus = ConexaoSerial.conectarSerial(cbPortasSeriais.getSelectionModel().getSelectedItem().toString());   //Chama método de conexão enviando a porta selecionada no combobox.
        
        if(conexaoSerialStatus == true){    //Se tiver efetuado a conexão
            System.out.println("Conectado. Iniciando tela do cronômetro... Método: btnConectarPortaSerial de FXMLConexaoController.");
            Conexao.getStage().close(); //Fecha a tela de conexão
            ModoExplosao telaModoExplosao = new ModoExplosao();
            try {
                telaModoExplosao.start(new Stage()); //Inicia a tela do modo escolhido
            } catch (Exception ex) {
                Logger.getLogger(FXMLConexaoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else { //Essa tela é usada apenas para conectar-se. Se na tentativa de conexão tiver recebido false, então provavelmente houve um erro e não conectou-se.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de conexão");
            alert.setHeaderText("Erro ao tentar conectar-se com a porta serial.");
            alert.setContentText("Houve um erro ao tentar conectar-se com a porta serial. Verifique se o dispositivo já não está sendo usado, ou se está tentando se conectar na porta serial correta.");
            alert.show();
        }
    }
    
}
