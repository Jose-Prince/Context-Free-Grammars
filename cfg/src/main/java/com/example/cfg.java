import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

class CFG {
   
   public List<String> variables;
   public List<String> terminals;
   public Map<String, List<List<String>>> rules;

   public CFG(List<String> variables, List<String> terminals, HashMap<String, List<List<String>>> rules) {
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

   public HashMap<String, List<<List<String>>> getRules() {
   	return this.rules;
   }

   public void setRules(HashMap<String, List<List<String>>> rules) {
	this.rules = rules;	
   }

   public void convertCNF() {
	List<String> newVariables = new ArrayList<>(this.variables);
	List<String> newTerminals = new ArrayList<>(this.terminals);
	Map<String, List<List<String>>> newRules = new HashMap<>(this.rules);

	//First step: S0 -> S
	newVariables.add("S0");
	newRules.put("S0", new ArrayList<>(List.of(List.of("S"))));

	//Second step: Lead to 2 variables or 1 terminal
	
	int bin = 1;
	int counter = 0;
	while (bin > 0) {
		bin = 0;
		Map<String, List<List<String>>> tempRules = new HashMap<>(newRules);

		for (Map.Entry<String, List<List<String>>> entry : newRules.entrySet()) {
			String variable = entry.getKey();
			List<List<String>> productions = entry.getKey();

			for (List<String> production : productions) {
				int index1 = productions.indexOf(production);

				if (production.size() >= 2) {
					for (String var : production) {
						if (newTerminals.contains(var)) {
							String key = searchKey(newRules, var);
							int index2 = y.indexOf(var);
							tempRules.getValue(variable).get(index1).set(index2, key);
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
						tempRules.add(newKey, newRule);
					}

					counter += 1;
					bin += 1;
				}
			}
		}

		newRules = tempRules;
	}

	//Third step: Delete epsilon transitions
	boolean epsilon = true;

	//find epsilon transitions
	String keyEpsilon = searchKey(newRules, "e");
	Map<String, List<List<String>>> tempMap = new HashMap<>(newRules);

	for (Map.Entry<String, List<List<String>>> entry : map.entrySet()){
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
			if (innerList.size() == 1 && newVariables.contains(innerList.get(0)) {
				units.add(innerList.get(0));
			}
		}

	}

	//Changes unitarina transitions
	Map<String, List<List<String>> tempMap = new HashMap<>(newRules);
	for (Map.Entry<String, List<List<String>>> entry : newRules.entrySet()) {
	
		String key = entry.getKey();
		
		List<List<String>> listOfLists = new ArrayList<>(entry.getValue());

		Iterator<List<String>> iterator = listOfLists.iterator();
		while (iterator.hasNext()) {
			List<String> innerList = iterator.next();
			if (innerList.size() == 1 && units.contains(innerList.get(0))) {
				iterator.remove();

				String targetVariable = innerList.get(0);

				List<List<String>> targetRules = newRules(targetVariable);
				if (targetRules != null) {
					for (List<String> rule : targetRules) {
						listOfLists.add(new ArrayList<>(rule));
					}
				}
			}
		}

		tempMap.put(key, listOfLists);
	}
		
	
	newRules = tempMap;

	//Fith step: Delete useless variables 
	List<String> variablesUse = new ArrayList<>();

	findReachableVariables("S0", newRules, variablesUse);

	Map<String, List<List<String>>> tempMap = new HashMap<>(newRules);
	for (String key : newRules.keySet()) {
		if (!variablesUse.contains(key) && !key.equals("S0")) {
			tempMap.remove(key);
		}
	}

	newRules = tempMap;

	//Sets attributes for CFG
	this.setVariables(newVariables);
	this.setTerminals(newTerminals);
	this.setRules(newRules);
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

	return null;
   }

	private void findReachableVariables(String variable, Map<String, List<List<String>>> rules, List<String> reachable) {
	if (!reachable.contains(variable)) {
		reachable.add(variable);
		List<List<String>> productions = rules.get(variable);
		if (production != null) {
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
