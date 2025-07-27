@echo off
echo Starting the Flashcards backend...
start cmd /k "gradlew bootRun"

echo Waiting for backend to start...
timeout /t 10 /nobreak

echo Starting the Flashcards frontend...
cd frontend
npm start
