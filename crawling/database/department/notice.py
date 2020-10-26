import ssl
from urllib.request import urlopen
from urllib import parse
import pymysql

import requests
from bs4 import BeautifulSoup

def callreq(base, url):
    """
    일반적인 홈페이지 불러올 때 사용하는 함수
    requests.get 함수를 사용한다.
    return soup (홈페이지 소스)
    """
    headers = {'User-Agent' : 'Chrome/85.0.4183.102'}
    resp = requests.get(base+url, headers=headers)
    soup = BeautifulSoup(resp.content, "html.parser")
    return soup

def callurl(base, url):
    """
    인증서가 필요한 홈페이지를 불러올 때 사용하는 함수
    urlopen 함수를 사용한다.
    return soup (홈페이지 소스)
    """
    context = ssl._create_unverified_context()          # 인증서 생성
    resp = urlopen(base+url, context=context)
    soup = BeautifulSoup(resp.read(), "html.parser")
    return soup

def numbering(num):
    """
    num을 받아 와서 num이 비어있거나 알파벳이 들어가 있으면
    num을 공지로 바꿔주고 리턴한다.
    숫자일 경우 그대로 리턴해준다.
    return num (공지 or 숫자)
    """
    compare = num[0:1]
    if num == '':
        num = '공지'
    elif (65 <= ord(compare) <= 90) \
            or (97 <= ord(compare) <= 122):
        num = '공지'
    return num

def save_notice_to_db(data):
    con = pymysql.connect(host='localhost', port=3306, user='root', passwd='knuapp', db='knuapp', charset='utf8')
    cur = con.cursor()
    sql = f"insert ignore into notice values (%s, %s, %s, %s, %s, %s)"
    cur.executemany(sql, data)
    con.commit().close()
    print(f"Update Database: {data[0][1]} {len(data)} notice updated")

def cba(department, soup, titlesign, callsign, baseurl):
    """
    - soup : 홈페이지 소스
    - titlesign : 제목의 태그가 무엇인지 받아오는 변수
    - callsign : callreq를 사용할 것인지 callurl을 사용할 것인지 받아오는 변수
    """
    data = []
    # iframe을 발견하면 iframe 페이지를 불러온다.
    if soup.find('iframe') is not None:
        soup = callreq(baseurl, soup.find('iframe').get('src').lstrip("."))
    keyword = soup.find('td', "td_subject").parent

    while keyword is not None:
        url = keyword.find('td', "td_subject").a.get('href')
        # 숫자 or 공지를 받아올 변수
        if titlesign == 'h1':
            number = numbering(keyword.find('td', 'td_num').text.strip())
        elif titlesign == 'h2':
            number = numbering(keyword.find('td', 'td_num2').text.strip())

        if callsign == "url":
            soup = callurl('', url)
        elif callsign == "req":
            soup = callreq('', url)
        # 게시글의 제목을 받아올 변수
        title = soup.find(titlesign, id="bo_v_title").text.strip()
        # 게시글의 게시 날짜를 받아올 변수
        date = soup.find('section', id="bo_v_info")
        if titlesign == 'h1':
            date = "20" + date.find_all('strong')[1].text.split(" ")[0].strip()
        elif titlesign == 'h2':
            date = "20" + date.find('strong', 'if_date').text.split(" ")[1].strip()
        # 하이퍼링크를 받아올 변수
        link = keyword.a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def account(department, soup):
    data = []
    keyword = soup.find('table', "table table-hover").tbody.tr

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        link = keyword.a.get('href')
        # 게시글을 받아오는 작업
        # 공지사항 페이지에 내용이 부실한 경우 게시글에 들어가서 직접 데이터를 가져옴
        # ex) 날짜가 없거나 부실한 경우, 태그의 속성이나 이름이 없어 제목에 접근하기 힘든 경우,
        #     공지사항 페이지는 다른데 게시글이 유사한 학과들을 통합하기 위한 경우 등
        resp = requests.get(link)
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('h4', "subject").text.strip().rstrip("new").strip()
        date = soup.find('div', "desc").find_all('strong')[1].text.split(" ")[0]
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def archi(department, soup, url):
    data = []
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling is not None:
        number = numbering(keyword.find('td').text.strip())
        title = keyword.a.text.strip()
        date = keyword.a.parent
        for i in range(0, 4):
            date = date.next_sibling
        link = url + keyword.a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['BID'][0]

        data.append([id, department, number, title, date.text, link])

        keyword = keyword.next_sibling

    return data

