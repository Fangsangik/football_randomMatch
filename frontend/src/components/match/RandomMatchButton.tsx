import React, { useState } from 'react';
import { toast } from 'react-toastify';
import styled from 'styled-components';
import { matchService } from '../../services/api';

const Button = styled.button<{ isLoading: boolean }>`
  padding: 1rem 2rem;
  background: linear-gradient(45deg, #28a745, #20c997);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);
  position: relative;
  overflow: hidden;
  
  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(40, 167, 69, 0.4);
  }
  
  &:active {
    transform: translateY(0);
  }
  
  &:disabled {
    background: #6c757d;
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
  }
  
  ${props => props.isLoading && `
    &::after {
      content: '';
      position: absolute;
      top: 50%;
      left: 50%;
      width: 20px;
      height: 20px;
      margin: -10px 0 0 -10px;
      border: 2px solid transparent;
      border-top: 2px solid white;
      border-radius: 50%;
      animation: spin 1s linear infinite;
    }
    
    @keyframes spin {
      0% { transform: rotate(0deg); }
      100% { transform: rotate(360deg); }
    }
  `}
`;

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin: 2rem 0;
`;

const Title = styled.h3`
  color: #333;
  margin: 0;
  text-align: center;
`;

const Description = styled.p`
  color: #666;
  text-align: center;
  margin: 0;
  max-width: 300px;
  line-height: 1.5;
`;

const StatusText = styled.div<{ status: 'waiting' | 'success' | 'error' }>`
  padding: 0.5rem 1rem;
  border-radius: 4px;
  font-size: 0.9rem;
  font-weight: 500;
  
  ${props => {
    switch (props.status) {
      case 'waiting':
        return `
          background: #fff3cd;
          color: #856404;
          border: 1px solid #ffeaa7;
        `;
      case 'success':
        return `
          background: #d4edda;
          color: #155724;
          border: 1px solid #c3e6cb;
        `;
      case 'error':
        return `
          background: #f8d7da;
          color: #721c24;
          border: 1px solid #f5c6cb;
        `;
      default:
        return '';
    }
  }}
`;

export const RandomMatchButton: React.FC = () => {
  const [isLoading, setIsLoading] = useState(false);
  const [status, setStatus] = useState<'idle' | 'waiting' | 'success' | 'error'>('idle');

  const handleJoinRandomMatch = async () => {
    setIsLoading(true);
    setStatus('waiting');
    
    try {
      await matchService.joinMatch();
      setStatus('success');
      toast.success('랜덤 매치 대기열에 참가했습니다!');
      
      // Reset status after 3 seconds
      setTimeout(() => {
        setStatus('idle');
      }, 3000);
    } catch (error: any) {
      setStatus('error');
      toast.error(error.response?.data?.message || '랜덤 매치 참가에 실패했습니다.');
      
      // Reset status after 3 seconds
      setTimeout(() => {
        setStatus('idle');
      }, 3000);
    } finally {
      setIsLoading(false);
    }
  };

  const getStatusText = () => {
    switch (status) {
      case 'waiting':
        return '매치 상대를 찾는 중입니다...';
      case 'success':
        return '매치 대기열에 참가했습니다!';
      case 'error':
        return '매치 참가에 실패했습니다. 다시 시도해주세요.';
      default:
        return '';
    }
  };

  return (
    <Container>
      <Title>⚽ 랜덤 매치</Title>
      <Description>
        빠르게 다른 플레이어들과 매칭되어 즉석에서 축구 경기를 즐겨보세요!
      </Description>
      
      <Button 
        onClick={handleJoinRandomMatch}
        disabled={isLoading}
        isLoading={isLoading}
      >
        {isLoading ? '매칭 중...' : '랜덤 매치 참가'}
      </Button>
      
      {status !== 'idle' && (
        <StatusText status={status === 'waiting' ? 'waiting' : status}>
          {getStatusText()}
        </StatusText>
      )}
    </Container>
  );
};