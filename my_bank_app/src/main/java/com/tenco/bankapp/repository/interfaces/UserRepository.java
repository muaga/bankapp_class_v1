package com.tenco.bankapp.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bankapp.repository.entity.User;

@Mapper
public interface UserRepository {
	// 사용자 등록
	public int insert(User user);
	
	// 사용자 수정
	public int updateById(User user);

	// 사용자 삭제
	public int deleteById(Integer id);
	
	// 사용자 1명 조회
	public User findById(Integer id);
	
	// 사용자 전체 조회
	public List<User> findAll();
}

//mapper 프레임워크
// @Mapper를 통해 mapper의 user.xml 파일과 연결된다.
// user.xml에는 연결할 repository의 package명을 namespace에 넣는다.
