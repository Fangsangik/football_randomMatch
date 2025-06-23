import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'react-toastify';
import styled from 'styled-components';
import { matchService } from '../../services/api';
import { MatchRequestDto } from '../../types/api';

const Container = styled.div`
  max-width: 600px;
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

const TextArea = styled.textarea`
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  min-height: 100px;
  resize: vertical;
  
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

const PlayerSection = styled.div`
  border: 1px solid #ddd;
  border-radius: 4px;
  padding: 1rem;
  background: #f8f9fa;
`;

const PlayerList = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-top: 0.5rem;
`;

const PlayerTag = styled.span`
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

interface CreateMatchFormProps {
  onCancel: () => void;
  onSuccess: () => void;
}

export const CreateMatchForm: React.FC<CreateMatchFormProps> = ({ onCancel, onSuccess }) => {
  const [isLoading, setIsLoading] = useState(false);
  const [players, setPlayers] = useState<number[]>([]);
  const [playerInput, setPlayerInput] = useState('');
  
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<MatchRequestDto>();

  const addPlayer = () => {
    const playerId = parseInt(playerInput);
    if (playerId && !players.includes(playerId)) {
      setPlayers([...players, playerId]);
      setPlayerInput('');
    }
  };

  const removePlayer = (playerId: number) => {
    setPlayers(players.filter(id => id !== playerId));
  };

  const onSubmit = async (data: MatchRequestDto) => {
    setIsLoading(true);
    try {
      const matchData = {
        ...data,
        matchUsers: players
      };
      
      await matchService.createMatch(matchData);
      toast.success('매치가 성공적으로 생성되었습니다!');
      onSuccess();
    } catch (error: any) {
      toast.error(error.response?.data?.message || '매치 생성에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container>
      <Title>새 매치 만들기</Title>
      <Form onSubmit={handleSubmit(onSubmit)}>
        <FormGroup>
          <Label htmlFor="matchName">매치명</Label>
          <Input
            id="matchName"
            type="text"
            placeholder="매치명을 입력하세요"
            {...register('matchName', {
              required: '매치명은 필수입니다.',
              minLength: {
                value: 2,
                message: '매치명은 최소 2자 이상이어야 합니다.'
              }
            })}
          />
          {errors.matchName && <ErrorMessage>{errors.matchName.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="reservationId">예약 ID</Label>
          <Input
            id="reservationId"
            type="number"
            placeholder="예약 ID를 입력하세요"
            {...register('reservationId', {
              required: '예약 ID는 필수입니다.',
              min: {
                value: 1,
                message: '유효한 예약 ID를 입력해주세요.'
              }
            })}
          />
          {errors.reservationId && <ErrorMessage>{errors.reservationId.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="stadiumId">경기장 ID</Label>
          <Input
            id="stadiumId"
            type="number"
            placeholder="경기장 ID를 입력하세요"
            {...register('stadiumId', {
              required: '경기장 ID는 필수입니다.',
              min: {
                value: 1,
                message: '유효한 경기장 ID를 입력해주세요.'
              }
            })}
          />
          {errors.stadiumId && <ErrorMessage>{errors.stadiumId.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="matchDate">경기 일시</Label>
          <Input
            id="matchDate"
            type="datetime-local"
            {...register('matchDate', {
              required: '경기 일시는 필수입니다.'
            })}
          />
          {errors.matchDate && <ErrorMessage>{errors.matchDate.message}</ErrorMessage>}
        </FormGroup>

        <PlayerSection>
          <Label>참가자 관리</Label>
          <div style={{ display: 'flex', gap: '0.5rem', marginTop: '0.5rem' }}>
            <Input
              type="number"
              placeholder="사용자 ID 입력"
              value={playerInput}
              onChange={(e) => setPlayerInput(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && (e.preventDefault(), addPlayer())}
            />
            <Button type="button" onClick={addPlayer}>
              추가
            </Button>
          </div>
          <PlayerList>
            {players.map((playerId) => (
              <PlayerTag key={playerId}>
                사용자 {playerId}
                <RemoveButton type="button" onClick={() => removePlayer(playerId)}>
                  ×
                </RemoveButton>
              </PlayerTag>
            ))}
          </PlayerList>
        </PlayerSection>

        <ButtonGroup>
          <Button type="button" onClick={onCancel}>
            취소
          </Button>
          <Button type="submit" variant="primary" disabled={isLoading}>
            {isLoading ? '생성 중...' : '매치 생성'}
          </Button>
        </ButtonGroup>
      </Form>
    </Container>
  );
};