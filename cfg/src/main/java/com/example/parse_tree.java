package com.example;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;

import static guru.nidi.graphviz.model.Factory.*;


public class parse_tree {
    private final MutableGraph graph;
    private final AtomicInteger nodeCounter;

    // Estos son los objetos que se iran modificando para luego crear el árbol 
    public parse_tree(){
        this.graph = mutGraph("arbol_resultado").setDirected(true);
        this.nodeCounter = new AtomicInteger(0);
    }


    // Usa como parámetro lo que nos da como resultado el algoritmo cyk
    public void obtener_infomacion(List<Object> parseTree){
        try {
            // Crea la estructura del árbol
            createNodes(parseTree, null);

            // Configura y guarda el grafo  
            // De todos los nodos que se van agregando se guardan en graph para que aca con la dependecia se haga el archivo
            Graphviz.fromGraph(graph)
            .width(800)
            .render(Format.PNG)
            // Guarda en archivo
            .toFile(new File("Arbol_Resultado.png"));
            
            System.out.println("Tu imagen del árbol de a generado como 'Arbol_Resultado.png'");


        } catch (Exception e) {
            System.err.println("Error generating syntax tree: " + e.getMessage());
        }
    
    }

    private MutableNode createNodes(Object node, MutableNode parent) {
        // Obtenermos la lista de listas de los nodos  
        // Si el nodo es una lista
        if (node instanceof List<?>) {
            List<?> list = (List<?>) node;
            if (!list.isEmpty()) {
                // Obtiene el símbolo (primer elemento de la lista)
                String symbol = list.get(0).toString();

                // Genera ID único para el nodo
                String nodeId = "node" + nodeCounter.getAndIncrement();

                // Crea el nodo con el símbolo
                MutableNode currentNode = createSymbolNode(symbol, nodeId);

                // Si tiene padre, conecta con él
                if (parent != null) {
                    parent.addLink(currentNode);
                }
                
                // Añade el nodo al grafo
                graph.add(currentNode);

                // Procesa recursivamente los hijos
                for (int i = 1; i < list.size(); i++) {
                    createNodes(list.get(i), currentNode);
                }

                return currentNode;
            }
        } else if (node != null) {  // Quiere decir que es un nodo terminal, para mover a la izquierda 
            // Genera ID único
            String nodeId = "node" + nodeCounter.getAndIncrement();
            // Crea nodo terminal
            MutableNode leafNode = createTerminalNode(node.toString(), nodeId);
            
            // Si tiene padre, conecta con él
            if (parent != null) {
                parent.addLink(leafNode);
            }

            // Añade al grafo
            graph.add(leafNode);
            return leafNode;
        }
        return null;
    }

    /*DECORACIONES DE LA IMAGEN*/
    private MutableNode createSymbolNode(String symbol, String id) {
            return mutNode(id)
                    .add(Color.rgb("9999ff").fill()) // Light blue fill
                    .add(Style.FILLED)
                    .add("label", symbol);
    }

    private MutableNode createTerminalNode(String symbol, String id) {
        return mutNode(id)
                .add(Color.rgb("ff99ff").fill()) // Light pink fill
                .add(Style.FILLED)
                .add("label", symbol);
    }
                     
}
