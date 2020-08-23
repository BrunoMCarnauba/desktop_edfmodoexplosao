/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Nucleo de robótica - Cesmac
 */
public class Tempo {

    private int centesimas = 0;
    private int segundos = 0;
    private int minutos = 0;
    private int horas = 0;

    private String txtCentesimas = ":00";
    private String txtSegundos = ":00";
    private String txtMinutos = ":00";
    private String txtHoras = "00";
    
    private String tempoString = "00:00:00:00";

    public int getCentesimas() {
        return centesimas;
    }

    public void setCentesimas(int centesimas) {
        this.centesimas = centesimas;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }

    public String getTempoString() {
        return tempoString;
    }

    public void setTempoString(String tempoString) {
        this.tempoString = tempoString;
    }

    /*
     * Colocar essa função dentro de um timer: timer.scheduleAtFixedRate(new TimerTask(){ @Override public void run(){ Platform.runLater(() -> { //AQUI }); } }, 0, 10); 
     * Adaptação de: https://codepen.io/Romlonix/pen/Fwsza (JavaScript) com ajuda de: http://www.guj.com.br/t/cronometro-em-java/129367/5 e https://youtu.be/Vb-Qbz8dT0E
     * https://stackoverflow.com/questions/26916640/javafx-not-on-fx-application-thread-when-using-timer
     */
    public String passou10Milissegundos() {
        try {
            if (this.centesimas < 99) {
                this.centesimas++;
                if (this.centesimas < 10) {
                    this.txtCentesimas = ":0"+this.centesimas;
                } else {
                    this.txtCentesimas = ":"+this.centesimas;
                    /*centesimas = "0"+centesimas;*/
                }
            }
            if (this.centesimas == 99) {
                this.centesimas = -1;
            }
            if (this.centesimas == 0) {  //Quando alcançar 100 centésimos. Se centesimos for igual a 0 quer dizer que ele antes estava = -1, ou seja, entrou na condição de cima, e ao repetir, passou a ser = 0 (passou 100 centesimos. 1 Segundo.)
                this.segundos++;
                if (this.segundos < 10) {
                    this.txtSegundos = ":0"+this.segundos; //segundos = "0"+segundos;
                } else {
                    this.txtSegundos = ":"+this.segundos;
                }
            }
            if (this.segundos == 59) {
                this.segundos = -1;
            }
            if ((this.centesimas == 0) && (this.segundos == 0)) { //Se alcançar 60 segundos. Para isso, antes precisa o centesimo ter passado na condição (== 0) pra poder adicionar aos segundos.
                this.minutos++;
                if (this.minutos < 10) {
                    this.txtMinutos = ":0"+this.minutos; //minutos = "0"+minutos;
                } else {
                    this.txtMinutos = ":"+this.minutos;
                }
            }
            if (this.minutos == 59) {
                this.minutos = -1;
            }
            if ((this.centesimas == 0) && (this.segundos == 0) && (this.minutos == 0)) {
                this.horas++;
                if (this.horas < 10) {
                    this.txtHoras = "0"+this.horas; //horas = "0"+horas;
                } else {
                    this.txtHoras = ""+this.horas;
                }
            }
            
            this.tempoString = this.txtHoras+this.txtMinutos+this.txtSegundos+this.txtCentesimas;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return this.tempoString;
    }
    
    public String resetarValores(){
        this.centesimas = 0;
        this.segundos = 0;
        this.minutos = 0;
        this.horas = 0;

        this.txtCentesimas = ":00";
        this.txtSegundos = ":00";
        this.txtMinutos = ":00";
        this.txtHoras = "00";
        
        this.tempoString = "00:00:00:00";
        
        return tempoString;
    }

}
