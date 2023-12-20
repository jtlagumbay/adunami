#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <regex>
#include <vector>
#include <algorithm>
#include <cctype>

/**
 *  Internal Libraries that will be needed for the scanner
 **/
#include "tokenEnum.hpp" // Holds the enumerator for the possible token types
#include "errorHandler.hpp" // Will be used for throwing error

using namespace std;

/**
 * Holds the necessary information about the token. 
 */
struct TokenInfo {
  int line_number; // Line number where the token is found
  int token_number; // Order of the token in the line
  int depth; // Indention counter
  Token type; // Type of the token
  string lexeme; // The actual token value
};

/**
 * 
 * Scanner Class for the ADM Programming Language 
 * 
 * Accepts name of the adm file
 * Outputs vectors of vectors of tokens of the input adm file
 * 
 */
class Scanner {
  private:

    /***  ATTRIBUTES ***/
    ifstream adm_file_reader; // Reads the adm file
    string adm_file; // adm file name
    vector<vector<TokenInfo>> token_list; // List of tokens per instruction line
    
    /***  METHODS ***/
    Token checkTokenType(const string&); // Returns the token type
    int countDepth(const std::string&); // Counts the indention
    vector<TokenInfo> scanLine(const string&, int, int); // Returns vector of tokens for an instruction line

  public:
    Scanner(string file_name); // Constructor accepting file name, and opens the file reader
    ~Scanner(); // Deconstructor, closes the file reader
    vector<vector<TokenInfo>> start(); // Starts parsing, returns the vector of vector of tokens
    void printTokenList(); // Prints actual token list, for debugging purposes.
};

/**
 * SCANNER: CONSTRUCTOR
 * 
 * Accepts adm file name
 */
Scanner::Scanner(string file_name){
  adm_file = file_name; // sets the attribute adm_file to the file_name accepted
  adm_file_reader.open(adm_file); // opens the file reader of the adm file

  // Checks if the adm_file_reader is opened, throws error if not.
  if(!adm_file_reader.is_open()){
    throw Error(
      INPUT_OUTPUT,
      "Adm file not found.",
      "scanner.cpp > Scanner::Scanner",
      "Incorrect file path."
    );
  }
}

/**
 * SCANNER: DECONSTRUCTOR
 * 
 * Closes adm_file_reader
 */
Scanner::~Scanner(){
  // Checks if adm_file_reader is open and closes it.
  if(adm_file_reader.is_open()){
    adm_file_reader.close();
  }
}

/**
 * SCANNER: COUNTS DEPTH
 * 
 * PARAMETER: Reference to a string
 * RETURN: Number of indention 
 * 
 */
int Scanner::countDepth(const string& line){
  int space = 0; // Initialize space to zero

  // Checks the characters in the line until it finds non-space character
  for (char c : line) // Gets the character in the line
  {
    // If the character is not a space, stop counting.
    if (!isspace(c)){ 
      break;
    } 
    // If the character is tab or space, increment space
    else if (c == '\t' || c == ' '){ 
      space++;
    }
  }
  // Returns the space divided by two, kay ang tab worth 2 spaces
  return space / 2; 
}

/**
 * SCANNER: Starts Scanning
 * 
 * PARAMETER: None
 * RETURN: Vector of Vector of tokens 
 * 
 */
vector<vector<TokenInfo>> Scanner::start(){
  string stream_line=""; // Storage for the current line being scanned.
  int line_number = 0; // Number of the line being scanned

  // Skips empty lines, do-while kay initially stream_line is set to empty string.
  do{
    getline(adm_file_reader, stream_line);
    line_number++;
  } while (stream_line == "");

  // Checks if starts with adm.
  // Gilahi ang sa adm kay arun di maapil sa getline na separated by space kay dapat di buwag ang "sa" og "adm" 

  // Throws error kay wala nagstart sa adm:
  if(stream_line !="sa adm:"){
    throw Error(
      SYNTAX,
      "Unrecognized program. Adunami files should start with \'sa adm:\'",
      "scanner.cpp > Scanner::start",
      "User error or Naputol ang file pag save."
    );
  } 

  // Since ga start man sa adm:, i sulod siya sa token_list.
  else {
    vector<TokenInfo> token_list_this_line; // Instantiate a vector of vector, pero ang ma sud ra jud ani kay ang sa_adm
    token_list_this_line.push_back(
      TokenInfo{
        line_number++,
        1,
        countDepth(stream_line),
        PROG_BEGIN,
        stream_line
      }
    );
    token_list.push_back(token_list_this_line); // ipush na sa token_list
  }

  // Scans per line, hantod mahurot ang lines sa input file
  while(getline(adm_file_reader, stream_line)){
    // remove all comments
    string line = regex_replace(stream_line, regex("\\*\\*\\s*([^*]+)\\s*\\*\\*"), "");
    // add space sa special characters kay iseparate by space ang tokens later.
    line = regex_replace(line, regex("::|=|\\+|\\-|/|\\*"), " $& ");

    // If line is empty, dili na magscan sa line, pero ang line_number dapat mo increment dayun.
    if(line.empty()){
      line_number++;
      continue;
    }

    // If line is not empty, i scan ang line, then ang result na vectors of tokens kay ipush sa token_list.
    token_list.push_back(
      scanLine(
        line, // line na iscan
        countDepth(stream_line), // count sa depth
        line_number++ // I post-increment ang line_number
      )
    );
  }

  // I return na ang vector of vector of tokens.
  return token_list;
}

