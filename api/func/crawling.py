import ssl
from urllib.request import urlopen

import requests
from bs4 import BeautifulSoup

baseurl = ""


def callreq(base, url):
    global baseurl
    baseurl = base
    resp = requests.get(base+url)
    soup = BeautifulSoup(resp.content, "html.parser")
    return soup


def callurl(base, url):
    global baseurl
    baseurl = base
    context = ssl._create_unverified_context()
    resp = urlopen(base+url, context=context)
    soup = BeautifulSoup(resp.read(), "html.parser")
    return soup


# 학과 통합
def cba(soup, callsign):
    if soup.find('iframe') is not None:
        soup = callreq(baseurl, soup.find('iframe').get('src').lstrip("."))
    keyword = soup.find('td', "td_subject").parent

    while keyword is not None:
        url = keyword.find('td', "td_subject").a.get('href')
        if callsign == "url":
            soup = callurl('', url)
        elif callsign == "req":
            soup = callreq('', url)
        title = soup.find('h1', id="bo_v_title")
        date = soup.find('section', id="bo_v_info").find_all('strong')[1]
        datetext = "20"+date.text.split(" ")[0]
        print(title.text.strip(), datetext.strip())
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


def biz(soup):
    global baseurl
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        title = keyword.find_all('a')
        num = len(title)
        date = keyword.find('td', "datetime").text.replace(".", "-", 2)
        if len(date) <= 9:
            date = "20"+date
        print(title[num-1].text.strip(), date)
        print(baseurl+title[num-1].get('href').lstrip("."))
        keyword = keyword.next_sibling.next_sibling


# 경영대 회계학전공
def account(soup):
    keyword = soup.find('table', "table table-hover").tbody.tr

    while keyword is not None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('h4', "subject").text.strip().rstrip("new")
        date = soup.find('div', "desc").find_all('strong')[1]
        datetext = date.text.split(" ")[0]
        print(title.strip(), datetext)
        print(url)
        keyword = keyword.next_sibling.next_sibling


# 경영대 국제무역학과
def itb(soup):
    keyword = soup.find('tr', "bo_notice")

    while keyword is not None:
        url = keyword.a.get('href')
        resp = requests.get(url)
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('h4', id="bo_v_title").b
        date = soup.find('ul', "list-inline").li.next_sibling.next_sibling.b
        datetext = "20"+date.text.split(" ")[0]
        print(title.text, datetext)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 농생대
def agrilifesci():
    base = "http://knucals.kangwon.ac.kr/contents.do?v="
    lasturl = "&pageIndex=1&divId=&searchCondition=1&searchKeyword="
    cid = "&cid=db2548006ba044b09df5b6b5df7aacd4"
    masterid = "&masterid=03cc0395b649458081fb83355712a973"
    resp = requests.get(base+"&id="+cid+masterid+lasturl)
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "board_body").ul.li

    while keyword is not None:
        title = keyword.find('a', "content-list")
        date = keyword.find('span', "board_col date").text.lstrip("등록일")
        print(title.text.strip(), date.strip())
        print(base + "view&id=" + keyword.a.get('data-id')+cid+masterid+lasturl)
        keyword = keyword.next_sibling.next_sibling


def cll(soup):
    keyword = soup.find('td', "td_subject").parent

    while keyword is not None:
        url = keyword.find('div', "bo_tit").a.get('href')
        soup = callreq('', url)
        title = soup.find('span', "bo_v_tit")
        date = soup.find('section', id="bo_v_info").find('strong', "if_date")
        datetext = "20"+date.text.split(" ")[1]
        print(title.text.strip(), datetext.strip())
        print(url)
        keyword = keyword.next_sibling.next_sibling


