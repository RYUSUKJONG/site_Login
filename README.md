# JSP를 사용한 WebSite 개발

### BbsDAO.java
+ mySQL 연결
```
try {
			String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
			String dbID = "root";
			String dbPassword = "비밀번호";
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

		} catch (Exception e) {
			e.printStackTrace();
		}
```

+ 시간을 가져오는 함수 getDate()
```
    String SQL = "SELECT NOW()";  //현재 시간을 가져오는 mySQL 문장
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  
      // 데이터베이스 간 충돌방지를 위해 각 try문에 삽입
      
			rs = pstmt.executeQuery(); //실행 결과를 rs 변수에 저장
			
			if(rs.next()) {   //값이 있을 시
				return rs.getString(1);  
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; // 데이터베이스 오류
```

+ 게시판 글 고유번호(bbsID)를 가져오기 위한 함수 getNext()
```
    String SQL = "SELECT bbsID FROM BBS ORDER BY bbsID DESC";  //bbsID 값으로 내림차순으로 정렬
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL); 
			rs = pstmt.executeQuery(); //실행 결과
			
			if(rs.next()) {    // 다음 글이 있는 경우
				return rs.getInt(1)+1;   
			}
			
			return 1;  // 첫번째 게시글인 경우
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
```
+ 게시글 작성을 위한 함수 write()
```
    String SQL = "INSERT INTO BBS VALUES (?, ?, ?, ?, ?, ?)"; //값 추가
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);  
      // 각 순서대로 값을 추가
			pstmt.setInt(1, getNext());
			pstmt.setString(2, bbsTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, bbsContent);
			pstmt.setInt(6, 1);   //available 여부(삭제 여부), 기본값 1
					
			return pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
```
--------
### main.jsp
+ 세션 값 확인 후 userID 생성
```
    String userID = null;  // userID 변수 생성
		if(session.getAttribute("userID") != null){    //받아온 세션 값이 NULL값이 아닐 경우 (로그인을 한 경우)
			userID = (String) session.getAttribute("userID");   //세션을 String으로 형 변환 후 userID에 대입
		}
```


+ 이미지를 가져온 후 Slide 효과
```
  <div id="myCarousel" class="carousel" data-ride="carousel"> 
  //Carousel을 통해 슬라이드 효과를 생성 후 좌 우 아이콘을 생성 및 동작 시킴

    <img src="images/1.jpg"> 
  
    <a class="left carousel-control" href="#myCarousel" data-slide="prev"> //이전 버튼
			  <span class="glyphicon glyphicon-chevron-left"></span>
	  </a>
  
    <a class="right carousel-control" href="#myCarousel" data-slide="next"> //다음 버튼
		  	<span class="glyphicon glyphicon-chevron-right"></span>
	  </a>
  
  </div>
```

------
### login.jsp / loginAction.jsp
+ 로그인 화면 
```
  <div class="jumbotron" style="padding-top: 20px">  //부트스트랩 jumbotron 클래스를 사용하여 틀 생성
  
  <form method="post" action="loginAction.jsp">  //form 태그를 통해 post 방식으로 loginAction에 데이터 전송
  ...
  
  //form 태그를 통한 데이터 수신(loginAction.jsp)
  <jsp:useBean id="user" class="user.User" scope="page"></jsp:useBean>
  <jsp:setProperty property="userID" name="user"/>
  <jsp:setProperty property="userPassword" name="user"/>
  
  int result = userDAO.login(user.getUserID(), user.getUserPassword()); //userDAO의 로그인 함수를 통해 로그인 및 실패 확인
```



