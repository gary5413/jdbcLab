package com.ispan.garylee3.bean;

import javax.sound.midi.MidiDevice.Info;

public class Student {
	private int flowID;
	private int type;
	private String iDCard;
	private String examCard;
	private String name;
	private String location;
	private int grade;
	
	
//	@Override
//	public String toString() {
//		return "Student [flowID=" + flowID + ", type=" + type + ", iDCard=" + iDCard + ", examCard=" + examCard
//				+ ", name=" + name + ", location=" + location + ", garde=" + grade + "]";
//	}
	public String toString() {
		System.out.println("===============查詢結果================");
		return info();
	}
	
	private String info() {
		return "id:"+flowID+"\n年級:"+type+"\n身分證字號:"+iDCard+"\n准考證號碼"
				+"\n學生姓名"+name+"\n區域:"+location+"\n成績:"+grade;
	}

	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Student(int flowID, int type, String iDCard, String examCard, String name, String location, int grade) {
		super();
		this.flowID = flowID;
		this.type = type;
		this.iDCard = iDCard;
		this.examCard = examCard;
		this.name = name;
		this.location = location;
		this.grade = grade;
	}
	public int getFlowID() {
		return flowID;
	}
	public void setFlowID(int flowID) {
		this.flowID = flowID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getiDCard() {
		return iDCard;
	}
	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}
	public String getExamCard() {
		return examCard;
	}
	public void setExamCard(String examCard) {
		this.examCard = examCard;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public int getGarde() {
		return grade;
	}
	public void setGarde(int garde) {
		this.grade = garde;
	}
	
}
