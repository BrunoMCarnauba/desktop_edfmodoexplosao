/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.ConexaoSerial;
import Models.ModoExplosao.ResultadoExplosao;
import Models.Tempo;
import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Nucleo de robótica - Cesmac
 */
public class FXMLModoExplosaoController implements Initializable {

    @FXML
    private MenuItem btnDesconectar;
    
    @FXML
    private MenuItem btnSobre;
    
    @FXML
    private Text txtParticipanteAtual;
    
    @FXML
    private Text txtCronometro;
    
    @FXML
    private Button btnIniciar;

    @FXML
    private Button btnParar;

    @FXML
    private Button btnResumir;

    @FXML
    private Button btnResetar;

    @FXML
    private Button btnVoltarParticipante;

    @FXML
    private Button btnProximoParticipante;
    
    @FXML
    private Button btnDeletarUltimoResultado;

    @FXML
    private Button btnResetarLista;
    
    @FXML
    private Text txtTeste;
    
    Timer timer;
    private Tempo tempoCronometro = new Tempo();  //Tempo (centesimas, segundos, minutos e horas) do corredor - Usado pelo timer para cronometrar o tempo
    private boolean statusCronometro = false;
    
    //LISTA - TESTES
    //https://youtu.be/__SbLOBxzb0
    @FXML
    private ListView<ResultadoExplosao> lvResultados;
    private ResultadoExplosao resultado;
    private int ordemLista = 1;
    private List<ResultadoExplosao> resultados = new ArrayList<>();
    private ObservableList<ResultadoExplosao> obsResultados;
    
