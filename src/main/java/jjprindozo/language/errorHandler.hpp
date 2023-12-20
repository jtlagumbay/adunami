#pragma once        // prevents multiple inclusions of this header file during compilation
#include <iostream>
using namespace std;
// Enum of possible error codes
enum ErrorCode
{
  SYNTAX,       // Not starting with sa adm, or ending with hmn, unindentified token, Unexpected token
  SEMANTIC,     // Misuse of operators, 
  TYPE,         // Arithmetic operations on incompatible data types
  REFERENCE,    // Undefined variables,
  RUNTIME,      // Division by zero
  INPUT_OUTPUT, // File not found.
  ASM_GENERATION // Related to asm generation
};

// Function to convert ErrorCode enum values to a string for printing
const char* ErrorCodeToString(ErrorCode t) {
    switch (t) {
        // Mapping each ErrorCode enum value to its respective string representation
        case SYNTAX: return "SYNTAX";
        case SEMANTIC: return "SEMANTIC";
        case TYPE: return "TYPE";
        case REFERENCE: return "REFERENCE";
        case RUNTIME: return "RUNTIME";
        case INPUT_OUTPUT: return "INPUT_OUTPUT";
        default: return "ErrorCodeToString ERROR";   // Default case for handling unknown error codes
    }
}

class Error{
  public:
    ErrorCode error_code; // type sa error
    string client_msg; // Maoy makita sa compiler error 
    string trace; // What file and function gave the error
    string possible_cause;

    // constructor for error class
    Error(ErrorCode, string,  string, string);

    // function to print debug info about the error
    void debug();

    // allow easy printing of error objects
    friend ostream &operator<<(ostream &os, const Error &error){
    {
      os << ErrorCodeToString(error.error_code) << " ERROR: " << error.client_msg;
      return os;
    };
    
}
};

// Definition of the debug method to display detailed error information
void Error::debug() {
  cout <<endl<< "----------DEBUG MESSAGE----------" << endl;
  cout << ErrorCodeToString(error_code) << " ERROR" << "\nTrace: " << trace << "\nPossible Cause: " << possible_cause <<"\n";
  cout << endl<<"** NOTE: Delete all debug on production **" << endl<<endl;
}

// Implementation of the constructor for Error class
Error::Error(ErrorCode code, string m_client_msg, string m_trace, string m_possible_cause) : 
  error_code(code), 
  client_msg(m_client_msg), 
  trace(m_trace), possible_cause(m_possible_cause){
}
