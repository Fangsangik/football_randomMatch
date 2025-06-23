import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import styled, { createGlobalStyle } from 'styled-components';

import { AuthProvider } from './contexts/AuthContext';
import { ProtectedRoute } from './components/auth/ProtectedRoute';
import { Navigation } from './components/layout/Navigation';
import { LoginForm } from './components/auth/LoginForm';
import { SignupForm } from './components/auth/SignupForm';
import { Dashboard } from './pages/Dashboard';
import { MatchPage } from './pages/MatchPage';
import { TeamPage } from './pages/TeamPage';
import { ReservationPage } from './pages/ReservationPage';
import { StadiumPage } from './pages/StadiumPage';

// Global styles
const GlobalStyle = createGlobalStyle`
  * {
    box-sizing: border-box;
  }
  
  body {
    margin: 0;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Oxygen',
      'Ubuntu', 'Cantarell', 'Fira Sans', 'Droid Sans', 'Helvetica Neue',
      sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    background-color: #f8f9fa;
  }
  
  h1, h2, h3, h4, h5, h6 {
    margin: 0;
  }
  
  a {
    color: inherit;
    text-decoration: none;
  }
`;

const AppContainer = styled.div`
  min-height: 100vh;
`;


const Main = styled.main`
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
`;

const AuthContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
`;

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Router>
          <GlobalStyle />
          <AppContainer>
            <Routes>
              {/* Public routes */}
              <Route 
                path="/login" 
                element={
                  <AuthContainer>
                    <LoginForm />
                  </AuthContainer>
                } 
              />
              <Route 
                path="/signup" 
                element={
                  <AuthContainer>
                    <SignupForm />
                  </AuthContainer>
                } 
              />
              
              {/* Protected routes */}
              <Route path="/*" element={
                <ProtectedRoute>
                  <Navigation />
                  <Main>
                    <Routes>
                      <Route path="/" element={<Navigate to="/dashboard" replace />} />
                      <Route path="/dashboard" element={<Dashboard />} />
                      <Route path="/matches" element={<MatchPage />} />
                      <Route path="/teams" element={<TeamPage />} />
                      <Route path="/reservations" element={<ReservationPage />} />
                      <Route path="/stadiums" element={<StadiumPage />} />
                    </Routes>
                  </Main>
                </ProtectedRoute>
              } />
            </Routes>
            
            <ToastContainer
              position="top-right"
              autoClose={3000}
              hideProgressBar={false}
              newestOnTop={false}
              closeOnClick
              rtl={false}
              pauseOnFocusLoss
              draggable
              pauseOnHover
            />
          </AppContainer>
        </Router>
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;