import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import styled from 'styled-components';
import { teamService } from '../../services/api';
import { TeamRequestDto } from '../../types/api';

const Container = styled.div`
  max-width: 500px;
  margin: 0 auto;
  padding: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const Title = styled.h2`
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
`;

const TeamIcon = styled.span`
  font-size: 2rem;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
`;

const FormGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
`;

const Label = styled.label`
  font-weight: 500;
  color: #555;
`;

const Input = styled.input`
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  
  &:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
  }
`;

const ErrorMessage = styled.span`
  color: #dc3545;
  font-size: 0.875rem;
`;

const ButtonGroup = styled.div`
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
`;

const Button = styled.button<{ variant?: 'primary' | 'secondary' }>`
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s;
  
  ${props => props.variant === 'primary' ? `
    background-color: #007bff;
    color: white;
    &:hover { background-color: #0056b3; }
  ` : `
    background-color: #6c757d;
    color: white;
    &:hover { background-color: #545b62; }
  `}
  
  &:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
  }
`;

const MemberSection = styled.div`
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 1rem;
  background: #f8f9fa;
`;

const MemberList = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
`;

const MemberTag = styled.span`
  background: #007bff;
  color: white;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
`;

const RemoveButton = styled.button`
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 1rem;
  line-height: 1;
`;

const InfoBox = styled.div`
  background: #e3f2fd;
  border: 1px solid #bbdefb;
  border-radius: 4px;
  padding: 1rem;
  margin-top: 1rem;
`;

const InfoTitle = styled.h4`
  margin: 0 0 0.5rem 0;
  color: #1565c0;
  font-size: 0.9rem;
`;

const InfoText = styled.p`
  margin: 0;
  color: #1976d2;
  font-size: 0.8rem;
  line-height: 1.4;
`;

interface CreateTeamFormProps {
  onCancel: () => void;
  onSuccess: () => void;
}

export const CreateTeamForm: React.FC<CreateTeamFormProps> = ({ onCancel, onSuccess }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [members, setMembers] = useState<number[]>([]);
  const [memberInput, setMemberInput] = useState('');
  
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<TeamRequestDto>();

  const addMember = () => {
    const memberId = parseInt(memberInput);
    if (memberId && !members.includes(memberId)) {
      setMembers([...members, memberId]);
      setMemberInput('');
    }
  };

  const removeMember = (memberId: number) => {
    setMembers(members.filter(id => id !== memberId));
  };

  const onSubmit = async (data: TeamRequestDto) => {
    setIsLoading(true);
    try {
      const teamData = {
        ...data,
        userIds: members
      };
      
      await teamService.createTeam(teamData);
      toast.success('팀이 성공적으로 생성되었습니다!');
      onSuccess();
    } catch (error: any) {
      toast.error(error.response?.data?.message || '팀 생성에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container>
      <Title>
        <TeamIcon>⚽</TeamIcon>
        새 팀 만들기
      </Title>
      
      <Form onSubmit={handleSubmit(onSubmit)}>
        <FormGroup>
          <Label htmlFor="teamName">팀명</Label>
          <Input
            id="teamName"
            type="text"
            placeholder="팀명을 입력하세요"
            {...register('teamName', {
              required: '팀명은 필수입니다.',
              minLength: {
                value: 2,
                message: '팀명은 최소 2자 이상이어야 합니다.'
              },
              maxLength: {
                value: 20,
                message: '팀명은 최대 20자까지 가능합니다.'
              }
            })}
          />
          {errors.teamName && <ErrorMessage>{errors.teamName.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="headCount">모집 인원</Label>
          <Input
            id="headCount"
            type="number"
            placeholder="모집할 인원 수를 입력하세요"
            {...register('headCount', {
              required: '모집 인원은 필수입니다.',
              min: {
                value: 1,
                message: '최소 1명 이상이어야 합니다.'
              },
              max: {
                value: 20,
                message: '최대 20명까지 모집 가능합니다.'
              }
            })}
          />
          {errors.headCount && <ErrorMessage>{errors.headCount.message}</ErrorMessage>}
        </FormGroup>

        <MemberSection>
          <Label>팀 멤버 초대 (선택사항)</Label>
          <div style={{ display: 'flex', gap: '0.5rem', marginTop: '0.5rem' }}>
            <Input
              type="number"
              placeholder="사용자 ID 입력"
              value={memberInput}
              onChange={(e) => setMemberInput(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addMember())}
            />
            <Button type="button" onClick={addMember}>
              추가
            </Button>
          </div>
          <MemberList>
            {members.map((memberId) => (
              <MemberTag key={memberId}>
                사용자 {memberId}
                <RemoveButton type="button" onClick={() => removeMember(memberId)}>
                  ×
                </RemoveButton>
              </MemberTag>
            ))}
          </MemberList>
        </MemberSection>

        <InfoBox>
          <InfoTitle>💡 팀 생성 안내</InfoTitle>
          <InfoText>
            • 팀을 생성하면 자동으로 팀 리더가 됩니다<br />
            • 모집 인원은 생성 후에도 수정할 수 있습니다<br />
            • 팀 멤버는 나중에도 추가할 수 있습니다<br />
            • 팀 리더만 팀 정보를 수정하거나 삭제할 수 있습니다
          </InfoText>
        </InfoBox>

        <ButtonGroup>
          <Button type="button" onClick={onCancel}>
            취소
          </Button>
          <Button type="submit" variant="primary" disabled={isLoading}>
            {isLoading ? '생성 중...' : '팀 생성'}
          </Button>
        </ButtonGroup>
      </Form>
    </Container>
  );
};