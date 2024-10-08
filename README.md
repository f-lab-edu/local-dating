# local-dating

<br><br>
## 시나리오

### 회원가입 /  로그인
1. 회원가입 시 id, pwd, 이름, 생년월일, 핸드폰번호, 이메일 입력을 받는다
2. id, pwd, 핸드폰, 이메일 유효성검사
3. 핸드폰 or 이메일에대한 인증번호 확인
4. id, pwd 입력해서 로그인하며 7회 틀릴 경우 계정이 잠긴다
5. 잠긴 계정은 핸드폰, 이메일 인증번호를 수신해서 초기화한다
<br><br>
### 정보 입력
1. 사용자의 외모, 직업, MBTI, 관심분야, 성향, 음주, 흡연, 종교, 거주지의 정보를 입력한다
2. 상대방에게 보여줄 대표 소개 문구를 작성하고 대표 사진을 업로드한다
3. 매칭상대의 나이, 외모, MBTI, 성향, 음주, 흡연, 종교, 거주지의 희망 조건을 입력한다
4. 매칭상대의 희망 조건 중 우선 순위를 선정한다
5. 매칭 예외되는 상대의 핸드폰번호를 등록한다
6. 매칭 시작을 위해서 코인을 충전한다
7. 충전한 코인잔액을 확인한다
<br><br>
### 매칭 준비
1. 정보 입력이 모두 끝나면 매칭 시작 버튼 활성화된다
2. 매칭 시작 버튼을 누르면 입력정보 기반하여 2개의 매칭카드가 나오고 카드에는 블러처리된 프로필 사진, 무작위 생성된 닉네임이 표시된다
3. 카드는 일주일 단위로 새로운 2장으로 변경되며 코인을 통해서 원하는 시점에 변경을 한다
4. 카드를 누르면 상대 대표 소개문구, 거주지, 매칭 시작 버튼이 표시된다
5. 매칭시작 버튼을 누르면 코인잔액에서 1코인이 차감 알럿이 나온다
6. 코인이 없으면 코인충전 알럿이 나온다
7. 코인 차감알럿에서 확인을 누르면 두 사용자의 거주지 정보 기반하여 중간장소가 선정되어 표시된다
8. 위치정보를 확인하고 요청을 보내면 상대방에게 매칭 요청이 넘어간다.
9. 상대방이 매칭 요청을 수락하면 두 사용자는 현재날짜 ~ 1달 중 가능한 날짜를 체크해서 보낸다
10. 두 사용자가 보낸 날짜에서 공통되는 날짜 하루를 선정해서 보여준다
11. 두 사용자가 보낸 날짜에서 공통일이 없으면 상대가 고른 날짜가 보이고 다시 날짜를 보내거나 매칭을 취소할 수 있다
12. 날짜 선정 후 시간대를 원하는 시간대를 선정해서 보내준다
13. 시간대가 선정되면 매칭 진행일이 카드에 표시되며 최초 매칭 요청자 코인이 차감된다
14. 매칭일 3일 전 까지 일정변경, 취소버튼이 활성화 된다
15. 일정 변경 시 두 사용자는 날짜, 시간을 다시 선택한다
16. 취소 시 차감된 코인을 반환한다
<br><br>
### 매칭 진행
1. 선정장소에 매칭시작 시간에 도착했을 경우 사용자 위치정보를 전송한다
2. 두 사용자 모두 도착하여 위치정보가 전송되었을 경우, 나중에 전송된 시간을 시작시간으로 기록한다
3. 실시간으로 위치정보를 전송하여 중간 두 사람의 거리가 일정거리 이상인 상태가 10분 이상 지속 되었을 경우 매칭 실패로 본다
4. 시작시간 30분 후 사용자 위치정보를 전송하며 두 사람의 거리가 가까운 거리에 있으면 성공으로 본다
5. 두 사용자 모두 종료 위치정보가 전송되면 카드에서 O/X를 선택할 수 있다
6. 양쪽이 O를 선택하면 상대 카드에서 사진 블러가 풀린다
7. 블러가 풀린 사용자와 7일 간 대화세션이 활성화되어 메세지를 보낸다
8. 7일 후 기존 카드는 사라지고 새로운 2장이 나타난다
9. 매칭시작 시간 위치정보 전송 후 상대방 위치정보가 30분 안에 이루어지지 않을 경우 노쇼로 본다
10. 노쇼를 했을 경우 매칭을 위해 소비한 코인 복구하고 무료 매칭권을 준다
11. 노쇼 당사자는 현재 나와있는 카드가 14일간 잠기며 7일간 들어오는 매칭 수신이 안된다


<br><br>
## 기능명세
<!--

분류(메뉴별도 괜찮음) | 기능명 | 우선순위 | 중요도 
 
### 회원 정보
0. 회원가입화면
1. ID, 핸드폰, 이메일 중복검사
2. 핸드폰 인증번호 발송
3. 이메일 인증번호 발송
4. 회원가입요청
5. 비밀번호 변경
6. 로그인
7. 회원정보 조회
8. 회원정보 수정

### 매칭 정보
1. 사용자 프로필 정보 저장/수정
2. 사용자 프로필 정보 조회
3. 소개문구, 사진 저장/수정
4. 매칭 희망 정보 저장/수정
5. 매칭 희망 정보 조회
6. 매칭 우선순위 저장/수정
7. 매칭 우선순위 조회
8. 예외 핸드폰 번호 조회
9. 예외 핸드폰 번호 저장
10. 예외 핸드폰 번호 삭제
11. 코인 충전
12. 코인 조회
13. 끝

