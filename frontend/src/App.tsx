import React from 'react';
import { Routes, Route } from 'react-router-dom';
import { Container } from '@mui/material';
import Header from './components/Header';
import Home from './components/Home';
import DeckList from './components/DeckList';
import DeckDetail from './components/DeckDetail';
import CreateDeck from './components/CreateDeck';
import CardList from './components/CardList';
import CreateCard from './components/CreateCard';
import SearchByTags from './components/SearchByTags';
import StudyDeckSelection from './components/StudyDeckSelection';
import StudyMode from './components/StudyMode';
import CardGenerationPage from './components/CardGenerationPage';
import GeneratedCardsPage from './components/GeneratedCardsPage';
import ProjectsPage from './components/ProjectsPage';
import ProjectDetail from './components/ProjectDetail';

function App() {
  return (
    <div className="App">
      <Header />
      <Container sx={{ mt: 4, mb: 4 }}>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/decks" element={<DeckList />} />
          <Route path="/decks/:id" element={<DeckDetail />} />
          <Route path="/decks/new" element={<CreateDeck />} />
          <Route path="/cards" element={<CardList />} />
          <Route path="/cards/new" element={<CreateCard />} />
          <Route path="/search" element={<SearchByTags />} />
          <Route path="/study" element={<StudyDeckSelection />} />
          <Route path="/study/:deckId" element={<StudyMode />} />
          {/* Neue Routen für die Karteikartengenerierung */}
          <Route path="/decks/:deckId/generate" element={<CardGenerationPage />} />
          <Route path="/decks/:deckId/generate/:requestId/results" element={<GeneratedCardsPage />} />
          
          {/* OCR und Projekt-Management Routen */}
          <Route path="/projects" element={<ProjectsPage />} />
          <Route path="/projects/:id" element={<ProjectDetail />} />
        </Routes>
      </Container>
    </div>
  );
}

export default App;
