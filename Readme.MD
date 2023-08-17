# 어쩌따 싸피 READEME

## 프로젝트 소개
![아이콘](asaf_real_logo.png)

    싸피생들과 프로님들의 편리한 싸피 생활을 돕기 위한 목적으로 만들어진 모바일 어플리케이션
    

## 팀원 소개

    - 팀장 : 김준성
    - 팀원 : 박현우
    - 팀원 : 최재원
    - 팀원 : 장진욱
    - 팀원 : 권민재
    - 팀원 : 김형진


## 사용 기술

### Andriod

### Spring Boot
    
### FCM

### NFC

### Beacon

### QR


## 기능 명세서

 [기능 명세서](https://docs.google.com/spreadsheets/d/1YXWEoj4FwIA5q2wiOGrWThPSlwKs1TSfE4VgIX8EKCw/edit#gid=0)

 ## ERD
 ![ERD](ERD.png)

 ## 배포 정보 

 [배포 정보](exec/1번 문서.md)

 ## 프로젝트 트리
 ├─.idea
├─backend
│  ├─.idea
│  └─ASAF
│      ├─.gradle
│      │  ├─8.0
│      │  │  ├─checksums
│      │  │  ├─dependencies-accessors
│      │  │  ├─executionHistory
│      │  │  ├─fileChanges
│      │  │  ├─fileHashes
│      │  │  └─vcsMetadata
│      │  ├─buildOutputCleanup
│      │  └─vcs-1
│      ├─.idea
│      ├─build
│      │  ├─classes
│      │  │  └─java
│      │  │      └─main
│      │  │          └─com
│      │  │              └─ASAF
│      │  │                  ├─config
│      │  │                  ├─controller
│      │  │                  ├─dto
│      │  │                  ├─entity
│      │  │                  ├─repository
│      │  │                  └─service
│      │  ├─generated
│      │  │  └─sources
│      │  │      ├─annotationProcessor
│      │  │      │  └─java
│      │  │      │      └─main
│      │  │      └─headers
│      │  │          └─java
│      │  │              └─main
│      │  ├─resources
│      │  │  └─main
│      │  │      ├─META-INF
│      │  │      ├─static
│      │  │      │  └─images
│      │  │      │      ├─profile_images
│      │  │      │      └─sign_images
│      │  │      └─templates
│      │  └─tmp
│      │      └─compileJava
│      │          └─compileTransaction
│      │              ├─annotation-output
│      │              ├─compile-output
│      │              │  └─com
│      │              │      └─ASAF
│      │              │          ├─config
│      │              │          ├─controller
│      │              │          ├─dto
│      │              │          ├─entity
│      │              │          ├─repository
│      │              │          └─service
│      │              ├─header-output
│      │              └─stash-dir
│      ├─gradle
│      │  └─wrapper
│      ├─META-INF
│      └─src
│          ├─main
│          │  ├─java
│          │  │  ├─com
│          │  │  │  └─ASAF
│          │  │  │      ├─config
│          │  │  │      ├─controller
│          │  │  │      ├─dto
│          │  │  │      ├─entity
│          │  │  │      ├─repository
│          │  │  │      └─service
│          │  │  └─META-INF
│          │  └─resources
│          │      ├─META-INF
│          │      ├─static
│          │      │  └─images
│          │      │      ├─profile_images
│          │      │      └─sign_images
│          │      └─templates
│          └─test
│              └─java
│                  └─com
│                      └─ASAF
├─exec
└─frontend
    └─ASAF
        ├─.idea
        │  └─codeStyles
        ├─app
        │  ├─libs
        │  └─src
        │      ├─androidTest
        │      │  └─java
        │      │      └─com
        │      │          └─d103
        │      │              └─asaf
        │      ├─main
        │      │  ├─java
        │      │  │  └─com
        │      │  │      └─d103
        │      │  │          └─asaf
        │      │  │              ├─common
        │      │  │              │  ├─component
        │      │  │              │  │  └─adapter
        │      │  │              │  ├─config
        │      │  │              │  ├─model
        │      │  │              │  │  ├─api
        │      │  │              │  │  ├─dto
        │      │  │              │  │  └─Room
        │      │  │              │  ├─splash
        │      │  │              │  └─util
        │      │  │              └─ui
        │      │  │                  ├─bus
        │      │  │                  ├─home
        │      │  │                  │  ├─pro
        │      │  │                  │  └─student
        │      │  │                  ├─join
        │      │  │                  ├─library
        │      │  │                  │  ├─adapter
        │      │  │                  │  ├─pro
        │      │  │                  │  └─student
        │      │  │                  ├─login
        │      │  │                  ├─market
        │      │  │                  ├─noti
        │      │  │                  ├─op
        │      │  │                  │  ├─adapter
        │      │  │                  │  └─dialog
        │      │  │                  ├─schedule
        │      │  │                  ├─setting
        │      │  │                  └─sign
        │      │  ├─jniLibs
        │      │  │  ├─arm64-v8a
        │      │  │  ├─armeabi
        │      │  │  └─armeabi-v7a
        │      │  └─res
        │      │      ├─drawable
        │      │      ├─drawable-v24
        │      │      ├─font
        │      │      ├─layout
        │      │      ├─menu
        │      │      ├─mipmap-anydpi-v26
        │      │      ├─mipmap-hdpi
        │      │      ├─mipmap-mdpi
        │      │      ├─mipmap-xhdpi
        │      │      ├─mipmap-xxhdpi
        │      │      ├─mipmap-xxxhdpi
        │      │      ├─navigation
        │      │      ├─raw
        │      │      ├─values
        │      │      ├─values-night
        │      │      └─xml
        │      └─test
        │          └─java
        │              └─com
        │                  └─d103
        │                      └─asaf
        └─gradle
            └─wrapper


