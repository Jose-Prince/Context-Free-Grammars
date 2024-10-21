class CFG {
   
   public List<String> variables;
   public List<String> terminals;
   public HashMap<String, List<String>> rules;

   public CFG(List<String> variables, List<String> terminals, HashMap<String, List<String>> rules) {
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

   public HashMap<String, List<String>> getRules() {
   	return this.rules;
   }

   public void setRules(HashMap<String, List<String>>) {
	this.rules = rules;	
   }

   public void convertCNF() {
   	
   }

}
