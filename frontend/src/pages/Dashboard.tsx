import React from 'react';
import styled from 'styled-components';
import { useAuth } from '../contexts/AuthContext';
import { RandomMatchButton } from '../components/match/RandomMatchButton';

const Container = styled.div`
  padding: 2rem 0;
`;

const WelcomeSection = styled.div`
  background: linear-gradient(135deg, #007bff, #28a745);
  color: white;
  padding: 3rem 2rem;
  border-radius: 12px;
  text-align: center;
  margin-bottom: 3rem;
`;

const WelcomeTitle = styled.h1`
  font-size: 2.5rem;
  margin-bottom: 1rem;
  font-weight: 700;
`;

const WelcomeSubtitle = styled.p`
  font-size: 1.2rem;
  opacity: 0.9;
  margin: 0;
`;

const StatsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 3rem;
`;

const StatCard = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.2s;
  
  &:hover {
    transform: translateY(-2px);
  }
`;

const StatIcon = styled.div`
  font-size: 3rem;
  margin-bottom: 1rem;
`;

const StatValue = styled.div`
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin-bottom: 0.5rem;
`;

const StatLabel = styled.div`
  color: #666;
  font-size: 1rem;
`;

const QuickActionsSection = styled.div`
  margin-bottom: 3rem;
`;

const SectionTitle = styled.h2`
  color: #333;
  margin-bottom: 2rem;
  text-align: center;
`;

const QuickActionsGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
`;

const QuickActionCard = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.2s, box-shadow 0.2s;
  
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  }
`;

const ActionIcon = styled.div`
  font-size: 4rem;
  margin-bottom: 1rem;
`;

const ActionTitle = styled.h3`
  color: #333;
  margin-bottom: 1rem;
  font-size: 1.5rem;
`;

const ActionDescription = styled.p`
  color: #666;
  margin-bottom: 2rem;
  line-height: 1.6;
`;

const ActionButton = styled.button`
  background: linear-gradient(45deg, #007bff, #0056b3);
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 6px;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s;
  
  &:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(0, 123, 255, 0.3);
  }
`;

const RecentActivitySection = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
`;

const ActivityList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 1rem;
`;

const ActivityItem = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
`;

const ActivityIcon = styled.div`
  font-size: 1.5rem;
  width: 40px;
  text-align: center;
`;

const ActivityContent = styled.div`
  flex: 1;
`;

const ActivityTitle = styled.div`
  font-weight: 500;
  color: #333;
  margin-bottom: 0.25rem;
`;

const ActivityTime = styled.div`
  font-size: 0.875rem;
  color: #666;
`;

export const Dashboard: React.FC = () => {
  const { user } = useAuth();

  const handleCreateMatch = () => {
    // Navigate to match creation
    window.location.href = '/matches';
  };

  const handleCreateTeam = () => {
    // Navigate to team creation
    window.location.href = '/teams';
  };

  const handleMakeReservation = () => {
    // Navigate to reservation
    window.location.href = '/reservations';
  };

  return (
    <Container>
      <WelcomeSection>
        <WelcomeTitle>⚽ 축구 관리 플랫폼에 오신 것을 환영합니다!</WelcomeTitle>
        <WelcomeSubtitle>
          {user?.name || '사용자'}님, 오늘도 멋진 경기를 만들어보세요!
        </WelcomeSubtitle>
      </WelcomeSection>

      <StatsGrid>
        <StatCard>
          <StatIcon>🏆</StatIcon>
          <StatValue>12</StatValue>
          <StatLabel>참가한 매치</StatLabel>
        </StatCard>
        
        <StatCard>
          <StatIcon>👥</StatIcon>
          <StatValue>3</StatValue>
          <StatLabel>소속 팀</StatLabel>
        </StatCard>
        
        <StatCard>
          <StatIcon>📅</StatIcon>
          <StatValue>5</StatValue>
          <StatLabel>예약 내역</StatLabel>
        </StatCard>
        
        <StatCard>
          <StatIcon>⭐</StatIcon>
          <StatValue>4.2</StatValue>
          <StatLabel>평균 평점</StatLabel>
        </StatCard>
      </StatsGrid>

      <RandomMatchButton />

      <QuickActionsSection>
        <SectionTitle>빠른 작업</SectionTitle>
        <QuickActionsGrid>
          <QuickActionCard>
            <ActionIcon>⚽</ActionIcon>
            <ActionTitle>매치 만들기</ActionTitle>
            <ActionDescription>
              새로운 축구 경기를 생성하고 참가자들을 모집해보세요.
            </ActionDescription>
            <ActionButton onClick={handleCreateMatch}>
              매치 생성
            </ActionButton>
          </QuickActionCard>

          <QuickActionCard>
            <ActionIcon>👥</ActionIcon>
            <ActionTitle>팀 구성</ActionTitle>
            <ActionDescription>
              새로운 팀을 만들거나 기존 팀에 가입해보세요.
            </ActionDescription>
            <ActionButton onClick={handleCreateTeam}>
              팀 관리
            </ActionButton>
          </QuickActionCard>

          <QuickActionCard>
            <ActionIcon>🏟️</ActionIcon>
            <ActionTitle>경기장 예약</ActionTitle>
            <ActionDescription>
              원하는 경기장을 찾아 예약하고 경기를 준비하세요.
            </ActionDescription>
            <ActionButton onClick={handleMakeReservation}>
              예약하기
            </ActionButton>
          </QuickActionCard>
        </QuickActionsGrid>
      </QuickActionsSection>

      <RecentActivitySection>
        <SectionTitle>최근 활동</SectionTitle>
        <ActivityList>
          <ActivityItem>
            <ActivityIcon>⚽</ActivityIcon>
            <ActivityContent>
              <ActivityTitle>주말 축구 매치에 참가했습니다</ActivityTitle>
              <ActivityTime>2시간 전</ActivityTime>
            </ActivityContent>
          </ActivityItem>
          
          <ActivityItem>
            <ActivityIcon>👥</ActivityIcon>
            <ActivityContent>
              <ActivityTitle>FC United 팀에 가입했습니다</ActivityTitle>
              <ActivityTime>1일 전</ActivityTime>
            </ActivityContent>
          </ActivityItem>
          
          <ActivityItem>
            <ActivityIcon>🏟️</ActivityIcon>
            <ActivityContent>
              <ActivityTitle>강남 축구장을 예약했습니다</ActivityTitle>
              <ActivityTime>2일 전</ActivityTime>
            </ActivityContent>
          </ActivityItem>
          
          <ActivityItem>
            <ActivityIcon>⭐</ActivityIcon>
            <ActivityContent>
              <ActivityTitle>매치 평가를 완료했습니다</ActivityTitle>
              <ActivityTime>3일 전</ActivityTime>
            </ActivityContent>
          </ActivityItem>
        </ActivityList>
      </RecentActivitySection>
    </Container>
  );
};