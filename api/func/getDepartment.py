from . import departmentnotice as dn
from . import crawling as cw
from . import makingJson as mj
import pymysql

"""
    학과 이름을 받아온 후
    해당하는 학과에 맞는 함수를 호출한다.
"""

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


def get_department_func(department_id):

    """
        해당하는 학과의 id를 받아온 후 데이터베이스를 검색하여
        해당 학과 주소에 맞게 크롤링한다.
        크롤링이 완료되면 JSON 형태로 반환한다.
        - department_id: 크롤링하려는 학과의 id번호
        - return: 완성된 JSON 형태의 공지사항
    """

    cursor = db_connect()
    sql = "select * from table_department where id=%s"
    cursor.execute(sql, (department_id))
    data = cursor.fetchall()

    for row in data:

        if row['crf'] == 'cba':
            return mj.make_notice_json(
                row['department'], dn.cba(dn.callreq('', row['site']), 'h1', "req")
            )
        elif row['crf'] == 'biz':
            return mj.make_notice_json(
                row['department'], dn.biz(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'cll':
            return mj.make_notice_json(
                row['department'], dn.cll(dn.callreq('', row['site']))
            )
        elif row['crf'] == 'dbe':
            return mj.make_notice_json(
                row['department'], dn.dbe(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'archi':
            return mj.make_notice_json(
                row['department'], dn.archi(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'edu':
            return mj.make_notice_json(
                row['department'], dn.edu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'social':
            if row['department'] in ['부동산학과', '사회학과']:
                return mj.make_notice_json(
                    row['department'], dn.social(dn.callreq(row['site'], row['site_append']), "i")
                )
            else:
                return mj.make_notice_json(
                    row['department'], dn.social(dn.callreq(row['site'], row['site_append']), "")
                )
        elif row['crf'] == 'masscom':
            return mj.make_notice_json(
                row['department'], dn.masscom(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'it':
            return mj.make_notice_json(
                row['department'], dn.it(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'physics':
            return mj.make_notice_json(
                row['department'], dn.physics(dn.callreq(row['site'], row['site_append']))
            ) # 공용함수의 끝
        elif row['crf'] == 'agrilifesci':
            return mj.make_notice_json(
                row['department'], dn.agrilifesci()
            )
        elif row['crf'] == 'architecture':
            return mj.make_notice_json(
                row['department'], dn.architecture(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'art':
            return mj.make_notice_json(
                row['department'], dn.art(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'educatio':
            return mj.make_notice_json(
                row['department'], dn.educatio(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'kedu':
            return mj.make_notice_json(
                row['department'], dn.kedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'engedu':
            return mj.make_notice_json(
                row['department'], dn.engedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'geoedu':
            return mj.make_notice_json(
                row['department'], dn.geoedu(dn.callreq(row['site'], ''))
            )
        elif row['crf'] == 'homecs':
            return mj.make_notice_json(
                row['department'], dn.homecs(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'mathedu':
            return mj.make_notice_json(
                row['department'], dn.mathedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'politics':
            return mj.make_notice_json(
                row['department'], dn.politics(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'padm':
            return mj.make_notice_json(
                row['department'], dn.padm(dn.callreq('', row['site']))
            )
        elif row['crf'] == 'humanities':
            return mj.make_notice_json(
                row['department'], dn.humanities(dn.callreq('', row['site']))
            )
        elif row['crf'] == 'korean':
            return mj.make_notice_json(
                row['department'], dn.korean(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] == 'france':
            return mj.make_notice_json(
                row['department'], dn.france(dn.callreq(row['site'], row['site_append']))
            )
        else:
            return

# def getDepartmentFunc(department):
#     if department == "컴퓨터공학과":
#         return dn.departmentComputerNotice()
#     elif department == "경영학전공":
#         return dn.departmentBizNotice()
