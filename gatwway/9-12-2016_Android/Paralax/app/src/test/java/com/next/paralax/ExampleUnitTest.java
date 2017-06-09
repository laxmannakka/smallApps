package com.next.paralax;

import android.widget.ImageView;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest
{
    MainActivity mainActivity = new MainActivity();
    @Test
    public void addition_isCorrect() throws Exception
    {
        assertEquals(4, 2 + 2);

    }
    @Test
    public  void sub() throws Exception
    {
        assertEquals(2,4-2);
    }


}