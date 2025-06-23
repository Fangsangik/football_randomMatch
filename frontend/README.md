# Football Project Frontend

축구 매치 관리 플랫폼의 React 프론트엔드 애플리케이션입니다.

## 🚀 주요 기능

### 1. **인증 시스템**
- 회원가입 및 로그인
- JWT 토큰 기반 인증
- Protected Route로 보안 강화

### 2. **매치 관리**
- 새 매치 생성
- 매치 목록 조회
- 랜덤 매치 참가
- 팀 매치 생성
- 매치 평가 시스템

### 3. **팀 관리**
- 팀 생성 및 관리
- 팀 가입/탈퇴
- 팀 리더 권한 관리
- 팀 멤버 초대

### 4. **대시보드**
- 사용자 활동 통계
- 빠른 작업 액세스
- 최근 활동 내역

## 🛠️ 기술 스택

- **React 18** - UI 라이브러리
- **TypeScript** - 정적 타입 검사
- **React Router v6** - 라우팅
- **React Query** - 서버 상태 관리
- **Axios** - HTTP 클라이언트
- **Styled Components** - CSS-in-JS 스타일링
- **React Hook Form** - 폼 관리
- **React Toastify** - 알림 시스템

## 📁 프로젝트 구조

```
src/
├── components/          # 재사용 가능한 컴포넌트
│   ├── auth/           # 인증 관련 컴포넌트
│   ├── match/          # 매치 관련 컴포넌트
│   └── team/           # 팀 관련 컴포넌트
├── contexts/           # React Context (인증 등)
├── pages/              # 페이지 컴포넌트
├── services/           # API 서비스
├── types/              # TypeScript 타입 정의
├── App.tsx            # 메인 앱 컴포넌트
└── index.tsx          # 앱 진입점
```

## 🚀 시작하기

### 1. 의존성 설치
```bash
cd frontend
npm install
```

### 2. 환경변수 설정
`.env` 파일을 생성하고 다음 내용을 추가:
```env
REACT_APP_API_URL=http://localhost:8080
```

### 3. 개발 서버 실행
```bash
npm start
```

앱이 [http://localhost:3000](http://localhost:3000)에서 실행됩니다.

## 📱 주요 페이지

### 1. **로그인/회원가입** (`/login`, `/signup`)
- 이메일/비밀번호 기반 인증
- 폼 유효성 검사
- 자동 리다이렉트

### 2. **대시보드** (`/dashboard`)
- 사용자 통계 및 활동 내역
- 빠른 작업 버튼
- 랜덤 매치 참가

### 3. **매치 관리** (`/matches`)
- 매치 목록 및 생성
- 매치 참가/완료
- 평가 시스템

### 4. **팀 관리** (`/teams`)
- 팀 생성 및 가입
- 팀 정보 수정
- 권한 기반 팀 관리

## 🔐 보안 기능

- JWT 토큰 자동 헤더 추가
- 토큰 만료 시 자동 로그아웃
- Protected Route로 인증 필요 페이지 보호
- API 에러 핸들링

## 🎨 UI/UX 특징

- 반응형 디자인
- 모던하고 직관적인 인터페이스
- 부드러운 애니메이션 효과
- 사용자 피드백 (토스트, 모달)
- 로딩 상태 표시

## 📡 API 연동

백엔드 API와 완전히 연동되어 있습니다:

- **인증**: 로그인, 회원가입, 토큰 관리
- **매치**: CRUD 및 매치 관련 모든 기능
- **팀**: 팀 생성, 가입, 관리
- **예약**: 경기장 예약 관리 (예정)
- **사용자**: 프로필 관리 (예정)

## 🚀 빌드 및 배포

### 프로덕션 빌드
```bash
npm run build
```

### 빌드 결과 확인
```bash
npm run serve
```

## 🛠️ 개발 가이드

### 새 컴포넌트 추가
1. `src/components/` 하위에 적절한 폴더 생성
2. TypeScript 컴포넌트 작성
3. styled-components로 스타일링
4. 부모 컴포넌트에서 import 및 사용

### API 서비스 추가
1. `src/services/api.ts`에 새 서비스 함수 추가
2. `src/types/api.ts`에 관련 타입 정의
3. 컴포넌트에서 React Query와 함께 사용

### 라우트 추가
1. `src/App.tsx`의 Routes에 새 Route 추가
2. 필요시 ProtectedRoute로 감싸기
3. 네비게이션 메뉴 업데이트

이제 백엔드 API와 완전히 연동된 풀스택 축구 관리 플랫폼이 완성되었습니다! 🎉