=============================================================
C99 - A domain independent linear text segmentation algorithm

Freddy Y. Y. Choi

Artificial Intelligence Group
Department of Computer Science
University of Manchester
ENGLAND

e:mail : choif@cs.man.ac.uk
www    : http://www.cs.man.ac.uk/~choif

=============================================================

PACKAGE CONTENTS
----------------
bin			- Directory containing the executable of C99
doc			- Directory containing the documentation of C99
bin/C99         	- Script to execute the C99 algorithm
bin/C99release.jar      - Java classes and source for the C99 algorithm
doc/naacl00.ps.gz	- Paper describing the C99 algorithm (in Proceedings of NAACL'00)
doc/citation.bib	- BibTex entry for citation of the NAACL'00 paper


INTRODUCTION
------------
This package contains C99, a domain independent algorithm for partitioning text
into topically coherent sections. Our test results show this algorithm is significantly
more accurate then existing algorithms. Details of the algorithm is presented in
"../doc/naacl00.ps.gz".


LICENSE
-------
This package is free for educational, research and other non-profit making uses
only. For other uses, please contact Freddy Choi (choif@cs.man.ac.uk) with subject
header "C99:License"


REQUIREMENTS
------------
The package was tested using JDK 1.1.7B, other version may work.


USAGE
-----
The algorithm takes a list of tokenized text blocks (e.g. sentences, paragraphs)
as input. Each text line is a sequence of space separated tokens. General usage :

	C99 < "input file" > "output file"

Use "C99 --help" to display the help on program parameters.


BUG REPORTS
-----------
Direct all bug reports to "choif@cs.man.ac.uk" with the subject header "C99:Bug".


SUPPORT
-------
Direct all questions to "choif@cs.man.ac.uk" with the subject header "C99:Support"


VERSION
-------
1.1	Exact implementation of the algorithm described in "../doc/naacl00.ps.gz"


Acknowledgements
----------------
Parents			- Support in all possible way
Mary McGee Wood		- Academic support, discussions and firewall for administrative nasties
Jeff Reynar 		- Inspiring discussions, Dotplotter
Dan Oram		- Computer vision, C programming
Min-Yen Kan		- Segmenter
Marti Hearst		- TextTiling
IBM 			- VisualAge for Java (Linux), the best IDE I've ever used.
Blackdown team		- JDK1.1.7b (Linux port)
RedHat			- RedHat Linux 6.0
KDE team		- The beautiful KDE


===========
Freddy Choi
6th March 2000