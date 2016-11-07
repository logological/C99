
            README for Segmenter Academic Research Distribution
                                      
          $Id: README.html,v 1.5 2000/01/24 01:53:57 min Exp min $
                                      
   Jump to: [ Copyright ] [ File Listing ] [ Installation and General
   Notes ] [ Installing and Running the CGI Web Interface ] [ Program
   Descriptions and Dependencies and Examples of Use ] [ Troubleshooting
   ] [ Log of revisions to this file]
     _________________________________________________________________
   
Copyright Notice

   Copyright © 1998-2000 by The Trustees of Columbia University in the
   City of New York
     _________________________________________________________________
   
Explanation of files in this directory

     * LICENSE.txt - license of software. Must be distributed with any
       copy of the software installed.
     * Makefile - makefile for test cases
     * README.html - This file
     * bin/ - executable directory
     * doc/ - all documentation on the software and specific
       subdirectories
     * lib/ - library data needed by segmenter and its subcomponents as
       well as evaluation materials
     * lib/corpus - evaluation corpus, cited in paper
     * test/ - subdirectory of test cases and results, for evaluating
       shipment package integrity
     _________________________________________________________________
   
Installation and General Notes

   Requirements: perl5, some perl4 libraries
   Main program components: termer, segmenter
   Utility program components: dbmEdit, layoutRecognizer,
   sentenceRecognizer
   Data components: modifiedComlex.db modifiedComlex.new.db normal.prm
   
   The segmenter programs should be executable without any formal
   installation. You can distribute the data components to any accessible
   location, provided you edit the executable file to point to it. If you
   want to do this, search for "PROGRAM COMPONENTS" in each program, and
   adjust accordingly.
   
   Make sure that the first line of *each* program file points to a
   working version of perl, version 5. Also ensure that each file is
   executable.
   
   All the programs are written as perl scripts and have three standard
   switches to which you can query aspects of the program. Use "-h" to
   get program usage information, "-v" to print version information and
   "-q" (quiet) to surpress the license information output on STDERR. You
   can also use a Control-C interrupt to abort the current processing at
   any stage. The programs will attempt to clean up any temporary files
   that it is using and terminate cleanly.
     _________________________________________________________________
   
Installating and Running the CGI Web Interface

   This new release of the segmenter package includes a script for
   running the segmenter and associated (important) programs using a web
   interface. The program, segment.cgi and its associated components,
   should be copied to a WWW accessible directory. Make sure the
   components are accessible by the web, and that the CGI script is
   executable and can reach the program components directly.
   
   Ask your local administrator for help installing support for CGI
   scripts on your local web server. If all else fails you can email me
   <min@cs.columbia.edu>, or see the web interface on our machines. You
   can find it from my home page: http://www.cs.columbia.edu/~min/
     _________________________________________________________________
   
