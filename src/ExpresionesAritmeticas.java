import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class ExpresionesAritmeticas {
    public static void main(String[] args) {
        ExpresionesAritmeticas expresiones = new ExpresionesAritmeticas();
        Deque<String> conversion = expresiones.conversionPostfija("(8+4)/2*3");
        System.out.println(conversion);
    }

    public int prioridadAlgebraica(String operacion){
        switch (operacion){
            case "+":
                return 1;
            case "-":
                return 1;
            case "*":
                return 2;
            case "/":
                return 2;
            default: return 0;
        }
    }

    public Deque<String> conversionPostfija(String expresion){
        Deque<String> resultado = new ArrayDeque<>();
        Deque<String> operadores = new ArrayDeque<>();
        List<String> operadoresValidos = List.of("+", "-", "*", "/");
        expresion = expresion.replaceAll(" ", "");

        for (int i = 0; i < expresion.length(); i++){
            if (Character.isDigit(expresion.charAt(i))){
                resultado.add(String.valueOf(expresion.charAt(i)));
            } else if (expresion.charAt(i) == '(') {
                operadores.push(String.valueOf(expresion.charAt(i)));
            }else if (expresion.charAt(i) == ')') {
                    boolean encontrado = false;
                    while (!operadores.isEmpty()) {
                        String tope = operadores.pop();
                        if (tope.equals("(")) {
                            encontrado = true;
                            break;
                        }
                        resultado.add(tope);
                    }
                    if (!encontrado) {
                        throw new IllegalArgumentException("falta un parentesis AAAAAA");
                    }
            } else if (operadoresValidos.contains(String.valueOf(expresion.charAt(i)))) {
                while (!operadores.isEmpty() && !operadores.peek().equals("(") && prioridadAlgebraica(operadores.peek()) >= prioridadAlgebraica((String.valueOf(expresion.charAt(i))))){
                    resultado.add(operadores.pop());
                }
                operadores.push(String.valueOf(expresion.charAt(i)));
            }
        }
        while (!operadores.isEmpty()) {
            String tope = operadores.pop();
            if (tope.equals("(")) {
                throw new IllegalArgumentException("se perdio un ) GG");
            }
            resultado.add(tope);
        }
        return resultado;
    }
}
