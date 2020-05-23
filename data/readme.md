1. database
1-1. 생성
use 데이터베이스명 으로 생성한다. 이미 있는 경우엔 현존하는 데이터베이스를 사용한다. 1개 이상의 Collection이 있어야 데이터베이스 리스트에서 보인다.

1-2. 조회
db : 현재 사용중인 데이터베이스 확인
show dbs : 데이터베이스 리스트 확인
db.stats() : 데이터베이스 상태확인
1-3. 제거
db.dropDatabase() 로 데이터베이스를 제거한다. use로 해당 데이터베이스에 스위치하고선 실행해야한다.



2. collection
2-1. 생성
db.createCollection(name, [options]) 으로 컬렉션을 생성한다. name은 컬렉션이름이고, options은 document 타입으로 구성된 해당 컬렉션의 설정값이다.

options 객체의 속성들은 아래와 같다.

capped : Boolean타입이다. 이 값을 true로 설정하면 capped collection을 활성화 시킨다. Capped collection 이란 고정된 크기(fixed size)를 가진 컬렉션으로서, size가 초과되면 가장 오래된 데이터를 덮어쓴다. 이 값을 true로 설정하면 size 값을 꼭 설정해야 한다.
autoIndex : Boolean타입이다. 이 값을 true로 설정하면, _id 필드에 index를 자동으로 생성한다. 기본값은 false이다. 곧 deprecated 될 예정이므로 쓰지 말자.
size : number타입이다. Capped collection을 위해 해당 컬렉션의 최대 사이즈를 ~bytes로 지정한다.
max : number타입이다. 해당 컬렉션에 추가 할 수 있는 최대 document 갯수를 설정한다.
예를들면 아래와 같이 컬렉션을 생성할 수 있다.

> db.createCollection("platform", {
	capped: true,
	size: 6142800,
	max: 10000
	})
{ "ok" : 1 }
2-2. 조회
show collections 으로 컬렉션 리스트를 확인할 수 있다.

2-3. 제거
db.컬렉션명.drop() 으로 컬렉션을 제거한다.

2-4. 유틸
db.OLD컬렉션명.renameCollection("NEW컬렉션명") : 이름변경

3. document
어떤 따옴표를 쓰던, 생략하던 데이터베이스에는 큰따옴표로 보이는 듯하다.

3-1. 생성
db.컬렉션명.insert(document) 로 document를 추가한다. 배열형식으로 전달하면 여러 document를 bulk형식으로 추가할 수 있다.

> db.books.insert([
	{"language": "java", "level": 5},
	{"language": "ruby", "framework": "rails"}
	]);
BulkWriteResult({
    "writeErrors" : [ ],
    "writeConcernErrors" : [ ],
    "nInserted" : 2,
    "nUpserted" : 0,
    "nMatched" : 0,
    "nModified" : 0,
    "nRemoved" : 0,
    "upserted" : [ ]
})
3-2. 조회
db.컬렉션명.find([query, projection]) 로 컬렉션의 document 리스트를 확인할 수 있다.
한 줄이 너무 길어 불편할 때는 끝에 .pretty()를 붙이면 json이 이쁘게 나온다.
매개변수로 아래와 같은 것이 들어갈 수 있다.

query : document타입이다. Optional이며, document를 조회할 때 기준을 정한다. 기준이 없이 컬렉션에 있는 모든 document를 조회할때는 이 매개변수를 비우거나, { } 를 전달하면 된다. 연산자는 4번을 참고하자.
projection : document타입이다. Optional이며, document를 조회할 때 보여질 field를 정한다. (ex. > db.articles.find( { } , { “_id”: false, “title”: true, “content”: true } ))
find() 는 기준값에 해당하는 document들을 선택하여 cursor를 반환한다. cursor는 query 요청의 결과를 가르키는 포인터다. cursor 객체를 통해서 보이는 데이터의 수를 제한할 수도 있고 데이터를 정렬할 수도 있다. 이 포인터는 10분동안 사용되지않으면 소멸된다. cursor 객체는 5번을 참고하자

3-3. 제거
db.컬렉션명.remove(criteria[, justOne]) 로 document를 제거할 수 있다.

매개변수로 들어가는 객체의 속성들은 아래와 같다.

criteria : document 타입이다. 데이터의 기준 값으로서 일치하면 기본적으로 다 삭제한다. 이 값이 { } 이면 컬렉션의 모든 데이터를 제거한다. 꼭 넣어야한다.
justOne boolean타입이다. Optional 매개변수이며, 이 값이 true면 1개의 document만 제거한다. 이 매개변수가 생략되면 기본값은 false이고 criteria에 해당되는 모든 document를 제거한다.

4. 쿼리 연산자
참조문서 : https://docs.mongodb.com/v3.4/reference/operator/query/



SQL처럼 MongoDB에서도 원하는 데이터를 찾기 위해 연산자를 사용할 수 있다.

4-1. 비교 연산자
$eq : (equals) 주어진 값과 일치하는 값
$gt : (greater than) 주어진 값보다 큰 값
$gte : (greather than or equals) 주어진 값보다 크거나 같은 값
$lt : (less than) 주어진 값보다 작은 값
$lte : (less than or equals) 주어진 값보다 작거나 같은 값
$ne : (not equal) 주어진 값과 일치하지 않는 값
$in : 주어진 배열 안에 속하는 값
$nin : 주어빈 배열 안에 속하지 않는 값
ex. > db.articles.find( { “likes”: { $gt: 10, $lt: 30 } } )

