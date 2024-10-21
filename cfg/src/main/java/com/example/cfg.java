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

   public List<String> getTerminals() {
	return this.terminals;
   }

   public HashMap<String, List<String>> getRules() {
   	return this.rules;
   }

}
