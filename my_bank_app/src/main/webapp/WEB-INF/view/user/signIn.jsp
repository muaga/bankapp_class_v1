<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

    <div class="col-sm-8">
      <h2>로그인</h2>
      <h5>어서오세요</h5>
   		<div>
    		<form action="/" method="post">
			  <div class="form-group">
			    <label for="username">username:</label>
			    <input type="text" class="form-control" placeholder="Enter username" id="username" name="username">
			  </div>
			  <div class="form-group">
			    <label for="pwd">Password:</label>
			    <input type="password" class="form-control" placeholder="Enter password" id="pwd" name="password">
			  </div>
			  <button type="submit" class="btn btn-primary">로그인</button>
			</form>
   		</div>  
    </div>
  </div>
</div>


<%@ include file="/WEB-INF/view/layout/footer.jsp" %>