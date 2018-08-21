package com.example.slurp.myapplication;

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
    public void deepCopyIsSame(){
        Snake snake2 = (Snake) this.snake.clone();
        assertEquals(snake.getHeadAndBody(), snake2.getHeadAndBody());
    }

    @Test
    public void deepCopyObjRefIsDifferent(){
        Snake snake2 = (Snake) this.snake.clone();
        assertNotEquals(snake, snake2);
    }
}