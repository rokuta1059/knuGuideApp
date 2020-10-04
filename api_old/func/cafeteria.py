from bs4 import BeautifulSoup
import urllib.request
from datetime import datetime, date, time

def make_diet_table(dormitory):
    """
    식단표를 만드는 함수
    - domitory : 식단을 확인할 식당명
    """
    foodtable = []
    weekday = datetime.today().weekday()

    if dormitory == '재정':
        parse_dormitory = 'foodtab1'
    elif dormitory == '새롬':
        parse_dormitory = 'foodtab2'
    elif dormitory == '이룸':
        parse_dormitory = 'foodtab3'

    kangwon_url = 'http://knudorm.kangwon.ac.kr/home/sub02/sub02_05_bj.jsp'
    with urllib.request.urlopen(kangwon_url) as response:
        html = response.read()
        soup = BeautifulSoup(html, 'html.parser')

    diet_table = soup.find('div', {'id': parse_dormitory})
    diet1 = diet_table.find_all('table', {'class': 'table_type01'})
    diet2 = diet1[1].find_all('td')

    foodtable.append(diet2[weekday * 3].get_text().replace('\r', '').replace('\t', '').strip())
    foodtable.append(diet2[weekday * 3 + 1].get_text().replace('\r', '').replace('\t', '').strip())
    foodtable.append(diet2[weekday * 3 + 2].get_text().replace('\r', '').replace('\t', '').strip())

    new_food_table = ['없음' if x == '' else x for x in foodtable]

    return new_food_table
