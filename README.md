# hangman-judge

Judge Interactor program for a Hangman contest held for the Ateneo Senior High School Programming Varsity.  You need to have Java Runtime Environment version 8 or higher in order to run this.  To check, you can type `java -version` in the command line/terminal, and ensure that the version that appears is at least `1.8.something`.

Download `Judge.jar` from the repo.  Run the player program through the command line/terminal by entering `java -jar Judge.jar`.
Then, input the your program's file name and extension when prompted.  The following file extensions are supported,

    File Extension          Process Run
    
    fileName.py            python fileName.py (or python3 fileName.py on Linux and Mac)
    fileName.class         java fileName
    fileName.exe           fileName.exe
    fileName.out           ./fileName.out
    
Ensure that the player program is in the same directory as the `Judge.jar` file.

Since the I/O of this problem can be quite a lot, the interaction between the Judge and the Player is recorded in the file `log.txt`.  See the PDF for the specifications of the I/O.

The Judge currently is set to ask all words contained in `word_bank.txt` so if you want to test against custom words, that is the file to edit.

If the Judge program is not responsive, manually terminate the process and check that your program's I/O matches the description given in the pdf.  If it is, and the program is still not working (or if anything else doesn't seem to work), please contact me about it so I can fix it.
