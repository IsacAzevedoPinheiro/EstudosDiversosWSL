# 🖥️ SimuMIC

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-FF0000?style=for-the-badge&logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

> *Um simulador educacional interativo e visual da microarquitetura MIC-1, projetado para facilitar o estudo e a compreensão de datapaths e execução de instruções.*

---

## 🎯 Proposta
A proposta do projeto é criar um simulador que explique, de forma visual e passo a passo, o funcionamento da microarquitetura **MIC-1**, em estrita conformidade com a literatura de *Organização Estruturada de Computadores* de Andrew S. Tanenbaum, 3ªEdição.

## 📚 Estudos e Referências
O embasamento teórico para o desenvolvimento do SimuMIC contou com:
* Literatura clássica sobre Arquitetura de Computadores.
* Pesquisas e referências em websites especializados.
* Análise de arquivos e diagramas focados no funcionamento do MIC-1.
* Utilização de um simulador MIC-1 base para estudos prévios e testes práticos sobre o comportamento do *datapath* e das instruções implementadas.

## 🛠️ Implementação e Tecnologias
O simulador foi inteiramente desenvolvido na linguagem **Java**, utilizando conceitos sólidos de Programação Orientada a Objetos e as seguintes ferramentas:

* **[JavaFX](https://openjfx.io/):** Responsável por toda a interface gráfica e diagramação visual (através de arquivos `.fxml` e *Controllers* para interação em tempo real).
* **[Maven](https://maven.apache.org/):** Utilizado para a estruturação do projeto, *build* automatizado e gerenciamento de dependências.

## ⚙️ Estruturas Implementadas
O motor do simulador modela o hardware com alta fidelidade, exibindo o estado da máquina através de três visões principais (`main`, `ram-view` e `cache-view`). As estruturas contemplam:

- **Registradores:** 16 registradores básicos do caminho de dados + `MAR` e `MBR`.
- **Sinalização:** 2 flags de desvio (N, Z) e status dinâmico da RAM (*Idle*, *Read*, *Write*).
- **Barramentos:** Implementação completa dos barramentos `A`, `B` e `C`.
- **Hierarquia de Memória:** Caches `L1` e `L2` operando com Mapeamento Direto e política de atualização *Write Through*.
- **Microcódigo:** Interpretação nativa das macroinstruções do MIC-1.
- **Ciclo de Clock:** Execução de todos os `MPCs` (Microprogram Counter) dividida rigorosamente em 4 subciclos de clock.

---

## 🐳 Como rodar com Docker (Recomendado)

Não é necessário ter o Java ou o Maven instalados na sua máquina local para testar a aplicação. Basta ter o Docker configurado.

**1. Construir a imagem do simulador:**
```bash
sudo docker build -t simumic-app .
```
**2. Executar o simulador (Linux):**

```bash
sudo docker run -it --rm \
    --net=host \
    -e DISPLAY=$DISPLAY \
    -v /tmp/.X11-unix:/tmp/.X11-unix \
    -v $HOME/.Xauthority:/root/.Xauthority \
    simumic-app
```
*(Nota: Para Windows com WSL2 ou macOS, consulte a documentação sobre partilha do servidor X11/XQuartz).*

## 🚀 Perspectiva de Features (Roadmap)

O desenvolvimento é contínuo. As próximas atualizações focam em elevar a complexidade arquitetural do simulador:

- **Multiprocessamento:** Integrar dois processadores MIC-1 operando em paralelo.

- **Pipelining:** Implementar a arquitetura de pipeline para demonstrar o ganho de performance na sobreposição de instruções.