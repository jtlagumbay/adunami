#pragma once

#include <iostream>
#include <vector>
#include <algorithm>
#include "tokenEnum.hpp"
#include "asmInstructions.cpp"
#include "errorHandler.hpp"
#include "scanner.cpp"

using namespace std;

/**
 * SYMBOL:  Symbol structure that contains token type, variable name, 
 *          value, and assigned register
 * 
*/
struct Symbol {
  Token type; 
  string var_name; // variable name
  string value; // value
  AsmRegisters reg; // assigned register
};

/**
 * SYMBOLTABLE: contains the symbols and functions in relation to the symbol table
*/
class SymbolTable{
  vector<Symbol> symbols; // dynamic array of symbols
  vector<Symbol>::iterator searchSymbol(string);

public: // functions for the symbol table
  void addSymbol(Token m_type, string m_var_name, string m_value = "", AsmRegisters m_reg = ZERO); 
  void editSymbol(Token m_type, string m_var_name, string m_value, AsmRegisters m_reg = ZERO);
  void printSymbols(); // prints the symbol table (necessary for debugging purposes)
  void resetSymbol(TokenInfo m_token_info, string m_var_name); // resets symbol for reassignment

  // fetch specific symbol from the symbol table
  Symbol getSymbol(string m_var_name, TokenInfo m_token_info =  
        TokenInfo{
          -1,
          -1,
          -1,
          UNKNOWN,
          ""
        }
  );

  // function that returns unused register
  AsmRegisters giveUnusedRegister();
};

// overloading of the operator '<<'
ostream& operator<<(std::ostream& os, const Symbol& m_symbol) {
    os << tokenToString(m_symbol.type) << "\t" << m_symbol.var_name << "\t" << m_symbol.value << "\t" << asmRegToString(m_symbol.reg);
    return os;
}

/**
 * SYMBOLTABLE: prints the symbols inside the table
 * 
 * PARAMETER: none
 * RETURN: none
*/
void SymbolTable::printSymbols(){
  cout << "------ START OF SYMBOL TABLE ------" << endl;
  for (const auto& symbol : symbols){ // gets each symbol inside the array symbols
    cout << symbol << endl;
  }
  cout << "------ END OF SYMBOL TABLE ------" << endl << endl;
}

/**
 * SYMBOLTABLE: searches a symbol inside the symbol array
 * 
 * PARAMETER: variable name of the symbol
 * RETURN: iterator it that points at the symbol being searched
*/
vector<Symbol>::iterator SymbolTable::searchSymbol(string m_var_name){
  auto it = find_if(symbols.begin(), symbols.end(), 
    [m_var_name](const Symbol &m_symbol)
      { return m_symbol.var_name == m_var_name; });

  return it;
}

/**
 * SYMBOLTABLE: adds the symbol to the dynamic array
 * 
 * PARAMETER: token type, variable name, value, and the register used
 * RETURN: none
*/
void SymbolTable::addSymbol(Token m_type, string m_var_name, string m_value, AsmRegisters m_reg){

  // checks the token type if it is a character, integer, or double.
  // if so, the value must not be empty
  if((m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE) && m_value==""){
    string m_type_str = tokenToString(m_type);
    throw Error(
        SEMANTIC,
        m_type_str + " \'" + m_var_name + "\' cannot be empty.",
        "symbolTable.cpp > SymbolTable::addSymbol > if((m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE) && m_value==\"\")",
        "Cannot be user error. Wala na tarong og pasa ang parameter. Ga pasa og data type pero walay value gi pasa.");
  }

  // checks the token type, UNKNOWN if the variable is not yet initialized to any type
  if(!(m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE || m_type == STRING || m_type == UNKNOWN)){
    
    // throws the error if token type is not character, integer, double, string, nor unknown
    throw Error(
        SEMANTIC,
        "Variable \'" + m_var_name + "\' can only be character, integer, double, or string.",
        "symbolTable.cpp > SymbolTable::addSymbol > if(!(m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE || m_type == STRING || m_type == UNKNOWN))",
        "Cannot be user error. Wala na tarong og pasa ang parameter.");
  }

  // checks if the given symbol to be added is already in the array
  auto it = searchSymbol(m_var_name);

  // if the symbol is in the array, throw a semantic error
  if(it != symbols.end()){
    throw Error(
      SEMANTIC,
      "Variable \'"+ m_var_name + "\' has already been used.",
      "symbolTable.cpp > SymbolTable::addSymbol > if(it != symbols.end())",
      "User error or wa na tarong store ang symbol.");
  
  // if not in the array, add it to the array
  } else {
    symbols.push_back(
        Symbol{
            m_type,
            m_var_name,
            m_value,
            m_reg});
  }

}

