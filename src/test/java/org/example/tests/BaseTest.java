package org.example.tests;

import org.example.data.AppConfig;
import org.junit.Assert;
import org.junit.Test;


public class BaseTest {

    @Test
    public void testHello(){
        Assert.assertEquals(2, 1+1);
    }

    @Test
    public void testProperties(){
        Assert.assertEquals("user4", AppConfig.getProperty("user"));
    }

    @Test
    public void testGetBooks(){

    }
}
