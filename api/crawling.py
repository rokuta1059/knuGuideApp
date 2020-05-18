import requests
from bs4 import BeautifulSoup


def Computer():
    resp = requests.get("https://cse.kangwon.ac.kr")
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "tab t1 on").ul.li
    baseURL = "https://cse.kangwon.ac.kr/"

    while keyword.next_sibling != None:
        resp = requests.get(baseURL+keyword.a.get('href'))
        soup = BeautifulSoup(resp.content, "html.parser")
        title = soup.find('div', "bbs_view")
        print(title.dl.dt.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling


def Biz():
    resp = requests.get('http://biz.kangwon.ac.kr/main.php')
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('div', "notice_text").find('div', "con").ul.li
    baseURL = "http://biz.kangwon.ac.kr/"

    while keyword.next_sibling.next_sibling != None:
        print(keyword.a.text)
        print(baseURL + keyword.a.get('href'))
        keyword = keyword.next_sibling.next_sibling


if __name__ == '__main__':
   Computer()

