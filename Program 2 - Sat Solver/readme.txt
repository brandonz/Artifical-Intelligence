/**********************************************************************************/
// Edited By: Brandon Zhou
// netID: brandonz
// Course: COS 402
/**********************************************************************************/

MySatSolver.java

The SAT Solver is an extension of the WalkSat algorithm. It generates a random 
starting model. Until the timer runs out or a satisfiable model is found, select 
a random unsatisfiable clause. With some probability p, a random symbol in the 
clause is flipped, otherwise iterative through each symbol in the selected clause. 
For each symbol flip it and find the number of clauses that become satisfiable and 
unsatisfiable as a result, the difference is the heuristic to use. Flip the symbol 
which has the highest heuristic. In the event of a tie, choose the symbol which 
broke less clauses. In the case where nothing broke flip the symbol, since it is 
strictly better to do so. The index of unsatisfied clauses is stored in an 
ArrayList, while the number of broken/made clauses for each symbol is stored 
in an object called a Pair.

MyGenerator.java

The SAT Generator is based on the Dinner Party Planning Algorithm. The code 
randomly generates a table size (even int between the MIN-MAX number of seats). 
It adds the first two sets of clauses; the first is that each node seats one 
person. The second set limits each seat to one person.
Having the initial boundaries of the problem in place, we need to add constraints 
for pairs that must sit together and pairs that cannot sit together. To ensure 
that a random generation of pairs creates a satisfiable problem, we first 
generate a satisfiable solution (randomly set the table and choose pairs such that 
the table is still a satisfiable model). It does this by doing a random assignment 
to the table, and choosing a random number of complementary/opposing pairs that is 
between 0 and the max number of such pairs in the table. The program then selects 
these pairs systematically from the table (unique pairs are selected for 
complementary/opposing pairs first). Once these pairs are selected, add them to the 
CNF and return it.