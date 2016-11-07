/*
 * Copyright (c) 1994
 *      The Regents of the University of California.  All rights reserved.
 *
 * This code is derived from software contributed to Berkeley by
 * Marti Hearst.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *	This product includes software developed by the University of
 *	California, Berkeley and its contributors.
 * 4. Neither the name of the University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 */

/* 
  Flex code for reading in text.

  This code assumes that the text is single spaced and that paragraphs
  are separated by a blank line.  If some other structure is used, you
  need to reflect that here.

  This is a simple tokenizer for straight text.  If a document
  contains markup, or there are multiple documents in a single file,
  this is the file you would change to return the appropiate tokens.
  The directory called "tokenizers" contains two examples of more
  complicated flex code that does exactly this.
*/

%{

#include <stdio.h>

#include "token.h"

int token_type;
int token_seen = 0;

int lastbytes = 0;
int bytes = 0;
#undef YY_DECL
#define YY_DECL char *yylex(void) 
%}

TERM          [a-zA-Z]+([\-\'][a-zA-Z]+)*

%%

{TERM}                    {
                             token_seen = 1;
			     bytes += lastbytes;
			     lastbytes = yyleng;
                             token_type = TEXT_TOKEN;
		             return((char *)yytext);
		          }


^[ ]*\n                {
		     bytes += lastbytes;
		     lastbytes = yyleng;
		      if (token_seen == 1)
                         token_type = BLANK_TOKEN;
		      else
                         token_type = NULL_TOKEN;
		      return((char *)yytext);
                      }

^[ 	]+		{
		 bytes += lastbytes;
		 lastbytes = yyleng;
		token_type = INDENT_TOKEN;
		return((char *)yytext);
		}

\n|.              {
		 bytes += lastbytes;
		 lastbytes = yyleng;
                     token_type = NULL_TOKEN;
                     return((char *)yytext);

                    }
%%

lex_reset() {
	YY_NEW_FILE;
}
