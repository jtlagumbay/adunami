#include <iostream>
#include <cstdlib>

// Internal libraries
#include "tokenEnum.hpp" // types of tokens
#include "scanner.cpp" // scanner
#include "asmInstructions.cpp" // Asm related enumerator
#include "symbolTable.cpp" // Symbol table
#include "simpleCalculator.cpp" // Calculator for arithmetic


using namespace std;


/**
 * PARSER: Class for parsing and code generation
 * 
 * PARAMETER: None
 * RETURN: Vector of Vector of tokens 
 * 
 */
class Parser{
  /****** TOKENS AND ITERATOR ********/
  vector<vector<TokenInfo>> tokens;                 // Tokens per line
  vector<vector<TokenInfo>>::iterator curr_line;    // Current line
  vector<vector<TokenInfo>>::iterator end_line;     // End of the line
  vector<TokenInfo>::iterator curr_token;           // Start of token per line
  vector<TokenInfo>::iterator end_token;            // End of token per line

  /****** TOKENS MOVEMENT HELPER ********/
  void moveNext();                          // Move to next token
  void moveNextLine();                      // Move to next line
  TokenInfo peekNext();                     // Peek at the next token
  bool isEnd();                             // Check if at the end of tokens
  bool isEndLine();                         // Check if at the end of the line

  /****** PARSING TOKENS ********/
  void expect(Token);                       // Expect a specific token type
  void expectStatement(string);             // Expect a statement
  void expectInstruction();                 // Expect an instruction
  void expectAssign(string);                // Expect an assignment
  void parseInstruction(vector<TokenInfo>::iterator);       // Parse an instruction

  /****** ASM FILE WRITING ******/
  string adm_file_name; // adm_file
  string asm_file_name; // file name of generated asm
  ofstream asm_file_writer; // writer and reader of asm file
  void initAsmFile();   // Initialize the ASM file
  void appendData(AsmDataType, string, string); // Writes on the data section
  void appendLoadWord(AsmRegisters, string); // Writes MIPS load word instruction
  void appendLoadAddress(AsmRegisters, string); // Writes MIPS load address instruction
  void appendLoadImmediate(AsmRegisters, int); // Writes MIPS load immediate instruction
  void appendMove(AsmRegisters, AsmRegisters); // Writes MIPS Move instruction, accepts two registers
  void appendAdd(AsmRegisters, AsmRegisters, AsmRegisters); // add $reg, $reg, $reg
  void appendAddI(AsmRegisters, AsmRegisters, int); // add $reg, $reg, Immediate
  void appendJal(string); // jal branch_name
  void appendBeq(AsmRegisters, int, string); // beq $reg, Immediate, branch_name
  void appendBne(AsmRegisters, int, string); // bne $reg, Immediate, branch_name
  void appendBranchName(string); // branch_name:
  void appendJump(string); // j branch_name
  void appendSyscall(); // syscall
  void printInt(TokenInfo, string); // Series of MIPS instruction to print Integer
  void printStr(TokenInfo, string); // Series of MIPS instruction to print string

  /****** SYMBOL TABLE ******/
  SymbolTable symbol_table; // dedicated symbol table of the parser

public:
  Parser(vector<vector<TokenInfo>>&, string); // Constructor, accepts token list
  ~Parser(); // deconstructor, closes file readers and writers
  void start();                               // Start parsing
  void generateAsm();                         // Generate ASM code
  void printSymbolTable();                    // Print the symbol table
};

int main() {
  string file;
  // cout << "Enter file name (without file extension): ";
  cin >> file;


  string file_name = file + ".adm"; // file path of the source code
  string asmCommand = "java -jar mars.jar " + file + ".asm";
  
  try {
    
    Scanner m_scanner(file_name); // Instantiates scanner, inputs the file name
    vector<vector<TokenInfo>> tokens = m_scanner.start(); // generate the token list
    // m_scanner.printTokenList(); // debug scanner, comment later on

    Parser m_parser(tokens, file_name); // starts parsing, sends token list and file name
    m_parser.start(); // starts parsing

    // m_parser.printSymbolTable(); // debug symbol table, comment later on
    m_parser.generateAsm(); // finishing touches for the asm, closes files
    // run asm
    
    int result = system(asmCommand.c_str()); // sends system command to run MIPS Emulator
  } catch (Error& e) {
      cerr << e << endl; // Prints error message for the user
      e.debug(); // Prints debug message for the compiler writer
  }
  return 0;
}

