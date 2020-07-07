import ssl
from urllib.request import urlopen

import requests
from bs4 import BeautifulSoup


# 게시글의 하이퍼링크가 너무 길어 앞이 짤리는 부분을 채워주기 위한 변수
# ex) cba.kangwon.ac.kr/  <- baseurl
#     ./bbs/board.php?pageid=1 <- href
baseurl = ""


def callreq(base, url):
    """
    일반적인 홈페이지 불러올 때 사용하는 함수
    requests.get 함수를 사용한다.
    return soup (홈페이지 소스)
    """
    global baseurl
    baseurl = base
    head = {'User-Agent': 'Mozilla/5.0', 'referer': 'http://itb.kangwon.ac.kr'}
    resp = requests.get(base+url, headers=head)
    soup = BeautifulSoup(resp.content, "html.parser")
    return soup


def callurl(base, url):
    """
    인증서가 필요한 홈페이지를 불러올 때 사용하는 함수
    urlopen 함수를 사용한다.
    return soup (홈페이지 소스)
    """
    global baseurl
    baseurl = base
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


def cba(department, soup, titlesign, callsign):
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
        number = ""
        if titlesign == 'h1':
            number = keyword.find('td', 'td_num').text.strip()
        elif titlesign == 'h2':
            number = keyword.find('td', 'td_num2').text.strip()
        number = numbering(number)
        if callsign == "url":
            soup = callurl('', url)
        elif callsign == "req":
            soup = callreq('', url)
        # 게시글의 제목을 받아올 변수
        title = soup.find(titlesign, id="bo_v_title").text.strip()
        # 게시글의 게시 날짜를 받아올 변수
        date = soup.find('section', id="bo_v_info")
        if titlesign == 'h1':
            date = date.find_all('strong')[1]
            date = "20" + date.text.split(" ")[0].strip()
        elif titlesign == 'h2':
            date = date.find('strong', 'if_date')
            date = "20" + date.text.split(" ")[1].strip()
        # 하이퍼링크를 받아올 변수
        link = keyword.a.get('href')

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


def biz(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        number = keyword.find('td', 'num').text.strip()
        number = numbering(number)
        title = keyword.find_all('a')
        length = len(title)
        date = keyword.find('td', "datetime").text.replace(".", "-", 2)
        # 앞에 20을 붙여주어 연도를 완성시킨다.
        # ex) 20.01.01 -> 2020.01.01
        if len(date) <= 9:
            date = "20"+date
        link = baseurl+title[length-1].get('href').lstrip(".")
        title = title[length-1].text.strip()

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 경영대 회계학전공
def account(department, soup):
    data = []
    keyword = soup.find('table', "table table-hover").tbody.tr

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        url = keyword.a.get('href')
        # 게시글을 받아오는 작업
        # 공지사항 페이지에 내용이 부실한 경우 게시글에 들어가서 직접 데이터를 가져옴
        # ex) 날짜가 없거나 부실한 경우, 태그의 속성이나 이름이 없어 제목에 접근하기 힘든 경우,
        #     공지사항 페이지는 다른데 게시글이 유사한 학과들을 통합하기 위한 경우 등
        resp = requests.get(url)
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('h4', "subject").text.strip().rstrip("new").strip()
        date = soup.find('div', "desc").find_all('strong')[1]
        date = date.text.split(" ")[0]

        # print(number, title, date)
        # print(url)

        tmp = [department, number, title, date, url]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 경영대 국제무역학과
def itb(department, soup):
    data = []
    keyword = soup.find('table', "table table-hover text-center bottom-3b").tbody.tr

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('h4', id="bo_v_title").b.text
        date = soup.find('ul', "list-inline").li.next_sibling.next_sibling.b
        date = "20"+date.text.split(" ")[0]
        link = keyword.a.get('href')

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 농생대
def agrilifesci(department):
    data = []
    # 앞부분의 url
    base = "http://knucals.kangwon.ac.kr/contents.do?v="
    # 뒷부분의 url
    lasturl = "&pageIndex=1&divId=&searchCondition=1&searchKeyword="
    # 중간부분의 url
    cid = "&cid=db2548006ba044b09df5b6b5df7aacd4"
    masterid = "&masterid=03cc0395b649458081fb83355712a973"
    resp = requests.get(base+"&id="+cid+masterid+lasturl)
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "board_body").ul.li

    while keyword is not None:
        number = keyword.span.text.strip()
        number = numbering(number).lstrip("번호")
        title = keyword.find('a', "content-list").text.strip()
        date = keyword.find('span', "board_col date").text.lstrip("등록일").strip()
        link = base + "view&id=" + keyword.a.get('data-id')+cid+masterid+lasturl

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


