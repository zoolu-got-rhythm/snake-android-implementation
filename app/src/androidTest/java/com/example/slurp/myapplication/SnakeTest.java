package com.example.slurp.myapplication;

import android.graphics.Point;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SnakeTest {
    private Snake snake;

    @Before
    public void setUp() throws Exception {
        this.snake = new Snake(4, 5, 5);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void deepCopyIsSameValuesAtCopy(){
        this.snake.move("n");
        Snake snake2 = (Snake) this.snake.clone();
        assertEquals(snake.getHeadAndBody(), snake2.getHeadAndBody());
        Point snake2Head = snake2.getHeadAndBody().get(0);
        Point originalSnakeHead = snake.getHeadAndBody().get(0);
        assertEquals(snake2Head.x + snake2Head.y, originalSnakeHead.x + originalSnakeHead.y);

    }

    @Test
    public void deepCopyObjRefIsDifferent(){
        Snake snake2 = (Snake) this.snake.clone();
        assertNotEquals(snake, snake2);
        assertTrue(snake != snake2);
    }

    @Test
    public void testCloneMutationEffects(){
        Snake snake2 = (Snake) this.snake.clone();
        snake2.move("n");
        snake2.move("n");
        snake2.move("n");
        Point snake2Head = snake2.getHeadAndBody().get(0);
        Point originalSnakeHead = snake.getHeadAndBody().get(0);
        System.out.println(snake2Head.y);
        Log.d("test val", Integer.toString(snake2Head.y));
        Log.d("x and y", Integer.toString(snake2Head.x + snake2Head.y));
        assertNotEquals(snake2Head.x + snake2Head.y, originalSnakeHead.x + originalSnakeHead.y);
    }
}