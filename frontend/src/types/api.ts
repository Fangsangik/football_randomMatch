// API Request/Response Types
export type UserRole = "ADMIN" | "VENDOR" | "TEAM_LEADER" | "NORMAL";
export type UserTier = "ROOKIE" | "BEGINNER" | "AMATEUR" | "SEMI_PRO" | "PRO";
export type StadiumStatus = "AVAILABLE" | "NOT_POSSIBLE";

// User Types
export interface UserRequestDto {
  email: string;
  password: string;
  name: string;
  phoneNumber: string;
  age: number;
  userRole: UserRole;
}

export interface UserResponseDto {
  id: number;
  email: string;
  name: string;
  phoneNumber: string;
  age: number;
  userTier: UserTier;
  userRole: UserRole;
  createdAt: string;
}

export interface LoginRequestDto {
  email: string;
  password: string;
}

export interface LoginResponseDto {
  accessToken: string;
  refreshToken?: string;
  tokenType: string;
  expiresIn: number;
  message: string;
  email?: string;
  name?: string;
  role?: string;
  tier?: string;
}

export interface UserPasswordUpdateDto {
  currentPassword: string;
  newPassword: string;
  newPasswordConfirm: string;
}

// Match Types
export interface MatchRequestDto {
  matchName: string;
  reservationId: number;
  stadiumId: number;
  matchDate: string;
  matchUsers: number[];
}

export interface MatchResponseDto {
  id: number;
  matchName: string;
  reservationId: number;
  stadiumId: number;
  matchDate: string;
  matchUsers: number[];
}

export interface MatchRatingRequestDto {
  userId: number;
  rating: number;
}

export interface TeamMatchRequestDto {
  MatchName: string;
  reservationId: number;
  stadiumId: number;
  teamIds: number[];
  teamName: string[];
  headCount: number;
}

// Team Types - Define Team interface first to avoid circular reference
export interface Team {
  id: number;
  teamName: string;
  headCount: number;
  createdAt: string;
}

export interface TeamRequestDto {
  teamName: string;
  headCount: number;
  userIds: number[];
}

export interface TeamResponseDto {
  id: number;
  teamName: string;
  headCount: number;
  createdAt: string;
}

export interface TeamMatchResponseDto {
  id: number;
  teams: Array<{ teams: Team[] }>;
  headCount: number;
}

// Stadium Types - Define Stadium types first to avoid circular reference
export interface StadiumRequestDto {
  userId: number;
  name: string;
  description: string;
  capacity: number;
  city: string;
  state: string;
  postalCode: string;
  specificAddress: string;
}

export interface StadiumResponseDto {
  id: number;
  name: string;
  status: StadiumStatus;
  description: string;
  capacity: number;
  user: UserResponseDto;
}

export interface StadiumUpdateDto {
  id: number;
  name: string;
  description: string;
  capacity: number;
  city: string;
  state: string;
  stadiumStatus: StadiumStatus;
  postalCode: string;
  specificAddress: string;
}

// Reservation Types
export interface ReservationRequestDto {
  name: string;
  fee: number;
  userId: number;
  stadiumId: number;
}

export interface ReservationResponseDto {
  id: number;
  name: string;
  fee: number;
  user: UserResponseDto;
  stadium: StadiumResponseDto;
}

export interface ReservationDeleteRequestDto {
  id: number;
}

// Shorts Types
export interface ShortsRequestDto {
  title: string;
  description: string;
  url: string;
}

export interface ShortsResponseDto {
  id: number;
  title: string;
  description: string;
  url: string;
  createdAt: string;
}

// Pagination Types
export interface PageInfo {
  page: number;
  size: number;
  sort: string[];
}

export interface PaginatedResponse<T> {
  content: T[];
  pageable: PageInfo;
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  numberOfElements: number;
  size: number;
  number: number;
  sort: {
    sorted: boolean;
    unsorted: boolean;
    empty: boolean;
  };
  empty: boolean;
}