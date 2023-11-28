<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/layout/header.jsp" %>

    <div class="col-sm-8">
      <h2>이체 페이지(인증)</h2>
      <h5>어서오세요</h5>
   		<div class="bg-light p-md-5 h-75">
    		<form action="/account/transfer" method="post">
			  <div class="form-group">
			    <label for="amount">이체 금액:</label>
			    <input type="text" class="form-control" placeholder="Enter amount" id="amount" name="amount" value="100">
			  </div>
			  <div class="form-group">
			    <label for="wAccountNumber">출금 계좌 번호:</label>
			    <input type="text" class="form-control" placeholder="Enter wAccountNumber" id="wAccountNumber" name="wAccountNumber" value="1111">
			  </div>
			  <div class="form-group">
			    <label for="dAccountNumber">이체 계좌 번호:</label>
			    <input type="text" class="form-control" placeholder="Enter dAccountNumber" id="dAccountNumber" name="dAccountNumber" value="2222">
			  </div>
			  <div class="form-group">
			    <label for="password">출금계좌 비밀번호:</label>
			    <input type="password" class="form-control" placeholder="Enter password" id="password" name="password" value="1234">
			  </div>
			  <button type="submit" class="btn btn-primary">이체하기</button>
			</form>
   		</div>  
    </div>
  </div>
</div>

<%@ include file="/WEB-INF/view/layout/footer.jsp" %>