def dbe(soup):
    global baseurl
    keyword = soup.find('td', 'text-center hidden-xs').parent

    while keyword is not None:
        if keyword.find('td') != -1:
            array = keyword.find_all('td')
            title = array[1].a
            date = array[3].text.replace("/", "-", 2)
            print(title.text.strip(), date)
            print(baseurl+title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 문예대공대 건축학과(5년제)
def architecture(soup):
    global baseurl
    keyword = soup.find('td', "left").parent

    while keyword is not None:
        title = keyword.a
        date = title.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(title.text, date.text)
        print(baseurl+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


def archi(soup):
    global baseurl
    keyword = soup.find('td', "tit").parent

    while keyword.next_sibling is not None:
        date = keyword.a.parent
        for i in range(0, 4):
            date = date.next_sibling
        print(keyword.a.text.strip(), date.text)
        print(baseurl + keyword.a.get('href'))
        keyword = keyword.next_sibling


# 문예대공대 무용학과 <- 터졋나?

# 문예대공대 미술학과
def art(soup):
    global baseurl
    keyword = soup.find('tr', "kboard-list-notice")

    while keyword is not None:
        title = keyword.find('div', "cut_strings").a
        date = keyword.find('td', "kboard-list-date")
        print(title.text.strip(), date.text.replace(".", "-", 2))
        print(baseurl+title.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사범대
def educatio(soup):
    global baseurl
    keyword = soup.find('iframe').get('src').lstrip(".")
    soup = callreq(baseurl, keyword)
    keyword = soup.find('table', align="center").table.next_sibling.next_sibling
    array = keyword.find_all('tr', align="center")

    for i in range(1, len(array)):
        date = array[i].find_all('span', "tt")
        if i == 1:
            date = date[3]
        else:
            date = date[4]
        print(array[i].a.text, "20"+date.text)
        print(baseurl+array[i].a.get('href').lstrip("./"))


def edu(soup):
    global baseurl
    keyword = soup.find('iframe')
    soup = callreq(baseurl, keyword.get('src').lstrip("."))

    arr = soup.find_all('td', align="left")
    for i in range(0, len(arr) - 2):
        url = arr[i].a.get('href').lstrip(".")
        soup = callreq(baseurl, url)
        date = soup.find_all('span', "text_bold888")
        datetext = date[1].next_sibling.next_sibling.text.split("조회")[0]
        print(arr[i].text.strip(), datetext.strip())
        print(baseurl + url)


# 사범대 국어교육과
def kedu(soup):
    global baseurl
    keyword = soup.find('div', id="con").find('iframe').get('src')
    soup = callreq(baseurl, keyword)
    array = soup.find_all('td', "text_03")
    for i in range(0, len(array)):
        title = array[i]
        date = array[i]
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        print(title.text.strip(), date.text.replace(".", "-", 2))
        link = title.a.next_sibling.next_sibling.get('onclick')
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://kedu.kangwon.ac.kr/board/dboard.php?id=com1&notice_id=&s=&tot=&search=&search_cond=&no=" \
              + linkarray[0] + "&print_no=" + linkarray[1] + "&exec=view&npop=&sort=&desc=&search_cat_no="
        print(url)


# 사범대 영어교육과
def engedu(soup):
    global baseurl
    keyword = soup.find('div', id="user_board_list").tr.next_sibling.next_sibling

    while keyword is not None:
        title = keyword.find('td', "title")
        date = keyword.find('td', "date")
        print(title.text, date.text)
        print(baseurl+title.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사범대 지리교육과
def geoedu(soup):
    global baseurl
    array = soup.find_all('td', "text-left")

    for i in range(0, len(array)):
        title = array[i].a
        date = array[i].next_sibling.next_sibling
        b_id = array[i].a.get('href').lstrip("javascript:ContentsView(").rstrip(");")
        link = baseurl+"?boardID=notice&b_id="+b_id+"&mode=view&npage=1&search_opt=b_writerName&search_text="
        link = link + "&pw_check1=&pw_check2=&pw_check3=&pw_check4=&pw_check5" \
                      "=&pw_check6=&pw_check7=&pw_check8=&pw_check9=&pw_check10="
        print(title.text, date.text)
        print(link)


# 사범대 가정교육과
def homecs(soup):
    global baseurl
    keyword = soup.find('tr', "title").next_sibling.next_sibling

    while keyword is not None:
        title = keyword.a
        date = keyword.find_all('td', "list_eng3")[1]
        datetext = date.text.replace("·", "-", 2)
        print(title.text, datetext)
        print(baseurl+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 사범대 수학교육과
def mathedu(soup):
    global baseurl
    keyword = soup.find('tr', height="25")

    while keyword.next_sibling is not None:
        title = keyword.a
        date = keyword.find_all('td', align="center")[2]
        print(title.text.strip(), date.text)
        print(baseurl+keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling.next_sibling


def social(soup, sign):
    global baseurl
    keyword = soup.find_all('td', "list_han3")
    date = soup.find_all('td', "list_eng3")

    for i in range(0, int(len(keyword) / 2)):
        title = keyword[i * 2].a
        if sign == 'i':
            datetext = date[i + i + i * 2 + 1].text.replace("·", "-", 2)
            print(title.text.strip(), datetext)
        else:
            datetext = date[i + i * 2 + 1].text.replace("·", "-", 2)
            print(title.text.strip(), datetext)
        print(baseurl+title.get('href'))


def masscom(soup):
    global baseurl
    if soup.find('iframe').get('src') is not None:
        soup = callreq(baseurl, soup.find('iframe').get('src').lstrip("."))
    keyword = soup.find('tr', "bg1")

    while keyword is not None:
        url = keyword.find_all('a')
        num = len(url)
        url = url[num-1].get('href').lstrip(".")
        soup = callreq(baseurl, url)
        arr = soup.find('div', style="clear:both; height:30px;")
        title = arr.next_sibling.next_sibling.text.replace(" ", "", 24)
        date = "20"+arr.text.split(" ")[2]
        print(title.strip(), date)
        print(baseurl+url)
        keyword = keyword.next_sibling.next_sibling


# 사과대 정치외교학과
def politics(soup):
    global baseurl
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        url = keyword.a.get('href').lstrip(".")
        soup = callreq(baseurl, url)
        title = keyword.a
        date = soup.find('div', "info").find('span', "date").text.split(" ")[1]
        print(title.text, "20"+date)
        print(baseurl+url)
        keyword = keyword.next_sibling.next_sibling


# 사과대 행정학전공
def padm(soup):
    keyword = soup.find('li', "list-item")

    while keyword is not None:
        soup = callurl('', keyword.a.get('href'))
        title = soup.find('h1')
        date = soup.find('div', "panel-heading").find_all('span')
        num = len(date)
        datetext = date[num-1].get('content').split("KST")[0]
        print(title.text.strip(), datetext)
        print(keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


# 인문대
def humanities(soup):
    keyword = soup.find_all('table', align="center")

    for i in range(5, len(keyword)-2):
        title = keyword[i].find('td', "text_03")
        date = keyword[i].find('td', "text_03")
        for j in range(0, 4):
            title = title.previous_sibling
            date = date.next_sibling
        print(title.text.strip(), date.text.replace(".", "-", 2))
        link = title.a.get('onclick')
        linkarray = link.lstrip("viewPage('").rstrip("');return false;").split("','")
        url = "http://humanities.kangwon.ac.kr/sub04_01.php?id=notice&notice_id=&s=&tot=&search=&search_cond=&no="\
              + linkarray[0]+"&print_no="+linkarray[1]+"&exec=view&npop=&sort=&desc=&search_cat_no="
        print(url)


# 인문대 국어국문학전공
def korean(soup):
    global baseurl
    keyword = soup.find_all('tr', align="center")
    for i in range(1, len(keyword)):
        title = keyword[i].find('td', align="left").a
        date = soup.find_all('span', "member")[i-1].parent.parent.next_sibling.next_sibling
        print(title.text, date.text)
        print(baseurl+title.get('href').lstrip("."))


# 인문대 불어불문학과
def france(soup):
    global baseurl
    keyword = soup.find_all('tr', align="center")

    for i in range(0, len(keyword)):
        title = keyword[i].a
        date = keyword[i].find_all('td', "bh")[2]
        print(title.text, "20"+date.text)
        print(baseurl+title.get('href').lstrip("."))


# 자연대 물리학과
def physics(soup):
    global baseurl
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        if keyword.find('td') != -1:
            array = keyword.find_all('td')
            title = array[1].a
            date = array[3]
            print(title.text.strip(), date.text.replace(".", "-", 2))
            print(baseurl + title.get('href'))
        keyword = keyword.next_sibling


# 자연대 수학과
def math(soup):
    keyword = soup.find('tr', "notice")

    while keyword is not None:
        if keyword.find('td') != -1:
            array = keyword.find_all('td')
            title = array[1].a
            date = array[3]
            print(title.text.strip(), date.text.replace(".", "-", 2))
            print(title.get('href'))
        keyword = keyword.next_sibling


def it(soup):
    global baseurl
    keyword = soup.find('table', "bbs_list").tbody.tr

    while keyword.next_sibling is not None:
        title = keyword.find('td', "tit").a
        date = keyword.find_all('td')
        num = len(date)
        print(title.text.strip(), date[num-2].text)
        print(baseurl+title.get('href'))
        keyword = keyword.next_sibling


if __name__ == '__main__':
    # 경영대
    cbaurl = "http://cba.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cba(callreq('', cbaurl), "req")

    # 경영대 경제학전공
    economicsurl = "http://economics.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cba(callreq('', economicsurl), "req")

    # 경영대 관광경영학과
    tourismurl = "http://tourism.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # cba(callreq('', tourismurl), "req")

    # 농생대 식품생명공학과
    foodtechurl = "http://foodtech.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', foodtechurl), "req")

    # 농생대 식물자원응용과학전공
    appliedplanturl = "http://appliedplant.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', appliedplanturl), "req")

    # 농생대 응용생물학전공
    applybiourl = "http://applybio.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', applybiourl), "req")

    # 농생대 원예과학전공
    hortiurl = "http://horti.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1"
    # cba(callreq('', hortiurl), "req")

    # 농생대 농업자원경제학과
    ageconurl = "http://agecon.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', ageconurl), "req")

    # 농생대 지역건설공학과
    aedurl = "http://aed.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', aedurl), "req")

    # 문예대 스포츠과학과
    sporturl = ["http://sport.kangwon.ac.kr", "/sub2/04.php"]
    # cba(callreq(sporturl[0], sporturl[1]), "req")

    # 사범대 체육교육과
    phyeduurl = ["http://phyedu.kangwon.ac.kr/", "sub5/03.php"]
    # cba(callreq(phyeduurl[0], phyeduurl[1]), "req")

    # 산림대
    foresturl = "http://forest.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cba(callreq('', foresturl), "req")

    # 산림대 산림자원학전공
    forestryurl = "http://forestry.kangwon.ac.kr/bbs/board.php?bo_table=sub04_01"
    # cba(callreq('', forestryurl), "req")

    # 산림대 산림소재공학전공
    woodurl = "https://wood.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cba(callurl('', woodurl), "url")

    # 산림대 생태조경디자인학과
    landsurl = "http://lands.kangwon.ac.kr/bbs/board.php?bo_table=sub03_01"
    # cba(callreq('', landsurl), "req")

    # 약대
    pharmacyurl = "http://pharmacy.kangwon.ac.kr/bbs/board.php?bo_table=sub02_05"
    # cba(callreq('', pharmacyurl), "req")

    # 의생대
    bmurl = "http://bmcollege.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', bmurl), "req")

    # 의생대 분자생명과학과
    molscienurl = "http://molscien.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', molscienurl), "req")

    # 의생대 생명건강공학과
    biohealthurl = "http://bio-health.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1"
    # cba(callreq('', biohealthurl), "req")

    # 의생대 생물의소재공학과
    bmeurl = "http://bme.kangwon.ac.kr/bbs/board.php?bo_table=sub04_1"
    # cba(callreq('', bmeurl), "req")

    # 의생대 의생명융합학부
    bmcurl = "http://bmc.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cba(callreq('', bmcurl), "req")

    # 의생대 시스템면역과학전공
    siurl = "http://si.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cba(callreq('', siurl), "req")

    # 의생대 의생명공학전공
    dmbturl = "http://dmbt.kangwon.ac.kr/bbs/board.php?bo_table=sub07_1"
    # cba(callreq('', dmbturl), "req")

    # 인문대 영어영문학전공
    englishurl = "http://english.kangwon.ac.kr/new/board/bbs/board.php?bo_table=info"
    # cba(callreq('', englishurl), "req")

    # 인문대 독어독문학전공
    germanurl = "http://german.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # cba(callreq('', germanurl), "req")

    # 인문대 중어중문학전공
    chineseurl = "http://chinese.kangwon.ac.kr/bbs/board.php?bo_table=sub5_1"
    # cba(callreq('', chineseurl), "req")

    # 인문대 사학전공
    histourl = "http://knuhisto.kangwon.ac.kr/bbs/board.php?bo_table=sub7_1"
    # cba(callreq('', histourl), "req")

    # 경영대 경영학전공
    bizurl = ["http://biz.kangwon.ac.kr/", "bbs/board.php?bo_table=sub06_1"]
    # biz(callreq(bizurl[0], bizurl[1]))

    # 산림대 산림환경보호학전공
    fepurl = ["http://fep.kangwon.ac.kr/", "bbs/board.php?bo_table=sub05_1"]
    # biz(callreq(fepurl[0], fepurl[1]))

    # 산림대 제지공학전공
    paperurl = ["https://paper.kangwon.ac.kr/board", "/bbs/board.php?bo_table=sub03_1"]
    # biz(callurl(paperurl[0], paperurl[1]))

    # 수의과
    vetmedurl = ["http://vetmed.kangwon.ac.kr", "/bbs/board.php?bo_table=sub07_1"]
    # biz(callreq(vetmedurl[0], vetmedurl[1]))

    # 농생대 미래농업융합학부
    cllurl = "http://cll.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cll(callreq('', cllurl))

    # 농생대 바이오시스템기계공학전공
    bseurl = "http://bse.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cll(callreq('', bseurl))

    # 동생대
    anilifsciurl = "http://cals.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cll(callreq('', anilifsciurl))

    # 동생대 동물산업융합학과
    animalurl = "http://animal.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cll(callreq('', animalurl))

    # 동생대 동물응용과학과
    applanimalsciurl = "http://applanimalsci.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cll(callreq('', applanimalsciurl))

    # 동생대 동물자원과학과
    aniscienceurl = "http://aniscience.kangwon.ac.kr/bbs/board.php?bo_table=sub05_1"
    # cll(callreq('', aniscienceurl))

    # 산림대 산림경영학전공
    fmurl = "http://fm.kangwon.ac.kr/bbs/board.php?bo_table=sub06_1"
    # cll(callreq('', fmurl))

    # 인문대 일본학전공
    japanurl = "http://www.kw-japan.com/bbs/board.php?bo_table=4_1"
    # cll(callreq('', japanurl))

    # 농생대 바이오자원환경학전공
    dbeurl = ["https://dbe.kangwon.ac.kr/dbe/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(dbeurl[0], dbeurl[1]))

    # 농생대 에코환경과학전공
    ecoenvurl = ["https://ecoenv.kangwon.ac.kr/ecoenv/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(ecoenvurl[0], ecoenvurl[1]))

    # 문예대공대 환경공학전공
    environurl = ["https://environ.kangwon.ac.kr/environ/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(environurl[0], environurl[1]))

    # 문예대공대 메카트로닉스공학전공
    mechaurl = ["https://mecha.kangwon.ac.kr/mecha/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(mechaurl[0], mechaurl[1]))

    # 문예대공대 디자인학과
    designurl = ["https://design.kangwon.ac.kr/design/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(designurl[0], designurl[1]))

    # 문예대공대 영상문화학과
    vcultureurl = ["https://vculture.kangwon.ac.kr/vculture/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(vcultureurl[0], vcultureurl[1]))

    # 간호대
    nurseurl = ["https://nurse.kangwon.ac.kr/nurse/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(nurseurl[0], nurseurl[1]))

    # 자연대 생명과학과
    biologyurl = ["https://biology.kangwon.ac.kr/biology/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(biologyurl[0], biologyurl[1]))

    # 자연대 지구물리학전공
    geophysicsurl = ["https://geophysics.kangwon.ac.kr/geophysics/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(geophysicsurl[0], geophysicsurl[1]))

    # 자연대 지질학전공
    geologyurl = ["https://geology.kangwon.ac.kr/geology/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(geologyurl[0], geologyurl[1]))

    # 자연대 생화학전공
    biochemurl = ["https://biochem.kangwon.ac.kr/biochem/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(biochemurl[0], biochemurl[1]))

    # it대 전기전자공학과
    eeeurl = ["https://eee.kangwon.ac.kr/eee/", "bbs_list.php?code=sub07a&keyvalue=sub07"]
    # dbe(callreq(eeeurl[0], eeeurl[1]))

    # 문예대 건축공학전공공
    archiurl = ["http://archi.kangwon.ac.kr/", "index.php?mp=4_1_1"]
    # archi(callreq(archiurl[0], archiurl[1]))

    # 문예대 토목공학전공
    civilurl = ["http://civil.kangwon.ac.kr/2014/", "index.php?mp=6_1"]
    # archi(callreq(civilurl[0], civilurl[1]))

    # 문예대 기계의용공학전공
    mechanicalurl = ["http://mechanical.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archi(callreq(mechanicalurl[0], mechanicalurl[1]))

    # 문예대 재료공학전공
    materialurl = ["http://material.kangwon.ac.kr/", "index.php?mp=5_1_1"]
    # archi(callreq(materialurl[0], materialurl[1]))

    # 문예대 에너지자원공학전공
    enreurl = ["http://www.enre.kr/", "index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1"]
    # archi(callreq(enreurl[0], enreurl[1]))

    # 문예대 산업공학전공
    smeurl = ["http://sme.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archi(callreq(smeurl[0], smeurl[1]))

    # 문예대 화학공학전공
    chemengurl = ["http://chemeng.kangwon.ac.kr/", "index.php?mt=page&mp=5_1&mm=oxbbs&oxid=1"]
    # archi(callreq(chemengurl[0], chemengurl[1]))

    # 문예대 생물공학전공
    bioengurl = ["http://bioeng.kangwon.ac.kr/", "index.php?mp=6_1_1"]
    # archi(callreq(bioengurl[0], bioengurl[1]))

    # 사범대 역사교육과
    historyurl = ["http://history.kangwon.ac.kr/", "index.php?mp=5_1"]
    # archi(callreq(historyurl[0], historyurl[1]))

    # 사범대 교육학과
    eduurl = ["http://edu.kangwon.ac.kr/new/", "sub04_02.php"]
    # edu(callreq(eduurl[0], eduurl[1]))

    # 사범대 윤리교육과
    ethicseduurl = ["http://ethicsedu.kangwon.ac.kr/new/", "sub06_01.php"]
    # edu(callreq(ethicseduurl[0], ethicseduurl[1]))

    # 사범대 일반사회교육과
    sseduurl = ["http://ssedu.kangwon.ac.kr/new/", "sub02_01.php"]
    # edu(callreq(sseduurl[0], sseduurl[1]))

    # 사범대 한문교육과
    cceduurl = ["http://ccedu.kangwon.ac.kr/new/", "sub03_01.php"]
    # edu(callreq(cceduurl[0], cceduurl[1]))

    # 사범대 과학교육학부
    scieduurl = ["http://sciedu.kangwon.ac.kr/new/", "sub02_01.php"]
    # edu(callreq(scieduurl[0], scieduurl[1]))

    # 사과대
    socialurl = ["http://social.kangwon.ac.kr/bbs/", "zboard.php?id=notice"]
    # social(callreq(socialurl[0], socialurl[1]), "")

    # 사과대 문화인류학과
    anthrourl = ["http://anthro.kangwon.ac.kr/bbs/", "zboard.php?id=bbs41"]
    # social(callreq(anthrourl[0], anthrourl[1]), "")

    # 사과대 부동산학과
    reurl = ["http://re1978.kangwon.ac.kr/bbs/", "zboard.php?id=bbs41"]
    # social(callreq(reurl[0], reurl[1]), "i")

    # 사과대 사회학과
    sociologyurl = ["http://sociology.kangwon.ac.kr/bbs/", "zboard.php?id=notice"]
    # social(callreq(sociologyurl[0], sociologyurl[1]), "i")

    # 사과대 신문방송학과
    masscomurl = ["http://masscom.kangwon.ac.kr", "/bbs/board.php?bo_table=sub3_1"]
    # masscom(callreq(masscomurl[0], masscomurl[1]))

    # 사과대 심리학전공
    psychurl = ["http://psych.kangwon.ac.kr/gnuboard4", "/bbs/board.php?bo_table=bbs41"]
    # masscom(callreq(psychurl[0], psychurl[1]))

    # 인문대 철학전공
    philourl = ["http://kwphilo.kangwon.ac.kr", "/sub05_01.htm"]
    # masscom(callreq(philourl[0], philourl[1]))

    # 자연대 화학전공
    chemisurl = ["http://chemis.kangwon.ac.kr/board", "/bbs/board.php?bo_table=chemis_table04"]
    # masscom(callreq(chemisurl[0], chemisurl[1]))

    # 경영대 정보통계학전공
    statisticsurl = ["http://statistics.kangwon.ac.kr/board", "/bbs/board.php?bo_table=notice_bbs"]
    # masscom(callreq(statisticsurl[0], statisticsurl[1]))

    # 문예대공대
    aceurl = ["http://ace.kangwon.ac.kr/", "index.php?mp=4_1"]
    # it(callreq(aceurl[0], aceurl[1]))

    # it대
    iturl = ["http://it.kangwon.ac.kr/", "index.php?mp=4_1"]
    # it(callreq(iturl[0], iturl[1]))

    # it대 전자공학과
    eeurl = ["http://ee.kangwon.ac.kr/", "index.php?mp=5_1"]
    # it(callreq(eeurl[0], eeurl[1]))

    # it대 컴퓨터공학과
    cseurl = ["https://cse.kangwon.ac.kr/", "index.php?mp=5_1_1"]
    # it(callreq(cseurl[0], cseurl[1]))

    # 경영대 회계학전공
    accounturl = "http://account.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # account(callreq('', accounturl))

    # 경영대 국제무역학과
    itburl = "http://itb.kangwon.ac.kr/bbs/board.php?bo_table=notice"
    # itb(callreq('', itburl))

    # 농생대
    # agrilifesci()

    # 문예대공대 건축학과(5년제)
    architectureurl = ["http://architecture.kangwon.ac.kr/nano/www/board/", "list.php?bid_=comm_notice"]
    # architecture(callreq(architectureurl[0], architectureurl[1]))

    # 문예대공대 미술학과
    arturl = ["http://kangwonart.ac.kr", "/wp/?page_id=1782"]
    # art(callreq(arturl[0], arturl[1]))

    # 사범대
    educatiourl = ["http://educatio.kangwon.ac.kr/", "sub04_01.php"]
    # educatio(callreq(educatiourl[0], educatiourl[1]))

    # 사범대 국어교육과
    keduurl = ["http://kedu.kangwon.ac.kr/", "sub05_01.php"]
    # kedu(callreq(keduurl[0], keduurl[1]))

    # 사범대 영어교육과
    engeduurl = ["http://engedu.kangwon.ac.kr/twb_bbs/", "user_bbs_list.php?bcd=01_06_04_00_00"]
    # engedu(callreq(engeduurl[0], engeduurl[1]))

    # 사범대 지리교육과
    geoeduurl = "http://geoedu.kangwon.ac.kr/department/notice.php"
    # geoedu(callreq(geoeduurl, ''))

    # 사범대 가정교육과
    homecsurl = ["http://homecs.kangwon.ac.kr/bbs/",
                 "zboard.php?id=bbs31&page=1&page_num=20&category=&sn=off&ss=on&sc=on"
                 "&keyword=&prev_no=504&sn1=&divpage=1&select_arrange=headnum&desc=asc"]
    # homecs(callreq(homecsurl[0], homecsurl[1]))

    # 사범대 수학교육과
    matheduurl = ["http://mathedu.kangwon.ac.kr/", "main.php?mt=page&mp=3_1&mm=oxbbs&oxid=2"]
    # mathedu(callreq(matheduurl[0], matheduurl[1]))

    # 사과대 정치외교학과        <- some character error
    politicsurl = ["http://politics.kangwon.ac.kr", "/bbs/board.php?bo_table=sub9_3"]
    # politics(callreq(politicsurl[0], politicsurl[1]))

    # 사과대 행정학전공
    padmurl = "https://padm.kangwon.ac.kr:40592/bbs/board.php?bo_table=sub3_2"
    # padm(callurl('', padmurl))

    # 인문대
    humanitiesurl = "http://humanities.kangwon.ac.kr/sub04_01.php"
    # humanities(callreq('', humanitiesurl))

    # 인문대 국어국문학전공       <- some character error
    koreanurl = ["http://korean.kangwon.ac.kr/2013", "/bbs/board.php?bo_table=sub05_01"]
    # korean(callreq(koreanurl[0], koreanurl[1]))

    # 인문대 불어불문학과
    franceurl = ["http://france.kangwon.ac.kr", "/?doc=bbs/board.php&bo_table=gongi"]
    # france(callreq(franceurl[0], franceurl[1]))

    # 자연대 물리학과
    physicsurl = ["http://physics.bluechips.co.kr", "/sub46"]
    # physics(callreq(physicsurl[0], physicsurl[1]))     # <- find, url 제외 코드 똑같음

    # 자연대 수학과
    mathurl = "http://math.kangwon.ac.kr/xe/notice"
    # math(callreq('', mathurl))        # <- find, url 제외 코드 똑같음
