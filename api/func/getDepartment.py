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
    f = open('databasePW.txt', 'r')
    user = f.readline()
    passwd = f.readline()
    host = f.readline()
    db = f.readline()
    charset = f.readline()
    conn = pymysql.connect(
        user=user, passwd=passwd, host=host, db=db, charset=charset
    )
    cursor = conn.cursor(pymysql.cursors.DictCursor)
    return cursor


def getDepartmentFunc(department_id):

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

        if row['crf'] is 'cba':
            return mj.makingNoticeJSON(
                row['department'], dn.cba(dn.callreq('', row['site']), 'h1', "req")
            )
        elif row['crf'] is 'biz':
            return mj.makingNoticeJSON(
                row['department'], dn.biz(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'cll':
            return mj.makingNoticeJSON(
                row['department'], dn.cll(dn.callreq('', row['site']))
            )
        elif row['crf'] is 'dbe':
            return mj.makingNoticeJSON(
                row['department'], dn.dbe(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'archi':
            return mj.makingNoticeJSON(
                row['department'], dn.archi(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'edu':
            return mj.makingNoticeJSON(
                row['department'], dn.edu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'social':
            if row['department'] in ['부동산학과', '사회학과']:
                return mj.makingNoticeJSON(
                    row['department'], dn.social(dn.callreq(row['site'], row['site_append']), "i")
                )
            else:
                return mj.makingNoticeJSON(
                    row['department'], dn.social(dn.callreq(row['site'], row['site_append']), "")
                )
        elif row['crf'] is 'masscom':
            return mj.makeNoticeJSON(
                row['department'], dn.masscom(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'it':
            return mj.makeNoticeJSON(
                row['department'], dn.it(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'physics':
            return mj.makeNoticeJSON(
                row['department'], dn.physics(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'agrilifesci':
            return mj.makeNoticeJSON(
                row['department'], dn.agrilifesci()
            )
        elif row['crf'] is 'architecture':
            return mj.makeNoticeJSON(
                row['department'], dn.architecture(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'art':
            return mj.makeNoticeJSON(
                row['department'], dn.art(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'educatio':
            return mj.makeNoticeJSON(
                row['department'], dn.educatio(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'kedu':
            return mj.makeNoticeJSON(
                row['department'], dn.kedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'engedu':
            return mj.makeNoticeJSON(
                row['department'], dn.engedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'geoedu':
            return mj.makeNoticeJSON(
                row['department'], dn.geoedu(dn.callreq(row['site'], ''))
            )
        elif row['crf'] is 'homecs':
            return mj.makeNoticeJSON(
                row['department'], dn.homecs(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'mathedu':
            return mj.makeNoticeJSON(
                row['department'], dn.mathedu(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'politics':
            return mj.makeNoticeJSON(
                row['department'], dn.politics(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'padm':
            return mj.makeNoticeJSON(
                row['department'], dn.padm(dn.callreq('', row['site']))
            )
        elif row['crf'] is 'humanities':
            return mj.makeNoticeJSON(
                row['department'], dn.humanities(dn.callreq('', row['site']))
            )
        elif row['crf'] is 'korean':
            return mj.makeNoticeJSON(
                row['department'], dn.korean(dn.callreq(row['site'], row['site_append']))
            )
        elif row['crf'] is 'france':
            return mj.makeNoticeJSON(
                row['department'], dn.france(dn.callreq(row['site'], row['site_append']))
            )
        else:
            return

# def getDepartmentFunc(department):
#     if department == "컴퓨터공학과":
#         return dn.departmentComputerNotice()
#     elif department == "경영학전공":
#         return dn.departmentBizNotice()
