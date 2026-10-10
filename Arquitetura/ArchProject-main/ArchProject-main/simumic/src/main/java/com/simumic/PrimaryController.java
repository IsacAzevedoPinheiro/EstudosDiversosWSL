package com.simumic;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;

public class PrimaryController {

    // DECLARANDO OS ELEMENTOS BASEADO NO FX:ID DO FXML
    @FXML
    private Label labelPC, labelAC, labelSP, labelIR, labelTIR, labelLV,
            labelB, labelC, labelD, labelE, labelF,
            labelAMASK, labelSMASK, labelMAR, labelMBR,
            labelZERO, labelP1, labelM1;

    @FXML
    private Rectangle rectPC, rectAC, rectSP, rectIR, rectTIR, rectLV,
            rectB, rectC, rectD, rectE, rectF,
            rectAMASK, rectSMASK, rectMAR, rectMBR,
            rectZERO, rectP1, rectM1;

    @FXML
    private Label labelBusA, labelBusB, labelBusC, labelLatchA, labelLatchB,
            labelALU, labelShifter, labelStatus,
            labelFlagN, labelFlagZ;

    @FXML
    private Rectangle rectLatchA, rectLatchB, rectAMUX, rectShifter,
            rectFlagN, rectFlagZ, rectMicroSeq;

    @FXML
    private Rectangle rectRAM, rectL1, rectL2, rectFlagRD, rectFlagWR;
    @FXML
    private Polygon polygonALU;

    @FXML
    private Rectangle rectDecA, rectDecB, rectDecC;

    @FXML
    private Line lBusC1, lBusC2, lBusC3, lBusA1, lBusA2, lBusA3, lBusB1, lBusB2,
            lMarL1, lL1L2_Mar, lL2Ram_Mar, lMbrL1, lL1L2_Mbr, lL2Ram_Mbr, lMarB, lMbrC, lRegC, lAmux1, lAmux2,
            lAluA, lAluB, lShifter, lCtrl1, lCtrl2, lCtrl3, lCtrl4,
            lCond, lFlags1, lFlags2, lAddr1, lAddr2, lAddr3, lAddr4, lAddr5,
            lDecA, lDecB, lDecC;

    @FXML
    private Label num1, num2, num3, num4;

    @FXML
    private Label labelMpcVal;

    @FXML
    private Label labelMirAmux, labelMirCond, labelMirAlu, labelMirSh, labelMirMbr,
            labelMirMar, labelMirRd, labelMirWr, labelMirEnc, labelMirC, labelMirB,
            labelMirA, labelMirAddr;

    private Line[] todasLinhas;
    private Label[] numsClock;

    @FXML
    private TextArea consoleMacro;
    @FXML
    private ScrollPane scrollMPC;
    @FXML
    private Label labelMPC, labelAssemblerStatus, labelEstatisticas, labelInstr, labelL1Stats, labelL2Stats;
    @FXML
    private Button btnSubciclo;
    @FXML
    private Button btnAutoRun;
    @FXML
    private TextField txtDelay;

    private CPU cpu;
    private Memoria ram;
    private Cache cacheL1;
    private Cache cacheL2;

    private Timeline autoRunTimeline;
    private int ultimoEnderecoPrograma = 0;
    private Stage janelaMemoria;
    private Stage janelaCache;
    private TertiaryController tertiaryController;

    

    // PALETA PARA ALTERAÇÕES DE COR
    private static final Color COR_FUNDO = Color.web("#000000");
    private static final Color COR_CINZA = Color.web("#d2d5d5");
    private static final String CSS_CINZA = "#d2d5d5";
    private static final Color COR_VERDE = Color.web("#077d29");
    private static final String CSS_VERDE = "#077d29";
    private static final Color COR_VERMELHO = Color.web("#c3150e");
    private static final Color COR_AMARELO = Color.web("#f1c40f");

