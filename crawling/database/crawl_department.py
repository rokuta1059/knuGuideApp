import pymysql
from database.department import notice

class ZeroDataError(Exception):
    pass
class NoSiteExist(Exception):
    pass

def db_connect():
    """
        데이터베이스에 연결한 후
        커넥션을 반환한다.
    """
    f = open('account.txt', 'r')
    db_connect_table = f.read().split()
    user = db_connect_table[0]
    passwd = db_connect_table[1]
    host = db_connect_table[2]
    port = db_connect_table[3]
    db = db_connect_table[4]
    charset = db_connect_table[5]
    conn = pymysql.connect(
        user=user, passwd=passwd, port=int(port), host=host, db=db, charset=charset
    )
    f.close()
    return conn


def save_db(data_list):
    con = db_connect()
    cur = con.cursor()
    sql = f"insert ignore into notice values (%s, %s, %s, %s, %s, %s)"
    cur.executemany(sql, data_list)
    con.commit()
    con.close()


def crawling_department():
    con = db_connect()
    cur = con.cursor()
    sql = "SELECT id, site, site_append, crf FROM department"
    cur.execute(sql)
    data = cur.fetchall()

    for row in data:
        try:
            if row[1] is None:
                raise NoSiteExist()
            notice_data = []
            if row[3] == 'account':
                notice_data = notice.account(row[0], notice.callreq('', row[1]))
            elif row[3] == 'archi':
                notice_data = notice.archi(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'architecture':
                notice_data = notice.architecture(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'art':
                notice_data = notice.art(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'biz':
                notice_data = notice.biz(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'cba':
                if row[0] in ['AIC']:
                    notice_data = notice.cba(row[0], notice.callreq(row[1], ''), 'h2', 'req', '')
                else:
                    url = ''
                    if row[2] is not None:
                        url = row[2]
                    notice_data = notice.cba(row[0], notice.callreq(row[1], url), 'h1', 'req', row[1])
            elif row[3] == 'cll':
                notice_data = notice.cll(row[0], notice.callreq('', row[1]))
            elif row[3] == 'dbe':
                notice_data = notice.dbe(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'edu':
                notice_data = notice.edu(row[0], notice.callreq(row[1], row[2]), row[1])
            elif row[3] == 'engedu':
                notice_data = notice.engedu(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'france':
                notice_data = notice.france(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'geoedu':
                notice_data = notice.geoedu(row[0], notice.callreq(row[1], ''), row[1])
            elif row[3] == 'homecs':
                notice_data = notice.homecs(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'it':
                notice_data = notice.it(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'itb':
                notice_data = notice.itb(row[0], notice.callreq(row[1], row[2]))
            elif row[3] == 'kedu':
                notice_data = notice.kedu(row[0], notice.callreq(row[1], row[2]), row[1])
            elif row[3] == 'korean':
                notice_data = notice.korean(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'masscom':
                notice_data = notice.masscom(row[0], notice.callreq(row[1], row[2]), row[1])
            elif row[3] == 'mathedu':
                notice_data = notice.mathedu(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            elif row[3] == 'padm':
                notice_data = notice.padm(row[0], notice.callreq(row[1], ''))
            elif row[3] == 'physics':
                url = ''
                if row[2] is not None:
                    url = row[2]
                notice_data = notice.physics(row[0], notice.callreq(row[1], url), row[1] + url)
            elif row[3] == 'politics':
                notice_data = notice.politics(row[0], notice.callreq(row[1], row[2]), row[1])
            elif row[3] == 'social':
                if row[0] in ['AHB', 'AHC']:
                    notice_data = notice.social(row[0], notice.callreq(row[1], row[2]), row[1] + row[2], "i")
                else:
                    notice_data = notice.social(row[0], notice.callreq(row[1], row[2]), row[1] + row[2], "")
            elif row[3] == 'aed':
                notice_data = notice.biz(row[0], notice.callreq(row[1], row[2]), row[1] + row[2])
            if not notice_data:
                raise ZeroDataError()
            save_db(notice_data)
            print(f"Department id: {row[0]}, CRF: {row[3]}, Notice Count: {len(notice_data)}")
        except NoSiteExist:
            print(f'Department id: {row[0]}, CRF: {row[3]}, Data Failure, Exception: No Site Exist')
        except ZeroDataError:
            print(f'Department id: {row[0]}, CRF: {row[3]}, Data Failure, Exception: No Data Exist')
        except Exception as e:
            print(f'Department id: {row[0]}, CRF: {row[3]}, Data Failure, Exception: ', e)


if __name__ == '__main__':
    crawling_department()
