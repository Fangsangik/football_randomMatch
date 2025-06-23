import React from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { useAuth } from '../../contexts/AuthContext';

const Header = styled.header`
  background: linear-gradient(135deg, #007bff, #28a745);
  color: white;
  padding: 1rem 2rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
`;

const Nav = styled.nav`
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
`;

const Logo = styled.h1`
  font-size: 1.5rem;
  font-weight: bold;
  cursor: pointer;
`;

const NavLinks = styled.div`
  display: flex;
  gap: 1rem;
  align-items: center;
`;

const NavLink = styled.button`
  background: none;
  border: none;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  transition: background-color 0.2s;
  cursor: pointer;
  font-size: 1rem;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
`;

const LogoutButton = styled.button`
  background: none;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 1rem;
  
  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
  }
`;

export const Navigation: React.FC = () => {
  const navigate = useNavigate();
  const { logout } = useAuth();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <Header>
      <Nav>
        <Logo onClick={() => navigate('/dashboard')}>⚽ Football Manager</Logo>
        <NavLinks>
          <NavLink onClick={() => navigate('/dashboard')}>대시보드</NavLink>
          <NavLink onClick={() => navigate('/matches')}>매치</NavLink>
          <NavLink onClick={() => navigate('/teams')}>팀</NavLink>
          <NavLink onClick={() => navigate('/reservations')}>예약</NavLink>
          <NavLink onClick={() => navigate('/stadiums')}>경기장</NavLink>
          <LogoutButton onClick={handleLogout}>로그아웃</LogoutButton>
        </NavLinks>
      </Nav>
    </Header>
  );
};