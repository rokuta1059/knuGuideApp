import pymysql
from . import makingJson as mj
# 학과 사무실 위치는 데이터베이스 구축한 다음 생성할 예정
# 임시로 파일만 만들었슴미다

def db_connect():
    """
        데이터베이스에 연결한 후
        커서를 반환한다.
    """
    f = open('./knuGuideApp/api/func/databasePW.txt', 'r')
    db_connect_table = f.read().split()
    user = db_connect_table[0]
    passwd = db_connect_table[1]
    host = db_connect_table[2]
    port = db_connect_table[3]
    db = db_connect_table[4]
    charset = db_connect_table[5]
    conn = pymysql.connect(
        user=user, passwd=passwd, host=host, db=db, charset=charset
    )
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    return cursor

def get_department_office():
    
    cursor = db_connect()
    sql = "select * from table_department"
    cursor.execute(sql)
    data = cursor.fetchall()

    return mj.make_department_office_json(data)

def get_department_office(department_id):
    
    cursor = db_connect()
    sql = "select * from table_department where id=%s"
    cursor.execute(sql)
    data = cursor.fetchall()

    return mj.make_department_office_json(data)


