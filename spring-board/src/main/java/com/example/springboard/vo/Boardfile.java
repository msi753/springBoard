package com.example.springboard.vo;

public class Boardfile {
	private int boardfileNo;
	private int boardNo;
	private String fileName;
	private String fileExt;
	private String fileType;
	private long fileSize;
	public int getBoardfileNo() {
		return boardfileNo;
	}
	public void setBoardfileNo(int boardfileNo) {
		this.boardfileNo = boardfileNo;
	}
	public int getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
