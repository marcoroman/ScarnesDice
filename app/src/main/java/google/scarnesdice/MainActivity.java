package google.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random gen = new Random();
    ImageView face;
    TextView score, winnerText;

    ArrayList<Integer> faces = new ArrayList<>();

    private int uTotal = 0, uTurn = 0, cTotal = 0, cTurn = 0, rollValue;
    long startTime = 0;

    android.os.Handler timerHandler = new android.os.Handler();

    Runnable timerRunnable = new Runnable(){
        public void run(){
            score.setText("Computer's turn.");
            computerHelper();

            timerHandler.postDelayed(this, 500);

            if(rollValue == 1 || cTurn >= 20) {
                timerHandler.removeCallbacks(timerRunnable);
                endComputerTurn();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        face = (ImageView) findViewById(R.id.dieFace);
        score = (TextView) findViewById(R.id.userView);
        winnerText = (TextView) findViewById(R.id.winnerText);
        winnerText.setVisibility(View.INVISIBLE);

        faces.add(R.drawable.dice1);
        faces.add(R.drawable.dice2);
        faces.add(R.drawable.dice3);
        faces.add(R.drawable.dice4);
        faces.add(R.drawable.dice5);
        faces.add(R.drawable.dice6);
    }

    public void roll(View view){
        rollValue = gen.nextInt(6) + 1;
        face.setImageResource(faces.get(rollValue - 1));

        if(rollValue != 1){
            uTurn += rollValue;
        }else{
            uTurn = 0;
            computerTurn();
        }

        updateText();
    }

    public void reset(View view){
        uTotal = 0;
        uTurn = 0;
        cTotal = 0;
        cTurn = 0;

        face.setImageResource(R.drawable.dice1);
        findViewById(R.id.rollButton).setEnabled(true);
        findViewById(R.id.holdButton).setEnabled(true);
        winnerText.setVisibility(View.INVISIBLE);

        updateText();
    }

    public void hold(View view){
        uTotal += uTurn;
        uTurn = 0;

        updateText();

        if(uTotal >= 100) {
            winner("User");
        }else {
            computerTurn();
        }
    }

    public void computerTurn(){
        findViewById(R.id.rollButton).setEnabled(false);
        findViewById(R.id.resetButton).setEnabled(false);

        rollValue = 0;

        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 500);
    }

    public void computerHelper(){
        rollValue = gen.nextInt(6) + 1;
        face.setImageResource(faces.get(rollValue - 1));

        if(rollValue != 1){
            cTurn += rollValue;
        }else{
            cTurn = 0;
        }
    }

    public void endComputerTurn(){
        cTotal += cTurn;
        cTurn = 0;

        findViewById(R.id.rollButton).setEnabled(true);
        findViewById(R.id.resetButton).setEnabled(true);

        updateText();

        if(cTotal >= 100)
            winner("Computer");
    }

    public void updateText(){
        score.setText("Your score: " + uTotal +
                        " Computer Score: " + cTotal +
                        " Your turn score: " + uTurn);
    }

    public void winner(String w){
        findViewById(R.id.rollButton).setEnabled(false);
        findViewById(R.id.holdButton).setEnabled(false);
        winnerText.setVisibility(View.VISIBLE);
        winnerText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        winnerText.setText("WINNER!\nCongratulations, " + w);
    }
}