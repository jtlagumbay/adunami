#include <iostream>
#include <cstdlib>

#include "tokenEnum.hpp"
#include "scanner.cpp"
#include "asmInstructions.cpp"
#include "symbolTable.cpp"
#include "simpleCalculator.cpp"


using namespace std;

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
  string asm_file_name;
  ofstream asm_file_writer;
  void initAsmFile();   // Initialize the ASM file
  void appendData(AsmDataType, string, string);
  void appendLoadWord(AsmRegisters, string);
  void appendLoadAddress(AsmRegisters, string);
  void appendLoadImmediate(AsmRegisters, int);
  void appendMove(AsmRegisters, AsmRegisters);
  void appendAdd(AsmRegisters, AsmRegisters, AsmRegisters);
  void appendAddI(AsmRegisters, AsmRegisters, int);
  void appendJal(string);
  void appendBeq(AsmRegisters, int, string);
  void appendBne(AsmRegisters, int, string);
  void appendBranchName(string);
  void appendJump(string);
  void appendSyscall();
  void printInt(TokenInfo, string);
  void printStr(TokenInfo, string);

  /****** SYMBOL TABLE ******/
  SymbolTable symbol_table;

public:
  Parser(vector<vector<TokenInfo>>&, string);
  ~Parser();
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

    Scanner m_scanner(file_name);
    vector<vector<TokenInfo>> tokens = m_scanner.start();
    // m_scanner.printTokenList(); // comment later on

    Parser m_parser(tokens, file_name);
    m_parser.start();

    // m_parser.printSymbolTable(); // comment later on
    m_parser.generateAsm();
    // run asm
    
    int result = system(asmCommand.c_str());
  } catch (Error& e) {
      cerr << e << endl;
      e.debug();
  }
  return 0;
}


Parser::Parser(vector<vector<TokenInfo>>& m_tokens, string m_file_name){

  tokens = m_tokens;
  tokens.erase(
    remove_if(tokens.begin(), tokens.end(),
        [](const std::vector<TokenInfo>& innerVec) {
            return innerVec.empty();
        }
    ),
    tokens.end()
  );
  curr_line = tokens.begin();
  end_line = tokens.end();
  adm_file_name = m_file_name;

  initAsmFile();
}

Parser::~Parser(){
  if(asm_file_writer.is_open()){
    asm_file_writer.close();
  }
}

void Parser::printSymbolTable(){
  // Print the symbol table
  symbol_table.printSymbols();
}

void Parser::initAsmFile(){

  istringstream iss(adm_file_name);
  getline(iss, asm_file_name, '.');
  asm_file_name.append(".asm");

  asm_file_writer.open(asm_file_name, ios::trunc);

  if(!asm_file_writer.is_open()){
    throw Error(
      INPUT_OUTPUT,
      "Error creating asm file",
      "parser.cpp > Parser::initAsmFile()",
      "Pagopen or create sa asm file."
    );
  }

  asm_file_writer << ".data\n\n\n\n"
                  << ".text\n";
}

void Parser::moveNextLine(){
  if(!isEndLine()){
    curr_line++;
  }
}
void Parser::moveNext(){
  if(!isEnd()){
    curr_token++;
  }
}

