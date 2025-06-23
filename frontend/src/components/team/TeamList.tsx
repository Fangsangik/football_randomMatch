import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { TeamResponseDto } from '../../types/api';
import { TeamCard } from './TeamCard';

const Container = styled.div`
  padding: 2rem;
`;

const Header = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
`;

const Title = styled.h2`
  color: #333;
  margin: 0;
`;

const CreateButton = styled.button`
  padding: 0.75rem 1.5rem;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  
  &:hover {
    background-color: #0056b3;
  }
`;

const TeamGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
`;

const LoadingText = styled.div`
  text-align: center;
  padding: 2rem;
  color: #666;
`;

const EmptyState = styled.div`
  text-align: center;
  padding: 3rem;
  color: #666;
  
  h3 {
    margin-bottom: 1rem;
  }
  
  p {
    margin-bottom: 2rem;
  }
`;

interface TeamListProps {
  onCreateTeam: () => void;
}

export const TeamList: React.FC<TeamListProps> = ({ onCreateTeam }) => {
  const [teams, setTeams] = useState<TeamResponseDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // In a real app, you would fetch teams from API
    // For now, we'll simulate loading
    const timer = setTimeout(() => {
      setTeams([]);
      setLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  const handleTeamUpdate = (updatedTeam: TeamResponseDto) => {
    setTeams(teams.map(team => 
      team.id === updatedTeam.id ? updatedTeam : team
    ));
  };

  const handleTeamDelete = (teamId: number) => {
    setTeams(teams.filter(team => team.id !== teamId));
  };

  if (loading) {
    return <LoadingText>팀 정보를 불러오는 중...</LoadingText>;
  }

  return (
    <Container>
      <Header>
        <Title>팀 목록</Title>
        <CreateButton onClick={onCreateTeam}>
          새 팀 만들기
        </CreateButton>
      </Header>

      {teams.length === 0 ? (
        <EmptyState>
          <h3>등록된 팀이 없습니다</h3>
          <p>첫 번째 팀을 만들어보세요!</p>
          <CreateButton onClick={onCreateTeam}>
            팀 만들기
          </CreateButton>
        </EmptyState>
      ) : (
        <TeamGrid>
          {teams.map((team) => (
            <TeamCard 
              key={team.id} 
              team={team}
              onUpdate={handleTeamUpdate}
              onDelete={handleTeamDelete}
            />
          ))}
        </TeamGrid>
      )}
    </Container>
  );
};