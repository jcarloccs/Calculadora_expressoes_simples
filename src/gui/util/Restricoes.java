package gui.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

import javafx.scene.control.Label;
import model.entities.ListaCalc;

public class Restricoes {
	
	// formata a expressão no visor da calculadora
	public static void separarNumerosVisor(String texto, Label visor) {
		
        Locale.setDefault(Locale.US);
        DecimalFormat decimalformat = new DecimalFormat(",###.###############");

        String expressaoResultado = "";
        
        if (!texto.isEmpty()) {
	        String[] expressaoCortada = texto.split("(?=[÷]|[-]|[×]|[+]|[%]|[(]|[)])|(?<=[÷]|[-]|[×]|[+]|[%]|[(]|[)])");
	        
	        for (int i = 0; i < expressaoCortada.length; i++) {
	        	if (expressaoCortada[i].matches(".*\\.$")) {
	        		expressaoCortada[i] = decimalformat.format(new BigDecimal(expressaoCortada[i].substring(0, expressaoCortada[i].length() - 1))) + ".";
	        	}
	        	else if (!expressaoCortada[i].matches("[÷]|[-]|[×]|[+]|[%]|[(]|[)]")) {
	        		expressaoCortada[i] = decimalformat.format(new BigDecimal(expressaoCortada[i]));
	        	}
	        }
	        
	        for (String x : expressaoCortada) {
	        	expressaoResultado += x;
	        }
	
	        expressaoResultado = expressaoResultado.replace(",", ";");
	        expressaoResultado = expressaoResultado.replace(".", ",");
	        expressaoResultado = expressaoResultado.replace(";", ".");
        }

        visor.setText(expressaoResultado);

    }

	public static void quantidadeCaracteres(Label visorTemp, ListaCalc listaCalc) {
		visorTemp.textProperty().addListener((obs, oldValue, newValue) -> {
			
			if (newValue.length() > 100) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }
			
		});
	}
	
	public static void controlaParenteses(Label visorTemp, ListaCalc listaCalc) {
		visorTemp.textProperty().addListener((obs, oldValue, newValue) -> {
			
			int aberto = 0, fechado = 0;
			for (int i = 0; i < newValue.length(); i++) {
				if (newValue.charAt(i) == '(') {
					aberto++;
				}
				if (newValue.charAt(i) == ')') {
					fechado++;
				}
			}
			
			if (aberto < fechado) {
				visorTemp.setText(oldValue);
				listaCalc.setTextoVisor(oldValue);
			}
			
		});
	}
	
	public static void corrigeExpressao(Label visorTemp, ListaCalc listaCalc) {
		visorTemp.textProperty().addListener((obs, oldValue, newValue) -> {
			
			// remove pontos duplicados
            if ((newValue != null) && newValue.matches(".*[\\.]\\d*[\\.]$")) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            //adiciona parênteses entre alguns operadores
            if ((newValue != null) && (newValue.matches(".*[×][-]$") ||
                    newValue.matches(".*[÷][-]$") ||
                    newValue.matches(".*[\\^][-]$"))) {
                String temp = oldValue;
                temp += "(";
                temp += Character.toString(newValue.charAt(newValue.length() - 1));
                visorTemp.setText(temp);
                listaCalc.setTextoVisor(temp);
            }

            //coloca 0 antes da vírgula no início da expressão
            if ((newValue != null) && newValue.matches("\\.")) {
                visorTemp.setText("0.");
                listaCalc.setTextoVisor("0.");
            }

            // remove caracteres inválidos no início da expressão
            if ((newValue != null) &&
                    !(newValue.matches("^[-].*") ||
                            newValue.matches("^[(].*") ||
                            newValue.matches("^[\\.].*") ||
                            newValue.matches("") ||
                            newValue.matches("∞") ||
                            newValue.matches("Erro de Sintaxe") ||
                            newValue.matches("Erro") ||
                            newValue.matches("^[\\d].*"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            // regras gerais para os caracteres

            if ((newValue != null) &&
                    (newValue.matches(".*[+][+]$")) ||
                    (newValue.matches(".*[+][-]$")) ||
                    (newValue.matches(".*[+][×]$")) ||
                    (newValue.matches(".*[+][÷]$")) ||
                    (newValue.matches(".*[+][\\.]$")) ||
                    (newValue.matches(".*[+][%]$")) ||
                    (newValue.matches(".*[+][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[-][+]$")) ||
                    (newValue.matches(".*[-][-]$")) ||
                    (newValue.matches(".*[-][×]$")) ||
                    (newValue.matches(".*[-][÷]$")) ||
                    (newValue.matches(".*[-][\\.]$")) ||
                    (newValue.matches(".*[-][%]$")) ||
                    (newValue.matches(".*[-][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[×][+]$")) ||
                    (newValue.matches(".*[×][×]$")) ||
                    (newValue.matches(".*[×][÷]$")) ||
                    (newValue.matches(".*[×][\\.]$")) ||
                    (newValue.matches(".*[×][%]$")) ||
                    (newValue.matches(".*[×][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[÷][+]$")) ||
                    (newValue.matches(".*[÷][×]$")) ||
                    (newValue.matches(".*[÷][÷]$")) ||
                    (newValue.matches(".*[÷][\\.]$")) ||
                    (newValue.matches(".*[÷][%]$")) ||
                    (newValue.matches(".*[÷][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[\\.][+]$")) ||
                    (newValue.matches(".*[\\.][-]$")) ||
                    (newValue.matches(".*[\\.][×]$")) ||
                    (newValue.matches(".*[\\.][÷]$")) ||
                    (newValue.matches(".*[\\.][\\.]$")) ||
                    (newValue.matches(".*[\\.][%]$")) ||
                    (newValue.matches(".*[\\.][(]$")) ||
                    (newValue.matches(".*[\\.][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[%][\\.]$")) ||
                    (newValue.matches(".*[%][%]$")) ||
                    (newValue.matches(".*[%][(]$")) ||
                    (newValue.matches(".*[%][\\d]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }


            if ((newValue != null) &&
                    (newValue.matches(".*[(][+]$")) ||
                    (newValue.matches(".*[(][×]$")) ||
                    (newValue.matches(".*[(][÷]$")) ||
                    (newValue.matches(".*[(][\\.]$")) ||
                    (newValue.matches(".*[(][%]$")) ||
                    (newValue.matches(".*[(][)]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
                    (newValue.matches(".*[)][\\.]$")) ||
                    (newValue.matches(".*[)][(]$")) ||
                    (newValue.matches(".*[)][\\d]$"))) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }

            if ((newValue != null) &&
            		newValue.matches(".*[\\d][(]$")) {
                visorTemp.setText(oldValue);
                listaCalc.setTextoVisor(oldValue);
            }
			
		});
	}
	
}