for t in range(1,6):
            for first in range(POPULATION_SIZE):
                str1 = convert_genome_to_json(population[first])
                game_result = evaluate_game("CGP", str1, "HCINSTANCE", " ", t)
                game_result = parse_result(game_result)
                fitness_first = fitness_from_result(game_result, "Player1")
                fitness[