package model.entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import exceptions.NumInvalidoException;
import model.enums.Operadores;
import model.enums.Parenteses;

public class ListaCalc {

    private List<Items> listaExpress = new ArrayList<>();
    private String textoVisor = "", aux;

    public String getTextoVisor() {
        return textoVisor;
    }

    public void setTextoVisor(String textoVisor) {
        this.textoVisor = textoVisor;
    }

    public void concatTextoVisor(String caractere) {
        this.textoVisor += caractere;
    }

    public void invalido(Double aux) {
        if (aux.isNaN()) throw new NumInvalidoException("Número inválido", "Erro");
        if (aux.isInfinite()) throw new NumInvalidoException("Número excedido", "∞");
    }

    public BigDecimal exibir() {
        return listaExpress.get(0).getNumero();
    }

    public List<Items> getListaExpress() {
        return listaExpress;
    }

    public void resetLista() {
        listaExpress.clear();
    }

    public boolean organizarExpressao() {

        aux = textoVisor;
        int aberto = 0, fechado = 0;

        // exclui caracteres errados no final da expressão
        while (!textoVisor.matches(".*\\d$") && !textoVisor.matches(".*[)]$") && !textoVisor.matches(".*[%]$")
                && !(textoVisor.length() == 0)) {
            textoVisor = textoVisor.substring(0, textoVisor.length() - 1);
        }

        // adiciona os parênteses que faltam
        for (int i = 0; i < textoVisor.length(); i++) {
            if (textoVisor.charAt(i) == '(')
                aberto++;
            if (textoVisor.charAt(i) == ')')
                fechado++;
        }
        for (int i = 0; i < (aberto - fechado); i++)
            textoVisor += ")";

        // formatação para exibição na tela
        if (inserirExpressao(textoVisor)) {

            if (exibir().doubleValue() == 0) {
                textoVisor = "0";
            }

            else if ((exibir().doubleValue() >= 1000000000 || exibir().doubleValue() <= -1000000000)
                    && !exibir().toString().contains("E")) {
                textoVisor = String.format("%.10E", exibir());
            }

            else if (exibir().toString().contains("E")) {
                textoVisor = String.format("%.10E", exibir());
            }

            else {
                textoVisor = exibir().setScale(10, RoundingMode.HALF_UP).toString();
                while (textoVisor.charAt(textoVisor.length() - 1) == '0')
                    textoVisor = textoVisor.substring(0, textoVisor.length() - 1);
                if (textoVisor.charAt(textoVisor.length() - 1) == '.')
                    textoVisor = textoVisor.substring(0, textoVisor.length() - 1);
            }

            listaExpress.clear();
            return true;
        }
        return false;

    }
    
    // colocar itens da expressão matemática numa lista
    public boolean inserirExpressao(String expressao) {

        String[] expressaoCortada = expressao.split("(?=[÷]|[-]|[×]|[+]|[%]|[(]|[)])|(?<=[÷]|[-]|[×]|[+]|[%]|[(]|[)])");
        
        try {
            
        	for (String x : expressaoCortada) {
                
        		switch(x) {
        			case "÷": listaExpress.add(new Items(Operadores.DIVISAO)); break;
        			case "×": listaExpress.add(new Items(Operadores.MULTIPLICACAO)); break;
        			case "-": listaExpress.add(new Items(Operadores.SUBTRACAO)); break;
        			case "+": listaExpress.add(new Items(Operadores.ADICAO)); break;
        			case "%": listaExpress.add(new Items(Operadores.PORCENTAGEM)); break;
        			case "(": listaExpress.add(new Items(Parenteses.ABERTO)); break;
        			case ")": listaExpress.add(new Items(Parenteses.FECHADO)); break;
        			default: listaExpress.add(new Items(new BigDecimal(x)));
        		}
        		
            }

            if (listaExpress.size() <= 1) {// se tiver somente 1 item item ele não executa a operação
                listaExpress.clear();
                textoVisor = aux;
                return false; // impede que imprima algo no visor
            } else
                organizarItems(listaExpress);

            return true; // confirma a impressão do resultado no visor

        } catch (NumInvalidoException e) {
            textoVisor = e.visor;
            listaExpress.clear();
            return false; // impede que imprima algo no visor
        } catch (ArithmeticException e) {
            textoVisor = "Erro";
            listaExpress.clear();
            return false; // impede que imprima algo no visor
        } catch (NumberFormatException e) {
            textoVisor = "Erro de sintaxe";
            listaExpress.clear();
            return false; // impede que imprima algo no visor
        }
    }