def architecture(department, soup, url):
    data = []
    keyword = soup.find('td', "left").parent

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        link = url+keyword.a.get('href')
        title = title.text
        id = parse.parse_qs(parse.urlparse(link).query)['idx'][0]

        data.append([id, department, number, title, date.text, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def art(department, soup, url):
    data = []
    keyword = soup.find('tr', "kboard-list-notice")

    while keyword is not None:
        number = numbering(keyword.td.text.strip()).rstrip("사항")
        title = keyword.find('div', "cut_strings").a
        date = keyword.find('td', "kboard-list-date").text.replace(".", "-", 2)
        link = url+title.get('href')
        title = title.text.strip()
        id = parse.parse_qs(parse.urlparse(link).query)['uid'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def biz(department, soup, url):

    data = []
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        number = numbering(keyword.find('td', 'num').text.strip())
        title = keyword.find_all('a')
        length = len(title)
        date = keyword.find('td', "datetime").text.replace(".", "-", 2)
        # 앞에 20을 붙여주어 연도를 완성시킨다.
        # ex) 20.01.01 -> 2020.01.01
        if len(date) <= 9:
            date = "20"+date
        link = url+title[length-1].get('href').lstrip(".")
        title = title[length-1].text.strip()
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def cll(department, soup):
    data = []
    keyword = soup.find('td', "td_subject").parent

    while keyword is not None:
        number = numbering(keyword.find('td', 'td_num2').text.strip())
        url = keyword.find('div', "bo_tit").a.get('href')
        soup = callreq('', url)
        title = soup.find('span', "bo_v_tit").text.strip()
        date = "20"+soup.find('section', id="bo_v_info").find('strong', "if_date").text.split(" ")[1].strip()
        id = parse.parse_qs(parse.urlparse(url).query)['wr_id'][0]

        data.append([id, department, number, title, date, url])

        keyword = keyword.next_sibling.next_sibling

    return data

def dbe(department, soup, url):
    data = []
    keyword = soup.find('td', 'text-center hidden-xs').parent

    while keyword is not None:
        if keyword.find('td') != -1:
            number = numbering(keyword.find('td', 'text-center hidden-xs').text.strip())
            array = keyword.find_all('td')
            title = array[1].a.text.strip()
            date = array[3].text.replace("/", "-", 2)
            link = url+array[1].a.get('href')
            id = parse.parse_qs(parse.urlparse(array[1].a.get('href')).query)['bbs_data'][0]

            data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def edu(department, soup, url):

    data = []
    keyword = soup.find('iframe')
    soup = callreq(url, keyword.get('src').lstrip("."))

    arr = soup.find_all('td', align="left")
    number = soup.find_all('tr', align="center")
    for i in range(0, len(arr) - 2):
        number[i] = numbering(number[i].td.text.strip())
        url_sec = arr[i].a.get('href').lstrip(".")
        soup = callreq(url, url_sec)
        title = arr[i].text.strip()
        date = soup.find_all('span', "text_bold888")[1].next_sibling.next_sibling.text.split("조회")[0].strip()
        link = url + url_sec
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number[i], title, date, link])

    return data

def engedu(department, soup, url):
    data = []
    keyword = soup.find('div', id="user_board_list").tr.next_sibling.next_sibling

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        title = keyword.find('td', "title")
        date = keyword.find('td', "date").text
        link = url + title.a.get('href')
        title = title.text.strip()
        id = parse.parse_qs(parse.urlparse(link).query)['bn'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def france(department, soup, url):
    data = []
    keyword = soup.find_all('tr', align="center")

    for i in range(0, len(keyword)):
        number = numbering(keyword[i].td.text.strip())
        title = keyword[i].a
        date = "20" + keyword[i].find_all('td', "bh")[2].text
        link = url+title.get('href').lstrip(".")
        title = title.text
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

    return data

def geoedu(department, soup, url):
    data = []
    array = soup.find_all('td', "text-left")

    for i in range(0, len(array)):
        number = numbering(array[i].previous_sibling.previous_sibling.text.strip())
        title = array[i].a.text.strip()
        date = array[i].next_sibling.next_sibling.text
        # b_id의 값을 받아오기 위한 변수
        b_id = array[i].a.get('href').lstrip("javascript:ContentsView(").rstrip(");")
        link = url+"?boardID=notice&b_id="+b_id+"&mode=view&npage=1&search_opt=b_writerName&search_text="
        link = link + "&pw_check1=&pw_check2=&pw_check3=&pw_check4=&pw_check5" \
                      "=&pw_check6=&pw_check7=&pw_check8=&pw_check9=&pw_check10="

        tmp = [b_id, department, number, title, date, link]
        data.append(tmp)

    return data

def homecs(department, soup, url):
    data = []
    keyword = soup.find('tr', "title").next_sibling.next_sibling

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        title = keyword.a.text
        date = keyword.find_all('td', "list_eng3")[1].text.replace("·", "-", 2)
        link = url+keyword.a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['no'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def it(department, soup, url):
    data = []
    keyword = soup.find('table', "bbs_list").tbody.tr

    while keyword.next_sibling is not None:
        number = numbering(keyword.td.text.strip())
        title = keyword.find('td', "tit").a
        date = keyword.find_all('td')
        date = date[len(date)-2].text
        link = url+title.get('href')
        title = title.text.strip()
        id = parse.parse_qs(parse.urlparse(link).query)['BID'][0]

        tmp = [id, department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling

    return data

def itb(department, soup):
    data = []
    keyword = soup.find('table', "table table-hover text-center bottom-3b").tbody.tr

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        link = keyword.a.get('href')
        soup = BeautifulSoup(requests.get(link).content, "html.parser")
        title = soup.find('h4', id="bo_v_title").b.text
        date = "20" + soup.find('ul', "list-inline").li.next_sibling.next_sibling.b.text.split(" ")[0]
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def kedu(department, soup, baseurl):
    data = []
    soup = callreq(baseurl, soup.find('div', id="con").find('iframe').get('src'))
    array = soup.find_all('td', "text_03")
    for i in array:
        title = i
        date = i
        for _ in range(4):
            title = title.previous_sibling
            date = date.next_sibling
        number = title
        for _ in range(4):
            number = number.previous_sibling
        number = numbering(number.text.strip())

        # key에 맞는 value를 불러오기 위한 변수
        link = title.a.next_sibling.next_sibling.get('onclick')
        # value에서 값만 추출하기 위한 배열
        # 0번은 no의 값, 1번은 print_no의 값
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://kedu.kangwon.ac.kr/board/dboard.php?id=com1&notice_id=&s=&tot=&search=&search_cond=&no=" \
              + linkarray[0] + "&print_no=" + linkarray[1] + "&exec=view&npop=&sort=&desc=&search_cat_no="
        title = title.text.strip()
        date = date.text.replace(".", "-", 2)

        data.append([linkarray[0], department, linkarray[1], title, date, url])

    return data

def korean(department, soup, url):
    data = []
    keyword = soup.find_all('tr', align="center")

    for i in range(1, len(keyword)):
        number = numbering(keyword[i].td.text.strip())
        title = keyword[i].find('td', align="left").a
        date = soup.find_all('span', "member")[i-1].parent.parent.next_sibling.next_sibling.text
        link = url+title.get('href').lstrip(".")
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title.text, date, link])

    return data

def masscom(department, soup, baseurl):
    data = []
    if soup.find('iframe').get('src') is not None:
        soup = callreq(baseurl, soup.find('iframe').get('src').lstrip("."))
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        url = keyword.find_all('a')[-1].get('href').lstrip(".")
        soup = callreq(baseurl, url)
        arr = soup.find('div', style="clear:both; height:30px;")
        title = arr.next_sibling.next_sibling.text.replace(" ", "", 24).strip()
        date = "20"+arr.text.split(" ")[2]
        link = baseurl+url
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def mathedu(department, soup, url):
    data = []
    keyword = soup.find('tr', height="25")

    while keyword.next_sibling is not None:
        number = numbering(keyword.td.text.strip())
        title = keyword.a.text.strip()
        date = keyword.find_all('td', align="center")[2].text
        link = url+keyword.a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['BID'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling.next_sibling

    return data

def padm(department, soup):
    data = []
    keyword = soup.find('li', "list-item")

    while keyword is not None:
        number = numbering(keyword.div.text.strip())
        soup = callurl('', keyword.a.get('href'))
        title = soup.find('h1').text.strip()
        date = soup.find('div', "panel-heading").find_all('span')
        num = len(date)
        if date[num-2].find('span', "orangered") is not None:
            datetext = date[num-2].get('content').split("KST")[0]
        else:
            datetext = date[num-1].get('content').split("KST")[0]
        link = keyword.a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, datetext, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def physics(department, soup, baseurl):
    data = []
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        if keyword.find('td') != -1:
            number = numbering(keyword.td.text.strip())
            array = keyword.find_all('td')
            title = array[1].a.text.strip()
            date = array[3].text.replace(".", "-", 2)
            link = baseurl + array[1].a.get('href')

            data.append([link.split('/')[-1], department, number, title, date, link])

        keyword = keyword.next_sibling

    return data

def politics(department, soup, baseurl):
    data = []
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        number = numbering(keyword.td.text.strip())
        url = keyword.a.get('href').lstrip(".")
        soup = callreq(baseurl, url)
        title = keyword.a.text
        date = "20" + soup.find('div', "info").find('span', "date").text.split(" ")[1]
        link = baseurl + url
        id = parse.parse_qs(parse.urlparse(link).query)['wr_id'][0]

        data.append([id, department, number, title, date, link])

        keyword = keyword.next_sibling.next_sibling

    return data

def social(department, soup, baseurl, sign):
    data = []
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword) / 2)):
        title = keyword[i * 2].a.text.strip()
        if sign == 'i':
            number = date[i * 4].text.strip()
            datetext = date[i + i + i * 2 + 1].text.replace("·", "-", 2)
        else:
            number = date[i * 3].text.strip()
            datetext = date[i + i * 2 + 1].text.replace("·", "-", 2)
        number = number.split("\n")
        number = numbering(number[0] if len(number) == 1 else number[-1])
        link = baseurl+keyword[i * 2].a.get('href')
        id = parse.parse_qs(parse.urlparse(link).query)['no'][0]

        data.append([id, department, number, title, datetext, link])

    return data

