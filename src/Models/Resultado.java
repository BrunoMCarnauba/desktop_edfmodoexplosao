/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.sql.Timestamp;

/**
 *
 * @author Nucleo de robótica - Cesmac
 */
public class Resultado {
    
    private String participante;
    private Timestamp dataEHora;
    private int tempoTotal[];   //Horas, minutos, segundos e centésimos.
    
    //Construtores
    public Resultado(){
        dataEHora = new Timestamp(System.currentTimeMillis());  //Pega a data e horário atual da máquina
    }
    
    //Getters and Setters
    
    public String getParticipante() {
        return participante;
    }

    public void setParticipante(String participante) {
        this.participante = participante;
    }
    
    public Timestamp getDataEHora() {
        return dataEHora;
    }
    
    public int[] getTempoTotal() {  //Retorna o vetor com os valores inteiros
        return tempoTotal;
    }
    
    public String getTempoTotalString(){  //Retorna uma String montada apartir dos valores do vetor
        String textoTempoTotal = "";
        if(this.tempoTotal.length > 0){ //Se não estiver vazio
            textoTempoTotal = transformarTempoString(this.tempoTotal[0],this.tempoTotal[1],this.tempoTotal[2],this.tempoTotal[3]);
        }
        return textoTempoTotal;
    }

    public void setTempoTotal(int tempoTotal[]){
        this.tempoTotal = tempoTotal;
    }
    
    public void setTempoTotal(int horas, int minutos, int segundos, int centesimos) {
        System.out.println("Registrando tempo total... Método: setTempoTotal de Participante");
        tempoTotal = new int[4]; // alocação de espaço para vetor
        tempoTotal[0] = horas;
        tempoTotal[1] = minutos;
        tempoTotal[2] = segundos;
        tempoTotal[3] = centesimos;
        this.tempoTotal = tempoTotal;
    }
    
    //Métodos
    
    protected String transformarTempoString(int horas, int minutos, int segundos, int centesimos){
        //Organiza a string para ter sempre 2 dígitos a cada ":"
        String tempo = "";
        String strHoras = "", strMinutos = "", strSegundos = "", strCentesimos = "";
        if (centesimos < 10) { 
            strCentesimos = ":0"+centesimos;
        } else{
            strCentesimos = ":"+centesimos; /*centesimas = "0"+centesimas;*/ 
        }
                        
        if (segundos < 10) {
            strSegundos = ":0"+segundos; //segundos = "0"+segundos;
        } else {
            strSegundos = (":"+segundos);
        }

        if (minutos < 10) {
            strMinutos = ":0"+minutos; //minutos = "0"+minutos;
        } else {
            strMinutos = ":"+minutos;
        }
        
        if (horas < 10) { 
            strHoras = "0"+horas; //horas = "0"+horas;
        } else {
            strHoras = ""+horas;
        }
        
        tempo = strHoras+strMinutos+strSegundos+strCentesimos;
        return tempo;
    }
    
    /**
     * Converte o vetor de tempo [centesimos, segundos, minutos, horas] em apenas centesimos.
     * @param tempo
     */
    protected int converterHorarioParaCentesimos(int tempo[]){
        System.out.println("Convertendo horário (Horas:Minutos:Segundos:Centesimos) para apenas centésimos. Método: converterHorarioParaCentesimos da classe Resultado.");
        int horasParaCentesimos = tempo[0]*360000;
        int minutosParaCentesimos = tempo[1]*6000;
        int segundosParaCentesimos = tempo[2]*100;
        int centesimos = tempo[3] + segundosParaCentesimos + minutosParaCentesimos + horasParaCentesimos;
        return centesimos;
    }
    
    /**
     * Ajusta o tempo recebido em centesimos em [centesimos, segundos, minutos, horas].
     * @param centesimos
     */
    protected int[] ajustarTempo(int centesimos){    //Ajusta de apenas centesimos para horas, minutos, segundos e centesimos
        System.out.println("Ajustando tempo... De apenas centesimos para horas, minutos, segundos e centesimos. Método AjustarTempo da classe Resultado.");
        int segundos = 0,minutos = 0,horas = 0;
        while(centesimos > 99){
            if(centesimos>99){
                centesimos = centesimos - 100;
                segundos++;
            }
            
            if(segundos>59){
                segundos = segundos - 60;
                minutos++;
            }
            
            if(minutos>59){
                minutos = minutos - 60;
                horas++;
            }
        }
        
        int tempoAjustado[] = new int[4];
        tempoAjustado[0] = horas;
        tempoAjustado[1] = minutos;
        tempoAjustado[2] = segundos;
        tempoAjustado[3] = centesimos;
        
        return tempoAjustado;
    }
    
    /**
     * Faz a conversão dos tempos [centesimos, segundos, minutos e horas] para centésimos e então calcula a diferença entre os dois horários (tempoMenor - tempoMaior)
     * @param tempoMenor
     * @param tempoMaior
     */
    protected int[] diferencaEntreHorario(int tempoMenor[], int tempoMaior[]){
        int centesimosTempoMenor = this.converterHorarioParaCentesimos(tempoMenor);
        int centesimosTempoMaior = this.converterHorarioParaCentesimos(tempoMaior);
        System.out.println("Calculando diferença entre horários em centésimos. Método: diferençaEntreHorario da classe Resultado.");
        int tempoDiferenca[] = ajustarTempo((centesimosTempoMaior - centesimosTempoMenor));
        return tempoDiferenca;
    }
    
    /**
     * Calcula a velocidade média (velocidadeMedia = segundos/metros)
     * @param tempoTotal
     * @param distanciaEmMetros
     */
    protected double calcularVelocidadeMedia(int tempoTotal[], double distanciaEmMetros){
        System.out.println("Calculando velocidade média... Método: calcularVelocidadeMedia da classe Resultado.");
        int centesimosTempo = this.converterHorarioParaCentesimos(tempoTotal);
        int segundosTempo = 0;
        double velocidadeMedia = 0;
        if(centesimosTempo > 99){
            segundosTempo = centesimosTempo/100;
            velocidadeMedia = segundosTempo/distanciaEmMetros;
            return velocidadeMedia;
        } else {
            velocidadeMedia = 1/distanciaEmMetros;
        }
        
        return velocidadeMedia;
    }
    
}
