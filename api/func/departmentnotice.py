import requests
from bs4 import BeautifulSoup

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

