import requests
from bs4 import BeautifulSoup as bs

# 학사일정
def acaSchedule():
    resp = requests.get('http://www.kangwon.ac.kr/www/selectTnSchafsSchdulListUS.do?sc1=%ED%95%99%EC%82%AC%EC%9D%BC%EC%A0%95&key=156&')
    soup = bs(resp.content, "html.parser")
    keyword = soup.find('tbody', "text_left").tr

    data = []
    while keyword != None:
        tmp = []
        array = keyword.find_all('td')
        date = array[0].text.strip().split(" ~ ")
        if len(date) == 1:
            tmp.append(date[0])
            tmp.append(date[0])
        else:
            tmp.append(date[0])
            tmp.append(date[1])
        tmp.append((array[1].text.strip()))
        data.append(tmp)
        #print(array[0].text.strip()+"\t\t"+ array[1].text.strip())
        keyword = keyword.next_sibling.next_sibling
    return data


if __name__ == '__main__':
    array = acaSchedule()
    print(array)