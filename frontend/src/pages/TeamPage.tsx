import React, { useState } from 'react';
import styled from 'styled-components';
import { TeamList } from '../components/team/TeamList';
import { CreateTeamForm } from '../components/team/CreateTeamForm';

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
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
`;

export const TeamPage: React.FC = () => {
  const [showCreateForm, setShowCreateForm] = useState(false);

  const handleCreateTeam = () => {
    setShowCreateForm(true);
  };

  const handleCloseForm = () => {
    setShowCreateForm(false);
  };

  const handleFormSuccess = () => {
    setShowCreateForm(false);
    // Optionally refresh the team list here
  };

  return (
    <Container>
      <TeamList onCreateTeam={handleCreateTeam} />
      
      {showCreateForm && (
        <Modal onClick={handleCloseForm}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <CreateTeamForm 
              onCancel={handleCloseForm}
              onSuccess={handleFormSuccess}
            />
          </ModalContent>
        </Modal>
      )}
    </Container>
  );
};