import pymysql
from . import makingJson as mj

def db_connect():
    """
        데이터베이스에 연결한 후
        커서를 반환한다.
    """
    f = open('databasePW.txt', 'r')
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

def split_id(d_id):
    """
        department_id를 3자리 단위로 자르고 배열로 반환하는 함수
    """
    return [d_id[i:i+3] for i in range(0, len(d_id), 3)]

def get_department_office():
    
    cursor = db_connect()
    sql = "select * from table_department"
    cursor.execute(sql)
    data = cursor.fetchall()

    return mj.make_department_office_json(data)

def get_department_office(department_id):
    """
        department_id를 받아온 후 해당하는 학과 정보를 반환하는 함수
    """
    cursor = db_connect()
    json_data = []
    for id in split_id(department_id):
        sql = "select * from table_department where id=%s"
        cursor.execute(sql, (id))
        data = cursor.fetchall()

        json_data.append(mj.make_department_office_json(data))

    return json_data


