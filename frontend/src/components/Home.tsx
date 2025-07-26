import React from 'react';
import { Typography, Box, Button, Grid, Paper } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const Home: React.FC = () => {
  return (
    <Box>
      <Typography variant="h4" component="h1" gutterBottom>
        Willkommen beim Flashcards-System
      </Typography>
      
      <Typography variant="body1" paragraph>
        Mit diesem System können Sie Karteikarten erstellen und in Decks organisieren, um effektiv zu lernen.
      </Typography>
      
      <Grid container spacing={4} sx={{ mt: 2 }}>
        <Grid item xs={12} md={6}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 3, 
              height: '100%', 
              display: 'flex', 
              flexDirection: 'column',
              justifyContent: 'space-between'
            }}
          >
            <div>
              <Typography variant="h5" component="h2" gutterBottom>
                Decks verwalten
              </Typography>
              <Typography variant="body1" paragraph>
                Erstellen und organisieren Sie Ihre Lernmaterialien in thematischen Decks.
              </Typography>
            </div>
            <Button 
              variant="contained" 
              component={RouterLink} 
              to="/decks"
              sx={{ alignSelf: 'flex-start' }}
            >
              Zu den Decks
            </Button>
          </Paper>
        </Grid>
        
        <Grid item xs={12} md={6}>
          <Paper 
            elevation={3} 
            sx={{ 
              p: 3, 
              height: '100%', 
              display: 'flex', 
              flexDirection: 'column',
              justifyContent: 'space-between'
            }}
          >
            <div>
              <Typography variant="h5" component="h2" gutterBottom>
                Karten erstellen
              </Typography>
              <Typography variant="body1" paragraph>
                Erstellen Sie neue Karteikarten und fügen Sie sie Ihren Decks hinzu.
              </Typography>
            </div>
            <Button 
              variant="contained" 
              component={RouterLink} 
              to="/cards/new"
              sx={{ alignSelf: 'flex-start' }}
            >
              Neue Karte erstellen
            </Button>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Home;
