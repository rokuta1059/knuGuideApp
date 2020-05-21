import requests
from bs4 import BeautifulSoup as bs

# 학사일정
def acaSchedule():
    resp = requests.get('http://www.kangwon.ac.kr/www/selectTnSchafsSchdulListUS.do?sc1=%ED%95%99%EC%82%AC%EC%9D%BC%EC%A0%95&key=156&')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tbody', "text_left").tr

    data = []
    while keyword != None:
        array = keyword.find_all('td')
        data.append(array[0].text.strip())
        data.append((array[1].text.strip()))
        print(array[0].text.strip()+"\t\t"+ array[1].text.strip())
        keyword = keyword.next_sibling.next_sibling
    return data


if __name__ == '__main__':
    array = acaSchedule()
    print(array)