def cll(department, soup):
    data = []
    keyword = soup.find('td', "td_subject").parent

    while keyword is not None:
        number = keyword.find('td', 'td_num2').text.strip()
        number = numbering(number)
        url = keyword.find('div', "bo_tit").a.get('href')
        soup = callreq('', url)
        title = soup.find('span', "bo_v_tit").text.strip()
        date = soup.find('section', id="bo_v_info").find('strong', "if_date")
        date = "20"+date.text.split(" ")[1].strip()

        # print(number, title, date)
        # print(url)

        tmp = [department, number, title, date, url]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


def dbe(department, soup):
    global baseurl
    data = []
    keyword = soup.find('td', 'text-center hidden-xs').parent

    while keyword is not None:
        if keyword.find('td') != -1:
            number = keyword.find('td', 'text-center hidden-xs').text.strip()
            number = numbering(number)
            array = keyword.find_all('td')
            title = array[1].a.text.strip()
            date = array[3].text.replace("/", "-", 2)
            link = baseurl+array[1].a.get('href')

            # print(number, title, date)
            # print(link)

            tmp = [department, number, title, date, link]
            data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 문예대공대 건축학과(5년제)
def architecture(department, soup):
    global baseurl
    data = []
    keyword = soup.find('td', "left").parent

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        link = baseurl+keyword.a.get('href')
        title = title.text
        # print(number, title, date.text)
        # print(link)

        tmp = [department, number, title, date.text, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


def archi(department, soup):
    global baseurl
    data = []
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling is not None:
        number = keyword.find('td').text.strip()
        number = numbering(number)
        title = keyword.a.text.strip()
        date = keyword.a.parent
        for i in range(0, 4):
            date = date.next_sibling
        link = baseurl + keyword.a.get('href')

        # print(number, title, date.text)
        # print(link)

        tmp = [department, number, title, date.text, link]
        data.append(tmp)

        keyword = keyword.next_sibling

    return data


# 문예대공대 무용학과 <- 터졋나?

# 문예대공대 미술학과
def art(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', "kboard-list-notice")

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number).rstrip("사항")
        title = keyword.find('div', "cut_strings").a
        date = keyword.find('td', "kboard-list-date").text.replace(".", "-", 2)
        link = baseurl+title.get('href')
        title = title.text.strip()

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 사범대
def educatio(department, soup):
    global baseurl
    data = []
    keyword = soup.find('iframe').get('src').lstrip(".")
    soup = callreq(baseurl, keyword)
    keyword = soup.find('table', align="center").table.next_sibling.next_sibling
    array = keyword.find_all('tr', align="center")

    for i in range(1, len(array)):
        number = array[i].find('span', "tt").text.strip()
        number = numbering(number)
        title = array[i].a.text
        date = array[i].find_all('span', "tt")
        if i == 1:
            date = date[3]
        else:
            date = date[4]
        date = "20"+date.text
        link = baseurl+array[i].a.get('href').lstrip("./")

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

    return data


def edu(department, soup):
    global baseurl
    data = []
    keyword = soup.find('iframe')
    soup = callreq(baseurl, keyword.get('src').lstrip("."))

    arr = soup.find_all('td', align="left")
    number = soup.find_all('tr', align="center")
    for i in range(0, len(arr) - 2):
        number[i] = numbering(number[i].td.text.strip())
        url = arr[i].a.get('href').lstrip(".")
        soup = callreq(baseurl, url)
        title = arr[i].text.strip()
        date = soup.find_all('span', "text_bold888")
        datetext = date[1].next_sibling.next_sibling.text.split("조회")[0].strip()
        link = baseurl + url

        # print(number[i], title, datetext)
        # print(link)

        tmp = [department, number[i], title, datetext, link]
        data.append(tmp)

    return data


# 사범대 국어교육과
def kedu(department, soup):
    global baseurl
    data = []
    keyword = soup.find('div', id="con").find('iframe').get('src')
    soup = callreq(baseurl, keyword)
    array = soup.find_all('td', "text_03")
    for i in range(0, len(array)):
        title = array[i]
        date = array[i]
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        number = title
        for j in range(0, 4):
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

        # print(number, title, date)
        # print(url)

        tmp = [department, number, title, date, url]
        data.append(tmp)

    return data


# 사범대 영어교육과
def engedu(department, soup):
    global baseurl
    data = []
    keyword = soup.find('div', id="user_board_list").tr.next_sibling.next_sibling

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        title = keyword.find('td', "title")
        date = keyword.find('td', "date").text
        link = baseurl+title.a.get('href')
        title = title.text.strip()

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 사범대 지리교육과
def geoedu(department, soup):
    global baseurl
    data = []
    array = soup.find_all('td', "text-left")

    for i in range(0, len(array)):
        number = array[i].previous_sibling.previous_sibling
        number = numbering(number.text.strip())
        title = array[i].a.text.strip()
        date = array[i].next_sibling.next_sibling.text
        # b_id의 값을 받아오기 위한 변수
        b_id = array[i].a.get('href').lstrip("javascript:ContentsView(").rstrip(");")
        link = baseurl+"?boardID=notice&b_id="+b_id+"&mode=view&npage=1&search_opt=b_writerName&search_text="
        link = link + "&pw_check1=&pw_check2=&pw_check3=&pw_check4=&pw_check5" \
                      "=&pw_check6=&pw_check7=&pw_check8=&pw_check9=&pw_check10="

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

    return data


# 사범대 가정교육과
def homecs(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', "title").next_sibling.next_sibling

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        title = keyword.a.text
        date = keyword.find_all('td', "list_eng3")[1]
        datetext = date.text.replace("·", "-", 2)
        link = baseurl+keyword.a.get('href')

        # print(number, title, datetext)
        # print(link)

        tmp = [department, number, title, datetext, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 사범대 수학교육과
def mathedu(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', height="25")

    while keyword.next_sibling is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        title = keyword.a.text.strip()
        date = keyword.find_all('td', align="center")[2].text
        link = baseurl+keyword.a.get('href')

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling.next_sibling

    return data


# sign
# (number와 date에 접근하는 부분을 제외하고 나머지 부분이
#  똑같은 코드이기 때문에 합친 후, number와 date 접근의 차이를 두기 위한 변수)
def social(department, soup, sign):
    global baseurl
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
        if len(number) == 1:
            number = number[0]
        else:
            number = number[len(number)-1]
        number = numbering(number)
        link = baseurl+keyword[i * 2].a.get('href')

        # print(number, title, datetext)
        # print(link)

        tmp = [department, number, title, datetext, link]
        data.append(tmp)

    return data


def masscom(department, soup):
    global baseurl
    data = []
    if soup.find('iframe').get('src') is not None:
        soup = callreq(baseurl, soup.find('iframe').get('src').lstrip("."))
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        url = keyword.find_all('a')
        num = len(url)
        url = url[num-1].get('href').lstrip(".")
        soup = callreq(baseurl, url)
        arr = soup.find('div', style="clear:both; height:30px;")
        title = arr.next_sibling.next_sibling.text.replace(" ", "", 24).strip()
        date = "20"+arr.text.split(" ")[2]
        link = baseurl+url

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 사과대 정치외교학과
def politics(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        url = keyword.a.get('href').lstrip(".")
        soup = callreq(baseurl, url)
        title = keyword.a.text
        date = soup.find('div', "info").find('span', "date").text.split(" ")[1]
        date = "20"+date
        link = baseurl + url

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 사과대 행정학전공
def padm(department, soup):
    data = []
    keyword = soup.find('li', "list-item")

    while keyword is not None:
        number = keyword.div.text.strip()
        number = numbering(number)
        soup = callurl('', keyword.a.get('href'))
        title = soup.find('h1').text.strip()
        date = soup.find('div', "panel-heading").find_all('span')
        num = len(date)
        if date[num-2].find('span', "orangered") is not None:
            datetext = date[num-2].get('content').split("KST")[0]
        else:
            datetext = date[num-1].get('content').split("KST")[0]
        link = keyword.a.get('href')

        # print(number, title, datetext)
        # print(link)

        tmp = [department, number, title, datetext, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


# 인문대
def humanities(department, soup):
    data = []
    keyword = soup.find_all('table', align="center")

    for i in range(5, len(keyword)-2):
        number = keyword[i].td.text.strip()
        number = numbering(number)
        title = keyword[i].find('td', "text_03")
        date = keyword[i].find('td', "text_03")
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        # key의 맞는 value를 불러오기 위한 변수
        link = title.a.get('onclick')
        # value에서 값만 추출하여 저장하는 배열
        # 0번은 no의 값, 1번은 print_no의 값
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://humanities.kangwon.ac.kr/sub04_01.php?id=notice&notice_id=&s=&tot=&search=&search_cond=&no=" \
              + linkarray[0] + "&print_no=" + linkarray[1] + "&exec=view&npop=&sort=&desc=&search_cat_no="
        title = title.text.strip()
        date = date.text.replace(".", "-", 2)

        # print(number, title, date)
        # print(url)

        tmp = [department, number, title, date, url]
        data.append(tmp)

    return data


# 인문대 국어국문학전공
def korean(department, soup):
    global baseurl
    data = []
    keyword = soup.find_all('tr', align="center")

    for i in range(1, len(keyword)):
        number = keyword[i].td.text.strip()
        number = numbering(number)
        title = keyword[i].find('td', align="left").a
        date = soup.find_all('span', "member")[i-1].parent.parent.next_sibling.next_sibling
        date = date.text
        link = baseurl+title.get('href').lstrip(".")
        title = title.text

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

    return data


# 인문대 불어불문학과
def france(department, soup):
    global baseurl
    data = []
    keyword = soup.find_all('tr', align="center")

    for i in range(0, len(keyword)):
        number = keyword[i].td.text.strip()
        number = numbering(number)
        title = keyword[i].a
        date = keyword[i].find_all('td', "bh")[2]
        date = "20"+date.text
        link = baseurl+title.get('href').lstrip(".")
        title = title.text

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

    return data


def physics(department, soup):
    global baseurl
    data = []
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        if keyword.find('td') != -1:
            number = keyword.td.text.strip()
            number = numbering(number)
            array = keyword.find_all('td')
            title = array[1].a.text.strip()
            date = array[3].text.replace(".", "-", 2)
            link = baseurl + array[1].a.get('href')

            # print(number, title, date)
            # print(link)

            tmp = [department, number, title, date, link]
            data.append(tmp)

        keyword = keyword.next_sibling

    return data


def it(department, soup):
    global baseurl
    data = []
    keyword = soup.find('table', "bbs_list").tbody.tr

    while keyword.next_sibling is not None:
        number = keyword.td.text.strip()
        number = numbering(number)
        title = keyword.find('td', "tit").a
        date = keyword.find_all('td')
        num = len(date)
        date = date[num-2].text
        link = baseurl+title.get('href')
        title = title.text.strip()

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling

    return data


# 창업지원단
def ksef(department, soup):
    global baseurl
    data = []
    keyword = soup.find('table', 'table_bbs').tbody.tr

    while keyword is not None:
        arr = keyword.find_all('td')
        number = numbering(arr[0].text.strip())
        title = arr[1].text.strip()
        link = baseurl+arr[1].a.get('href')
        date = arr[4].text.replace('.', '-').strip()

        # print(number, title, date)
        # print(link)

        tmp = [department, number, title, date, link]
        data.append(tmp)

        keyword = keyword.next_sibling.next_sibling

    return data


def museum(soup):
    global baseurl
    data = []
    keyword = soup.find_all('tr', align="center")

    for i in range(1, len(keyword)):
        arr = keyword[i].find_all('td')
        number = numbering(arr[0].text.strip())
        title = arr[1].text.strip()
        link = arr[1].a.get('href').lstrip('.')
        soup = callreq(baseurl, link)
        date = "20" + soup.find('span', style="color:#888888;").text.split(' ')[2]
        url = baseurl + link

        print(number, title, date)
        print(url)

    return data


# 학생생활관
def knudorm(soup):
    global baseurl
    data = []
    keyword = soup.find('iframe').get('src')
    soup = callreq(baseurl, keyword)
    keyword = soup.find('table', "Notice_Board").tbody.tr.next_sibling.next_sibling

    while keyword is not None:
        arr = keyword.find_all('td')
        number = numbering(arr[0].text.strip())
        title = arr[1].text.strip()
        if keyword.get('onclick') is not None:
            link = keyword.get('onclick').split('href=\'')[1].rstrip('\'')
        else:
            link = arr[1].a.get('onclick').split('location=\'')[1].rstrip('\'')
        date = arr[3].text.strip()
        url = baseurl + "/admin/board/" + link

        print(number, title, date)
        print(url)

        keyword = keyword.next_sibling.next_sibling

    return data


# 교육혁신원
def itl(soup):
    global baseurl
    data = []
    keyword = soup.find('li', "tbody notice")

    while keyword is not None:
        arr = keyword.find_all('span')
        number = numbering(arr[0].text.strip())
        title = arr[1].text.strip()
        link = baseurl + arr[1].a.get('href')
        date = arr[5].text.strip()

        print(number, title, date)
        print(link)

        keyword = keyword.next_sibling.next_sibling

    return data


# 창업보육센터
def kwbi(soup):
    global baseurl
    data = []


    return data


if __name__ == '__main__':
    # 경영대
    cbaurl = "http://cba.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cbaarr = cba(callreq('', cbaurl), 'h1', "req")

    # 경영대 경제학전공
    economicsurl = "http://economics.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cbaarr = cba(callreq('', economicsurl), 'h1', "req")

    # 경영대 관광경영학과
    tourismurl = "http://tourism.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # cbaarr = cba(callreq('', tourismurl), 'h1', "req")

    # 농생대 식품생명공학과
    foodtechurl = "http://foodtech.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', foodtechurl), 'h1', "req")

    # 농생대 식물자원응용과학전공
    appliedplanturl = "http://appliedplant.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', appliedplanturl), 'h1', "req")

    # 농생대 응용생물학전공
    applybiourl = "http://applybio.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', applybiourl), 'h1', "req")

    # 농생대 원예과학전공
    hortiurl = "http://horti.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1"
    # cbaarr = cba(callreq('', hortiurl), 'h1', "req")

    # 농생대 농업자원경제학과
    ageconurl = "http://agecon.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', ageconurl), 'h1', "req")

    # 농생대 지역건설공학과
    aedurl = "http://aed.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', aedurl), 'h1', "req")

    # 문예대 스포츠과학과
    sporturl = ["http://sport.kangwon.ac.kr", "/sub2/04.php"]
    # cbaarr = cba(callreq(sporturl[0], sporturl[1]), 'h1', "req")

    # 사범대 체육교육과
    phyeduurl = ["http://phyedu.kangwon.ac.kr/", "sub5/03.php"]
    # cbaarr = cba(callreq(phyeduurl[0], phyeduurl[1]), 'h1', "req")

    # 산림대
    foresturl = "http://forest.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cbaarr = cba(callreq('', foresturl), 'h1', "req")

    # 산림대 산림자원학전공
    forestryurl = "http://forestry.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cbaarr = cba(callreq('', forestryurl), 'h2', "req")

    # 산림대 산림소재공학전공
    woodurl = "https://wood.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cbaarr = cba(callurl('', woodurl), 'h1', "url")

    # 산림대 생태조경디자인학과
    landsurl = "http://lands.kangwon.ac.kr/bbs/board.php?bo_table=sub03_01"
    # cbaarr = cba(callreq('', landsurl), 'h1', "req")

    # 약대
    pharmacyurl = "http://pharmacy.kangwon.ac.kr/bbs/board.php?bo_table=sub02_05"
    # cbaarr = cba(callreq('', pharmacyurl), 'h1', "req")

    # 의생대
    bmurl = "http://bmcollege.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', bmurl), 'h1', "req")

    # 의생대 분자생명과학과
    molscienurl = "http://molscien.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', molscienurl), 'h1', "req")

    # 의생대 생명건강공학과
    biohealthurl = "http://bio-health.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1"
    # cbaarr = cba(callreq('', biohealthurl), 'h1', "req")

    # 의생대 생물의소재공학과
    bmeurl = "http://bme.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1"
    # cbaarr = cba(callreq('', bmeurl), 'h1', "req")

    # 의생대 의생명융합학부
    bmcurl = "http://bmc.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cbaarr = cba(callreq('', bmcurl), 'h1', "req")

    # 의생대 시스템면역과학전공
    siurl = "http://si.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cbaarr = cba(callreq('', siurl), 'h1', "req")

    # 의생대 의생명공학전공
    dmbturl = "http://dmbt.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cbaarr = cba(callreq('', dmbturl), 'h1', "req")

    # 인문대 영어영문학전공
    englishurl = "http://english.kangwon.ac.kr/new/board/bbs/board.php?bo_table=info"
    # cbaarr = cba(callreq('', englishurl), 'h1', "req")

    # 인문대 독어독문학전공
    germanurl = "http://german.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # cbaarr = cba(callreq('', germanurl), 'h1', "req")

    # 인문대 중어중문학전공
    chineseurl = "http://chinese.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1"
    # cbaarr = cba(callreq('', chineseurl), 'h1', "req")

    # 인문대 사학전공
    histourl = "http://knuhisto.kangwon.ac.kr/bbs/board.php?bo_table=sub7_1"
    # cbaarr = cba(callreq('', histourl), 'h1', "req")

    # 경영대 경영학전공
    bizurl = ["http://biz.kangwon.ac.kr/", "bbs/board.php?bo_table=sub06_1"]
    # bizarr = biz(callreq(bizurl[0], bizurl[1]))

    # 산림대 산림환경보호학전공
    fepurl = ["http://fep.kangwon.ac.kr/", "bbs/board.php?bo_table=sub05_1"]
    # bizarr = biz(callreq(fepurl[0], fepurl[1]))

    # 산림대 제지공학전공
    paperurl = ["https://paper.kangwon.ac.kr/board", "/bbs/board.php?bo_table=sub03_1"]
    # bizarr = biz(callurl(paperurl[0], paperurl[1]))

    # 수의과
    vetmedurl = ["http://vetmed.kangwon.ac.kr", "/bbs/board.php?bo_table=sub07_1"]
    # bizarr = biz(callreq(vetmedurl[0], vetmedurl[1]))

    # 농생대 미래농업융합학부
    cllurl = "http://cll.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cllarr = cll(callreq('', cllurl))

    # 농생대 바이오시스템기계공학전공
    bseurl = "http://bse.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cllarr = cll(callreq('', bseurl))

    # 동생대
    anilifsciurl = "http://cals.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cllarr = cll(callreq('', anilifsciurl))

    # 동생대 동물산업융합학과
    animalurl = "http://animal.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cllarr = cll(callreq('', animalurl))

    # 동생대 동물응용과학과
    applanimalsciurl = "http://applanimalsci.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cllarr = cll(callreq('', applanimalsciurl))

    # 동생대 동물자원과학과
    aniscienceurl = "http://aniscience.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cllarr = cll(callreq('', aniscienceurl))

    # 산림대 산림경영학전공
    fmurl = "http://fm.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cllarr = cll(callreq('', fmurl))

    # 인문대 일본학전공
    japanurl = "http://www.kw-japan.com/bbs/board.php?bo_table=4_1"
    # cllarr = cll(callreq('', japanurl))

    # 농생대 바이오자원환경학전공
    dbeurl = ["https://dbe.kangwon.ac.kr/dbe/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(dbeurl[0], dbeurl[1]))

    # 농생대 에코환경과학전공
    ecoenvurl = ["https://ecoenv.kangwon.ac.kr/ecoenv/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(ecoenvurl[0], ecoenvurl[1]))

    # 문예대공대 환경공학전공
    environurl = ["https://environ.kangwon.ac.kr/environ/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(environurl[0], environurl[1]))

    # 문예대공대 메카트로닉스공학전공
    mechaurl = ["https://mecha.kangwon.ac.kr/mecha/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(mechaurl[0], mechaurl[1]))

    # 문예대공대 디자인학과
    designurl = ["https://design.kangwon.ac.kr/design/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(designurl[0], designurl[1]))

    # 문예대공대 영상문화학과
    vcultureurl = ["https://vculture.kangwon.ac.kr/vculture/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(vcultureurl[0], vcultureurl[1]))

    # 간호대
    nurseurl = ["https://nurse.kangwon.ac.kr/nurse/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(nurseurl[0], nurseurl[1]))

    # 자연대 생명과학과
    biologyurl = ["https://biology.kangwon.ac.kr/biology/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(biologyurl[0], biologyurl[1]))

    # 자연대 지구물리학전공
    geophysicsurl = ["https://geophysics.kangwon.ac.kr/geophysics/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(geophysicsurl[0], geophysicsurl[1]))

    # 자연대 지질학전공
    geologyurl = ["https://geology.kangwon.ac.kr/geology/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(geologyurl[0], geologyurl[1]))

    # 자연대 생화학전공
    biochemurl = ["https://biochem.kangwon.ac.kr/biochem/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(biochemurl[0], biochemurl[1]))

    # it대 전기전자공학과
    eeeurl = ["https://eee.kangwon.ac.kr/eee/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbearr = dbe(callreq(eeeurl[0], eeeurl[1]))

    # 문예대 건축공학전공공
    archiurl = ["http://archi.kangwon.ac.kr/", "index.php?mp=4_1_1"]
    # archiarr = archi(callreq(archiurl[0], archiurl[1]))

    # 문예대 토목공학전공
    civilurl = ["http://civil.kangwon.ac.kr/2014/", "index.php?mp=6_1"]
    # archiarr = archi(callreq(civilurl[0], civilurl[1]))

    # 문예대 기계의용공학전공
    mechanicalurl = ["http://mechanical.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archiarr = archi(callreq(mechanicalurl[0], mechanicalurl[1]))

    # 문예대 재료공학전공
    materialurl = ["http://material.kangwon.ac.kr/", "index.php?mp=5_1_1"]
    # archiarr = archi(callreq(materialurl[0], materialurl[1]))

    # 문예대 에너지자원공학전공
    enreurl = ["http://www.enre.kr/", "index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1"]
    # archiarr = archi(callreq(enreurl[0], enreurl[1]))

    # 문예대 산업공학전공
    smeurl = ["http://sme.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archiarr = archi(callreq(smeurl[0], smeurl[1]))

    # 문예대 화학공학전공
    chemengurl = ["http://chemeng.kangwon.ac.kr/", "index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1"]
    # archiarr = archi(callreq(chemengurl[0], chemengurl[1]))

    # 문예대 생물공학전공
    bioengurl = ["http://bioeng.kangwon.ac.kr/", "index.php?mp=6_1_1"]
    # archiarr = archi(callreq(bioengurl[0], bioengurl[1]))

    # 사범대 역사교육과
    historyurl = ["http://history.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archiarr = archi(callreq(historyurl[0], historyurl[1]))

    # 사범대 교육학과
    eduurl = ["http://edu.kangwon.ac.kr/new/", "sub04_02.php"]
    # eduarr = edu(callreq(eduurl[0], eduurl[1]))

    # 사범대 윤리교육과
    ethicseduurl = ["http://ethicsedu.kangwon.ac.kr/new/", "sub06_01.php"]
    # eduarr = edu(callreq(ethicseduurl[0], ethicseduurl[1]))

    # 사범대 일반사회교육과
    sseduurl = ["http://ssedu.kangwon.ac.kr/new/", "sub02_01.php"]
    # eduarr = edu(callreq(sseduurl[0], sseduurl[1]))

    # 사범대 한문교육과
    cceduurl = ["http://ccedu.kangwon.ac.kr/new/", "sub03_01.php"]
    # eduarr = edu(callreq(cceduurl[0], cceduurl[1]))

    # 사범대 과학교육학부
    scieduurl = ["http://sciedu.kangwon.ac.kr/new/", "sub02_01.php"]
    # eduarr = edu(callreq(scieduurl[0], scieduurl[1]))

    # 사과대
    socialurl = ["http://social.kangwon.ac.kr/bbs/", "zboard.php?id=notice"]
    # socialarr = social(callreq(socialurl[0], socialurl[1]), "")

    # 사과대 문화인류학과
    anthrourl = ["http://anthro.kangwon.ac.kr/bbs/", "zboard.php?id=bbs41"]
    # socialarr = social(callreq(anthrourl[0], anthrourl[1]), "")

    # 사과대 부동산학과
    reurl = ["http://re1978.kangwon.ac.kr/bbs/", "zboard.php?id=bbs41"]
    # socialarr = social(callreq(reurl[0], reurl[1]), "i")

    # 사과대 사회학과
    sociologyurl = ["http://sociology.kangwon.ac.kr/bbs/", "zboard.php?id=notice"]
    # socialarr = social(callreq(sociologyurl[0], sociologyurl[1]), "i")

    # 사과대 신문방송학과
    masscomurl = ["http://masscom.kangwon.ac.kr", "/bbs/board.php?bo_table=sub3_1"]
    # masscomarr = masscom(callreq(masscomurl[0], masscomurl[1]))

    # 사과대 심리학전공
    psychurl = ["http://psych.kangwon.ac.kr/gnuboard4", "/bbs/board.php?bo_table=bbs41"]
    # masscomarr = masscom(callreq(psychurl[0], psychurl[1]))

    # 인문대 철학전공
    philourl = ["http://kwphilo.kangwon.ac.kr", "/sub05_01.htm"]
    # masscomarr = masscom(callreq(philourl[0], philourl[1]))

    # 자연대 화학전공
    chemisurl = ["http://chemis.kangwon.ac.kr/board", "/bbs/board.php?bo_table=chemis_table04"]
    # masscomarr = masscom(callreq(chemisurl[0], chemisurl[1]))

    # 경영대 정보통계학전공
    statisticsurl = ["http://statistics.kangwon.ac.kr/board", "/bbs/board.php?bo_table=notice_bbs"]
    # masscomarr = masscom(callreq(statisticsurl[0], statisticsurl[1]))

    # 문예대공대
    aceurl = ["http://ace.kangwon.ac.kr/", "index.php?mp=4_1"]
    # itarr = it(callreq(aceurl[0], aceurl[1]))

    # it대
    iturl = ["http://it.kangwon.ac.kr/", "index.php?mp=4_1"]
    # itarr = it(callreq(iturl[0], iturl[1]))

    # it대 전자공학과
    eeurl = ["http://ee.kangwon.ac.kr/", "index.php?mp=5_1"]
    # itarr = it(callreq(eeurl[0], eeurl[1]))

    # it대 컴퓨터공학과
    cseurl = ["https://cse.kangwon.ac.kr/", "index.php?mp=5_1_1"]
    # itarr = it(callreq(cseurl[0], cseurl[1]))

    # 경영대 회계학전공
    accounturl = "http://account.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # accountarr = account(callreq('', accounturl))

    # 경영대 국제무역학과
    itburl = "http://itb.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # itbarr = itb(callreq('', itburl))

    # 농생대
    # agrilifesciarr = agrilifesci()

    # 문예대공대 건축학과(5년제)
    architectureurl = ["http://architecture.kangwon.ac.kr/nano/www/board/", "list.php?bid_=comm_notice"]
    # architecturearr = architecture(callreq(architectureurl[0], architectureurl[1]))

    # 문예대공대 미술학과
    arturl = ["http://kangwonart.ac.kr", "/wp/?page_id=1782"]
    # artarr = art(callreq(arturl[0], arturl[1]))

    # 사범대
    educatiourl = ["http://educatio.kangwon.ac.kr/", "sub04_01.php"]
    # educatioarr = educatio(callreq(educatiourl[0], educatiourl[1]))

    # 사범대 국어교육과
    keduurl = ["http://kedu.kangwon.ac.kr/", "sub05_01.php"]
    # keduarr = kedu(callreq(keduurl[0], keduurl[1]))

    # 사범대 영어교육과
    engeduurl = ["http://engedu.kangwon.ac.kr/twb_bbs/", "user_bbs_list.php?bcd=01_06_04_00_00"]
    # engeduarr = engedu(callreq(engeduurl[0], engeduurl[1]))

    # 사범대 지리교육과
    geoeduurl = "http://geoedu.kangwon.ac.kr/department/notice.php"
    # geoeduarr = geoedu(callreq(geoeduurl, ''))

    # 사범대 가정교육과
    homecsurl = ["http://homecs.kangwon.ac.kr/bbs/",
                 "zboard.php?id=bbs31&page=1&page_num=20&category=&sn=off&ss=on&sc=on"
                 "&keyword=&prev_no=504&sn1=&divpage=1&select_arrange=headnum&desc=asc"]
    # homecsarr = homecs(callreq(homecsurl[0], homecsurl[1]))

    # 사범대 수학교육과
    matheduurl = ["http://mathedu.kangwon.ac.kr/", "main.php?mt=page&mp=3_1&mm=oxbbs&oxid=2"]
    # matheduarr = mathedu(callreq(matheduurl[0], matheduurl[1]))

    # 사과대 정치외교학과        <- some character error
    politicsurl = ["http://politics.kangwon.ac.kr", "/bbs/board.php?bo_table=sub9_3"]
    # politicsarr = politics(callreq(politicsurl[0], politicsurl[1]))

    # 사과대 행정학전공
    padmurl = "https://padm.kangwon.ac.kr:40592/bbs/board.php?bo_table=sub3_2"
    # padmarr = padm(callurl('', padmurl))

    # 인문대
    humanitiesurl = "http://humanities.kangwon.ac.kr/sub04_01.php"
    # humanitiesarr = humanities(callreq('', humanitiesurl))

    # 인문대 국어국문학전공       <- some character error, 글자 깨짐 확인 필요
    koreanurl = ["http://korean.kangwon.ac.kr/2013", "/bbs/board.php?bo_table=sub05_01"]
    # koreanarr = korean(callreq(koreanurl[0], koreanurl[1]))

    # 인문대 불어불문학과
    franceurl = ["http://france.kangwon.ac.kr", "/?doc=bbs/board.php&bo_table=gongi"]
    # francearr = france(callreq(franceurl[0], franceurl[1]))

    # 자연대 물리학과
    physicsurl = ["http://physics.bluechips.co.kr", "/sub46"]
    # physicsarr = physics(callreq(physicsurl[0], physicsurl[1]))

    # 자연대 수학과
    mathurl = "http://math.kangwon.ac.kr/xe/notice"
    # physicsarr = physics(callreq('', mathurl))

    # 부속시설
    # 백령아트센터
    kwbcurl = ["https://kwbc.kangwon.ac.kr/kwbc/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # kwbcarr = dbe(callreq(kwbcurl[0], kwbcurl[1]))

    # 창업지원단
    ksefurl = ['http://ksef.kangwon.ac.kr/',
               'board_list.asp?boardCode=notice&blang=&searchBoardField=&searchBoardText=&page=1&delMain=&cpSection=']
    # ksefarr = ksef(callreq(ksefurl[0], ksefurl[1]))

    # 링크사업단
    lincurl = ['http://linc.kangwon.ac.kr/', 'index.php?mp=4_1']
    # lincarr = it(callreq(lincurl[0], lincurl[1]))

    # 교양교육원
    kileurl = ['https://kile.kangwon.ac.kr/kile/', 'bbs_list.php?code=sub07a&keyvalue=sub07']
    # kilearr = dbe(callreq(kileurl[0], kileurl[1]))

    # 중앙박물관
    museumurl = ['http://museum.kangwon.ac.kr', '/bbs/board.php?bo_table=km_notice']
    # museumarr = museum(callreq(museumurl[0], museumurl[1]))

    # SW중심대학사업단
    swunivurl = ['http://swuniv.kangwon.ac.kr/', 'index.php?mp=5_1']
    # swunivarr = it(callreq(swunivurl[0], swunivurl[1]))

    # 인권센터
    khrurl = ['http://khr.kangwon.ac.kr/', 'index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1']
    # khrarr = it(callreq(khrurl[0], khrurl[1]))

    # 체육진흥원
    sportsurl = 'http://sports.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1'
    # sportsarr =

    # 학생생활관
    knudormurl = ['http://knudorm.kangwon.ac.kr', '/home/sub04/index.jsp?board_nm=notice']
    # knudormarr = knudorm(callreq(knudormurl[0], knudormurl[1]))

    # 교육혁신원
    itlurl = ['https://itl.kangwon.ac.kr', '/ko/community/notice']
    # itlurl = itl(callreq(itlurl[0], itlurl[1]))

    # 창업보육센터
    kwbiurl = ['http://kwbi.kangwon.ac.kr/', 'board_list.asp?boardcode=notice']
    kwbiarr = kwbi(callreq(kwbiurl[0], kwbiurl[1]))