4-2. 논리 연산자
$or
$and
$not
$nor
ex. > db.articles.find({ $or: [ { “title”: “article01” }, { “writer”: “Alpha” } ] })

4-3. $regex 연산자
{ <field>: /pattern/<options> } 이런 형태로 정규표현식으로 쿼리를 날릴 수 있다.

options 에는 다음과 같은 것들을 쓸 수 있다.

i : 대소문자 무시
m : 정규식에서 anchor(^) 를 사용 할 때 값에 \n 이 있다면 무력화
x : 정규식 안에있는 whitespace를 모두 무시
s : dot (.) 사용 할 떄 \n 을 포함해서 매치
ex. > db.articles.find( { "title" : /article0[1-2]/ } )

4-4. $where 연산자
$where 연산자를 통하여 javascript expression 을 사용 할 수 있다.

ex. > db.articles.find( { $where: “this.comments.length == 0” } )

4-5. $elemMatch 연산자
$elemMatch 연산자는 Embedded Documents 배열을 쿼리할때 사용된다.



5. cursor
커서는 find()가 반환하는 객체이다. 3-2번 참고. 아래 함수들도 cursor를 반환하므로 중첩해서 쓸 수 있다.

5-1. 정렬
cursor.sort(document) 는 커서 객체를 정렬할때 사용한다. 매개변수로 들어가는 document는 { KEY: value } 의 구조를 가진다. key에는 데이터 필드명이 들어가고, value에는 1 또는 -1이 들어간다. 1은 오름차순이고 -1은 내림차순이다. key에 여러 값을 넣었을 경우, 먼저 입력한 key가 우선순위가 높다.

ex. > db.orders.find().sort( { "_id": 1 } )

5-2. 출력갯수 제한
cursor.limit(value) 로 출력할 데이터 갯수를 제한할 수 있다. value에는 출력할 개수를 넣어준다.

ex. > db.orders.find().limit(3)

5-3. 출력시작할 부분 설정
cursor.skip(value) 로 출력할 데이터의 시작부분을 설정해줄 수 있다. value값 이후부터 출력한다.

ex. > db.orders.find().skip(2) 는 _id 3번부터 출력한다.



6. Index
인덱스는 MongoDB에서 데이터 쿼리를 효율적으로 할 수 있게 해준다. 인덱스가 없다면 컬렉션의 데이터를 하나하나씩 조회해서 스캔하지만, 인덱스를 사용하면 더 적은 횟수의 조회로 원하는 데이터를 찾을 수 있다. 이는 MongoDB가 인덱스에 대하여 B-Tree를 구성하기 때문이다. B-tree에서 이진탐색으로 데이터를 조회하면 O(logN)의 시간복잡도를 가지기 때문에 빠르다.

6-1. 기본인덱스 _id
모든 MongoDB의 컬렉션은 기본적으로 _id 필드에 인덱스가 존재한다. 만약에 컬렉션을 만들 때 _id 필드를 따로 지정하지 않으면 mongod 드라이버가 자동으로 _id 필드 값을 ObjectId로 설정해준다.

6-2. 다양한 인덱스 지원
Single Field Index : 기본적인 인덱스 타입, 설정안하면 default로 _id
Compound Index : RDBMS의 복합인덱스 같은 거, 2개이상 필드를 인덱스로 사용
Multikey Index : Array에 매칭되는 값이 하나라도 있으면 인덱스에 추가하는 멀티키 인덱스
Geospatial Index : 위치기반 인덱스와 쿼리
Text Index : String에도 인덱싱이 가능
Hashed Index : Btree 인덱스가 아닌 Hash 타입의 인덱스도 사용 가능, 검색효율이 더 좋아진다. 그러나 Btree처럼 정렬을 하지는 않는다.
6-3. 인덱스 생성
db.컬렉션명.createIndex(document[, options]) 로 인덱스를 생성한다. document에는 { Key : Value } 형식으로 넣어주면 되고, value가 1이면 오름차순, -1이면 내림차순이다.

options 객체에는 다음과 같은 속성들이 있다.

unique : boolean타입이다. 컬렉션에 단 한개의 값만 존재할 수 있다. (ex. db.userinfo.createIndex( { email: 1 }, { unique: true } ))
partialFilterExpression : document타입이다. 조건을 정하여 일부 document에만 인덱스를 정할 때 사용된다. (ex. db.store.createIndex( { name: 1 }, { partialFilterExpression: { visitors: { $gt: 1000 } } }))
expireAfterSeconds : integer타입이다. Date타입에 적용하며 N초 후에 document를 제거시킬 수 있다. (ex. db.notifications.createIndex( { "notifiedDate": 1 }, { expireAfterSeconds: 3600 } )) 만료되는 document를 제거하는 쓰레드는 60초마다 실행되기 때문에 제거시간은 정확하지 않다.
6-4. 인덱스 조회
db.컬렉션명.getIndexes() 로 생성된 인덱스를 확인할 수 있다.

6-5. 인덱스 제거
db.컬렉션명.dropIndex(document) 로 제거한다



출처: https://sjh836.tistory.com/100 [빨간색코딩]
