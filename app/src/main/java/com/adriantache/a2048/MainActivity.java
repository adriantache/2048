package com.adriantache.a2048;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
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
    TextView resetTV;

    //placeholder
    TextView blank;

    //define board matrix
    int[][] board = new int[4][4];

    //variable for storing score
    int score = 0;

    //detect first move for highlighting
    boolean firstMove = true;

    //define row and column for adding a new number globally, so we can access them for highlighting
    int row;
    int column;

    //set up touch gestures
    private GestureDetectorCompat mDetector;

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
        resetTV = findViewById(R.id.reset);

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

        //reset all colors
        resetColorFilters();

        //then remove any zeroes and apply coloring
        if (column11.getText().equals("0")) column11.setText(null);
        else colorizeText(column11);
        if (column12.getText().equals("0")) column12.setText(null);
        else colorizeText(column12);
        if (column13.getText().equals("0")) column13.setText(null);
        else colorizeText(column13);
        if (column14.getText().equals("0")) column14.setText(null);
        else colorizeText(column14);
        if (column21.getText().equals("0")) column21.setText(null);
        else colorizeText(column21);
        if (column22.getText().equals("0")) column22.setText(null);
        else colorizeText(column22);
        if (column23.getText().equals("0")) column23.setText(null);
        else colorizeText(column23);
        if (column24.getText().equals("0")) column24.setText(null);
        else colorizeText(column24);
        if (column31.getText().equals("0")) column31.setText(null);
        else colorizeText(column31);
        if (column32.getText().equals("0")) column32.setText(null);
        else colorizeText(column32);
        if (column33.getText().equals("0")) column33.setText(null);
        else colorizeText(column33);
        if (column34.getText().equals("0")) column34.setText(null);
        else colorizeText(column34);
        if (column41.getText().equals("0")) column41.setText(null);
        else colorizeText(column41);
        if (column42.getText().equals("0")) column42.setText(null);
        else colorizeText(column42);
        if (column43.getText().equals("0")) column43.setText(null);
        else colorizeText(column43);
        if (column44.getText().equals("0")) column44.setText(null);
        else colorizeText(column44);

        //apply a blink animation based on which number was last added
        if (firstMove) firstMove = false;
        else {
            if (row >= 0 && row < 4 && column >= 0 && column < 4) {
                if (row == 0 && column == 0)
                    column11.startAnimation(getBlinkAnimation());
                if (row == 0 && column == 1)
                    column12.startAnimation(getBlinkAnimation());
                if (row == 0 && column == 2)
                    column13.startAnimation(getBlinkAnimation());
                if (row == 0 && column == 3)
                    column14.startAnimation(getBlinkAnimation());
                if (row == 1 && column == 0)
                    column21.startAnimation(getBlinkAnimation());
                if (row == 1 && column == 1)
                    column22.startAnimation(getBlinkAnimation());
                if (row == 1 && column == 2)
                    column23.startAnimation(getBlinkAnimation());
                if (row == 1 && column == 3)
                    column24.startAnimation(getBlinkAnimation());
                if (row == 2 && column == 0)
                    column31.startAnimation(getBlinkAnimation());
                if (row == 2 && column == 1)
                    column32.startAnimation(getBlinkAnimation());
                if (row == 2 && column == 2)
                    column33.startAnimation(getBlinkAnimation());
                if (row == 2 && column == 3)
                    column34.startAnimation(getBlinkAnimation());
                if (row == 3 && column == 0)
                    column41.startAnimation(getBlinkAnimation());
                if (row == 3 && column == 1)
                    column42.startAnimation(getBlinkAnimation());
                if (row == 3 && column == 2)
                    column43.startAnimation(getBlinkAnimation());
                if (row == 3 && column == 3)
                    column44.startAnimation(getBlinkAnimation());
            }
        }
    }

    public Animation getBlinkAnimation(){
        Animation animation = new AlphaAnimation(1.0f, 0.5f);   // Change alpha from fully visible to invisible
        animation.setDuration(100);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(1);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }

    //remove all color filters
    private void resetColorFilters() {
        column11.getBackground().setColorFilter(null);
        column12.getBackground().setColorFilter(null);
        column13.getBackground().setColorFilter(null);
        column14.getBackground().setColorFilter(null);
        column21.getBackground().setColorFilter(null);
        column22.getBackground().setColorFilter(null);
        column23.getBackground().setColorFilter(null);
        column24.getBackground().setColorFilter(null);
        column31.getBackground().setColorFilter(null);
        column32.getBackground().setColorFilter(null);
        column33.getBackground().setColorFilter(null);
        column34.getBackground().setColorFilter(null);
        column41.getBackground().setColorFilter(null);
        column42.getBackground().setColorFilter(null);
        column43.getBackground().setColorFilter(null);
        column44.getBackground().setColorFilter(null);
    }

    //add coloured backgrounds to text based on value
    private void colorizeText(TextView view) {
        String value = view.getText().toString();
        int val = Integer.valueOf(value);
        int pow = 0;
        while (val > 1) {
            val /= 2;
            pow++;
        }

        //apply color filter based on power of 2
        switch (pow) {
            case 1:
                view.getBackground().setColorFilter(Color.parseColor("#42A5F5"), PorterDuff.Mode.ADD);
                break;
            case 2:
                view.getBackground().setColorFilter(Color.parseColor("#7E57C2"), PorterDuff.Mode.ADD);
                break;
            case 3:
                view.getBackground().setColorFilter(Color.parseColor("#ef5350"), PorterDuff.Mode.ADD);
                break;
            case 4:
                view.getBackground().setColorFilter(Color.parseColor("#EC407A"), PorterDuff.Mode.ADD);
                break;
            case 5:
                view.getBackground().setColorFilter(Color.parseColor("#26A69A"), PorterDuff.Mode.ADD);
                break;
            case 6:
                view.getBackground().setColorFilter(Color.parseColor("#D4E157"), PorterDuff.Mode.ADD);
                break;
            case 7:
                view.getBackground().setColorFilter(Color.parseColor("#FFCA28"), PorterDuff.Mode.ADD);
                break;
            case 8:
                view.getBackground().setColorFilter(Color.parseColor("#FFA726"), PorterDuff.Mode.ADD);
                break;
            case 9:
                view.getBackground().setColorFilter(Color.parseColor("#E91E63"), PorterDuff.Mode.ADD);
                break;
            case 10:
                view.getBackground().setColorFilter(Color.parseColor("#66BB6A"), PorterDuff.Mode.ADD);
                break;
            case 11:
                view.getBackground().setColorFilter(Color.parseColor("#FFEB3B"), PorterDuff.Mode.ADD);
                break;
            default:
                view.getBackground().setColorFilter(Color.parseColor("#607D8B"), PorterDuff.Mode.ADD);
                break;
        }

        //call for text resize here, since we already know which view it applies to
        resizeText(view, value.length());
    }

    //set text size based on length of the number
    private void resizeText(TextView view, int length) {
        //modify text value to make sure values fit
        switch (length) {
            case 1:
                view.setTextSize(32);
                break;
            case 2:
                view.setTextSize(32);
                break;
            case 3:
                view.setTextSize(32);
                break;
            case 4:
                view.setTextSize(24);
                break;
            case 5:
                view.setTextSize(20);
                break;
            default:
                view.setTextSize(18);
                break;
        }
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
    private boolean detectImpasse(){
        if (!detectFullBoard()) return false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == board[i+1][j] || board[i][j] == board[i][j+1]) return false;
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
