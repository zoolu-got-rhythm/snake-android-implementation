package com.example.slurp.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;

public class SnakeGameView extends View implements Observer{
    private Game currentGameState;
    private android.os.Handler mHandler;
    private Paint mPaint;
    private int width, height;

    public SnakeGameView(Context context, int width, int height) {
        super(context);
        this.mHandler = new android.os.Handler();

        this.width = width;
        this.height = height;



        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(this.width + 36,
                this.height + 36);

        this.setLayoutParams(layoutParams);
//        this.setBackgroundColor(Color.BLACK);

        this.mPaint = new Paint();
        this.mPaint.setColor(getResources().getColor(R.color.pink));

//        this.mPaint.setStrokeWidth(16f);
//        this.mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        // draw
//            canvas.drawCircle(tap.getPoint().x, tap.getPoint().y, tap.getCurrentRadius(), this.mPaint);

        Paint mBackgroundTilePaint = new Paint();
        mBackgroundTilePaint.setColor(Color.DKGRAY);

        int squareSize = this.width / this.currentGameState.getBoard().xTiles;
        System.out.println(squareSize);
        Log.d("square size", Integer.toString(squareSize));
        for(int i = 0; i <= this.currentGameState.getBoard().xTiles; i++){
            for(int j = 0; j <= this.currentGameState.getBoard().yTiles; j++){
                canvas.drawRect(new Rect(i * squareSize,
                                j * squareSize, (i * squareSize) + squareSize - 2,
                                (j * squareSize) + squareSize - 2),
                        mBackgroundTilePaint);
            }
        }


            Paint redPaint = new Paint();
            redPaint.setColor(Color.RED);
            Point apple = this.currentGameState.getCurrentApple() != null ?
                    this.currentGameState.getCurrentApple().getPos() : null;


            if(apple != null)
                canvas.drawRect(new Rect(apple.x * squareSize,
                        apple.y * squareSize, (apple.x * squareSize) + squareSize - 2,
                        (apple.y * squareSize) + squareSize - 2), redPaint);


            for (Point p : this.currentGameState.getPlayerSnake().getHeadAndBody()) {

                if(this.currentGameState.getPlayerSnake().getHeadAndBody().indexOf(p) == 0){
                    Paint snakeHeadPaint = new Paint();
                    snakeHeadPaint.setColor(getResources().getColor(R.color.purple));
                    canvas.drawRect(new Rect(p.x * squareSize,
                                    p.y * squareSize, (p.x * squareSize) + squareSize - 2,
                                    (p.y * squareSize) + squareSize -2),
                            snakeHeadPaint);

                }else{
                    canvas.drawRect(new Rect(p.x * squareSize,
                                    p.y * squareSize, (p.x * squareSize) + squareSize - 2,
                                    (p.y * squareSize) + squareSize - 2),
                            this.mPaint);
                }
            }

        if(currentGameState.getGameOver()) {

            String gameOver = "GAME OVER";
            String tapScreenToPlayAgain = "tap screen to play again";

            Paint gameOverPaint = new Paint();
            gameOverPaint.setTextSize(100f);
            gameOverPaint.setColor(Color.RED);
            gameOverPaint.setStyle(Paint.Style.FILL);

            Paint tapScreenPaint = new Paint();
            tapScreenPaint.setTextSize(50f);
            tapScreenPaint.setColor(Color.GREEN);
            tapScreenPaint.setStyle(Paint.Style.FILL);

            Rect resultGameOver = new Rect();
            Rect resultTapScreenToPlayAgain = new Rect();


            gameOverPaint.getTextBounds(gameOver, 0, gameOver.length(), resultGameOver);

            tapScreenPaint.getTextBounds(tapScreenToPlayAgain, 0, tapScreenToPlayAgain.length(),
                    resultTapScreenToPlayAgain);


            canvas.drawText(gameOver, (width / 2) - resultGameOver.width() / 2,
                    (height / 2) + resultGameOver.height() / 2, gameOverPaint);

            canvas.drawText(tapScreenToPlayAgain, (width / 2) - resultTapScreenToPlayAgain.width() / 2,
                    ((height / 2) + resultTapScreenToPlayAgain.height() / 2)
                    + resultGameOver.height(), tapScreenPaint);


        }


        super.onDraw(canvas);
    }

    @Override
    public void update(Observable observable, Object o) {
        this.currentGameState = (Game) observable;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread
                invalidate();
            }
        });

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}



//
//
//package com.example.slurp.helloworldapplication.customView;
//
//        import android.content.Context;
//        import android.graphics.Color;
//        import android.graphics.Paint;
//        import android.graphics.Point;
//        import android.support.annotation.NonNull;
//        import android.util.Log;
//        import android.view.View;
//        import android.view.ViewGroup;
//
//        import java.util.ArrayList;
//        import java.util.List;
//        import java.util.concurrent.CopyOnWriteArrayList;
//
//public class RippleWhenTouchesCanvas extends View {
//    private List<SimulatedTapVisual> tapsOnScreenBuffer;
//    private Paint mPaint;
//    private Boolean isRendering;
//
//    public RippleWhenTouchesCanvas(Context context, int width, int height) {
//        super(context);
//
//        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(width,
//                height);
//
//        this.setLayoutParams(layoutParams);
//        this.setBackgroundColor(Color.BLACK);
//        this.mPaint = new Paint();
//        this.mPaint.setColor(Color.CYAN);
//        this.mPaint.setStrokeWidth(16f);
//        this.mPaint.setStyle(Paint.Style.STROKE);
//
//
//        this.tapsOnScreenBuffer = new CopyOnWriteArrayList<>(); // read up on how this works more
//        this.isRendering = false;
//    }
//
//    @Override
//    protected void onDraw(android.graphics.Canvas canvas) {
//        // draw
//        for(SimulatedTapVisual tap : this.tapsOnScreenBuffer){
//            canvas.drawCircle(tap.getPoint().x, tap.getPoint().y, tap.getCurrentRadius(), this.mPaint);
//        }
//        super.onDraw(canvas);
//    }
//
//
//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        this.render();
//
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        this.isRendering = false;
//
//    }
//
//    // make private?
//    public void render(){
//        this.isRendering = true;
//        new Thread(){
//            public void run() {
//                while(isRendering) {
////                    Log.d("rendering", "yup");
//                    try {
//                        checkAndClearBuffer();
//                        postInvalidate();
//                        Thread.sleep(25); // 40fp
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//    }
//
//    public void checkAndClearBuffer(){
//        for(SimulatedTapVisual tap : this.tapsOnScreenBuffer){
//            if(tap.getHasFinishedAnimating())
//                this.tapsOnScreenBuffer.remove(0);
//        }
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//
//    public synchronized void addTap(SimulatedTapVisual tap){
//        this.tapsOnScreenBuffer.add(tap);
//        tap.animate();
//    }
//}