    @FXML
    public void initialize() {
        ram = new Memoria();
        cacheL2 = new Cache("L2", ram, 8, 4);
        cacheL1 = new Cache("L1", cacheL2, 2, 4);
        cpu = new CPU(cacheL1);

        if (scrollMPC != null) {
            scrollMPC.heightProperty().addListener((obs, o, n) -> scrollMPC.setVvalue(1.0));
        }

        todasLinhas = new Line[] { // DEFINE PARA APAGAR TUDO QUANDO RESETAR
                lBusC1, lBusC2, lBusC3, lBusA1, lBusA2, lBusA3, lBusB1, lBusB2,
                lMarL1, lL1L2_Mar, lL2Ram_Mar, lMbrL1, lL1L2_Mbr, lL2Ram_Mbr, lMarB, lMbrC, lRegC, lAmux1, lAmux2,
                lAluA, lAluB, lShifter, lCtrl1, lCtrl2, lCtrl3, lCtrl4,
                lCond, lFlags1, lFlags2, lAddr1, lAddr2, lAddr3, lAddr4, lAddr5,
                lDecA, lDecB, lDecC
        };
        numsClock = new Label[] { num1, num2, num3, num4 };

        atualizarLabels();
    }

    @FXML
    private void resetCpu() {
        labelAssemblerStatus.setText("");
        cpu.reset();
        if (cacheL1 != null) {
            cacheL1.clear();
        }
        if (cacheL2 != null) {
            cacheL2.clear();
        }
        btnSubciclo.setText("SUBCICLO 0");

        atualizarBotaoSubciclo();
        atualizarLabels();
        atualizarJanelaCache();
    }

    @FXML
    private void execSubciclo() {
        labelAssemblerStatus.setText("");
        cpu.executarSubciclo();
        atualizarLabels();
        atualizarBotaoSubciclo();

        if ((cpu.getSubcicloAtual() == 1))
            imprimirConsoleMPC();
    }

    @FXML
    private void execCicloCompleto() {
        labelAssemblerStatus.setText("");
        cpu.executarCicloCompleto();
        atualizarLabels();
        atualizarBotaoSubciclo();
        imprimirConsoleMPC();
    }

    @FXML
    private void toggleAutoRun() {
        if (autoRunTimeline != null && autoRunTimeline.getStatus() == Animation.Status.RUNNING) {
            pararAutoRun();
        } else {
            int delay = Integer.parseInt(txtDelay.getText());
            autoRunTimeline = new Timeline(
                    new KeyFrame(Duration.millis(delay), e -> {
                        execSubciclo();
                        
                        if (cpu.getSubcicloAtual() == 1 && cpu.getMPC() == 0) {
                            int pcAtual = cpu.getRegs().get("PC");
                            if (pcAtual > ultimoEnderecoPrograma) {
                                pararAutoRun();
                            }
                        }
                    }));
            autoRunTimeline.setCycleCount(Timeline.INDEFINITE);
            autoRunTimeline.play();
            btnAutoRun.setText("⏹ PARAR");
            btnAutoRun.setStyle(
                    "-fx-background-color: #922b21; -fx-text-fill: white; " +
                            "-fx-font-weight: bold; -fx-font-size: 12; -fx-border-color: #922b21; -fx-border-width: 2;");
        }
    }

    private void pararAutoRun() {
        if (autoRunTimeline != null) {
            autoRunTimeline.stop();
            autoRunTimeline = null;
        }
        btnAutoRun.setText("▶ AUTO RUN");
        btnAutoRun.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-font-size: 12; -fx-border-color: #077d29; -fx-border-width: 2;");
    }

    @FXML
    private void abrirJanelaMemoria() {
        if (janelaMemoria != null && janelaMemoria.isShowing()) {
            janelaMemoria.toFront();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/simumic/secondary.fxml"));
            Parent root = loader.load();

            SecondaryController ctrl = loader.getController();
            ctrl.setMemoria(ram);
            ctrl.atualizar();

            Scene cena = new Scene(root, 900, 600);
            cena.setUserData(ctrl);

            janelaMemoria = new Stage();
            janelaMemoria.setTitle("Inspetor de Memória RAM — SimuMIC");
            janelaMemoria.setScene(cena);
            janelaMemoria.show();

        } catch (IOException e) {
            e.printStackTrace();
            labelAssemblerStatus.setTextFill(COR_VERMELHO);
            labelAssemblerStatus.setText("Erro ao abrir janela de memória.");
        }
    }
    private void atualizarJanelaMemoria() {
    if (janelaMemoria != null && janelaMemoria.isShowing()) {
        Object ctrl = janelaMemoria.getScene().getUserData();
        if (ctrl instanceof SecondaryController) {
            ((SecondaryController) ctrl).atualizar();
        }
    }
}   

