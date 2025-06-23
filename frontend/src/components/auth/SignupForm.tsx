import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import { toast } from 'react-toastify';
import styled from 'styled-components';
import { authService } from '../../services/api';
import { UserRequestDto, UserRole } from '../../types/api';

const Container = styled.div`
  max-width: 500px;
  margin: 0 auto;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background: white;
`;

const Title = styled.h2`
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
`;

const Form = styled.form`
  display: flex;
  flex-direction: column;
  gap: 1rem;
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

const Select = styled.select`
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  background: white;
  
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

const Button = styled.button`
  padding: 0.75rem;
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
  
  &:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
  }
`;

const LinkText = styled.p`
  text-align: center;
  margin-top: 1rem;
  
  a {
    color: #007bff;
    text-decoration: none;
    
    &:hover {
      text-decoration: underline;
    }
  }
`;

export const SignupForm: React.FC = () => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<UserRequestDto & { confirmPassword: string }>();

  const password = watch('password');

  const onSubmit = async (data: UserRequestDto & { confirmPassword: string }) => {
    setIsLoading(true);
    try {
      const { confirmPassword, ...signupData } = data;
      await authService.signup(signupData);
      toast.success('회원가입이 완료되었습니다!');
      navigate('/login');
    } catch (error: any) {
      toast.error(error.response?.data?.message || '회원가입에 실패했습니다.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container>
      <Title>회원가입</Title>
      <Form onSubmit={handleSubmit(onSubmit)}>
        <FormGroup>
          <Label htmlFor="email">이메일</Label>
          <Input
            id="email"
            type="email"
            placeholder="이메일을 입력하세요"
            {...register('email', {
              required: '이메일은 필수입니다.',
              pattern: {
                value: /^\S+@\S+$/i,
                message: '올바른 이메일 형식이 아닙니다.'
              }
            })}
          />
          {errors.email && <ErrorMessage>{errors.email.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="password">비밀번호</Label>
          <Input
            id="password"
            type="password"
            placeholder="비밀번호를 입력하세요"
            {...register('password', {
              required: '비밀번호는 필수입니다.',
              minLength: {
                value: 6,
                message: '비밀번호는 최소 6자 이상이어야 합니다.'
              }
            })}
          />
          {errors.password && <ErrorMessage>{errors.password.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="confirmPassword">비밀번호 확인</Label>
          <Input
            id="confirmPassword"
            type="password"
            placeholder="비밀번호를 다시 입력하세요"
            {...register('confirmPassword', {
              required: '비밀번호 확인은 필수입니다.',
              validate: (value) => value === password || '비밀번호가 일치하지 않습니다.'
            })}
          />
          {errors.confirmPassword && <ErrorMessage>{errors.confirmPassword.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="name">이름</Label>
          <Input
            id="name"
            type="text"
            placeholder="이름을 입력하세요"
            {...register('name', {
              required: '이름은 필수입니다.',
              minLength: {
                value: 2,
                message: '이름은 최소 2자 이상이어야 합니다.'
              }
            })}
          />
          {errors.name && <ErrorMessage>{errors.name.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="phoneNumber">전화번호</Label>
          <Input
            id="phoneNumber"
            type="tel"
            placeholder="전화번호를 입력하세요"
            {...register('phoneNumber', {
              required: '전화번호는 필수입니다.',
              pattern: {
                value: /^01[0-9]-?[0-9]{4}-?[0-9]{4}$/,
                message: '올바른 전화번호 형식이 아닙니다.'
              }
            })}
          />
          {errors.phoneNumber && <ErrorMessage>{errors.phoneNumber.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="age">나이</Label>
          <Input
            id="age"
            type="number"
            placeholder="나이를 입력하세요"
            {...register('age', {
              required: '나이는 필수입니다.',
              min: {
                value: 14,
                message: '14세 이상만 가입 가능합니다.'
              },
              max: {
                value: 100,
                message: '올바른 나이를 입력해주세요.'
              }
            })}
          />
          {errors.age && <ErrorMessage>{errors.age.message}</ErrorMessage>}
        </FormGroup>

        <FormGroup>
          <Label htmlFor="userRole">사용자 유형</Label>
          <Select
            id="userRole"
            {...register('userRole', {
              required: '사용자 유형을 선택해주세요.'
            })}
          >
            <option value="">사용자 유형을 선택하세요</option>
            <option value="NORMAL">일반 사용자</option>
            <option value="TEAM_LEADER">팀 리더</option>
            <option value="VENDOR">업체</option>
          </Select>
          {errors.userRole && <ErrorMessage>{errors.userRole.message}</ErrorMessage>}
        </FormGroup>

        <Button type="submit" disabled={isLoading}>
          {isLoading ? '가입 중...' : '회원가입'}
        </Button>
      </Form>
      
      <LinkText>
        이미 계정이 있으신가요? <Link to="/login">로그인</Link>
      </LinkText>
    </Container>
  );
};