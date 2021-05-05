package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Restricoes;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.entities.ListaCalc;

public class MainViewController implements Initializable {

	private ListaCalc listaCalc = new ListaCalc();
	
	@FXML
	private Button bt0;

	@FXML
	private Button bt1;

	@FXML
	private Button bt2;

	@FXML
	private Button bt3;

	@FXML
	private Button bt4;

	@FXML
	private Button bt5;

	@FXML
	private Button bt6;

	@FXML
	private Button bt7;

	@FXML
	private Button bt8;

	@FXML
	private Button bt9;

	@FXML
	private Button btPonto;
	
	@FXML
	private Button btParenteses;

	@FXML
	private Button btSoma;

	@FXML
	private Button btSubtracao;

	@FXML
	private Button btMultiplicacao;

	@FXML
	private Button btDivisao;
	
	@FXML
	private Button btPorcentagem;

	@FXML
	private Button btApagar;
	
	@FXML
	private Button btLimpar;

	@FXML
	private Button btResultado;
	
	@FXML
	private Label visor;
	
	@FXML
	private Label visorTemp;

	@FXML
	public void onBt0Action() {
		inserirItens("0", true);
	}

	@FXML
	public void onBt1Action() {
		inserirItens("1", true);
	}

	@FXML
	public void onBt2Action() {
		inserirItens("2", true);
	}

	@FXML
	public void onBt3Action() {
		inserirItens("3", true);
	}

	@FXML
	public void onBt4Action() {
		inserirItens("4", true);
	}

	@FXML
	public void onBt5Action() {
		inserirItens("5", true);
	}

	@FXML
	public void onBt6Action() {
		inserirItens("6", true);
	}

	@FXML
	public void onBt7Action() {
		inserirItens("7", true);
	}

	@FXML
	public void onBt8Action() {
		inserirItens("8", true);
	}

	@FXML
	public void onBt9Action() {
		inserirItens("9", true);
	}

	@FXML
	public void onBtPontoAction() {
		inserirItens(".", true);
	}
	
	@FXML
	public void onBtParenteses() {
		String parenteses = "(";
        if (!(listaCalc.getTextoVisor().length() == 0)) {
            char anterior = listaCalc.getTextoVisor().charAt(listaCalc.getTextoVisor().length() - 1);
            if (anterior == '+' || anterior == '-' || anterior == '×' || anterior == '÷' || anterior == '(') {
                parenteses = "(";
            } else parenteses = ")";
        }
		inserirItens(parenteses, true);
	}

	@FXML
	public void onBtSomaAction() {
		inserirItens("+", true);
	}

	@FXML
	public void onBtSubtracaoAction() {
		inserirItens("-", true);
	}

	@FXML
	public void onBtMultiplicacaoAction() {
		inserirItens("×", true);
	}

	@FXML
	public void onBtDivisaoAction() {
		inserirItens("÷", true);
	}

	@FXML
	public void onBtPorcentagemAction() {
		inserirItens("%", true);
	}
	
	@FXML
	public void onBtApagarAction() {
		if (listaCalc.getTextoVisor().length() > 0) {
            listaCalc.setTextoVisor(listaCalc.getTextoVisor().substring(0, listaCalc.getTextoVisor().length() - 1));
            visorTemp.setText(listaCalc.getTextoVisor());
        }
        else if (listaCalc.getTextoVisor().length() == 0) {
            visorTemp.setText(listaCalc.getTextoVisor());
        }
        inserirItens(null, false);
	}
	
	@FXML
	public void onBtLimparAction() {
		if (!visorTemp.getText().toString().isEmpty()) {
            listaCalc.setTextoVisor("");
            visorTemp.setText(listaCalc.getTextoVisor());
        }
        visor.setText(visorTemp.getText());
	}

	@FXML
	public void onBtResultadoAction() {
		if (!(listaCalc.getTextoVisor().length() == 0)) {
			
            //coloca cálculos feitos no visor secundário
            if(listaCalc.organizarExpressao()) {
            	visorTemp.setText(listaCalc.getTextoVisor());
            	Restricoes.separarNumerosVisor(visorTemp.getText().toString(), visor);
            	listaCalc.setTextoVisor("");
            	
            }
        }
	}

	public void inserirItens(String item, boolean completo) {
		if (completo) {
            listaCalc.concatTextoVisor(item);
            visorTemp.setText(listaCalc.getTextoVisor());
        }
        //exibe na tela
        Restricoes.separarNumerosVisor(listaCalc.getTextoVisor(), visor);
    }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Restricoes.corrigeExpressao(visorTemp, listaCalc);
		Restricoes.controlaParenteses(visorTemp, listaCalc);
		Restricoes.quantidadeCaracteres(visorTemp, listaCalc);
	}

}
