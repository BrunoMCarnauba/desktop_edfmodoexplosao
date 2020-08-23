/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

/**
 *
 * @author Núcleo de robótica - Cesmac
 */
public class ConexaoSerial {

    private static SerialPort portaSerial;
    private static boolean conexaoSerialStatus = false;

    /*
    * Getters and Setters
    */
    
    public static SerialPort getPortaSerial() {
        return portaSerial;
    }

    public static void setPortaSerial(SerialPort portaSerial) {
        ConexaoSerial.portaSerial = portaSerial;
    }

    public static boolean isConexaoSerialStatus() {
        return conexaoSerialStatus;
    }

    public static void setConexaoSerialStatus(boolean conexaoSerialStatus) {
        ConexaoSerial.conexaoSerialStatus = conexaoSerialStatus;
    }
    
    /*
    * Métodos
    */
    
    /**
     * Retorna um array com as portas seriais do computador que estão com um dispositivo conectado
     * @return portasSeriais: ArrayList<String>
     */
    public static ArrayList<String> obterArrayPortas(){
        //Tutorial: https://youtu.be/DQymC8J9gJU
        SerialPort[] portNames = SerialPort.getCommPorts();
        ArrayList<String> nomesPortas = new ArrayList<String>();
        
        //Identificar sistema operacional: https://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
        String sistemaOperacional = System.getProperty("os.name");
        System.out.println("Sistema operacional que está sendo usado = "+sistemaOperacional);
        
        for(SerialPort portName: portNames){
            if(!sistemaOperacional.startsWith("Mac")){
                nomesPortas.add(portName.getSystemPortName());
            }else{
                nomesPortas.add("/dev/"+portName.getSystemPortName());
            }
        }
        
        return nomesPortas;
    }
    
    /**
     * Conecta-se com a porta serial passada por parâmetro
     * https://youtu.be/0upnex1N3CA
     * @param portaSelecionada
     * @return conexaoSerialStatus: boolean
     */
    public static boolean conectarSerial(String portaSelecionada){
        if(conexaoSerialStatus == false){   //Se estiver desconectado - Conecta
            portaSerial = SerialPort.getCommPort(portaSelecionada);
            System.out.println(portaSerial);

            if(portaSerial.openPort()){ //Se conseguir abrir a porta
                conexaoSerialStatus = true;
            }
        } else {    //Se estiver conectado - Desconecta
            if(portaSerial.closePort()){ //Fecha a porta
                conexaoSerialStatus = false;
            }
        }

        return conexaoSerialStatus;
    }
    
    /**
     * Desconecta-se da porta serial
     * @return conexaoSerialStatus: boolean
     */
    public static boolean desconectarSerial(){ //Também é chamado na classe principal ao fechar a janela pelo "X".
        if(conexaoSerialStatus == true){
            portaSerial.closePort(); //Fecha a porta
            conexaoSerialStatus = false;
        }

        return conexaoSerialStatus;
    }
    
    /**
     * Desconecta e conecta na porta serial que já está conectada
     * @return 
     */
    public static boolean reiniciarConexao(){
        if(conexaoSerialStatus == true && portaSerial.closePort()){ //Se tiver conectado e ele conseguir fechar a porta
            conexaoSerialStatus = false;
        }
        
        //Dá um tempo para poder reconectar-se
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException ex) {
            System.out.println("Falha ao tentar pausar o processo entre a reconexão");
        }
        
        if(conexaoSerialStatus == false && portaSerial.openPort()){ //Se tiver desconectado e ele conseguir conectar-se
            conexaoSerialStatus = true;
        }
        
        return conexaoSerialStatus;
    }
    
    /**
     * Imprime um valor na porta serial conectada
     * @param mensagem 
     */
    public static void imprimirPortaSerial(String mensagem){
        //Tutorial - Impressão na porta serial: https://youtu.be/97Qj7uI_Lvc
        if(conexaoSerialStatus == true){
            PrintWriter output = new PrintWriter(portaSerial.getOutputStream());
            output.print(mensagem); //Imprime a mensagem na porta serial
            output.flush();
        }else{
            System.out.println("Você não está conectado na porta serial.");
        }
    }
    
    /**
     * Tenta ler o máximo de dados possíveis da porta serial, para limpar os buffers de hardware. Depois disso, pode chamar o lerPortaSerial().
     */
    public static void limparBuffer(){
        if(conexaoSerialStatus == true){
            InputStream comPortInput = portaSerial.getInputStream();
            
            try {
                while(comPortInput.available() > 0){
                    System.out.println(new Date(System.currentTimeMillis())+" - Ignorando leituras disponíveis da porta serial. Valor do byte read() = "+comPortInput.read());
                }
            } catch (IOException ex) {
                System.out.println("Erro ao ler porta serial. Verifique se o dispositivo está conectado na porta correta. - Erro: "+ex);
            }
        }else{
            System.out.println("Você não está conectado na porta serial.");
        }
    }
    
    
    /**
     * Ler a porta serial e se houver, retorna o character recebido.
     * (!!!) Use o método limparBuffer() antes de chamar esse método.
     */
    public static char lerPortaSerial(){ 
        int leituraByteASCII;
        char leituraChar = '\0';
        
        if(conexaoSerialStatus == true){
            InputStream comPortInput = portaSerial.getInputStream();
            
            try {
                if(comPortInput.available() > 0){
//                    System.out.println(new Date(System.currentTimeMillis())+" - comPortInput.available() = "+comPortInput.available());
                    leituraByteASCII = comPortInput.read(); //Faz a leitura e recebe o código ASCII do character;
                    leituraChar = (char)leituraByteASCII;   //Converte de ASCII para char
//                    System.out.println("Leu na porta serial o character = "+(char)leituraByteASCII);   //Imprime o que leu na porta serial. - Para ser feito testes.;
                }
            } catch (IOException ex) {
                System.out.println("Erro ao ler porta serial. Verifique se o dispositivo está conectado na porta correta. - Erro: "+ex);
            }
        }else{
            System.out.println("Você não está conectado na porta serial.");
        }
        
        return leituraChar;
    }
    
}
