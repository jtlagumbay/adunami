#pragma once    // Ensures this header is included only once during compilation to avoid redefinition issues

// Enum definition for various tokens that represent different elements in the programming language
enum Token {
    PROG_BEGIN,                     // beginning of program
    DECLARE,                        // declaration keyword
    VAR_NAME,                       // variable name
    ASSIGN,                         // assignment operation
    INPUT,                          // input operation
    OUTPUT,                         // output operation
    IF,                             // if keyword
    IF_STATEMENT,                   // if statement
    LOGICAL_EXPRESSION,             // logical expression
    ARITHMETIC_EXPRESSION,          // arithmetic expression
    ELSE,                           // else keyword
    ELSE_STATEMENT,                 // else statemtent
    ENDL,                           // end of line
    INTEGER,                        // integer type
    DOUBLE,                         // double type
    STRING,                         // string type
    CHARACTER,                      // character type
    COMMENT,                        // comment
    UNKNOWN,                        // unknown token
    END,                            // end of program
    PUNCTUATION,                    // punctuation symbol
    IN_OUT_OPERATOR,                // input/output operator
    ASSIGN_OPERATOR,                // assignment operator
    ARITHMETIC_OPERATOR,            // arithmetic operator
    USER_ARITHMETIC_EXPRESSION,     // user-defined arithmetic expression
    LOGICAL_OPERATOR,               // logical operator
    USER_INPUT,                     // Unknown type na variable kay depende sa user
};

// Function to convert a token enum value to its corresponding string representation
const char* tokenToString(Token t) {
    switch (t) {
        // map each token enum value to its respective string representation
        case PROG_BEGIN: return "PROG_BEGIN";
        case DECLARE: return "DECLARE";
        case VAR_NAME: return "VAR_NAME";
        case ASSIGN: return "ASSIGN";
        case INPUT: return "INPUT";
        case OUTPUT: return "OUTPUT";
        case IF: return "IF";
        case IF_STATEMENT: return "IF_STATEMENT";
        case LOGICAL_EXPRESSION: return "LOGICAL_EXPRESSION";
        case ARITHMETIC_EXPRESSION: return "ARITHMETIC_EXPRESSION";
        case USER_ARITHMETIC_EXPRESSION: return "USER_ARITHMETIC_EXPRESSION";
        case ELSE: return "ELSE";
        case ELSE_STATEMENT: return "ELSE_STATEMENT";
        case ENDL: return "ENDL";
        case INTEGER: return "INTEGER";
        case DOUBLE: return "DOUBLE";
        case STRING: return "STRING";
        case CHARACTER: return "CHARACTER";
        case COMMENT: return "COMMENT";
        case UNKNOWN: return "UNKNOWN";
        case END: return "END";
        case PUNCTUATION: return "PUNCTUATION";
        case IN_OUT_OPERATOR: return "IN_OUT_OPERATOR";
        case ASSIGN_OPERATOR: return "ASSIGN_OPERATOR";
        case ARITHMETIC_OPERATOR: return "ARITHMETIC_OPERATOR";
        case LOGICAL_OPERATOR: return "LOGICAL_OPERATOR";
        case USER_INPUT: return "USER_INPUT";
        default: return "tokenToString Error";               // default case for handling unknown tokens
    }
} 