    @FXML
    private void abrirJanelaCache() {
        if (janelaCache != null && janelaCache.isShowing()) {
            atualizarJanelaCache();
            janelaCache.toFront();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/simumic/tertiary.fxml"));
            Parent root = loader.load();

            tertiaryController = loader.getController();
            tertiaryController.setCaches(cacheL1, cacheL2);

            Scene cena = new Scene(root, 980, 620);

            janelaCache = new Stage();
            janelaCache.setTitle("Cache - SimuMIC");
            janelaCache.setScene(cena);
            janelaCache.setOnShown(e -> atualizarJanelaCache());
            janelaCache.show();

        } catch (IOException e) {
            e.printStackTrace();
            labelAssemblerStatus.setTextFill(COR_VERMELHO);
            labelAssemblerStatus.setText("Erro ao abrir janela de cache.");
        }
    }

    private void imprimirConsoleMPC() {
        if (labelMPC != null) {
            String mpcMsg = cpu.getMsgMPC();
            if (mpcMsg.startsWith("MPC 77") || mpcMsg.startsWith("MPC 27")) {
                return;
            }

            if (mpcMsg.startsWith("MPC 00") && cpu.getInstrucoesExecutadas() != 1) {
                mpcMsg = "\n" + mpcMsg;
            }

            String atual = labelMPC.getText();
            labelMPC.setText(atual.equals("---") ? mpcMsg : atual + "\n" + mpcMsg);

            if (scrollMPC != null)
                scrollMPC.setVvalue(1.0);
        }
    }

    private int atualizarBotaoSubciclo() {
        int proximo = cpu.getSubcicloAtual();
        String[] nomes = { "SUBCICLO 1", "SUBCICLO 2", "SUBCICLO 3", "SUBCICLO 4" };

        btnSubciclo.setText(nomes[proximo - 1]);

        int porcentagem = proximo * 25;

        btnSubciclo.setStyle(
                "-fx-background-color: linear-gradient(to right, #077d29 " + porcentagem + "%, #000000 " + porcentagem + "%); " +
                "-fx-background-radius: 0; " +
                "-fx-border-radius: 0; " +
                "-fx-border-color: #077d29; " +
                "-fx-border-width: 2;"
        );

        return proximo;
    }

    private Rectangle getRectByIndex(int index) {
        switch (index) {
            case 0:
                return rectPC;
            case 1:
                return rectAC;
            case 2:
                return rectSP;
            case 3:
                return rectIR;
            case 4:
                return rectTIR;
            case 5:
                return rectZERO;
            case 6:
                return rectP1;
            case 7:
                return rectM1;
            case 8:
                return rectAMASK;
            case 9:
                return rectSMASK;
            case 10:
                return rectLV;
            case 11:
                return rectB;
            case 12:
                return rectC;
            case 13:
                return rectD;
            case 14:
                return rectE;
            case 15:
                return rectF;
            default:
                return null;
        }
    }

    private void acenderBusA() {
        lBusA1.setStroke(COR_VERDE);
        lBusA2.setStroke(COR_VERDE);
        lBusA3.setStroke(COR_VERDE);
    }

    private void acenderBusB() {
        lBusB1.setStroke(COR_VERDE);
        lBusB2.setStroke(COR_VERDE);
    }

    private void acenderBusC() {
        lBusC1.setStroke(COR_VERDE);
        lBusC2.setStroke(COR_VERDE);
        lBusC3.setStroke(COR_VERDE);
        lRegC.setStroke(COR_VERDE);

    }

