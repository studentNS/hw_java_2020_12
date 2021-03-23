package ru.otus;

import com.google.common.math.IntMath;

import java.util.ArrayList;
import java.util.List;

public class HelloOtus {

    public static void main(String args[]){

        List<Integer> example = new ArrayList<>();
        int min = 1;
        int max = 60;
        for (int i = min; i < max; i++) {
            example.add(i);
        }

        for(Integer number : example){
            if(IntMath.isPrime(number))
                System.out.println(number + ": простое число");
            else
                System.out.println(number + ": не простое число");
        }

    }
}
