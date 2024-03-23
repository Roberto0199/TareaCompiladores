import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {
    private String sourceCode; // Código fuente a analizar
    private int currentPosition; // Posición actual de lectura en el código fuente
    private List<String> keywords; // Lista de palabras clave y combinaciones posibles
    private List<String> errors; // Lista de errores encontrados

    // Constructor de la clase LexicalAnalyzer
    public LexicalAnalyzer(String sourceCode) {
        this.sourceCode = sourceCode; // Asignar el código fuente proporcionado
        this.currentPosition = 0; // Inicializar la posición actual en 0
        this.keywords = new ArrayList<>(); // Inicializar la lista de palabras clave
        this.errors = new ArrayList<>(); // Inicializar la lista de errores

        // Agregar las palabras reservadas y combinaciones posibles a la lista de palabras clave
        keywords.add("if");
        keywords.add("else");
        keywords.add("for");
        keywords.add("print");
        keywords.add("int");
        keywords.add("bfhjk");
        keywords.add("bfhjkbfhjk");
        keywords.add("bfhjkb");
        keywords.add("bfbfhjk");
        keywords.add("fhjkbfhjk");
    }

    // Método para realizar el análisis léxico del código fuente
    public void analyze() {
        // Expresiones regulares para identificar tokens
        String identifierRegex = "[a-zA-Z_][a-zA-Z0-9_]*";
        String integerConstantRegex = "\\d+";
        String stringLiteralRegex = "\"[^\"]*\"";

        // Patrones para las expresiones regulares
        Pattern identifierPattern = Pattern.compile(identifierRegex);
        Pattern integerConstantPattern = Pattern.compile(integerConstantRegex);
        Pattern stringLiteralPattern = Pattern.compile(stringLiteralRegex);

        // Recorrer el código fuente hasta el final
        while (currentPosition < sourceCode.length()) {
            char currentChar = sourceCode.charAt(currentPosition); // Obtener el carácter actual

            // Ignorar espacios en blanco y caracteres de nueva línea
            if (Character.isWhitespace(currentChar)) {
                currentPosition++; // Moverse al siguiente carácter
                continue; // Continuar con el siguiente ciclo del bucle
            }

            // Identificar identificadores
            if (Character.isLetter(currentChar)) {
                Matcher matcher = identifierPattern.matcher(sourceCode.substring(currentPosition));
                if (matcher.find()) {
                    String identifier = matcher.group(); // Obtener el identificador
                    currentPosition += identifier.length(); // Actualizar la posición actual
                    if (identifier.length() > 15) { // Verificar la longitud del identificador
                        errors.add("Error: Identificador demasiado largo - " + identifier);
                    } else if (!keywords.contains(identifier)) {
                        System.out.println("Identificador: " + identifier); // Imprimir el identificador
                    }
                }
                continue; // Continuar con el siguiente ciclo del bucle
            }

            // Identificar constantes enteras
            if (Character.isDigit(currentChar)) {
                Matcher matcher = integerConstantPattern.matcher(sourceCode.substring(currentPosition));
                if (matcher.find()) {
                    String integerConstant = matcher.group(); // Obtener la constante entera
                    currentPosition += integerConstant.length(); // Actualizar la posición actual
                    int value = Integer.parseInt(integerConstant);
                    if (value < 0 || value > 100) {
                        errors.add("Error: Constante entera fuera de rango - " + integerConstant);
                    } else {
                        System.out.println("Constante entera: " + integerConstant);
                    }
                }
                continue; // Continuar con el siguiente ciclo del bucle
            }

            // Identificar cadenas literales
            if (currentChar == '"') {
                Matcher matcher = stringLiteralPattern.matcher(sourceCode.substring(currentPosition));
                if (matcher.find()) {
                    String stringLiteral = matcher.group(); // Obtener la cadena literal
                    currentPosition += stringLiteral.length(); // Actualizar la posición actual
                    System.out.println("Cadena de caracteres: " + stringLiteral);
                } else {
                    errors.add("Error: Cadena de caracteres no válida");
                    currentPosition++; // Moverse al siguiente carácter
                }
                continue; // Continuar con el siguiente ciclo del bucle
            }

            // Identificar operadores y otros tokens
            switch (currentChar) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '=':
                case '>':
                case '<':
                case '{':
                case '}':
                case '[':
                case ']':
                case '(':
                case ')':
                case ',':
                case ';':
                case ':':
                    System.out.println("Operador: " + currentChar); // Imprimir el operador
                    currentPosition++; // Moverse al siguiente carácter
                    break; // Salir del switch
                case '.':
                    // Identificar el operador ".."
                    if (currentPosition + 1 < sourceCode.length() && sourceCode.charAt(currentPosition + 1) == '.') {
                        System.out.println("Operador: .."); // Imprimir el operador ".."
                        currentPosition += 2; // Moverse dos posiciones hacia adelante
                        break; // Salir del switch
                    } else {
                        errors.add("Error: Símbolo '.' no reconocido");
                        currentPosition++; // Moverse al siguiente carácter
                    }
                    break; // Salir del switch
                default:
                    errors.add("Error: Carácter no reconocido - " + currentChar);
                    currentPosition++; // Moverse al siguiente carácter
            }
        }

        // Mostrar los errores encontrados
        if (!errors.isEmpty()) {
            System.out.println("\nErrores encontrados:");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // Método para leer un identificador del código fuente
    private String readIdentifier() {
        StringBuilder identifier = new StringBuilder(); // Inicializar un StringBuilder para almacenar el identificador
        // Leer caracteres mientras sean letras, dígitos o guiones bajos y no se haya alcanzado el final del código fuente
        while (currentPosition < sourceCode.length() &&
                (Character.isLetterOrDigit(sourceCode.charAt(currentPosition)) || sourceCode.charAt(currentPosition) == '_')) {
            identifier.append(sourceCode.charAt(currentPosition)); // Agregar el carácter al identificador
            currentPosition++; // Moverse al siguiente carácter
        }
        return identifier.toString(); // Devolver el identificador como una cadena
    }

    // Método para leer una constante entera del código fuente
    private int readIntegerConstant() {
        StringBuilder integerConstant = new StringBuilder(); // Inicializar un StringBuilder para almacenar la constante entera
        // Leer caracteres mientras sean dígitos y no se haya alcanzado el final del código fuente
        while (currentPosition < sourceCode.length() && Character.isDigit(sourceCode.charAt(currentPosition))) {
            integerConstant.append(sourceCode.charAt(currentPosition)); // Agregar el carácter a la constante entera
            currentPosition++; // Moverse al siguiente carácter
        }
        return Integer.parseInt(integerConstant.toString()); // Convertir la constante entera a un entero y devolverlo
    }

    // Método para leer una cadena literal del código fuente
    private String readStringLiteral() {
        StringBuilder stringLiteral = new StringBuilder(); // Inicializar un StringBuilder para almacenar la cadena literal
        currentPosition++; // Ignorar el primer "
        // Leer caracteres hasta encontrar el siguiente "
        while (currentPosition < sourceCode.length() && sourceCode.charAt(currentPosition) != '"') {
            stringLiteral.append(sourceCode.charAt(currentPosition)); // Agregar el carácter a la cadena literal
            currentPosition++; // Moverse al siguiente carácter
        }
        currentPosition++; // Ignorar el último "
        return stringLiteral.toString(); // Devolver la cadena literal como una cadena
    }
}