    private void atualizarLabels() {
        Map<String, Integer> regs = cpu.getRegs();

        preencherReg(labelPC, rectPC, regs.get("PC"));
        preencherReg(labelAC, rectAC, regs.get("AC"));
        preencherReg(labelIR, rectIR, regs.get("IR"));
        preencherReg(labelMAR, rectMAR, cpu.getMAR());
        preencherReg(labelMBR, rectMBR, cpu.getMBR());
        preencherReg(labelSP, rectSP, regs.get("SP"));
        preencherReg(labelLV, rectLV, regs.get("LV"));
        preencherReg(labelZERO, rectZERO, regs.get("ZERO"));
        preencherReg(labelP1, rectP1, regs.get("P1"));
        preencherReg(labelM1, rectM1, regs.get("M1"));
        preencherReg(labelAMASK, rectAMASK, regs.get("AMASK"));
        preencherReg(labelSMASK, rectSMASK, regs.get("SMASK"));
        preencherReg(labelB, rectB, regs.get("B"));
        preencherReg(labelC, rectC, regs.get("C"));
        preencherReg(labelD, rectD, regs.get("D"));
        preencherReg(labelE, rectE, regs.get("E"));
        preencherReg(labelF, rectF, regs.get("F"));
        preencherReg(labelTIR, rectTIR, regs.get("TIR"));

        polygonALU.setStroke(COR_CINZA);
        rectShifter.setStroke(COR_CINZA);
        rectMicroSeq.setStroke(COR_CINZA);

        for (Line l : todasLinhas)
            l.setStroke(COR_CINZA);
        for (Label n : numsClock)
            n.setStyle("-fx-text-fill:" + CSS_CINZA + ";");

        boolean n = cpu.isFlagN();
        boolean z = cpu.isFlagZ();
        labelFlagN.setText(n ? "1" : "0");
        labelFlagZ.setText(z ? "1" : "0");
        rectFlagN.setFill(n ? COR_VERDE : COR_FUNDO);
        rectFlagN.setStroke(n ? COR_VERDE : COR_CINZA);
        rectFlagZ.setFill(z ? COR_VERDE : COR_FUNDO);
        rectFlagZ.setStroke(z ? COR_VERDE : COR_CINZA);

        rectFlagRD.setFill(COR_FUNDO);
        rectFlagRD.setStroke(COR_CINZA);
        rectFlagWR.setFill(COR_FUNDO);
        rectFlagWR.setStroke(COR_CINZA);
        rectRAM.setStroke(COR_CINZA);
        rectL1.setStroke(COR_AMARELO);
        rectL2.setStroke(COR_AMARELO);

        String sinal = cpu.getSinalControle();
        if ("READ".equals(sinal)) {
            rectFlagRD.setFill(COR_VERDE);
            rectFlagRD.setStroke(COR_VERDE);
        } else if ("WRITE".equals(sinal)) {
            rectFlagWR.setFill(COR_VERDE);
            rectFlagWR.setStroke(COR_VERDE);
        }
                
        if (cpu.getSinalControle().equals("READ") || cpu.getSinalControle().equals("WRITE")) {
            
            rectL1.setStroke(cacheL1.isUltimoAcessoHit() ? COR_VERDE : COR_VERMELHO);
            rectL2.setStroke(cacheL2.isUltimoAcessoHit() ? COR_VERDE : COR_VERMELHO);
            
            if (!cacheL1.isUltimoAcessoHit() && !cacheL2.isUltimoAcessoHit()) {
                rectRAM.setStroke(COR_VERDE);
            } else {
                rectRAM.setStroke(COR_CINZA);
            }
}
        String status = cpu.getstatusCiclo();

        labelStatus.setText(status);
        if (status.equals("IDLE"))
            labelStatus.setTextFill(COR_CINZA);
        else
            labelStatus.setTextFill(COR_VERDE);

        labelBusA.setText(String.format("%04X", cpu.getBusA()));
        labelBusB.setText(String.format("%04X", cpu.getBusB()));
        labelBusC.setText(String.format("%04X", cpu.getBusC()));
        labelLatchA.setText(String.format("%04X", cpu.getLatchA()));
        labelLatchB.setText(String.format("%04X", cpu.getLatchB()));
        labelALU.setText(String.format("%04X", cpu.getAluResult()));
        labelShifter.setText(String.format("%04X", cpu.getShifterResult()));
        rectLatchA.setStroke(COR_CINZA);
        rectLatchB.setStroke(COR_CINZA);
        rectAMUX.setStroke(COR_CINZA);
        rectDecA.setStroke(COR_CINZA);
        rectDecB.setStroke(COR_CINZA);
        rectDecC.setStroke(COR_CINZA);

        // Atualiza o MPC numérico (registrador do microsequenciador)
        if (labelMpcVal != null) {
            labelMpcVal.setText(String.format("%03d", cpu.getMPC()));
        }

        // Atualiza o painel do MIR (Microinstruction Register)
        if (labelMirAmux != null) {
            labelMirAmux.setText(String.valueOf(cpu.getAmuxCtrl()));
            labelMirCond.setText(String.format("%02d", cpu.getCondCtrl()));
            labelMirAlu.setText(String.format("%02d", cpu.getAluCtrl()));
            labelMirSh.setText(String.valueOf(cpu.getShCtrl()));
            labelMirMbr.setText(String.valueOf(cpu.getMbrCtrl()));
            labelMirMar.setText(String.valueOf(cpu.getMarCtrl()));
            labelMirRd.setText(String.valueOf(cpu.getRdCtrl()));
            labelMirWr.setText(String.valueOf(cpu.getWrCtrl()));
            labelMirEnc.setText(String.valueOf(cpu.getEncCtrl()));
            labelMirC.setText(String.format("%04d", cpu.getCReg()));
            labelMirB.setText(String.format("%04d", cpu.getBReg()));
            labelMirA.setText(String.format("%04d", cpu.getAReg()));
            labelMirAddr.setText(String.format("%08d", cpu.getAddrField()));
        }

        if ("SUB1".equals(status)) {
            num1.setStyle("-fx-text-fill: " + CSS_VERDE + ";");
            Rectangle rA = getRectByIndex(cpu.getAReg());
            rA.setStroke(COR_VERDE);
            Rectangle rB = getRectByIndex(cpu.getBReg());
            rB.setStroke(COR_VERDE);
            rectAMUX.setStroke(COR_VERDE);

            lDecA.setStroke(COR_VERDE);
            lDecB.setStroke(COR_VERDE);

            acenderBusA();
            acenderBusB();

            if ("READ".equals(sinal)) {
                rectMBR.setStroke(COR_VERDE);
                acenderFiosMemoria();
            }

        } else if ("SUB2".equals(status)) {
            num2.setStyle("-fx-text-fill: " + CSS_VERDE + ";");
            rectDecA.setStroke(COR_VERDE);
            rectDecB.setStroke(COR_VERDE);
            rectLatchA.setStroke(COR_VERDE);
            rectLatchB.setStroke(COR_VERDE);

            polygonALU.setStroke(COR_VERDE);
            rectShifter.setStroke(COR_VERDE);

            lAluB.setStroke(COR_VERDE);
            lShifter.setStroke(COR_VERDE);
            lFlags1.setStroke(COR_VERDE);

            if (cpu.getAmuxCtrl() == 1) {
                rectMBR.setStroke(COR_VERDE);
                lAmux1.setStroke(COR_VERDE);
                lAmux2.setStroke(COR_VERDE);
                lAluA.setStroke(COR_VERDE);
            } else {
                lAluA.setStroke(COR_VERDE);
            }

        } else if ("SUB3".equals(status)) {
            num3.setStyle("-fx-text-fill: " + CSS_VERDE + ";");
            polygonALU.setStroke(COR_CINZA);

            acenderBusC();

            if (cpu.getMarCtrl() == 1) {
                rectMAR.setStroke(COR_VERDE);
                if (lMarB != null)
                    lMarB.setStroke(COR_VERDE);
                acenderBusB();
            }
            if (cpu.getMbrCtrl() == 1) {
                rectMBR.setStroke(COR_VERDE);
                if (lMbrC != null)
                    lMbrC.setStroke(COR_VERDE);
            }

        } else if ("SUB4".equals(status)) {
            num4.setStyle("-fx-text-fill: " + CSS_VERDE + ";");
            acenderBusC();

            rectMicroSeq.setStroke(COR_VERDE);

            if (cpu.getEncCtrl() == 1) {
                Rectangle rC = getRectByIndex(cpu.getCReg());
                rC.setStroke(COR_VERDE);
                rectDecC.setStroke(COR_VERDE);
                lDecC.setStroke(COR_VERDE);
            }
            if ("WRITE".equals(sinal)) {
                rectMAR.setStroke(COR_VERDE);
                rectMBR.setStroke(COR_VERDE);
                acenderFiosMemoria();
            }

            lCtrl1.setStroke(COR_VERDE);
            lCtrl2.setStroke(COR_VERDE);
            lCtrl3.setStroke(COR_VERDE);
            lCtrl4.setStroke(COR_VERDE);
            lCond.setStroke(COR_VERDE);
            lFlags2.setStroke(COR_VERDE);
            lAddr1.setStroke(COR_VERDE);
            lAddr2.setStroke(COR_VERDE);
            lAddr3.setStroke(COR_VERDE);
            lAddr4.setStroke(COR_VERDE);
            lAddr5.setStroke(COR_VERDE);
        }
        if (cacheL1 != null && cacheL2 != null) {
            labelL1Stats.setText(formatarStats(cacheL1));
            labelL2Stats.setText(formatarStats(cacheL2));
        }

        if (labelEstatisticas != null) {
            labelEstatisticas
                    .setText(cpu.getTotalCiclos() + " Ciclos | " + cpu.getTotalSubciclos() + " Sub");
            labelInstr.setText(cpu.getInstrucoesExecutadas() + " Instr. Executadas");
        }

        atualizarJanelaCache();
        atualizarJanelaMemoria();
    }

