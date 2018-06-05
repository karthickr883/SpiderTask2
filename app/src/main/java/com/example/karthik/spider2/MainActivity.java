package com.example.karthik.spider2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    String[] animals = {"tiger", "lion", "giraffe", "monkey", "cheetah", "leopard", "jaguar", "zebra", "cat", "dog", "cow"};
    Button game, check, newGame;
    int k, length, flag, best_score = 0, count;
    EditText guess;
    TextView guessed, currentScore, bestScore;
    ImageView imageView;
    int[] images = {R.drawable.hang0, R.drawable.hang1, R.drawable.hang2, R.drawable.hang3, R.drawable.hang4, R.drawable.hang5};
    // first is a variable to check whether the game is played for the first time.
    private int first = 0;
    char[] singleAnimal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        guessed = (TextView) findViewById(R.id.guessed);
        imageView = (ImageView) findViewById(R.id.hangman);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.words);
        game = (Button) findViewById(R.id.game);
        newGame = (Button) findViewById(R.id.newGame);
        check = (Button) findViewById(R.id.check);
        currentScore = (TextView) findViewById(R.id.currentScore);
        guess = (EditText) findViewById(R.id.guess);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "You will get a new word", Toast.LENGTH_SHORT).show();
                game.callOnClick();
            }
        });
        game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                imageView.setVisibility(View.INVISIBLE);
                game.setVisibility(View.INVISIBLE);
                layout.removeAllViews();
                Random random = new Random();
                k = random.nextInt(animals.length);

                length = animals[k].length();

                singleAnimal = animals[k].toCharArray();
                final TextView[] animal = new TextView[length];

                for (int i = 0; i < length; i++) {
                    animal[i] = new TextView(MainActivity.this);
                    layout.addView(animal[i]);
                    animal[i].setText("_ ");
                }

                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageView.setVisibility(View.VISIBLE);
                        flag = 0;
                        char checkalpha = guess.getText().toString().charAt(0);
                        Log.d("Check h", guess.getText().toString());
                        for (int i = 0; i < length; i++) {

                            if (singleAnimal[i] == Character.toLowerCase(checkalpha)) {
                                animal[i].setTypeface(null, Typeface.BOLD);
                                animal[i].setText(String.valueOf(singleAnimal[i]));
                                flag++;
                                count++;
                            }

                        }
                        Log.d("Checking count", String.valueOf(count));
                        if (count == length) {
                            if (best_score < getBest_score())
                                Toast.makeText(MainActivity.this, "Congrats new high score", Toast.LENGTH_SHORT).show();
                            // This alert dialog is to show when the user has guessed correctly.
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setMessage("Congrats want to play again or exit ? ");
                            alert.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    game.callOnClick();
                                }
                            });

                            alert.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    System.exit(0);
                                }
                            });
                            alert.show();
                        }
                        if (flag == 0) {
                            if (best_score == 5) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                                dialog.setMessage("You have lost");
                                dialog.setPositiveButton("Play again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        game.performClick();
                                    }
                                });
                                dialog.setNegativeButton("Exit game", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                });
                                dialog.show();
                            }
                            guessed.append(String.valueOf(Character.toLowerCase(checkalpha)) + ",");
                            imageView.setImageResource(images[best_score]);
                            best_score++;


                            currentScore.setText("Current Score : " + String.valueOf(best_score));
                        }
                        Log.d("Checking best score", String.valueOf(getBest_score()));
                        if (best_score <= getBest_score())
                            calculateCurrentScore(best_score);
                        guess.setText("");

                    }
                });
                if (first != 0)
                    calculateBestScore(getCurrentScore());

                guessed.setText("Incorrect guesses :");
                if (best_score <= getBest_score() && first != 0) {

                    bestScore = (TextView) findViewById(R.id.bestScore);
                    bestScore.setText("Best Score : " + String.valueOf(getBest_score()));
                }

                best_score = 0;
                first++;
                currentScore.setText("Current Score : ");
            }
        });


        if (best_score <= getBest_score() && getBest_score() != 10) {

            bestScore = (TextView) findViewById(R.id.bestScore);
            bestScore.setText("Best Score : " + String.valueOf(getBest_score()));
        }
    }

    private void calculateBestScore(int best_score) {
        SharedPreferences sharedPreferences = getSharedPreferences("Saved file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Bestscore", best_score);
        editor.apply();
    }

    private int getBest_score() {
        SharedPreferences sharedPreferences = getSharedPreferences("Saved file", Context.MODE_PRIVATE);

        int score = sharedPreferences.getInt("Bestscore", 10);

        return score;

    }

    private void calculateCurrentScore(int current_score) {
        SharedPreferences sharedPreferences = getSharedPreferences("Saved file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CurrentScore", current_score);
        editor.apply();
    }

    private int getCurrentScore() {
        SharedPreferences sharedPreferences = getSharedPreferences("Saved file", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("CurrentScore", 0);
    }

}

