package com.adriantache.a2048;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //define TextViews and GridView
    GridView gridView;
    TextView scoreTV;
    TextView resetTV;

    //placeholder
    TextView blank;

    //define board matrix and its flat counterpart
    int[][] board = new int[4][4];
    String[] flatBoard;

    //variable for storing score
    int score = 0;

    //detect first move for highlighting
    boolean firstMove = true;

    //define row and column for adding a new number globally, so we can access them for highlighting
    int row;
    int column;

    //set up array adapter to populate grid view
    ArrayAdapter<String> adapter;

    //set up touch gestures
    GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find TextViews to display the game
        scoreTV = findViewById(R.id.score);
        resetTV = findViewById(R.id.reset);
        gridView = findViewById(R.id.gridView);

        //spawn first two values
        generateNewNumbers(2);

        //display the board for a new game
        updateScores();

        //set up touch gestures
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
    }

    //capture touches https://developer.android.com/training/gestures/movement.html
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void interpretTouch(float velocityX, float velocityY) {
        if (Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityX) > 100) {
            if (velocityX > 0) btnRight(blank);
            else btnLeft(blank);
        } else if (Math.abs(velocityY) > 100) {
            if (velocityY < 0) btnUp(blank);
            else btnDown(blank);
        }
    }

    //flatten board to display using ArrayAdapter
    private void flattenBoard() {
        flatBoard = new String[16];
        int index = 0;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    flatBoard[index] = String.valueOf(board[i][j]);
                } else {
                    flatBoard[index] = "";
                }
                index++;
            }
        }
    }

    //set all scores based on the board values
    private void updateScores() {
        flattenBoard();
        adapter = new ArrayAdapter<>(this, R.layout.grid_text_view, flatBoard);
        adapter.notifyDataSetChanged();
        gridView.setAdapter(adapter);

        //set all values first to ensure values that change to zero are reset
        //todo reset all colors
        //todo then remove any zeroes and apply coloring
        //todo apply a blink animation based on which number was last added
    }

    //generate the next number in order to continue the game
    private void generateNewNumbers(int amount) {
        Random random = new java.util.Random();

        while (amount > 0 && !detectFullBoard()) {

            row = random.nextInt(4);
            column = random.nextInt(4);

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

    //detect if all adjacent numbers are unique
    private boolean detectImpasse() {
        if (!detectFullBoard()) return false;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == board[i + 1][j] || board[i][j] == board[i][j + 1]) return false;
            }
        }

        return true;
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
                            score += temp;
                            updateTotalScore();
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
            //test for game loss
            gameLoss();
        } else {
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
                            score += temp;
                            updateTotalScore();
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
            //test for game loss
            gameLoss();
        } else {
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
                            score += temp;
                            updateTotalScore();
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
            //test for game loss
            gameLoss();
        } else {
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
                            score += temp;
                            updateTotalScore();
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
            //test for game loss
            gameLoss();
        } else {
            //add one more number
            generateNewNumbers(1);

            //update display
            updateScores();
        }
    }

    //detect if game is lost
    private void gameLoss() {
        if (detectImpasse()) {
            String result = "Game Over\nScore: " + score;
            scoreTV.setText(result);
            resetTV.setVisibility(View.VISIBLE);
        }
    }

    //detect if game is won
    private void gameWon() {
        String result = "Game Won\nScore: " + score;
        scoreTV.setText(result);
        resetTV.setVisibility(View.VISIBLE);
    }

    //display score
    private void updateTotalScore() {
        String result = "Score: " + score;
        scoreTV.setText(result);
    }

    //reset game
    public void reset(View view) {
        //define board matrix
        board = new int[4][4];

        //variable for storing score
        score = 0;

        //spawn first two values
        generateNewNumbers(2);

        //display the board for a new game
        updateScores();

        //reset score display
        updateTotalScore();

        //hide the reset button
        resetTV.setVisibility(View.INVISIBLE);

        //detect first move for highlighting
        firstMove = true;
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            interpretTouch(velocityX, velocityY);
            return true;
        }
    }
}
