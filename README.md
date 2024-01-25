# GAME with UI
1. Locate runGame.jar in root directory of project
2. Start the game with java -jar runGame.jar (in case java is not setup in PATH, you can use <ApsolutePathToJava.exe> -jar runGame.jar)
3. UI appears 
4. Enter the level that you want to play (1-5)
5. Drag and drop ai players txt file,or hardcoded bot txt files in player1 and player2 positions
6. If you want to play, you should drag and drop player.txt
7. Controls for manual play are wasd+q and ^<v>+enter
8. All player files can be found in generated_jedinke folder

9. To generate a new runable version of game(exp. with changed game settings), you can use gradle build command. Your runGame.jar file will be in build/libs folder inside root directory 


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


# How to use GameInstance x python

First write a program in python, view Cartesian_GP/exampleForCGP for usage example.

Firstly, launch python, wait for Server is listening...
Then, start GameInstance in evaluation/GameInstance from Java.
Now java accepts any further game requests and evaluates given games.

For more complex usage check Cartesian_GP/troturnirski...