Program Descriptions and Dependencies and Examples of Use

  Termer and Segmenter
  
   The two main programs. Termer takes an TEI.2 structured SGML file and
   produces noun phrases, pronomial forms and cue phrases as output in
   tabular form. This output is then utilized by segmenter to produce a
   linear segmentation, complete with segmentation labelling, key noun
   phrases, and segment importance.
   
   Typical invocation:
                bin/termer somefile.sgml | bin/segmenter

   Output format: Both termer and segmenter output high level information
   about the file in commented lines that begin with a pound sign (#).
   Termer's output reads as follows:
7[1]    ok[2]   year[3] (7 9 22 26 33 45 53[4])

[1]     number of occurences of the term in the document
[2]     subsumption label (broken in this distributed version)
[3]     The term itself.  The term may be suffixed with one to four stars (*).

        no stars = common NP            * = pronomial form
        ** = proper NP
        *** = cue phrase that occured at the beginning of the sentence
[4]     Location of occurences: in sentences 7,9,22...

   Segmenter's output reads as follows:
1. [1] Detail Segment: [2]
        Spans Unit(s):  1-2 [3]
        Segment Importance:     33 [4]
        Localized Terms:        fall,Stag**,Winiarski** [5]
        Non-local Terms:        bottle,wine,price,Cabernet** [6]

[1]     Segment number
[2]     Segment type
[3]     Segment size: first paragraph to last paragraph number
[4]     Segment Importance: linear scale, higher is more important
[5]     Local terms: common NPs () or proper NPs (**) that are local to the seg
ment
[6]     Non-local terms: common NPs () or proper NPs (**) that also occur elsew
here
        in other segments

   Notes: There is a paper that uses these tools and reports favorable
   results compared to earlier work by others. To get the latest version
   of this paper, go to http://www.cs.columbia.edu/~min/papers/seg.ps
   ("Linear Segmentation and Segment Relevance", published in the 6th
   Workshop on Very Large Corpora (1998).) I have included the current
   version of the paper in the doc/ subdirectory, as a MS Word95 file
   "seg.doc" and as a gzip'ed postscript file for US letter dimensions
   "seg.ps.gz".
   
   These tools are slightly different from the ones described in the
   paper, in particular the cgi (web interface), input, evaluation and
   analysis modules have been removed. If you are interested in obtaining
   these auxillary modules, please contact me.
   
   Dependencies: Both Segmenter and Termer are dependent on the input
   file being in TEI.2 format. Termer also utilizes the lookup table
   lib/modifiedComlex.db for POS tagging. Segmenter utilizes the
   lib/normal.prm parameter file to assign scores to paragraphs for
   segmentation. If the Segmenter and/or Termer are not working due to
   the database interfaces, you may have to recompile the lexical
   databases. See Troubleshooting section below.
   
  dbmEdit
  
   dbmEdit is a general purpose utility to manipulate db files created
   and manipulate thru perl's DB_File module. I use this to add, delete,
   change or view data in the modifiedComlex.db file. If you find that
   you need to edit modifiedComlex.db file to exclude or include certain
   words/POS_tag combinations, this might be a good utility to use. The
   tool is general to all perl DB_File files; thus this utility has use
   beyond the scope of the segmenter package.
   
   Typical invocation:
                bin/util/dbmEdit  ../../lib/modifiedComlex.db

   Notes: This can be an interactive program. Remember, arguments to
   commands are separated by tabs. Help is available from the dbmEdit>
   prompt by typing an "h".
   
  layoutRecognizer and sentenceRecognizer
  
   To create TEI.2 formatted text for use with the termer | segmenter
   pipeline, I have created two conversion programs to help with
   converting HTML files and text files following the simplistic
   convention of the LDC Wall Street Journal files. The sentence
   recognition program is really naïve and doesn't do a good job; it was
   a hack at the time. I advise manually proofing the sentence
   recognizer's output for correcting errors. The output is the document
   with <s> and </s> tags inserted to delimit boundaries.
   
   layoutRecognizer implicity calls sentenceRecognizer to recognize
   sentences within a whole document. It adds the mandatory TEI header
   information and encloses
   
   information. For HTML documents, many tags are removed and only
   essential (read "<P>") tags are left. The sentenceRecognizer program
   is called on each paragraph chunk to delimit sentences.
   
   Typical invocation:
                bin/util/layoutRecognizer wsj_0071 > wsj_0071.sgml

   Notes: We have a small corpus of TEI.2 information complete with
   manually added tags for showing the referents for instances of
   pronominal anaphora. The corpus is not included in this distribution,
   but is available on request. The encoding style and information is
   shown in the file doc/sgmlMarkup.html. The corpus also comes with an
   evaluation database that includes segmentation data taken from human
   judges.
   
  mergeAnnotations
  
   To merge together the annotations from segmenter back into the TEI.2
   formatted text as output by layoutRecognizer. Also to be compatible
   with use with the output of layser layout parser program \251 (CU &
   IBM).
   
   mergeAnnotations merely needs the segmenter output file and will
   retrieve the according filename.sgml file associated with the original
   input to the termer/segmenter pipeline.
   
   Typical invocation:
                bin/util/mergeAnnotations somefile.seg
        or      bin/termer somefile.sgml | bin/segmenter | bin/util/mergeAnnota
tions

  dbmBuildAverageSegment
  
   To reconstruct the majority (average) gold standard from the
   userSegment database. The averageUserSegment database is used by the
   segmenter program in determining precision and recall.
   
   Typical invocation (has defaults):
 dbmBuildAverageSegment

  positioner
  
   Takes the output of the termer program and displays a histogram of the
   term locations, one sentence per column.
   
   Used to visualize term occurences. If you want to sort the output, you
   may want to use the -m (matrix only) operand. Many visualization
   options, see -h for more details.
                bin/termer -q somefile.sgml | bin/positioner -q
or              bin/termer -q somefile.sgml | bin/positioner -qm | sort
     _________________________________________________________________
   
Troubleshooting

    1. Loading segmenter on other platforms.
       Many thanks to Jennifer Smith <jsmith@media.mit.edu> for nailing
       some instructions down on getting the segmenter package to work on
       other platforms!

I wanted to let you know that I finally have segmenter installed.   Let me
tell you what I discovered was necessary for installation so that you can
perhaps include it on your installation instructions.

As I said before, I needed to install DB_File, which does not seem to come
standard with perl, even in the latest version (5.004).  Here's the link
to where I got it: http://www.CPAN.org/modules/by-module/DB_File/

I first chose the latest version of DB_File (version 1.64), which they
said worked with my version of perl.  However, when I ran segmenter's
"make", it turned out that for some reason it had to be version 1.55 of
DB_File.  I am not completely sure that this was a problem with segmenter
- it could have been something about my system - but if it is segmenter,
it would be good to include that in the instructions.

For DB_File to work, I also needed to install a version of Berkeley DB for
the IRIX I am using.  If you are interested, here's the link to where I
got that: http://reality.sgi.com/ariel/freeware/.  However, that URL
is also in the Readme for DB_File.
    2. Database access seems to break
       Many thanks to Stan Szpakowitz <szpak@cs.waikato.ac.nz> for
       helping me debug the installation of the library files. I feel
       this may be caused by the same problem with the DB_File version
       that Jennifer Smith had to end up using. Sigh, version control for
       third party software. In any case there's a workaround. If you
       need the two plain format files, please let me know.
       In Solaris 5.7 and in (some versions of?) linux the DBM file
       format is different. You need to redirect the $comlexLoc variable
       in the termer script to reflect this. Point it to the
       modifiedComlex.new.db instead of the modifiedComlex.db.

       > Hi. I've been trying to run the Segmenter self-testing script. Here is
       > where it gets stuck:
       >
       >
       > bin/termer -q test/wsj_0071.sgml | sort > test7190.txt
       > Cannot open database "/home/szpak/nzdl/segmenter/lib/modifiedComlex.db
": Invalid argument at bin/termer line 78.
       > diff test7190.txt test/bts0071.txt > /dev/null || (cat test/fail4.txt
&& rm -f test7190.txt && exit 1)
       > ****************************************
       > ** fail test (4) termer
       > ****************************************
       >
       > I checked that the file exists all right:
       >
       > segmenter [279]: ls -l /home/szpak/nzdl/segmenter/lib/modifiedComlex.d
b
       > -rw-rw-rw-   1 szpak    compsci  1245184 Jun 29 07:43 /home/szpak/nzdl
/segmenter/lib/modifiedComlex.db
       >
       > What's wrong?
       >
       > Thanks!
       >

       That's a good question.  I'm not sure.  I have a feeling the database fi
le
       might be corrupted, or needs to be rebuilt because your system's DB file
s
       formatting differ from the ones that I included in the package.  The goo
d
       news is it's easy to fix.

       I'll leave you two files on the web server that you can download, that h
ave
       the databases in plaintext format.  The files are named userSegment.txt
and
       modifiedComlex.txt.  Again, the URL, userid and password are the same as
       for the segmenter.tgz file.

       You'll need to use the dbmEdit utility, under segmenter/bin/util to
       re-create the database files to your system's format, like so:

       ~min/ab/segmenter$ ./bin/util/dbmEdit -c ./lib/modifiedComlex.db < \
       modifiedComlex.txt
    3. Which command runs the segmenter
       If you have the sgml tagged text it's just:
       
                        termer file.sgml | segmenter
       where termer finds the term locations and segmenter uses termer's
       output to calculate a segmentation.
    4. Does Segmenter need SGML-tagged text, and is there a bare minimum
       mark-up?
       Segmenter (or more specifically termer) uses SGML files that are
       TEI P.3 compliant. For more information on the TEI format see the
       Text Encoding Initiative home page at
       http://www.uic.edu:80/orgs/tei/.
       Not all the TEI P.3 are recognized, in fact very few are used. The
       TEI P.3 header is used store canonical file information such as
       title, publication and author information. The header, although
       required, is skipped for segmentation processing. The only tags
       directly used by the segmentation are paragraph (<P> and </P>) and
       sentence delimiters (<S>. and </S>) pairs. These can occur
       anywhere in the file. We have provided a prototype utility
       layoutRecognizer designed to help convert raw text or HTML to the
       minimal markup needed by the segmenter application. See above
       section for notes on its use.
    5. Should the input text be tagged for sentence numbers as well?
       No, this is not required. The software will count the sentences by
       itself. The termer output specifically detects the technical
       terminology and builds the correlations.
     _________________________________________________________________
   
Log of revisions to this file

$Log: README.html,v $
Revision 1.5  2000/01/24 01:53:57  min
Added log section.
     _________________________________________________________________
   
      
    Min-Yen Kan <min@cs.columbia.edu>
    
      Created on: Thu Apr 30 02:42:15 1998 | Version: 1.0 | Last modified: Tue
      Jan 25 14:36:56 2000