void Parser::start(){

  // Check if the program begins with the correct token
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
  if ((*--end_line)[0].type != END)
  {
    throw Error(
      SYNTAX,
      "Program did not end properly. Adunami files should end with \'hmn\'",
      "parser.cpp > Parser::start",
      "User error or Naputol ang file pag save."
    );
  }

  // Loop through lines and parse instructions
  while(curr_line!=end_line && (*curr_line)[0].type!=END){
    if((*curr_line)[0].depth<1){
      throw Error(
      SYNTAX,
      "Expecting indentation on line "+to_string((*curr_line)[0].line_number),
      "parser.cpp > Parser::start",
      "User error or Wrong ang count sa depth."
    );
    }
    expectInstruction();
    moveNextLine();
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

void Parser::expectInstruction(){
  // Expect an instruction

  // Skip if at the end of line
  if(isEndLine()){
    return;
  }
  
  curr_token = (*curr_line).begin();
  end_token = (*curr_line).end();
  TokenInfo curr_token_info = *curr_token;



  switch(curr_token_info.type){
    case OUTPUT:
      expect(OUTPUT);
      expect(IN_OUT_OPERATOR);
      if((*curr_token).type==VAR_NAME){
        TokenInfo token = *curr_token;
        string m_var_name = (*curr_token).lexeme;
        Symbol m_symbol = symbol_table.getSymbol(m_var_name);
        expect(VAR_NAME);

        // Print integer or string based on symbol type
        if(m_symbol.type == INTEGER){
          printInt(token, m_symbol.value);
        } else if(m_symbol.type == STRING){
          printStr(token, m_symbol.value);
        } else if(m_symbol.type == USER_INPUT){
          // Handle user input
          string temp_key = m_symbol.var_name + to_string(token.line_number) + to_string(token.token_number);

          appendLoadAddress(A0, m_symbol.var_name);
          appendMove(S2, A0);
          appendJal("adm_remove_last_new_line");
          appendMove(A0, S2);
          appendLoadImmediate(V0, 4);
          appendSyscall();
        } else if (m_symbol.type == VAR_NAME){
          // Handle output of variable name
          Symbol m_var_symbol = symbol_table.getSymbol(m_symbol.value);

          if(m_var_symbol.type==ARITHMETIC_EXPRESSION){
            int integer_to_print = calculate(m_var_symbol.value);
            printInt(*--curr_token, to_string(integer_to_print));
            curr_token++;
          } else if (m_var_symbol.type==STRING){
            printStr(*curr_token, m_var_symbol.value);
          } else if (m_var_symbol.type==INTEGER){
            printInt(*curr_token, m_var_symbol.value);
          } else {
            throw Error(SEMANTIC, "Variable can only be of type integer or string or arithmetic expression", "parser.cpp > void Parser::expectInstruction(){ else if (m_symbol.type == VAR_NAME) ", "Most probably uncaught na case.");
          }


        } else if (m_symbol.type == ARITHMETIC_EXPRESSION){
          // Handle arithmetic expression result
          // symbol_table.printSymbols();
          int result = calculate(m_symbol.value);
          printInt(token, to_string(result));
        } else {
          throw Error(SEMANTIC, "Variable can only be of type integer or string", "parser.cpp > void Parser::expectInstruction(){ >  case OUTPUT: > if((*curr_token).type==VAR_NAME){ > else", "Most probably uncaught na case.");
        }
      } else if((*curr_token).type==STRING){
        // Handle output of string
        TokenInfo string_token = *curr_token;

        expect(STRING);

        printStr(string_token, string_token.lexeme);

      } else if((*curr_token).type==INTEGER){
        // Handle output of integer
        TokenInfo int_token = *curr_token;

        expect(INTEGER);

        printInt(int_token, int_token.lexeme);

      } else if((*curr_token).type==DOUBLE){
        expect(INTEGER);
      } else if((*curr_token).type==CHARACTER){
        expect(INTEGER);
      } else {
        throw Error(
          SYNTAX,
          "Expects variable name or value on or before line "+to_string((*--curr_token).line_number)+":"+to_string((*--curr_token).token_number+1)+". Check Adunami syntax.",
          "parser.cpp > Parser::expectInstruction() > case OUTPUT > else",
          "User error or wala na properly identify or separate ang token.");
      }
      break;
    case INPUT:
      // Handle input instruction
      expect(INPUT);
      expect(IN_OUT_OPERATOR);
      {
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME);
        
        // Allocate register for user input
        AsmRegisters m_reg = symbol_table.giveUnusedRegister();

        symbol_table.editSymbol(USER_INPUT, m_var_name,"", m_reg);

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
        appendJal("adm_check_type");
        appendMove(m_reg, V1);

        
      }

      break;
    case DECLARE:
      {
        expect(DECLARE);
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME);
        symbol_table.addSymbol(UNKNOWN, m_var_name);
        // Check if assignment follows the declaration
        if(!isEnd() and (*curr_token).type == ASSIGN_OPERATOR){
          expectAssign(m_var_name);
        }
      }
      break;
    case END:
      expect(END);
      return;
    case VAR_NAME:
      {
        string m_var_name = (*curr_token).lexeme;
        expect(VAR_NAME);
        symbol_table.resetSymbol(*curr_token,m_var_name);
        expectAssign(m_var_name);
      }
      break;
    default:
      throw Error(
          SYNTAX,
          "Unknown instruction on line "+to_string((*curr_token).line_number)+":"+to_string((*curr_token).token_number)+". Check Adunami syntax.",
          "parser.cpp > Parser::expectInstruction() > default",
          "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
  }
  
  return;
}

void Parser::expectAssign(string m_var_name){
  expect(ASSIGN_OPERATOR);
  if(isEnd()){
    throw Error(
      SYNTAX,
      "Expecting a variable or value on line "+to_string((*curr_token).line_number)+":"+to_string((*--curr_token).token_number+1),
      "parser.cpp > Parser::expectInstruction() > expectAssign",
      "Either wala na tarong separate ang tokens, or wala na tarong identify ang tokens.");
  }

  // Expect statement after assignment
  expectStatement(m_var_name);
      
}

void Parser::expect(Token expected_token){
  if(isEnd()){
    return;
  }

  // Check if the current token matches the expected token
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

  moveNext();
}

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
        // cout << *curr_token << endl;
        // If the token type is VAR_NAME, get the symbol and calculate its value
        Symbol m_var_symbol = symbol_table.getSymbol((*curr_token).lexeme, *curr_token);
        int var_value = calculate(m_var_symbol.value);
        m_lexeme = to_string(var_value);

        // Consume and expect the VAR_NAME token
        expect(VAR_NAME);
      }
      break;
    case STRING:
      expect(STRING);
      break;
    case CHARACTER:
      expect(CHARACTER);
      break;
    case INTEGER:
      expect(INTEGER);
      break;
    case DOUBLE:
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
    symbol_table.editSymbol(m_token.type, m_var_name, m_symbol.value+m_lexeme);
  } else {
    symbol_table.editSymbol(m_symbol.type, m_var_name, m_symbol.value+m_lexeme);
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

bool Parser::isEnd(){
  // Check if the current token has reached the end token
  return curr_token == end_token;
}
bool Parser::isEndLine(){
  // Check if the current line has reached the end line
  return curr_line == end_line;
}

void Parser::generateAsm(){
  // Generate the assembly code for program termination
  asm_file_writer
      << "\tli   $v0, 10  \n"
      << "\tsyscall \n\n\n\n"
      << "adm_check_type:\n\tlbu $s1, 0($a2)\n\tbeq $s1, 10, adm_is_int\n\tblt $s1, 48, adm_is_string\n\tbgt $s1, 57, adm_is_string\n\taddi $a2, $a2, 1\n\tj adm_check_type\n\nadm_is_int:\n\tli $v0, 0\n\tjr $ra\n\nadm_is_string:\n\tli $v0, 1\n\tjr $ra\n\nadm_remove_last_new_line:\n\tlbu $s1, 0($a0)\n\tbeq $s1, 10, exit_adm_remove_last_new_line\n\taddi $a0, $a0, 1\n\tj adm_remove_last_new_line\n\nexit_adm_remove_last_new_line:\n\tli $t0, 0\n\tsb $t0, 0($a0)\n\tjr $ra\n\n";

  // Close the assembly file
  asm_file_writer.close();
}

void Parser::appendData(AsmDataType data_type, string data_name, string data_value){
  // Append data section to the assembly file
  string to_append="\t";

  to_append += 
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

  while (getline(temp_file_reader, temp_curr_line)) {
    temp_lines.push_back(temp_curr_line);
    if(temp_curr_line==".data"){
      temp_lines.push_back(to_append);
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

void Parser::appendLoadWord(AsmRegisters reg, string label){
  // Append load word instruction to the assembly file
  asm_file_writer<<"\tlw "<<asmRegToString(reg)<<", "<<label<<endl;
}

void Parser::appendLoadAddress(AsmRegisters reg, string label){
  // Append load address instruction to the assembly file
  asm_file_writer<<"\tla "<<asmRegToString(reg)<<", "<<label<<endl;
}

void Parser::appendLoadImmediate(AsmRegisters reg, int value){
  // Append load immediate instruction to the assembly file
  asm_file_writer<<"\tli "<<asmRegToString(reg)<<", "<<value<<endl;

}

void Parser::appendSyscall(){
  // Append syscall instruction to the assembly file
  asm_file_writer<<"\tsyscall "<<endl<<endl;

}

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

void Parser::appendMove(AsmRegisters dest, AsmRegisters src){
  asm_file_writer << "\tmove " << asmRegToString(dest) << ", " << asmRegToString(src) << endl;
}

void Parser::appendAdd(AsmRegisters dest, AsmRegisters src1, AsmRegisters src2){
  asm_file_writer << "\tadd " << asmRegToString(dest) << ", " << asmRegToString(src1) << ", " << asmRegToString(src2) << endl;
}
void Parser::appendAddI(AsmRegisters dest, AsmRegisters src, int immediate){
  asm_file_writer << "\taddi " << asmRegToString(dest) << ", " << asmRegToString(src) << ", " << to_string(immediate) << endl;
}
void Parser::appendJal(string branch){
  asm_file_writer << "\tjal " + branch <<endl<< endl;
}

void Parser::appendBeq(AsmRegisters src, int immediate, string branch){
  asm_file_writer<<"\tbeq "<<asmRegToString(src)<<", "<<to_string(immediate)<<", "<<branch<<endl;

}
void Parser::appendBne(AsmRegisters src, int immediate, string branch){
  asm_file_writer<<"\tbnq "<<asmRegToString(src)<<", "<<to_string(immediate)<<", "<<branch<<endl;

}
void Parser::appendBranchName(string branch){
  asm_file_writer <<"\n"<< branch << ":" << endl;
}
void Parser::appendJump(string branch){
  asm_file_writer << "\tj "<< branch << endl;
}