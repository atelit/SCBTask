package org.example.steps;

import io.cucumber.java.Before;

public class Hooks {
    @Before(value = "@tag1")
    public void firstHook(){

    }
}