/**
 * SYMBOLTABLE: edit a symbol inside the array
 * 
 * PARAMETER: token type, variable name, value, and the register used
 * RETURN: none
 * 
*/
void SymbolTable::editSymbol(Token m_type, string m_var_name, string m_value, AsmRegisters m_reg)
{
  // search the symbol given using the variable name parameter
  auto it = searchSymbol(m_var_name);

  // checks the token type, if it is a character, integer, double, arithmetic expression, or variable name, value must not be empty.
  if((m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE || m_type == ARITHMETIC_EXPRESSION || m_type == VAR_NAME) && m_value==""){
    string m_type_str = tokenToString(m_type);
    throw Error( // throws the error if so
        SEMANTIC,
        m_type_str + " \'" + m_var_name + "\' cannot be empty.",
        "symbolTable.cpp > SymbolTable::editSymbol > if((m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE) && m_value==\"\")",
        "Cannot be user error. Wala na tarong og pasa ang parameter. Ga pasa og data type pero walay value gi pasa.");
  }

  // checks the token type if it is recognized. if not, throw error
  if(!(m_type == CHARACTER || m_type == INTEGER || m_type == DOUBLE || m_type == STRING || m_type == ARITHMETIC_EXPRESSION || m_type == VAR_NAME || m_type == USER_INPUT)){
    throw Error(
        SEMANTIC,
        "Cannot initialize variable \'" + m_var_name + "\'.",
        "symbolTable.cpp > SymbolTable::editSymbol > if(m_type == UNKNOWN)",
        "Cannot be user error. Wala na tarong og pasa ang parameter. Kung maginitialize, dapat nahibaw-an na ang data type daan.");
  }

  // if the searched symbol is not in the array, throw error
  if(it == symbols.end()){
    throw Error(
      SEMANTIC,
      "Variable \'" + m_var_name + "\' does not exist.",
      "symbolTable.cpp > SymbolTable::editSymbol > if(it == symbols.end())",
      "User error or wa na store ang symbol before.");

  // else, edit the details
  } else {
    it->type = m_type;
    it->value = m_value;
    if(m_reg!=ZERO){
      it->reg = m_reg;
    }
  }
}

/**
 * SYMBOLTABLE: fetch the symbol details in the array
 * 
 * PARAMETER: variable name and token information
 * RETURN: the symbol fetched
*/
Symbol SymbolTable::getSymbol(string m_var_name, TokenInfo m_token_info){
  // search the symbol in the array
  auto it = searchSymbol(m_var_name);
  
  // if it's in the array, return the symbol fetched
  if(it!=symbols.end()){
    return *it;
  
  // else, throw the error
  } else {
    string msg = "Variable \'" + m_var_name + "\' does not exist.";
    if(m_token_info.line_number != -1){
      string check_line = " Check line " + to_string(m_token_info.line_number) + ":" +to_string( m_token_info.token_number) + ".";
      msg.append (check_line);
    }
    throw Error(
      SEMANTIC,
      msg,
      "symbolTable.cpp > Symbol SymbolTable::getSymbol > else",
      "User error or wa na store ang symbol before.");
  }
}

/**
 * SYMBOLTABLE: resets the symbol given
 * 
 * PARAMETER: token information, the variable name
 * RETURN: none
 * 
*/
void SymbolTable::resetSymbol(TokenInfo m_token_info, string m_var_name){
  // search the symbol
  auto it = searchSymbol(m_var_name);
  
  // if it is in the array, reset its details
  if(it!=symbols.end()){
    it->reg = ZERO;
    it->type = UNKNOWN;
    it->value = "";
  
  // if not in the array, throw error
  } else {
    string msg = "Variable \'" + m_var_name + "\' does not exist.";
    if(m_token_info.line_number != -1){
      string check_line = " Check line " + to_string(m_token_info.line_number) + ":" +to_string( m_token_info.token_number) + ".";
      msg.append (check_line);
    }
    throw Error(
      SEMANTIC,
      msg,
      "symbolTable.cpp > Symbol SymbolTable::getSymbol > else",
      "User error or wa na store ang symbol before.");
  }
}

/**
 * SYMBOLTABLE: provides an unused register available for use
 * 
 * PARAMETER: none
 * RETURN: the available register
 * 
*/
AsmRegisters SymbolTable::giveUnusedRegister(){
  // dynamic list of unused registers
   static vector<AsmRegisters> unusedRegisters = {
        T0, T1, T2, T3, T4, T5, T6, T7,
        T8, T9
    };

    // if the list is empty, throw the error
    if (unusedRegisters.empty())
    {
        // All registers are used, handle this case as needed (throw an exception, return a special value, etc.)
        // For simplicity, let's return ZERO, but you might want to handle this differently.
        throw Error(RUNTIME, "No more memory.", "symbolTable.cpp > smRegisters SymbolTable::giveUnusedRegister() > if (unusedRegisters.empty())", "Limitation sa implementation. Nagamit na tanan register");
    }

    // Get the first unused register
    AsmRegisters result = unusedRegisters.back();

    // Remove it from the list of unused registers
    unusedRegisters.pop_back();

    return result; // return the register
}