/**
 * SCANNER: Print token list
 * Debugging Purposes only.
 * 
 * PARAMETER: None
 * RETURN: None
 * 
 * 
 */
void Scanner::printTokenList(){
  cout << "------ START OF TOKEN LIST ------" << endl;
  
  // Prints token list
  for (const auto& token_per_line : token_list) { // Iterator per instruction line
    for (const auto& element : token_per_line) // Iterator per token in the line
      cout << element.line_number  <<":"<<element.token_number<<" " << element.depth << " " << tokenToString(element.type) << " " << element.lexeme << endl; // Prints information of the token
  }
  cout << "------ END OF TOKEN LIST ------" << endl << endl;
}

/**
 * SCANNER: Scans line of instruction
 * 
 * PARAMETER: (String) Reference to the instruction line, (Int) depth of the line, (Int) line number
 * RETURN: Vector of tokens
 * 
 */
vector<TokenInfo> Scanner::scanLine(const string& inputLine, int depth, int line_number){
  vector<TokenInfo> tokens_this_line; // declares the vector of the tokens of the input line
  int token_number = 1; // Initiates the token_number, starting at one
  string expr; // Holds the token lexeme
  Token token; // Holds the token type later
  string prevExpr; // Holds the previous token lexeme.
  stringstream SS(inputLine); // String reader for the input line

  while (getline(SS, expr, ' ')) {
    // if the token is empty, proceed to next iteration
    if (expr.empty()) {
        continue;
    }
    
    string temp_expr = expr; // Holds temp_expr for the negative values, will be concatenated by next value
    
    // Checks if the integer is negative value
    if (expr == "-" && prevExpr=="=") {
       
        getline(SS, expr); // Scans the input line
        temp_expr += "" + expr; // Concatenatenaes the negative sign to the value
        expr = temp_expr; // sets the expr to the temp_expr

        // Stores the struct of the TokenInfo to the tokens_this_line
        tokens_this_line.push_back(
          TokenInfo{
            line_number, // Stores the line_number of the token
            token_number++, // Increments the token_number
            depth, // Stores the depth
            INTEGER, // Type is inteeger
            expr // stores the lexeme
          }
        );

        // Proceeds to next iteration
        continue;
    }

    // Checks if lexeme is logical expression, "mas labing". Initially separated man ni
    if (expr == "mas" || expr == "labing") {
      string logical_expr = expr; // sets the logical_expr to the expr
      while (getline(SS, expr, ' ')) {  // continues scanning
        temp_expr += " " + expr; // concatenates to the temp_expr
        logical_expr += " " + expr; // concatenates to the logical expression
        break;
      }
      // adds to the vector
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        LOGICAL_EXPRESSION, // sets token type to logical expression
        logical_expr
      });

      continue;
    }

    // if nag end og semicolon niya digit ang gicompare, ending sa conditional statement
    if (isdigit(expr.front()) && expr.back() == ':') {
      string digit = expr.substr(0, expr.size() - 1); // tangtangon ang colon sa digit
      string colon = ":";
      
      // Ipush ang token na integer sa list
      tokens_this_line.push_back(TokenInfo{ 
        line_number,
        token_number++,
        depth,
        INTEGER, // set it to integer
        digit
      });

      // push the colon to the list
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        PUNCTUATION, //type is function
        colon
      });
      // push ang tab kay gamit para mailhan na conditional siya
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        IF_STATEMENT,
        "\t"
      });

      continue;
    }
    // Checks else na part
    if (expr == "kondili:") {
      // Push the kondili lexeme, niya i type kay ELSE
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        ELSE,
        "kondili"
      });
      // Push the colon lexeme, iya type is punctuation
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        PUNCTUATION,
        ":"
      });
      // Push ang tab, para mailhan na ang ubos na start na sa else condition
      tokens_this_line.push_back(TokenInfo{
        line_number,
        token_number++,
        depth,
        ELSE_STATEMENT,
        "\t"
      });

      continue;
    }

    // Kung kitag double quotes, magsige og add sa temp_expr hantod sa kita og closing quotation. This is because ang string pwede naay space sa sulod niya gi separate by string man to initially.
    if (expr.front() == '\"') {
      while (getline(SS, expr, ' ')) { // Scans word
        temp_expr += " " + expr;  // concatenates to temp_expr
        if (expr.back() == '\"') { // stops scanning ig kita nag closing quotation
          break;
        }   
      }
      expr = temp_expr;   // sets the expr to the string na with spaces.
    } 

    prevExpr = expr; // sets the prev_expr to the current expr

    token = checkTokenType(expr); // checks token type of the lexeme

    tokens_this_line.push_back(TokenInfo{ // Pushes token info to the vector
      line_number,
      token_number++,
      depth,
      token,
      expr});
  }

  // Returns the tokens of this line
  return tokens_this_line;
}

