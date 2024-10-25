package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

class CFG {
   
   public List<String> variables;
   public List<String> terminals;
   public Map<String, List<List<String>>> rules;

   public CFG(List<String> variables, List<String> terminals, Map<String, List<List<String>>> rules) {
	this.variables = variables;
	this.terminals = terminals;
	this.rules = rules;
   }

   public List<String> getVariables() {
   	return this.variables;
   }

   public void setVariables(List<String> variables) {
   	this.variables = variables;
   }

   public List<String> getTerminals() {
	return this.terminals;
   }

   public void setTerminals(List<String> terminals) {
	this.terminals = terminals;
   }

   public Map<String, List<List<String>>> getRules() {
   	return this.rules;
   }

   public void setRules(Map<String, List<List<String>>> rules) {
	this.rules = rules;	
   }

   public CFG convertCNF(CFG cfg) {
	List<String> newVariables = new ArrayList<>(cfg.getVariables());
	List<String> newTerminals = new ArrayList<>(cfg.getTerminals());
	Map<String, List<List<String>>> newRules = new HashMap<>(cfg.getRules());

	//First step: S0 -> S
	newVariables.add("S0");
	newRules.put("S0", new ArrayList<>(List.of(List.of("S"))));

	//Second step: Lead to 2 variables or 1 terminal
	int bin = 1;
	int counter = 0;

	while (bin > 0) {
		bin = 0;

		// Copia temporal para evitar modificar la colección mientras la iteramos
		Map<String, List<List<String>>> tempRules = new HashMap<>(newRules);

		for (Map.Entry<String, List<List<String>>> entry : new HashMap<>(newRules).entrySet()) {
			String variable = entry.getKey();
			List<List<String>> productions = entry.getValue();

			for (List<String> production : new ArrayList<>(productions)) {
				int index1 = productions.indexOf(production);

				if (production.size() >= 2) {
					for (String var : new ArrayList<>(production)) {
						if (newTerminals.contains(var)) {
							String key = searchKey(newRules, var);
							int index2 = production.indexOf(var);
							tempRules.get(variable).get(index1).set(index2, key);
						}
					}

					if (production.size() > 2) {
						String newKey = "X" + counter;
						String firstValue = production.get(0);
						String secondValue = production.get(1);

						List<List<String>> newRule = List.of(List.of(firstValue, secondValue));

						production.remove(0);
						production.set(0, newKey);

						newVariables.add(newKey);
						tempRules.put(newKey, newRule);

						counter += 1;
						bin += 1;
					}
				}
			}
		}

		newRules = tempRules; // Asignar las reglas modificadas al final
	}


	//Third step: Delete epsilon transitions
	boolean epsilon = true;

	//find epsilon transitions
	String keyEpsilon = searchKey(newRules, "e");
	Map<String, List<List<String>>> tempMap = new HashMap<>(newRules);

	for (Map.Entry<String, List<List<String>>> entry : newRules.entrySet()){
		String key = entry.getKey();
		List<List<String>> listOfLists = entry.getValue();

		List<List<String>> toAdd = new ArrayList<>();
		List<List<String>> toRemove = new ArrayList<>();

		for (List<String> innerList : listOfLists) {
			if (innerList.contains(keyEpsilon)) {
				List<String> newTransition = new ArrayList<>(innerList);
				newTransition.remove("e");
				
				if (!newTransition.isEmpty()) {
					toAdd.add(newTransition);
				}

			} else if (innerList.size() == 1 && innerList.get(0).equals("e")) {
				if (!key.equals("S0")) {
					toRemove.add(innerList);
				}
			}
		}
	}
	newRules = tempMap;
	
	//Fourth step: Delete unitarians transitions
	List<String> units = new ArrayList<>();

	//Identify unitarians transitions
	for (Map.Entry<String, List<List<String>>> entry : newRules.entrySet()) {
		String key = entry.getKey();

		List<List<String>> listOfLists = entry.getValue();

		for (List<String> innerList: listOfLists) {
			if (innerList.size() == 1 && newVariables.contains(innerList.get(0))) {
				units.add(innerList.get(0));
			}
		}

	}

	//Changes unitarina transitions
	tempMap = new HashMap<>(newRules); // Crea un nuevo map para evitar modificaciones concurrentes.
	for (Map.Entry<String, List<List<String>>> entry : newRules.entrySet()) {
		
		String key = entry.getKey();
		
		List<List<String>> listOfLists = new ArrayList<>(entry.getValue());

		List<List<String>> toAdd = new ArrayList<>(); // Lista temporal para agregar reglas.
		List<List<String>> toRemove = new ArrayList<>(); // Lista temporal para eliminar reglas.

		for (List<String> innerList : listOfLists) {
			if (innerList.size() == 1 && units.contains(innerList.get(0))) {
				toRemove.add(innerList); // Agregar a la lista de eliminaciones.

				String targetVariable = innerList.get(0);
				List<List<String>> targetRules = newRules.get(targetVariable);
				if (targetRules != null) {
					for (List<String> rule : targetRules) {
						toAdd.add(new ArrayList<>(rule)); // Agregar las nuevas reglas.
					}
				}
			}
		}

		// Aplica los cambios fuera del bucle de iteración.
		listOfLists.removeAll(toRemove);
		listOfLists.addAll(toAdd);

		tempMap.put(key, listOfLists); // Actualiza el map temporal.
	}

	newRules = tempMap;


	// Fifth step: Delete useless variables 
	List<String> variablesUse = new ArrayList<>();

	findReachableVariables("S0", newRules, variablesUse);

	tempMap = new HashMap<>(newRules); // Copiamos el map para evitar problemas con modificaciones concurrentes.

	List<String> keysToRemove = new ArrayList<>(); // Lista para almacenar las claves que queremos eliminar.

	for (String key : newRules.keySet()) {
		if (!variablesUse.contains(key) && !key.equals("S0")) {
			keysToRemove.add(key); // Agregar claves a eliminar.
		}
	}

	// Realizamos la eliminación fuera del bucle.
	for (String key : keysToRemove) {
		tempMap.remove(key);
	}

	newRules = tempMap;


	//Sets attributes for CFG
	return new CFG(newVariables, newTerminals, newRules);
   }

   private String searchKey(Map<String, List<List<String>>> map, String value) {
	for (Map.Entry<String, List<List<String>>> entry : map.entrySet()) {
		String key = entry.getKey();
		List<List<String>> listOfLists = entry.getValue();

		for (List<String> innerList : listOfLists) {
			if (innerList.size() == 1 && innerList.contains(value)) {
				return key;
			}
		}
	}

	return "-";
   }

	private void findReachableVariables(String variable, Map<String, List<List<String>>> rules, List<String> reachable) {
	if (!reachable.contains(variable)) {
		reachable.add(variable);
		List<List<String>> productions = rules.get(variable);
		if (productions != null) {
			for (List<String> production : productions) {
				for (String symbol : production) {
					if (rules.containsKey(symbol)) {
						findReachableVariables(symbol, rules, reachable);
					}
				}
			}	
		}
	}
   }

}
