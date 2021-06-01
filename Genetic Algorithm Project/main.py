from genetic import Genetic

# Read levels from file
levels = []
for i in range(1, 11):
    level = open("./levels/level" + str(i) + ".txt", 'r').readline()
    levels.append(level)

# Genetic Algorithm with method 1
g_method1 = Genetic(levels[7], 1, 200)
g_method1.initial_population()
g_method1.generate_n_generations(40)
g_method1.show_results()

# Genetic Algorithm with method 2
g_method2 = Genetic(levels[7], 2, 500)
g_method2.initial_population()
g_method2.generate_n_generations(40)
g_method2.show_results()


