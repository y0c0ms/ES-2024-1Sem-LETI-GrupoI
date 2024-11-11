package com.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        LeitorCSV leitorCSV = new LeitorCSV();
        List<Propriedade> propriedades = leitorCSV.lerCSV("D:\\ProjEngSoft\\src\\main\\resources\\Madeira-Moodle.csv");

        for (Propriedade propriedade : propriedades) {
            System.out.println(propriedade);
        }
    }
}