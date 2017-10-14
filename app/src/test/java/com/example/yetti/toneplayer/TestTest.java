package com.example.yetti.toneplayer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(constants = BuildConfig.class)
@RunWith(RobolectricTestRunner.class)
public class TestTest {
    @Test
    public void test(){
        System.out.println("TEST");
    }
}