    // prepara a expressão para calcular números dentro de parênteses primeiro e porcentagem tem quase a mesma função dos parênteses
    public void organizarItems(List<Items> listacalc) {

        List<Items> listaprov;
        int aberto = -1, fechado = -1, porcent = -1;
        boolean existeParenteses = false, existePorcent = false;

        while (listacalc.size() > 1) {

            for (int i = 0; i < listacalc.size(); i++) {
                if (listacalc.get(i).getParenteses() == Parenteses.ABERTO) {
                    aberto = i;
                } else if (listacalc.get(i).getParenteses() == Parenteses.FECHADO) {
                    fechado = i;
                    existeParenteses = true;
                    break;
                } else if (listacalc.get(i).getOperador() == Operadores.PORCENTAGEM && i >= 3){
                    if (!(listacalc.get(i - 1).getNumero() == null)) {
                        if (listacalc.get(i - 2).getOperador() == Operadores.SUBTRACAO || listacalc.get(i - 2).getOperador() == Operadores.ADICAO) {
                            if (listacalc.get(i - 3).getOperador() == Operadores.PORCENTAGEM) {
                                if (!(listacalc.get(i - 4).getNumero() == null)) {
                                    porcent = i;
                                    existePorcent = true;
                                    break;
                                }
                            }
                            else if (!(listacalc.get(i - 3).getNumero() == null)) {
                                porcent = i;
                                existePorcent = true;
                                break;
                            }
                        }
                    }
                }
            }

            if (existePorcent) {
                listaprov = listacalc.subList(aberto + 1, porcent - 2);
                calculo(listaprov);
                for (int i = 0; i < listacalc.size(); i++) {
                    if (listacalc.get(i).getOperador() == Operadores.PORCENTAGEM) {
                        porcent = i;
                        break;
                    }
                }
                listacalc.get(porcent - 1).resultado(listacalc.get(porcent - 3).getNumero().multiply(listacalc.get(porcent - 1).getNumero().divide(new BigDecimal("100"), 30, RoundingMode.UP)), null);
                listacalc.remove(porcent);
                existePorcent = false;
            }

            else if (existeParenteses) {
                listaprov = listacalc.subList(aberto + 1, fechado);
                listacalc.get(aberto).repor(null, null, null);
                listacalc.get(fechado).repor(null, null, null);
                calculo(listaprov);

                for (int i = 0; i < listacalc.size(); i++) {
                    if (listacalc.get(i).getNumero() == null && listacalc.get(i).getOperador() == null
                            && listacalc.get(i).getParenteses() == null) {
                        listacalc.remove(i);
                        existeParenteses = false;
                    }
                }
            }

            else {
                calculo(listacalc);
            }

            aberto = -1;
            fechado = -1;
            porcent = -1;

        }

        invalido(listacalc.get(0).getNumero().doubleValue());

    }

    // cálculo bruto
    public void calculo(List<Items> listacalc) {

        int i;
        boolean add_and_rem = false;
        int operacao = 1;

        while (listacalc.size() > 1) {

            for (i = 0; i < listacalc.size(); i++) {

                if (operacao == 1 && listacalc.get(i).getOperador() != null) {
                    // faz a operação e substitui o operador pelo resultado da operação
                    switch (listacalc.get(i).getOperador()) {

                        case PORCENTAGEM:
                            if (listacalc.get(i - 1).getNumero() == null)
                                throw new NumInvalidoException("Erro na lista", "Erro de sintaxe");
                            listacalc.get(i).resultado(listacalc.get(i - 1).getNumero().divide(new BigDecimal("100"), 30, RoundingMode.UP), null);
                            listacalc.remove(i - 1);
                            i = -1;
                            break;
                        default:
                            break;

                    }
                }

                if (operacao == 2 && listacalc.get(i).getOperador() != null) {
                    // faz a operação e substitui o operador pelo resultado da operação
                    switch (listacalc.get(i).getOperador()) {

                        case DIVISAO:
                            if (listacalc.get(i + 1).getOperador() == Operadores.SUBTRACAO) {
                                listacalc.get(i + 1).resultado(listacalc.get(i + 2).getNumero().multiply(new BigDecimal("-1")), null);
                                listacalc.remove(i + 2);
                            }
                            listacalc.get(i).resultado(listacalc.get(i - 1).getNumero().divide(listacalc.get(i + 1).getNumero(), 30, RoundingMode.HALF_UP), null);
                            add_and_rem = true;
                            break;
                        case MULTIPLICACAO:
                            if (listacalc.get(i + 1).getOperador() == Operadores.SUBTRACAO) {
                                listacalc.get(i + 1).resultado(listacalc.get(i + 2).getNumero().multiply(new BigDecimal("-1")), null);
                                listacalc.remove(i + 2);
                            }
                            listacalc.get(i).resultado(listacalc.get(i - 1).getNumero().multiply(listacalc.get(i + 1).getNumero()),
                                    null);
                            add_and_rem = true;
                            break;
                        default:
                            break;

                    }
                }

                if (operacao == 3 && listacalc.get(i).getOperador() != null) {
                    // faz a operação e substitui o operador pelo resultado da operação
                    switch (listacalc.get(i).getOperador()) {

                        case ADICAO:
                            listacalc.get(i).resultado(listacalc.get(i - 1).getNumero().add(listacalc.get(i + 1).getNumero()),
                                    null);
                            add_and_rem = true;
                            break;
                        case SUBTRACAO:
                            if (i == 0) {
                                listacalc.get(i).resultado(listacalc.get(i + 1).getNumero().multiply(new BigDecimal("-1")), null);
                                //invalido(i, listacalc);
                                listacalc.remove(i + 1);
                                i = -1;
                                break;
                            }
                            listacalc.get(i).resultado(listacalc.get(i - 1).getNumero().subtract(listacalc.get(i + 1).getNumero()),
                                    null);
                            add_and_rem = true;
                            break;
                        default:
                            break;

                    }
                }

                if (add_and_rem) { // apaga os números da operação
                    listacalc.remove(i + 1);
                    listacalc.remove(i - 1);
                    i = -1;
                    add_and_rem = false;
                }

                if ((operacao == 1) && i == listacalc.size() - 1) {
                    operacao++;
                    i = -1;
                } else if ((operacao == 2) && i == listacalc.size() - 1) {
                    operacao++;
                    i = -1;
                }

            }

        }

    }

}