# sungshin-club-app

회원가입/로그인과 하단 탭 네비게이션(홈, 일정, 바로가기)을 기반으로 동아리 정보를 확인하고 일정을 관리할 수 있는 Android 애플리케이션입니다.

## Demo Flow
**MainActivity (Launcher)** → LoginActivity → HomeActivity  
- 앱 실행 시 `MainActivity`가 런처로 실행되며, 2초 후 `LoginActivity`로 이동합니다.
- 로그인 상태가 유지되어 있으면 `LoginActivity`에서 `HomeActivity`로 자동 이동합니다.

## Features
### 1) 회원가입 / 로그인 + 로그인 유지
- 회원가입: 이름/학번(8자리)/비밀번호를 입력받아 `SharedPreferences`에 저장
- 로그인: 학번/비밀번호 검증 후 `isLoggedIn`, `currentUser` 저장
- 앱 재실행 시 로그인 유지 상태이면 자동으로 홈 화면 진입

### 2) Bottom Navigation 기반 화면 구성
- `HomeActivity`에서 `BottomNavigationView`로 Fragment 전환
- 탭 구성: **Home / Schedule / ShortCut**
- 우측 상단 사용자 아이콘 클릭 시 `UserInfoFragment`로 이동

### 3) 홈(Home) 화면
- 현재 사용자 이름 및 선택된 동아리 정보 표시(SharedPreferences 기반)
- SQLite에서 전체 이벤트를 날짜순으로 불러와 예정 일정 리스트로 출력(RecyclerView)

### 4) 일정(Schedule) 화면: 캘린더 기반 일정 CRUD
- CalendarView로 날짜 선택 → 해당 날짜 이벤트 조회
- Dialog를 통해 이벤트 추가/수정/삭제
- 일정 데이터는 SQLite(`events.db`)에 저장

### 5) 바로가기(ShortCut) 화면
- 성신여대 포털/홈페이지/LMS 링크 클릭 시 브라우저로 이동(Intent ACTION_VIEW)

### 6) 사용자(UserInfo) 화면
- 로그아웃 기능 (isLoggedIn=false)
- 동아리 등록/선택 기능 (SharedPreferences 저장)

## Tech Stack
- Kotlin / Android Studio
- Activity + Fragment 구조
- Material `BottomNavigationView`
- SharedPreferences (로그인 상태/사용자 정보/동아리 정보 저장)
- SQLite (일정 저장) + SQLiteOpenHelper
- RecyclerView / ListView / Dialog

## Navigation Structure
- **HomeActivity**
  - `fragment_container`(FrameLayout)에 Fragment 교체 방식으로 화면 전환
  - 하단 탭 메뉴(`bottom_nav_menu.xml`)
    - `nav_home` → HomeFragment
    - `nav_schedule` → ScheduleFragment
    - `nav_community` → ShortcutFragment
  - 우측 상단 `userInfoIcon` 클릭 → UserInfoFragment

## Data Storage
### SharedPreferences (`AppPrefs`)
- `isLoggedIn`: 로그인 상태 유지
- `currentUser`: 현재 로그인된 사용자(학번)
- 사용자별 키 저장:
  - `$studentId:name`
  - `$studentId:password`
  - `$studentId:clubs` (StringSet)
  - `$studentId:selectedClub` ("동아리명:설명")

### SQLite (`events.db`)
- Table: `events (id, date, event)`
- 제공 기능:
  - 날짜별 이벤트 조회
  - 전체 이벤트 날짜순 조회

## Screenshots

<img width="382" height="734" alt="image" src="https://github.com/user-attachments/assets/d89d02e6-3591-4514-b3eb-1775c5871581" />
<img width="382" height="734" alt="image" src="https://github.com/user-attachments/assets/ad6fe31b-c98a-40d5-829b-89e6c92c84ca" />
<img width="376" height="731" alt="image" src="https://github.com/user-attachments/assets/ae2d9124-9955-45e9-bf73-1a4068e445ed" />
<img width="352" height="723" alt="image" src="https://github.com/user-attachments/assets/e8f01a02-4517-4632-9143-35f4eacf065e" />
<img width="359" height="717" alt="image" src="https://github.com/user-attachments/assets/27a076a7-0837-475f-9f4f-6d06dddfd026" />
<img width="357" height="719" alt="image" src="https://github.com/user-attachments/assets/1da265f9-2b92-4d85-aaab-0a3cac0e159c" />
<img width="359" height="711" alt="image" src="https://github.com/user-attachments/assets/d8e00b99-432f-4766-ae83-d7e48209d778" />
<img width="352" height="715" alt="image" src="https://github.com/user-attachments/assets/1b67fd08-4151-4792-9ac7-8c80fa140cc6" />


## How to Run
1. Android Studio에서 프로젝트 열기
2. Gradle Sync 완료 후 에뮬레이터 또는 실제 기기 실행
3. Run ▶︎
