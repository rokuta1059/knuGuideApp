import requests
from bs4 import BeautifulSoup as bs
import ssl
from urllib.request import urlopen

# 컴퓨터공학과
def cse():
    baseURL = "https://cse.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=5_1_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "bbs_list").tbody.tr

    while keyword.next_sibling != None:
        url = baseURL+keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('div', "bbs_view").dl.dt
        print(title.text, keyword.find('td', "dt").text)
        print(url)
        keyword = keyword.next_sibling


# 경영대
def cba():
    resp = requests.get('http://cba.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('h1', id="bo_v_title")
        print(title.text.strip(), keyword.find('td', "td_date").text)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 경영학전공
def biz():
    resp = requests.get('http://biz.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling
    baseURL = "http://biz.kangwon.ac.kr/"

    while keyword != None:
        url = baseURL+keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('table', "board_view").td
        print(title.text.strip(), keyword.find('td', "datetime").text)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 회계학전공
def account():
    resp = requests.get('http://account.kangwon.ac.kr/bbs/board.php?bo_table=notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-hover").tbody.tr

    while keyword != None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('h4', "subject")
        print(title.text.strip().rstrip("new").strip())
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 경제학전공
def economics():
    resp = requests.get('http://economics.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('article', id="bo_v").header
        print(title.text.strip(), keyword.find('td', "td_date").text)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 정보통계학전공
def statistics():
    resp = requests.get('http://statistics.kangwon.ac.kr/board/bbs/board.php?bo_table=notice_bbs')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling
    baseURL = "http://statistics.kangwon.ac.kr/board"

    while keyword != None:
        url = baseURL+keyword.a.get('href').lstrip("..")
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('div', id="conbox").td.td.div
        print(title.text.strip(), keyword.find('td', "datetime").text)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 관광경영학과
def tourism():
    resp = requests.get('http://tourism.kangwon.ac.kr/bbs/board.php?bo_table=notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "bo_notice")

    while keyword != None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('h1', id="bo_v_title")
        print(title.text.strip(), keyword.find('td', "td_date").text)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 국제무역학과
def itb():
    resp = requests.get('http://itb.kangwon.ac.kr/bbs/board.php?bo_table=notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "bo_notice")

    while keyword != None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('h4', id="bo_v_title").b
        date = soup.find('ul', "list-inline").li.next_sibling.next_sibling.b
        print(title.text, date.text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대
def agrilifesci():
    baseURL = "http://knucals.kangwon.ac.kr/contents.do?v="
    lastURL = "&pageIndex=1&divId=&searchCondition=1&searchKeyword="
    resp = requests.get(baseURL+"&id=&cid=db2548006ba044b09df5b6b5df7aacd4&masterId=03cc0395b649458081fb83355712a973"+lastURL)
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('div', "board_body").ul.li
    cId = "&cid=db2548006ba044b09df5b6b5df7aacd4"
    masterId = "&masterid=03cc0395b649458081fb83355712a973"

    while keyword != None:
        title = keyword.find('a', "content-list")
        date = keyword.find('span', "board_col date").text.lstrip("등록일")
        print(title.text.strip(), date.strip())
        print(baseURL + "view&id="+ keyword.a.get('data-id')+cId+masterId+lastURL)
        keyword = keyword.next_sibling.next_sibling


# 농생대 미래농업융합학부
def cll():
    resp = requests.get('http://cll.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 바이오시스템기계공학전공
def bse():
    resp = requests.get('http://bse.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 식품생명공학전공
def foodtech():
    resp = requests.get('http://foodtech.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 식물자원응용과학전공
def appliedplant():
    resp = requests.get('http://appliedplant.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 응용생물학전공
def applybio():
    resp = requests.get('http://applybio.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 원예과학전공
def horti():
    resp = requests.get('http://horti.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 농업자원경제학전공
def agecon():
    resp = requests.get('http://agecon.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 지역건설공학과
def aed():
    resp = requests.get('http://aed.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대 바이오자원환경학전공 - 미완성
def dbe():
    baseURL = "https://dbe.kangwon.ac.kr/dbe/"
    resp = requests.get(baseURL+'bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "text-center hidden-xs").parent
    while keyword != None:
        resp = requests.get(baseURL + keyword.a.get('href'))
        soup = bs(resp.content, "html.parser")
        title = soup.find('div', "table-responsive").tr.td.next_sibling.next_sibling
        print(title.text)
        keyword = keyword.next_sibling.next_sibling


# 농생대 에코환경 - 미완성 바이오자원환경과 형식 똑같음
def ecoenv():
    resp = requests.get('https://ecoenv.kangwon.ac.kr/ecoenv/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 동생대
def anilifsci():
    resp = requests.get('http://cals.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        title = keyword.find('div', "bo_tit")
        print(title.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 동생대 동물산업융합학과
def animal():
    resp = requests.get('http://animal.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        title = keyword.find('div', "bo_tit")
        print(title.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 동생대 동물응용과학과
def applanimalsci():
    resp = requests.get('http://applanimalsci.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        title = keyword.find('div', "bo_tit")
        print(title.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 동생대 동물자원과학과
def aniscience():
    resp = requests.get('http://aniscience.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        title = keyword.find('div', "bo_tit")
        print(title.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 문예대공대
def ace():
    baseURL = "http://ace.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=4_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "bbs_list").tbody.tr

    while keyword.next_sibling != None:
        url = baseURL + keyword.a.get('href')
        resp = requests.get(url)
        soup = bs(resp.content, "html.parser")
        title = soup.find('div', "bbs_view").dl.dt
        print(title.text, keyword.find('td', "dt").text)
        print(url)
        keyword = keyword.next_sibling


# 문예대공대 건축학과(5년제)
def architecture():
    baseURL = "http://architecture.kangwon.ac.kr/nano/www/board/"
    resp = requests.get(baseURL+'list.php?bid_=comm_notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "left").parent

    while keyword != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text, date.text)
        print(baseURL+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 문예대공대 건축공학전공
def archi():
    baseURL = "http://archi.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=4_1_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 토목공학전공
def civil():
    baseURL = "http://civil.kangwon.ac.kr/2014/"
    resp = requests.get(baseURL+'index.php?mp=6_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 환경공학전공
def environ():
    resp = requests.get('https://environ.kangwon.ac.kr/environ/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 문예대공대 기계의용공학전공
def mechanical():
    baseURL = "http://mechanical.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=5_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 메카트로닉스공학전공
def mecha():
    resp = requests.get('https://mecha.kangwon.ac.kr/mecha/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 문예대공대 재료공학전공
def material():
    baseURL = "http://material.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=5_1_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 에너지자원공학전공
def enre():
    baseURL = "http://www.enre.kr/"
    resp = requests.get(baseURL+'index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 산업공학전공
def sme():
    baseURL = "http://sme.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=5_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 화학공학전공
def chemeng():
    baseURL = "http://chemeng.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 생물공학전공
def bioeng():
    baseURL = "http://bioeng.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=6_1_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 디자인학과
def design():
    resp = requests.get('https://design.kangwon.ac.kr/design/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 문예대공대 무용학과 <- 터졋나?

# 문예대공대 미술학과
def art():
    baseURL = "http://kangwonart.ac.kr"
    resp = requests.get(baseURL+'/wp/?page_id=1782')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "kboard-list-notice")

    while keyword != None:
        title = keyword.find('div', "cut_strings").a
        date = keyword.find('td', "kboard-list-date")
        print(title.text.strip(), date.text)
        print(baseURL+title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 문예대공대 스포츠과학과
def sport():
    baseURL = "http://sport.kangwon.ac.kr"
    resp = requests.get('http://sport.kangwon.ac.kr/sub2/04.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL+keyword.get('src').lstrip("."))
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "bo_notice")

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 문예대공대 영상문화학과
def vculture():
    resp = requests.get('https://vculture.kangwon.ac.kr/vculture/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 사범대
def educatio():
    baseURL = "http://educatio.kangwon.ac.kr/"
    resp = requests.get(baseURL+'sub04_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL + keyword.get('src').lstrip("."))
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', align="center").table.next_sibling.next_sibling
    array = keyword.find_all('tr', align="center")

    for i in range(1, len(array)):
        date = array[i].find_all('span', "tt")
        if i == 1:
            date = date[3]
        else:
            date = date[4]
        print(array[i].a.text, date.text)
        print(baseURL+array[i].a.get('href').lstrip("./"))


# 사범대 교육학과
def edu():
    baseURL = "http://edu.kangwon.ac.kr/new/"
    resp = requests.get(baseURL+'sub04_02.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL + keyword.get('src').lstrip("."))
    soup = bs(resp.content, "html.parser")
    array = soup.find_all('td', align="left")
    date = soup.find_all('td', "text8")

    for i in range(0, len(array)-2):
        print(array[i].text.strip(), date[i*2].text)
        print(baseURL+array[i].a.get('href').lstrip("./"))


# 사범대 국어교육과
def kedu():
    baseURL = "http://kedu.kangwon.ac.kr/"
    resp = requests.get(baseURL+'sub05_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('div', id="con").find('iframe')
    resp = requests.get(baseURL+keyword.get('src'))
    soup = bs(resp.content, "html.parser")
    array = soup.find_all('td', "text_03")
    for i in range(0, len(array)):
        title = array[i]
        date = array[i]
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        print(title.text.strip(), date.text)
        link = title.a.next_sibling.next_sibling.get('onclick')
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://kedu.kangwon.ac.kr/board/dboard.php?id=com1&notice_id=&s=&tot=&search=&search_cond=&no="+linkarray[0]+"&print_no="+linkarray[1]+"&exec=view&npop=&sort=&desc=&search_cat_no="
        print(url)


# 사범대 역사교육과
def history():
    baseURL = "http://history.kangwon.ac.kr/"
    resp = requests.get(baseURL+'index.php?mp=5_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling != None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text.strip(), date.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 사범대 영어교육과
def engedu():
    baseURL = "http://engedu.kangwon.ac.kr/twb_bbs/"
    resp = requests.get(baseURL+'user_bbs_list.php?bcd=01_06_04_00_00')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('div', id="user_board_list").tr.next_sibling.next_sibling

    while keyword != None:
        title = keyword.find('td', "title")
        date = keyword.find('td', "date")
        print(title.text, date.text)
        print(baseURL+title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사범대 윤리교육과
def ethicsedu():
    baseURL = "http://ethicsedu.kangwon.ac.kr/new/"
    resp = requests.get(baseURL+'sub06_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL+keyword.get('src'))
    soup = bs(resp.content, "html.parser")
    #print(soup)
    array = soup.find_all('td', align="left")
    date = soup.find_all('td', "text8")
    for i in range(0, len(array)-2):
        print(array[i].text.strip(), date[i*2].text)
        print(baseURL+array[i].a.get('href').lstrip("./"))


# 사범대 일반사회교육과
def ssedu():
    baseURL = "http://ssedu.kangwon.ac.kr/new/"
    resp = requests.get(baseURL+'sub02_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL + keyword.get('src'))
    soup = bs(resp.content, "html.parser")

    array = soup.find_all('td', align="left")
    date = soup.find_all('td', "text8")
    for i in range(0, len(array) - 2):
        print(array[i].text.strip(), date[i * 2].text)
        print(baseURL + array[i].a.get('href').lstrip("./"))


# 사범대 지리교육과
def geoedu():
    resp = requests.get('http://geoedu.kangwon.ac.kr/department/notice.php')
    soup = bs(resp.content, "html.parser")
    array = soup.find_all('td', "text-left")

    for i in range(0, len(array)):
        title = array[i].a
        date = array[i].next_sibling.next_sibling
        id = array[i].a.get('href').lstrip("javascript:ContentsView(").rstrip(");")
        link = "http://geoedu.kangwon.ac.kr/department/notice.php?boardID=notice&b_id="+id+"&mode=view&npage=1&search_opt=b_writerName&search_text=&pw_check1=&pw_check2=&pw_check3=&pw_check4=&pw_check5=&pw_check6=&pw_check7=&pw_check8=&pw_check9=&pw_check10="
        print(title.text, date.text)
        print(link)


# 사범대 한문교육과
def ccedu():
    baseURL = "http://ccedu.kangwon.ac.kr/new/"
    resp = requests.get(baseURL+'sub03_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL + keyword.get('src'))
    soup = bs(resp.content, "html.parser")
    # print(soup)
    array = soup.find_all('td', align="left")
    date = soup.find_all('td', "text8")
    for i in range(0, len(array) - 2):
        print(array[i].text.strip(), date[i * 2].text)
        print(baseURL + array[i].a.get('href').lstrip("./"))


# 사범대 가정교육과
def homecs():
    baseURL = "http://homecs.kangwon.ac.kr/bbs/"
    resp = requests.get(baseURL+'zboard.php?id=bbs31&page=1&page_num=20&category=&sn=off&ss=on&sc=on&keyword=&prev_no=504&sn1=&divpage=1&select_arrange=headnum&desc=asc')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "title").next_sibling.next_sibling

    while keyword != None:
        title = keyword.a
        date = keyword.find('td', "list_eng3")
        print(title.text, date.text)
        print(baseURL+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사범대 과학교육학부
def sciedu():
    baseURL = "http://sciedu.kangwon.ac.kr/new/"
    resp = requests.get(baseURL+'sub02_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL + keyword.get('src'))
    soup = bs(resp.content, "html.parser")
    # print(soup)
    array = soup.find_all('td', align="left")
    date = soup.find_all('td', "text8")
    for i in range(0, len(array) - 2):
        print(array[i].text.strip(), date[i * 2].text)
        print(baseURL + array[i].a.get('href').lstrip("./"))


# 사범대 수학교육과
def mathedu():
    baseURL = "http://mathedu.kangwon.ac.kr/"
    resp = requests.get(baseURL+'main.php?mt=page&mp=3_1&mm=oxbbs&oxid=2')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', height="25")

    while keyword.next_sibling != None:
        title = keyword.a
        date = keyword.find_all('td', align="center")[2]
        print(title.text.strip(), date.text)
        print(baseURL+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling.next_sibling


# 사범대 체육교육과
def phyedu():
    baseURL = "http://phyedu.kangwon.ac.kr/"
    resp = requests.get(baseURL+'sub5/03.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('iframe')
    resp = requests.get(baseURL+keyword.get('src').lstrip("./"))
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사과대
def social():
    baseURL = "http://social.kangwon.ac.kr/bbs/"
    resp = requests.get(baseURL+'zboard.php?id=notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword)/2)):
        title = keyword[i*2].a
        print(title.text.strip(), date[i+i*2+1].text)
        print(baseURL+title.get('href'))


# 사과대 문화인류학과
def anthro():
    baseURL = "http://anthro.kangwon.ac.kr/bbs/"
    resp = requests.get(baseURL+'zboard.php?id=bbs41')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword) / 2)):
        title = keyword[i * 2].a
        print(title.text.strip(), date[i + i * 2 + 1].text)
        print(baseURL + title.get('href'))


# 사과대 부동산학과
def re():
    baseURL = "http://re1978.kangwon.ac.kr/bbs/"
    resp = requests.get(baseURL+'zboard.php?id=bbs41')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword) / 2)):
        title = keyword[i * 2].a
        print(title.text.strip(), date[i + i + i * 2 + 1].text)
        print(baseURL + title.get('href'))


# 사과대 사회학과
def sociology():
    baseURL = "http://sociology.kangwon.ac.kr/bbs/"
    resp = requests.get(baseURL+'zboard.php?id=notice')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword) / 2)):
        title = keyword[i * 2].a
        print(title.text.strip(), date[i + i + i * 2 + 1].text)
        print(baseURL + title.get('href'))


# 사과대 신문방송학과
def masscom():
    baseURL = "http://masscom.kangwon.ac.kr"
    resp = requests.get(baseURL+'/bbs/board.php?bo_table=sub3_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling

    while keyword != None:
        title = keyword.find('td', "subject").find_all('a')[1]
        date = keyword.find('td', "datetime")
        print(title.text.strip(), date.text)
        print(baseURL+title.get('href').lstrip("."))
        keyword = keyword.next_sibling.next_sibling


# 사과대 정치외교학과
def politics():
    baseURL = "http://politics.kangwon.ac.kr"
    resp = requests.get(baseURL+'/bbs/board.php?bo_table=sub9_3')
    soup = bs(resp.content, "html.parser")
    print(soup)


# 사과대 행정학전공
def padm():
    context = ssl._create_unverified_context()
    resp = urlopen('https://padm.kangwon.ac.kr:40592/bbs/board.php?bo_table=sub3_2', context=context)
    soup = bs(resp.read(), "html.parser")
    keyword = soup.find_all('li', "list-item")

    for i in range(0, len(keyword)):
        title = keyword[i].find('div', "wr-subject").a
        date = keyword[i].find('div', "wr-date hidden-xs")
        if title.find('i') != None:
            start = len(title.find('span').b.text)
            end = len(title.text)
            title = title.text[start*4-1:end-start*2]
            print(title.strip(), date.text.strip())
        else:
            print(title.text.strip(), date.text.strip())
        print(keyword[i].find('div', "wr-subject").a.get('href'))


# 사과대 심리학전공
def psych():
    baseURL = "http://psych.kangwon.ac.kr/gnuboard4"
    resp = requests.get(baseURL+'/bbs/board.php?bo_table=bbs41')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling

    while keyword != None:
        title = keyword.find('td', "subject").find_all('a')[1]
        date = keyword.find('td', "datetime")
        print(title.text.strip(), date.text)
        print(baseURL + title.get('href').lstrip("."))
        keyword = keyword.next_sibling.next_sibling


# 산림대
def forest():
    resp = requests.get('http://forest.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 산림대 산림경영학전공
def fm():
    resp = requests.get('http://fm.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        title = keyword.find('div', "bo_tit")
        print(title.a.text.strip(), keyword.find('td', "td_datetime").text)
        print(title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 산림대 산림자원학전공
def forestry():
    resp = requests.get('http://forestry.kangwon.ac.kr/bbs/board.php?bo_table=sub04_01')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 산림대 산림환경보호학전공
def fep():
    baseURL = "http://fep.kangwon.ac.kr/"
    resp = requests.get(baseURL+'bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling

    while keyword != None:
        title = keyword.find('td', "subject").a
        date = keyword.find('td', "datetime")
        print(title.text.strip(), date.text)
        print(baseURL + title.get('href').lstrip("."))
        keyword = keyword.next_sibling.next_sibling


# 산림대 산림소재공학전공
def wood():
    context = ssl._create_unverified_context()
    resp = urlopen('https://wood.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1', context=context)
    soup = bs(resp.read(), "html.parser")
    keyword = soup.find('td', "td_subject").parent

    while keyword != None:
        print(keyword.a.text.strip(), keyword.find('td', "td_date").text)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 산림대 제지공학전공
def paper():
    baseURL = "https://paper.kangwon.ac.kr/board"
    context = ssl._create_unverified_context()
    resp = urlopen(baseURL+'/bbs/board.php?bo_table=sub03_1', context=context)
    soup = bs(resp.read(), "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling

    while keyword != None:
        title = keyword.find('td', "subject").a
        date = keyword.find('td', "datetime")
        print(title.text.strip(), date.text)
        print(baseURL + title.get('href').lstrip("."))
        keyword = keyword.next_sibling.next_sibling


# 산림대 생태조경디자인학과
def lands():
    resp = requests.get('http://lands.kangwon.ac.kr/bbs/board.php?bo_table=sub03_01')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tr', "bo_notice")

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date narrow_view")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 수의과
def vetmed():
    baseURL = "http://vetmed.kangwon.ac.kr"
    resp = requests.get(baseURL+'/bbs/board.php?bo_table=sub07_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "board_list").tr.next_sibling.next_sibling


    while keyword != None:
        title = keyword.find('span', "small").next_sibling.next_sibling
        date = keyword.find('td', "datetime")
        print(title.text, date.text)
        print(baseURL+title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 약대
def pharmacy():
    resp = requests.get('http://pharmacy.kangwon.ac.kr/bbs/board.php?bo_table=sub02_05')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 간호대
def nurse():
    resp = requests.get('https://nurse.kangwon.ac.kr/nurse/bbs_list.php?code=sub07a&keyvalue=sub07')
    soup = bs(resp.content, "html.parser")


# 의생대
def bm():
    resp = requests.get('http://bmcollege.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 분자생명과학과
def molscien():
    resp = requests.get('http://molscien.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 생명건강공학과
def biohealth():
    resp = requests.get('http://bio-health.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 생물의소재공학과
def bme():
    resp = requests.get('http://bme.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 의생명융합학부
def bmc():
    resp = requests.get('http://bmc.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 시스템면역과학전공
def si():
    resp = requests.get('http://si.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 의생대 의생명공학전공
def dmbt():
    resp = requests.get('http://dmbt.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('table', "table table-striped").tbody.tr

    while keyword != None:
        title = keyword.find('td', "td_subject").a
        date = keyword.find('td', "td_date")
        print(title.text.strip(), date.text)
        print(title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 인문대
def humanities():
    resp = requests.get('http://humanities.kangwon.ac.kr/sub04_01.php')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('table', align="center")

    for i in range(5, len(keyword)-2):
        title = keyword[i].find('td', "text_03")
        date = keyword[i].find('td', "text_03")
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        print(title.text.strip(), date.text)
        link = title.a.get('onclick')
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://humanities.kangwon.ac.kr/sub04_01.php?id=notice&notice_id=&s=&tot=&search=&search_cond=&no="+linkarray[0]+"&print_no="+linkarray[1]+"&exec=view&npop=&sort=&desc=&search_cat_no="
        print(url)


# 인문대 국어국문학전공
def korean():
    baseURL = "http://korean.kangwon.ac.kr/2013"
    resp = requests.get(baseURL+'/bbs/board.php?bo_table=sub05_01')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find_all('tr', align="center")

    for i in range(1, len(keyword)):
        title = keyword[i].find('td', align="left").a
        date = keyword[i].find('td', align="center")
        print(keyword[i])
        #print(title.text, date.text)
        print(baseURL+title.get('href').lstrip("."))

if __name__ == '__main__':
    #cse()
    #cba()
    #biz()
    #account()
    #economics()
    #statistics()
    #tourism()
    #itb()
    #agrilifesci()
    #cll()          <-
    #bse()          <- url제외 코드 똑같음
    #foodtech()     <- datetime -> date 제외 똑같음
    #appliedplant() <- datetime -> date 제외 똑같음
    #applybio()     <- datetime -> date 제외 똑같음
    #horti()        <- datetime -> date 제외 똑같음
    #agecon()       <- datetime -> date 제외 똑같음
    #aed()          <- datetime -> date 제외 똑같음
    #dbe()
    #anilifsci()        <-
    #animal()           <-
    #applanimalsci()    <-
    #aniscience()       <- url제외 코드 똑같음
    #ace()                  <- 컴공이랑 url제외 코드 똑같음
    #architecture()
    #archi()                    <-
    #civil()                    <-
    #environ()
    #mechanical()               <-
    #mecha()
    #material()                 <-
    #enre()                     <-
    #sme()                      <-
    #chemeng()                  <-
    #bioeng()                   <-
    #design()
    #art()
    #sport()       <- datetime -> date 제외 똑같음
    #vculture()
    #educatio()
    #edu()
    #kedu()
    #history()                  <- url제외 코드 똑같음
    #engedu()
    #ethicsedu()                    <-
    #ssedu()                        <-
    #geoedu()
    #ccedu()                        <-
    #homecs()
    #sciedu()                       <- url제외 코드 똑같음
    #mathedu()
    #phyedu()
    #social()                           <-
    #anthro()                           <- url 제외 코드 똑같음
    #re()                               <- i+, url 제외 코드 똑같음
    #sociology()                        <- i+, url 제외 코드 똑같음
    #masscom()                              <-
    #politics() <- 미완
    #padm()
    #psych()                                <- url 제외 코드 똑같음
    #forest()        <- datetime -> date 제외 코드 똑같음
    #fm()               <- url 제외 코드 똑같음
    #forestry()     <- datetime -> date 제외 코드 똑같음
    #fep()                                          <-
    #wood()         <- url, soup, datetime -> date 제외 코드 똑같음
    #paper()                                        <- url, soup 제외 코드 똑같음
    #lands()
    #vetmed()       <- biz와 매우 비슷
    #pharmacy()     <- cba와 매우 비슷
    #nurse()
    #bm()           <- pharmacy와 url제외 코드 똑같음
    #molscien()     <- bm과 url 제외 코드 똑같음
    #biohealth()    <- bm과 url 제외 코드 똑같음
    #bme()          <- bm과 url 제외 코드 똑같음
    #bmc()          <- bm과 url 제외 코드 똑같음
    #si()           <- bm과 url 제외 코드 똑같음
    #dmbt()         <- bm과 url 제외 코드 똑같음
    #humanities()
    korean()