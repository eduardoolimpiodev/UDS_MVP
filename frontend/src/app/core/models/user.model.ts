export interface User {
  id: number;
  username: string;
  role: 'ADMIN' | 'USER';
}

export interface AuthResponse {
  token: string;
  username: string;
  role: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}
