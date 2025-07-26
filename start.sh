#!/bin/bash

# Start the backend
echo "Starting the Flashcards backend..."
./gradlew bootRun &

# Wait for the backend to start
echo "Waiting for backend to start..."
sleep 10

# Navigate to the frontend directory and start the React app
echo "Starting the Flashcards frontend..."
cd frontend
npm start
