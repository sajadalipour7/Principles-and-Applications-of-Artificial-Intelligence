import numpy as np
import matplotlib.pyplot as plt
import random


class Genetic:
    def __init__(self, level, ravesh, population):
        self.level = level
        self.population = population
        self.ravesh = ravesh
        self.chromosomes = []
        self.best_per_gen = []
        self.worst_per_gen = []
        self.average_per_gen = []
        self.is_first_winner_found=False

    def initial_population(self):
        """
        Generates first population
        """
        size = len(self.level)
        for i in range(0, self.population):
            chromosome = ""
            for j in range(0, size):
                chromosome += str(random.randint(0, 2))
            self.chromosomes.append(self.validate_chromosome(chromosome))
        self.add_to_generation_statistics()
        self.announce_first_winner(0)

    def validate_chromosome(self, chromosome):
        """
        removes invalid chromosomes
        """
        new_chromosome = list(chromosome)
        for i in range(0, len(chromosome) - 1):
            if new_chromosome[i] == "1" and new_chromosome[i + 1] != "0":
                new_chromosome[i + 1] = "0"
        result = ''.join(new_chromosome)
        return result

    def add_to_generation_statistics(self):
        """
        saves generations details
        """
        maximum = self.fitness_function(self.chromosomes[0])
        minimum = self.fitness_function(self.chromosomes[0])
        average = 0
        for i in range(0, self.population):
            tmp = self.fitness_function(self.chromosomes[i])
            if tmp > maximum:
                maximum = tmp
            if tmp < minimum:
                minimum = tmp
            average += tmp
        average /= self.population
        self.best_per_gen.append(maximum)
        self.worst_per_gen.append(minimum)
        self.average_per_gen.append(average)

    def check_win_or_loss(self, chromosome):
        """
        checks if a chromosome is winner or not
        """
        level = self.level
        # detect winner or loser
        for i in range(0, len(chromosome) - 1):
            if (chromosome[i] == "0" or chromosome[i] == "2" ) and level[i + 1] == "G":
                return False
            if (chromosome[i] == "0" or chromosome[i] == "1") and level[i + 1] == "L":
                return False
        for i in range(0, len(chromosome) - 2):
            if chromosome[i] == "1" and level[i + 2] == "L":
                return False
        return True

    def fitness_function(self, chromosome):
        """
        calculates fitness function for a chromosome
        """
        level = self.level
        score = 0
        if self.check_win_or_loss(chromosome) and self.ravesh == 1:
            score += 5
        # gharchs
        for i in range(0, len(chromosome) - 1):
            if chromosome[i] == "0" and level[i + 1] == "M":
                score += 2
        # jump in last place
        if chromosome[len(chromosome) - 1] == "1":
            score += 1
        # Kill gompas
        for i in range(0, len(chromosome) - 2):
            if chromosome[i] == "1" and level[i + 2] == "G":
                score += 2

        # Maximum perfection
        score += self.maximum_perfection(chromosome)

        return score

    def maximum_perfection(self, chromosome):
        """
        Finds longest winning path in a chromosome
        """
        level=self.level
        s=[]
        tmp=0
        for i in range(0,len(chromosome)-2):
            if (chromosome[i] == "0" or chromosome[i] == "2" ) and level[i + 1] == "G":
                s.append(tmp)
                tmp=0
            elif (chromosome[i] == "0" or chromosome[i] == "1") and level[i + 1] == "L":
                s.append(tmp)
                tmp=0
            elif chromosome[i] == "1" and level[i + 2] == "L":
                s.append(tmp)
                tmp=0
            else:
                tmp+=1
        s.append(tmp)
        return max(s)

    def choose_the_best(self):
        """
        Chooses half of population for being parents
        """
        if self.ravesh == 1:
            # choose the best
            best_chromosomes = []
            for i in range(0, self.population):
                best_chromosomes.append((self.chromosomes[i], self.fitness_function(self.chromosomes[i])))
            best_chromosomes.sort(key=lambda x: x[1])
            best_chromosomes = best_chromosomes[self.population // 2:]
            result = []
            for i in range(0, self.population // 2):
                result.append(best_chromosomes[i][0])
            return result
        else:
            # choose with random weight
            sum_of_scores = 0.0
            for i in range(0, self.population):
                sum_of_scores += self.fitness_function(self.chromosomes[i])
            probability_distribution = []
            for i in range(0, self.population):
                probability_distribution.append((1.0 * self.fitness_function(self.chromosomes[i])) / sum_of_scores)
            result = np.random.choice(self.chromosomes, self.population // 2, p=probability_distribution)
            return result

    def cross_over(self, chromosome1, chromosome2):
        """
        Cross-over function
        """
        if self.ravesh == 1:
            # One-point cross-over
            child1 = ""
            child2 = ""
            point = random.randint(1, len(chromosome1) - 1)
            child1 = chromosome1[:point] + chromosome2[point:]
            child2 = chromosome2[:point] + chromosome2[point:]
            return (child1, child2)
        else:
            # Two-point cross-over
            child1 = ""
            child2 = ""
            first_point = random.randint(1, len(chromosome1) // 2)
            second_point = random.randint((len(chromosome1) // 2) + 1, len(chromosome1) - 1)
            child1 = chromosome1[:first_point] + chromosome2[first_point:second_point] + chromosome1[second_point:]
            child2 = chromosome2[:first_point] + chromosome1[first_point:second_point] + chromosome2[second_point:]
            return (child1, child2)

    def mutate(self, chromosome):
        """
        Mutation Function
        """
        place_to_mutate = random.randint(0, len(chromosome) - 1)
        new_chromosome = ""
        if chromosome[len(chromosome)-1]!="1":
            new_chromosome=chromosome[:len(chromosome)-1]+"1"
            return new_chromosome
        for i in range(0,len(chromosome)):
            if place_to_mutate==i:
                new_chromosome+="0"
            else:
                new_chromosome+=chromosome[i]
        return new_chromosome

    def announce_first_winner(self,generation):
        """
        Announces first winner between generations
        """
        if not self.is_first_winner_found:
            for i in range(0,self.population):
                if self.check_win_or_loss(self.chromosomes[i]):
                    self.is_first_winner_found=True
                    print("First winner found at generation "+str(generation)+" : "+self.chromosomes[i])
                    break

    def generation_creator(self):
        """
        Generates next generation
        """
        new_generation = []
        parents = self.choose_the_best()
        for i in range(0, self.population // 2):
            new_childs = self.cross_over(parents[random.randint(0, (self.population // 2) - 1)],
                                         parents[random.randint(0, (self.population // 2) - 1)])
            new_generation.append(new_childs[0])
            new_generation.append(new_childs[1])
        probability_for_mutation = 0
        if self.ravesh == 1:
            probability_for_mutation = 1
        else:
            probability_for_mutation = 5
        for i in range(0, self.population):
            tmp = random.randint(1, 10)
            if tmp <= probability_for_mutation:
                new_generation[i] = self.mutate(new_generation[i])
            new_generation[i] = self.validate_chromosome(new_generation[i])
        self.chromosomes = new_generation
        self.add_to_generation_statistics()

    def generate_n_generations(self, n):
        """
        Generates n generation
        """
        for i in range(0, n):
            self.generation_creator()
            self.announce_first_winner(i+1)

    def show_results(self):
        """
        shows statistical results
        """
        num = len(self.average_per_gen)
        generation = []
        for i in range(0, num):
            generation.append(i)

        plt.plot(generation, self.average_per_gen)
        plt.title("Average with method " + str(self.ravesh))
        plt.xlabel("Generation (" + str(self.population) + " chromosomes per generation)")
        plt.ylabel("Fitness")
        plt.show()

        plt.plot(generation, self.best_per_gen)
        plt.title("Best with method " + str(self.ravesh))
        plt.xlabel("Generation (" + str(self.population) + " chromosomes per generation)")
        plt.ylabel("Fitness")
        plt.show()

        plt.plot(generation, self.worst_per_gen)
        plt.title("Worst with method " + str(self.ravesh))
        plt.xlabel("Generation (" + str(self.population) + " chromosomes per generation)")
        plt.ylabel("Fitness")
        plt.show()
