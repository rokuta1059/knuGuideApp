import requests
from bs4 import BeautifulSoup

# 필독!!!!!!!!!!!!!!!!!!!!
# return 값은 아래처럼 해주세요
# [['번호', '제목', '작성일자', '링크'], ['번호', '제목', '작성일자', '링크'], .... ]
# 번호 항목은 NOTICE와 같은 값일 경우 '공지'가 될 수 있도록 해주세요

# 컴퓨터공학과 공지사항
def departmentComputerNotice():
    resp = requests.get("https://cse.kangwon.ac.kr")
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "tab t1 on").ul.li
    baseURL = "https://cse.kangwon.ac.kr/"
    # return할 테이블
    data = []

    while keyword.next_sibling != None:
        tmp = []
        resp = requests.get(baseURL+keyword.a.get('href'))
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('div', "bbs_view")
        tmp.append(title.dl.dt.text)
        tmp.append(baseURL + keyword.a.get('href'))
        data.append(tmp)
        keyword = keyword.next_sibling

    return data

# 경영학전공 공지사항
def departmentBizNotice():
    resp = requests.get('http://biz.kangwon.ac.kr/main.php')
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "notice_text").find('div', "con").ul.li
    baseURL = "http://biz.kangwon.ac.kr/"
    data = []

    while keyword.next_sibling.next_sibling != None:
        tmp = []
        tmp.append(keyword.a.text)
        tmp.append(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling

    return data

#학사일정
