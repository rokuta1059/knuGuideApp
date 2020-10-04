# func

## 1. 개요

api에서 사용하는 각종 데이터 전처리 등에 필요한 함수가 저장되어 있습니다.

## 2. 파일 목록

### 1. cafeteria.py

각종 **식당 관련 항목**들을 전처리하는 함수 모음

| 함수명 | 설명 | 파라메터 | 반환 |
|:-- |:-- |:-- |:-- |
|makeDietTable| 각 생활관의 오늘의 식단을 확인하고 반환한다. |domitory(기숙사의 이름)|해당 기숙사 식당의 식단표. ['아침', '점심', '저녁']|

### 2. departmentnotice.py

각종 학과의 **공지사항**을 받아오는 함수 모음

| 함수명 | 설명 |
|:-- |:-- |
| departmentComputerNotice | 컴퓨터공학과 |
| departmentBizNotice | 경영학전공 |

### 3. getDepartment.py

각각의 학과 이름을 받아온 후 학과에 맞는 함수를 호출해주는 파일