    private void acenderFiosMemoria() {
        lMarL1.setStroke(COR_VERDE);
        lMbrL1.setStroke(COR_VERDE);

        if (!cacheL1.isUltimoAcessoHit()) {
            lL1L2_Mar.setStroke(COR_VERDE);
            lL1L2_Mbr.setStroke(COR_VERDE);

            if (!cacheL2.isUltimoAcessoHit()) {
                lL2Ram_Mar.setStroke(COR_VERDE);
                lL2Ram_Mbr.setStroke(COR_VERDE);
            }
        }
    }
    private void preencherReg(Label label, Rectangle rect, Integer valor) {
        if (label == null)
            return;
        int v = (valor == null) ? 0 : valor;
        label.setText(String.format("%04X", v & 0xFFFF));
        rect.setStroke(COR_CINZA);
    }

    @FXML
    private void montarCodigo() {
        String texto = consoleMacro.getText();
        if (texto == null || texto.trim().isEmpty()) {
            labelAssemblerStatus.setTextFill(COR_VERMELHO);
            labelAssemblerStatus.setText("Console vazio!");
            return;
        }

        String[] linhas = texto.split("\\n");
        int endereco = 0;
        int sucesso = 0;

        for (String linha : linhas) {
            linha = linha.trim();
            if (linha.isEmpty() || linha.startsWith(";"))
                continue;

            try {
                int instrucaoMontada = parseLinha(linha);
                ram.write(endereco, instrucaoMontada);
                ultimoEnderecoPrograma = Math.max(ultimoEnderecoPrograma, endereco);
                endereco++;
                sucesso++;
            } catch (Exception e) {
                labelAssemblerStatus.setTextFill(COR_VERMELHO);
                labelAssemblerStatus.setText("Erro de sintaxe na linha " + (sucesso + 1) + "!");
                return;
            }
        }

        labelAssemblerStatus.setTextFill(COR_VERDE);
        labelAssemblerStatus.setText(sucesso + " instruções na RAM!");
        System.out.println("Montagem concluída com sucesso.");
    }

