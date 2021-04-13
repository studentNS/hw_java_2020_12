package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class TestClass {

    @Before
    void beforeMethod() throws Exception {
        System.out.println("Before");
        throw new Exception("Error");
    }

    @Test
    void testMethod(){
        System.out.println("Test");
    }

    @After
    void afterMethod(){
        System.out.println("After");
    }

}
