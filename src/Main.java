import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String sourceCode = "if (x > 0) { print(\"Hello, world!\"); } + $ %"; // Código fuente con errores
        LexicalAnalyzer lexer = new LexicalAnalyzer(sourceCode); // Crear un objeto LexicalAnalyzer
        lexer.analyze(); // Realizar el análisis léxico
    }
}


