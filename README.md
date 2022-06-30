This project takes the "traveling salesman problem" and approaches it in two ways: heuristically and via backtracking. Below are different runtimes(may vary by machine)
that highlight the times in a comparable fashion between the two approaches and my
own. More details are below the results. This was done for CSC210 - Software Development

"big11.mtx" - Results

======================================================
Run 1

heuristic: cost = 3.3969307170000005. 1 milliseconds

mine: cost = 1.3566775349999998. 20 milliseconds

backtrack: cost = 1.3566775349999998. 28 milliseconds

======================================================


Run 2
heuristic: cost = 3.3969307170000005. 2 milliseconds
mine: cost = 1.3566775349999998. 20 milliseconds
backtrack: cost = 1.3566775349999998. 28 milliseconds


Run 3
heuristic: cost = 3.3969307170000005. 2 milliseconds
mine: cost = 1.3566775349999998. 21 milliseconds
backtrack: cost = 1.3566775349999998. 28 milliseconds


Run 4
heuristic: cost = 3.3969307170000005. 1 milliseconds
mine: cost = 1.3566775349999998. 20 milliseconds
backtrack: cost = 1.3566775349999998. 31 milliseconds


Run 5
heuristic: cost = 3.3969307170000005. 2 milliseconds
mine: cost = 1.3566775349999998. 21 milliseconds
backtrack: cost = 1.3566775349999998. 28 milliseconds




The performance differences lie in the implementations of each algorithm as we discussed in class.

The heuristic approach, being greedy, does optimized choices at a local level. That is it for each
step it looks at the most "optimal" route. Meaning it is fast, but overall can miss a more optimal 
route in the large scale and even entire routes if there are no neighbors for particular nodes.

The backtracking approach is very thourough by nature, but because it is thourough it is slow to reach
it's complete route. That being said it tends to be much more optimized than the heuristic approach
as seen in the results. 

My approach is based on the backtracking approach. I still use backtracking to find the most optimal
route but I prune off locally irrelevant nodes. Much like the heuristic approach but I take a look
if the next couple of adjacent nodes are better fits. If it finds they are, it skips this current
node for later. 
