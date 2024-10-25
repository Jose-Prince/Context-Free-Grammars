package com.example;

import com.example.CFG;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Test for verifying the right number of states from cfg to cnf
     */
    public void testCnfRulesSize()
    {
	CFG cfg = createCFG();
	CFG cnf = cfg.convertCNF(cfg);	

	assertEquals(cnf.getRules().size(), 8);
    }

    /**
     * Test for verifying new first state in cnf
    */
    public void testCnfInitialState()
    {
	CFG cfg = createCFG();
	CFG cnf = cfg.convertCNF(cfg);	

	assertEquals(cnf.getVariables().contains("S0"), true);
    }

    /**
     * Test for checking rules in cnf
     */
    public void testCnfRules() {
	CFG cfg = createCFG();
	CFG cnf = cfg.convertCNF(cfg);	

	boolean check = true;

	Map<String, List<List<String>>> rules = new HashMap<>(cnf.getRules());
	List<String> variables = new ArrayList<>(cnf.getVariables());
	List<String> terminals = new ArrayList<>(cnf.getTerminals());

	for (Map.Entry<String, List<List<String>>> entry : rules.entrySet()) {
		String key = entry.getKey();
		List<List<String>> productions = entry.getValue();

		for (List<String> production : productions) {
			if (production.size() == 1 && (!terminals.contains(production.get(0)) || (!key.equals("S0") && production.get(0).equals("e")))) {
				check = false;
			} else if (production.size() > 2) {
				check = false;
			} else if (production.size() == 2) {
				for (String var : production) {
					if (!variables.contains(var) || (!key.equals("S0") && var.equals("e"))) {
						check = false;
					}
				}
			}
		}
	}

	assertEquals(check, true);

    }

    public CFG createCFG() {
	//CFG inicialization
	List<String> variables = List.of("S","VP","PP","NP","V","P","N","Det");
	List<String> terminals = List.of("cooks","drinks","eats","cuts","he","she","in","with","cat","dog","beer","cake","juice","meat","soup","fork","knife","oven","spoon","a","the");
	List<List<String>> rule1 = new ArrayList<>();
	rule1.add(List.of("NP","VP"));

	List<List<String>> rule2 = new ArrayList<>();
	rule2.add(List.of("VP","PP"));
	rule2.add(List.of("V","NP"));
	rule2.add(List.of("cooks"));
	rule2.add(List.of("drinks"));
	rule2.add(List.of("eats"));
	rule2.add(List.of("cuts"));

	List<List<String>> rule3 = new ArrayList<>();
	rule3.add(List.of("P","NP"));

	List<List<String>> rule4 = new ArrayList<>();
	rule4.add(List.of("Det","N"));
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
	rules.put("S",rule1);
	rules.put("VP",rule2);
	rules.put("PP",rule3);
	rules.put("NP",rule4);
	rules.put("V",rule5);
	rules.put("P",rule6);
	rules.put("N",rule7);
	rules.put("Det",rule8);

	return new CFG(variables, terminals, rules);	
    }

}
