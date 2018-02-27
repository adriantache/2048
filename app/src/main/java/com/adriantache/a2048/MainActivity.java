package com.adriantache.a2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //define game display TextViews
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
    //define board matrix
    int[][] board = new int[4][4];
    //mark board full if appropriate
    boolean boardFull = false;
    //triggers to determine board is stuck
    boolean cannotMoveDown = false;
    boolean cannotMoveUp = false;
    boolean cannotMoveRight = false;
    boolean cannotMoveLeft = false;

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

        //spawn first two values
        generateNewNumbers(2);

        //display the board for a new game
        updateScores();
    }

    //set all scores based on the board values
    private void updateScores() {
        if (board[0][0] != 0) column11.setText(String.valueOf(board[0][0]));
        if (board[0][1] != 0) column12.setText(String.valueOf(board[0][1]));
        if (board[0][2] != 0) column13.setText(String.valueOf(board[0][2]));
        if (board[0][3] != 0) column14.setText(String.valueOf(board[0][3]));
        if (board[1][0] != 0) column21.setText(String.valueOf(board[1][0]));
        if (board[1][1] != 0) column22.setText(String.valueOf(board[1][1]));
        if (board[1][2] != 0) column23.setText(String.valueOf(board[1][2]));
        if (board[1][3] != 0) column24.setText(String.valueOf(board[1][3]));
        if (board[2][0] != 0) column31.setText(String.valueOf(board[2][0]));
        if (board[2][1] != 0) column32.setText(String.valueOf(board[2][1]));
        if (board[2][2] != 0) column33.setText(String.valueOf(board[2][2]));
        if (board[2][3] != 0) column34.setText(String.valueOf(board[2][3]));
        if (board[3][0] != 0) column41.setText(String.valueOf(board[3][0]));
        if (board[3][1] != 0) column42.setText(String.valueOf(board[3][1]));
        if (board[3][2] != 0) column43.setText(String.valueOf(board[3][2]));
        if (board[3][3] != 0) column44.setText(String.valueOf(board[3][3]));
    }

    private void generateNewNumbers(int amount) {
        Random random = new java.util.Random();

        while (amount > 0 && !boardFull) {
            detectFullBoard();

            int row = random.nextInt(4);
            int column = random.nextInt(4);

            if (board[row][column] == 0) {
                int number = random.nextInt(2);
                number += 1;
                number *= 2;

                board[row][column] = number;
                amount--;
            }
        }

    }

    private void detectFullBoard() {
        boolean fullBoard = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) fullBoard = false;
            }
        }

        if (fullBoard) boardFull = true;
        else boardFull = false;
    }

    //react to button down (starting with this since it's the simplest case)
    public void btnDown(View v) {
        //create a new matrix to temporarily store values
        int[][] boardCopy = new int[4][4];

        //determine if board has moved
        boolean boardMoved = false;

        Log.i(TAG, "boardCopy init: " + Arrays.deepToString(boardCopy));

        for (int j = 0; j < 4; j++) {
            for (int i = 3; i > 0; i--) {
                boolean changed = false;
                //change the numbers if they match
                if (board[i][j] != 0) {
                    for (int k = i - 1; k >= 0; k--) {
                        if (board[k][j] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            board[k][j] = 0;
                            boardCopy[k][j] = temp;
                            boardMoved = true;
                            changed = true;
                            Log.i(TAG, "btnDown: duplicate");
                            break;
                        }
                    }
                }
                Log.i(TAG, "boardCopy: " + j + Arrays.deepToString(boardCopy));
                //if we haven't changed anything, go ahead and copy the value to boardCopy
                if (!changed) boardCopy[i][j] = board[i][j];
            }
        }

        Log.i(TAG, "boardCopy before removing blanks: " + Arrays.deepToString(boardCopy));

        //better system to remove blank spaces between cells
        for (int j = 0; j < 4; j++) {
            int counter = 3;

            for (int i = 3; i >= 0; i--) {
                if (boardCopy[i][j] != 0) {
                    boardCopy[counter][j] = boardCopy[i][j];
                    boardCopy[i][j] = 0;
                    if (counter != i) boardMoved = true;
                    counter--;
                }
            }
        }

        Log.i(TAG, "boardCopy: " + Arrays.deepToString(boardCopy));

        //test if board actually "moved"
        if (!boardMoved) {
            //set flag to determine game loss possibility
            cannotMoveDown = true;

            //test for game loss
            gameLoss();
        } else {
            cannotMoveDown = false;

            //copy all values back to main board
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                board[i][j] = boardCopy[i][j];
                }
            }

            Log.i(TAG, "board: " + Arrays.deepToString(board));

            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //react to button up
    public void btnUp(View v) {

    }

    //react to button left
    public void btnLeft(View v) {

    }

    //react to button right
    public void btnRight(View v) {

    }

    private void gameLoss() {
        if (cannotMoveDown && cannotMoveLeft && cannotMoveRight && cannotMoveUp) {
            TextView score = findViewById(R.id.score);
            score.setText("Game Over");
        }
    }

}
