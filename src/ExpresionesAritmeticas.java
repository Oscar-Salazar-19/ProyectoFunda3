import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

public class ExpresionesAritmeticas {
    public static void main(String[] args) {
        ExpresionesAritmeticas expresiones = new ExpresionesAritmeticas();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("\nIngresa la expresión infija (o 'salir' para terminar): ");
            String entrada = sc.nextLine();

            if ( entrada.equals("salir") ) break;

            try {
                Deque<String> conversion = expresiones.conversionPostfija(entrada);
                System.out.println( "Expresión postfija: " + String.join(" ", conversion) );

                double resultado = expresiones.calcularPostfija(conversion);
                System.out.println("Resultado: " + resultado);
            } catch (IllegalArgumentException | ArithmeticException e) {
                System.out.println(e.getMessage());
            }
        }

        sc.close();
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
                        throw new IllegalArgumentException("Error: Falta un parentesis en la expresion");
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
                throw new IllegalArgumentException("Error: En la expresion hace falta un parentesis");
            }
            resultado.add(tope);
        }
        return resultado;
    }


    public double calcularPostfija(Deque<String> exp)
    {
        Deque<Double> numeros = new ArrayDeque<>();
        double op1, op2;

        while(!exp.isEmpty())
        {
            String elemento = exp.pop();
            if ( Character.isDigit(elemento.charAt( elemento.length() - 1)) ) numeros.push( Double.parseDouble(elemento) );
            else
            {
                if (numeros.size() < 2) throw new IllegalArgumentException("Error: expresion incompleta o mal formada");

                op2 = numeros.pop();
                op1 = numeros.pop();
                numeros.push( operaciones(elemento, op1, op2) );
            }

        }

        return numeros.pop();
    }

    private double operaciones(String operacion, double num1, double num2){
        switch (operacion){
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                if( num2 == 0) throw new ArithmeticException("Error: division entre cero");
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Error: operador no valido");
        }
    }
}
