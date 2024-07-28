# CSNETWK MCO
Commands
Must be Windows OS
To activate Client Application

step 1 : goto client_fxml executable

cd Client_App\client_fxml\out\artifacts\client_fxml_jar

Step 2: Execute in cmd
java --module-path ".\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar client_fxml.jar



To activate Server Application

Step1 : goto Sever App folder
cd Server_App
cd out\artifacts\Server_App_jar

Step 2: Execute
java --module-path ".\javafx-sdk-17.0.12\lib" --add-modules javafx.controls,javafx.base,javafx.fxml,javafx.graphics,javafx.media,javafx.web --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx.event=ALL-UNNAMED -jar Server_App.jar
