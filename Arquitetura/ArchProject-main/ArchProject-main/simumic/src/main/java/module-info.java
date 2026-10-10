// Esse arquivo autoriza o javafx a ler as classes
module com.simumic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // Libera Stage e Scene

    opens com.simumic to javafx.fxml;
    exports com.simumic;
}   
//Padrão Maven, apenas adicionei graphics