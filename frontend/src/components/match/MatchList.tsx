import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import { MatchResponseDto } from '../../types/api';
import { MatchCard } from './MatchCard';

const Container = styled.div`
  padding: 2rem;
`;

const Header = styled.div`
  display: flex;
  justify-content: between;
  align-items: center;
  margin-bottom: 2rem;
`;

const Title = styled.h2`
  color: #333;
  margin: 0;
`;

const CreateButton = styled.button`
  padding: 0.75rem 1.5rem;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  
  &:hover {
    background-color: #218838;
  }
`;

const MatchGrid = styled.div`
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

interface MatchListProps {
  onCreateMatch: () => void;
}

export const MatchList: React.FC<MatchListProps> = ({ onCreateMatch }) => {
  const [matches, setMatches] = useState<MatchResponseDto[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // In a real app, you would fetch matches from API
    // For now, we'll simulate loading
    const timer = setTimeout(() => {
      setMatches([]);
      setLoading(false);
    }, 1000);

    return () => clearTimeout(timer);
  }, []);

  if (loading) {
    return <LoadingText>매치 정보를 불러오는 중...</LoadingText>;
  }

  return (
    <Container>
      <Header>
        <Title>매치 목록</Title>
        <CreateButton onClick={onCreateMatch}>
          새 매치 만들기
        </CreateButton>
      </Header>

      {matches.length === 0 ? (
        <EmptyState>
          <h3>등록된 매치가 없습니다</h3>
          <p>첫 번째 매치를 만들어보세요!</p>
          <CreateButton onClick={onCreateMatch}>
            매치 만들기
          </CreateButton>
        </EmptyState>
      ) : (
        <MatchGrid>
          {matches.map((match) => (
            <MatchCard key={match.id} match={match} />
          ))}
        </MatchGrid>
      )}
    </Container>
  );
};