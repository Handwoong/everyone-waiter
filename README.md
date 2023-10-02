# 모두의 웨이터

<img width="1281" alt="image" src="https://github.com/Handwoong/everyone-waiter/assets/95131477/66790928-117b-413a-9be8-71785175565e">
<br/>
<br/>
매장의 웨이팅을 관리하고 손님이 테이블에서 태블릿을 통해 메뉴를 주문하고 카운터에서 주문/결제를 관리할 수 있는 서비스입니다.

## 목차

- [모두의 웨이터](#모두의-웨이터)
    - [목차](#목차)
    - [서비스 개요](#서비스-개요)
    - [서비스 시연](#서비스-시연)
    - [기능 소개](#기능-소개)
    - [개발 환경](#개발-환경)
    - [ERD](#erd)
    - [프로젝트 구조](#프로젝트-구조)

## 서비스 개요

요즘 많은 식당이 인건비 절감을 위해 웨이팅 관리 및 테이블 오더 서비스를 도입하고 있습니다.<br/>
하지만 웨이팅 관리와 테이블 오더 서비스를 같이 하는 서비스는 찾기 힘들며, 사장님은 웨이팅 관리 업체와 계약, 테이블 오더 업체와 계약을 해야합니다.<br/>
두 서비스 업체의 약정을 관리해야하는 불편함과 이중으로 부과되는 비용으로 두 서비스를 하나의 서비스로 관리가 필요하게 되었습니다.<br/>
**모두의 웨이터**는 웨이팅 관리와 테이블 오더 서비스를 하나의 서비스로 관리합니다.<br/>
타 서비스 **장비 대여비용 및 관리비용 1500만원 -> 300만원**으로 비용을 $\frac{1}{5}$로 절감 할 수 있습니다.<br/>
또한 주말 기준 홀 인원 3인 -> 2인으로 1인 구인효과로 **매달 약 800,000원**의 인건비를 절감 할 수 있습니다.<br/>

## 서비스 시연

**주문**<br/>
<br/>
<img src="https://github.com/Handwoong/everyone-waiter/blob/eebd4f33f246f212404313ef782ec398dd9ca64d/docs/%EC%A3%BC%EB%AC%B8.gif">

**결제**<br/>
<br/>
<img src="https://github.com/Handwoong/everyone-waiter/blob/eebd4f33f246f212404313ef782ec398dd9ca64d/docs/%EA%B2%B0%EC%A0%9C.gif">

## 기능 소개

[기능 소개 WIKI](https://github.com/Handwoong/everyone-waiter/wiki/Introduce)<br/>
[업데이트 내역 WIKI](https://github.com/Handwoong/everyone-waiter/wiki/Release)

## 개발 환경

**백엔드**

- OpenJDK 11.0.18
- SpringBoot 2.6.15
- QueryDsl 5.0.0
- Thymeleaf 3.0.15

**DB**

- MySQL 8.0.33
- Redis 7.0.11

## ERD

![diagram](https://github.com/Handwoong/everyone-waiter/assets/95131477/12541fc8-e3ed-45db-9712-69e5319efa6e)

## 프로젝트 구조

```
everyone-waiter
├─ .gitignore
├─ build.gradle
├─ README.md
├─ gradle
├─ gradlew
├─ gradlew.bat
├─ settings.gradle
└─ src
   ├─ main
   │  └─ kotlin
   │     └─ com
   │        └─ handwoong
   │           └─ everyonewaiter
   │              ├─ EveryoneWaiterApplication.kt
   │              ├─ config
   │              │  ├─ aop
   │              │  ├─ message
   │              │  │  ├─ MessageServiceConfig.kt
   │              │  │  └─ template
   │              │  ├─ querydsl
   │              │  ├─ security
   │              │  └─ socket
   │              ├─ controller
   │              │  ├─ BasicController.kt
   │              │  ├─ member
   │              │  ├─ menu
   │              │  ├─ order
   │              │  ├─ payment
   │              │  ├─ receipt
   │              │  ├─ store
   │              │  └─ waiting
   │              ├─ domain
   │              │  ├─ BaseEntity.kt
   │              │  ├─ category
   │              │  ├─ member
   │              │  ├─ menu
   │              │  ├─ order
   │              │  ├─ payment
   │              │  ├─ receipt
   │              │  ├─ store
   │              │  └─ waiting
   │              ├─ dto
   │              │  ├─ category
   │              │  ├─ member
   │              │  ├─ menu
   │              │  ├─ order
   │              │  ├─ payment
   │              │  ├─ receipt
   │              │  ├─ socket
   │              │  ├─ store
   │              │  └─ waiting
   │              ├─ exception
   │              ├─ repository
   │              │  ├─ category
   │              │  ├─ member
   │              │  ├─ menu
   │              │  ├─ order
   │              │  ├─ ordercall
   │              │  ├─ payment
   │              │  ├─ receipt
   │              │  ├─ store
   │              │  └─ waiting
   │              ├─ service
   │              │  ├─ category
   │              │  ├─ member
   │              │  ├─ menu
   │              │  ├─ order
   │              │  ├─ payment
   │              │  ├─ receipt
   │              │  ├─ store
   │              │  └─ waiting
   │              └─ util
   └─ test
```
