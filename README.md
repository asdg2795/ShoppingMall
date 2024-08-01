-- 프로젝트  개발 환경
 운영체제 : Window
 통합개발환경 : IntelliJ
 JDK 버전 : JDK 17
 스프링 부트 버전 : 3.3.0
 데이터베이스 : MySQL
 빌드 툴 : Gradle
 관리 툴 : Git

-- 프로젝트 기술 스택
 프론트엔드 : HTML, CSS, JS, BootStrap, Thymeleaf
 백엔드 : Spring Boot, Spring Security6, Spring Data JPA
 데이터베이스 : Hibernate, MySQL

-- 프로젝트 구현 기능
 회원(Member) : 회원가입 / 로그인 및 로그아웃
 상품(Item) : 상품 등록 / 상품 관리 / 상품 수정 / 상품 조회 (메인화면) / 상품 상세 페이지
 주문(Order) : 상품 주문 / 주문 내역 조회 / 주문 취소
 장바구니(Cart) : 장바구니 담기 / 장바구니 조회 / 장바구니 삭제 / 장바구니 상품 주문

 -- 프로젝트 DB 모델링
  ![image](https://github.com/user-attachments/assets/3aeed259-e0b3-4775-a3f3-7ca2da214719)
  member : 회원 정보 테이블
  cart : 장바구니 목록 테이블
  cart_item : 장바구니에 담긴 상품 정보 테이블
  orders : 주문 목록 테이블
  order_item : 주문된 상품 정보 테이블
  item : 상품 정보 테이블
  item_img : 상품 이미지 정보 테이블

  -- 프로젝트 API 명세서
  https://docs.google.com/spreadsheets/d/1AJ5cyir0OYHMsDImUxTAlcOVFjibPcDAn4zYqUUEtt0/edit?usp=sharing
  