    private int parseLinha(String linha) {
        String[] partes = linha.trim().split("\\s+");
        String mnem = partes[0].toUpperCase();
        int arg = 0;

        if (partes.length > 1)
            arg = Integer.parseInt(partes[1]);

        int opcode = 0;

        switch (mnem) {
            case "LODD":
                opcode = 0x0000;
                break;
            case "STOD":
                opcode = 0x1000;
                break;
            case "ADDD":
                opcode = 0x2000;
                break;
            case "SUBD":
                opcode = 0x3000;
                break;
            case "JPOS":
                opcode = 0x4000;
                break;
            case "JZER":
                opcode = 0x5000;
                break;
            case "JUMP":
                opcode = 0x6000;
                break;
            case "LOCO":
                opcode = 0x7000;
                break;
            case "LODL":
                opcode = 0x8000;
                break;
            case "STOL":
                opcode = 0x9000;
                break;
            case "ADDL":
                opcode = 0xA000;
                break;
            case "SUBL":
                opcode = 0xB000;
                break;
            case "JNEG":
                opcode = 0xC000;
                break;
            case "JNZE":
                opcode = 0xD000;
                break;
            case "CALL":
                opcode = 0xE000;
                break;
            case "PSHI":
                opcode = 0xF000;
                break;
            case "POPI":
                opcode = 0xF200;
                break;
            case "PUSH":
                opcode = 0xF400;
                break;
            case "POP":
                opcode = 0xF600;
                break;
            case "RETN":
                opcode = 0xF800;
                break;
            case "SWAP":
                opcode = 0xFA00;
                break;
            case "INSP":
                opcode = 0xFC00;
                break;
            case "DESP":
                opcode = 0xFE00;
                break;
            default:
                throw new IllegalArgumentException("Mnemônico desconhecido: " + mnem);
        }

            return opcode | (arg & 0x0FFF);
    }

    private void atualizarJanelaCache() {
        if (tertiaryController != null) {
            tertiaryController.atualizar();
        }
    }
    private String formatarStats(Cache c) {
        double total = c.getHits() + c.getMisses();
        double percent = (total == 0) ? 0 : (c.getHits() / total) * 100;
        return String.format("H: %d | M: %d | %.1f%%", c.getHits(), c.getMisses(), percent);
    }
}