from datetime import datetime
import _datetime
from database import crawl_department


# 기숙사식당
def dormcafeteria(soup):
    data = []
    keyword = soup.find('div', id='sub02_text')
    date = keyword.text.split('~')[0].strip()
    for i in range(1, 4):
        # 1번은 재정, 2번은 새롬관, 3번은 이룸관 식단표
        dormtype = 'foodtab'+i.__str__()
        if i == 1:
            dorm = '재정'
        elif i == 2:
            dorm = '새롬관'
        else:
            dorm = '이룸관'
        datevalue = datetime.strptime(date, '%Y-%m-%d')
        keyword = soup.find('div', id=dormtype)
        keyword = keyword.find_all('table')[1]
        keyword = keyword.find_all('tr')
        for j in range(1, len(keyword)):
            datestr = datevalue.year.__str__() + '-' + datevalue.month.__str__() + '-' + datevalue.day.__str__()
            tmp = [datestr, dorm,  keyword[j].th.text]
            datevalue = datevalue + _datetime.timedelta(days=1)
            foodlist = keyword[j].find_all('td')
            for t in range(0, 3):
                tmp.append(foodlist[t].text.replace('\t', '').replace('\r', '').lstrip('\n').rstrip('\n'))
            data.append(tmp)
    return data

# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    crawl_department.crawling_department()
