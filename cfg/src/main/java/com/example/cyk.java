package com.example;

import java.util.*;

public class cyk {
    public boolean result;
    private List<String> variables;
    private List<String> terminals;
    private Map<String, List<List<String>>> rules;
    private String w;
    private String[] words;
    private Set<String>[][] table;

    public cyk(List<String> variables, List<String> terminals, Map<String, List<List<String>>> rules,
            String w) {
        this.variables = variables;
        this.terminals = terminals;
        this.rules = rules;
        this.w = w;
    }

    @SuppressWarnings("unchecked")
    public void apply_cyk(String splitChar, String istate) {
        words = w.split(splitChar);
        int n = words.length;

        this.table = new HashSet[n][n + 1];

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
        result = table[0][n].contains(istate); // Registramos istate como el estado inicial
    }

    // Función para generar el árbol de derivación usando la función gen
    public List<Object> generateParseTree() {
        if (!result) {
            throw new IllegalArgumentException("La palabra no puede ser generada por la gramática.");
        }
        return gen(0, words.length, "S0");
    }

    private List<Object> gen(int i, int j, String A) {
        // Caso base: producción A -> ai
        if (j == 1) {
            String ai = words[i];
            for (List<String> production : rules.get(A)) {
                if (production.size() == 1 && production.get(0).equals(ai)) {
                    return List.of(A, ai); // Devuelve la producción en el formato [A, ai]
                }
            }
        }

        // Caso recursivo: producción A -> BC
        for (int k = 1; k < j; k++) {
            Set<String> BSet = table[i][k];
            Set<String> CSet = table[i + k][j - k];

            for (List<String> production : rules.get(A)) {
                if (production.size() == 2) {
                    String B = production.get(0);
                    String C = production.get(1);
                    if (BSet.contains(B) && CSet.contains(C)) {
                        // Construimos el árbol de derivación recursivamente
                        List<Object> leftTree = gen(i, k, B);
                        List<Object> rightTree = gen(i + k, j - k, C);
                        return List.of(A, leftTree, rightTree);
                    }
                }
            }
        }

        throw new IllegalArgumentException("No se encontró una producción válida.");
    }

    public static void main(String[] args) {
        // EJEMPLO SENCILLO 1
        // List<String> variables = Arrays.asList("S0", "A", "B");
        // List<String> terminals = Arrays.asList("a", "b");
        // Map<String, List<List<String>>> rules = new HashMap<>();
        // rules.put("S0", Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("B",
        // "A")));
        // rules.put("A", Arrays.asList(Arrays.asList("a")));
        // rules.put("B", Arrays.asList(Arrays.asList("b")));
        // String word = "ab";

        // EJEMPLO SENCILLO 2
        List<String> variables = Arrays.asList("S0", "A", "B", "C");
        List<String> terminals = Arrays.asList("a", "b");

        Map<String, List<List<String>>> rules = new HashMap<>();
        rules.put("S0", Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("B", "C")));
        rules.put("A", Arrays.asList(Arrays.asList("B", "A"), Arrays.asList("a")));
        rules.put("B", Arrays.asList(Arrays.asList("C", "C"), Arrays.asList("b")));
        rules.put("C", Arrays.asList(Arrays.asList("A", "B"), Arrays.asList("a")));

        String word = "baaba";
        String istate = "S0";

        cyk parser = new cyk(variables, terminals, rules, word);

        parser.apply_cyk("", istate);
        if (parser.result) {
            List<Object> parseTree = parser.generateParseTree();
            System.out.println("Árbol de derivación: " + parseTree);
        } else {
            System.out.println("No se puede generar la palabra.");
        }
    }
}
