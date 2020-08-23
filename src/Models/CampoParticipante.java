/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 *
 * @author Núcleo de robótica - Cesmac
 */
public class CampoParticipante {
    
    private VBox vBoxCampoParticipante;
    private TextField txtFieldNomePartipante;

    //Construtores

    public CampoParticipante() {
    }
    
    public CampoParticipante(VBox vBoxCampoParticipante, TextField txtFieldNomePartipante) {
        this.vBoxCampoParticipante = vBoxCampoParticipante;
        this.txtFieldNomePartipante = txtFieldNomePartipante;
    }

    //Getters and Setters
    
    public VBox getvBoxCampoParticipante() {
        return vBoxCampoParticipante;
    }

    public void setvBoxCampoParticipante(VBox vBoxCampoParticipante) {
        this.vBoxCampoParticipante = vBoxCampoParticipante;
    }

    public TextField getTxtFieldNomePartipante() {
        return txtFieldNomePartipante;
    }

    public void setTxtFieldNomePartipante(TextField txtFieldNomePartipante) {
        this.txtFieldNomePartipante = txtFieldNomePartipante;
    }

}
