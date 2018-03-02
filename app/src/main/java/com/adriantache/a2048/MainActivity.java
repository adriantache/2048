package com.adriantache.a2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //define TextViews
    TextView column11;
    TextView column12;
    TextView column13;
    TextView column14;
    TextView column21;
    TextView column22;
    TextView column23;
    TextView column24;
    TextView column31;
    TextView column32;
    TextView column33;
    TextView column34;
    TextView column41;
    TextView column42;
    TextView column43;
    TextView column44;
    TextView scoreTV;

    //define board matrix
    int[][] board = new int[4][4];

    //triggers to determine board is stuck => gameLoss()
    boolean cannotMoveDown = false;
    boolean cannotMoveUp = false;
    boolean cannotMoveRight = false;
    boolean cannotMoveLeft = false;

    //variable for storing score
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find TextViews to display the game
        column11 = findViewById(R.id.column11);
        column12 = findViewById(R.id.column12);
        column13 = findViewById(R.id.column13);
        column14 = findViewById(R.id.column14);
        column21 = findViewById(R.id.column21);
        column22 = findViewById(R.id.column22);
        column23 = findViewById(R.id.column23);
        column24 = findViewById(R.id.column24);
        column31 = findViewById(R.id.column31);
        column32 = findViewById(R.id.column32);
        column33 = findViewById(R.id.column33);
        column34 = findViewById(R.id.column34);
        column41 = findViewById(R.id.column41);
        column42 = findViewById(R.id.column42);
        column43 = findViewById(R.id.column43);
        column44 = findViewById(R.id.column44);
        scoreTV = findViewById(R.id.score);

        //spawn first two values
        generateNewNumbers(2);

        //display the board for a new game
        updateScores();
    }

    //todo set text size based on value
    //set all scores based on the board values
    private void updateScores() {
        //set all values first to ensure values that change to zero are reset
        column13.setText(String.valueOf(board[0][2]));
        column12.setText(String.valueOf(board[0][1]));
        column11.setText(String.valueOf(board[0][0]));
        column14.setText(String.valueOf(board[0][3]));
        column21.setText(String.valueOf(board[1][0]));
        column22.setText(String.valueOf(board[1][1]));
        column23.setText(String.valueOf(board[1][2]));
        column24.setText(String.valueOf(board[1][3]));
        column31.setText(String.valueOf(board[2][0]));
        column32.setText(String.valueOf(board[2][1]));
        column33.setText(String.valueOf(board[2][2]));
        column34.setText(String.valueOf(board[2][3]));
        column41.setText(String.valueOf(board[3][0]));
        column42.setText(String.valueOf(board[3][1]));
        column43.setText(String.valueOf(board[3][2]));
        column44.setText(String.valueOf(board[3][3]));

        //then remove any zeroes
        if (column11.getText().equals("0")) column11.setText(null);
        if (column12.getText().equals("0")) column12.setText(null);
        if (column13.getText().equals("0")) column13.setText(null);
        if (column14.getText().equals("0")) column14.setText(null);
        if (column21.getText().equals("0")) column21.setText(null);
        if (column22.getText().equals("0")) column22.setText(null);
        if (column23.getText().equals("0")) column23.setText(null);
        if (column24.getText().equals("0")) column24.setText(null);
        if (column31.getText().equals("0")) column31.setText(null);
        if (column32.getText().equals("0")) column32.setText(null);
        if (column33.getText().equals("0")) column33.setText(null);
        if (column34.getText().equals("0")) column34.setText(null);
        if (column41.getText().equals("0")) column41.setText(null);
        if (column42.getText().equals("0")) column42.setText(null);
        if (column43.getText().equals("0")) column43.setText(null);
        if (column44.getText().equals("0")) column44.setText(null);
    }

    //generate the next number in order to continue the game
    private void generateNewNumbers(int amount) {
        Random random = new java.util.Random();

        while (amount > 0 && !detectFullBoard()) {

            int row = random.nextInt(4);
            int column = random.nextInt(4);

            if (board[row][column] == 0) {
                int number = random.nextInt(10);
                //giving a 10% chance to spawn the number 4
                if (number == 9) number = 4;
                else number = 2;

                board[row][column] = number;
                amount--;
            }
        }

    }

    //detect if the board is full in order to prevent overwriting
    @org.jetbrains.annotations.Contract(pure = true)
    private boolean detectFullBoard() {
        boolean fullBoard = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) fullBoard = false;
            }
        }

        return fullBoard;
    }

    //react to button down
    public void btnDown(View v) {
        boolean boardMoved = false;

        //check for duplicates to sum up
        for (int j = 0; j < 4; j++) {
            for (int i = 3; i >= 0; i--) {
                //merge the numbers if they match
                if (board[i][j] != 0 && i > 0) {
                    for (int k = i - 1; k >= 0; k--) {
                        if (board[k][j] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            score+=temp;
                            updateScore();
                            if (temp == 2048) gameWon();
                            board[i][j] = temp;
                            board[k][j] = 0;
                            boardMoved = true;
                            break;
                        } else if (board[k][j] != 0) {
                            break;
                        }
                    }
                }
            }
        }

        //remove blank spaces between cells
        for (int j = 0; j < 4; j++) {
            int counter = 3;

            for (int i = 3; i >= 0; i--) {
                if (board[i][j] != 0) {
                    board[counter][j] = board[i][j];
                    if (counter != i) {
                        boardMoved = true;
                        board[i][j] = 0;
                    }
                    counter--;
                }
            }
        }

        //test if board actually "moved"
        if (!boardMoved) {
            //set flag to determine game loss possibility
            cannotMoveDown = true;
            //test for game loss
            gameLoss();
        } else {
            //reset flag to determine game loss possibility
            cannotMoveDown = false;

            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //react to button up
    public void btnUp(View v) {
        boolean boardMoved = false;

        //check for duplicates to sum up
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                //merge the numbers if they match
                if (board[i][j] != 0 && i < 3) {
                    for (int k = i + 1; k < 4; k++) {
                        if (board[k][j] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            score+=temp;
                            updateScore();
                            if (temp == 2048) gameWon();
                            board[i][j] = temp;
                            board[k][j] = 0;
                            boardMoved = true;
                            break;
                        } else if (board[k][j] != 0) {
                            break;
                        }
                    }
                }
            }
        }

        //remove blank spaces between cells
        for (int j = 0; j < 4; j++) {
            int counter = 0;

            for (int i = 0; i < 4; i++) {
                if (board[i][j] != 0) {
                    board[counter][j] = board[i][j];
                    if (counter != i) {
                        boardMoved = true;
                        board[i][j] = 0;
                    }
                    counter++;
                }
            }
        }

        //test if board actually "moved"
        if (!boardMoved) {
            //set flag to determine game loss possibility
            cannotMoveUp = true;
            //test for game loss
            gameLoss();
        } else {
            //reset flag to determine game loss possibility
            cannotMoveUp = false;

            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //react to button left
    public void btnLeft(View v) {
        boolean boardMoved = false;

        //check for duplicates to sum up
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                //merge the numbers if they match
                if (board[i][j] != 0 && j < 3) {
                    for (int k = j + 1; k < 4; k++) {
                        if (board[i][k] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            score+=temp;
                            updateScore();
                            if (temp == 2048) gameWon();
                            board[i][j] = temp;
                            board[i][k] = 0;
                            boardMoved = true;
                            break;
                        } else if (board[i][k] != 0) {
                            break;
                        }
                    }
                }
            }
        }

        //remove blank spaces between cells
        for (int i = 0; i < 4; i++) {
            int counter = 0;

            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    board[i][counter] = board[i][j];
                    if (counter != j) {
                        boardMoved = true;
                        board[i][j] = 0;
                    }
                    counter++;
                }
            }
        }

        //test if board actually "moved"
        if (!boardMoved) {
            //set flag to determine game loss possibility
            cannotMoveLeft = true;
            //test for game loss
            gameLoss();
        } else {
            //reset flag to determine game loss possibility
            cannotMoveLeft = false;

            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //react to button right
    public void btnRight(View v) {
        boolean boardMoved = false;

        //check for duplicates to sum up
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >= 0; j--) {
                //merge the numbers if they match
                if (board[i][j] != 0 && j > 0) {
                    for (int k = j - 1; k >= 0; k--) {
                        if (board[i][k] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            score+=temp;
                            updateScore();
                            if (temp == 2048) gameWon();
                            board[i][j] = temp;
                            board[i][k] = 0;
                            boardMoved = true;
                            break;
                        } else if (board[i][k] != 0) {
                            break;
                        }
                    }
                }
            }
        }

        //remove blank spaces between cells
        for (int i = 0; i < 4; i++) {
            int counter = 3;

            for (int j = 3; j >= 0; j--) {
                if (board[i][j] != 0) {
                    board[i][counter] = board[i][j];
                    if (counter != j) {
                        boardMoved = true;
                        board[i][j] = 0;
                    }
                    counter--;
                }
            }
        }

        //test if board actually "moved"
        if (!boardMoved) {
            //set flag to determine game loss possibility
            cannotMoveRight = true;
            //test for game loss
            gameLoss();
        } else {
            //reset flag to determine game loss possibility
            cannotMoveRight = false;

            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //todo disable or replace buttons
    //detect if game is lost
    private void gameLoss() {
        if (cannotMoveDown && cannotMoveLeft && cannotMoveRight && cannotMoveUp) {
            String result = "Game Over\nScore: "+score;
            scoreTV.setText(result);
        }
    }

    //detect if game is won
    private void gameWon() {
        if (cannotMoveDown && cannotMoveLeft && cannotMoveRight && cannotMoveUp) {
            String result = "Game Won\nScore: "+score;
            scoreTV.setText(result);
        }
    }

    //display score
    private void updateScore(){
        String result = "Score: "+score;
        scoreTV.setText(result);
    }

    //todo implement color coding for numbers

    //todo implement reset function

    //todo implement swiping gestures

    //todo highlight added number

    //todo implement color highlighting system

}
