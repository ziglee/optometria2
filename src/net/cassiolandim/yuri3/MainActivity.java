package net.cassiolandim.yuri3;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private CountDownTimer timer;
	private int pontuacao = 0;
	private int fase;
	private int estado;
	private int tempo = 5000;
	private TextView instrucoesTextView;
	private TextView pontuacaoTextView;
	private TextView primeiroTextView;
	private TextView segundoTextView;
	private TextView tempoTextView;
	private String primeiraParte;
	private String segundaParte;
	private Button actionButton;
	private Button opcao1Button;
	private Button opcao2Button;
	private Button opcao3Button;
	private String[] respostas = new String[3];

	private static final Random r = new Random();
	private static final int ESTADO_JOGANDO = 1;
	private static final int ESTADO_PRONTO = 2;

	private static final Character[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Y', 'Z' };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		estado = ESTADO_PRONTO;

		instrucoesTextView = (TextView) findViewById(R.id.instrucoesTextView);
		pontuacaoTextView = (TextView) findViewById(R.id.pontuacaoTextView);
		tempoTextView = (TextView) findViewById(R.id.tempoTextView);
		primeiroTextView = (TextView) findViewById(R.id.primeiroTextView);
		segundoTextView = (TextView) findViewById(R.id.segundoTextView);
		actionButton = (Button) findViewById(R.id.actionButton);
		opcao1Button = (Button) findViewById(R.id.opcao1Button);
		opcao2Button = (Button) findViewById(R.id.opcao2Button);
		opcao3Button = (Button) findViewById(R.id.opcao3Button);

		actionButton.setOnClickListener(this);
		opcao1Button.setOnClickListener(this);
		opcao2Button.setOnClickListener(this);
		opcao3Button.setOnClickListener(this);
	}

	private void setupObjetivo() {
		fase = 1;
		estado = ESTADO_JOGANDO;
				
		primeiraParte = LETTERS[randomCharacter()].toString() + LETTERS[randomCharacter()].toString();
		segundaParte = LETTERS[randomCharacter()].toString() + LETTERS[randomCharacter()].toString();

		int respostaCerta = showRandomInteger(0, 2);
		for (int x = 0; x <= 2; x++) {
			if (x == respostaCerta) {
				respostas[x] = primeiraParte + segundaParte;
			} else {
				String wrongAnswer = LETTERS[randomCharacter()].toString() + LETTERS[randomCharacter()].toString()
						+ LETTERS[randomCharacter()].toString() + LETTERS[randomCharacter()].toString();
				respostas[x] = wrongAnswer;
			}
		}

		opcao1Button.setVisibility(View.GONE);
		opcao2Button.setVisibility(View.GONE);
		opcao3Button.setVisibility(View.GONE);
		primeiroTextView.setText(primeiraParte);
		segundoTextView.setText(segundaParte);
		primeiroTextView.setVisibility(View.VISIBLE);
		segundoTextView.setVisibility(View.INVISIBLE);
		instrucoesTextView.setVisibility(View.INVISIBLE);
		segundoTextView.setTextSize(TypedValue.COMPLEX_UNIT_MM, randomFontSize());
		actionButton.setText("Parar");

		if (timer != null)
			timer.cancel();

		timer = new CountDownTimer(5000, 500) {
			public void onTick(long millisUntilFinished) {
				tempoTextView.setText(String.valueOf(millisUntilFinished / 1000));
			}
			public void onFinish() {
				tempoTextView.setText("0");
				mudaPraSegundaFase();
			}
		}.start();
	}

	private int randomCharacter() {
		return showRandomInteger(0, 24);
	}

	private static int showRandomInteger(int aStart, int aEnd) {
		if (aStart > aEnd)
			throw new IllegalArgumentException("Start cannot exceed End.");

		// get the range, casting to long to avoid overflow problems
		long range = (long) aEnd - (long) aStart + 1;

		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * r.nextDouble());
		return (int) (fraction + aStart);
	}

	private static float randomFontSize() {
		int integerValue = showRandomInteger(0, 10);
		if (integerValue < 5)
			return 1.4f;
		else
			return 2.8f;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.opcao1Button) {
			clicouOpcao((Button) v);
		} else if (id == R.id.opcao2Button) {
			clicouOpcao((Button) v);
		} else if (id == R.id.opcao3Button) {
			clicouOpcao((Button) v);
		} else if (id == R.id.actionButton) {
			if (estado == ESTADO_PRONTO) {
				pontuacao = 0;
				pontuacaoTextView.setText("0");
				tempoTextView.setText("5");
				setupObjetivo();
			} else if (estado == ESTADO_JOGANDO) {
				fimDeJogo();
			}
		}
	}

	private void fimDeJogo() {
		if (timer != null)
			timer.cancel();
		
		estado = ESTADO_PRONTO;
		tempoTextView.setText("5");
		actionButton.setText("Iniciar");
		opcao1Button.setVisibility(View.GONE);
		opcao2Button.setVisibility(View.GONE);
		opcao3Button.setVisibility(View.GONE);
		primeiroTextView.setVisibility(View.INVISIBLE);
		segundoTextView.setVisibility(View.INVISIBLE);
		instrucoesTextView.setVisibility(View.VISIBLE);
	}

	private void mudaPraSegundaFase() {
		if (fase == 1) {
			fase = 2;

			opcao1Button.setText(respostas[0]);
			opcao2Button.setText(respostas[1]);
			opcao3Button.setText(respostas[2]);

			opcao1Button.setVisibility(View.VISIBLE);
			opcao2Button.setVisibility(View.VISIBLE);
			opcao3Button.setVisibility(View.VISIBLE);
			
			primeiroTextView.setVisibility(View.INVISIBLE);
			segundoTextView.setVisibility(View.VISIBLE);

			if (timer != null)
				timer.cancel();

			timer = new CountDownTimer(tempo, 500) {
				public void onTick(long millisUntilFinished) {
					tempoTextView.setText(String.valueOf(millisUntilFinished / 1000));
				}
				public void onFinish() {
					tempoTextView.setText("0");
					fimDeJogo();
				}
			}.start();
		}
	}

	private void clicouOpcao(Button button) {
		boolean acertou = button.getText().toString().equals(primeiraParte + segundaParte);
		if (acertou) {
			if (timer != null)
				timer.cancel();
			tempo -= 150;
			pontuacao++;
			pontuacaoTextView.setText(String.valueOf(pontuacao));
			setupObjetivo();
		}
	}
}