### 매칭 준비
1. 매칭 상태 변경 (활성화/비활성화)
2. 매칭카드 정보 받기(계정 상태에 따라서 다름)
3. 매칭카드 리셋
4. 신규 카드 수신
5. 카드 상세정보 확인
6. 선택한 카드 매칭 시작(잔여코인 확인)
7. 매칭 장소 확인
8. 상대방에게 매칭확인 요청
9. 신규 매칭요청 확인
10. 매칭날짜, 시간대 전송
11. 매칭날짜, 시간대 확인
12. 현재 매칭 취소
13. 최종 선정 장소, 시간 확인 / 코인차감
14. 매칭날짜, 시간대 변경 요청
15. 코인차감 / 코인반환

### 매칭 진행
1. 사용자 위치정보 전송 (시작, 중간확인, 종료)
2. 매칭 상태 수신
3. 애프터 OX 선택 전송
4. 애프터 결과 확인
5. 상대방 사진 확인
6. 메세지 보내기
7. 메세지 수신
8. 코인복구
`-->



| 분류         | 화면         | 기능                               | 우선순위   |
|-----------------|-----------------|------------------------------------|----------|
| 회원 정보   | 회원가입   | ID, 핸드폰, 이메일 중복검사  | 37 |
| 		   | 회원가입, 비밀번호 찾기   | 핸드폰 인증번호 발송   		| 42 |
| 		   | 회원가입, 비밀번호 찾기   | 이메일 인증번호 발송 		| 39  |
| 		   | 회원가입   | 회원가입요청 				 | 1 |
| 		   | 사용자정보   | 비밀번호 변경  	 		| 38 |
| 		   | 로그인   | 로그인					 | 2  |
| 		   | 사용자정보   | 회원정보 조회 			 | 34 |
| 		   | 사용자정보   | 회원정보 수정  			 | 35 |
| 매칭 정보   | 프로필설정   | 사용자 프로필 정보 저장/수정 	| 3  |
| 		   | 프로필설정   | 사용자 프로필 정보 조회 		 | 4 |
| 		   | 프로필설정   | 소개문구, 사진 저장/수정  	 | 33 |
| 		   | 희망조건설정   | 매칭 희망 정보 저장/수정		 | 5  |
| 		   | 희망조건설정   | 매칭 희망 정보 조회  		| 6 |
| 		   | 희망조건설정   | 매칭 우선순위 저장/수정  		 | 7 |
| 		   | 희망조건설정   | 매칭 우선순위 조회 			| 8  |
| 		   | 사용자정보   | 예외 핸드폰 번호 조회 		 | 30 |
| 		   | 사용자정보   | 예외 핸드폰 번호 저장 		  | 31 |
| 		   | 사용자정보   | 예외 핸드폰 번호 삭제 		| 32  |
| 		   | 코인   | 코인 충전  				| 24 |
| 		   | 코인, 메인   | 코인 조회  				 | 25 |
| 매칭 준비   | 메인   | 매칭 상태 변경 (활성화/비활성화) | 17  |
| 		   | 메인   | 매칭카드 정보 받기(계정 상태에 따라서 다름)  | 9 |
| 		   | 메인   | 매칭카드 리셋   				| 18 |
| 		   | 메인   | 신규 카드 수신 				| 28  |
| 		   | 카드상세   | 카드 상세정보 확인  				| 10 |
| 		   | 카드상세   | 선택한 카드 매칭 시작(잔여코인 확인)   | 11 |
| 		   | 매칭   | 매칭 장소 확인				 | 21  |
| 		   | 매칭   | 상대방에게 매칭확인 요청  			| 22 |
| 		   | 매칭   | 신규 매칭요청 확인   			| 23 |
| 		   | 매칭   | 매칭날짜, 시간대 전송 			| 12  |
| 		   | 매칭   | 매칭날짜, 시간대 확인 			 | 13 |
| 		   | 매칭, 카드상세   | 현재 매칭 취소   				| 15 |
| 		   | 매칭   | 최종 선정 장소, 시간 확인 / 코인차감 | 14  |
| 		   | 매칭   | 매칭날짜, 시간대 변경 요청  		| 29 |
| 		   | 매칭, 메인   | 코인차감 / 코인반환   			| 26 |
| 매칭 진행   |    		| 사용자 위치정보 전송 (시작, 중간확인, 종료) | 16  |
| 		   | 	   | 매칭 상태 수신 				 | 17 |
| 		   | 매칭   | 애프터 OX 선택 전송   			| 19 |
| 		   | 매칭   | 애프터 결과 확인 				| 20  |
| 		   | 카드상세   | 상대방 사진 확인 				| 36  |
| 		   | 매칭   | 메세지 보내기  				| 40 |
| 		   | 매칭   | 메세지 수신  					 | 41 |
| 		   |    | 코인복구 					| 27  |

<br><br>
## ERD
![local-dating](https://github.com/user-attachments/assets/08cb9db0-8abe-4a5e-9a35-8db1c3fbb884)
<!--<img width="619" alt="local-dating drawio" src="https://github.com/user-attachments/assets/2fa65d0a-eb36-4f94-8f90-669fc4feaffb">-->
