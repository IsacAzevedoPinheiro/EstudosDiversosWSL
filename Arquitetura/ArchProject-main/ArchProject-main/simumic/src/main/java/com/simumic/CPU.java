package com.simumic;

import java.util.HashMap;
import java.util.Map;

/**
 * CAMPOS DO MIR
 * AMUX(1) | COND(2) | ULA(2) | SH(2) | MBR(1) | MAR(1) | RD(1) | WR(1)
 * ENC(1) | C(4) | B(4) | A(4) | ADDR(8)
 */

public class CPU {
    private final AcessoMemoria memoria;

    // Indexs regs
    private final int[] regs = new int[16];

    private static final int PC = 0, AC = 1, SP = 2, IR = 3, TIR = 4, ZERO = 5, P1 /* +1 */ = 6, M1 /*-1*/ = 7,
            AMASK = 8, SMASK = 9,
            A = 10, B = 11, C = 12, D = 13, E = 14, F = 15;

    private int mar, mbr;
    private boolean flagN, flagZ;
    private int mpc;
    private int busA, busB, busC;

    // Sinais de controle do MIR
    private int amux_ctrl; // 0=LatchA, 1=MBR
    private int cond_ctrl; // 0=nenhum, 1=ifN, 2=ifZ, 3=goto
    private int alu_ctrl; // 0=A+B, 1=A&B, 2=A, 3=~A
    private int sh_ctrl; // 0=nenhum, 1=lshift, 2=rshift
    private int mbr_ctrl, mar_ctrl, rd_ctrl, wr_ctrl;
    private int enc_ctrl; // 1=habilita escrita no banco de registradores via C

    private int a_reg, b_reg, c_reg, addr_field;

    // Mostrar na interface
    private int subcicloAtual, opcodeAtual;
    private String statusCiclo, msgMPC, sinalControle;
    private final String[] mpcStrings;

    // Analise ULA
    private int latchA, latchB;
    private int aluResult; // resultado bruto
    private int shifterResult; // resultado após o shifter (vai para busC)
    private boolean flagN_latched, flagZ_latched; // travadas até o sub4

    private long totalSubciclos = 0;
    private long totalCiclos = 0;
    private long instrucoesExecutadas = 0;

    private boolean rdPending, wrPending; // booleano em vez de salvar a prev em uma variável

    private static final int[][] CONTROL_STORE = buildControlStore();

