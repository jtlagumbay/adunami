#include <iostream>
#include <algorithm>
#include <queue>
#include <stack>

/**
 * SIMPLE CALCULATOR
 * 
 * This calculator uses the Shunting Yard Algorithm to find the answer to the expression.
 * The general structure of this algorithm is that it changes the common infix expression
 * to a postfix expression. After that, the algorithm uses a queue and a stack to calculate the
 * answer.
 *  
*/

using namespace std;

queue<string> q;

/**
 * CALCULATOR: checks for the operator with the least precedence
 * 
 * PARAMETER: an expression 'x' in string datatype
 * RETURN:  either: 1. the index of the operator
 *                  2. -1 -> signifying that there are no operators left in the expression
 *  
*/
int leastPrec(string x) { 
    /**
     * PRECEDENCE RULE: 
     *   --> '*' | '/',  '+' | '-'
     *   --> if operators have the same precedence, the rightmost operator is the least precedent
    */

    // traverse through string x starting from the rightmost
    for(int i = x.length() - 1; i >= 0; i--)
        if(!isdigit(x[i])) // if the character is not a digit, then assume it's an operator
            if(x[i] == '+' || x[i] == '-') // if the operator is either '+' or '-', return index.
                return i;
    
    // if the code continues on here, there were no '+' or '-' operators found.
    // hence, assume operators left are either '*' or '/'

    // second traversal of string x from the rightmost
    for(int i = x.length() - 1; i >= 0; i--)
        if(!isdigit(x[i])) // if the character is not a digit, then it is an operator. return the index.
            return i;
    
    // if the code continues on here, there were no operators left in the string.
    // hence return -1 to signify 'NO OPERATOR'
    return -1;
}

/**
 * CALCULATOR: changes the infix expression to postfix expression
 * 
 * PARAMETER: an infix expression 'x' in string datatype
 * RETURN: none
 *  
*/
void postfix(string x) {
    // get the index of the least precedent operator
    int pos = leastPrec(x);
    
    string prnt;

    // extract the operator from the expression 'x'
    if(pos >= 0) // if there is an operator, extract it using substr function.
        prnt = x.substr(pos, 1);
    else { // else, print the integer
        prnt = x; 
    }

    // recursively find the next least precedent operator from the LEFT of the current operator
    if(pos >= 0) 
        postfix(x.substr(0, pos));
    
    // recursively find the next least precedent operator from the RIGHT of the current operator
    if(pos < x.length()) 
        postfix(x.substr(pos + 1));

    // push the current operator to the queue
    q.push(prnt);
}

/**
 * CALCULATOR: helper function that checks whether the given string is a digit or not
 * 
 * PARAMETER: expression 'x' in string datatype
 * RETURN: boolean expression
*/
bool isDigit(string x) {
    // traverses through the expression 'x' until it finds a non-digit character
    for (char c : x) { // gets the character from x
        // if the character is a non-digit character, return false.
        if (!isdigit(c)) {
            return false;
        }
    }

    // if the code continues on here, then assume the string only contains digit characters, return true.
    return true;
}

/**
 * CALCULATOR: returns a string result of solving x op y
 * 
 * PARAMETERS: string x and string y are the operands and string op is the operator
 * RETURN: result of x op y in string datatype
 * 
*/
string solveArithmetic(string x, string y, string op) {
    // convert x and y to integer to be able to do arithmetic operations
    int a = stol(x);
    int b = stol(y);
    
    // initialize the answer
    int answer;

    // check the given operator and execute the corresponding operation
    if(op == "-") // if the operation is '-', subtract b from a
        answer = a - b;

    else if(op == "+") // if the operation is '+', add a and b
        answer = a + b;

    else if(op == "*") // if the operation is '*', multiply a and b
        answer = a * b;

    else // assume op is '/', divide a and b
        answer = a / b;

    return to_string( answer ); // return the answer in string datatype
}

/**
 * CALCULATOR: solves the whole given expression using stack and queue
 * 
 * PARAMETER: none
 * RETURN: the resulting integer from the given arithmetic expression
*/
int solveQ() {
    // let x and y be the operands in the stack
    string x, y;

    // let s be the stack 
    stack<string> s;

    // loop through the queue until its empty
    while(!q.empty()) {
        // if the front element of the queue is an operator
        if(!isDigit( q.front() )) {
            // pop the first two elements of the stack
            y = s.top();
            s.pop();
            x = s.top();
            s.pop();

            // push to the stack the result of the arithmetic expression: x op y
            // let op be the fromt element of the queue
            s.push( solveArithmetic(x, y, q.front()) );

            // if the front element of the queue is a digit, push it to the stack
        } else
            s.push( q.front() );
        
        // pop the front element of the queue
        q.pop();
    }

    // return the top of the stack (the answer) in long int ddatatype
    return stol( s.top() );
}

/**
 * CALCULATOR: helper function that removes all the spaces in the given string
 * 
 * PARAMETER: string expression
 * RETURN: the expression with no spaces
*/
string removeSpaces(string expr) {
    // using erase function to remove any spaces in the string
    expr.erase(remove_if(expr.begin(), expr.end(), ::isspace), expr.end());

    // return the new expression
    return expr;
}

/**
 * CALCULATOR: the "main" function of this simple calculator
 * 
 * PARAMETER: string arithmetic expression
 * RETURN: the resulting answer for the given expression
 * 
*/
int calculate(string expr) {
    // remove the spaces in the expression first
    expr = removeSpaces(expr);

    // change the infix expression to postfix expression in queue
    postfix(expr);

    // return the answer of the given expression
    return solveQ();
}