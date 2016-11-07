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

#include <stdio.h>
#include <errno.h>
#include <unistd.h>
#include "tile.h"

int	Argc;
char	**Argv;
int	f_showoffsets;

/*
 *  Main
 */
main(int argc, char **argv)
{
	TILEDOC *tdp;
	int ret = 0;

	Argc = argc;
	Argv = argv;

	tile_getopts();

	for (; *Argv; ++Argv) {
		if (verbose)
			fprintf(stderr, "processing %s\n", *Argv);
		if ((tdp = tile(*Argv, NULL)) == NULL) {
			fprintf(stderr, "tile: %s: %s\n", 
				*Argv, strerror(errno));
			ret |= 1;
			continue;
		}
		if (f_showoffsets)
			ret |= showoffsets(*Argv, tdp);
		else
			ret |= showtext(*Argv, tdp);
		freetiledoc(tdp);
	}

	exit(ret);
}

tile_getopts()
{
	int	c;
	extern char *optarg;
	extern int optind;

	while ((c = getopt(Argc, Argv, "oib:n:k:w:xpv")) != -1) {
		switch (c) {
		case 'o':
			f_showoffsets++;
			break;
		case 'i':
			indented++;
			break;
		case 'b': 
			bound = atoi(optarg);
			break;
		case 'n': 
			numiter = atoi(optarg);
			break;
		case 'k': 
			this_k = atoi(optarg);
			break;
		case 'w': 
			word_sep_num = atoi(optarg);
			break;
		case 'p': 
			not_para_boundaries = 1;
			break;
		case 'v': 
			verbose = 1;
			break;
                case '?':
                default:
                        exit(1);
		}

	}
	Argv += optind;
	Argc -= optind;
}

/*
 * output routine:
 *	show the actual text of the tiles appropiatly labeled
 */
showtext(char *fname, TILEDOC *tdp)
{
	FILE *fp;
	int tx, c;
	long i;
	TILE *tp = tdp->tilearray;

	if ((fp = fopen(fname, "r")) == NULL) {
		fprintf(stderr, "tile: can't open %s: %s\n", 
			fname, strerror(errno));
		return (1);
	}
	for (tx = 0; tx < tdp->numtiles; tx++) {
		printf("<TILE %d - FILE: %s START: %ld END: %ld>\n",
			tx, fname, tp[tx].startoff, tp[tx].endoff);
		fseek(fp, tp[tx].startoff, SEEK_SET);
		for (i = 0; i < tp[tx].endoff - tp[tx].startoff; i++) {
			c = getc(fp);
			if (c == EOF)
				break;
			(void)putc(c, stdout);
		}
		printf("</TILE %d - FILE: %s START: %ld END: %ld>\n",
			tx, fname, tp[tx].startoff, tp[tx].endoff);
	}
	fclose(fp);

	return (0);
}

/*
 * output routine:
 *	show just the tile locations
 */
showoffsets(char *fname, TILEDOC *tdp)
{
	int tx;
	TILE *tp = tdp->tilearray;

	printf("# FILE: %s\n# NUMTILES: %d\n# start	end\n",
		fname, tdp->numtiles);
	for (tx = 0; tx < tdp->numtiles; tx++) {
		printf("%ld	%ld\n",
			tdp->tilearray[tx].startoff,
			tdp->tilearray[tx].endoff);
	}

	return (0);
}
