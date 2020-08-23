/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 *
 * @author alcoolis - https://stackoverflow.com/questions/26792812/android-toast-equivalent-in-javafx/38373408#38373408 adaptado
 */
public final class Toast
{
    /**
     * Exibe uma mensagem temporária centralizada na parte inferior da tela.
     * Exemplo de chamada: Toast.makeText(CadastroModoReacao.getStage(), "Modo aplicado", 1000, 500, 500);
     * @param ownerStage  Tela principal. Exemplo: CadastroModoReacao.getStage()
     * @param toastMsg  Mensagem que será exibida. Exemplo: "Modo aplicado".
     * @param toastDelay  Tempo (ms) que o toast fica visível depois de apare (depois do FadeIn). Exemplo: 1000.
     * @param fadeInDelay  Tempo (ms) que leva para o toast aparecer até ficar totalmente visível. Exemplo: 500.
     * @param fadeOutDelay  Tempo (ms) que leva para o toast sumir quando acabar o tempo de visibilidade (depois do toastDelay). Exemplo 500.
     */
    public static void makeText(Stage ownerStage, String toastMsg, int toastDelay, int fadeInDelay, int fadeOutDelay)
    {
        //Cria uma nova stage (stage para o toast) - É uma janela que é aberta
        Stage toastStage=new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        //Cria um componente de texto
        Text text = new Text(toastMsg);
        text.setFont(Font.font("System", 40));
        text.setFill(Color.WHITE);

        //Cria um painel que recebe o componente de texto
        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-radius: 20; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-padding: 10px;");
        root.setOpacity(0);

        //Cria uma cena que ficará dentro da stage, para poder inserir o painel dentro dela e ela seja mostrada na stage
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        toastStage.setScene(scene);
        toastStage.show();
        
        //Configura a posição da stage na tela do usuário
        toastStage.setX(ownerStage.getX() + (ownerStage.getWidth()/2 - toastStage.getWidth()/2));   //Para que fique no centro da janela principal.
        toastStage.setY(ownerStage.getY() + (ownerStage.getHeight() - toastStage.getHeight()) - 15);    //Para que fique na parte inferior daa janela principal, mas com uma leve margem.

        //Trecho responsável pela duração do toast e duração do FadeIn e FadeOut
        //Prepara o FadeIn
        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey1 = new KeyFrame(Duration.millis(fadeInDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 1)); 
        fadeInTimeline.getKeyFrames().add(fadeInKey1);   
        //Prepara o que vai ser feito após o FadeIn
        fadeInTimeline.setOnFinished((ae) ->
        {
            new Thread(() -> {
                try
                {
                    Thread.sleep(toastDelay);   //Depois dese delay (tempo de ficar visível), ele começa o FadeOut
                }
                catch (InterruptedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                   //Prepara o FadeOut
                   Timeline fadeOutTimeline = new Timeline();
                    KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeOutDelay), new KeyValue (toastStage.getScene().getRoot().opacityProperty(), 0)); 
                    fadeOutTimeline.getKeyFrames().add(fadeOutKey1);   
                    //Prepara o que vai acontecer quando acabar o FadeOut
                    fadeOutTimeline.setOnFinished((aeb) -> toastStage.close());
                    //Começa o FadeOut
                    fadeOutTimeline.play();
            }).start();
        }); 
        //Inicia o FadeIn
        fadeInTimeline.play();
    }
    
}