/**
 * PARSER: Constructor
 * 
 * PARAMETER: Vector of vector of tokens, name of the adm file
 * RETURN: None
 * 
 */
Parser::Parser(vector<vector<TokenInfo>>& m_tokens, string m_file_name){

  tokens = m_tokens; // initiate the tokens of the parser with the input tokens
  // Erases empty tokens
  tokens.erase(
    remove_if(tokens.begin(), tokens.end(),
        [](const std::vector<TokenInfo>& innerVec) {
            return innerVec.empty();
        }
    ),
    tokens.end()
  );
  curr_line = tokens.begin(); // initiate the curr_line to the beginning of the list
  end_line = tokens.end(); // end iterator of the end of the list
  adm_file_name = m_file_name; // sets parser adm_file_name to the input name

  initAsmFile(); // Initializes asm writing and reading
}

/**
 * PARSER: Deconstructor
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
Parser::~Parser(){
  // Closes the asm file writer if open
  if(asm_file_writer.is_open()){
    asm_file_writer.close();
  }
}

/**
 * PARSER: Prints symbol table
 * Debugging purposes only.
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::printSymbolTable(){
  // Print the symbol table
  symbol_table.printSymbols(); // calls the method of the symbol table to print
}

/**
 * PARSER: ASM File initializer
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::initAsmFile(){

  istringstream iss(adm_file_name); // string reader of the file name
  getline(iss, asm_file_name, '.'); // separate by dot to get the root file name without extension
  asm_file_name.append(".asm"); // adds the asm file extension

  asm_file_writer.open(asm_file_name, ios::trunc); // opens the asm file writer then uses trunc to truncate existing asm content and start anew and create the asm file if not yet created

  // if there is error opening the file writer, throw error
  if(!asm_file_writer.is_open()){
    throw Error(
      INPUT_OUTPUT,
      "Error creating asm file",
      "parser.cpp > Parser::initAsmFile()",
      "Pagopen or create sa asm file."
    );
  }

  // Writes the data and text section of the asm file.
  asm_file_writer << ".data\n\n\n\n"
                  << ".text\n";
}

/**
 * PARSER: Move iterator to next instruction line
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::moveNextLine(){
  if(!isEndLine()){ // Checks if it is the end, do not increment if end already.
    curr_line++;
  }
}

/**
 * PARSER: Move to next token
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::moveNext(){
  if(!isEnd()){ // Checks if it is the last token in the line, do not mvoe if end already
    curr_token++;
  }
}

/**
 * PARSER: Starts parsing
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::start(){

  // Check if the program begins with the correct token, throw error if not
  if((*curr_line++)[0].type==PROG_BEGIN){
    expect(PROG_BEGIN);
  } else {
    throw Error(
      SYNTAX,
      "Unrecognized program. Adunami files should start with \'sa adm:\'",
      "parser.cpp > Parser::start",
      "User error or Wa natarong og save."
    );
  }

  // Checks if program is properly ended, throw error if not.
  if ((*--end_line)[0].type != END)
  {
    throw Error(
      SYNTAX,
      "Program did not end properly. Adunami files should end with \'hmn\'",
      "parser.cpp > Parser::start",
      "User error or Naputol ang file pag save."
    );
  }

  // Loop through lines and parse instructions while it is not yet end
  while(curr_line!=end_line && (*curr_line)[0].type!=END){
    // If not indented, throw error
    if((*curr_line)[0].depth<1){
      throw Error(
      SYNTAX,
      "Expecting indentation on line "+to_string((*curr_line)[0].line_number),
      "parser.cpp > Parser::start",
      "User error or Wrong ang count sa depth."
    );
    }
    
    expectInstruction(); // Expects instruction per line
    moveNextLine(); // after parsing the insttruction, move to next line
  }

  // Check for instructions after 'hmn'
  if(curr_line!=end_line){
    throw Error(
      SYNTAX,
      "Instructions after hmn. Adunami files should end with \'hmn\'",
      "parser.cpp > Parser::start",
      "User error or Naputol ang file pag save."
    );
  }

  // Check if the program ends with the correct token
  expect(END);
 

  return;
}

/**
 * PARSER: Expect Instruction
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 */
void Parser::expectInstruction(){
  // Expect an instruction

  // Return if it is end of the line already
  if(isEndLine()){
    return;
  }
  
  curr_token = (*curr_line).begin(); // sets current token to beginning of the instruction line.
  end_token = (*curr_line).end(); // sets end token to end of the instruction line
  TokenInfo curr_token_info = *curr_token; // point to current token



  switch(curr_token_info.type){ // checks what type of instruction is being parsed and give different parsing instruction depending on the type
    case OUTPUT: // if instruction starts with isuwat::
      expect(OUTPUT);
      expect(IN_OUT_OPERATOR);
      if((*curr_token).type==VAR_NAME){ // Parsing instructions if what is outputted is a variable name
        TokenInfo token = *curr_token;
        string m_var_name = (*curr_token).lexeme;
        Symbol m_symbol = symbol_table.getSymbol(m_var_name); // searches for the symbol using the varname which is the lexeme of the current token, throws error if not found
        expect(VAR_NAME); // expects var_name

        // Print integer or string based on symbol type
        if(m_symbol.type == INTEGER){
          printInt(token, m_symbol.value); // calls the instruction for appending to asm, print integers
        } else if(m_symbol.type == STRING){
          printStr(token, m_symbol.value); // calls instruction for appending to asm printing strings
        } else if(m_symbol.type == USER_INPUT){ // If the outputted file is a user input, check the data section of the asm
          // Handle user input
          string temp_key = m_symbol.var_name + to_string(token.line_number) + to_string(token.token_number);

          appendLoadAddress(A0, m_symbol.var_name); // loads the address of the variable 
          appendMove(S2, A0); // move to S0 because it is the parameter for the adm_remove_last_new_line
          appendJal("adm_remove_last_new_line"); // calls the mips code weve written to remove the last new line because the terminator in mips is \n
          appendMove(A0, S2); // move to A0 because it is the parameter for the syscall
          appendLoadImmediate(V0, 4); // Syscall value for printing string
          appendSyscall(); // syscall
        } else if (m_symbol.type == VAR_NAME){ // Prints a variable
          // Handle output of variable name
          Symbol m_var_symbol = symbol_table.getSymbol(m_symbol.value); // gets its value from the symbol table

          if(m_var_symbol.type==ARITHMETIC_EXPRESSION){ // if the variable contains arithmetic expression
            int integer_to_print = calculate(m_var_symbol.value); // compute first
            printInt(*--curr_token, to_string(integer_to_print)); // append mips instructions
            curr_token++;
          } else if (m_var_symbol.type==STRING){ // if the variable contains string
            printStr(*curr_token, m_var_symbol.value); // append mips instructions
          } else if (m_var_symbol.type==INTEGER){ // if the variable contains integer
            printInt(*curr_token, m_var_symbol.value); // append mips instructions
          } else { // throw error if none of the above
            throw Error(SEMANTIC, "Variable can only be of type integer or string or arithmetic expression", "parser.cpp > void Parser::expectInstruction(){ else if (m_symbol.type == VAR_NAME) ", "Most probably uncaught na case.");
          }


        } else if (m_symbol.type == ARITHMETIC_EXPRESSION){ // if the variable contains arithmetic expression
          // Handle arithmetic expression result
          // symbol_table.printSymbols();
          int result = calculate(m_symbol.value);
          printInt(token, to_string(result));
        } else { // throws error if none of the above
          throw Error(SEMANTIC, "Variable can only be of type integer or string", "parser.cpp > void Parser::expectInstruction(){ >  case OUTPUT: > if((*curr_token).type==VAR_NAME){ > else", "Most probably uncaught na case.");
        }
      } else if((*curr_token).type==STRING){ // if the output is a string
        // Handle output of string
        TokenInfo string_token = *curr_token; // assign to a tokeninfo, because expect() will move the curr token

        expect(STRING);

        printStr(string_token, string_token.lexeme); // append mips instruction for printing

      } else if((*curr_token).type==INTEGER){ // if the desired output is an integer
        // Handle output of integer
        TokenInfo int_token = *curr_token; // assign to a tokenInfo

        expect(INTEGER);

        printInt(int_token, int_token.lexeme); // appends mips instruction

      } else if((*curr_token).type==DOUBLE){ // If double, no parsing instruction for double yet
        expect(DOUBLE);
      } else if((*curr_token).type==CHARACTER){ // If character, no parsing instruction for character yet
        expect(CHARACTER);
      } else { // throw error if not of the above.
        throw Error(
          SYNTAX,
          "Expects variable name or value on or before line "+to_string((*--curr_token).line_number)+":"+to_string((*--curr_token).token_number+1)+". Check Adunami syntax.",
          "parser.cpp > Parser::expectInstruction() > case OUTPUT > else",
          "User error or wala na properly identify or separate ang token.");
      }
      break;
    case INPUT: // If the insturction is of type input, starts with isulod::
      // Handle input instruction
      expect(INPUT);
      expect(IN_OUT_OPERATOR);
      { // add block scope because cpp throws error if mag instantiate of varaible sa switch case niya wala nakasud sa scope
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME);
        
        // Allocate register for user input
        AsmRegisters m_reg = symbol_table.giveUnusedRegister(); // gives unused register

        symbol_table.editSymbol(USER_INPUT, m_var_name,"", m_reg); // edits symbol table to edit variable and add the unused register

        // Allocate register for user input
        appendData(SPACE, m_var_name, "128");
        appendLoadAddress(A0, m_var_name); 

        // Saving
        appendLoadAddress(m_reg, m_var_name);
        appendLoadAddress(A1, "128");
        appendLoadImmediate(V0, 8);
        appendSyscall();

        // Checking if integer
        appendMove(A2, m_reg);
        appendAddI(V0, V0, 0); // If Integer or not
        appendAddI(V1, V1, 0); // Returns the Integer if integer
        appendJal("adm_check_type"); // sets v0 to 0 if integer, then saves the integer form to v1
        appendMove(m_reg, V1); // move the value of the integer to m_reg
      }

      break;
    case DECLARE: // if the instruction starts with kuptan::
      {
        expect(DECLARE); 
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME); // should follow with a variable name
        symbol_table.addSymbol(UNKNOWN, m_var_name); // adds variable to the symbol table
        // Check if assignment follows the declaration
        if(!isEnd() and (*curr_token).type == ASSIGN_OPERATOR){ // if there is = operator after, it is expecting variable assignment, pass the m_var_name because it will be used for accessing the symbol table
          expectAssign(m_var_name);
        }
      }
      break;
    case END: // if the instruction is hmn
      expect(END);
      return;
    case VAR_NAME: // if the instruction is assignment type, example: name = 
      {
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME);
        symbol_table.resetSymbol(*curr_token,m_var_name); // resets the symbol because it will be reassigned to new value
        expectAssign(m_var_name); // calls the expect assignment function
      }
      break;
    default: // if none of the above, throw error
      throw Error(
          SYNTAX,
          "Unknown instruction on line "+to_string((*curr_token).line_number)+":"+to_string((*curr_token).token_number)+". Check Adunami syntax.",
          "parser.cpp > Parser::expectInstruction() > default",
          "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
  }
  
  return;
}

