# Sistema de educação física para avaliação do atleta

Versão simplificada do modo explosão do sistema desktop desenvolvido pelo [núcleo de robótica cesmac](https://www.instagram.com/roboticacesmac/) em parceria com o curso de educação física, com o objetivo de criar um software para computador que se comunica com um equipamento externo que funciona com uma placa arduino e permite a avaliação do atleta através dos dados registrados. Essa versão simplificada do modo explosão não possui cadastro de usuário e registra apenas o tempo total que o atleta levou para percorrer determinada distância. Você pode conferir a demonstração do sistema completo clicando [aqui](https://youtu.be/7EKrd3_x-kU).

## Descrição técnica

O software foi desenvolvido em Java para desktop. Usa a biblioteca gráfica [JavaFX](https://www.youtube.com/watch?v=P13ycvI-17Q&list=PLWd_VnthxxLejQ9CcHrsT5HCFn-10kquZ), e para a comunicação pela porta serial com o arduino faz uso da biblioteca [jSerialComm](https://www.youtube.com/watch?v=AFUBjqdpK_4&list=PLWd_VnthxxLf1hyk3_cez8yJbqcy6iqOD).

### Pré-requisitos

É necessário ter o Java Development Kit instalado na máquina <br/>
É recomendado utilizar o software [SceneBuilder](https://youtu.be/DpyVnys1nvs) para edições das telas que foram feitas com FXML, utilizada para definir a interface de uma aplicação JavaFx <br/>
A IDE utilizada para o desenvolvimento foi o NetBeans <br/>
Obs.: Se precisar indicar o local de alguma biblioteca para a IDE, elas estão na pasta "biblioteca" no diretório raíz do projeto

### Leituras e impressões na porta serial

Para a comunicação com o arduino, o sistema envia e recebe caracteres pela porta serial que são interpretados como uma ação. <br/>

* Modo explosão <br/>
Imprime 1 quando é iniciado o exercício <br/>
Lê 0 quando o participante passa pelo laser do equipamento <br/>