import requests
from bs4 import BeautifulSoup


# 학사일정
def aca_schedule():
    """
        학사 일정을 받아오는 함수
    """
    resp = requests.get('http://www.kangwon.ac.kr/www/selectTnSchafsSchdulListUS.do?'
                        'sc1=%ED%95%99%EC%82%AC%EC%9D%BC%EC%A0%95&key=156&')
    soup = BeautifulSoup(resp.content, "html.parser")
    keyword = soup.find('tbody', "text_left").tr

    data = []
    id = 0
    while keyword is not None:
        tmp = []
        id += 1
        tmp.append(id)
        arr = keyword.find_all('td')
        date = arr[0].text.strip().split(" ~ ")
        if len(date) == 1:
            tmp.append(date[0])
            tmp.append(date[0])
        else:
            tmp.append(date[0])
            tmp.append(date[1])
        tmp.append((arr[1].text.strip()))
        # print(array[0].text.strip()+"\t\t"+ array[1].text.strip())
        data.append(tmp)
        keyword = keyword.next_sibling.next_sibling

    return data


if __name__ == '__main__':
    array = aca_schedule()
    print(array)