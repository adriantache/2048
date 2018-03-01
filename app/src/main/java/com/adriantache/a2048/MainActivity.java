package com.adriantache.a2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
                int number = random.nextInt(2);
                number += 1;
                number *= 2;

                board[row][column] = number;
                amount--;
            }
        }

    }

    //detect if the board is full in order to prevent overwriting
    private boolean detectFullBoard() {
        boolean fullBoard = true;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) fullBoard = false;
            }
        }

        return fullBoard;
    }

    //react to button down (starting with this since it's the simplest case)
    public void btnDown(View v) {
        boolean boardMoved = false;

        //create a new matrix to temporarily store values
        //int[][] boardCopy = new int[4][4];

        //check for duplicates to sum up
        for (int j = 0; j < 4; j++) {
            for (int i = 3; i >= 0; i--) {
                //boolean changed = false;

                //change the numbers if they match
                if (board[i][j] != 0 && i > 0) {
                    for (int k = i - 1; k >= 0; k--) {
                        if (board[k][j] == board[i][j]) {
                            int temp = board[i][j] * 2;
                            board[i][j] = temp;
                            board[k][j] = 0;
                            //boardCopy[k][j] = temp;
                            boardMoved = true;
                            //changed = true;
                            break;
                        } else if (board[k][j] != 0) break;
                    }
                }

                //if we haven't changed anything, go ahead and copy the value to boardCopy
                //if (!changed) boardCopy[i][j] = board[i][j];
            }
        }

        //better system to remove blank spaces between cells
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

            //copy all values back to main board
//            for (int i = 0; i < 4; i++) {
//                for (int j = 0; j < 4; j++) {
//                    board[i][j] = boardCopy[i][j];
//                }
//                //todo replace with arraycopy when everything works fine
//                //System.arraycopy(boardCopy,0, board, 0, 4);
//            }

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
