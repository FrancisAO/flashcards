import React, { useState } from 'react';
import {
  Card,
  CardContent,
  CardActions,
  Typography,
  Box,
  IconButton,
  Chip,
  Divider,
  Tooltip,
  Collapse
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import ExpandLessIcon from '@mui/icons-material/ExpandLess';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import { AIGeneratedCard } from '../types/models';

interface GeneratedCardItemProps {
  card: AIGeneratedCard;
  onEdit: (card: AIGeneratedCard) => void;
}

/**
 * Komponente zur Anzeige einer generierten Karteikarte.
 * Zeigt die Vorder- und Rückseite der Karteikarte an und bietet die Möglichkeit, sie zu bearbeiten.
 */
const GeneratedCardItem: React.FC<GeneratedCardItemProps> = ({ card, onEdit }) => {
  const [expanded, setExpanded] = useState<boolean>(false);

  const handleExpandClick = () => {
    setExpanded(!expanded);
  };

  const handleEditClick = () => {
    onEdit(card);
  };

  return (
    <Card 
      sx={{ 
        height: expanded ? 'auto' : '100%',
        display: 'flex',
        flexDirection: 'column',
        position: 'relative',
        transition: 'all 0.3s ease',
        '&:hover': {
          boxShadow: 6
        }
      }}
    >
      {card.edited && (
        <Tooltip title="Bearbeitet">
          <Chip
            icon={<CheckCircleIcon />}
            label="Bearbeitet"
            size="small"
            color="primary"
            sx={{
              position: 'absolute',
              top: 8,
              right: 8,
              zIndex: 1
            }}
          />
        </Tooltip>
      )}

      <CardContent sx={{ flexGrow: 1, pt: card.edited ? 4 : 2 }}>
        <Typography variant="h6" component="h3" gutterBottom>
          {card.front}
        </Typography>
        
        <Divider sx={{ my: 2 }} />
        
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
          <Typography variant="subtitle1" color="text.secondary">
            Rückseite
          </Typography>
          <IconButton
            onClick={handleExpandClick}
            aria-expanded={expanded}
            aria-label="Rückseite anzeigen"
            size="small"
          >
            {expanded ? <ExpandLessIcon /> : <ExpandMoreIcon />}
          </IconButton>
        </Box>
        
        <Collapse in={expanded} timeout="auto" unmountOnExit>
          <Box sx={{ mt: 2 }}>
            <Typography variant="body1">
              {card.back}
            </Typography>
          </Box>
        </Collapse>
      </CardContent>
      
      <CardActions sx={{ justifyContent: 'flex-end', p: 2, pt: 0 }}>
        <Tooltip title="Karteikarte bearbeiten">
          <IconButton 
            onClick={handleEditClick}
            aria-label="Bearbeiten"
            color="primary"
          >
            <EditIcon />
          </IconButton>
        </Tooltip>
      </CardActions>
    </Card>
  );
};

export default GeneratedCardItem;