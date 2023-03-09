# Swetter

twitter를 클론 코딩 하는 항해 12기 swetter입니다.

## Team Member

| FE/BE | 이름 | Github | Blog |
| --- | --- | --- | --- |
| FE | 이현동 | https://github.com/hdlee0619 |  |
| FE | 김준형 | https://github.com/juninkorea95 |  |
| BE | 이진규 | https://github.com/vegeterianlee | https://www.notion.so/25432243bae642cf9497fc0de776f94a |
| BE | 이도경 | https://github.com/colleenInKorea | https://velog.io/@colleen_korea |
| BE | 최정환 | https://github.com/the1224 | https://hustlekr.tistory.com/ |

## Github

> FE:
https://github.com/Vegeterian-LEE/Sweetter_FE
> 

> BE:
https://github.com/Vegeterian-LEE/Sweetter-BE
## ProtoType

> Figma address:
https://www.figma.com/file/VriPcY0YzZOyyGNpAktytP/Twitter-Hifi-prototype?node-id=0-1

> ![스크린샷 2023-03-03 오후 2 07 41](https://user-images.githubusercontent.com/92035417/223990504-138b14cc-b6f6-4f29-a8de-e6b90623506b.png)
><img width="565" alt="스크린샷 2023-03-03 오후 2 08 45" src="https://user-images.githubusercontent.com/92035417/223990531-da91e91f-5bee-440f-a25c-c7ba0058f053.png">
><img width="565" alt="스크린샷 2023-03-03 오후 2 11 42" src="https://user-images.githubusercontent.com/92035417/223990714-519c1b88-eb4d-4921-9bdf-a430be467fdf.png">
><img width="565" alt="스크린샷 2023-03-03 오후 2 09 20" src="https://user-images.githubusercontent.com/92035417/223990735-f48afa49-1422-462a-8471-0e5cc34bc894.png">
><img width="565" alt="스크린샷 2023-03-03 오후 2 09 38" src="https://user-images.githubusercontent.com/92035417/223990801-6d208758-cc62-48ef-bf73-a2aeca082897.png">
><img width="565" alt="스크린샷 2023-03-03 오후 2 10 20" src="https://user-images.githubusercontent.com/92035417/223990842-a8948161-7849-403d-87f0-3a5cfc327c57.png">
><img width="565" alt="스크린샷 2023-03-03 오후 2 10 36" src="https://user-images.githubusercontent.com/92035417/223990874-d43dcb6c-c6fe-4c48-9b6f-3024d1df7eae.png">

## ERD

![image](https://user-images.githubusercontent.com/92035417/223990434-f25fd4da-8525-456d-952c-97f17507a3e5.png)


## 프로젝트 기능

### 회원가입 & 로그인 기능

> 회원 가입후 같은 정보를 가지고 있는 경우만 로그인이 가능하게 합니다.
로그인 성공후, 토큰 발급
해당 토큰이 있어야만 다른 기능을 사용할 수 있습니다.
> 

### 트윗 기능, 리트윗 기능

> 간단하게 최대 사진 4개까지 올리고, 글을 작성, 삭제가 가능하게 만들고 그 트윗을 리트윗(타인 또는 본인의 글을 그대로 가져 오는 것) 하는 기능을 구현했습니다.
리트윗이 된 글은 boolean으로 했는지 여부를 확인 할 수 있게 했습니다.
> 

### 트윗 좋아요와 댓글 좋아요 기능

> 트위터나 인스타를 보면,  가장 기억이 많이 될 것 같은 기능 이라고 생각합니다. 좋아요 여부와 갯수, 댓글 갯수를 확인 할 수 있도록 했습니다.
> 

### 북마크 기능

> SNS를 사용하다보면 혼자만 간직하고 싶은 글이 있지 않으신가요? 북마크를 사용하여 트윗을 저장하고 한 곳에서 모아보세요!
> 

### 프로필에서 다양한 방법으로 유저 또는 유저가 팔로우한 사람들의 트윗을 확인

> 트위터에서 해당 페이지마다 나오는 리스트 별로 로직을 짜서 확인 할수 있도록 만들었습니다.
좋아요를 한 리스트, 미디어가 있는 리스트, 내가 리트윗한 글과 트윗한 글 그리고 코멘트한 리스트, 내가 트윗과 리트윗한 리스트 4가지로 나누어 처리했습니다.
> 

### 트위터 홈 화면

> 간단하게 유저가 팔로우한 사람들의 리스트를 확인 할 수 있도록 합니다.  사이드 상단에 유저 검색이 가능하고 검색을 안할때 다른 유저들을 팔로우 할 수 있도록 했습니다.
> 

## FE적용 기술

### ☑ 회원가입 및 로그인 - 준형

> JWT 토큰을 통해 서버와 연결, 정규식을 통한 정보 검수, 로그인시 토큰의 정보를저 장해 전역에서 사용. (패스워드 제외)
> 

### ☑ CRUD - 준형, 현동

> 게시물을 생성하고 조회하고 삭제할 수 있습니다.
> 

### ☑ 검색 기능 - 현동

> 검색 기능 구현
> 

### ☑ 좋아요 기능 - 준형

> 좋아요기능 구현
> 

### ☑ 이미지 업로드 기능 - 현동

> base64로 받아온 이미지 파일을 formData로 서버에 전송 (S3)(사용자의 이미지가 없다면) ?
디폴트 이미지 : 사용자 이미지이미지 업로드를 통한 프로필 수정 기능
> 

### ☑ 북마크 기능 - 준형

> 게시물 북마크 기능 구현
> 

### ☑ 리트윗 기능 - 준형

> 리트윗 기능 구현
> 

### ☑ 검색 기능

> 검색 기능 구현 - 현동
> 

## BE적용 기술

### ◻ Swagger - 진규

> 프론트엔드와 정확하고 원활한 소통을 위해 스웨거를 도입하여 적용하였습니다.
> 

### ◻ Spring Security - 진규

> 사용자 인증, 인가 기능 구현을 위해 Spring Security를 사용하였습니다.
> 

### ◻ S3를 통한 이미지 업로드 - 정환

> 이미지를 업로드 하기 위해 S3를 활용하였습니다
> 

### ◻ Profile Controller, Comment Controller, Post create, delete , like - 도경

> 간단한 CRUD와 list Response 관련 내용을 통해서, 서비스를 구성했습니다.
> 

### ◻ Post Controller, UserController - 정환

> 트윗 관련 내용을 홈에서 보여주는 것과 유저관련한 서비스를 구현했습니다.
> 

### ◻ rds db 연결 - 도경

> 공통적으로 db를 사용 할 수 있게 활용 하였습니다
> 

### ◻ BookMark, Follower - 진규

> 북마크 기능과 팔로잉과 팔로워 관련한 기능을 구현했습니다.
>
