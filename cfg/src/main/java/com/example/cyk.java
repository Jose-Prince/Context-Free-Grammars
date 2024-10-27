package com.example;

import java.util.*;

public class cyk {
    public boolean result;
    private List<String> variables;
    private List<String> terminals;
    private Map<String, List<List<String>>> rules;
    private String w;

    public cyk(List<String> variables, List<String> terminals, Map<String, List<List<String>>> rules,
            String w) {
        this.variables = variables;
        this.terminals = terminals;
        this.rules = rules;
        this.w = w;
    }

    public void apply_cyk(String splitChar) {
        String[] words = w.split(splitChar);
        int n = words.length;

        @SuppressWarnings("unchecked")
        Set<String>[][] table = new HashSet[n][n + 1];

        // Inicializar: Para i, 1 ≤ i ≤ n
        for (int i = 0; i < n; i++) {
            table[i][1] = new HashSet<>();
            String ai = words[i];

            // Agregar variables que producen el terminal ai
            for (String variable : variables) {
                if (rules.containsKey(variable)) {
                    for (List<String> production : rules.get(variable)) {
                        if (production.size() == 1 && production.get(0).equals(ai)) {
                            table[i][1].add(variable);
                        }
                    }
                }
            }
        }

        // Para j = 2 hasta n
        for (int j = 2; j <= n; j++) {
            for (int i = 0; i <= n - j; i++) {
                table[i][j] = new HashSet<>();

                // Para cada k, 1 ≤ k < j
                for (int k = 1; k < j; k++) {
                    Set<String> B = table[i][k];
                    Set<String> C = table[i + k][j - k];

                    // Verificar si existe alguna producción A -> BC
                    for (String variable : variables) {
                        if (rules.containsKey(variable)) {
                            for (List<String> production : rules.get(variable)) {
                                if (production.size() == 2 && B.contains(production.get(0))
                                        && C.contains(production.get(1))) {
                                    table[i][j].add(variable);
                                }
                            }
                        }
                    }
                }
            }
        }

        // La palabra es aceptada si el símbolo inicial está en t[0][n]
        result = table[0][n].contains("S"); // Consideramos que "S" es el símbolo inicial
    }

    public static void main(String[] args) {
        List<String> variables = Arrays.asList("S", "A", "B"); // Ejemplo de variables
        List<String> terminals = Arrays.asList("a", "b"); // Ejemplo de terminales
        Map<String, List<List<String>>> rules = new HashMap<>(); // Ejemplo de reglas
        rules.put("S", Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("B", "A")));
        rules.put("A", Arrays.asList(Arrays.asList("a")));
        rules.put("B", Arrays.asList(Arrays.asList("b")));

        String word = "ab";
        cyk parser = new cyk(variables, terminals, rules, word);

        parser.apply_cyk("");
        System.out.println("¿La palabra es generada por la gramática? " + (parser.result ? "si" : "no"));
    }
}
