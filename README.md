# GAME
expected args when starting:
1. --LevelChoice >> int from 1 to 5, decides the level on which the game will be played (1-easy,5-hard)
2. --Player1 >> Absolute path to Player1 brain (txt or ptf file preferably). First line should always be in all Caps type of brain that we want to use for game (exp. NN,GCP,GP,HARDCODED MISKO). If argument isn't used, control of player is given to user
3. --Player2 >> Absolute path to Player2 brain (txt or ptf file preferably). First line should always be in all Caps type of brain that we want to use for game (exp. NN,GCP,GP,HARDCODED MISKO). If argument isn't used, control of player is given to user
4. --TimeLimit >> determines time in second (integer) that has to pass for a game to be a draw

exp.
--LevelChoice 4 --Player1 C:\Users\mpristav\IdeaProjects\tank-game\src\main\resources\HardcodePlayerMisko --TimeLimit 100 --Player2 C:\Users\mpristav\IdeaProjects\tank-game\src\main\resources\HardcodePlayerMisko 

# GAME NO Visuals
expected args when starting:
1. --LevelChoice >> int from 1 to 5, decides the level on which the game will be played (1-easy,5-hard)
2. --Player1 >> Absolute path to Player1 brain (txt or ptf file preferably). First line should always be in all Caps type of brain that we want to use for game (exp. NN,GCP,GP,HARDCODED MISKO). Argument is mandatory
3. --Player2 >> Absolute path to Player2 brain (txt or ptf file preferably). First line should always be in all Caps type of brain that we want to use for game (exp. NN,GCP,GP,HARDCODED MISKO). Argument is mandatory
4. --TimeLimit >> determines number of decisions (integer) that has to happen for a game to be a draw
5. --ResultFile >> absolute path to file in which we want the result and statistics of game to be saved after the game finishes

exp.
--LevelChoice 4 --Player1 C:\Users\mpristav\IdeaProjects\tank-game\src\main\resources\HardcodePlayerMisko --TimeLimit 100 --Player2 C:\Users\mpristav\IdeaProjects\tank-game\src\main\resources\HardcodePlayerMisko --
