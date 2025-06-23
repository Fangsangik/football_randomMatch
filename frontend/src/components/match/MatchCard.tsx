import React from 'react';
import styled from 'styled-components';
import { format } from 'date-fns';
import { MatchResponseDto } from '../../types/api';

const Card = styled.div`
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  transition: transform 0.2s, box-shadow 0.2s;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
`;

const MatchName = styled.h3`
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.25rem;
`;

const InfoRow = styled.div`
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
`;

const Label = styled.span`
  color: #666;
  font-weight: 500;
`;

const Value = styled.span`
  color: #333;
`;

const Date = styled.div`
  color: #007bff;
  font-weight: 500;
  margin-bottom: 1rem;
`;

const PlayerCount = styled.div`
  background: #f8f9fa;
  padding: 0.5rem;
  border-radius: 4px;
  text-align: center;
  margin-top: 1rem;
  font-size: 0.9rem;
  color: #495057;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
`;

const Button = styled.button<{ variant?: 'primary' | 'secondary' | 'danger' }>`
  flex: 1;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: background-color 0.2s;
  
  ${props => {
    switch (props.variant) {
      case 'primary':
        return `
          background-color: #007bff;
          color: white;
          &:hover { background-color: #0056b3; }
        `;
      case 'danger':
        return `
          background-color: #dc3545;
          color: white;
          &:hover { background-color: #c82333; }
        `;
      default:
        return `
          background-color: #6c757d;
          color: white;
          &:hover { background-color: #545b62; }
        `;
    }
  }}
`;

interface MatchCardProps {
  match: MatchResponseDto;
}

export const MatchCard: React.FC<MatchCardProps> = ({ match }) => {
  const formattedDate = format(new Date(match.matchDate), 'yyyy년 MM월 dd일 HH:mm');

  const handleJoinMatch = () => {
    // TODO: Implement join match logic
    console.log('Joining match:', match.id);
  };

  const handleViewDetails = () => {
    // TODO: Implement view details logic
    console.log('Viewing match details:', match.id);
  };

  const handleCompleteMatch = () => {
    // TODO: Implement complete match logic
    console.log('Completing match:', match.id);
  };

  return (
    <Card>
      <MatchName>{match.matchName}</MatchName>
      
      <Date>{formattedDate}</Date>
      
      <InfoRow>
        <Label>예약 ID:</Label>
        <Value>{match.reservationId}</Value>
      </InfoRow>
      
      <InfoRow>
        <Label>경기장 ID:</Label>
        <Value>{match.stadiumId}</Value>
      </InfoRow>
      
      <PlayerCount>
        참가자: {match.matchUsers.length}명
      </PlayerCount>
      
      <ActionButtons>
        <Button variant="primary" onClick={handleJoinMatch}>
          참가하기
        </Button>
        <Button variant="secondary" onClick={handleViewDetails}>
          상세보기
        </Button>
        <Button variant="danger" onClick={handleCompleteMatch}>
          경기완료
        </Button>
      </ActionButtons>
    </Card>
  );
};