/**
 * PARSER: Expect Assign
 * 
 * PARAMETER: variable name
 * RETURN: None
 * 
 */
void Parser::expectAssign(string m_var_name){ 
  expect(ASSIGN_OPERATOR); // expects = operator
  if(isEnd()){ // if nothing follows it returns error
    throw Error(
      SYNTAX,
      "Expecting a variable or value on line "+to_string((*curr_token).line_number)+":"+to_string((*--curr_token).token_number+1),
      "parser.cpp > Parser::expectInstruction() > expectAssign",
      "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
  }

  // Expect statement after assignment
  expectStatement(m_var_name);
      
}

/**
 * PARSER: expects the token appropriate for the syntax
 * 
 * PARAMETER: expected token
 * RETURN: none
*/
void Parser::expect(Token expected_token){
  // end function if it is the end of the program
  if(isEnd()){
    return;
  }

  // Check if the current token matches the expected token
  // if not, throw error
  if((*curr_token).type!=expected_token){
    string expected_token_string = tokenToString(expected_token);
    string error_msg = "Expected " + expected_token_string + " at line " + to_string((*curr_token).line_number)+":"+to_string((*--curr_token).token_number+1)+".";
    throw Error(
      SYNTAX,
      error_msg,
      "parser.cpp > Parser::expect(Token expected_token)",
      "User error or token not scanned properly."
      );
  }

  // move to the next token 
  moveNext();
}

/**
 * PARSER: expects the appropriate statement for the expression
 * 
 * PARAMETER: string variable
 * RETURN: none
 * 
*/
void Parser::expectStatement(string m_var_name){
  // Get the current token
  TokenInfo m_token = *curr_token;
  // Retrieve symbol information for the current variable name
  Symbol m_symbol = symbol_table.getSymbol(m_var_name, *curr_token);
  // Initialize lexeme and symbol_value strings
  string m_lexeme = (*curr_token).lexeme;
  string m_symbol_value;

  // Check the type of the current token
  switch (m_token.type){
    case VAR_NAME:
      {
        // If the token type is VAR_NAME, get the symbol and calculate its value
        Symbol m_var_symbol = symbol_table.getSymbol((*curr_token).lexeme, *curr_token);
        int var_value = calculate(m_var_symbol.value);
        m_lexeme = to_string(var_value);

        // Consume and expect the VAR_NAME token
        expect(VAR_NAME);
      }
      break;

    case STRING: // expect a STRING token if type is STRING
      expect(STRING);
      break;
    case CHARACTER: // expect a CHARACTER token if type is CHARACTER
      expect(CHARACTER);
      break;
    case INTEGER: // expect an INTEGER token if token type is INTEGER
      expect(INTEGER);
      break;
    case DOUBLE: // expect a DOUBLE token if token type is DOUBLE
      expect(DOUBLE);
      break;
    default:
      throw Error(      // Throw an error if an unexpected token type is encountered
        SYNTAX,
        "Expecting a variable or value on line "+to_string((*curr_token).line_number)+":"+to_string((*--curr_token).token_number+1),
        "parser.cpp > Parser::expectStatement()",
        "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
  }

  if(m_symbol.type == UNKNOWN){ // meaning mao pay pag declare sa variable so ang data type sa variable kay ang data type sa sunod na tokem
    symbol_table.editSymbol(m_token.type, m_var_name, m_symbol.value+m_lexeme); // edit the symbol and pass the new token type
  } else {
    symbol_table.editSymbol(m_symbol.type, m_var_name, m_symbol.value+m_lexeme); // edit the symbol but pass the current token type
  }

   // Check if the end of the input is reached
  if(isEnd()){
    return;
  }

  // Check if the next token is an arithmetic operator
  if ((*curr_token).type==ARITHMETIC_OPERATOR){
    // Get the operator lexeme and symbol information
    string m_operator = (*curr_token).lexeme;
    Symbol m_symbol = symbol_table.getSymbol(m_var_name, *curr_token);

    // Consume and expect the ARITHMETIC_OPERATOR token
    expect(ARITHMETIC_OPERATOR);

    // Update the symbol type to ARITHMETIC_EXPRESSION and concatenate the operator
    symbol_table.editSymbol(ARITHMETIC_EXPRESSION, m_var_name, m_symbol.value + m_operator);

    if(isEnd()){          // Check if the end of the input is reached after consuming the operator
      throw Error(
      SYNTAX,
      "Expecting a variable or value on line "+to_string((*curr_token).line_number)+":"+to_string((*--curr_token).token_number+1),
      "parser.cpp > Parser::expectStatement() >  if ((*curr_token).type==ARITHMETIC_OPERATOR)",
      "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
    }

    // Recursively call expectStatement to handle the next part of the expression
    expectStatement(m_var_name);
  }
}

/**
 * PARSER: checks if the current token parsed is the end token
 * 
 * PARAMETER: none
 * RETURN: boolean expressionn if current token is the end token
*/
bool Parser::isEnd(){
  // Check if the current token has reached the end token
  return curr_token == end_token;
}

/**
 * PARSER: helper function that checks if the current line parsed is the end line
 * 
 * PARAMETER: none
 * RETURN: boolean expression if it is the end line
*/
bool Parser::isEndLine(){
  // Check if the current line has reached the end line
  return curr_line == end_line;
}

/**
 * PARSER: helper function that generates the starter code for assembly program
 * 
 * PARAMETER: none
 * RETURN: none
*/
void Parser::generateAsm(){
  // Generate the assembly code for program termination
  asm_file_writer
      << "\tli   $v0, 10  \n"
      << "\tsyscall \n\n\n\n"
      << "adm_check_type:\n\tlbu $s1, 0($a2)\n\tbeq $s1, 10, adm_is_int\n\tblt $s1, 48, adm_is_string\n\tbgt $s1, 57, adm_is_string\n\taddi $a2, $a2, 1\n\tj adm_check_type\n\nadm_is_int:\n\tli $v0, 0\n\tjr $ra\n\nadm_is_string:\n\tli $v0, 1\n\tjr $ra\n\nadm_remove_last_new_line:\n\tlbu $s1, 0($a0)\n\tbeq $s1, 10, exit_adm_remove_last_new_line\n\taddi $a0, $a0, 1\n\tj adm_remove_last_new_line\n\nexit_adm_remove_last_new_line:\n\tli $t0, 0\n\tsb $t0, 0($a0)\n\tjr $ra\n\n";

  // Close the assembly file
  asm_file_writer.close();
}

/**
 * PARSER: helper function that appends data to the assembly program
 * 
 * PARAMETER: datatype, the name, and the value
 * RETURN: none
 * 
*/
void Parser::appendData(AsmDataType data_type, string data_name, string data_value){
  // Append data section to the assembly file
  string to_append="\t";

  to_append += // store the data to be appended in a string
      data_name 
      + ":\t" 
      + asmDataToString(data_type) 
      + "\t"
      + data_value;

  // Close the assembly file
  asm_file_writer.close();

  // Open the temporary file for reading
  ifstream temp_file_reader(asm_file_name);

  // Check if the temporary file is successfully opened
  // throw error if file is not opened
  if (!temp_file_reader.is_open()) {
    throw Error(
      ASM_GENERATION,
      "Error generating asm code.",
      "parser.cpp > Parser::appendData()",
      "error sa pagcreate og temporary input file");
  }

  // Read the lines from the temporary file and update the data section
  vector<std::string> temp_lines;
  string temp_curr_line;

  // traverse through the file and find the .data section
  while (getline(temp_file_reader, temp_curr_line)) {
    temp_lines.push_back(temp_curr_line);
    if(temp_curr_line==".data"){
      temp_lines.push_back(to_append); // once found, push the data to append
    }
  }

  temp_file_reader.close();         // Close the temporary file

  // Open the assembly file in truncation mode
  asm_file_writer.open(asm_file_name, ios::trunc);

  // Write the updated lines to the assembly file
  for (const auto& updatedLine : temp_lines) {
      asm_file_writer << updatedLine << endl;
  }
}

/**
 * PARSER: helper function that writes load word instruction in the assembly program
 * 
 * PARAMETER: register to use, label
 * RETURN: none
 * 
*/
void Parser::appendLoadWord(AsmRegisters reg, string label){
  // Append load word instruction to the assembly file
  asm_file_writer<<"\tlw "<<asmRegToString(reg)<<", "<<label<<endl;
}

/**
 * PARSER: helper function that writes load address instruction to the assembly program
 * 
 * PARAMETER: register used, label
 * RETURN: none
 * 
*/
void Parser::appendLoadAddress(AsmRegisters reg, string label){
  // Append load address instruction to the assembly file
  asm_file_writer<<"\tla "<<asmRegToString(reg)<<", "<<label<<endl;
}

/**
 * PARSER: helper function that writes load immediate instruction to assembly program
 * 
 * PARAMETER: register used, label
 * RETURN: none
 * 
*/
void Parser::appendLoadImmediate(AsmRegisters reg, int value){
  // Append load immediate instruction to the assembly file
  asm_file_writer<<"\tli "<<asmRegToString(reg)<<", "<<value<<endl;

}

/**
 * PARSER: helper function that writes syscall instruction in the assembly program
 * 
 * PARAMETER: none
 * RETURN: none
 * 
*/
void Parser::appendSyscall(){
  // Append syscall instruction to the assembly file
  asm_file_writer<<"\tsyscall "<<endl<<endl;

}

/**
 * PARSER: helper function that writes the print insttructions for integer in the assembly program
 * 
 * PARAMETER: token information and value
 * RETURN: none
 * 
*/
void Parser::printInt(TokenInfo token, string val) {
  // Create a unique label for the integer value
  string int_label = "print_" + to_string(token.line_number) + "_" + to_string(token.token_number);
  
  // Append data section for the integer value
  appendData(WORD, int_label, val);

  // Load the integer value into register A0
  appendLoadWord(A0, int_label);
  
  // Load the syscall code for printing an integer into register V0
  appendLoadImmediate(V0, 1);

  // Execute the syscall for printing an integer
  appendSyscall();
}

/**
 * PARSER: helper function that writes the print instructions for string datatype in the assembly program
 * 
 * PARAMETER: token information and value
 * RETURN: none
 * 
*/
void Parser::printStr(TokenInfo token, string val) {
  // Create a unique label for the string value
  string to_print_label = "print_" + to_string(token.line_number) + "_" + to_string(token.token_number);

  // Append data section for the string value
  appendData(ASCIIZ, to_print_label, val);

  // Load the address of the string value into register A0
  appendLoadAddress(A0, to_print_label);

  // Load the syscall code for printing a string into register V0
  appendLoadImmediate(V0, 4);

  // Execute the syscall for printing a string
  appendSyscall();
}

/**
 * PARSER: helper function that writes move instruction in the assembly program
 * 
 * PARAMETER: destination register and the source register
 * RETURN: none
 * 
*/
void Parser::appendMove(AsmRegisters dest, AsmRegisters src){
  asm_file_writer << "\tmove " << asmRegToString(dest) << ", " << asmRegToString(src) << endl;
}

/**
 * PARSER: helper function that writes add instruction in the assembly program
 * 
 * PARAMETER: destination register and the 2 source registers
 * RETURN: none
 * 
*/
void Parser::appendAdd(AsmRegisters dest, AsmRegisters src1, AsmRegisters src2){
  asm_file_writer << "\tadd " << asmRegToString(dest) << ", " << asmRegToString(src1) << ", " << asmRegToString(src2) << endl;
}

/**
 * PARSER: helper function that writes addi instruction (value and a register addition) in the assembly program
 * 
 * PARAMETER: destination register, the source register, and the immediate value
 * RETURN: none
 * 
*/
void Parser::appendAddI(AsmRegisters dest, AsmRegisters src, int immediate){
  asm_file_writer << "\taddi " << asmRegToString(dest) << ", " << asmRegToString(src) << ", " << to_string(immediate) << endl;
}

/**
 * PARSER: helper function that writes jal instruction in the assembly program
 * 
 * PARAMETER: branch name
 * RETURN: none
 * 
*/
void Parser::appendJal(string branch){
  asm_file_writer << "\tjal " + branch <<endl<< endl;
}

/**
 * PARSER: helper function that writes branch equal instruction in the assembly program
 * 
 * PARAMETER: source register, immediate value, and the branch name
 * RETURN: none
 * 
*/
void Parser::appendBeq(AsmRegisters src, int immediate, string branch){
  asm_file_writer<<"\tbeq "<<asmRegToString(src)<<", "<<to_string(immediate)<<", "<<branch<<endl;

}

/**
 * PARSER: helper function that writes branch not equal instruction to the assembly program
 * 
 * PARAMETER: source register, immediate value, and branch name to go to
 * RETURN: none
 * 
*/
void Parser::appendBne(AsmRegisters src, int immediate, string branch){
  asm_file_writer<<"\tbnq "<<asmRegToString(src)<<", "<<to_string(immediate)<<", "<<branch<<endl;

}

/**
 * PARSER: helper function that writes branch name to the assembly program
 * 
 * PARAMETER: branch name
 * RETURN: none
 * 
*/
void Parser::appendBranchName(string branch){
  asm_file_writer <<"\n"<< branch << ":" << endl;
}

/**
 * PARSER: helper function that writes jump instruction to the assembly program
 * 
 * PARAMETER: branch name to go to
 * RETURN: none
 * 
*/
void Parser::appendJump(string branch){
  asm_file_writer << "\tj "<< branch << endl;
}