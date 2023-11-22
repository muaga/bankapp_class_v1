insert into user_tb(username, password, fullname, created_at) 
values ('길동', '1234', '고', now());

insert into user_tb(username, password, fullname, created_at) 
values ('둘리', '1234', '애기공룡', now());

insert into user_tb(username, password, fullname, created_at) 
values ('마이', '1234', '콜', now());

-- 기본 계좌 등록
insert into account_tb(number, password, balance, user_id, created_at)
values('1111', '1234', 1300, 1, now());

insert into account_tb(number, password, balance, user_id, created_at)
values('2222', '1234', 1100, 2, now());

insert into account_tb(number, password, balance, user_id, created_at)
values('3333', '1234', 0, 3, now());

-- 이체 내역 기록
-- 1번 계좌 잔액 : 1000원
-- 2번 계좌 잔액 : 1000원
-- 3번 계좌 잔액 : 0원

-- 1) 1번 계좌 -> 2번 계좌로 100원 이체
insert into history_tb
(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values(100, 900, 1100, 1, 2, now());

-- 2) ATM 출금, 1번 계좌 - 100원만 출금
insert into history_tb
(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values(100, 800, null, 1, null, now());

-- 3) 입금 내역, 1번 계좌 - 500원 입금
insert into history_tb
(amount, w_balance, d_balance, w_account_id, d_account_id, created_at)
values(500, null, 1300, null, 1, now());