    /*
     * COND: 0=nenhum 1=ifN→addr 2=ifZ→addr 3=goto addr
     * ULA: 0=A+B 1=A AND B 2=A 3=NOT A
     * SH: 0=nenhum 1=lshift 2=rshift
     */
    private static int[][] buildControlStore() {
        int[][] cs = new int[256][13];
        
        // Fetch / Decode
        cs[ 0] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,  PC,  0,   1);  
        cs[ 1] = mi(0, 0, 0, 0,  0, 0, 1, 0, 1, PC,  P1, PC,   2);  
        cs[ 2] = mi(1, 1, 2, 0,  0, 0, 0, 0, 1, IR,   0,  0,  28);  
        cs[ 3] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR,  IR, IR,  20);  
        cs[ 4] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  11);  
        cs[ 5] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR,   9);  
        
        // LODD
        cs[ 6] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,  IR,  0,   7);  
        cs[ 7] = mi(0, 0, 2, 0,  0, 0, 1, 0, 0,  0,   0,  0,   8);  
        cs[ 8] = mi(1, 3, 2, 0,  0, 0, 0, 0, 1, AC,   0,  0,   0);  
        
        // STOD
        cs[ 9] = mi(0, 0, 2, 0,  1, 1, 0, 1, 0,  0,  IR, AC,  10);  
        cs[10] = mi(0, 3, 0, 0,  0, 0, 0, 1, 0,  0,   0,  0,   0);  
        
        // Decode bit 13
        cs[11] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  15);  
        
        // ADDD
        cs[12] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,  IR,  0,  13);  
        cs[13] = mi(0, 0, 2, 0,  0, 0, 1, 0, 0,  0,   0,  0,  14);  
        cs[14] = mi(1, 3, 0, 0,  0, 0, 0, 0, 1, AC,  AC,  0,   0);  
        
        // SUBD (Com complemento de 2 corrigido)
        cs[15] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  25);  
        cs[16] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,  IR,  0,  17);  
        cs[17] = mi(0, 0, 2, 0,  0, 0, 1, 0, 0,  0,   0,  0,  18);  
        cs[18] = mi(1, 3, 3, 0,  0, 0, 0, 0, 1,  A,   0,  0,  80);  
        cs[80] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1,  A,   A, P1,  19);  
        cs[19] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1, AC,  AC,  A,   0);  

        // Decode JUMPS
        cs[20] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  26);  
        cs[21] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  24);  
        cs[22] = mi(0, 1, 2, 0,  0, 0, 0, 0, 0,  0,  AC, AC,   0);  
        cs[23] = mi(0, 3, 1, 0,  0, 0, 0, 0, 1, PC,AMASK,IR,   0);  
        cs[24] = mi(0, 2, 2, 0,  0, 0, 0, 0, 0,  0,  AC, AC,  23);  
        cs[25] = mi(0, 3, 0, 0,  0, 0, 0, 0, 0,  0,   0,  0,   0);  
        cs[26] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  77);  
        cs[27] = mi(0, 3, 1, 0,  0, 0, 0, 0, 1, PC,AMASK,IR,   0);  
        
        // Decode bit 15
        cs[28] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR,  IR, IR,  40);  
        cs[29] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  36);  
        cs[30] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR,  33);  
        
        // LODL 
        cs[31] = mi(0, 0, 0, 0,  0, 0, 0, 0, 1,  A,  SP, IR,  32);  
        cs[32] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,   A,  0,   7);  
        
        // STOL 
        cs[33] = mi(0, 0, 0, 0,  0, 0, 0, 0, 1,  A,  SP, IR,  34);  
        cs[34] = mi(0, 0, 2, 0,  1, 1, 0, 1, 0,  0,   A, AC,  10);  
        cs[35] = mi(0, 3, 0, 0,  0, 0, 0, 0, 0,  0,   0,  0,   0);  
        
        // ADDL / SUBL
        cs[36] = mi(0, 0, 0, 0,  0, 0, 0, 0, 1,  A,  SP, IR,  37);  
        cs[37] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR,  39);  
        cs[38] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,   A,  0,  13);  
        cs[39] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,   A,  0,  16);  
        
        // Decode C, D, E, F
        cs[40] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  46);  
        cs[41] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR,  44);  
        
        // JNEG (C000) 
        cs[42] = mi(0, 1, 2, 0,  0, 0, 0, 0, 0,  0,  AC, AC,  27);  
        cs[43] = mi(0, 3, 0, 0,  0, 0, 0, 0, 0,  0,   0,  0,   0);  
        
        // JNZE (D000)
        cs[44] = mi(0, 2, 2, 0,  0, 0, 0, 0, 0,  0,  AC, AC,   0);  
        cs[45] = mi(0, 3, 0, 0,  0, 0, 0, 0, 0,  0,   0,  0,  27);  
        
        // Decode E, F
        cs[46] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR, 100);  
        
        // CALL (E000)
        cs[47] = mi(0, 0, 0, 0,  0, 0, 0, 0, 1, SP,  SP, M1,  48);  
        cs[48] = mi(0, 0, 2, 0,  1, 1, 0, 1, 0,  0,  SP, PC,  49);  
        cs[49] = mi(0, 3, 1, 0,  0, 0, 0, 1, 1, PC,AMASK,IR,   0);  
        
        // DECODDE 1111
        cs[100] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR, 130); 
        cs[101] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR, 110); 
        cs[102] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR, 105); 
        
        // PSHI
        cs[103] = mi(0, 0, 2, 0,  0, 1, 1, 0, 0,  0,  AC,  0, 104); 
        cs[104] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1, SP,  SP, M1, 107); 
        cs[107] = mi(0, 3, 0, 0,  0, 1, 0, 1, 0,  0,  SP,  0,  10); 
        // POPI
        cs[105] = mi(0, 0, 0, 0,  0, 1, 1, 0, 1, SP,  SP, P1, 106); 
        cs[106] = mi(0, 3, 2, 0,  0, 1, 0, 1, 0,  0,  AC,  0,  10); 

        cs[110] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR, 113); 
        // PUSH
        cs[111] = mi(0, 0, 0, 0,  0, 0, 0, 0, 1, SP,  SP, M1, 112); 
        cs[112] = mi(0, 3, 2, 0,  1, 1, 0, 1, 0,  0,  SP, AC,  10); 
        // POP
        cs[113] = mi(0, 0, 0, 0,  0, 1, 1, 0, 1, SP,  SP, P1, 114); 
        cs[114] = mi(1, 3, 2, 0,  0, 0, 0, 0, 1, AC,   0,  0,   0); 

        cs[130] = mi(0, 1, 0, 0,  0, 0, 0, 0, 1,TIR, TIR,TIR, 140); 
        cs[131] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR, 134); 
        
        // RETN
        cs[132] = mi(0, 0, 0, 0,  0, 1, 1, 0, 1, SP,  SP, P1, 133); 
        cs[133] = mi(1, 3, 2, 0,  0, 0, 0, 0, 1, PC,   0,  0,   0); 
        
        // SWAP
        cs[134] = mi(0, 0, 2, 0,  0, 0, 0, 0, 1,  A,  AC, AC, 135); 
        cs[135] = mi(0, 0, 2, 0,  0, 0, 0, 0, 1, AC,  SP, SP, 136); 
        cs[136] = mi(0, 3, 2, 0,  0, 0, 0, 0, 1, SP,   A,  A,   0); 

        cs[140] = mi(0, 1, 0, 0,  0, 0, 0, 0, 0,  0, TIR,TIR, 143); 
        // INSP
        cs[141] = mi(0, 0, 1, 0,  0, 0, 0, 0, 1,  A,SMASK,IR, 142); 
        cs[142] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1, SP,  SP,  A,   0); 
        // DESP
        cs[143] = mi(0, 0, 1, 0,  0, 0, 0, 0, 1,  A,SMASK,IR, 144); 
        cs[144] = mi(0, 3, 3, 0,  0, 0, 0, 0, 1,  A,   0,  A, 145); 
        cs[145] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1,  A,   A, P1, 146); 
        cs[146] = mi(0, 3, 0, 0,  0, 0, 0, 0, 1, SP,  SP,  A,   0); 

        // LOCO EXECUTION
        cs[77] = mi(0, 3, 1, 0,  0, 0, 0, 0, 1, AC,AMASK,IR,   0);  
        
        return cs;
    }

    // Construir micro new int[]{ amux cond alu sh mbr mar rd wr enc C B A addr }
    private static int[] mi(int amux, int cond, int alu, int sh, int mbrF, int marF, int rd, int wr, int enc, int cF,
            int bF, int aF, int addr) {
        return new int[] { amux, cond, alu, sh, mbrF, marF, rd, wr, enc, cF, bF, aF, addr };
    }

    // Strings para exibição de cada MPC
    private String[] buildMpcStrings() {
        String[] s = new String[256];
        for (int i = 0; i < 256; i++) s[i] = "---";
        s[ 0] = "mar:=pc; rd;";
        s[ 1] = "pc:=pc + 1; rd;";
        s[ 2] = "ir:=mbr; if n then goto 28;";
        s[ 3] = "tir:=lshift(ir + ir); if n then goto 20;";
        s[ 4] = "tir:=lshift(tir); if n then goto 11;";
        s[ 5] = "alu:=tir; if n then goto 9;";
        s[ 6] = "mar:=ir; rd;";
        s[ 7] = "rd;";
        s[ 8] = "ac:=mbr; goto 0;";
        s[ 9] = "mar:=ir; mbr:=ac; wr;";
        s[10] = "wr; goto 0;";
        s[11] = "tir:=lshift(tir); if n then goto 15;";
        s[12] = "mar:=ir; rd;";
        s[13] = "rd;";
        s[14] = "ac:=mbr + ac; goto 0;";
        s[15] = "tir:=lshift(tir); if n then goto 25;";
        s[16] = "mar:=ir; rd;";
        s[17] = "rd;";
        s[18] = "a:=inv(mbr); goto 80;";
        s[80] = "a:=a + 1; goto 19;";
        s[19] = "ac:=ac + a; goto 0;";
        s[20] = "tir:=lshift(tir); if n then goto 26;";
        s[21] = "tir:=lshift(tir); if n then goto 24;";
        s[22] = "alu:=ac; if n then goto 0;";
        s[23] = "pc:=band(ir,amask); goto 0;";
        s[24] = "alu:=ac; if z then goto 23;";
        s[25] = "goto 0;";
        s[26] = "tir:=lshift(tir); if n then goto 77;";
        s[27] = "pc:=band(ir,amask); goto 0;";
        s[28] = "tir:=lshift(ir + ir); if n then goto 40;";
        s[29] = "tir:=lshift(tir); if n then goto 36;";
        s[30] = "alu:=tir; if n then goto 33;";
        s[31] = "a:=ir + sp;";
        s[32] = "mar:=a; rd; goto 7;";
        s[33] = "a:=ir + sp;";
        s[34] = "mar:=a; mbr:=ac; wr; goto 10;";
        s[35] = "goto 0;";
        s[36] = "a:=ir + sp;";
        s[37] = "alu:=tir; if n then goto 39;";
        s[38] = "mar:=a; rd; goto 13;";
        s[39] = "mar:=a; rd; goto 16;";
        s[40] = "tir:=lshift(tir); if n then goto 46;";
        s[41] = "tir:=lshift(tir); if n then goto 44;";
        s[42] = "alu:=ac; if n then goto 27;";
        s[43] = "goto 0;";
        s[44] = "alu:=ac; if z then goto 0;";
        s[45] = "goto 27;";
        s[46] = "tir:=lshift(tir); if n then goto 100;";
        s[47] = "sp:=sp + (-1);";
        s[48] = "mar:=sp; mbr:=pc; wr;";
        s[49] = "pc:=band(ir,amask); wr; goto 0;";
        
        s[100] = "tir:=lshift(tir); if n then goto 130;";
        s[101] = "tir:=lshift(tir); if n then goto 110;";
        s[102] = "alu:=tir+tir; if n then goto 105;";
        s[103] = "mar:=ac; rd;";
        s[104] = "sp:=sp + (-1); goto 107;";
        s[107] = "mar:=sp; wr; goto 10;";
        s[105] = "mar:=sp; sp:=sp + 1; rd;";
        s[106] = "mar:=ac; wr; goto 10;";
        
        s[110] = "alu:=tir+tir; if n then goto 113;";
        s[111] = "sp:=sp + (-1);";
        s[112] = "mar:=sp; mbr:=ac; wr; goto 10;";
        s[113] = "mar:=sp; sp:=sp + 1; rd;";
        s[114] = "ac:=mbr; goto 0;";
        
        s[130] = "tir:=lshift(tir); if n then goto 140;";
        s[131] = "alu:=tir+tir; if n then goto 134;";
        s[132] = "mar:=sp; sp:=sp + 1; rd;";
        s[133] = "pc:=mbr; goto 0;";
        s[134] = "a:=ac;";
        s[135] = "ac:=sp;";
        s[136] = "sp:=a; goto 0;";
        
        s[140] = "alu:=tir+tir; if n then goto 143;";
        s[141] = "a:=band(ir,smask);";
        s[142] = "sp:=sp + a; goto 0;";
        s[143] = "a:=band(ir, smask);";
        s[144] = "a:=inv(a); goto 145;";
        s[145] = "a:=a + 1; goto 146;";
        s[146] = "sp:=sp + a; goto 0;";
        
        s[77] = "ac:=band(ir,amask); goto 0;";
        return s;
    }

    public CPU(AcessoMemoria memoria) {
        this.memoria = memoria;
        this.mpcStrings = buildMpcStrings();
        reset();
    }

    public void reset() {
        for (int i = 0; i < 16; i++)
            regs[i] = 0;
        // Constantes
        regs[ZERO] = 0;
        regs[P1] = 1;
        regs[M1] = -1; // complemento de 2 → 0xFFFF como 16 bits
        regs[AMASK] = 0x0FFF; // 12 bits: máscara de endereço
        regs[SMASK] = 0x00FF; // 8 bits: máscara de constante (INSP/DESP)

        mar = 0;
        mbr = 0;
        busA = 0;
        busB = 0;
        busC = 0;
        mpc = 0;
        subcicloAtual = 1;
        flagN = false;
        flagZ = true;
        rdPending = false;
        wrPending = false;
        statusCiclo = "IDLE";
        msgMPC = "---";
        sinalControle = "IDLE";
        opcodeAtual = -1;
        latchA = 0;
        latchB = 0;
        aluResult = 0;
        shifterResult = 0;
        totalSubciclos = 0;
        totalCiclos = 0;
        instrucoesExecutadas = 0;

        // Carrega MIR inicial
        carregarMIR(mpc);
    }

    private void carregarMIR(int addr) {
        if (addr < 0 || addr >= CONTROL_STORE.length || CONTROL_STORE[addr] == null) {
            // Endereço fora da faixa ou NOP
            amux_ctrl = 0;
            cond_ctrl = 0;
            alu_ctrl = 0;
            sh_ctrl = 0;
            mbr_ctrl = 0;
            mar_ctrl = 0;
            rd_ctrl = 0;
            wr_ctrl = 0;
            enc_ctrl = 0;
            c_reg = 0;
            b_reg = 0;
            a_reg = 0;
            addr_field = 0;
            return;
        }
        int[] mir = CONTROL_STORE[addr]; // Carrega sinais
        amux_ctrl = mir[0];
        cond_ctrl = mir[1];
        alu_ctrl = mir[2];
        sh_ctrl = mir[3];
        mbr_ctrl = mir[4];
        mar_ctrl = mir[5];
        rd_ctrl = mir[6];
        wr_ctrl = mir[7];
        enc_ctrl = mir[8];
        c_reg = mir[9];
        b_reg = mir[10];
        a_reg = mir[11];
        addr_field = mir[12];
    }

    public void executarSubciclo() {
        totalSubciclos++;
        switch (subcicloAtual) {
            case 1:
                sub1();
                break;
            case 2:
                sub2();
                break;
            case 3:
                sub3();
                break;
            case 4:
                sub4();
                break;
        }
        subcicloAtual = (subcicloAtual % 4) + 1;
    }

    private void sub1() {
        if (mpc == 0)
            instrucoesExecutadas++;
        statusCiclo = "SUB1";
        msgMPC = String.format("MPC %02d: [%s]", mpc, mpcStrings[mpc]);

        carregarMIR(mpc); // MIR ← Control Store[MPC]

        // Captura de dado de leitura anterior
        if (rdPending) {

            mbr = memoria.read(mar) & 0xFFFF; // MBR ← memória[MAR]
            rdPending = false;
            sinalControle = "READ";
        }
        if (mpc == 2)
            opcodeAtual = (mbr >> 12) & 0xF; // atualiza opcode se estamos no MPC 2 (ir:=mbr)

        // Atualizar rd/wr (transições)
        if (rd_ctrl == 1)
            rdPending = true; // leitura será capturada no próximo sub1
        if (wr_ctrl == 1)
            wrPending = true; // escrita será completada no sub4

        busA = regs[a_reg];
        busB = regs[b_reg]; // Joga registradores selecionados nos barramentos

        sinalControle = (rd_ctrl == 1) ? "READ" : (wr_ctrl == 1) ? "WRITE" : "IDLE";
    }

    private void sub2() {
        statusCiclo = "SUB2";

        latchA = busA;
        latchB = busB; // habilita saída dos barramentos

        int entradaULA = (amux_ctrl == 1) ? mbr : latchA; // AMUX

        // Processa ULA
        switch (alu_ctrl) {
            case 0:
                aluResult = entradaULA + latchB;
                break; // A + B
            case 1:
                aluResult = entradaULA & latchB;
                break; // A AND B
            case 2:
                aluResult = entradaULA;
                break; // A (passa)
            case 3:
                aluResult = ~entradaULA;
                break; // NOT A
        }
        aluResult &= 0xFFFF; // trunca a 16 bits

        // Flags calculadas pela ULA e travadas
        flagN_latched = (aluResult & 0x8000) != 0;
        flagZ_latched = (aluResult == 0);

        // Shifter
        switch (sh_ctrl) {
            case 0:
                shifterResult = aluResult;
                break;
            case 1:
                shifterResult = ((aluResult << 1) & 0xFFFF);
                break; // lshift
            case 2:
                shifterResult = ((aluResult >> 1) & 0x7FFF);
                break; // rshift
        }

        busC = shifterResult; // Entrada do Barramento C
    }

    private void sub3() {
        statusCiclo = "SUB3";

        if (mar_ctrl == 1)
            mar = busB & 0x0FFF;
        if (mbr_ctrl == 1)
            mbr = busC & 0xFFFF;
    }

    private void sub4() {
        totalCiclos++;
        statusCiclo = "SUB4";

        if (enc_ctrl == 1 && (c_reg < ZERO || c_reg > SMASK))
            regs[c_reg] = busC; // constantes 0, P1, M1, AMASK e SMASK não escrevem

        flagN = flagN_latched;
        flagZ = flagZ_latched; // armazena flags travadas

        // Completa escrita na memória (RAM[MAR] ← MBR)
        if (wrPending) {
            memoria.write(mar, mbr & 0xFFFF);
            wrPending = false;
            sinalControle = "WRITE";
        }

        int proximoMPC;
        switch (cond_ctrl) {
            case 0:
                proximoMPC = mpc + 1;
                break;
            case 1:
                proximoMPC = flagN ? addr_field : (mpc + 1);
                break; // if n
            case 2:
                proximoMPC = flagZ ? addr_field : (mpc + 1);
                break; // if z
            case 3:
                proximoMPC = addr_field;
                break; // goto
            default:
                proximoMPC = mpc + 1;
                break;
        }
        mpc = proximoMPC & 0xFF; // 8 bits (256 posições na Control Store)
    }

    public void executarCicloCompleto() {
        sub1();
        sub2();
        sub3();
        sub4();
        subcicloAtual = 1;
    }

    public int getMAR() {
        return mar;
    }

    public int getMBR() {
        return mbr;
    }

    public int getAReg() {
        return a_reg;
    }

    public int getBReg() {
        return b_reg;
    }

    public int getCReg() {
        return c_reg;
    }

    public int getAmuxCtrl() {
        return amux_ctrl;
    }

    public int getMarCtrl() {
        return mar_ctrl;
    }

    public int getMbrCtrl() {
        return mbr_ctrl;
    }

    public int getEncCtrl() {
        return enc_ctrl;
    }

    public int getCondCtrl() {
        return cond_ctrl;
    }

    public int getAluCtrl() {
        return alu_ctrl;
    }

    public int getShCtrl() {
        return sh_ctrl;
    }

    public int getRdCtrl() {
        return rd_ctrl;
    }

    public int getWrCtrl() {
        return wr_ctrl;
    }

    public int getAddrField() {
        return addr_field;
    }

    public int getMPC() {
        return mpc;
    }

    public int getSubcicloAtual() {
        return subcicloAtual;
    }

    public boolean isFlagN() {
        return flagN;
    }

    public boolean isFlagZ() {
        return flagZ;
    }

    public String getSinalControle() {
        return sinalControle;
    }

    public String getMsgMPC() {
        return msgMPC;
    }

    public String getstatusCiclo() {
        return statusCiclo;
    }

    public int getOpcodeAtual() {
        return opcodeAtual;
    }

    public long getTotalSubciclos() {
        return totalSubciclos;
    }

    public long getTotalCiclos() {
        return totalCiclos;
    }

    public long getInstrucoesExecutadas() {
        return instrucoesExecutadas;
    }

    public int getBusA() {
        return busA;
    }

    public int getBusB() {
        return busB;
    }

    public int getBusC() {
        return busC;
    }

    public int getLatchA() {
        return latchA;
    }

    public int getLatchB() {
        return latchB;
    }

    public int getAluResult() {
        return aluResult;
    }

    public int getShifterResult() {
        return shifterResult;
    }

    public int getReg(int idx) {
        return (idx >= 0 && idx < 16) ? regs[idx] : 0;
    }

    // HM pras labels da interface
    public Map<String, Integer> getRegs() {
        Map<String, Integer> m = new HashMap<>();
        m.put("PC", regs[PC]);
        m.put("AC", regs[AC]);
        m.put("SP", regs[SP]);
        m.put("IR", regs[IR]);
        m.put("TIR", regs[TIR]);
        m.put("ZERO", regs[ZERO]);
        m.put("P1", regs[P1]);
        m.put("M1", regs[M1]);
        m.put("AMASK", regs[AMASK]);
        m.put("SMASK", regs[SMASK]);
        m.put("LV", regs[A]); // LV mapeado no A Tanembaum
        m.put("B", regs[B]);
        m.put("C", regs[C]);
        m.put("D", regs[D]);
        m.put("E", regs[E]);
        m.put("F", regs[F]);
        m.put("MAR", mar);
        m.put("MBR", mbr);
        return m;
    }
}