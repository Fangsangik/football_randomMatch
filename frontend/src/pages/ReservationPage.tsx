import React from 'react';
import styled from 'styled-components';

const Container = styled.div`
  padding: 2rem;
  text-align: center;
`;

const Title = styled.h1`
  color: #333;
  margin-bottom: 2rem;
`;

const Description = styled.p`
  color: #666;
  font-size: 1.1rem;
`;

export const ReservationPage: React.FC = () => {
  return (
    <Container>
      <Title>🏟️ 경기장 예약</Title>
      <Description>
        경기장 예약 기능은 곧 출시될 예정입니다.
      </Description>
    </Container>
  );
};