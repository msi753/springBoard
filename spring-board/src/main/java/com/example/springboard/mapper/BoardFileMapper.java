package com.example.springboard.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.springboard.vo.Boardfile;
@Mapper
public interface BoardFileMapper {
	//
	void insertBoardFile(Boardfile boardfile);
}