def aed(department, soup):
    data = []
    baseurl = 'https://aed.kangwon.ac.kr/contents.do?v=view&'
    cid = 'cid=156ca9ebd47d4a2598f7a816d394e1e9&'
    masterid = 'masterId=e435256b016a4b5f8b795f2870a2ee41&'
    lasturl = 'pageIndex=1&divId=&searchCondition=1&searchKeyword='

    keyword = soup.find('div', 'board-body').ul.find_all('li')
    for i in range(0, len(keyword)):
        dataid = keyword[i].a.get('data-id')
        row = keyword[i].find_all('div')
        number = numbering(row[0].text.strip())
        title = row[1].text.strip().lstrip("제목").strip()
        date = row[3].text.strip().lstrip("등록일").strip()
        link = baseurl + "id=" + dataid + "&" + cid + masterid + lasturl
        id = parse.parse_qs(parse.urlparse(link).query)['id'][0]

        data.append([id, department, number, title, date, link])

    return data


if __name__ == '__main__':

    # account 함수 사용

    # ACF - 경영대학 회계학부
    # url = "http://account.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # print('\n'.join(str(a) for a in account('ACF', callreq('', url))))

    # archi 함수 사용

    # AFA - 문화예술·공과대학 건축공학전공
    # url = 'http://archi.kangwon.ac.kr/'
    # url2 = 'index.php?mp=4_1_1'
    # print('\n'.join(str(a) for a in archi('AFA', callreq(url, url2), url+url2)))

    # AFC - 문화예술·공과대학 기계의용공학전공
    # url = 'http://mechanical.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1'
    # print('\n'.join(str(a) for a in archi('AFC', callreq(url, url2), url+url2)))

    # AFH - 문화예술·공과대학 산업공학전공
    # url = 'http://sme.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1'
    # print('\n'.join(str(a) for a in archi('AFH', callreq(url, url2), url+url2)))

    # AFI - 문화예술·공과대학 생물공학전공
    # url = 'http://bioeng.kangwon.ac.kr/'
    # url2 = 'index.php?mp=6_1_1'
    # print('\n'.join(str(a) for a in archi('AFI', callreq(url, url2), url+url2)))

    # AFK - 문화예술·공과대학 에너지자원공학전공
    # url = 'http://www.enre.kr/'
    # url2 = 'index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1'
    # print('\n'.join(str(a) for a in archi('AFK', callreq(url, url2), url+url2)))

    # AFN - 문화예술·공과대학 재료공학전공
    # url = 'http://material.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1_1'
    # print('\n'.join(str(a) for a in archi('AFN', callreq(url, url2), url+url2)))

    # AFO - 문화예술·공과대학 토목공학전공
    # url = 'http://civil.kangwon.ac.kr/2014/'
    # url2 = 'index.php?mp=6_1'
    # print('\n'.join(str(a) for a in archi('AFO', callreq(url, url2), url+url2)))

    # AFP - 문화예술·공과대학 화학공학전공
    # url = 'http://chemeng.kangwon.ac.kr/'
    # url2 = 'index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1'
    # print('\n'.join(str(a) for a in archi('AFP', callreq(url, url2), url + url2)))

    # AGF - 사범대학 역사교육과
    # url = 'http://history.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1'
    # print('\n'.join(str(a) for a in archi('AGF', callreq(url, url2), url + url2)))

    # architecture 함수 사용

    # AFB - 문화예술·공과대학 건축학과[5년제]
    # url = 'http://architecture.kangwon.ac.kr/nano/www/board/'
    # url2 = 'list.php?bid_=comm_notice'
    # print('\n'.join(str(a) for a in architecture('AFB', callreq(url, url2), url + url2)))

    # art 함수 사용

    # AFG - 문화예술·공과대학 미술학과
    # soup에서 'tr', "kboard-list-notice"를 찾지 못함  -   수정
    # url = 'http://art.kangwon.ac.kr'
    # url2 = '/wp/?page_id=1782'
    # print('\n'.join(str(a) for a in art('AFG', callreq(url, url2), url + url2)))

    # biz 함수 사용

    # ACA - 경영대학 경영학부
    # url = 'http://biz.kangwon.ac.kr/'
    # url2 = 'bbs/board.php?bo_table=sub06_1'
    # print('\n'.join(str(a) for a in biz('ACA', callreq(url, url2), url + url2)))

    # AID - 산림환경과학대학 산림환경보호학전공
    # url = 'http://fep.kangwon.ac.kr/'
    # url2 = 'bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in biz('AID', callreq(url, url2), url + url2)))

    # AIF - 산림환경과학대학 제지공학전공
    # url = 'https://paper.kangwon.ac.kr/board'
    # url2 = '/bbs/board.php?bo_table=sub03_1'
    # print('\n'.join(str(a) for a in biz('AIF', callreq(url, url2), url + url2)))

    # AJA - 수의과대학 수의예과
    # url = 'http://vetmed.kangwon.ac.kr'
    # url2 = '/bbs/board.php?bo_table=sub07_1'
    # print('\n'.join(str(a) for a in biz('AJA', callreq(url, url2), url + url2)))

    # AJB - 수의과대학 수의학과
    # 수의예과, 수의학과 동일...?
    # url = 'http://vetmed.kangwon.ac.kr'
    # url2 = '/bbs/board.php?bo_table=sub07_1'
    # print('\n'.join(str(a) for a in biz('AJB', callreq(url, url2), url + url2)))

    # cba 함수 사용

    # ACB - 경영대학 경제학부
    # url = 'http://economics.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1'
    # print('\n'.join(str(a) for a in cba('ACB', callreq(url, ''), 'h1', 'req', url)))

    # ACC - 경영대학 관광경영학과
    # url = 'http://tourism.kangwon.ac.kr/bbs/board.php?bo_table=notice'
    # print('\n'.join(str(a) for a in cba('ACC', callreq(url, ''), 'h1', 'req', url)))

    # ADB - 농업생명과학대학 농업자원경제학전공
    # url = 'http://agecon.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ADB', callreq(url, ''), 'h1', 'req', url)))

    # ADF - 농업생명과학대학 식물자원응용과학전공
    # url = 'http://appliedplant.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ADF', callreq(url, ''), 'h1', 'req', url)))

    # ADG - 농업생명과학대학 식품생명공학전공
    # url = 'http://foodtech.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ADG', callreq(url, ''), 'h1', 'req', url)))

    # ADI 농업생명과학대학 - 원예과학전공
    # url = 'http://horti.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1'
    # print('\n'.join(str(a) for a in cba('ADI', callreq(url, ''), 'h1', 'req', url)))

    # ADJ - 농업생명과학대학 응용생물학전공
    # url = 'http://applybio.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ADJ', callreq(url, ''), 'h1', 'req', url)))

    # ADK - 농업생명과학대학 지역건설공학과
    # 사이트 리뉴얼로 인해 수정 필요  -  수정
    url = 'https://aed.kangwon.ac.kr/contents.do?cid=156ca9ebd47d4a2598f7a816d394e1e9'
    print('\n'.join(str(a) for a in aed('ADK', callreq(url, ''))))

    # AFJ - 문화예술·공과대학 스포츠과학과
    # 이건 왜 안되냐...흑흑흑
    # url = 'http://sport.kangwon.ac.kr'
    # url2 = '/sub2/04.php'
    # print('\n'.join(str(a) for a in cba('AFJ', callreq(url, url2), 'h1', 'req', url+url2)))

    # AGK - 사범대학 체육교육과
    # 이건 또 왜 안되냐...흑흑흑
    # url = 'http://phyedu.kangwon.ac.kr/'
    # url2 = 'sub5/03.php'
    # print('\n'.join(str(a) for a in cba('AGK', callreq(url, url2), 'h1', 'req', url+url2)))

    # AIB - 산림환경과학대학 산림소재공학전공
    # url = 'https://wood.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1'
    # print('\n'.join(str(a) for a in cba('AIB', callreq(url, ''), 'h1', 'req', url)))

    # AIC - 산림환경과학대학 산림자원학전공
    # url = 'http://forestry.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1'
    # print('\n'.join(str(a) for a in cba('AIC', callreq(url, ''), 'h2', 'req', url)))

    # AIE - 산림환경과학대학 생태조경디자인학과
    # url = 'http://lands.kangwon.ac.kr'
    # url2 = '/bbs/board.php?bo_table=sub03_01'
    # print('\n'.join(str(a) for a in cba('AIE', callreq(url, url2), 'h1', 'req', url+url2)))

    # AKA - 약대 약학과
    # url = 'http://pharmacy.kangwon.ac.kr/bbs/board.php?bo_table=sub02_05'
    # print('\n'.join(str(a) for a in cba('AKA', callreq(url, ''), 'h1', 'req', url)))

    # ALA - 의생명과학대학 분자생명과학과
    # url = 'http://molscien.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ALA', callreq(url, ''), 'h1', 'req', url)))

    # ALB - 의생명과학대학 생명건강공학과
    # url = 'http://bio-health.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1'
    # print('\n'.join(str(a) for a in cba('ALB', callreq(url, ''), 'h1', 'req', url)))

    # ALC - 의생명과학대학 생물의소재공학과
    # url = 'http://bme.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1'
    # print('\n'.join(str(a) for a in cba('ALC', callreq(url, ''), 'h1', 'req', url)))

    # ALD - 의생명과학대학 시스템면역과학전공
    # url = 'http://si.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cba('ALD', callreq(url, ''), 'h1', 'req', url)))

    # ALE - 의생명과학대학 의생명공학전공
    # url = 'http://dmbt.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1'
    # print('\n'.join(str(a) for a in cba('ALE', callreq(url, ''), 'h1', 'req', url)))

    # AMB - 인문대학 독어독문학전공
    # url = 'http://german.kangwon.ac.kr/bbs/board.php?bo_table=notice'
    # print('\n'.join(str(a) for a in cba('AMB', callreq(url, ''), 'h1', 'req', url)))

    # AMD - 인문대학 사학전공
    # url = 'http://knuhisto.kangwon.ac.kr/bbs/board.php?bo_table=sub7_1'
    # print('\n'.join(str(a) for a in cba('AMD', callreq(url, ''), 'h1', 'req', url)))

    # AME - 인문대학 영어영문학전공
    # url = 'http://english.kangwon.ac.kr/new/board/bbs/board.php?bo_table=info'
    # print('\n'.join(str(a) for a in cba('AME', callreq(url, ''), 'h1', 'req', url)))

    # AMG - 인문대학 중어중문학전공
    # url = 'http://chinese.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1'
    # print('\n'.join(str(a) for a in cba('AMG', callreq(url, ''), 'h1', 'req', url)))

    # cll 사용 함수

    # ADC - 농업생명과학대학 바이오시스템기계공학전공
    # url = 'http://bse.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1'
    # print('\n'.join(str(a) for a in cll('ADC', callreq(url, ''))))

    # AEA - 동물생명과학대학 동물산업융합학과
    # url = 'http://animal.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cll('AEA', callreq(url, ''))))

    # AEB - 동물생명과학대학 동물응용과학과
    # url = 'http://applanimalsci.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cll('AEB', callreq(url, ''))))

    # AEC - 동물생명과학대학 동물자원과학과
    # url = 'http://aniscience.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1'
    # print('\n'.join(str(a) for a in cll('AEC', callreq(url, ''))))

    # AIA - 산림환경과학대학 산림경영학전공
    # url = 'http://fm.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1'
    # print('\n'.join(str(a) for a in cll('AIA', callreq(url, ''))))

    # AMF - 인문대학 일본학전공
    # url = 'http://www.kw-japan.com/bbs/board.php?bo_table=4_1'
    # print('\n'.join(str(a) for a in cll('AMF', callreq(url, ''))))

    # dbe 사용 함수
    # id값이 151자 고유 값
    # 랜덤값은 아닌거 같은데 하아... 겁나길다
    # aWR4PTg5MCZzdGFydFBhZ2U9MCZsaXN0Tm89NTAzJnRhYmxlPWNzX2Jic19kYXRhJmNvZGU9c3ViMDdhJnNlYXJjaF9pdGVtPSZzZWFyY2hfb3JkZXI9JnVybD1zdWIwN2Ema2V5dmFsdWU9c3ViMDc

    # AAA - IT대학 전기전자공학과
    url = 'https://eee.kangwon.ac.kr/eee/'
    url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    print('\n'.join(str(a) for a in dbe('AAA', callreq(url, url2), url)))

    # ABA - 간호대 간호학과
    # url = 'https://nurse.kangwon.ac.kr/nurse/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ABA', callreq(url, url2), url + url2)))

    # ADD - 농업생명과학대학 바이오자원환경학전공
    # url = 'https://dbe.kangwon.ac.kr/dbe/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ADD', callreq(url, url2), url + url2)))

    # ADH - 농업생명과학대학 에코환경과학전공
    # url = 'https://ecoenv.kangwon.ac.kr/ecoenv/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ADH', callreq(url, url2), url + url2)))

    # AFD - 문화예술·공과대학 디자인학과
    # url = 'https://design.kangwon.ac.kr/design/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('AFD', callreq(url, url2), url + url2)))

    # AFE - 문화예술·공과대학 메카트로닉스공학전공
    # url = 'https://mecha.kangwon.ac.kr/mecha/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('AFE', callreq(url, url2), url + url2)))

    # AFL - 문화예술·공과대학 영상문화학과
    # url = 'https://vculture.kangwon.ac.kr/vculture/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('AFL', callreq(url, url2), url + url2)))

    # AFQ - 문화예술·공과대학 환경공학전공
    # url = 'https://environ.kangwon.ac.kr/environ/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('AFQ', callreq(url, url2), url + url2)))

    # ANB - 자연과학대학 생명과학과
    # url = 'https://biology.kangwon.ac.kr/biology/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ANB', callreq(url, url2), url + url2)))

    # ANC - 자연과학대학 생화학전공
    # url = 'https://biochem.kangwon.ac.kr/biochem/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ANC', callreq(url, url2), url + url2)))

    # ANE - 자연과학대학 지구물리학전공
    # url = 'https://geophysics.kangwon.ac.kr/geophysics/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ANE', callreq(url, url2), url + url2)))

    # ANF - 자연과학대학 지구물리학전공
    # url = 'https://geology.kangwon.ac.kr/geology/'
    # url2 = 'bbs_list.php?code=sub07a&keyvalue=sub07'
    # print('\n'.join(str(a) for a in dbe('ANF', callreq(url, url2), url + url2)))

    # AGB - 사범대학 과학교육학부
    # url = 'http://sciedu.kangwon.ac.kr/new/'
    # url2 = 'sub02_01.php'
    # print('\n'.join(str(a) for a in edu('AGB', callreq(url, url2), url)))

    # AGC - 사범대학 교육학과
    # url = 'http://edu.kangwon.ac.kr/new/'
    # url2 = 'sub04_02.php'
    # print('\n'.join(str(a) for a in edu('AGC', callreq(url, url2), url)))

    # AGH - 사범대학 윤리교육과
    # url = 'http://ethicsedu.kangwon.ac.kr/new/'
    # url2 = 'sub06_01.php'
    # print('\n'.join(str(a) for a in edu('AGH', callreq(url, url2), url)))

    # AGI - 사범대학 일반사회교육과
    # url = 'http://ssedu.kangwon.ac.kr/new/'
    # url2 = 'sub02_01.php'
    # print('\n'.join(str(a) for a in edu('AGI', callreq(url, url2), url)))

    # AGL - 사범대학 한문교육과
    # url = 'http://ccedu.kangwon.ac.kr/new/'
    # url2 = 'sub03_01.php'
    # print('\n'.join(str(a) for a in edu('AGL', callreq(url, url2), url)))

    # engedu 사용 함수

    # AGG - 사범대학 영어교육과
    # url = 'http://engedu.kangwon.ac.kr/twb_bbs/'
    # url2 = 'user_bbs_list.php?bcd=01_06_04_00_00'
    # print('\n'.join(str(a) for a in engedu('AGG', callreq(url, url2), url)))

    # france 사용 함수

    # AMC - 인문대학 불어불문학전공
    # url = 'http://france.kangwon.ac.kr'
    # url2 = '/?doc=bbs/board.php&bo_table=gongi'
    # print('\n'.join(str(a) for a in france('AGG', callreq(url, url2), url + url2)))

    # geoedu 사용 함수

    # AGJ - 사범대학 지리교육과
    # url = 'http://geoedu.kangwon.ac.kr/department/notice.php'
    # print('\n'.join(str(a) for a in geoedu('AGG', callreq(url, ''), url)))

    # homecs 사용 함수

    # AGA - 사범대학 가정교육과
    # url = 'http://homecs.kangwon.ac.kr/bbs/'
    # url2 = 'zboard.php?id=bbs31&page=1&page_num=20'
    # print('\n'.join(str(a) for a in homecs('AGA', callreq(url, url2), url + url2)))

    # it 사용 함수

    # AAB - IT대학 전자공학과
    # url = 'http://ee.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1'
    # print('\n'.join(str(a) for a in it('AAB', callreq(url, url2), url + url2)))

    # AAD IT대학 컴퓨터공학전공
    # url = 'https://cse.kangwon.ac.kr/'
    # url2 = 'index.php?mp=5_1_1'
    # print('\n'.join(str(a) for a in it('AAD', callreq(url, url2), url + url2)))

    # itb 사용 함수

    # ACD - 경영대학 국제무역학과
    # url = 'http://itb.kangwon.ac.kr/bbs/board.php?bo_table=notice'
    # print('\n'.join(str(a) for a in itb('ACD', callreq(url, ''))))

    # kedu 사용 함수

    # AGD - 사범대학 국어교육과
    # url = 'http://kedu.kangwon.ac.kr/'
    # url2 = 'sub05_01.php'
    # print('\n'.join(str(a) for a in kedu('AGD', callreq(url, url2), url)))

    # korean 사용 함수

    # AMA - 인문대학 국어국문학전공
    # url = 'http://korean.kangwon.ac.kr/2013'
    # url2 = '/bbs/board.php?bo_table=sub05_01'
    # print('\n'.join(str(a) for a in korean('AGD', callreq(url, url2), url + url2)))

    # masscom 사용 함수

    # ACE - 경영대학 정보통계학부
    # url = 'http://statistics.kangwon.ac.kr/board'
    # url2 = '/bbs/board.php?bo_table=notice_bbs'
    # print('\n'.join(str(a) for a in masscom('ACE', callreq(url, url2), url)))

    # AHD - 사회과학대학 신문방송학과
    # url = 'http://masscom.kangwon.ac.kr'
    # url2 = '/bbs/board.php?bo_table=sub3_1'
    # print('\n'.join(str(a) for a in masscom('AHD', callreq(url, url2), url)))

    # AHE - 사회과학대학 심리학부
    # url = 'http://psych.kangwon.ac.kr/gnuboard4'
    # url2 = '/bbs/board.php?bo_table=bbs41'
    # print('\n'.join(str(a) for a in masscom('AHE', callreq(url, url2), url)))

    # AMH - 인문대학 철학전공
    # url = 'http://kwphilo.kangwon.ac.kr'
    # url2 = '/sub05_01.htm'
    # print('\n'.join(str(a) for a in masscom('AMH', callreq(url, url2), url)))

    # ANG - 자연과학대학 화학전공
    # url = 'http://chemis.kangwon.ac.kr/board'
    # url2 = '/bbs/board.php?bo_table=chemis_table04'
    # print('\n'.join(str(a) for a in masscom('ANG', callreq(url, url2), url)))

    # mathedu 사용 함수

    # AGE - 사범대학 수학교육과
    # url = 'http://mathedu.kangwon.ac.kr/'
    # url2 = 'main.php?mt=page&mp=3_1&mm=oxbbs&oxid=2'
    # print('\n'.join(str(a) for a in mathedu('AGE', callreq(url, url2), url + url2)))

    # padm 사용 함수

    # AHG - 사회과학대학 행정학부
    # url = 'https://padm.kangwon.ac.kr/bbs/board.php?bo_table=sub3_2'
    # print('\n'.join(str(a) for a in padm('AHG', callreq(url, ''))))

    # physics 사용 함수

    # ANA - 자연과학대학 물리학과
    # url = 'http://physics.kangwon.ac.kr/'
    # url2 = '/sub46'
    # print('\n'.join(str(a) for a in physics('ANA', callreq(url, url2), url + url2)))

    # AND - 자연과학대학 수학과
    # url = 'http://math.kangwon.ac.kr/xe/notice'
    # print('\n'.join(str(a) for a in physics('AND', callreq(url, ''), url)))

    # politics 사용 함수

    # AHF - 사회과학대학 정치외교학과
    # url = 'http://politics.kangwon.ac.kr'
    # url2 = '/bbs/board.php?bo_table=sub9_3'
    # print('\n'.join(str(a) for a in politics('AHF', callreq(url, url2), url)))

    # social 사용 함수

    # AHA - 사회과학대학 문화인류학과
    # url = 'http://anthro.kangwon.ac.kr/bbs/'
    # url2 = 'zboard.php?id=bbs41'
    # print('\n'.join(str(a) for a in social('AHA', callreq(url, url2), url + url2, "")))

    # AHB - 사회과학대학 부동산학과
    # url = 'http://re1978.kangwon.ac.kr/bbs/'
    # url2 = 'zboard.php?id=bbs41'
    # print('\n'.join(str(a) for a in social('AHB', callreq(url, url2), url + url2, "i")))

    # AHC - 사회과학대학 사회학과
    # url = 'http://sociology.kangwon.ac.kr/bbs/'
    # url2 = 'zboard.php?id=notice'
    # print('\n'.join(str(a) for a in social('AHC', callreq(url, url2), url + url2, "i")))

    # 컴퓨터과학전공 - 컴퓨터공학전공
    # 농생명산업학전공, 시설농업학전공 - 미래농업융합부 공지로 만들어야함
    # 무용학과, 음악학과 - 과 홈페이지 없음


    pass
