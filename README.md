# Parallel Refactoring Analysis
This is the artifact page of the paper _An Empirical Study of Parallel Refactorings in Merge Conflicts_

## What is available
This repository contains
1. The source code. The repository root is an IntelliJ IDEA project. However, you can use the code without the IDE.
2. Database dump of the result

[This Google Sheet](https://docs.google.com/spreadsheets/d/1TkTBzYAWoLCYug9iGXyLjqXMFZcqz--UHqM5BSVgDYo/edit?usp=sharing) 
has summary of results found by the research. The tables shown in the paper can be found in the spreadsheet. 
There are even more representations of the data in hte spreadsheet which are not available in the paper.

## Abstract
Merge conflicts are problematic and frequent events in distributed software development.
Refactoring is also a frequent and suggested activity of everyday software development.
Refactoring is done to improve the code quality and often causes cross-module changes in the code-base, 
potentially increasing the probability and severity of merge conflict.
This assumption is supported by previous studies. 
In this study, we investigate this further to see whether refactorings
happening in both the branches being merged have any additional effect on conflict. 
We studied about a million merge scenarios and found that refactorings happening in both
branches and modifying the same program element is a rare event. 
Only 1.6% of all merge conflicts have such refactorings involved. 
However, when such refactoring pairs exist,
they almost always lead to a conflicting merge. We found
that Extract Method refactoring happening in both branches
happen most frequently, however, it is theoretically possible
to automatically merge them under some conditions.

## Reproducing the results

### Restore the database
**Prerequisite**: MySQL database version 8 or above

#### Steps
1. Install MySQL database version 8 or above 
1. Download the zip containing the database backup from here: https://github.com/mohayemin/parallel-refactoring-analysis/blob/master/datadump/parallel_refactoring_analysis.zip
1. Unzip the file, you will find a file `parallel-refactoring-analysis.sql`
1. Restore the database
   1. Open a command line terminal
   1. Move to the directory containing `parallel-refactoring-analysis.sql` file
   1. Login to MySQL: `mysql -u <your username> -p`
   1. Give your MySQL password if it is asked
   1. If you previously created the same database and want to discard that, run `DROP DATABASE IF EXISTS parallel-refactoring-analysis;`
   1. Create an empty database: `CREATE DATABASE parallel-refactoring-analysis;`
   1. Move to the database: `USE parallel-refactoring-analysis;`
   1. Restore: `source parallel-refactoring-analysis.sql;`

The database is now ready. You can play around with some queries to see if the results match with the ones in the paper.
Some analysis scripts are available in the `src/analysis-scripts` folder.

### Rerunning the analysis
#### Prerequisites
1. Git
2. JDK version 14+
3. IntelliJ IDEA version 2020.2 or later (optional)

#### Steps
1. Get the source code
    * Option 1: Using git- Open a command line terminal, move to a directory where you want to keep the code, run this command: `git clone https://github.com/mohayemin/parallel-refactoring-analysis`
    * Option 2: Direct download: https://github.com/mohayemin/parallel-refactoring-analysis/archive/master.zip. Remember that the program itself uses git commands. 
    Therefore, you will need to install git anyway.
2. Running the code
    * Option 1: using IntelliJ IDEA. The repository is an IntelliJ IDEA project. Therefore, easies way to run it is by opening the root folder in IntelliJ.
    * You can manually compile the project with `javac` command, and run using `java` command.
3. Setting up parameters: Unfortunately, the program does not support any command line argument or configuration file. 
You need to change the code to set different parameters. All the parameters can be set in the `src/main/java/Application.java` file. 
See the JavaDoc of `src/main/java/analyzer/AnalysisOptions.java` for details.
       
