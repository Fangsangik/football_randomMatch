import axios from 'axios';
import { LoginResponseDto } from '../types/api';

// Create axios instance with base configuration
export const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add auth token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle auth errors and token refresh
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // 토큰 갱신 시도
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
          const response = await axios.post('/auth/refresh', {}, {
            baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080',
            headers: {
              Authorization: `Bearer ${refreshToken}`
            }
          });
          
          // 새로운 토큰으로 업데이트
          localStorage.setItem('accessToken', response.data.accessToken);
          if (response.data.refreshToken) {
            localStorage.setItem('refreshToken', response.data.refreshToken);
          }
          
          // 원래 요청 재시도
          originalRequest.headers.Authorization = `Bearer ${response.data.accessToken}`;
          return api(originalRequest);
        }
      } catch (refreshError) {
        console.error('토큰 갱신 실패:', refreshError);
      }
      
      // 갱신 실패 시 로그아웃 처리
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    
    return Promise.reject(error);
  }
);

// Auth service
export const authService = {
  login: (email: string, password: string) =>
    api.post<LoginResponseDto>('/auth/login', { email, password }),
  
  signup: (userData: any) =>
    api.post('/auth/signup', userData),
  
  logout: async () => {
    try {
      // 백엔드 로그아웃 API 호출
      await api.post('/auth/logout');
    } catch (error) {
      console.error('로그아웃 API 호출 실패:', error);
    } finally {
      // API 호출 성공 여부와 관계없이 로컬 토큰 삭제
      localStorage.removeItem('accessToken');
      localStorage.removeItem('refreshToken');
      localStorage.removeItem('user');
    }
  },
  
  getCurrentUser: () => {
    const user = localStorage.getItem('user');
    return user ? JSON.parse(user) : null;
  },
  
  isAuthenticated: () => {
    return !!localStorage.getItem('accessToken');
  },

  refreshToken: async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      throw new Error('No refresh token available');
    }
    
    const response = await api.post<LoginResponseDto>('/auth/refresh', {}, {
      headers: {
        Authorization: `Bearer ${refreshToken}`
      }
    });
    
    // 새로운 토큰들로 업데이트
    localStorage.setItem('accessToken', response.data.accessToken);
    if (response.data.refreshToken) {
      localStorage.setItem('refreshToken', response.data.refreshToken);
    }
    
    return response.data;
  }
};

// User service
export const userService = {
  getUser: (userId: number) =>
    api.get(`/users/${userId}`),
  
  updateName: (userData: any) =>
    api.put('/users', userData),
  
  updatePassword: (passwordData: any) =>
    api.patch('/users', passwordData),
  
  deleteUser: (userData: any) =>
    api.delete('/users', { data: userData }),
  
  updateUserTier: (userId: number) =>
    api.patch(`/users/users/${userId}/tier`)
};

// Match service
export const matchService = {
  createMatch: (matchData: any) =>
    api.post('/matches', matchData),
  
  submitRatings: (matchId: number, ratings: any[]) =>
    api.post(`/matches/${matchId}/ratings`, ratings),
  
  completeMatch: (matchId: number) =>
    api.post(`/matches/${matchId}/complete`),
  
  joinMatch: () =>
    api.post('/matches/join'),
  
  createTeamMatch: (teamMatchData: any) =>
    api.post('/matches/team', teamMatchData),
  
  fillEmptySpot: (stadiumId: number) =>
    api.post(`/matches/fill/${stadiumId}`)
};

// Team service
export const teamService = {
  createTeam: (teamData: any) =>
    api.post('/team', teamData),
  
  getTeam: (teamId: number) =>
    api.get(`/team/${teamId}`),
  
  updateTeam: (teamId: number, teamData: any) =>
    api.put(`/team/${teamId}`, teamData),
  
  joinTeam: (teamId: number) =>
    api.patch(`/team/${teamId}/join`),
  
  deleteTeam: (teamId: number) =>
    api.delete(`/team/${teamId}`)
};

// Reservation service
export const reservationService = {
  createReservation: (reservationData: any) =>
    api.post('/reservations', reservationData),
  
  getReservation: (id: number) =>
    api.get(`/reservations/${id}`),
  
  getStadiumReservations: (stadiumId: number, page = 0, size = 10) =>
    api.get(`/reservations/stadiums/${stadiumId}?page=${page}&size=${size}`),
  
  getReservationByStadium: (stadiumId: number) =>
    api.get(`/reservations/stadium/${stadiumId}`),
  
  getUserReservations: (page = 0, size = 10) =>
    api.get(`/reservations/user?page=${page}&size=${size}`),
  
  deleteReservation: (id: number, deleteData: any) =>
    api.delete(`/reservations/${id}`, { data: deleteData })
};

// Stadium service
export const stadiumService = {
  createStadium: (stadiumData: any) =>
    api.post('/stadiums', stadiumData),
  
  getStadium: (id: number) =>
    api.get(`/stadiums/${id}`),
  
  updateStadium: (stadiumData: any) =>
    api.patch('/stadiums', stadiumData),
  
  deleteStadium: (id: number) =>
    api.delete(`/stadiums/${id}`)
};

// Shorts service
export const shortsService = {
  createShorts: (formData: FormData) =>
    api.post('/shorts', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    }),
  
  getShorts: (shortsId: number) =>
    api.get(`/shorts/${shortsId}`),
  
  getShortsFeeds: (page = 0, size = 10) =>
    api.get(`/shorts/feed?page=${page}&size=${size}`),
  
  updateShorts: (shortsId: number, shortsData: any) =>
    api.patch(`/shorts/${shortsId}`, shortsData),
  
  deleteShorts: (shortsId: number) =>
    api.delete(`/shorts/${shortsId}`)
};