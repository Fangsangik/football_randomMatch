import React, { useState } from 'react';
import styled from 'styled-components';
import { MatchList } from '../components/match/MatchList';
import { CreateMatchForm } from '../components/match/CreateMatchForm';

const Container = styled.div`
  padding: 1rem 0;
`;

const Modal = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  overflow-y: auto;
  padding: 2rem;
`;

const ModalContent = styled.div`
  background: white;
  border-radius: 8px;
  max-width: 800px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
`;

export const MatchPage: React.FC = () => {
  const [showCreateForm, setShowCreateForm] = useState(false);

  const handleCreateMatch = () => {
    setShowCreateForm(true);
  };

  const handleCloseForm = () => {
    setShowCreateForm(false);
  };

  const handleFormSuccess = () => {
    setShowCreateForm(false);
    // Optionally refresh the match list here
  };

  return (
    <Container>
      <MatchList onCreateMatch={handleCreateMatch} />
      
      {showCreateForm && (
        <Modal onClick={handleCloseForm}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <CreateMatchForm 
              onCancel={handleCloseForm}
              onSuccess={handleFormSuccess}
            />
          </ModalContent>
        </Modal>
      )}
    </Container>
  );
};