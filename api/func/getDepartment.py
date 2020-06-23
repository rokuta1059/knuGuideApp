from . import departmentnotice as dn
from . import makingJson as mj
import pymysql
from datetime import datetime

"""
    학과 이름을 받아온 후
    해당하는 학과에 맞는 함수를 호출한다.
"""

def sort_notice(data):
    return sorted(data, key=lambda x: (x[1] == "공지", datetime.strptime(x[3], '%Y-%m-%d')), reverse=True)

def split_id(d_id):
    """
        department_id를 3자리 단위로 자르고 배열로 반환하는 함수
    """
    return [d_id[i:i+3] for i in range(0, len(d_id), 3)]

def db_connect():
    """
        데이터베이스에 연결한 후
        커서를 반환한다.
    """
    f = open('func/databasePW.txt', 'r')
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


def get_department_func(department_id):

    """
        해당하는 학과의 id를 받아온 후 데이터베이스를 검색하여
        해당 학과 주소에 맞게 크롤링한다.
        크롤링이 완료되면 JSON 형태로 반환한다.
        - department_id: 크롤링하려는 학과의 id번호
        - return: 완성된 JSON 형태의 공지사항
    """

    cursor = db_connect()
    notice_data = []

    for id in split_id(department_id):
        sql = "select * from table_department where id=%s"
        cursor.execute(sql, (id))
        data = cursor.fetchall()

        for row in data:

            if row['crf'] == 'cba':
                notice_data.extend(
                    dn.cba(row['department'], dn.callreq('', row['site']), 'h1', "req")
                )
            elif row['crf'] == 'biz':
                notice_data.extend(
                    dn.biz(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'cll':
                notice_data.extend(
                    dn.cll(row['department'], dn.callreq('', row['site']))
                )
            elif row['crf'] == 'dbe':
                notice_data.extend(
                    dn.dbe(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'archi':
                notice_data.extend(
                    dn.archi(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'edu':
                notice_data.extend(
                    dn.edu(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'social':
                if row['department'] in ['부동산학과', '사회학과']:
                    notice_data.extend(
                        dn.social(row['department'], dn.callreq(row['site'], row['site_append']), "i")
                    )
                else:
                    notice_data.extend(
                        dn.social(row['department'], dn.callreq(row['site'], row['site_append']), "")
                    )
            elif row['crf'] == 'masscom':
                notice_data.extend(
                    dn.masscom(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'it':
                notice_data.extend(
                    dn.it(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'physics':
                notice_data.extend(
                    dn.physics(row['department'], dn.callreq(row['site'], row['site_append']))
                ) # 공용함수의 끝
            elif row['crf'] == 'account':
                notice_data.extend(
                    dn.account(row['department'], dn.callreq('', row['site']))
                )
            elif row['crf'] == 'itb':
                notice_data.extend(
                    dn.itb(row['department'], dn.callurl('', row['site']))
                )
            elif row['crf'] == 'agrilifesci':
                notice_data.extend(
                    dn.agrilifesci(row['department'])
                )
            elif row['crf'] == 'architecture':
                notice_data.extend(
                    dn.architecture(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'art':
                notice_data.extend(
                    dn.art(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'educatio':
                notice_data.extend(
                    dn.educatio(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'kedu':
                notice_data.extend(
                    dn.kedu(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'engedu':
                notice_data.extend(
                    dn.engedu(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'geoedu':
                notice_data.extend(
                    dn.geoedu(row['department'], dn.callreq(row['site'], ''))
                )
            elif row['crf'] == 'homecs':
                notice_data.extend(
                    dn.homecs(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'mathedu':
                notice_data.extend(
                    dn.mathedu(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'politics':
                notice_data.extend(
                    dn.politics(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'padm':
                notice_data.extend(
                    dn.padm(row['department'], dn.callreq('', row['site']))
                )
            elif row['crf'] == 'humanities':
                notice_data.extend(
                    dn.humanities(row['department'], dn.callreq('', row['site']))
                )
            elif row['crf'] == 'korean':
                notice_data.extend(
                    dn.korean(row['department'], dn.callreq(row['site'], row['site_append']))
                )
            elif row['crf'] == 'france':
                notice_data.extend(
                    dn.france(row['department'], dn.callreq(row['site'], row['site_append']))
                )
    
    return mj.make_notice_json(sort_notice(notice_data))

# def getDepartmentFunc(department):
#     if department == "컴퓨터공학과":
#         return dn.departmentComputerNotice()
#     elif department == "경영학전공":
#         return dn.departmentBizNotice()

if __name__ == '__main__':
    get_department_func("AAA")