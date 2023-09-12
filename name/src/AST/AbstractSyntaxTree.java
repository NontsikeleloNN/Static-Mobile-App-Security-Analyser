package AST;

import java.util.List;
/***
 * Assisted by: https://lambda.uta.edu/cse5317/notes/node23.html
 * @author ndumo
 *
 */
abstract class AbstractSyntaxTree {
}
class IntExpression extends AbstractSyntaxTree {
   public int num;
   public IntExpression ( int n ) 
   {
	   num = n; 
	   
	   }
}


class StrExpression extends AbstractSyntaxTree {
   public String string;
   public StrExpression ( String s )
   { 
	   string = s; 
	   }
}

class Variable extends AbstractSyntaxTree {
   public String varName;
   public Variable ( String n ) { 
	   varName = n;
	   }
}


class Binary extends AbstractSyntaxTree {
   public String operator;
   public AbstractSyntaxTree left;
   public AbstractSyntaxTree right;
   public Binary ( String o, AbstractSyntaxTree l, AbstractSyntaxTree r ) { 
	   operator = o; 
	   left = l; 
	   right = r; 
	   
   }
}



class Unary extends AbstractSyntaxTree {
   public String operator;
   public AbstractSyntaxTree operand;
   public Unary ( String o, AbstractSyntaxTree e ) {
	   operator = o; 
	   operand = e; 
	   
   }
}
