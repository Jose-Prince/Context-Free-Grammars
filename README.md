# Algoritmo CYK para Gramáticas en Forma Normal de Chomsky

Este proyecto implementa un analizador sintáctico utilizando el algoritmo CYK (Cocke-Younger-Kasami) para determinar si frases simples en inglés pertenecen a un lenguaje generado por una gramática específica. El proyecto incluye la conversión de Gramática Libre de Contexto (CFG) a Forma Normal de Chomsky (CNF) y visualización de árboles de análisis sintáctico.

## Descripción del Proyecto

Este proyecto es parte del curso de Teoría de la Computación 2024 y se centra en la implementación de:
1. Conversión de CFG a CNF
2. Algoritmo CYK para parsing
3. Generación de árboles de análisis sintáctico (parse trees)

## Estructura de la Gramática

La gramática implementada soporta frases simples en inglés con la siguiente estructura:

```
S → NP VP
VP → VP PP | V NP | cooks | drinks | eats | cuts
PP → P NP
NP → Det N | he | she
V → cooks | drinks | eats | cuts
P → in | with
N → cat | dog | beer | cake | juice | meat | soup | fork | knife | oven | spoon
Det → a | the
```

## Características Principales

- Conversión automática de CFG a CNF
- Implementación del algoritmo CYK usando programación dinámica
- Generación y visualización de árboles de análisis sintáctico
- Medición de tiempo de ejecución
- Interfaz de línea de comandos para pruebas

## Estructura del Proyecto

- `App.java`: Programa principal y manejo de entrada/salida
- `cfg.java`: Implementación de la conversión CFG a CNF
- `cyk.java`: Implementación del algoritmo CYK
- `parse_tree.java`: Generación de árboles de análisis
- `Test.java`: Suite de pruebas con ejemplos
- `AppTest.java`: Pruebas unitarias

## Requisitos

- Java JDK 8 o superior
- GraphViz (para visualización de árboles)
- JUnit (para pruebas)

## Instalación

1. Clonar el repositorio:
```bash
git clone https://github.com/Jose-Prince/Context-Free-Grammars
cd Context-Free-Grammars
```

## Ejemplos de Uso

1. Frases semánticamente correctas aceptadas:
```
he cuts the cake
a cat drinks the beer
```

2. Frases semánticamente incorrectas pero aceptadas:
```
the knife cuts the knife
the oven drinks a fork
```

3. Frases no aceptadas por la gramática:
```
she cooks in the kitchen
a dog eats meat with juice
```

## Resultados

Para cada frase analizada, el programa proporciona:
- Validación de pertenencia al lenguaje
- Tiempo de ejecución del análisis
- Árbol de análisis sintáctico (si la frase es válida)
- Visualización gráfica del árbol

## Referencias

- [CFG to CNF Converter](https://devimam.github.io/cfgtocnf/)
- [CYK Algorithm - Wikipedia](https://en.wikipedia.org/wiki/CYK_algorithm)
- [Phrase Structure Rules](https://en.wikipedia.org/wiki/Phrase_structure_rules)
- [CYK Algorithm Implementation](https://www.geeksforgeeks.org/cyk-algorithm-for-context-free-grammar/)
- [UC Davis CYK Algorithm](https://web.cs.ucdavis.edu/~rogaway/classes/120/winter12/CYK.pdf)

## Autores
- José Prince, 22087
- Fabiola Contreras, 22787
- Maria Jose Villafuerte, 22129

