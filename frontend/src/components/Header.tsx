import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const Header: React.FC = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          Flashcards
        </Typography>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button color="inherit" component={RouterLink} to="/">
            Home
          </Button>
          <Button color="inherit" component={RouterLink} to="/decks">
            Decks
          </Button>
          <Button color="inherit" component={RouterLink} to="/cards">
            Karten
          </Button>
          <Button color="inherit" component={RouterLink} to="/search">
            Suchen
          </Button>
          <Button color="inherit" component={RouterLink} to="/study">
            Lernen
          </Button>
        </Box>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