/**
 * SCANNER: Checks token type
 * 
 * PARAMETER: String reference to a lexeme
 * RETURN: Token Type
 * 
 */
Token Scanner::checkTokenType(const string& expr) {
  // Vector pairs of the Token Type and its corresponding regex pattern
  vector<pair<regex, Token>> regex_patterns = {
      {regex("^(sa adm:)$"), PROG_BEGIN},
      {regex("^(hmn)$"), END},
      {regex("^(isuwat)$"), OUTPUT},
      {regex("^(isulod)$"), INPUT},
      {regex("^(kuptan)$"), DECLARE},
      {regex("^(kon)$"), IF},
      {regex("^(kondili)$"), ELSE},
      {regex("^(sama|mas dako|mas gamay|labing dako|labing gamay)$"), LOGICAL_EXPRESSION},      
      {regex("^-?[0-9]+$"), INTEGER},
      {regex("^-?[0-9]+.[0-9]+$"), DOUBLE},
      {regex("^\".*\"$"), STRING},
      {regex("^(=)$"), ASSIGN_OPERATOR},
      {regex("^[a-zA-Z][a-zA-Z0-9]*(_[a-zA-Z0-9]+)*$"), VAR_NAME},
      {regex("^(::)$"), IN_OUT_OPERATOR},
      {regex("^(\\+|\\-|\\*|/)$"), ARITHMETIC_OPERATOR},
      {regex("^(\\(|\\)|:|,|.)$"), PUNCTUATION}
  };

  // Loops into the regex patterns, until a match is found
  for (const auto &pattern : regex_patterns)
  {
    // If the lexeme matches the regex pattern, return the token type
    if (regex_match(expr, pattern.first))
    {
      return pattern.second;
    }
    }
  // If nahuman nalang ang loop then wala juy match na kit-an. return UNKNOWN.
  return UNKNOWN;
}

/**
 * SCANNER: Overloads extractor to print Token Information
 * Useful para kung magprint og tokeninfo, ditso na.
 * Example: cout<<TokenInfo;
 * ang moprint kay:
 * Line Number: 1
 * Token Number: 2
 * Depth: 1
 * Token Type: VAR_NAME
 * Lexeme: "name"
 *
 * PARAMETER: reference to ostream, and reference to a tokenInfo
 * RETURN: reference to an ostream
 *
 */
ostream& operator<<(std::ostream& os, const TokenInfo& tokenInfo) {
    os << "Line Number: " << tokenInfo.line_number << "\n"; // Prints line number
    os << "Token Number: " << tokenInfo.token_number << "\n"; // Prints token number
    os << "Depth: " << tokenInfo.depth << "\n"; // Prints line depth
    os << "Token Type: " << tokenToString(tokenInfo.type) << "\n"; // Prints token type into string (kay enum is INTEGER in CPP)
    os << "Lexeme: " << tokenInfo.lexeme << "\n"; // Prints the Lexeme
    return os;
}