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
        </Routes>
      </Container>
    </div>
  );
}

export default App;
