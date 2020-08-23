/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.ModoExplosao;

import Models.Resultado;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Bruno
 */
public class ResultadoExplosao extends Resultado {
    
    private double velocidadeMedia;

    //Construtores
    public ResultadoExplosao(){
        super();    //a função super() chama o construtor da classe mãe
    }

    public ResultadoExplosao(double velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }
    
    //Getters and Setters
    public double getVelocidadeMedia() {
        return velocidadeMedia;
    }

    public void setVelocidadeMedia(int tempoTotal[], double distanciaEmMetros) {
        this.velocidadeMedia = calcularVelocidadeMedia(tempoTotal, distanciaEmMetros);
    }
    
    //Métodos
    //Obs.: Alguns métodos foram herdados da classe Resultado que está na pasta Models.
    
    @Override
    public String toString(){
        String velocidadeMedia = String.format("%.2f", getVelocidadeMedia());   //Para exibir apenas 2 casas após a virgula
        
        return "Participante: "+this.getParticipante()+"\n"
                + "Tempo total: "+getTempoTotalString()+"\n";
    }
    
    //Exportando dados para o excel: https://www.youtube.com/watch?v=ktwMW13FrQM - Com POI versão 3.13
    
    //Importar dados do excel: https://www.youtube.com/watch?v=qH5Yu34Unvg
    
}
