from bs4 import BeautifulSoup
import urllib.request
from datetime import datetime, date, time

def makeDietTable(dormitory):
    foodtable = []
    weekday = datetime.today().weekday()

    if dormitory == '재정':
        parseDormitory = 'foodtab1'
    elif dormitory == '새롬':
        parseDormitory = 'foodtab2'
    elif dormitory == '이룸':
        parseDormitory = 'foodtab3'

    kangwonURL = 'http://knudorm.kangwon.ac.kr/home/sub02/sub02_05_bj.jsp'
    with urllib.request.urlopen(kangwonURL) as response:
        html = response.read()
        soup = BeautifulSoup(html, 'html.parser')

    dietTable = soup.find('div', {'id': parseDormitory})
    diet1 = dietTable.find_all('table', {'class': 'table_type01'})
    diet2 = diet1[1].find_all('td')

    foodtable.append(diet2[weekday * 3].get_text().replace('\r', '').replace('\t', '').strip())
    foodtable.append(diet2[weekday * 3 + 1].get_text().replace('\r', '').replace('\t', '').strip())
    foodtable.append(diet2[weekday * 3 + 2].get_text().replace('\r', '').replace('\t', '').strip())

    newFoodTable = ['없음' if x == '' else x for x in foodtable]

    return newFoodTable