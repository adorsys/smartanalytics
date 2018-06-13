grammar Expression;

expression: statement | expression operator expression | '(' expression ')' | 'NOT' expression;

statement: attribute comparator value;

attribute: 'RFN' | 'GID' | 'VWZ' | 'IBAN' | 'KTO' | 'BLZ' | 'AMOUNT' | 'HKAT' | 'UKAT' | 'SPEZ';

comparator: 'NOT LIKE' | 'LIKE' | '=' | '>' | '<' | '=>' | '<=';

operator: 'AND' | 'OR';

value : STRING | INT;

STRING: '\'' (LETTER | INT | SPECIAL_CHAR | ' ' )+ '\'';
INT : [0-9]+;
LETTER: [A-Z];
SPECIAL_CHAR: [+,./_&*%-];
WS: [ \t\r\n]+ -> skip ;
