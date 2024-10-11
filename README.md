<div align="center">

# Episode 3: Evolving through Mutations

</div>

In episode 3, we tackle the problem of training... How do we train a
neural network to solve problems without explicitly giving it the correct
answers?

We look again at the XOR function:

| Input 1 | Input 2 | Output |
|---------|---------|--------|
| 0       | 0       | 0      |
| 0       | 1       | 1      |
| 1       | 0       | 1      |
| 1       | 1       | 0      |

We have 100s of fully connected neural networks with 2 inputs, and 1 output.
We can then randomly mutate the networks in these ways:
1. Add new neurons
2. Add new connection
3. Change weights
4. Change biases

We can then follow the following steps:
1. Evaluate the networks
2. Save the best networks (say, top 25% of the population)
3. Kill off the rest of the networks (bottom 75%)
4. Clone the best networks to refill the population
5. Mutate the cloned networks
6. Repeat

In theory, this should be able to solve any problem? We'll see... Either way,
this was a good enough genetic algorithm to solve XOR! Join us next time for
optimizations.
