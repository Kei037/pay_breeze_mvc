# :paperclip: Transaction Project (가계부 어플)
> 지출과 수입을 쉽게 관리할 수 있도록 하는 가계부 어플리케이션입니다.

![DB설계 및 구성](https://github.com/user-attachments/assets/90cff346-c1ab-472d-9997-9e99750ca404)

## 목차
- [들어가며](#들어가며)
  - [프로젝트 소개](#1-프로젝트-소개)    
  - [프로젝트 기능](#2-프로젝트-기능)    
  - [사용 기술](#3-사용-기술)   
     - [백엔드](#3-1-백엔드)
     - [프론트엔드](#3-2-프론트엔드)
  - [실행 화면](#4-실행-화면)   


- [구조 및 설계](#구조-및-설계)
  - [패키지 구조](#1-패키지-구조)
  - [DB 설계](#2. DB 설계)
  - [API 설계](#3-api-설계)

- [개발 내용](#개발-내용)

- [마치며](#마치며)
  - [프로젝트 보완사항](#1-프로젝트-보완사항)
  - [후기](#2-후기)

## 들어가며
### 1. 프로젝트 소개

Pay Breeze
쉽고 간편하게 지출과 수입을 관리하고 수입/지출, 날짜, 카테고리별 필터를 통해 쉽게 확인할 수 있는 가계부 어플리케이션입니다.
3명의 팀원들로 구성되어 작업한 팀 프로젝트입니다.

### 2. 프로젝트 기능

프로젝트의 주요 기능은 다음과 같습니다.
- **홈 화면 -** 수입/지출, 날짜, 카테고리별 필터기능, 오늘 금액의 통계 기능
- **달력 화면 -** 달력으로 상세한 날짜를 하루 or 기간별로 조회하는 기능
- **추가 화면 -** 가계부 추가 기능, 카테고리 선택, 카테고리 추가 기능
- **통계 화면 -** 날짜별, 수입/지출 별로 통계를 구해주는 기능
- **추가 화면 -** 카테고리 수정(추가/삭제) 기능, 삭제된 카테고리들이 저장된 가계부 조회 기능

### 3. 사용 기술

#### 3-1 백엔드

##### 통합 개발 환경 (IDE)
- Android Studio Jellyfish 2023.3.1

##### 주요 프레임워크 / 라이브러리
- Java 17
- Kotlin 1.9.0

##### Build Tool
- Gradle 8.6

##### DataBase
- Room 2.5.0
- SQLite

#### 3-2 프론트엔드
- Material 1.12.0
- Material Calendarview 2.0.1


### 4. 실행 화면
  <details>
    <summary>게시글 관련</summary>   
       
    
  **1. 게시글 전체 목록**   
  ![image](https://user-images.githubusercontent.com/59757689/156975336-c37c9866-bba2-4c69-9a3f-230339a80d5a.png)   
  전체 목록을 페이징 처리하여 조회할 수 있다.   
     
  **2. 게시글 등록**   
  ![image](https://user-images.githubusercontent.com/59757689/156975408-413151f1-3bd8-4788-bc8e-77a2ffbd6eea.png)   
  로그인 한 사용자만 새로운 글을 작성할 수 있고, 작성 후 목록 화면으로 redirect한다.   
     
  **3. 게시글 상세보기**   
  ![image](https://user-images.githubusercontent.com/59757689/156975794-9d7ef3fd-7e03-4a24-99de-d3f7a99c8167.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156975849-f3e02f34-47ed-4b7a-92f5-83ee66bed2bb.png)   
  본인이 작성한 글만 수정 및 삭제가 가능하다.   
     
   **4. 게시글 수정 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156975898-2f17bc37-df52-418e-8a84-dc17cec37070.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156975948-954960c8-987e-4364-a036-3c58cb66bbdd.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156975965-da3681c1-1a0d-4159-865a-b5c202b1f7ee.png)   
  제목과 내용만 수정할 수 있게 하고, Confirm으로 수정 여부를 확인 후 상세보기 화면으로 redirect 한다.   
  목록 버튼을 누를 시 상세보기 화면으로 돌아간다.   
  
  **5. 게시글 삭제 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976055-d6e8f6bd-9bda-4fc8-bb5f-3ea60d9f2f5d.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156976074-c27f90c8-c8e0-45b9-9d04-e3541a14b8c2.png)   
  Confirm으로 삭제할지 확인하고, 삭제 후 전체 목록 리스트 화면으로 redirect 한다.   
  
  **6. 게시글 검색 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976190-1dac1678-3cf4-4d21-9f3b-d5228b7d50ef.png)   
  검색 키워드에 포함된 글을 모두 보여준다.   
     
  **6-1. 게시글 검색 후 페이징 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976258-c4b28ef3-fd6e-4ebe-834c-d5c6bce4c02c.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156976314-c6733cb8-7aac-4502-88d4-02730f88021b.png)   
  검색된 게시글이 많을 경우 다음과 같이 페이징 처리되어 조회할 수 있다.   
     
  </details>
  <br/>   
  
  <details>
    <summary>회원 관련</summary>   
     
  **1. 회원가입 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976413-78b9e0e9-2ab1-47e0-a0cd-699ebacddb79.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156976436-fafec47f-3df3-4356-83d5-eb80e1aa2276.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156976548-3a440a6c-49d0-4e5c-9eb3-d5e3524c11b6.png)   
  회원가입 시 유효성 검사 및 중복확인을 진행하며 완료시 회원 정보를 저장하고 로그인 화면으로 이동한다.   
     
  **2. 로그인 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976619-6988837d-0dfe-4600-a63c-2e287db9c88e.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156976909-51b0d06c-502f-4e42-b0dd-516834e43efe.png)   
  로그인 실패시 어떤 이유로 실패 했는지 메시지가 나오고, 로그인에 성공하면 게시글 전체 리스트 화면으로 redirect 한다.   
     
  **2-1. OAuth 2.0 소셜 로그인 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156976991-c517d254-b4b8-4a34-99fd-2684856f2a2d.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156977007-7b44d157-f29c-4a43-9fd3-aa6b743a8fb8.png)   
  구글과 네이버 로그인이 가능하다.   
     
  **3. 회원정보 수정 화면**   
  ![image](https://user-images.githubusercontent.com/59757689/156977253-d1a4de93-da30-4adf-8634-dfe10d0635a8.png)   
  닉네임과 비밀번호만 변경할 수 있고, 변경된 닉네임이 이미 사용중일 경우 alert으로 현재 사용 중임을 알려주고,   
  완료시 게시글 전체 리스트 화면으로 redirect 한다.      
           
  </details>
  <br/>   
  
  <details>
    <summary>댓글 관련</summary>   
       
  **1. 댓글 작성 화면**   
  미로그인 사용자 화면   
  ![image](https://user-images.githubusercontent.com/59757689/156977476-37db357a-ac44-4b24-ad8c-a062d4fe99cf.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156977497-cc7fc2a7-e688-4733-b4c7-8aef4fba93e3.png)   
  댓글은 로그인 한 사용자만 달 수 있으며, 댓글 작성시 현재 페이지를 reload 한다.   
  
  **2. 댓글 수정**   
  ![image](https://user-images.githubusercontent.com/59757689/156977557-8a3dae77-9a8d-4fd3-824e-8ff22606609e.png)   
  다른 사용자는 다른 사람의 댓글을 수정/삭제할 수 없다.   
  ![image](https://user-images.githubusercontent.com/59757689/156977567-fd983777-5b04-4f57-a815-c89a59697377.png)   
  수정은 댓글 작성자만이 할 수 있다. 수정 완료 후 현재 페이지를 reload 한다.   
  
  **3. 댓글 삭제**   
  ![image](https://user-images.githubusercontent.com/59757689/156977655-8125a317-344e-4721-a836-46b36df3a3b5.png)   
  ![image](https://user-images.githubusercontent.com/59757689/156977661-5008733b-2932-4bfc-be01-60a33a093dc9.png)   
  삭제 또한 댓글 작성자만이 할 수 있다. 삭제 후 현재 페이지를 reload 한다.   
           
  </details>
  <br/>   
 
   
## 구조 및 설계   
   
### 1. 패키지 구조
   
<details>
  
<summary>패키지 구조 보기</summary>   
 

```
📦 src  
 ┣ 📂 androidTest  
 ┣ 📂 main  
 ┃ ┣ 📂 java  
 ┃ ┃ ┗ 📂 com.kei037.pay_breeze_mvc  
 ┃ ┃ ┃ ┗ 📂 data  
 ┃ ┃ ┃ ┃ ┗ 📂 db  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 dao  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryDao  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 TransactionDao  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 entity  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryEntity  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 TransactionEntity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 AppDatabase  
 ┃ ┃ ┃ ┃ ┗ 📂 repository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryRepository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 TransactionRepository  
 ┃ ┃ ┃ ┃ ┗ 📜 MyApplication  
 ┃ ┃ ┃ ┗ 📂 ui  
 ┃ ┃ ┃ ┃ ┗ 📂 addition  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionFragment  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AdditionRepository  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomChip  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 RegisterBottomSheetFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 analysis  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AnalysisFragment  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 AnalysisPageAdapter.kt  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 ExpenseTransactionsFragment  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 IncomeTransactionsFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 calender  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 calenderAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 ListItem.kt  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 ViewType  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 dacorators  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 MiddleDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 SingleDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 StartEndDateDecorator  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 CalenderFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 commons  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 commonsAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CategoryAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 EventAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 DetailedActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 EditActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 EditCategoryActivity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 utils.kt  
 ┃ ┃ ┃ ┃ ┗ 📂 home  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 homeAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 HomeItem  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 HomeFragment  
 ┃ ┃ ┃ ┃ ┗ 📂 setting  
 ┃ ┃ ┃ ┃ ┃ ┗ 📂 settingAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomCategoryAdapter  
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ 📜 HistoricalTransAdapter  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 TransactionDiffCallback  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 CustomCategoryActivity  
 ┃ ┃ ┃ ┃ ┃ ┣ 📜 HistoricalTransActivity  
 ┃ ┃ ┃ ┃ ┃ ┗ 📜 SettingFragment  
 ┃ ┃ ┃ ┗ 📜 MainActivity  
 ┃ ┗ 📂 res  
 ┃ ┗ 📜 AndroidManifest.xml  

 ```
  
 </details>   
 <br/>    
   
     
 ### 2. DB 설계

![DB설계 및 구성](https://github.com/user-attachments/assets/90cff346-c1ab-472d-9997-9e99750ca404)
   
<br/>

### 3. API 설계

![게시글 관련 API 설계](https://user-images.githubusercontent.com/59757689/156749365-5e4cee67-1431-4e3a-9140-7b58b6e1fd53.PNG)    
![회원 관련 API 설계 (2)](https://user-images.githubusercontent.com/59757689/148911411-0cfb65ee-5782-4f04-a7c9-7dcc84abfed8.PNG)   
![댓글 관련 API 설계v2](https://github.com/hojunnnnn/board/assets/59757689/fa9032f0-3ce1-4ec4-9dbd-f420fb4e6152)  

## 개발 내용

- <a href="https://dev-coco.tistory.com/111" target="_blank">게시판 프로젝트 명세서 정리</a>
- <a href="https://dev-coco.tistory.com/113" target="_blank">게시판 조회수 기능 추가</a>
- <a href="https://dev-coco.tistory.com/114" target="_blank">게시판 페이징 처리 구현</a>
- <a href="https://dev-coco.tistory.com/115" target="_blank">게시판 검색처리 및 페이징 구현</a>
- <a href="https://dev-coco.tistory.com/117" target="_blank">생성, 수정시간 LocalDateTime format 변경</a>
- <a href="https://dev-coco.tistory.com/120" target="_blank">Security 회원가입 및 로그인 구현</a>
- <a href="https://dev-coco.tistory.com/121" target="_blank">Security Mustache CSRF 적용 및 문제해결</a>
- <a href="https://dev-coco.tistory.com/122" target="_blank">커스텀 어노테이션을 통해 중복코드 개선</a>
- <a href="https://dev-coco.tistory.com/124" target="_blank">회원가입 Validation 유효성 검사</a>
- <a href="https://dev-coco.tistory.com/125" target="_blank">회원가입 Validation 커스터마이징 중복 검사</a>
- <a href="https://dev-coco.tistory.com/126" target="_blank">Security 로그인 실패시 메시지 출력하기</a>
- <a href="https://dev-coco.tistory.com/127" target="_blank">Security 회원정보 수정(ajax)</a>
- <a href="https://dev-coco.tistory.com/128" target="_blank">OAuth 2.0 구글 로그인 구현</a>
- <a href="https://dev-coco.tistory.com/129" target="_blank">OAuth 2.0 네이버 로그인 구현</a>
- <a href="https://dev-coco.tistory.com/130" target="_blank">JPA 연관관계 매핑으로 글 작성자만 수정, 삭제 가능하게 하기</a>
- <a href="https://dev-coco.tistory.com/133" target="_blank">JPA 양방향 순환참조 문제 및 해결</a>
- <a href="https://dev-coco.tistory.com/132" target="_blank">게시판 댓글 작성 및 조회 구현</a>
- <a href="https://dev-coco.tistory.com/134" target="_blank">게시판 댓글 수정 및 삭제 구현</a>
- <a href="https://dev-coco.tistory.com/136" target="_blank">게시판 댓글 작성자만 수정, 삭제 가능하게 하기</a>
- <a href="https://dev-coco.tistory.com/138" target="_blank">[리팩토링]Dto Class를 Inner Class로 한번에 관리하기</a>

## 마치며   
### 1. 프로젝트 보완사항   

초기에 구상한 기능은 기본적인 CRUD 즉, 게시판에 올라오는 게시글을 대상으로 Create, Read, Update, Delete가 가능한 게시판이었습니다.   
템플릿 엔진으로 Mustache를 선택했는데, 그 이유는 Mustache는 단순히 화면에 데이터를 렌더링 하는 엔진이고   
Logic-less 하기 때문에 View의 역할과 서버의 역할이 명확하게 분리되어 OOP의 5원칙 중 하나인 SRP를 지킬 수 있어    
MVC 설계에서 Model, View, Controller의 역할에 대한 구분도 명확하게 할 수 있겠다는 생각이 들었습니다.   
또한, 다른 템플릿에 비해 빠른 로딩 속도를 자랑하며, xss를 기본적으로 이스케이프 할 수 있다는 장점들에 이끌려 Mustache를 사용하게 되었습니다.   
그러나 게시판 CRUD 기능이 완성되어 갈 때 쯤, 아쉬운 부분이 계속해서 생겨 몇몇 기능들을 추가하게 되었습니다.   
mustache는 로직을 넣을 수 없어 그 과정에 데이터를 렌더링 하기 전 서버에서 전처리를 하거나,    
화면에 표시된 후에 자바스크립트로 후처리를 해줬지만 조금 아쉬운 부분이 몇 가지 남아있다고 생각합니다.   
<details>
  <summary>보완사항</summary>
     
  
- 페이징 처리 및 검색 페이징에서 페이지 번호 활성화
- 페이지 번호는 10페이지 단위로 보여주기
- 페이지 처음, 끝으로 이동하는 버튼
- 생성, 수정시간 format 설정 varchar > datetime
- 다른 사용자와 자신의 댓글이 댓글란에 있을때 자신의 댓글만 수정,삭제 버튼 보이기
  
</details>   

추후에 브랜치를 나눠 Mustache에서 Thymeleaf로 조금씩 바꾸며 프로젝트 완성도를 높이고, 고도화 할 계획에 있습니다.   
   
   <details>
  <summary>추가할 기능 </summary>
     
  
- 댓글 페이징 처리
- 쿠키나 세션을 이용해 조회수 중복 카운트 방지
- 파일 업로드 기능 추가
- 좋아요 기능 추가
  
</details>  


### 2. 후기   

혼자 독학하며 처음 만들어본 프로젝트이기 때문에,   
공부한 내용을 사용해보는 설렘만큼이나 부족한 부분에 대한 아쉬움도 많이 남았습니다.   
효율적인 설계를 위해 고민하고 찾아보며 실제로 많이 공부할 수 있었던 부분도 많았습니다.   
책이나 블로그, 강의로 공부한 예제에서 납득했던 부분들은 실제로 코드를 짜면서 다양한 애로 사항을 마주했고   
'이 로직은 이 단계에서 처리하는게 맞는가', '각 레이어간 데이터 전달은 어떤 방식이든 DTO로 하는게 맞는가' 등   
여러 고민에 빠져 헤맨적도 있었지만, 다행히 결과는 대부분 최선을 찾았었던 것 같습니다.   
그리고 내가 만든 코드를 남에게 보여줬을 때, 누군가 코드의 근거를 물어본다면   
과연 자신 있게 나의 생각을 잘 얘기할 수 있을까 라는 생각을 굉장히 많이 하게 되었습니다.   
그래서 하나를 구현할 때 '이렇게 구현 하는 것이 과연 최선인가', '더 나은 Best Practice는 없을까'   
스스로 의심하고 고민하게 되는 습관을 가지게 되었습니다.   

두 번째로 기술적인 부분에서 더 공부하고 싶은 '방향'을 찾을 수 있었습니다.   
이번 프로젝트는 저에게 좋은 경험이 되었고, 저의 부족한 부분을 스스로 알 수 있는 좋은 계기가 되었습니다.   
부족한 부분에 대해 스스로 인지하고 있고, 더 깊게 공부하며 스스로 발전할 수 있는 '방향'을 다시한번 찾을 수 있게 되었습니다.   
이를 통해 더 나은 웹 애플리케이션을 만들 수 있을 것 같다는 자신감도 생겼습니다.   

끝까지 읽어주셔서 감사합니다.
