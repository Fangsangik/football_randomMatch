import React, { useState } from 'react';
import styled from 'styled-components';
import { format } from 'date-fns';
import { toast } from 'react-toastify';
import { TeamResponseDto } from '../../types/api';
import { teamService } from '../../services/api';

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

const TeamName = styled.h3`
  margin: 0 0 1rem 0;
  color: #333;
  font-size: 1.25rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

const TeamIcon = styled.span`
  font-size: 1.5rem;
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

const CreatedDate = styled.div`
  color: #007bff;
  font-size: 0.85rem;
  margin-bottom: 1rem;
`;

const HeadCount = styled.div<{ available: boolean }>`
  background: ${props => props.available ? '#d4edda' : '#f8d7da'};
  color: ${props => props.available ? '#155724' : '#721c24'};
  padding: 0.5rem;
  border-radius: 4px;
  text-align: center;
  margin-bottom: 1rem;
  font-size: 0.9rem;
  font-weight: 500;
`;

const ActionButtons = styled.div`
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
`;

const Button = styled.button<{ variant?: 'primary' | 'secondary' | 'danger' | 'success' }>`
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
      case 'success':
        return `
          background-color: #28a745;
          color: white;
          &:hover { background-color: #218838; }
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
  
  &:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
    opacity: 0.6;
  }
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
`;

const ModalContent = styled.div`
  background: white;
  padding: 2rem;
  border-radius: 8px;
  max-width: 400px;
  width: 90%;
`;

const ModalTitle = styled.h3`
  margin: 0 0 1rem 0;
  color: #333;
`;

const ModalText = styled.p`
  margin: 0 0 1.5rem 0;
  color: #666;
`;

const ModalButtons = styled.div`
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
`;

interface TeamCardProps {
  team: TeamResponseDto;
  onUpdate: (team: TeamResponseDto) => void;
  onDelete: (teamId: number) => void;
}

export const TeamCard: React.FC<TeamCardProps> = ({ team, onUpdate, onDelete }) => {
  const [isJoining, setIsJoining] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  
  const formattedDate = format(new Date(team.createdAt), 'yyyy년 MM월 dd일');

  const hasAvailableSlots = team.headCount > 0;

  const handleJoinTeam = async () => {
    if (!hasAvailableSlots) return;
    
    setIsJoining(true);
    try {
      await teamService.joinTeam(team.id);
      toast.success('팀에 성공적으로 가입했습니다!');
      
      // Update team with reduced headCount
      const updatedTeam = {
        ...team,
        headCount: team.headCount - 1
      };
      onUpdate(updatedTeam);
    } catch (error: any) {
      toast.error(error.response?.data?.message || '팀 가입에 실패했습니다.');
    } finally {
      setIsJoining(false);
    }
  };

  const handleDeleteTeam = async () => {
    setIsDeleting(true);
    try {
      await teamService.deleteTeam(team.id);
      toast.success('팀이 삭제되었습니다.');
      onDelete(team.id);
    } catch (error: any) {
      toast.error(error.response?.data?.message || '팀 삭제에 실패했습니다.');
    } finally {
      setIsDeleting(false);
      setShowDeleteModal(false);
    }
  };

  const handleViewDetails = () => {
    // TODO: Implement view team details
    console.log('Viewing team details:', team.id);
  };

  return (
    <>
      <Card>
        <TeamName>
          <TeamIcon>⚽</TeamIcon>
          {team.teamName}
        </TeamName>
        
        <CreatedDate>생성일: {formattedDate}</CreatedDate>
        
        <InfoRow>
          <Label>팀 ID:</Label>
          <Value>{team.id}</Value>
        </InfoRow>
        
        <HeadCount available={hasAvailableSlots}>
          {hasAvailableSlots 
            ? `모집 중 (${team.headCount}명 더 필요)` 
            : '모집 완료'
          }
        </HeadCount>
        
        <ActionButtons>
          <Button 
            variant="success" 
            onClick={handleJoinTeam}
            disabled={!hasAvailableSlots || isJoining}
          >
            {isJoining ? '가입 중...' : '팀 가입'}
          </Button>
          <Button variant="primary" onClick={handleViewDetails}>
            상세보기
          </Button>
          <Button 
            variant="danger" 
            onClick={() => setShowDeleteModal(true)}
            disabled={isDeleting}
          >
            삭제
          </Button>
        </ActionButtons>
      </Card>

      {showDeleteModal && (
        <Modal onClick={() => setShowDeleteModal(false)}>
          <ModalContent onClick={(e) => e.stopPropagation()}>
            <ModalTitle>팀 삭제 확인</ModalTitle>
            <ModalText>
              '{team.teamName}' 팀을 정말 삭제하시겠습니까?
              <br />
              이 작업은 되돌릴 수 없습니다.
            </ModalText>
            <ModalButtons>
              <Button onClick={() => setShowDeleteModal(false)}>
                취소
              </Button>
              <Button 
                variant="danger" 
                onClick={handleDeleteTeam}
                disabled={isDeleting}
              >
                {isDeleting ? '삭제 중...' : '삭제'}
              </Button>
            </ModalButtons>
          </ModalContent>
        </Modal>
      )}
    </>
  );
};