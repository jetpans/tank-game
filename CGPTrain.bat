@echo off

rem Command to run CGPMainTraining.py
set CGP_SCRIPT=python\Cartesian_GP\CGPMainTraining.py

rem Command to run GameInstance.java
set GAME_SCRIPT=java -cp build/classes/java/main Evaluation.GameInstance

rem Function to run a command in a new command prompt window

rem Run CGPMainTraining.py in a new command prompt window
start cmd.exe /k "%CGP_SCRIPT%"

rem Wait for CGPMainTraining.py to finish (you may need to adjust the sleep time)
timeout /t 5

rem Run GameInstance.java in a new command prompt window
start cmd.exe /k "%GAME_SCRIPT%"