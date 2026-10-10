package com.simumic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class TertiaryController {

    @FXML private Label     labelTituloL1;
    @FXML private Label     labelStatsL1;
    @FXML private ListView<String> listaL1;

    @FXML private Label     labelTituloL2;
    @FXML private Label     labelStatsL2;
    @FXML private ListView<String> listaL2;

    private Cache cacheL1;
    private Cache cacheL2;

    public void setCaches(Cache cacheL1, Cache cacheL2) {
        this.cacheL1 = cacheL1;
        this.cacheL2 = cacheL2;
        if (labelTituloL1 != null) labelTituloL1.setText(cacheL1.getNome());
        if (labelTituloL2 != null) labelTituloL2.setText(cacheL2.getNome());
        atualizar();
    }

    @FXML
    public void initialize() {
        listaL1.setFixedCellSize(20.0);
        listaL2.setFixedCellSize(20.0);
        formatarListaCache(listaL1);
        formatarListaCache(listaL2);
    }
    
    @FXML
    public void atualizar() {
        if (cacheL1 != null) {
            preencher(listaL1, cacheL1);
            labelStatsL1.setText(formatarStats(cacheL1));
        }
        if (cacheL2 != null) {
            preencher(listaL2, cacheL2);
            labelStatsL2.setText(formatarStats(cacheL2));
        }
    }

    private void preencher(ListView<String> lista, Cache cache) {
        ObservableList<String> linhas = FXCollections.observableArrayList();
        int tamBloco = cache.getTamBloco();
        int linhaDestacada = cache.getUltimaLinha();

        for (int linha = 0; linha < cache.getTamCache(); linha++) {
            boolean valido = cache.isValida(linha);
            int tag = cache.getTag(linha);
            int inicio = tag * tamBloco;
            int fim = inicio + tamBloco - 1;

            StringBuilder conteudo = new StringBuilder();
            for (int i = 0; i < tamBloco; i++) {
                conteudo.append(String.format("%04X", cache.getDado(linha, i)));
                if (i < tamBloco - 1) conteudo.append(' ');
            }

            boolean destacada = (linha == linhaDestacada);

            String item = String.format(
                    "%02d;%d;%s;%04d-%04d;%s;%s",
                    linha,
                    valido ? 1 : 0,
                    valido ? String.valueOf(tag) : "----",
                    inicio, fim,
                    conteudo.toString(),
                    destacada ? "1" : "0"
            );
            linhas.add(item);
        }
        lista.setItems(linhas);
    }

    private String formatarStats(Cache c) {
        double total = c.getHits() + c.getMisses();
        double percent = (total == 0) ? 0 : (c.getHits() / total) * 100;
        String ultimoHit = "";
        if (c.getUltimoEndereco() >= 0) {
            ultimoHit = String.format("  |  Último endereço: %04d → linha %02d, tag %d (%s)",
                    c.getUltimoEndereco(), c.getUltimaLinha(), c.getUltimoBloco(),
                    c.isUltimoAcessoHit() ? "HIT" : "MISS");
        }
        return String.format("HITS=%d  MISSES=%d  (%.1f%%)%s", c.getHits(), c.getMisses(), percent, ultimoHit);
    }
    private static final String CSS_MONO = "-fx-font-family: Monospaced; -fx-font-size: 12;";

    private void formatarListaCache(ListView<String> lista) {
        lista.setCellFactory(lv -> new ListCell<>() {
            private final HBox hbox = new HBox(8.0);
            private final Label lblLinha    = new Label();
            private final Label lblV        = new Label();
            private final Label lblTag      = new Label();
            private final Label lblBloco    = new Label();
            private final Label lblConteudo = new Label();

            {
                // LARGURAS IDÊNTICAS ao cabeçalho de coluna no FXML (Linha/V/TAG/Bloco(end)/Conteúdo)
                lblLinha.setPrefWidth(42.0);     lblLinha.setAlignment(Pos.CENTER);
                lblV.setPrefWidth(22.0);         lblV.setAlignment(Pos.CENTER);
                lblTag.setPrefWidth(45.0);       lblTag.setAlignment(Pos.CENTER);
                lblBloco.setPrefWidth(90.0);     lblBloco.setAlignment(Pos.CENTER);
                lblConteudo.setPrefWidth(175.0); lblConteudo.setAlignment(Pos.CENTER);

                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.getChildren().addAll(lblLinha, lblV, lblTag, lblBloco, lblConteudo);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: #000; -fx-padding: 1 0 1 4;");
                    return;
                }

                String[] p = item.split(";");
                String linha      = p[0];
                boolean valido    = "1".equals(p[1]);
                String tag        = p[2];
                String bloco      = p[3];
                String conteudo   = p[4];
                boolean destacada = p.length > 5 && "1".equals(p[5]);

                lblLinha.setText(linha);
                lblV.setText(valido ? "1" : "0");
                lblTag.setText(tag);
                lblBloco.setText(bloco);
                lblConteudo.setText(conteudo);

                String corTexto = valido ? "#d2d5d5" : "#555555";
                String corV     = valido ? "#077d29" : "#555555";
                String corTag   = valido ? "#e91e8c" : "#555555"; // magenta, igual ao slide
                String corBloco = valido ? "#3498db" : "#555555"; // azul, igual ao slide

                lblLinha.setStyle(CSS_MONO + " -fx-text-fill: " + corTexto + ";");
                lblV.setStyle(CSS_MONO + " -fx-font-weight: bold; -fx-text-fill: " + corV + ";");
                lblTag.setStyle(CSS_MONO + " -fx-font-weight: bold; -fx-text-fill: " + corTag + ";");
                lblBloco.setStyle(CSS_MONO + " -fx-text-fill: " + corBloco + ";");
                lblConteudo.setStyle(CSS_MONO + " -fx-text-fill: " + corTexto + ";");

                setText(null);
                setGraphic(hbox);

                if (destacada) {
                    setStyle("-fx-background-color: #1a1a1a; -fx-padding: 1 0 1 4; " +
                             "-fx-border-color: #077d29; -fx-border-width: 1 0 1 0;");
                } else {
                    setStyle("-fx-background-color: #000; -fx-padding: 1 0 1 4;");
                }
            }
        });
    }
}