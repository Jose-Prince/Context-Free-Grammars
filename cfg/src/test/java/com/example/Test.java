package com.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {
    public static void testString(String word, List<String> variables, List<String> terminals, 
                                Map<String, List<List<String>>> rules) {
        System.out.println("\nProbando la cadena: \"" + word + "\"");
        System.out.println("----------------------------------------");
        
        cyk parser = new cyk(variables, terminals, rules, word);
        parser.apply_cyk(" ", "S0");
        
        if (parser.result) {
            System.out.println("✓ Sí pertenece al lenguaje");
            System.out.println("Tiempo de validación: " + parser.execution + "ns.");
            List<Object> parseTree = parser.generateParseTree();
            System.out.println("Árbol de derivación: " + parseTree);
        } else {
            System.out.println("✗ No pertenece al lenguaje");
            System.out.println("Tiempo de validación: " + parser.execution + "ns.");
        }
    }

    public static void main(String[] args) {
        // CFG inicialization
        List<String> variables = List.of("S", "VP", "PP", "NP", "V", "P", "N", "Det");
        List<String> terminals = List.of("cooks", "drinks", "eats", "cuts", "he", "she", "in", "with", "cat", "dog",
                "beer", "cake", "juice", "meat", "soup", "fork", "knife", "oven", "spoon", "a", "the");
        
        // Definición de reglas
        List<List<String>> rule1 = new ArrayList<>();
        rule1.add(List.of("NP", "VP"));

        List<List<String>> rule2 = new ArrayList<>();
        rule2.add(List.of("VP", "PP"));
        rule2.add(List.of("V", "NP"));
        rule2.add(List.of("cooks"));
        rule2.add(List.of("drinks"));
        rule2.add(List.of("eats"));
        rule2.add(List.of("cuts"));

        List<List<String>> rule3 = new ArrayList<>();
        rule3.add(List.of("P", "NP"));

        List<List<String>> rule4 = new ArrayList<>();
        rule4.add(List.of("Det", "N"));
        rule4.add(List.of("he"));
        rule4.add(List.of("she"));

        List<List<String>> rule5 = new ArrayList<>();
        rule5.add(List.of("cooks"));
        rule5.add(List.of("drinks"));
        rule5.add(List.of("eats"));
        rule5.add(List.of("cuts"));

        List<List<String>> rule6 = new ArrayList<>();
        rule6.add(List.of("in"));
        rule6.add(List.of("with"));

        List<List<String>> rule7 = new ArrayList<>();
        rule7.add(List.of("cat"));
        rule7.add(List.of("dog"));
        rule7.add(List.of("beer"));
        rule7.add(List.of("cake"));
        rule7.add(List.of("juice"));
        rule7.add(List.of("meat"));
        rule7.add(List.of("soup"));
        rule7.add(List.of("spoon"));
        rule7.add(List.of("fork"));
        rule7.add(List.of("knife"));
        rule7.add(List.of("oven"));

        List<List<String>> rule8 = new ArrayList<>();
        rule8.add(List.of("a"));
        rule8.add(List.of("the"));

        HashMap<String, List<List<String>>> rules = new HashMap<>();
        rules.put("S", rule1);
        rules.put("VP", rule2);
        rules.put("PP", rule3);
        rules.put("NP", rule4);
        rules.put("V", rule5);
        rules.put("P", rule6);
        rules.put("N", rule7);
        rules.put("Det", rule8);

        CFG cfg = new CFG(variables, terminals, rules);
        CFG cnf = cfg.convertCNF(cfg);

        Map<String, List<List<String>>> rulese = new HashMap<>(cnf.getRules());
        List<String> variablese = new ArrayList<>(cnf.getVariables());
        List<String> terminalse = new ArrayList<>(cnf.getTerminals());

        System.out.println("===========================================");
        System.out.println("EJEMPLOS DE CADENAS ACEPTADAS SEMÁNTICAMENTE CORRECTAS");
        System.out.println("===========================================");
        testString("he cuts the cake", variablese, terminalse, rulese);
        testString("a cat drinks the beer", variablese, terminalse, rulese);

        System.out.println("\n===========================================");
        System.out.println("EJEMPLOS DE CADENAS ACEPTADAS SEMÁNTICAMENTE INCORRECTAS");
        System.out.println("===========================================");
        testString("the knife cuts the knife", variablese, terminalse, rulese);
        testString("the oven drinks a fork", variablese, terminalse, rulese);

        System.out.println("\n===========================================");
        System.out.println("EJEMPLOS DE CADENAS NO ACEPTADAS POR LA GRAMÁTICA");
        System.out.println("===========================================");
        testString("she cooks in the kitchen", variablese, terminalse, rulese);
        testString("a dog eats meat with juice", variablese, terminalse, rulese);
    }
}