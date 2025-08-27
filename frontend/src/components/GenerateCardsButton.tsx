import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, Tooltip } from '@mui/material';
import AutoAwesomeIcon from '@mui/icons-material/AutoAwesome';

interface GenerateCardsButtonProps {
  deckId: string;
}

/**
 * Button-Komponente, die zur Karteikartengenerierungsseite führt.
 * Wird in der DeckDetail-Komponente angezeigt.
 */
const GenerateCardsButton: React.FC<GenerateCardsButtonProps> = ({ deckId }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/decks/${deckId}/generate`);
  };

  return (
    <Tooltip title="KI-gestützte Karteikarten generieren">
      <Button
        variant="contained"
        color="secondary"
        onClick={handleClick}
        startIcon={<AutoAwesomeIcon />}
      >
        KI-Karten generieren
      </Button>
    </Tooltip>
  );
};

export default GenerateCardsButton;