    /**
     * Métodos que rodam ao inicializar a tela
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Preparando texts e botões, conferindo se já houve cadastrado efetuado ou não.
        atualizarTextsParticipantes();   //Altera Texts com participante atual e próximo participante a correr
        carregarAudioRAM();
        btnProximoParticipante.setDisable(false);
        btnDeletarUltimoResultado.setDisable(false);
        btnResetarLista.setDisable(false);
    }
    
    @FXML
    private void btnDesconectarPortaSerial(ActionEvent event){
        removerAudioRAM();
        ModoExplosao.getStage().close(); //Fecha essa tela (do cronômetro).
        ConexaoSerial.desconectarSerial();  //Desconecta-se
        Conexao telaConexao = new Conexao();
        try {
            telaConexao.start(new Stage()); //Inicia a tela de conexão
        } catch (Exception ex) {
            Logger.getLogger(FXMLModoExplosaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void btnIniciarAplicacao(ActionEvent event) {
        iniciarAplicacao();
    }
   

    @FXML
    void btnTelaSobre(ActionEvent event) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sobre");
            alert.setHeaderText("Sobre a aplicação e desenvolvedores");
            alert.setContentText("Desenvolvido pelo núcleo de robótica do cesmac em parceria com o curso de educação física. - Instagram: @roboticacesmac");
            alert.show();
    }
    
    @FXML
    void btnVoltarParticipante(ActionEvent event) {
        if((ordemLista-1) < 1){   //TESTE
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operação inválida!");
            alert.setHeaderText("Não há participante antes dessa posição.");
            alert.setContentText("Se deseja reiniciar o exercício aperte em resetar lista.");
            alert.show();
        } else {
            ordemLista--;
            atualizarTextsParticipantes();
            atualizarBotaoParticipanteAnterior();
            resetarCronometro();
        }
    }

    @FXML
    void btnProximoParticipante(ActionEvent event){
        ordemLista++;
        atualizarTextsParticipantes();
        btnVoltarParticipante.setDisable(false);
        resetarCronometro();
    }
    
    private void iniciarAplicacao(){
        //Envia comando para o arduino (Imprime na porta serial) - https://youtu.be/97Qj7uI_Lvc
        ConexaoSerial.imprimirPortaSerial("1"); //Imprime 1 na porta serial
        
        //Configura e inicia o cronometro. O cronometro inicia a leitura do serial.
        btnIniciar.setDisable(true);
        btnParar.setDisable(false);
        btnResumir.setDisable(true);
        btnResetar.setDisable(false);
        btnVoltarParticipante.setDisable(true);
        btnProximoParticipante.setDisable(true);
        
        //Cria um novo objeto resultado onde serão colocados os tempos
        resultado = new ResultadoExplosao();
        
        ConexaoSerial.limparBuffer();
        
        //A sirene toca durante 3 segundos antes de iniciar o cronômetro
        txtTeste.setText("Aguardando sirene");
        tocarBuzina();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            System.out.println("Falha ao tentar contar 3 segundos para iniciar o cronômetro.");
        }
        
        txtTeste.setText("Cronômetro iniciado");

        statusCronometro = true;
        cronometro();
    }
    
    
    private Clip audio; 
    private AudioInputStream audioInputStream;
    private void carregarAudioRAM(){
        //https://www.geeksforgeeks.org/play-audio-file-using-java/ e https://stackoverflow.com/questions/21874361/clip-plays-wav-file-with-bad-lag-in-java e se necessário https://stackoverflow.com/questions/557903/how-can-i-wait-for-a-java-sound-clip-to-finish-playing-back
        try{
            //read audio data from whatever source (file/classloader/etc.)
            InputStream audioSrc = getClass().getResourceAsStream("/Sons/Contador3Sec.wav");
            //add buffer for mark/reset support
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            //create AudioInputStream object 
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
            
            // create clip reference 
            audio = AudioSystem.getClip(); 
            // open audioInputStream to the clip 
            audio.open(audioInputStream); 
            
            
            //Foi percebido que na primeira vez de execução estava dando delay e nas próximas estava rodando normal. Então, foi simulado um start logo no começo para depois não ter mais delay
            FloatControl gainControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gainControl.getMinimum()); // Reduce volume
            
            audio.start();
            
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException ex) {
                System.out.println("Falha ao tentar contar meio segundo para parar o audio de preparação.");
            }
            
            audio.stop();
            
            gainControl.setValue(gainControl.getMaximum());
            
            System.out.println("Audio preparado! O audio foi carregado na memória RAM. Método: carregarAudioRAM() de FXMLModoExplosaoController.");
        }catch(Exception ex){
            System.out.println("Erro ao tentar carregar audio na memória RAM.");
            ex.printStackTrace();
        }
    }
    
    private void tocarBuzina(){
        try{
            //põe a posição do cursor para o início do audio
            audio.setMicrosecondPosition(0);
            //start the clip 
            audio.start();
        }catch(Exception ex){
            System.out.println("Não conseguiu reproduzir o audio - Talvez não esteja carregado na memória. Método: tocarBuzina() de FXMLModoExplosaoController");
        }
    }
    
    private void pararAudio(){
        try{
            audio.stop();
        }catch(Exception ex){
            System.out.println("Não conseguiu parar o audio - Talvez não esteja carregado na memória. Método: tocarBuzina() de FXMLModoExplosaoController");
        }
    }
    
    private void removerAudioRAM(){
        //https://stackoverflow.com/questions/2792977/do-i-need-to-close-an-audio-clip
        try{
            audio.close();
        }catch(Exception ex){
            System.out.println("Não conseguiu remover o audio da memória RAM. Talvez ele não esteja lá. Método: removerAudioRAM() de FXMLModoExplosaoController");
        }
    }
    
    @FXML
    private void btnPararCronometro(ActionEvent event) {
        pararCronometro();
    }

    private void pararCronometro(){
        //Para o cronômetro
        //https://stackoverflow.com/questions/1409116/how-to-stop-the-task-scheduled-in-java-util-timer-class
        timer.cancel();
        timer.purge();
        statusCronometro = false;
        pararAudio();
        
        btnParar.setDisable(true);
        btnResumir.setDisable(false);
        
        ConexaoSerial.imprimirPortaSerial("0"); //Envia 0 para a porta serial.
        txtTeste.setText("Cronômetro parado");    //Teste
        
        carregarResultados();   //Carrega resultados no objeto participante
  
        atualizarBotaoParticipanteAnterior();
        btnProximoParticipante.setDisable(false);
    }
    
    @FXML
    private void btnResumirCronometro(ActionEvent event) {
        btnIniciar.setDisable(true);
        btnParar.setDisable(false);
        btnResumir.setDisable(true);
        btnResetar.setDisable(false);
        
        ConexaoSerial.limparBuffer();
        
        //A sirene toca durante 3 segundos antes de iniciar o cronômetro
        txtTeste.setText("Aguardando sirene");
        tocarBuzina();
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            System.out.println("Falha ao tentar contar 3 segundos para iniciar o cronômetro.");
        }
        
        txtTeste.setText("Cronômetro iniciado");

        statusCronometro = true;
        
        cronometro();
    }
    
    private void resetarCronometro(){
        //Para o timer (para o cronômetro) e configura os botões e números do cronômetro
        //https://stackoverflow.com/questions/1409116/how-to-stop-the-task-scheduled-in-java-util-timer-class
        timer.cancel();
        timer.purge();
        
        tempoCronometro.resetarValores();
        
        txtCronometro.setText("00:00:00:00");
        
        btnParar.setDisable(true);
        btnResumir.setDisable(true);
        btnResetar.setDisable(true);
        
        //ConexaoSerial.reiniciarConexao(); //Tentativa de evitar falhas no inicio do cronometro
        
        btnIniciar.setDisable(false);
    }
    
    @FXML
    private void btnResetarCronometro(ActionEvent event) {
        resetarCronometro();
    }
    
    private void cronometro(){
        //Adaptação de: https://codepen.io/Romlonix/pen/Fwsza (JavaScript) com ajuda de: http://www.guj.com.br/t/cronometro-em-java/129367/5 e https://youtu.be/Vb-Qbz8dT0E
        //https://stackoverflow.com/questions/26916640/javafx-not-on-fx-application-thread-when-using-timer
        //Inicia o cronometro
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    txtCronometro.setText(tempoCronometro.passou10Milissegundos());
                    lerSerial();
                });
            }
        }, 0, 10);                  
    }
    
    private void lerSerial(){ 
        char leituraChar = ConexaoSerial.lerPortaSerial();
        
        if(leituraChar == '0'){ //Passou pelo laser
            pararCronometro();
            txtTeste.setText("Cronômetro parado");    //Teste
            leituraChar = '\0';
        }
    }
    
    //Lista de resultados
    private void carregarResultados(){ 
        //Setando resultados. - Observação: O tempo reação foi setado no método lerSerial, logo quando recebeu o valor do arduino.
        resultado.setParticipante("Participante "+ordemLista);
        resultado.setTempoTotal(this.tempoCronometro.getHoras(), this.tempoCronometro.getMinutos(), this.tempoCronometro.getSegundos(), this.tempoCronometro.getCentesimas());
        resultados.add(resultado);

        obsResultados = FXCollections.observableArrayList(resultados);  //Converte o ArrayList em ObservableArrayList, que é o reconhecido pelo ListView.
        lvResultados.setItems(obsResultados);
        
        System.out.println("Resultados carregados. Aguardando ação do usuário (Resetar cronômetro, voltar participante ou próximo participante). Método: carregarResultados() de FXMLModoExplosaoController.");
    }
    
    private void atualizarTextsParticipantes(){
        txtParticipanteAtual.setText("Vez de: Participante "+ordemLista);
    }
    
    private void atualizarBotaoParticipanteAnterior(){
        //O botão de próximo não é desativado, pois se clicar no próximo e não tiver participante, ele pergunta se quer encerrar o eercício.
        //Botão de participante anterior
        if((ordemLista-1) < 1){
           btnVoltarParticipante.setDisable(true);  //Desativa o botão se não houver participante anterior
        }else{
           btnVoltarParticipante.setDisable(false);
        }
    }
    
    @FXML
    private void btnResetarListaResultados(ActionEvent event) {
        ordemLista = 1;
        atualizarBotaoParticipanteAnterior();
        atualizarTextsParticipantes();
        resultados.removeAll(resultados);
        obsResultados = FXCollections.observableArrayList(resultados);
        lvResultados.setItems(obsResultados);
        btnIniciar.setDisable(false);   //Teste
        resetarCronometro();
    }
    
    @FXML
    void btnDeletarUltimoResultado(ActionEvent event) {
        if(resultados.get(resultados.size()-1) != null){
            resultados.remove(resultados.size()-1);
            obsResultados = FXCollections.observableArrayList(resultados);
            lvResultados.setItems(obsResultados);
            resetarCronometro();
        }        
    }
    
}
